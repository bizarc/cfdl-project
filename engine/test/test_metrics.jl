# engine/test/test_metrics.jl
using Test
using Dates
using CFDLEngine

println("🧪 Starting Metrics tests...")

@testset "Core Metrics Tests" begin
    
    @testset "NPV Calculation" begin
        # Basic NPV calculation
        cash_flows = [100.0, 100.0, 100.0]
        discount_rate = 0.10
        initial_investment = 200.0
        
        npv = CFDLEngine.calculate_npv(cash_flows, discount_rate, initial_investment)
        expected_npv = 100.0/(1.1) + 100.0/(1.1^2) + 100.0/(1.1^3) - 200.0
        @test npv ≈ expected_npv atol=1e-10
        
        # Verify NPV calculation is correct
        expected_pv = sum(cash_flows[i] / (1 + discount_rate)^i for i in 1:length(cash_flows))
        expected_npv_manual = expected_pv - initial_investment
        @test npv ≈ expected_npv_manual atol=1e-10
        
        # Zero discount rate
        npv_zero = CFDLEngine.calculate_npv(cash_flows, 0.0, initial_investment)
        @test npv_zero == 300.0 - 200.0
        
        # Empty cash flows
        npv_empty = CFDLEngine.calculate_npv(Float64[], 0.10, 100.0)
        @test npv_empty == -100.0
        
        # Negative cash flows
        negative_cfs = [-50.0, 200.0, 150.0]
        npv_negative = CFDLEngine.calculate_npv(negative_cfs, 0.10, 100.0)
        expected_negative = -50.0/1.1 + 200.0/(1.1^2) + 150.0/(1.1^3) - 100.0
        @test npv_negative ≈ expected_negative atol=1e-10
        
        # Test with additional cash flows
        additional_cfs = [50.0, 75.0, 125.0]
        npv_additional = CFDLEngine.calculate_npv(additional_cfs, 0.10, 200.0)
        # Verify against manual calculation
        expected_additional = 50.0/(1.1^1) + 75.0/(1.1^2) + 125.0/(1.1^3) - 200.0
        @test npv_additional ≈ expected_additional atol=1e-10
    end
    
    @testset "IRR Calculation" begin
        # Simple IRR case with known solution
        cash_flows = [110.0]  # Single period, 10% return
        initial_investment = 100.0
        
        irr = CFDLEngine.calculate_irr(cash_flows, initial_investment)
        @test irr ≈ 0.10 atol=1e-6
        
        # Verify IRR is correct by checking NPV at this rate
        npv_at_irr = CFDLEngine.calculate_npv(cash_flows, irr, initial_investment)
        @test abs(npv_at_irr) < 1e-6  # NPV should be approximately zero at IRR
        
        # Multi-period IRR
        cash_flows_multi = [50.0, 60.0, 70.0]
        irr_multi = CFDLEngine.calculate_irr(cash_flows_multi, 150.0)
        @test !isnan(irr_multi)
        @test irr_multi > 0.0  # Should be positive return
        
        # Verify IRR by checking NPV ≈ 0
        npv_at_multi_irr = CFDLEngine.calculate_npv(cash_flows_multi, irr_multi, 150.0)
        @test abs(npv_at_multi_irr) < 1e-6  # NPV should be approximately zero at IRR
        
        # No solution case (negative total returns)
        bad_cash_flows = [10.0, 20.0, 30.0]
        bad_initial = 100.0
        irr_bad = CFDLEngine.calculate_irr(bad_cash_flows, bad_initial)
        @test isnan(irr_bad)
        
        # Empty cash flows
        irr_empty = CFDLEngine.calculate_irr(Float64[], 100.0)
        @test isnan(irr_empty)
        
        # Test bisection fallback with extreme case
        extreme_cfs = [1000.0]
        extreme_initial = 100.0
        irr_extreme = CFDLEngine.calculate_irr(extreme_cfs, extreme_initial)
        @test irr_extreme ≈ 9.0 atol=1e-6  # 900% return
    end
    
    @testset "DSCR Calculation" begin
        # Basic DSCR calculation
        operating_cfs = [120.0, 130.0, 140.0]
        debt_service = [100.0, 100.0, 100.0]
        
        dscr_values = CFDLEngine.calculate_dscr(operating_cfs, debt_service)
        @test length(dscr_values) == 3
        @test dscr_values[1] == 1.2
        @test dscr_values[2] == 1.3
        @test dscr_values[3] == 1.4
        
        # Zero debt service
        zero_debt = [0.0, 0.0, 0.0]
        dscr_no_debt = CFDLEngine.calculate_dscr(operating_cfs, zero_debt)
        @test all(isinf, dscr_no_debt)
        
        # Negative operating cash flows
        negative_ocf = [-50.0, 100.0, 150.0]
        dscr_negative = CFDLEngine.calculate_dscr(negative_ocf, debt_service)
        @test dscr_negative[1] == -0.5
        @test dscr_negative[2] == 1.0
        @test dscr_negative[3] == 1.5
        
        # Mismatched vector lengths
        @test_throws ArgumentError CFDLEngine.calculate_dscr([100.0], [100.0, 200.0])
    end
    
    @testset "MOIC Calculation" begin
        # Basic MOIC calculation
        total_distributions = 250.0
        initial_investment = 100.0
        
        moic = CFDLEngine.calculate_moic(total_distributions, initial_investment)
        @test moic == 2.5
        
        # Zero initial investment
        moic_zero = CFDLEngine.calculate_moic(100.0, 0.0)
        @test isnan(moic_zero)
        
        # Loss scenario
        moic_loss = CFDLEngine.calculate_moic(50.0, 100.0)
        @test moic_loss == 0.5
        
        # Exact breakeven
        moic_breakeven = CFDLEngine.calculate_moic(100.0, 100.0)
        @test moic_breakeven == 1.0
    end
    
    @testset "Payback Period Calculation" begin
        # Simple payback case
        cash_flows = [50.0, 60.0, 40.0]  # Recovers 100 in first two periods
        initial_investment = 100.0
        
        payback = CFDLEngine.calculate_payback_period(cash_flows, initial_investment)
        @test payback ≈ 1.0 + (50.0/60.0) atol=1e-10  # 1 full period + fraction of second
        
        # Exact recovery at period end
        exact_cfs = [50.0, 50.0, 50.0]
        payback_exact = CFDLEngine.calculate_payback_period(exact_cfs, 100.0)
        @test payback_exact == 2.0
        
        # Never recovered
        small_cfs = [10.0, 10.0, 10.0]
        payback_never = CFDLEngine.calculate_payback_period(small_cfs, 100.0)
        @test isinf(payback_never)
        
        # Empty cash flows
        payback_empty = CFDLEngine.calculate_payback_period(Float64[], 100.0)
        @test isinf(payback_empty)
        
        # Zero initial investment
        payback_zero = CFDLEngine.calculate_payback_period([100.0], 0.0)
        @test isinf(payback_zero)
        
        # Immediate recovery in first period
        immediate_cfs = [150.0, 50.0]
        payback_immediate = CFDLEngine.calculate_payback_period(immediate_cfs, 100.0)
        @test payback_immediate ≈ 2.0/3.0 atol=1e-10  # 2/3 of first period
    end
    
    @testset "Calculate All Metrics Integration" begin
        # Test integrated metric calculation
        cash_flows = [50.0, 60.0, 70.0, 80.0]
        assumptions = Dict{String, Any}(
            "discount_rate" => 0.12,
            "initial_investment" => 200.0,
            "debt_service" => [20.0, 20.0, 20.0, 20.0]
        )
        
        results = CFDLEngine.calculate_all_metrics(cash_flows, assumptions)
        
        # Check all metrics are present
        @test haskey(results, "npv")
        @test haskey(results, "irr") 
        @test haskey(results, "dscr")
        @test haskey(results, "moic")
        @test haskey(results, "payback")
        
        # Check MetricResult structure
        npv_result = results["npv"]
        @test npv_result.metric_name == "npv"
        @test isa(npv_result.value, Float64)
        @test isa(npv_result.calculation_date, Date)
        @test haskey(npv_result.assumptions, "discount_rate")
        @test haskey(npv_result.metadata, "calculation_method")
        
        # Verify calculations match individual functions
        expected_npv = CFDLEngine.calculate_npv(cash_flows, 0.12, 200.0)
        @test results["npv"].value ≈ expected_npv
        
        expected_irr = CFDLEngine.calculate_irr(cash_flows, 200.0)
        @test results["irr"].value ≈ expected_irr
        
        expected_moic = CFDLEngine.calculate_moic(sum(cash_flows), 200.0)
        @test results["moic"].value ≈ expected_moic
        
        expected_payback = CFDLEngine.calculate_payback_period(cash_flows, 200.0)
        @test results["payback"].value ≈ expected_payback
    end
    
    @testset "Edge Cases and Error Handling" begin
        # Test with infinite values
        inf_cfs = [100.0, Inf, 100.0]
        npv_inf = CFDLEngine.calculate_npv(inf_cfs, 0.10, 100.0)
        @test isfinite(npv_inf)  # Should handle infinite values gracefully
        
        # Test with NaN values
        nan_cfs = [100.0, NaN, 100.0]
        npv_nan = CFDLEngine.calculate_npv(nan_cfs, 0.10, 100.0)
        @test isfinite(npv_nan)  # Should handle NaN values gracefully
        
        # Test with very large numbers
        large_cfs = [1e10, 1e10, 1e10]
        npv_large = CFDLEngine.calculate_npv(large_cfs, 0.10, 1e9)
        @test isfinite(npv_large)
        
        # Test with very small numbers
        small_cfs = [1e-10, 1e-10, 1e-10]
        npv_small = CFDLEngine.calculate_npv(small_cfs, 0.10, 1e-9)
        @test isfinite(npv_small)
    end
    
    @testset "Custom Implementation Testing" begin
        # Test custom IRR implementation with normal case
        normal_cfs = [100.0, 110.0, 121.0]
        normal_initial = 300.0
        
        irr_normal = CFDLEngine.calculate_irr(normal_cfs, normal_initial)
        @test !isnan(irr_normal)
        @test -0.99 < irr_normal < 10.0  # Within reasonable bounds
        
        # Test edge cases
        # Very large numbers that might cause numerical issues
        large_cfs = [1e15, 1e15]
        large_initial = 1e15
        irr_large = CFDLEngine.calculate_irr(large_cfs, large_initial)
        # Should either work or fall back gracefully
        @test isfinite(irr_large) || isnan(irr_large)
        
        # Very small numbers
        tiny_cfs = [1e-10, 1e-10]
        tiny_initial = 1e-10
        irr_tiny = CFDLEngine.calculate_irr(tiny_cfs, tiny_initial)
        @test isfinite(irr_tiny) || isnan(irr_tiny)
        
        # Test metadata indicates which method was used
        assumptions = Dict{String, Any}(
            "discount_rate" => 0.10,
            "initial_investment" => 100.0
        )
        results = CFDLEngine.calculate_all_metrics([110.0], assumptions)
        
        # NPV should indicate manual_pv method
        @test results["npv"].metadata["calculation_method"] == "manual_pv"
        
        # IRR should indicate newton_raphson method (or fallback)
        @test haskey(results["irr"].metadata, "calculation_method")
        @test results["irr"].metadata["calculation_method"] in ["newton_raphson", "bisection", "custom"]
        
        # The important thing is that we get a valid IRR result
        @test !isnan(results["irr"].value)
        @test results["irr"].value > 0.0  # Should be positive for this profitable scenario
        
        # Test non-finite value handling in NPV
        problematic_cfs = [100.0, Inf, NaN, 100.0]
        npv_problematic = CFDLEngine.calculate_npv(problematic_cfs, 0.10, 200.0)
        @test isfinite(npv_problematic)  # Should filter out bad values
        
        # Verify it matches calculation with only valid values
        clean_cfs = [100.0, 100.0]  # Only the finite values
        npv_clean = CFDLEngine.calculate_npv(clean_cfs, 0.10, 200.0)
        @test npv_problematic ≈ npv_clean atol=1e-10
    end
    
    @testset "Real Estate Scenario Testing" begin
        # Realistic commercial real estate cash flows
        # Year 1-5: Operating cash flows, Year 5: Sale proceeds
        re_cash_flows = [125000.0, 135000.0, 145000.0, 155000.0, 165000.0 + 2000000.0]
        re_assumptions = Dict{String, Any}(
            "discount_rate" => 0.08,  # 8% cap rate
            "initial_investment" => 1500000.0,  # $1.5M purchase
            "debt_service" => [80000.0, 80000.0, 80000.0, 80000.0, 80000.0]  # Annual debt service
        )
        
        re_results = CFDLEngine.calculate_all_metrics(re_cash_flows, re_assumptions)
        
        # Verify reasonable real estate metrics
        @test re_results["npv"].value > 0  # Should be profitable
        @test re_results["irr"].value > 0.05  # Should exceed 5%
        @test re_results["irr"].value < 0.50  # Should be reasonable (<50%)
        @test re_results["moic"].value > 1.0  # Should have positive return
        @test re_results["payback"].value < 10.0  # Should payback within 10 years
        @test re_results["dscr"].value > 1.0  # Should cover debt service
        
        # Test that our custom implementation works correctly for this realistic scenario
        # Verify NPV calculation matches manual calculation
        expected_pv = sum(re_cash_flows[i] / (1.08)^i for i in 1:length(re_cash_flows))
        expected_npv = expected_pv - 1500000.0
        @test re_results["npv"].value ≈ expected_npv atol=1e-6
    end
end

println("✅ Metrics tests completed")