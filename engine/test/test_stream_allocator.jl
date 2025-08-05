# Tests for Stream Allocator functionality

using Test
using Dates
using Random
using CFDLEngine

@testset "Stream Allocator Tests" begin
    
    @testset "Stream Allocation Creation" begin
        allocation = StreamAllocation(
            "test_stream",
            1,
            Date(2024, 1, 1),
            Date(2024, 1, 31),
            100000.0,
            1.05,
            105000.0,
            "standard",
            Dict("category" => "Revenue")
        )
        
        @test allocation.stream_id == "test_stream"
        @test allocation.period_number == 1
        @test allocation.base_amount == 100000.0
        @test allocation.growth_factor == 1.05
        @test allocation.adjusted_amount == 105000.0
        @test allocation.allocation_method == "standard"
        @test allocation.metadata["category"] == "Revenue"
    end
    
    @testset "Base Amount Extraction" begin
        # Test fixed amount
        stream_fixed = Dict("amount" => 50000.0)
        amount = get_stream_base_amount(stream_fixed, Dict{String, Any}())
        @test amount == 50000.0
        
        # Test calculator (placeholder)
        stream_calc = Dict("amount" => Dict("type" => "loanPayment", "principal" => 1000000))
        amount_calc = get_stream_base_amount(stream_calc, Dict{String, Any}())
        @test amount_calc == 100000.0  # Placeholder value
        
        # Test missing amount
        stream_empty = Dict{String, Any}()
        amount_empty = get_stream_base_amount(stream_empty, Dict{String, Any}())
        @test amount_empty == 100000.0  # Default value
    end
    
    @testset "Growth Factor Calculation" begin
        rng = MersenneTwister(12345)
        
        # Test no growth
        stream_no_growth = Dict{String, Any}()
        factor = calculate_growth_factor(stream_no_growth, 1, Dict{String, Any}(), rng)
        @test factor == 1.0
        
        # Test fixed growth
        stream_fixed_growth = Dict(
            "growth" => Dict(
                "type" => "fixed",
                "rate" => 0.03
            )
        )
        factor1 = calculate_growth_factor(stream_fixed_growth, 1, Dict{String, Any}(), rng)
        factor2 = calculate_growth_factor(stream_fixed_growth, 2, Dict{String, Any}(), rng)
        factor3 = calculate_growth_factor(stream_fixed_growth, 3, Dict{String, Any}(), rng)
        
        @test factor1 ≈ 1.0
        @test factor2 ≈ 1.03
        @test factor3 ≈ 1.0609  # 1.03^2
        
        # Test distribution growth
        stream_dist_growth = Dict(
            "growth" => Dict(
                "type" => "distribution",
                "distribution" => Dict(
                    "type" => "Normal",
                    "mean" => 0.05,
                    "std" => 0.01
                )
            )
        )
        factor_dist = calculate_growth_factor(stream_dist_growth, 1, Dict{String, Any}(), rng)
        @test factor_dist > 1.0  # Should be around 1.05
        @test factor_dist < 1.1   # Reasonable bounds
    end
    
    @testset "Growth Distribution Sampling" begin
        rng = MersenneTwister(12345)
        
        # Test Normal distribution
        params_normal = Dict(
            "type" => "Normal",
            "mean" => 0.03,
            "std" => 0.01
        )
        factor = sample_growth_distribution(params_normal, rng)
        @test factor > 0.99  # Should be around 1.03
        @test factor < 1.07
        
        # Test Uniform distribution
        params_uniform = Dict(
            "type" => "Uniform",
            "min" => 0.02,
            "max" => 0.06
        )
        factor_uniform = sample_growth_distribution(params_uniform, rng)
        @test factor_uniform >= 1.02
        @test factor_uniform <= 1.06
        
        # Test unknown distribution
        params_unknown = Dict("type" => "Unknown")
        factor_unknown = sample_growth_distribution(params_unknown, rng)
        @test factor_unknown == 1.0
    end
    
    @testset "Applicable Periods" begin
        # Create a simple temporal grid
        periods = [
            TimePeriod(Date(2024, 1, 1), Date(2024, 1, 31), 1, "monthly", 22, 31, 31/365),
            TimePeriod(Date(2024, 2, 1), Date(2024, 2, 29), 2, "monthly", 21, 29, 29/365),
            TimePeriod(Date(2024, 3, 1), Date(2024, 3, 31), 3, "monthly", 21, 31, 31/365)
        ]
        
        grid = TemporalGrid(
            periods, "monthly", Date(2024, 1, 1), Date(2024, 3, 31),
            "following", "actual/365", "US", 3, Dict{String, Any}()
        )
        
        # Test stream with no schedule (should apply to all periods)
        stream_no_schedule = Dict{String, Any}()
        applicable = get_applicable_periods(stream_no_schedule, grid)
        @test length(applicable) == 3
        @test applicable == grid.periods
        
        # Test stream with empty schedule
        stream_empty_schedule = Dict("schedule" => Dict{String, Any}())
        applicable_empty = get_applicable_periods(stream_empty_schedule, grid)
        @test length(applicable_empty) == 3
    end
    
    @testset "Single Stream Allocation" begin
        # Create test data
        periods = [
            TimePeriod(Date(2024, 1, 1), Date(2024, 1, 31), 1, "monthly", 22, 31, 31/365),
            TimePeriod(Date(2024, 2, 1), Date(2024, 2, 29), 2, "monthly", 21, 29, 29/365)
        ]
        
        grid = TemporalGrid(
            periods, "monthly", Date(2024, 1, 1), Date(2024, 2, 29),
            "following", "actual/365", "US", 2, Dict{String, Any}()
        )
        
        stream = Dict(
            "id" => "rental_income",
            "amount" => 10000.0,
            "category" => "Revenue",
            "subType" => "Operating",
            "growth" => Dict(
                "type" => "fixed",
                "rate" => 0.02
            )
        )
        
        rng = MersenneTwister(12345)
        sampled_vars = Dict{String, Any}()
        
        allocations = allocate_single_stream(stream, grid, sampled_vars, rng)
        
        @test length(allocations) == 2
        @test allocations[1].stream_id == "rental_income"
        @test allocations[1].base_amount == 10000.0
        @test allocations[1].growth_factor == 1.0  # First period
        @test allocations[1].adjusted_amount == 10000.0
        
        @test allocations[2].growth_factor ≈ 1.02  # Second period
        @test allocations[2].adjusted_amount ≈ 10200.0
        
        @test allocations[1].metadata["category"] == "Revenue"
        @test allocations[2].metadata["subType"] == "Operating"
    end
    
    @testset "Aggregation Calculations" begin
        allocations = [
            StreamAllocation("stream1", 1, Date(2024, 1, 1), Date(2024, 1, 31), 
                           1000.0, 1.0, 1000.0, "standard", Dict("category" => "Revenue")),
            StreamAllocation("stream1", 2, Date(2024, 2, 1), Date(2024, 2, 29), 
                           1000.0, 1.05, 1050.0, "standard", Dict("category" => "Revenue")),
            StreamAllocation("stream2", 1, Date(2024, 1, 1), Date(2024, 1, 31), 
                           500.0, 1.0, 500.0, "standard", Dict("category" => "Expense")),
            StreamAllocation("stream2", 2, Date(2024, 2, 1), Date(2024, 2, 29), 
                           500.0, 1.02, 510.0, "standard", Dict("category" => "Expense"))
        ]
        
        # Test stream totals
        stream_totals = calculate_stream_totals(allocations)
        @test stream_totals["stream1"] == 2050.0
        @test stream_totals["stream2"] == 1010.0
        
        # Test period totals
        period_totals = calculate_period_totals(allocations)
        @test period_totals[1] == 1500.0
        @test period_totals[2] == 1560.0
        
        # Test category totals
        streams = [
            Dict("id" => "stream1", "category" => "Revenue"),
            Dict("id" => "stream2", "category" => "Expense")
        ]
        category_totals = calculate_category_totals(allocations, streams)
        @test category_totals["Revenue"] == 2050.0
        @test category_totals["Expense"] == 1010.0
    end
    
    @testset "Logic Block Context Creation" begin
        allocations = [
            StreamAllocation("test", 1, Date(2024, 1, 1), Date(2024, 1, 31), 
                           1000.0, 1.0, 1000.0, "standard", Dict{String, Any}())
        ]
        sampled_vars = Dict("rent_growth" => 0.03)
        rng = MersenneTwister(12345)
        
        context = create_logic_block_context(allocations, sampled_vars, rng)
        
        @test haskey(context, "allocations")
        @test haskey(context, "variables")
        @test haskey(context, "rng") 
        @test haskey(context, "current_period")
        @test haskey(context, "helper_functions")
        
        @test context["variables"]["rent_growth"] == 0.03
        @test context["current_period"] == 1
    end
    
    @testset "Rent Growth Override Logic Block" begin
        allocations = [
            StreamAllocation("revenue_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31), 
                           1000.0, 1.0, 1000.0, "standard", Dict("category" => "Revenue")),
            StreamAllocation("expense_stream", 1, Date(2024, 1, 1), Date(2024, 1, 31), 
                           500.0, 1.0, 500.0, "standard", Dict("category" => "Expense"))
        ]
        
        context = Dict{String, Any}(
            "variables" => Dict("rent_growth_rate" => 0.05)
        )
        
        # Apply the override
        apply_rent_growth_override!(allocations, context)
        
        # Revenue stream should be modified
        @test allocations[1].adjusted_amount == 1050.0  # 1000 * (1 + 0.05 * 0.1)
        @test allocations[1].allocation_method == "logic_block_override"
        @test allocations[1].metadata["logic_block_applied"] == true
        
        # Expense stream should be unchanged
        @test allocations[2].adjusted_amount == 500.0
        @test allocations[2].allocation_method == "standard"
    end
    
    @testset "Full Stream Allocation Integration" begin
        # Create test IR data
        test_ir = IRData(
            Dict("id" => "test_deal", "currency" => "USD"),
            [],
            [],
            [Dict(
                "id" => "rental_stream",
                "amount" => 5000.0,
                "category" => "Revenue",
                "subType" => "Operating",
                "growth" => Dict(
                    "type" => "fixed",
                    "rate" => 0.03
                )
            )],
            Dict("frequency" => "monthly"),
            Dict(),
            [],
            nothing,
            Dict{String, StochasticParameter}()
        )
        
        # Create temporal grid
        periods = [
            TimePeriod(Date(2024, 1, 1), Date(2024, 1, 31), 1, "monthly", 22, 31, 31/365),
            TimePeriod(Date(2024, 2, 1), Date(2024, 2, 29), 2, "monthly", 21, 29, 29/365)
        ]
        
        grid = TemporalGrid(
            periods, "monthly", Date(2024, 1, 1), Date(2024, 2, 29),
            "following", "actual/365", "US", 2, Dict{String, Any}()
        )
        
        rng = MersenneTwister(12345)
        sampled_vars = Dict{String, Any}()
        
        # Run allocation
        result = allocate_streams(test_ir, grid, sampled_vars, rng)
        
        @test length(result.allocations) == 2  # 1 stream × 2 periods
        @test length(result.total_by_stream) == 1
        @test haskey(result.total_by_stream, "rental_stream")
        @test result.total_by_stream["rental_stream"] ≈ 10150.0  # 5000 + 5150
        
        @test length(result.total_by_period) == 2
        @test result.total_by_period[1] == 5000.0
        @test result.total_by_period[2] ≈ 5150.0
        
        @test haskey(result.total_by_category, "Revenue")
        @test result.total_by_category["Revenue"] ≈ 10150.0
        
        # Test summary
        summary = summarize_allocation_result(result)
        @test summary["total_allocations"] == 2
        @test summary["total_amount"] == 10150.0
        @test summary["streams"] == 1
        @test summary["periods"] == 2
        @test haskey(summary["categories"], "Revenue")
    end
    
    @testset "Logic Block Execution" begin
        # Create test allocations
        allocations = [
            StreamAllocation("revenue", 1, Date(2024, 1, 1), Date(2024, 1, 31), 
                           1000.0, 1.0, 1000.0, "standard", Dict("category" => "Revenue"))
        ]
        
        # Create test logic block
        logic_blocks = [Dict(
            "id" => "rent_growth_block",
            "type" => "calculation",
            "language" => "julia",
            "code" => "# Apply rent growth adjustment",
            "executionOrder" => 1
        )]
        
        sampled_vars = Dict("rent_growth_rate" => 0.04)
        executions = Dict{String, Any}[]
        rng = MersenneTwister(12345)
        
        # Execute logic blocks
        execute_logic_blocks!(allocations, logic_blocks, sampled_vars, executions, rng)
        
        @test length(executions) == 1
        @test executions[1]["block_id"] == "rent_growth_block"
        @test executions[1]["status"] == "success"
        @test haskey(executions[1], "execution_time")
        @test haskey(executions[1], "timestamp")
        
        # Revenue allocation should be modified by the rent growth logic
        @test allocations[1].adjusted_amount == 1040.0  # 1000 * (1 + 0.04 * 0.1)
        @test allocations[1].allocation_method == "logic_block_override"
    end
end