# engine/test/test_stochastic_analysis.jl
using Test
using Dates
using Statistics
using CFDLEngine

println("🧪 Starting Stochastic Analysis tests...")

@testset "Stochastic Analysis Tests" begin
    
    @testset "DistributionStats Basic Functionality" begin
        # Test with normal-like data
        values = [10.0, 12.0, 11.0, 13.0, 9.0, 14.0, 8.0, 15.0, 10.5, 11.5]
        
        dist_stats = CFDLEngine.analyze_distribution(values)
        
        # Check basic statistics
        @test dist_stats.mean ≈ mean(values)
        @test dist_stats.median ≈ median(values)
        @test dist_stats.std_dev ≈ std(values)
        @test dist_stats.variance ≈ var(values)
        @test dist_stats.sample_size == length(values)
        @test dist_stats.min_value == minimum(values)
        @test dist_stats.max_value == maximum(values)
        
        # Check percentiles
        @test haskey(dist_stats.percentiles, 0.05)
        @test haskey(dist_stats.percentiles, 0.25)
        @test haskey(dist_stats.percentiles, 0.50)
        @test haskey(dist_stats.percentiles, 0.75)
        @test haskey(dist_stats.percentiles, 0.95)
        
        # Median should match 50th percentile
        @test dist_stats.median ≈ dist_stats.percentiles[0.50]
        
        # Check confidence intervals
        @test haskey(dist_stats.confidence_intervals, 0.95)
        ci_95 = dist_stats.confidence_intervals[0.95]
        @test ci_95[1] < dist_stats.mean < ci_95[2]
    end
    
    @testset "Distribution Analysis Edge Cases" begin
        # Empty vector
        empty_stats = CFDLEngine.analyze_distribution(Float64[])
        @test isnan(empty_stats.mean)
        @test empty_stats.sample_size == 0
        
        # Single value
        single_stats = CFDLEngine.analyze_distribution([5.0])
        @test single_stats.mean == 5.0
        @test single_stats.median == 5.0
        @test single_stats.std_dev == 0.0 || isnan(single_stats.std_dev)  # std of single value can be NaN in Julia
        @test single_stats.sample_size == 1
        
        # Values with NaN and Inf
        problematic_values = [1.0, NaN, 3.0, Inf, 5.0, -Inf, 7.0]
        clean_stats = CFDLEngine.analyze_distribution(problematic_values)
        @test clean_stats.sample_size == 4  # Only finite values counted
        @test clean_stats.mean ≈ mean([1.0, 3.0, 5.0, 7.0])
        
        # All invalid values
        invalid_values = [NaN, Inf, -Inf]
        invalid_stats = CFDLEngine.analyze_distribution(invalid_values)
        @test isnan(invalid_stats.mean)
        @test invalid_stats.sample_size == 0
    end
    
    @testset "Risk Metrics Calculation" begin
        # Create test data with known characteristics
        values = [-5.0, -2.0, 0.0, 1.0, 3.0, 5.0, 8.0, 10.0, 12.0, 15.0]
        target = 0.0
        
        risk_metrics = CFDLEngine.calculate_risk_metrics(values, target)
        
        @test haskey(risk_metrics, "downside_deviation")
        @test haskey(risk_metrics, "probability_of_loss")
        @test haskey(risk_metrics, "expected_shortfall")
        @test haskey(risk_metrics, "gain_loss_ratio")
        
        # Probability of loss should be 20% (2 out of 10 values below 0)
        @test risk_metrics["probability_of_loss"] ≈ 0.2
        
        # Expected shortfall should be average of losses: (5 + 2) / 2 = 3.5
        @test risk_metrics["expected_shortfall"] ≈ 3.5
        
        # All positive values should have zero probability of loss
        positive_values = [1.0, 2.0, 3.0, 4.0, 5.0]
        positive_risk = CFDLEngine.calculate_risk_metrics(positive_values, 0.0)
        @test positive_risk["probability_of_loss"] == 0.0
        @test positive_risk["expected_shortfall"] == 0.0
    end
    
    @testset "Stochastic Metrics Analysis Integration" begin
        # Create mock trial metrics
        trial_metrics = Vector{Dict{String, CFDLEngine.MetricResult}}()
        
        for i in 1:100
            # Simulate varying NPV and IRR across trials
            npv_value = 100000.0 + randn() * 50000.0  # Mean 100k, std 50k
            irr_value = 0.12 + randn() * 0.03  # Mean 12%, std 3%
            
            trial_result = Dict{String, CFDLEngine.MetricResult}(
                "npv" => CFDLEngine.MetricResult("npv", npv_value, today(), Dict{String, Any}(), Dict{String, Any}()),
                "irr" => CFDLEngine.MetricResult("irr", irr_value, today(), Dict{String, Any}(), Dict{String, Any}())
            )
            push!(trial_metrics, trial_result)
        end
        
        # Analyze stochastic results
        stochastic_result = CFDLEngine.analyze_stochastic_metrics(trial_metrics)
        
        @test stochastic_result.num_trials == 100
        @test stochastic_result.successful_trials == 100
        @test length(stochastic_result.trial_metrics) == 100
        
        # Check distributions were created
        @test haskey(stochastic_result.distributions, "npv")
        @test haskey(stochastic_result.distributions, "irr")
        
        # Check expected values
        @test haskey(stochastic_result.expected_values, "npv")
        @test haskey(stochastic_result.expected_values, "irr")
        
        # NPV distribution should be roughly centered around 100k
        npv_dist = stochastic_result.distributions["npv"]
        @test npv_dist.mean ≈ 100000.0 atol=10000.0  # Within 10k of expected
        @test npv_dist.sample_size == 100
        
        # IRR distribution should be roughly centered around 12%
        irr_dist = stochastic_result.distributions["irr"]
        @test irr_dist.mean ≈ 0.12 atol=0.01  # Within 1% of expected
        @test irr_dist.sample_size == 100
    end
    
    @testset "Distribution Summary Generation" begin
        # Create test distribution with known characteristics
        values = collect(1.0:100.0)  # Simple linear sequence
        dist_stats = CFDLEngine.analyze_distribution(values)
        
        summary = CFDLEngine.generate_distribution_summary(dist_stats, "test_metric")
        
        @test summary["metric_name"] == "test_metric"
        @test summary["sample_size"] == 100
        @test haskey(summary, "central_tendency")
        @test haskey(summary, "variability")
        @test haskey(summary, "distribution_shape")
        @test haskey(summary, "risk_level")
        @test haskey(summary, "key_percentiles")
        @test haskey(summary, "risk_metrics")
        
        # Check central tendency
        @test summary["central_tendency"]["mean"] ≈ 50.5
        @test summary["central_tendency"]["median"] ≈ 50.5
        
        # Check key percentiles
        percentiles = summary["key_percentiles"]
        @test percentiles["5th"] ≈ 5.95 atol=1.0
        @test percentiles["95th"] ≈ 95.05 atol=1.0
        
        # Test with empty distribution
        empty_stats = CFDLEngine.DistributionStats(
            NaN, NaN, NaN, NaN, NaN, NaN,
            Dict{Float64, Float64}(),
            Dict{Float64, Tuple{Float64, Float64}}(),
            NaN, NaN, NaN, NaN, 0
        )
        empty_summary = CFDLEngine.generate_distribution_summary(empty_stats, "empty_metric")
        @test empty_summary["status"] == "no_valid_data"
    end
    
    @testset "Distribution Comparison" begin
        # Create two different distributions
        dist1_values = [10.0, 12.0, 11.0, 13.0, 9.0]  # Mean ≈ 11, lower volatility
        dist2_values = [5.0, 15.0, 8.0, 18.0, 4.0]   # Mean ≈ 10, higher volatility
        
        dist1 = CFDLEngine.analyze_distribution(dist1_values)
        dist2 = CFDLEngine.analyze_distribution(dist2_values)
        
        comparison = CFDLEngine.compare_distributions(dist1, dist2, "Conservative", "Aggressive")
        
        @test comparison["comparison_type"] == "distribution_comparison"
        @test comparison["distributions"] == ["Conservative", "Aggressive"]
        @test haskey(comparison, "mean_difference")
        @test haskey(comparison, "volatility_comparison")
        @test haskey(comparison, "risk_comparison")
        @test haskey(comparison, "dominance_analysis")
        
        # Conservative should have higher mean
        @test comparison["dominance_analysis"]["higher_mean"] == "Conservative"
        
        # Conservative should have lower volatility
        @test comparison["dominance_analysis"]["lower_volatility"] == "Conservative"
    end
    
    @testset "Large Scale Performance Test" begin
        # Test with large dataset to ensure performance
        large_values = randn(10000)  # 10,000 random values
        
        # Time the analysis (should be fast)
        start_time = time()
        dist_stats = CFDLEngine.analyze_distribution(large_values)
        analysis_time = time() - start_time
        
        @test analysis_time < 1.0  # Should complete in less than 1 second
        @test dist_stats.sample_size == 10000
        @test abs(dist_stats.mean) < 0.1  # Should be close to 0 for large random sample
        @test 0.9 < dist_stats.std_dev < 1.1  # Should be close to 1 for standard normal
        
        # Test percentiles are reasonable
        @test dist_stats.percentiles[0.05] < dist_stats.percentiles[0.95]
        @test dist_stats.percentiles[0.25] < dist_stats.percentiles[0.75]
    end
    
    @testset "Custom Implementation Integration with Stochastic Analysis" begin
        # Test stochastic analysis with metrics calculated using custom implementations
        # Generate realistic cash flow scenarios for Monte Carlo analysis
        trial_metrics = Vector{Dict{String, CFDLEngine.MetricResult}}()
        
        # Simulate 50 different investment scenarios
        for trial in 1:50
            # Generate variable cash flows: base + random variation
            base_cfs = [100000.0, 110000.0, 120000.0, 130000.0, 140000.0]
            variable_cfs = base_cfs .* (1.0 .+ randn(5) * 0.1)  # ±10% variation
            
            assumptions = Dict{String, Any}(
                "discount_rate" => 0.08 + randn() * 0.01,  # 8% ± 1%
                "initial_investment" => 400000.0,
                "debt_service" => fill(30000.0, 5)
            )
            
            # Calculate metrics using custom implementations
            metrics = CFDLEngine.calculate_all_metrics(variable_cfs, assumptions)
            push!(trial_metrics, metrics)
        end
        
        # Analyze the stochastic results
        stochastic_result = CFDLEngine.analyze_stochastic_metrics(trial_metrics)
        
        @test stochastic_result.num_trials == 50
        @test stochastic_result.successful_trials == 50
        
        # Verify all expected metrics are present
        @test haskey(stochastic_result.distributions, "npv")
        @test haskey(stochastic_result.distributions, "irr")
        @test haskey(stochastic_result.distributions, "moic")
        @test haskey(stochastic_result.distributions, "payback")
        
        # NPV distribution should be reasonable
        npv_dist = stochastic_result.distributions["npv"]
        @test npv_dist.sample_size == 50
        @test npv_dist.mean > 0  # Should be profitable on average
        @test npv_dist.std_dev > 0  # Should have some variability
        
        # IRR distribution should be reasonable
        irr_dist = stochastic_result.distributions["irr"]
        @test irr_dist.sample_size == 50
        @test 0.05 < irr_dist.mean < 0.25  # Reasonable average return
        @test irr_dist.std_dev > 0  # Should have some variability
        
        # Test that each individual trial used custom implementations
        # by verifying metadata indicates the correct calculation method
        first_trial = trial_metrics[1]
        first_npv = first_trial["npv"]
        
        # Verify the metadata indicates the correct calculation method
        @test first_npv.metadata["calculation_method"] == "manual_pv"
    end
    
    @testset "Real-World Scenario Testing" begin
        # Simulate realistic real estate investment returns
        # Base case: 8% IRR with 3% volatility over 100 trials
        realistic_irr_values = [0.08 + randn() * 0.03 for _ in 1:100]
        
        # Base case: $500k NPV with $200k volatility
        realistic_npv_values = [500000.0 + randn() * 200000.0 for _ in 1:100]
        
        irr_dist = CFDLEngine.analyze_distribution(realistic_irr_values)
        npv_dist = CFDLEngine.analyze_distribution(realistic_npv_values)
        
        # IRR analysis
        @test irr_dist.mean ≈ 0.08 atol=0.01
        @test 0.02 < irr_dist.std_dev < 0.04  # Reasonable volatility
        
        # Check that 90% of IRR values fall within reasonable range
        irr_90_range = irr_dist.percentiles[0.95] - irr_dist.percentiles[0.05]
        @test 0.05 < irr_90_range < 0.20  # 5% to 20% range seems reasonable
        
        # NPV analysis
        @test npv_dist.mean ≈ 500000.0 atol=50000.0
        @test 150000.0 < npv_dist.std_dev < 250000.0  # Reasonable volatility
        
        # Risk analysis - probability of negative NPV should be low but non-zero
        npv_risk = CFDLEngine.calculate_risk_metrics(realistic_npv_values, 0.0)
        @test 0.0 <= npv_risk["probability_of_loss"] <= 0.3  # At most 30% chance of loss
    end
    
    @testset "Stochastic Analysis Performance with Custom Implementation" begin
        # Test performance when analyzing large numbers of custom-calculated metrics
        large_trial_metrics = Vector{Dict{String, CFDLEngine.MetricResult}}()
        
        # Generate 1000 trials quickly
        for trial in 1:1000
            # Simple but realistic cash flows
            cfs = [50000.0, 55000.0, 60000.0]
            assumptions = Dict{String, Any}(
                "discount_rate" => 0.10,
                "initial_investment" => 150000.0
            )
            
            metrics = CFDLEngine.calculate_all_metrics(cfs, assumptions)
            push!(large_trial_metrics, metrics)
        end
        
        # Time the stochastic analysis
        start_time = time()
        large_stochastic_result = CFDLEngine.analyze_stochastic_metrics(large_trial_metrics)
        analysis_time = time() - start_time
        
        @test analysis_time < 2.0  # Should complete in less than 2 seconds
        @test large_stochastic_result.num_trials == 1000
        @test large_stochastic_result.successful_trials == 1000
        
        # Verify statistical properties with large sample
        npv_dist = large_stochastic_result.distributions["npv"]
        @test npv_dist.sample_size == 1000
        
        # With identical inputs, should have very low variance (allowing for numerical precision)
        @test npv_dist.std_dev < 1e-6  # Should be very close to zero
        @test abs(npv_dist.mean - npv_dist.median) < 1e-6  # Should be very close
        
        # All percentiles should be very close to each other
        @test abs(npv_dist.percentiles[0.05] - npv_dist.percentiles[0.95]) < 1e-6
    end
end

println("✅ Stochastic Analysis tests completed")