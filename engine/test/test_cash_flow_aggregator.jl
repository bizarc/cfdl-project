# Tests for Cash Flow Aggregator

using Test
using Dates
using CFDLEngine

@testset "Cash Flow Aggregator Tests" begin
    
    # Test data setup
    function create_test_ir_data()
        return IRData(
            Dict{String, Any}(
                "id" => "test_deal_001",
                "name" => "Test Deal",
                "currency" => "USD"
            ),
            [Dict{String, Any}(
                "id" => "test_asset_001",
                "name" => "Test Asset"
            )],
            [Dict{String, Any}(
                "id" => "test_component_001",
                "name" => "Test Component"
            )],
            [
                Dict{String, Any}(
                    "id" => "revenue_stream",
                    "name" => "Revenue Stream",
                    "category" => "Revenue",
                    "subType" => "Operating",
                    "amount" => 100000.0
                ),
                Dict{String, Any}(
                    "id" => "expense_stream",
                    "name" => "Expense Stream", 
                    "category" => "Expense",
                    "subType" => "Operating",
                    "amount" => 30000.0
                )
            ],
            Dict{String, Any}(),  # calendar
            Dict{String, Any}(),  # assumptions
            Dict{String, Any}[],  # logic_blocks (Vector, not Dict)
            nothing,              # waterfall
            Dict{String, CFDLEngine.StochasticParameter}()  # stochastic_params
        )
    end
    
    function create_test_grid()
        start_date = Date(2024, 1, 1)
        end_date = Date(2024, 12, 31)
        periods = [
            CFDLEngine.TimePeriod(Date(2024, i, 1), Date(2024, i, 28), i, "monthly", 20, 28, 0.077)
            for i in 1:12
        ]
        
        return CFDLEngine.TemporalGrid(
            periods,
            "monthly",
            start_date,
            end_date,
            "following",
            "actual/365",
            "US",
            12,
            Dict{String, Any}()
        )
    end
    
    function create_test_allocation_result()
        allocations = CFDLEngine.StreamAllocation[]
        
        # Create 12 months of allocations for 2 streams
        for month in 1:12
            # Revenue stream allocation
            push!(allocations, CFDLEngine.StreamAllocation(
                "revenue_stream",
                month,
                Date(2024, month, 1),
                Date(2024, month, 28),
                100000.0,
                1.0 + (month - 1) * 0.02,  # 2% monthly growth
                100000.0 * (1.0 + (month - 1) * 0.02),
                "standard",
                Dict{String, Any}("category" => "Revenue", "subType" => "Operating")
            ))
            
            # Expense stream allocation
            push!(allocations, CFDLEngine.StreamAllocation(
                "expense_stream",
                month,
                Date(2024, month, 1),
                Date(2024, month, 28),
                30000.0,
                1.0 + (month - 1) * 0.01,  # 1% monthly growth
                30000.0 * (1.0 + (month - 1) * 0.01),
                "standard",
                Dict{String, Any}("category" => "Expense", "subType" => "Operating")
            ))
        end
        
        return CFDLEngine.AllocationResult(
            allocations,
            Dict{String, Float64}(),     # total_by_stream
            Dict{Int, Float64}(),        # total_by_period
            Dict{String, Float64}(),     # total_by_category
            Dict{String, Any}[],         # logic_block_executions
            Dict{String, Any}()          # metadata
        )
    end
    
    @testset "HierarchicalCashFlow Creation" begin
        hierarchical_flow = CFDLEngine.HierarchicalCashFlow(
            "deal_001",
            "asset_001", 
            "component_001",
            "stream_001",
            Date(2024, 1, 1),
            Date(2024, 1, 31),
            100000.0,
            "Revenue",
            "Operating",
            Dict{String, Any}("growth_factor" => 1.02)
        )
        
        @test hierarchical_flow.deal_id == "deal_001"
        @test hierarchical_flow.stream_id == "stream_001"
        @test hierarchical_flow.amount == 100000.0
        @test hierarchical_flow.category == "Revenue"
        @test hierarchical_flow.metadata["growth_factor"] == 1.02
    end
    
    @testset "Build Hierarchical Flows" begin
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        allocation_result = create_test_allocation_result()
        
        hierarchical_flows = CFDLEngine.build_hierarchical_flows(ir_data, allocation_result, grid)
        
        @test length(hierarchical_flows) == 24  # 2 streams × 12 months
        
        # Test first flow
        first_flow = hierarchical_flows[1]
        @test first_flow.deal_id == "test_deal_001"
        @test first_flow.stream_id == "revenue_stream"
        @test first_flow.category == "Revenue"
        @test first_flow.amount == 100000.0
        
        # Test revenue vs expense categorization
        revenue_flows = filter(f -> f.category == "Revenue", hierarchical_flows)
        expense_flows = filter(f -> f.category == "Expense", hierarchical_flows)
        @test length(revenue_flows) == 12
        @test length(expense_flows) == 12
    end
    
    @testset "Aggregate Period Flows" begin
        # Create test flows for a single period
        flows = [
            CFDLEngine.HierarchicalCashFlow(
                "deal_001", "asset_001", "component_001", "revenue_stream",
                Date(2024, 1, 1), Date(2024, 1, 31), 100000.0,
                "Revenue", "Operating", Dict{String, Any}()
            ),
            CFDLEngine.HierarchicalCashFlow(
                "deal_001", "asset_001", "component_001", "expense_stream",
                Date(2024, 1, 1), Date(2024, 1, 31), 30000.0,
                "Expense", "Operating", Dict{String, Any}()
            )
        ]
        
        aggregated = CFDLEngine.aggregate_period_flows(flows, Date(2024, 1, 1), Date(2024, 1, 31))
        
        @test aggregated.period_start == Date(2024, 1, 1)
        @test aggregated.period_end == Date(2024, 1, 31)
        @test aggregated.unlevered_cf == 70000.0  # 100000 - 30000
        @test aggregated.levered_cf == 56000.0    # 70000 * 0.8
        @test length(aggregated.line_items) == 2
        @test length(aggregated.drill_down_data) == 2
        
        # Test line items
        revenue_item = first(item for item in aggregated.line_items if item["category"] == "Revenue")
        expense_item = first(item for item in aggregated.line_items if item["category"] == "Expense")
        @test revenue_item["amount"] == 100000.0
        @test expense_item["amount"] == 30000.0
    end
    
    @testset "Create Monthly View" begin
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        allocation_result = create_test_allocation_result()
        
        hierarchical_flows = CFDLEngine.build_hierarchical_flows(ir_data, allocation_result, grid)
        monthly_view = CFDLEngine.create_monthly_view(hierarchical_flows, grid)
        
        @test length(monthly_view) == 12  # 12 months
        
        # Test first month
        first_month = monthly_view[1]
        @test first_month.period_start == Date(2024, 1, 1)
        @test first_month.unlevered_cf == 70000.0  # 100000 - 30000
        @test length(first_month.line_items) == 2
        
        # Test growth over months
        last_month = monthly_view[12]
        @test last_month.unlevered_cf > first_month.unlevered_cf  # Should be higher due to growth
    end
    
    @testset "Create Annual View" begin
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        allocation_result = create_test_allocation_result()
        
        hierarchical_flows = CFDLEngine.build_hierarchical_flows(ir_data, allocation_result, grid)
        annual_view = CFDLEngine.create_annual_view(hierarchical_flows, grid)
        
        @test length(annual_view) == 1  # 1 year (2024)
        
        # Test annual aggregation
        annual_flow = annual_view[1]
        @test annual_flow.period_start == Date(2024, 1, 1)
        @test annual_flow.period_end == Date(2024, 12, 31)
        @test length(annual_flow.line_items) == 24  # 2 streams × 12 months
        
        # Test that annual total is sum of monthly flows
        monthly_view = CFDLEngine.create_monthly_view(hierarchical_flows, grid)
        monthly_total = sum(m.unlevered_cf for m in monthly_view)
        @test abs(annual_flow.unlevered_cf - monthly_total) < 0.01  # Allow for rounding
    end
    
    @testset "Build Hierarchy Map" begin
        ir_data = create_test_ir_data()
        hierarchy_map = CFDLEngine.build_hierarchy_map(ir_data)
        
        @test haskey(hierarchy_map, "test_deal_001")
        @test "test_asset_001" in hierarchy_map["test_deal_001"]
        @test haskey(hierarchy_map, "test_asset_001")
        @test "test_component_001" in hierarchy_map["test_asset_001"]
        @test haskey(hierarchy_map, "test_component_001")
        @test "revenue_stream" in hierarchy_map["test_component_001"]
        @test "expense_stream" in hierarchy_map["test_component_001"]
    end
    
    @testset "Full Cash Flow Aggregation" begin
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        allocation_result = create_test_allocation_result()
        
        aggregation_result = aggregate_cash_flows(ir_data, allocation_result, grid)
        
        @test aggregation_result.deal_id == "test_deal_001"
        @test aggregation_result.currency == "USD"
        @test length(aggregation_result.monthly_view) == 12
        @test length(aggregation_result.annual_view) == 1
        @test !isempty(aggregation_result.hierarchy_map)
        
        # Test metadata (updated for modular architecture)
        @test haskey(aggregation_result.metadata, "aggregation_timestamp")
        @test aggregation_result.metadata["total_periods_monthly"] == 12
        @test aggregation_result.metadata["total_periods_annual"] == 1
        @test aggregation_result.metadata["pipeline_version"] == "7-stage-modular-industry-standard"
        @test haskey(aggregation_result.metadata, "pipeline_stages")
        @test haskey(aggregation_result.metadata, "stage_counts")
        @test aggregation_result.metadata["modular_architecture"] == true
        
        # Test that all 7 stages are represented
        expected_stages = [
            "stream_collection", "cash_flow_assembly", "operating_statement_generation",
            "financing_adjustments", "tax_processing", "available_cash_calculation", "statement_views"
        ]
        @test aggregation_result.metadata["pipeline_stages"] == expected_stages
        
        # Test stage counts
        stage_counts = aggregation_result.metadata["stage_counts"]
        @test haskey(stage_counts, "cash_flow_entries")
        @test haskey(stage_counts, "operating_statements")
        @test haskey(stage_counts, "financing_adjustments")
        @test haskey(stage_counts, "tax_adjustments")
        @test haskey(stage_counts, "available_cash_calculations")
        
        # Test that statement views are available
        @test haskey(aggregation_result.metadata, "statement_views_available")
    end
    
    @testset "Format Cash Flow Result - Monthly" begin
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        allocation_result = create_test_allocation_result()
        
        aggregation_result = aggregate_cash_flows(ir_data, allocation_result, grid)
        formatted_result = format_cash_flow_result(aggregation_result, MONTHLY)
        
        # Test schema compliance
        @test haskey(formatted_result, "dealId")
        @test haskey(formatted_result, "currency")
        @test haskey(formatted_result, "frequency")
        @test haskey(formatted_result, "entries")
        
        @test formatted_result["dealId"] == "test_deal_001"
        @test formatted_result["currency"] == "USD"
        @test formatted_result["frequency"] == "monthly"
        @test length(formatted_result["entries"]) == 12
        
        # Test entry format
        first_entry = formatted_result["entries"][1]
        @test haskey(first_entry, "periodStart")
        @test haskey(first_entry, "periodEnd")
        @test haskey(first_entry, "lineItems")
        @test haskey(first_entry, "unleveredCF")
        @test haskey(first_entry, "leveredCF")
        
        # Test line item format
        first_line_item = first_entry["lineItems"][1]
        @test haskey(first_line_item, "streamId")
        @test haskey(first_line_item, "amount")
        @test haskey(first_line_item, "category")
        @test haskey(first_line_item, "subType")
    end
    
    @testset "Format Cash Flow Result - Annual" begin
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        allocation_result = create_test_allocation_result()
        
        aggregation_result = aggregate_cash_flows(ir_data, allocation_result, grid)
        formatted_result = format_cash_flow_result(aggregation_result, ANNUAL)
        
        @test formatted_result["frequency"] == "annual"
        @test length(formatted_result["entries"]) == 1
        
        # Test that annual entry has all monthly line items
        annual_entry = formatted_result["entries"][1]
        @test length(annual_entry["lineItems"]) == 24  # 2 streams × 12 months
    end
    
    @testset "Summarize Aggregation Result" begin
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        allocation_result = create_test_allocation_result()
        
        aggregation_result = aggregate_cash_flows(ir_data, allocation_result, grid)
        summary = summarize_aggregation_result(aggregation_result)
        
        @test summary["deal_id"] == "test_deal_001"
        @test summary["currency"] == "USD"
        @test summary["monthly_periods"] == 12
        @test summary["annual_periods"] == 1
        @test summary["drill_down_available"] == true
        @test summary["total_unlevered_cf_monthly"] > 0
        @test summary["total_unlevered_cf_annual"] > 0
        
        # Monthly and annual totals should be equal for single year
        @test abs(summary["total_unlevered_cf_monthly"] - summary["total_unlevered_cf_annual"]) < 0.01
    end
    
    @testset "Helper Functions" begin
        grid = create_test_grid()
        
        # Test get_period_by_number
        period = CFDLEngine.get_period_by_number(grid, 1)
        @test period !== nothing
        @test period.period_number == 1
        
        period_not_found = CFDLEngine.get_period_by_number(grid, 999)
        @test period_not_found === nothing
        
        # Test find_stream_by_id
        streams = [
            Dict{String, Any}("id" => "stream1", "name" => "Stream 1"),
            Dict{String, Any}("id" => "stream2", "name" => "Stream 2")
        ]
        
        found_stream = CFDLEngine.find_stream_by_id(streams, "stream1")
        @test found_stream !== nothing
        @test found_stream["name"] == "Stream 1"
        
        not_found_stream = CFDLEngine.find_stream_by_id(streams, "nonexistent")
        @test not_found_stream === nothing
        
        # Test entity helper functions
        ir_data = create_test_ir_data()
        asset_id = CFDLEngine.get_asset_for_stream(ir_data, "revenue_stream")
        @test asset_id == "test_asset_001"
        
        component_id = CFDLEngine.get_component_for_stream(ir_data, "revenue_stream")
        @test component_id == "test_component_001"
    end
    
    @testset "Edge Cases" begin
        # Test with empty allocation result
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        empty_allocation = CFDLEngine.AllocationResult(
            CFDLEngine.StreamAllocation[], 
            Dict{String, Float64}(), 
            Dict{Int, Float64}(), 
            Dict{String, Float64}(), 
            Dict{String, Any}[], 
            Dict{String, Any}()
        )
        
        aggregation_result = aggregate_cash_flows(ir_data, empty_allocation, grid)
        @test length(aggregation_result.monthly_view) == 0
        @test length(aggregation_result.annual_view) == 0
        
        # Test with single allocation
        single_allocation = CFDLEngine.AllocationResult([
            CFDLEngine.StreamAllocation(
                "revenue_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, 1.0, 100000.0, "standard",
                Dict{String, Any}("category" => "Revenue")
            )
        ], 
        Dict{String, Float64}(), 
        Dict{Int, Float64}(), 
        Dict{String, Float64}(), 
        Dict{String, Any}[], 
        Dict{String, Any}())
        
        single_result = aggregate_cash_flows(ir_data, single_allocation, grid)
        @test length(single_result.monthly_view) == 1
        @test length(single_result.annual_view) == 1
        @test single_result.monthly_view[1].unlevered_cf == 100000.0
    end
    
    @testset "Multi-Year Aggregation" begin
        # Create multi-year test data
        multi_year_allocations = CFDLEngine.StreamAllocation[]
        
        for year in 2024:2025
            for month in 1:12
                push!(multi_year_allocations, CFDLEngine.StreamAllocation(
                    "revenue_stream",
                    (year - 2024) * 12 + month,
                    Date(year, month, 1),
                    Date(year, month, 28),
                    100000.0,
                    1.0,
                    100000.0,
                    "standard",
                    Dict{String, Any}("category" => "Revenue")
                ))
            end
        end
        
        multi_year_allocation = CFDLEngine.AllocationResult(
            multi_year_allocations, 
            Dict{String, Float64}(), 
            Dict{Int, Float64}(), 
            Dict{String, Float64}(), 
            Dict{String, Any}[], 
            Dict{String, Any}()
        )
        ir_data = create_test_ir_data()
        grid = create_test_grid()  # Note: grid is still single year, but allocations span multiple years
        
        hierarchical_flows = CFDLEngine.build_hierarchical_flows(ir_data, multi_year_allocation, grid)
        annual_view = CFDLEngine.create_annual_view(hierarchical_flows, grid)
        
        @test length(annual_view) == 2  # 2024 and 2025
        @test annual_view[1].period_start == Date(2024, 1, 1)
        @test annual_view[2].period_start == Date(2025, 1, 1)
    end
end

println("✅ Cash Flow Aggregator tests completed")