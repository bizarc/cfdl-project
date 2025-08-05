# Tests for Stream Allocator functionality - Fixed version

using Test
using Dates
using Random
using CFDLEngine

@testset "Stream Allocator Tests (Fixed)" begin
    
    @testset "StreamAllocation Creation" begin
        allocation = CFDLEngine.StreamAllocation(
            "test_stream",
            1,
            Date(2024, 1, 1),
            Date(2024, 1, 31),
            100000.0,
            1.02,
            102000.0,
            "standard",
            Dict{String, Any}("category" => "Revenue")
        )
        
        @test allocation.stream_id == "test_stream"
        @test allocation.period_number == 1
        @test allocation.period_start == Date(2024, 1, 1)
        @test allocation.period_end == Date(2024, 1, 31)
        @test allocation.base_amount == 100000.0
        @test allocation.growth_factor == 1.02
        @test allocation.adjusted_amount == 102000.0
        @test allocation.allocation_method == "standard"
        @test allocation.metadata["category"] == "Revenue"
    end
    
    @testset "AllocationResult Creation" begin
        allocations = [
            CFDLEngine.StreamAllocation(
                "revenue_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, 1.0, 100000.0, "standard",
                Dict{String, Any}("category" => "Revenue")
            ),
            CFDLEngine.StreamAllocation(
                "expense_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31),
                30000.0, 1.0, 30000.0, "standard", 
                Dict{String, Any}("category" => "Expense")
            )
        ]
        
        total_by_stream = Dict("revenue_stream" => 100000.0, "expense_stream" => 30000.0)
        total_by_period = Dict(1 => 130000.0)
        total_by_category = Dict("Revenue" => 100000.0, "Expense" => 30000.0)
        logic_block_executions = Dict{String, Any}[]
        metadata = Dict{String, Any}("test" => true)
        
        result = CFDLEngine.AllocationResult(
            allocations,
            total_by_stream,
            total_by_period,
            total_by_category,
            logic_block_executions,
            metadata
        )
        
        @test length(result.allocations) == 2
        @test result.total_by_stream["revenue_stream"] == 100000.0
        @test result.total_by_period[1] == 130000.0
        @test result.total_by_category["Revenue"] == 100000.0
        @test result.metadata["test"] == true
    end
    
    @testset "Stream Allocation Integration" begin
        # Create test IR data
        ir_data = CFDLEngine.IRData(
            Dict{String, Any}(
                "id" => "test_deal",
                "currency" => "USD"
            ),
            [Dict{String, Any}("id" => "test_asset")],
            [Dict{String, Any}("id" => "test_component")],
            [
                Dict{String, Any}(
                    "id" => "revenue_stream",
                    "name" => "Revenue Stream",
                    "category" => "Revenue",
                    "subType" => "Operating",
                    "amount" => 100000.0,
                    "growth" => Dict{String, Any}("type" => "fixed", "rate" => 0.02)
                ),
                Dict{String, Any}(
                    "id" => "expense_stream",
                    "name" => "Expense Stream",
                    "category" => "Expense", 
                    "subType" => "Operating",
                    "amount" => 30000.0,
                    "growth" => Dict{String, Any}("type" => "fixed", "rate" => 0.01)
                )
            ],
            Dict{String, Any}("start_date" => "2024-01-01", "end_date" => "2024-03-31", "frequency" => "monthly"),
            Dict{String, Any}(),
            Dict{String, Any}[],
            nothing,
            Dict{String, CFDLEngine.StochasticParameter}()
        )
        
        # Create temporal grid
        periods = [
            CFDLEngine.TimePeriod(Date(2024, 1, 1), Date(2024, 1, 31), 1, "monthly", 21, 31, 0.085),
            CFDLEngine.TimePeriod(Date(2024, 2, 1), Date(2024, 2, 29), 2, "monthly", 20, 29, 0.079),
            CFDLEngine.TimePeriod(Date(2024, 3, 1), Date(2024, 3, 31), 3, "monthly", 21, 31, 0.085)
        ]
        
        grid = CFDLEngine.TemporalGrid(
            periods,
            "monthly",
            Date(2024, 1, 1),
            Date(2024, 3, 31),
            "following",
            "actual/365",
            "US",
            3,
            Dict{String, Any}()
        )
        
        # Test allocation
        sampled_variables = Dict{String, Any}()
        rng = MersenneTwister(12345)
        
        result = allocate_streams(ir_data, grid, sampled_variables, rng)
        
        # Verify results
        @test length(result.allocations) == 6  # 2 streams × 3 periods
        @test length(result.total_by_stream) == 2
        @test length(result.total_by_period) == 3
        @test length(result.total_by_category) == 2
        
        # Check that we have both revenue and expense streams
        @test haskey(result.total_by_category, "Revenue")
        @test haskey(result.total_by_category, "Expense")
        @test result.total_by_category["Revenue"] > 0
        @test result.total_by_category["Expense"] > 0
        
        # Check that allocations have proper growth applied
        revenue_allocations = filter(a -> a.stream_id == "revenue_stream", result.allocations)
        @test length(revenue_allocations) == 3
        
        # First period should have growth factor 1.0 (no growth yet)
        period_1 = first(filter(a -> a.period_number == 1, revenue_allocations))
        @test period_1.growth_factor == 1.0
        @test period_1.adjusted_amount == 100000.0
        
        # Second period should have growth applied
        period_2 = first(filter(a -> a.period_number == 2, revenue_allocations))
        @test period_2.growth_factor > 1.0
        @test period_2.adjusted_amount > 100000.0
    end
    
    @testset "Summarize Allocation Result" begin
        # Create simple allocation result
        allocations = [
            CFDLEngine.StreamAllocation(
                "test_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31),
                50000.0, 1.0, 50000.0, "standard",
                Dict{String, Any}("category" => "Revenue")
            )
        ]
        
        result = CFDLEngine.AllocationResult(
            allocations,
            Dict("test_stream" => 50000.0),
            Dict(1 => 50000.0),
            Dict("Revenue" => 50000.0),
            Dict{String, Any}[],
            Dict{String, Any}()
        )
        
        summary = summarize_allocation_result(result)
        
        @test haskey(summary, "total_allocations")
        @test haskey(summary, "streams")
        @test haskey(summary, "total_amount")
        @test haskey(summary, "categories")
        @test summary["total_allocations"] == 1
        @test summary["streams"] == 1
        @test summary["total_amount"] == 50000.0
        @test summary["categories"]["Revenue"] == 50000.0
    end
end

println("✅ Stream Allocator tests (fixed) completed")