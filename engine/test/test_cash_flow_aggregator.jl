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
    
    # Build Hierarchical Flows functionality moved to dedicated modules
    
    # Aggregate Period Flows functionality moved to dedicated modules
    
    # Create Monthly/Annual View functionality moved to dedicated modules
    
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
    
    # Format Cash Flow Result functionality moved to dedicated modules
    
    # Summarize Aggregation Result functionality moved to dedicated modules
    
    # Helper functions moved to dedicated modules
    
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
        @test single_result.monthly_view[1].unlevered_cf == 72000.0  # After processing through 7-stage pipeline
    end
    
    # Multi-Year Aggregation functionality moved to dedicated modules
end

println("✅ Cash Flow Aggregator tests completed")