# Tests for Cash Flow Assembly Module (Stage 2)

using Test
using Dates
using CFDLEngine

@testset "Cash Flow Assembly Tests" begin
    
    # Test data setup
    function create_test_grouped_streams()
        # Create mock monthly groups
        allocations = [
            CFDLEngine.StreamAllocation(
                "revenue_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, 1.0, 100000.0, "standard",
                Dict{String, Any}("category" => "Revenue", "subType" => "Operating")
            ),
            CFDLEngine.StreamAllocation(
                "expense_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31),
                30000.0, 1.0, 30000.0, "standard",
                Dict{String, Any}("category" => "Expense", "subType" => "Operating")
            )
        ]
        
        # Convert allocations to stream dictionaries for StreamGroup
        streams = [Dict{String, Any}(
            "stream_id" => alloc.stream_id,
            "amount" => alloc.adjusted_amount,
            "growth_factor" => alloc.growth_factor,
            "allocation_method" => alloc.allocation_method,
            "metadata" => alloc.metadata
        ) for alloc in allocations]
        
        monthly_group = CFDLEngine.StreamGroup(
            "monthly_2024_01", "monthly", Date(2024, 1, 1), Date(2024, 1, 31),
            streams, 130000.0, Dict{String, Any}()
        )
        
        temporal_groups = CFDLEngine.TemporalStreamGroups(
            [monthly_group], [], [], Dict{String, Any}()
        )
        
        hierarchical_groups = CFDLEngine.HierarchicalStreamGroups(
            [], [], [], Dict{String, Any}()
        )
        
        return CFDLEngine.GroupedStreams(
            hierarchical_groups, temporal_groups, 2, 130000.0,
            (Date(2024, 1, 1), Date(2024, 1, 31))
        )
    end
    
    function create_test_ir_data()
        return CFDLEngine.IRData(
            Dict{String, Any}("id" => "test_deal", "currency" => "USD"),
            [Dict{String, Any}("id" => "test_asset")],
            [Dict{String, Any}("id" => "test_component")],
            [
                Dict{String, Any}(
                    "id" => "revenue_stream", "name" => "Revenue Stream",
                    "category" => "Revenue", "subType" => "Operating"
                ),
                Dict{String, Any}(
                    "id" => "expense_stream", "name" => "Expense Stream",
                    "category" => "Expense", "subType" => "Operating"
                )
            ],
            Dict{String, Any}(), Dict{String, Any}(), Dict{String, Any}[],
            nothing, Dict{String, CFDLEngine.StochasticParameter}()
        )
    end
    
    function create_test_grid()
        periods = [CFDLEngine.TimePeriod(Date(2024, 1, 1), Date(2024, 1, 31), 1, "monthly", 20, 31, 0.083)]
        return CFDLEngine.TemporalGrid(
            periods, "monthly", Date(2024, 1, 1), Date(2024, 1, 31),
            "following", "actual/365", "US", 1, Dict{String, Any}()
        )
    end
    
    @testset "CashFlowCategory Enum" begin
        @test OPERATING isa CashFlowCategory
        @test FINANCING isa CashFlowCategory
        @test INVESTING isa CashFlowCategory
        @test TAX_RELATED isa CashFlowCategory
    end
    
    @testset "CashFlowEntry Creation" begin
        entry = CashFlowEntry(
            "test_deal", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("revenue" => 100000.0), Dict("interest" => 5000.0),
            Dict("capex" => 10000.0), Dict("tax" => 2000.0),
            Dict("test" => true)
        )
        
        @test entry.entity_id == "test_deal"
        @test entry.entity_type == "deal"
        @test entry.operating_items["revenue"] == 100000.0
        @test entry.financing_items["interest"] == 5000.0
        @test entry.investing_items["capex"] == 10000.0
        @test entry.tax_items["tax"] == 2000.0
        @test entry.metadata["test"] == true
    end
    
    @testset "Stream Categorization" begin
        # Test operating stream
        operating_stream = Dict{String, Any}(
            "category" => "Revenue", "subType" => "Operating"
        )
        @test categorize_stream(operating_stream) == OPERATING
        
        # Test financing stream
        financing_stream = Dict{String, Any}(
            "category" => "Other", "subType" => "Debt"
        )
        @test categorize_stream(financing_stream) == FINANCING
        
        # Test investing stream
        investing_stream = Dict{String, Any}(
            "category" => "Other", "subType" => "CapEx"
        )
        @test categorize_stream(investing_stream) == INVESTING
        
        # Test tax stream
        tax_stream = Dict{String, Any}(
            "category" => "Other", "subType" => "Tax"
        )
        @test categorize_stream(tax_stream) == TAX_RELATED
        
        # Test default categorization
        default_stream = Dict{String, Any}(
            "category" => "Revenue", "subType" => "Unknown"
        )
        @test categorize_stream(default_stream) == OPERATING
    end
    
    @testset "Cash Flow Assembly" begin
        grouped_streams = create_test_grouped_streams()
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        
        cash_flow_entries = assemble_cash_flows(grouped_streams, ir_data, grid)
        
        @test length(cash_flow_entries) == 1
        
        entry = cash_flow_entries[1]
        @test entry.entity_id == "test_deal"
        @test entry.entity_type == "deal"
        @test entry.period_start == Date(2024, 1, 1)
        @test entry.period_end == Date(2024, 1, 31)
        
        # Check operating items (both revenue and expense should be categorized as operating)
        @test haskey(entry.operating_items, "Revenue Stream")
        @test haskey(entry.operating_items, "Expense Stream")
        @test entry.operating_items["Revenue Stream"] == 100000.0
        @test entry.operating_items["Expense Stream"] == 30000.0
        
        # Check metadata
        @test haskey(entry.metadata, "group_id")
        @test haskey(entry.metadata, "allocation_count")
        @test entry.metadata["allocation_count"] == 2
        @test entry.metadata["stage"] == "cash_flow_assembly"
    end
    
    @testset "Cash Flow Categorization" begin
        # Create test entries
        entries = [
            CashFlowEntry(
                "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict("revenue" => 100000.0), Dict{String, Float64}(),
                Dict{String, Float64}(), Dict{String, Float64}(),
                Dict{String, Any}()
            ),
            CashFlowEntry(
                "deal2", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict{String, Float64}(), Dict("debt" => 50000.0),
                Dict{String, Float64}(), Dict{String, Float64}(),
                Dict{String, Any}()
            )
        ]
        
        categorized = categorize_cash_flows(entries)
        
        @test haskey(categorized, "operating")
        @test haskey(categorized, "financing")
        @test haskey(categorized, "investing")
        @test haskey(categorized, "tax")
        
        @test length(categorized["operating"]) == 1
        @test length(categorized["financing"]) == 1
        @test length(categorized["investing"]) == 0
        @test length(categorized["tax"]) == 0
        
        @test categorized["operating"][1].entity_id == "deal1"
        @test categorized["financing"][1].entity_id == "deal2"
    end
    
    @testset "Cash Flow Summary" begin
        entries = [
            CashFlowEntry(
                "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict("revenue" => 100000.0), Dict("debt" => 20000.0),
                Dict("capex" => 5000.0), Dict("tax" => 1000.0),
                Dict{String, Any}()
            ),
            CashFlowEntry(
                "deal1", "deal", Date(2024, 2, 1), Date(2024, 2, 28),
                Dict("revenue" => 105000.0), Dict("debt" => 20000.0),
                Dict("capex" => 3000.0), Dict("tax" => 1200.0),
                Dict{String, Any}()
            )
        ]
        
        summary = summarize_cash_flow_entries(entries)
        
        @test summary["total_entries"] == 2
        @test summary["cash_flow_totals"]["operating"] == 205000.0
        @test summary["cash_flow_totals"]["financing"] == 40000.0
        @test summary["cash_flow_totals"]["investing"] == 8000.0
        @test summary["cash_flow_totals"]["tax"] == 2200.0
        @test summary["cash_flow_totals"]["net_total"] == 255200.0
        @test summary["entities_covered"] == 1
        @test summary["stage_completed"] == "cash_flow_assembly"
        
        # Check period range
        period_range = summary["period_range"]
        @test period_range[1] == Date(2024, 1, 1)
        @test period_range[2] == Date(2024, 2, 28)
    end
    
    @testset "Helper Functions" begin
        # Test find_stream_by_id
        streams = [
            Dict{String, Any}("id" => "stream1", "name" => "Stream 1"),
            Dict{String, Any}("id" => "stream2", "name" => "Stream 2")
        ]
        
        found = find_stream_by_id(streams, "stream1")
        @test found !== nothing
        @test found["name"] == "Stream 1"
        
        not_found = find_stream_by_id(streams, "nonexistent")
        @test not_found === nothing
    end
    
    @testset "Edge Cases" begin
        # Test with empty grouped streams
        empty_temporal = CFDLEngine.TemporalStreamGroups([], [], [], Dict{String, Any}())
        empty_hierarchical = CFDLEngine.HierarchicalStreamGroups([], [], [], Dict{String, Any}())
        empty_grouped = CFDLEngine.GroupedStreams(
            empty_hierarchical, empty_temporal, 0, 0.0, (Date(2024, 1, 1), Date(2024, 1, 1))
        )
        
        ir_data = create_test_ir_data()
        grid = create_test_grid()
        
        empty_entries = assemble_cash_flows(empty_grouped, ir_data, grid)
        @test length(empty_entries) == 0
        
        # Test summary with empty entries
        empty_summary = summarize_cash_flow_entries(CashFlowEntry[])
        @test haskey(empty_summary, "message")
        @test empty_summary["message"] == "No cash flow entries to summarize"
    end
    
    @testset "Stream Categorization Edge Cases" begin
        # Test stream with missing fields
        empty_stream = Dict{String, Any}()
        @test categorize_stream(empty_stream) == OPERATING  # Default fallback
        
        # Test various financing subtypes
        financing_subtypes = ["Financing", "Debt", "Interest", "Principal", "PreferredReturn"]
        for subtype in financing_subtypes
            stream = Dict{String, Any}("subType" => subtype)
            @test categorize_stream(stream) == FINANCING
        end
        
        # Test various investing subtypes
        investing_subtypes = ["Investing", "CapEx", "Acquisition", "Disposition", "CapitalImprovement"]
        for subtype in investing_subtypes
            stream = Dict{String, Any}("subType" => subtype)
            @test categorize_stream(stream) == INVESTING
        end
        
        # Test various tax subtypes
        tax_subtypes = ["Tax", "IncomeTax", "PropertyTax", "TaxCredit"]
        for subtype in tax_subtypes
            stream = Dict{String, Any}("subType" => subtype)
            @test categorize_stream(stream) == TAX_RELATED
        end
    end
end

println("✅ Cash Flow Assembly tests completed")