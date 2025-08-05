# Tests for Monte Carlo functionality

using Test
using JSON3
using Random
using Statistics

# Import the CFDLEngine module
using CFDLEngine

@testset "Monte Carlo Engine Tests" begin
    
    @testset "IR Loading and Validation" begin
        # Create test IR data
        test_ir = Dict(
            "deal" => Dict(
                "id" => "test_deal",
                "name" => "Test Deal",
                "currency" => "USD"
            ),
            "calendar" => Dict(
                "frequency" => "monthly"
            ),
            "assets" => [],
            "components" => [],
            "streams" => [],
            "assumptions" => Dict(
                "rent_growth" => Dict(
                    "type" => "Normal",
                    "mean" => 0.03,
                    "std" => 0.01
                ),
                "interest_rate" => 0.05
            ),
            "logic_blocks" => [],
            "waterfall" => nothing
        )
        
        # Write test IR to temporary file
        temp_file = tempname()
        write(temp_file, JSON3.write(test_ir))
        
        # Test loading
        ir_data = load_ir(temp_file)
        @test ir_data.deal["id"] == "test_deal"
        @test haskey(ir_data.stochastic_params, "rent_growth")
        @test haskey(ir_data.stochastic_params, "interest_rate")
        
        # Test parameter parsing
        rent_growth_param = ir_data.stochastic_params["rent_growth"]
        @test isa(rent_growth_param, DistributionParameter)
        @test rent_growth_param.distribution_type == "Normal"
        @test rent_growth_param.parameters["mean"] == 0.03
        
        interest_rate_param = ir_data.stochastic_params["interest_rate"]
        @test isa(interest_rate_param, FixedParameter)
        @test interest_rate_param.value == 0.05
        
        # Clean up
        rm(temp_file)
    end
    
    @testset "Parameter Sampling" begin
        # Test fixed parameter
        fixed_param = FixedParameter(42.0)
        rng = MersenneTwister(123)
        @test sample_parameter(fixed_param, rng) == 42.0
        
        # Test normal distribution parameter
        normal_param = DistributionParameter("Normal", Dict("mean" => 10.0, "std" => 2.0))
        samples = [sample_parameter(normal_param, rng) for _ in 1:1000]
        @test abs(mean(samples) - 10.0) < 0.2  # Should be close to mean
        @test abs(std(samples) - 2.0) < 0.2    # Should be close to std
        
        # Test uniform distribution parameter
        uniform_param = DistributionParameter("Uniform", Dict("min" => 5.0, "max" => 15.0))
        samples = [sample_parameter(uniform_param, rng) for _ in 1:1000]
        @test all(s >= 5.0 && s <= 15.0 for s in samples)
        @test abs(mean(samples) - 10.0) < 0.5  # Should be close to midpoint
        
        # Test random walk parameter
        rw_param = RandomWalkParameter(100.0, 0.02, 0.1)
        initial_value = sample_parameter(rw_param, rng)
        @test initial_value == 100.0
        
        # Test evolution
        evolved_value = evolve_random_walk(rw_param, initial_value, 1.0, rng)
        @test evolved_value != initial_value  # Should have changed
    end
    
    @testset "Seed Generation" begin
        base_seed = UInt32(12345)
        
        # Test that different trial IDs produce different seeds
        seed1 = generate_trial_seed(1, base_seed)
        seed2 = generate_trial_seed(2, base_seed)
        @test seed1 != seed2
        
        # Test reproducibility
        seed1_repeat = generate_trial_seed(1, base_seed)
        @test seed1 == seed1_repeat
        
        # Test RNG creation
        rng1 = create_trial_rng(seed1)
        rng2 = create_trial_rng(seed1)
        @test rand(rng1) == rand(rng2)  # Same seed should produce same values
    end
    
    @testset "Monte Carlo Engine Execution" begin
        # Create minimal test IR
        test_ir_data = IRData(
            Dict("id" => "test", "currency" => "USD"),
            [],
            [],
            [],
            Dict("frequency" => "monthly"),
            Dict(),
            [],
            nothing,
            Dict("test_param" => FixedParameter(1.0))
        )
        
        # Create engine
        engine = MonteCarloEngine(test_ir_data, UInt32(456))
        
        # Test single trial
        result = run_monte_carlo(engine, 1)
        @test result.total_trials == 1
        @test result.successful_trials >= 0
        @test length(result.trials) == 1
        @test result.trials[1].trial_id == 1
        
        # Test multiple trials
        result = run_monte_carlo(engine, 5)
        @test result.total_trials == 5
        @test length(result.trials) == 5
        @test all(t.trial_id in 1:5 for t in result.trials)
        
        # Test that trials have different seeds
        seeds = [t.seed for t in result.trials]
        @test length(unique(seeds)) == 5  # All seeds should be different
    end
    
    @testset "Result Serialization" begin
        # Create test result
        test_result = MonteCarloResult(
            [TrialResult(1, UInt32(123), Dict("x" => 1.0), nothing, nothing, nothing, 0.1, :success, nothing)],
            1,
            1,
            0.1,
            Dict("test" => "value")
        )
        
        # Test saving
        temp_file = tempname()
        save_results(test_result, temp_file)
        @test isfile(temp_file)
        
        # Test loading and structure
        saved_data = JSON3.read(read(temp_file, String))
        @test saved_data.total_trials == 1
        @test saved_data.successful_trials == 1
        @test length(saved_data.trials) == 1
        @test saved_data.trials[1].trial_id == 1
        
        # Clean up
        rm(temp_file)
    end
end