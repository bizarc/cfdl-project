# Tests for Tax Processing Module (Stage 5)

using Test
using Dates
using CFDLEngine

@testset "Tax Processing Tests" begin
    
    # Test data setup
    function create_test_financing_adjustments()
        return [
            FinancingAdjustment(
                "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict("interest" => 15000.0), Dict{String, Float64}(),
                100000.0, 85000.0, Dict{String, Any}()
            ),
            FinancingAdjustment(
                "deal1", Date(2024, 2, 1), Date(2024, 2, 28),
                Dict("interest" => 15000.0), Dict{String, Float64}(),
                105000.0, 90000.0, Dict{String, Any}()
            )
        ]
    end
    
    function create_test_cash_flow_entries()
        return [
            CashFlowEntry(
                "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict("revenue" => 120000.0, "expenses" => -20000.0),
                Dict("interest" => 15000.0), Dict{String, Float64}(),
                Dict("Building Depreciation" => 8000.0, "Loan Amortization" => 2000.0),
                Dict{String, Any}()
            ),
            CashFlowEntry(
                "deal1", "deal", Date(2024, 2, 1), Date(2024, 2, 28),
                Dict("revenue" => 125000.0, "expenses" => -20000.0),
                Dict("interest" => 15000.0), Dict{String, Float64}(),
                Dict("Building Depreciation" => 8000.0, "Property Tax" => 3000.0),
                Dict{String, Any}()
            )
        ]
    end
    
    @testset "TaxAdjustment Creation" begin
        adjustment = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 8000.0, 2000.0, 1000.0, 89000.0, 22250.0, 62750.0,
            Dict("test" => true)
        )
        
        @test adjustment.entity_id == "deal1"
        @test adjustment.period_start == Date(2024, 1, 1)
        @test adjustment.period_end == Date(2024, 1, 31)
        @test adjustment.pre_tax_income == 100000.0
        @test adjustment.depreciation == 8000.0
        @test adjustment.amortization == 2000.0
        @test adjustment.other_tax_adjustments == 1000.0
        @test adjustment.taxable_income == 89000.0
        @test adjustment.income_tax_expense == 22250.0
        @test adjustment.after_tax_cf == 62750.0
        @test adjustment.metadata["test"] == true
    end
    
    @testset "Tax Item Classification" begin
        # Test depreciation items
        @test is_depreciation_item("Building Depreciation") == true
        @test is_depreciation_item("depreciation") == true
        @test is_depreciation_item("FF&E_Depreciation") == true
        @test is_depreciation_item("building_deprec") == true
        
        # Test amortization items
        @test is_amortization_item("Loan Amortization") == true
        @test is_amortization_item("amortization") == true
        @test is_amortization_item("Intangible_Amortization") == true
        @test is_amortization_item("loan_amort") == true
        
        # Test non-tax items
        @test is_depreciation_item("Revenue") == false
        @test is_amortization_item("Interest Expense") == false
        @test is_depreciation_item("Property Tax") == false
    end
    
    @testset "Tax Rate Calculation" begin
        # Test current year tax rates (post-TCJA)
        rates_2024 = get_applicable_tax_rates("deal1", Date(2024, 1, 1))
        @test rates_2024["federal_rate"] == 0.21
        @test rates_2024["state_rate"] == 0.06
        @test rates_2024["local_rate"] == 0.01
        @test rates_2024["combined_rate"] == 0.28
        
        # Test historical tax rates (pre-TCJA)
        rates_2017 = get_applicable_tax_rates("deal1", Date(2017, 1, 1))
        @test rates_2017["federal_rate"] == 0.35
        @test rates_2017["combined_rate"] > 0.35
    end
    
    @testset "Income Tax Calculation" begin
        tax_rates = Dict{String, Float64}("combined_rate" => 0.25)
        
        # Test positive taxable income
        @test calculate_income_tax(100000.0, tax_rates) == 25000.0
        
        # Test zero taxable income
        @test calculate_income_tax(0.0, tax_rates) == 0.0
        
        # Test negative taxable income (no tax)
        @test calculate_income_tax(-10000.0, tax_rates) == 0.0
    end
    
    @testset "Calculate Tax Adjustments" begin
        financing_adjustments = create_test_financing_adjustments()
        cash_flow_entries = create_test_cash_flow_entries()
        
        tax_adjustments = calculate_tax_adjustments(financing_adjustments, cash_flow_entries)
        
        @test length(tax_adjustments) == 2
        
        # Test first adjustment
        adj1 = tax_adjustments[1]
        @test adj1.entity_id == "deal1"
        @test adj1.period_start == Date(2024, 1, 1)
        @test adj1.period_end == Date(2024, 1, 31)
        @test adj1.pre_tax_income == 100000.0  # From financing adjustment unlevered CF
        
        # Check tax deductions
        @test adj1.depreciation == 8000.0
        @test adj1.amortization == 2000.0
        @test adj1.other_tax_adjustments == 0.0  # No other tax items in first period
        
        # Check taxable income calculation
        expected_taxable = 100000.0 - 8000.0 - 2000.0 - 0.0
        @test adj1.taxable_income == expected_taxable
        
        # Check tax expense (28% combined rate)
        expected_tax = expected_taxable * 0.28
        @test adj1.income_tax_expense == expected_tax
        
        # Check after-tax cash flow
        expected_after_tax = 85000.0 - expected_tax  # levered CF minus tax
        @test adj1.after_tax_cf == expected_after_tax
        
        # Check metadata
        @test adj1.metadata["effective_tax_rate"] ≈ expected_tax / 100000.0 atol=0.001
        @test adj1.metadata["marginal_tax_rate"] == 0.28
        @test adj1.metadata["tax_shield_value"] == (8000.0 + 2000.0) * 0.28
        @test adj1.metadata["stage"] == "tax_processing"
        
        # Test second adjustment (has property tax in other adjustments)
        adj2 = tax_adjustments[2]
        @test adj2.depreciation == 8000.0
        @test adj2.amortization == 0.0  # No amortization in second period
        @test adj2.other_tax_adjustments == 3000.0  # Property tax
    end
    
    @testset "Apply Final Tax Adjustments" begin
        initial_adjustments = calculate_tax_adjustments(
            create_test_financing_adjustments(), 
            create_test_cash_flow_entries()
        )
        
        final_adjustments = apply_final_tax_adjustments(initial_adjustments)
        
        @test length(final_adjustments) == 2
        
        # Check that tax credits were applied
        for (initial, final) in zip(initial_adjustments, final_adjustments)
            @test haskey(final.metadata, "tax_credits")
            @test haskey(final.metadata, "final_tax_expense")
            @test haskey(final.metadata, "final_effective_tax_rate")
            
            # Tax expense should be reduced by credits
            @test final.income_tax_expense <= initial.income_tax_expense
            
            # After-tax cash flow should be improved by credits
            @test final.after_tax_cf >= initial.after_tax_cf
        end
    end
    
    @testset "Tax Credits Calculation" begin
        # Test depreciation-based credit
        adjustment_with_depreciation = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 10000.0, 0.0, 0.0, 90000.0, 25200.0, 64800.0,
            Dict{String, Any}()
        )
        
        credits = calculate_tax_credits(adjustment_with_depreciation)
        expected_credits = 10000.0 * 0.02  # 2% of depreciation
        @test credits == expected_credits
        
        # Test no depreciation
        adjustment_no_depreciation = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 0.0, 0.0, 0.0, 100000.0, 28000.0, 57000.0,
            Dict{String, Any}()
        )
        
        no_credits = calculate_tax_credits(adjustment_no_depreciation)
        @test no_credits == 0.0
    end
    
    @testset "Calculate Tax Metrics" begin
        tax_adjustments = calculate_tax_adjustments(
            create_test_financing_adjustments(), 
            create_test_cash_flow_entries()
        )
        
        metrics = calculate_tax_metrics(tax_adjustments)
        
        @test haskey(metrics, "aggregate_metrics")
        @test haskey(metrics, "tax_efficiency")
        @test haskey(metrics, "analysis_metadata")
        
        # Test aggregate metrics
        aggregate = metrics["aggregate_metrics"]
        @test aggregate["total_pre_tax_income"] == 205000.0  # 100000 + 105000
        @test aggregate["total_depreciation"] == 16000.0     # 8000 + 8000
        @test aggregate["total_amortization"] == 2000.0      # 2000 + 0
        
        # Test tax efficiency
        efficiency = metrics["tax_efficiency"]
        @test efficiency["effective_tax_rate"] > 0
        @test efficiency["tax_shield_value"] == 18000.0  # (16000 + 2000)
        @test haskey(efficiency, "average_effective_rate")
        @test haskey(efficiency, "tax_rate_volatility")
        
        # Test analysis metadata
        analysis = metrics["analysis_metadata"]
        @test analysis["periods_analyzed"] == 2
        @test analysis["entities_covered"] == 1
        @test analysis["stage_completed"] == "tax_processing"
    end
    
    @testset "Tax Rate Volatility" begin
        # Test stable rates
        stable_rates = [0.25, 0.25, 0.25, 0.25]
        @test calculate_tax_rate_volatility(stable_rates) == 0.0
        
        # Test volatile rates
        volatile_rates = [0.20, 0.30, 0.15, 0.35]
        volatility = calculate_tax_rate_volatility(volatile_rates)
        @test volatility > 0
        
        # Test insufficient data
        single_rate = [0.25]
        @test calculate_tax_rate_volatility(single_rate) == 0.0
        
        empty_rates = Float64[]
        @test calculate_tax_rate_volatility(empty_rates) == 0.0
    end
    
    @testset "Summarize Tax Adjustments" begin
        tax_adjustments = calculate_tax_adjustments(
            create_test_financing_adjustments(), 
            create_test_cash_flow_entries()
        )
        
        summary = summarize_tax_adjustments(tax_adjustments)
        
        @test haskey(summary, "summary_metrics")
        @test haskey(summary, "tax_deductions")
        @test haskey(summary, "period_coverage")
        
        # Test summary metrics
        summary_metrics = summary["summary_metrics"]
        @test summary_metrics["total_adjustments"] == 2
        @test summary_metrics["total_pre_tax_income"] == 205000.0
        @test summary_metrics["overall_effective_rate"] > 0
        
        # Test tax deductions
        deductions = summary["tax_deductions"]
        @test deductions["total_depreciation"] == 16000.0
        @test deductions["total_amortization"] == 2000.0
        @test deductions["total_other_adjustments"] == 3000.0
        @test deductions["total_deductions"] == 21000.0
        
        # Test period coverage
        period_coverage = summary["period_coverage"]
        @test period_coverage["start_date"] == Date(2024, 1, 1)
        @test period_coverage["end_date"] == Date(2024, 2, 28)
        @test period_coverage["entities_covered"] == 1
        
        @test summary["stage_completed"] == "tax_processing"
    end
    
    @testset "Tax Adjustment Validation" begin
        # Test valid adjustment
        valid_adjustment = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 8000.0, 2000.0, 0.0, 90000.0, 25200.0, 64800.0,
            Dict{String, Any}()
        )
        
        validation = validate_tax_adjustment(valid_adjustment)
        @test validation["is_valid"] == true
        @test length(validation["errors"]) == 0
        
        # Test adjustment with taxable income calculation error
        invalid_adjustment = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 8000.0, 2000.0, 0.0, 95000.0,  # Wrong taxable income
            25200.0, 64800.0, Dict{String, Any}()
        )
        
        validation_invalid = validate_tax_adjustment(invalid_adjustment)
        @test validation_invalid["is_valid"] == false
        @test length(validation_invalid["errors"]) > 0
        
        # Test adjustment with high effective tax rate (should generate warning)
        high_tax_adjustment = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, 0.0, 0.0, 0.0, 100000.0, 60000.0, 25000.0,  # 60% effective rate
            Dict{String, Any}()
        )
        
        validation_warning = validate_tax_adjustment(high_tax_adjustment)
        @test validation_warning["is_valid"] == true  # Valid but with warnings
        @test length(validation_warning["warnings"]) > 0
        @test any(w -> contains(w, "High effective tax rate"), validation_warning["warnings"])
        
        # Test adjustment with negative depreciation (should generate warning)
        negative_depreciation = TaxAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            100000.0, -5000.0, 2000.0, 0.0, 103000.0, 28840.0, 56160.0,
            Dict{String, Any}()
        )
        
        validation_neg_deprec = validate_tax_adjustment(negative_depreciation)
        @test validation_neg_deprec["is_valid"] == true
        @test any(w -> contains(w, "Negative depreciation"), validation_neg_deprec["warnings"])
    end
    
    @testset "Edge Cases" begin
        # Test with empty inputs
        empty_adjustments = calculate_tax_adjustments(FinancingAdjustment[], CashFlowEntry[])
        @test length(empty_adjustments) == 0
        
        empty_metrics = calculate_tax_metrics(TaxAdjustment[])
        @test haskey(empty_metrics, "message")
        
        empty_summary = summarize_tax_adjustments(TaxAdjustment[])
        @test haskey(empty_summary, "message")
        
        # Test with no tax items in cash flow entry
        no_tax_entry = CashFlowEntry(
            "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("revenue" => 100000.0), Dict("interest" => 15000.0),
            Dict{String, Float64}(), Dict{String, Float64}(),  # No tax items
            Dict{String, Any}()
        )
        
        financing_adj = FinancingAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("interest" => 15000.0), Dict{String, Float64}(),
            100000.0, 85000.0, Dict{String, Any}()
        )
        
        no_tax_adjustments = calculate_tax_adjustments([financing_adj], [no_tax_entry])
        @test length(no_tax_adjustments) == 1
        
        adj = no_tax_adjustments[1]
        @test adj.depreciation == 0.0
        @test adj.amortization == 0.0
        @test adj.other_tax_adjustments == 0.0
        @test adj.taxable_income == adj.pre_tax_income  # No deductions
    end
    
    @testset "Real Estate Tax Scenarios" begin
        # Test typical commercial real estate tax scenario
        commercial_financing = FinancingAdjustment(
            "office_building", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("interest" => 50000.0), Dict{String, Float64}(),
            200000.0, 150000.0, Dict{String, Any}()
        )
        
        commercial_cash_flow = CashFlowEntry(
            "office_building", "asset", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("Base Rent" => 250000.0, "Operating Expenses" => -50000.0),
            Dict("Interest Expense" => 50000.0), Dict{String, Float64}(),
            Dict(
                "Building Depreciation" => 15000.0,  # Typical building depreciation
                "FF&E Depreciation" => 3000.0,       # Furniture, fixtures, equipment
                "Property Tax" => 8000.0              # Property tax (not deductible from income tax)
            ),
            Dict{String, Any}()
        )
        
        tax_adjustments = calculate_tax_adjustments([commercial_financing], [commercial_cash_flow])
        @test length(tax_adjustments) == 1
        
        adj = tax_adjustments[1]
        @test adj.pre_tax_income == 200000.0
        @test adj.depreciation == 18000.0  # 15000 + 3000
        @test adj.amortization == 0.0
        @test adj.other_tax_adjustments == 8000.0  # Property tax
        
        # Calculate expected taxable income
        expected_taxable = 200000.0 - 18000.0 - 0.0 - 8000.0
        @test adj.taxable_income == expected_taxable
        
        # Check tax shield benefit
        tax_shield = adj.metadata["tax_shield_value"]
        expected_shield = 18000.0 * 0.28  # Only depreciation provides tax shield
        @test tax_shield == expected_shield
        
        # Check effective tax rate is reasonable for commercial real estate
        effective_rate = adj.metadata["effective_tax_rate"]
        @test effective_rate > 0.15 && effective_rate < 0.30  # Reasonable range
    end
end

println("✅ Tax Processing tests completed")