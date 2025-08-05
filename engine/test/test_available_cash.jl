# Tests for Available Cash Module (Stage 6)

using Test
using Dates
using CFDLEngine

@testset "Available Cash Tests" begin
    
    # Test data setup
    function create_test_tax_adjustments()
        return [
            TaxAdjustment(
                "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
                100000.0, 8000.0, 2000.0, 1000.0, 89000.0, 22250.0, 62750.0,
                Dict{String, Any}()
            ),
            TaxAdjustment(
                "deal1", Date(2024, 2, 1), Date(2024, 2, 28),
                105000.0, 8000.0, 0.0, 2000.0, 95000.0, 23750.0, 66250.0,
                Dict{String, Any}()
            )
        ]
    end
    
    @testset "AvailableCashCalculation Creation" begin
        calculation = AvailableCashCalculation(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            62750.0, 3000.0, 500.0, 1000.0, 58250.0,
            Dict("test" => true)
        )
        
        @test calculation.entity_id == "deal1"
        @test calculation.period_start == Date(2024, 1, 1)
        @test calculation.period_end == Date(2024, 1, 31)
        @test calculation.after_tax_cf == 62750.0
        @test calculation.capital_reserves == 3000.0
        @test calculation.working_capital_change == 500.0
        @test calculation.other_adjustments == 1000.0
        @test calculation.available_for_distribution == 58250.0
        @test calculation.metadata["test"] == true
    end
    
    @testset "Calculate Available Cash" begin
        tax_adjustments = create_test_tax_adjustments()
        calculations = calculate_available_cash(tax_adjustments)
        
        @test length(calculations) == 2
        
        # Test first calculation
        calc1 = calculations[1]
        @test calc1.entity_id == "deal1"
        @test calc1.period_start == Date(2024, 1, 1)
        @test calc1.period_end == Date(2024, 1, 31)
        @test calc1.after_tax_cf == 62750.0
        
        # Check capital reserves calculation (should be ~3% of after-tax cash flow)
        expected_reserves = calculate_capital_reserves(62750.0, Date(2024, 1, 1))
        @test calc1.capital_reserves == expected_reserves
        
        # Check working capital change (should be 0 for simplified calculation)
        @test calc1.working_capital_change == 0.0
        
        # Check other adjustments (should be small percentage)
        expected_other = 62750.0 * 0.005
        @test calc1.other_adjustments == expected_other
        
        # Check available for distribution calculation
        expected_available = 62750.0 - expected_reserves - 0.0 - expected_other
        @test calc1.available_for_distribution == expected_available
        
        # Check metadata
        @test calc1.metadata["reserve_rate"] > 0
        @test calc1.metadata["distribution_coverage"] > 0
        @test calc1.metadata["stage"] == "available_cash_calculation"
        @test calc1.metadata["calculation_method"] == "industry_standard"
        
        # Test second calculation
        calc2 = calculations[2]
        @test calc2.after_tax_cf == 66250.0
        @test calc2.available_for_distribution > calc1.available_for_distribution  # Should be higher
    end
    
    @testset "Capital Reserves Calculation" begin
        # Test current year reserve rate
        reserves_2024 = calculate_capital_reserves(100000.0, Date(2024, 1, 1))
        expected_rate = 0.03 * 1.1  # Base rate adjusted for current year
        expected_reserves = 100000.0 * expected_rate
        @test reserves_2024 == expected_reserves
        
        # Test historical reserve rate
        reserves_2019 = calculate_capital_reserves(100000.0, Date(2019, 1, 1))
        expected_reserves_2019 = 100000.0 * 0.03  # Base rate only
        @test reserves_2019 == expected_reserves_2019
        
        # Test negative cash flow (no reserves)
        reserves_negative = calculate_capital_reserves(-10000.0, Date(2024, 1, 1))
        @test reserves_negative == 0.0
        
        # Test custom reserve rate
        custom_reserves = calculate_capital_reserves(100000.0, Date(2024, 1, 1), 0.05)
        @test custom_reserves == 5000.0
    end
    
    @testset "Distribution Policy Application" begin
        calculations = calculate_available_cash(create_test_tax_adjustments())
        
        # Test default policy
        default_policy_calcs = apply_distribution_policy(calculations)
        
        @test length(default_policy_calcs) == 2
        
        for (original, adjusted) in zip(calculations, default_policy_calcs)
            # Available distribution should be reduced by policy constraints
            @test adjusted.available_for_distribution <= original.available_for_distribution
            
            # Check policy metadata
            @test adjusted.metadata["policy_applied"] == true
            @test haskey(adjusted.metadata, "min_retention_amount")
            @test haskey(adjusted.metadata, "additional_reserves")
            @test haskey(adjusted.metadata, "policy_adjustment")
            @test adjusted.metadata["distribution_frequency"] == "monthly"
        end
        
        # Test custom policy
        custom_policy = Dict{String, Any}(
            "min_cash_retention_months" => 6.0,
            "additional_reserve_rate" => 0.10,
            "distribution_frequency" => "quarterly"
        )
        
        custom_policy_calcs = apply_distribution_policy(calculations, custom_policy)
        
        # Should have more conservative distributions
        for (default, custom) in zip(default_policy_calcs, custom_policy_calcs)
            @test custom.available_for_distribution <= default.available_for_distribution
            @test custom.metadata["distribution_frequency"] == "quarterly"
        end
    end
    
    @testset "Calculate Distribution Metrics" begin
        calculations = calculate_available_cash(create_test_tax_adjustments())
        metrics = calculate_distribution_metrics(calculations)
        
        @test haskey(metrics, "aggregate_metrics")
        @test haskey(metrics, "distribution_efficiency")
        @test haskey(metrics, "analysis_metadata")
        
        # Test aggregate metrics
        aggregate = metrics["aggregate_metrics"]
        @test aggregate["total_after_tax_cf"] == 129000.0  # 62750 + 66250
        @test aggregate["total_available_for_distribution"] > 0
        @test aggregate["total_capital_reserves"] > 0
        @test aggregate["total_working_capital_changes"] == 0.0  # Simplified
        
        # Test distribution efficiency
        efficiency = metrics["distribution_efficiency"]
        @test efficiency["distribution_rate"] > 0 && efficiency["distribution_rate"] <= 1.0
        @test efficiency["reserve_rate"] > 0
        @test efficiency["average_coverage_ratio"] > 0
        @test haskey(efficiency, "cash_flow_volatility")
        
        # Test analysis metadata
        analysis = metrics["analysis_metadata"]
        @test analysis["periods_analyzed"] == 2
        @test analysis["entities_covered"] == 1
        @test analysis["stage_completed"] == "available_cash_calculation"
    end
    
    @testset "Cash Flow Volatility" begin
        # Test stable cash flows
        stable_amounts = [100000.0, 100000.0, 100000.0, 100000.0]
        @test calculate_cash_flow_volatility(stable_amounts) == 0.0
        
        # Test volatile cash flows
        volatile_amounts = [50000.0, 150000.0, 75000.0, 125000.0]
        volatility = calculate_cash_flow_volatility(volatile_amounts)
        @test volatility > 0
        
        # Test insufficient data
        single_amount = [100000.0]
        @test calculate_cash_flow_volatility(single_amount) == 0.0
        
        empty_amounts = Float64[]
        @test calculate_cash_flow_volatility(empty_amounts) == 0.0
        
        # Test zero mean (edge case)
        zero_mean_amounts = [-50000.0, 50000.0]
        zero_volatility = calculate_cash_flow_volatility(zero_mean_amounts)
        @test zero_volatility == 0.0  # Should handle division by zero
    end
    
    @testset "Summarize Available Cash" begin
        calculations = calculate_available_cash(create_test_tax_adjustments())
        summary = summarize_available_cash(calculations)
        
        @test haskey(summary, "summary_metrics")
        @test haskey(summary, "adjustment_breakdown")
        @test haskey(summary, "period_coverage")
        
        # Test summary metrics
        summary_metrics = summary["summary_metrics"]
        @test summary_metrics["total_calculations"] == 2
        @test summary_metrics["total_after_tax_cf"] == 129000.0
        @test summary_metrics["overall_distribution_rate"] > 0
        
        # Test adjustment breakdown
        breakdown = summary["adjustment_breakdown"]
        @test breakdown["total_capital_reserves"] > 0
        @test breakdown["total_working_capital_changes"] == 0.0
        @test breakdown["total_other_adjustments"] > 0
        @test breakdown["average_reserve_rate"] > 0
        
        # Test period coverage
        period_coverage = summary["period_coverage"]
        @test period_coverage["start_date"] == Date(2024, 1, 1)
        @test period_coverage["end_date"] == Date(2024, 2, 28)
        @test period_coverage["entities_covered"] == 1
        
        @test summary["stage_completed"] == "available_cash_calculation"
    end
    
    @testset "Monthly Operating Expense Estimation" begin
        # Test typical calculation
        typical_calc = AvailableCashCalculation(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 3000.0, 0.0, 500.0, 96500.0, Dict{String, Any}()
        )
        
        monthly_expense = estimate_monthly_operating_expense(typical_calc)
        expected_expense = max(10000.0, 100000.0 * 0.4 / 12)  # 40% of after-tax CF / 12 months
        @test monthly_expense == expected_expense
        
        # Test minimum threshold
        small_calc = AvailableCashCalculation(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            1000.0, 30.0, 0.0, 5.0, 965.0, Dict{String, Any}()
        )
        
        small_monthly_expense = estimate_monthly_operating_expense(small_calc)
        @test small_monthly_expense == 10000.0  # Should hit minimum threshold
    end
    
    @testset "Available Cash Validation" begin
        # Test valid calculation
        valid_calc = AvailableCashCalculation(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 3000.0, 500.0, 1000.0, 95500.0, Dict{String, Any}()
        )
        
        validation = validate_available_cash_calculation(valid_calc)
        @test validation["is_valid"] == true
        @test length(validation["errors"]) == 0
        
        # Test calculation with error
        invalid_calc = AvailableCashCalculation(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 3000.0, 500.0, 1000.0, 90000.0,  # Wrong available amount
            Dict{String, Any}()
        )
        
        validation_invalid = validate_available_cash_calculation(invalid_calc)
        @test validation_invalid["is_valid"] == false
        @test length(validation_invalid["errors"]) > 0
        
        # Test negative available cash (should generate warning)
        negative_calc = AvailableCashCalculation(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            50000.0, 30000.0, 5000.0, 20000.0, -5000.0, Dict{String, Any}()
        )
        
        validation_negative = validate_available_cash_calculation(negative_calc)
        @test validation_negative["is_valid"] == true  # Valid but with warnings
        @test length(validation_negative["warnings"]) > 0
        @test any(w -> contains(w, "Negative available"), validation_negative["warnings"])
        
        # Test high reserve rate (should generate warning)
        high_reserve_calc = AvailableCashCalculation(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 25000.0, 0.0, 0.0, 75000.0, Dict{String, Any}()  # 25% reserve rate
        )
        
        validation_high_reserve = validate_available_cash_calculation(high_reserve_calc)
        @test validation_high_reserve["is_valid"] == true
        @test any(w -> contains(w, "High reserve rate"), validation_high_reserve["warnings"])
    end
    
    @testset "Edge Cases" begin
        # Test with empty tax adjustments
        empty_calculations = calculate_available_cash(TaxAdjustment[])
        @test length(empty_calculations) == 0
        
        empty_metrics = calculate_distribution_metrics(AvailableCashCalculation[])
        @test haskey(empty_metrics, "message")
        
        empty_summary = summarize_available_cash(AvailableCashCalculation[])
        @test haskey(empty_summary, "message")
        
        # Test with zero after-tax cash flow
        zero_tax_adjustment = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            50000.0, 10000.0, 5000.0, 35000.0, 0.0, 0.0, 0.0,
            Dict{String, Any}()
        )
        
        zero_calculations = calculate_available_cash([zero_tax_adjustment])
        @test length(zero_calculations) == 1
        @test zero_calculations[1].after_tax_cf == 0.0
        @test zero_calculations[1].capital_reserves == 0.0  # No reserves on zero cash flow
        @test zero_calculations[1].available_for_distribution == 0.0
        
        # Test with negative after-tax cash flow
        negative_tax_adjustment = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            50000.0, 5000.0, 2000.0, 3000.0, 40000.0, 12000.0, -10000.0,
            Dict{String, Any}()
        )
        
        negative_calculations = calculate_available_cash([negative_tax_adjustment])
        @test length(negative_calculations) == 1
        @test negative_calculations[1].after_tax_cf == -10000.0
        @test negative_calculations[1].capital_reserves == 0.0  # No reserves on negative cash flow
        @test negative_calculations[1].available_for_distribution < 0  # Should be negative
    end
    
    @testset "Real Estate Distribution Scenarios" begin
        # Test typical multifamily property
        multifamily_adjustment = TaxAdjustment(
            "multifamily_complex", Date(2024, 1, 1), Date(2024, 1, 31),
            150000.0, 12000.0, 3000.0, 5000.0, 130000.0, 32500.0, 92500.0,
            Dict{String, Any}()
        )
        
        calculations = calculate_available_cash([multifamily_adjustment])
        @test length(calculations) == 1
        
        calc = calculations[1]
        @test calc.after_tax_cf == 92500.0
        @test calc.capital_reserves > 0  # Should have meaningful reserves
        @test calc.available_for_distribution > 0
        @test calc.available_for_distribution < calc.after_tax_cf  # After reserves and adjustments
        
        # Check distribution coverage is reasonable
        distribution_coverage = calc.metadata["distribution_coverage"]
        @test distribution_coverage > 0.8 && distribution_coverage < 1.0  # Reasonable range
        
        # Test with conservative distribution policy
        conservative_policy = Dict{String, Any}(
            "min_cash_retention_months" => 6.0,
            "additional_reserve_rate" => 0.15,
            "distribution_frequency" => "quarterly"
        )
        
        conservative_calcs = apply_distribution_policy(calculations, conservative_policy)
        conservative_calc = conservative_calcs[1]
        
        # Should have significantly lower available distribution
        @test conservative_calc.available_for_distribution < calc.available_for_distribution * 0.8
        
        # Check that total reserves increased
        total_original_reserves = calc.capital_reserves + calc.other_adjustments
        total_conservative_reserves = conservative_calc.capital_reserves + conservative_calc.other_adjustments
        @test total_conservative_reserves > total_original_reserves
    end
end

println("✅ Available Cash tests completed")