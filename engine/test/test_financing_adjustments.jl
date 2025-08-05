# Tests for Financing Adjustments Module (Stage 4)

using Test
using Dates
using CFDLEngine

@testset "Financing Adjustments Tests" begin
    
    # Test data setup
    function create_test_operating_statements()
        return [
            OperatingStatement(
                "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict("rent" => 100000.0), Dict("expenses" => 30000.0),
                70000.0, Dict{String, Any}()
            ),
            OperatingStatement(
                "deal1", Date(2024, 2, 1), Date(2024, 2, 28),
                Dict("rent" => 105000.0), Dict("expenses" => 32000.0),
                73000.0, Dict{String, Any}()
            )
        ]
    end
    
    function create_test_cash_flow_entries()
        return [
            CashFlowEntry(
                "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict("rent" => 100000.0, "expenses" => -30000.0),
                Dict("Interest Expense" => 15000.0, "Principal Payment" => 10000.0),
                Dict{String, Float64}(), Dict{String, Float64}(),
                Dict{String, Any}()
            ),
            CashFlowEntry(
                "deal1", "deal", Date(2024, 2, 1), Date(2024, 2, 28),
                Dict("rent" => 105000.0, "expenses" => -32000.0),
                Dict("Interest Expense" => 15000.0, "Principal Payment" => 10000.0, "Preferred Return" => 5000.0),
                Dict{String, Float64}(), Dict{String, Float64}(),
                Dict{String, Any}()
            )
        ]
    end
    
    @testset "FinancingAdjustment Creation" begin
        adjustment = FinancingAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("interest" => 15000.0), Dict("preferred" => 5000.0),
            70000.0, 50000.0, Dict("test" => true)
        )
        
        @test adjustment.entity_id == "deal1"
        @test adjustment.period_start == Date(2024, 1, 1)
        @test adjustment.period_end == Date(2024, 1, 31)
        @test adjustment.debt_service["interest"] == 15000.0
        @test adjustment.other_financing["preferred"] == 5000.0
        @test adjustment.unlevered_cf == 70000.0
        @test adjustment.levered_cf == 50000.0
        @test adjustment.metadata["test"] == true
    end
    
    @testset "Apply Financing Adjustments" begin
        operating_statements = create_test_operating_statements()
        cash_flow_entries = create_test_cash_flow_entries()
        
        adjustments = apply_financing_adjustments(operating_statements, cash_flow_entries)
        
        @test length(adjustments) == 2
        
        # Test first adjustment
        adj1 = adjustments[1]
        @test adj1.entity_id == "deal1"
        @test adj1.period_start == Date(2024, 1, 1)
        @test adj1.period_end == Date(2024, 1, 31)
        @test adj1.unlevered_cf == 70000.0  # From operating statement NOI
        
        # Check debt service categorization
        @test haskey(adj1.debt_service, "Interest Expense")
        @test haskey(adj1.debt_service, "Principal Payment")
        @test adj1.debt_service["Interest Expense"] == 15000.0
        @test adj1.debt_service["Principal Payment"] == 10000.0
        
        # Check other financing (should be empty for first period)
        @test length(adj1.other_financing) == 0
        
        # Check levered cash flow calculation
        total_debt_service = 15000.0 + 10000.0
        expected_levered_cf = 70000.0 - total_debt_service
        @test adj1.levered_cf == expected_levered_cf
        
        # Check metadata
        @test adj1.metadata["total_debt_service"] == total_debt_service
        @test adj1.metadata["total_other_financing"] == 0.0
        @test adj1.metadata["debt_service_coverage_ratio"] == 70000.0 / total_debt_service
        @test adj1.metadata["stage"] == "financing_adjustments"
        
        # Test second adjustment (has preferred return)
        adj2 = adjustments[2]
        @test adj2.unlevered_cf == 73000.0
        @test haskey(adj2.other_financing, "Preferred Return")
        @test adj2.other_financing["Preferred Return"] == 5000.0
        
        total_financing_2 = 25000.0 + 5000.0  # debt service + preferred return
        expected_levered_cf_2 = 73000.0 - total_financing_2
        @test adj2.levered_cf == expected_levered_cf_2
    end
    
    @testset "Debt Service Item Classification" begin
        # Test debt service items
        @test is_debt_service_item("Interest Expense") == true
        @test is_debt_service_item("Principal Payment") == true
        @test is_debt_service_item("Loan Fee") == true
        @test is_debt_service_item("Debt Service") == true
        @test is_debt_service_item("Mortgage Payment") == true
        @test is_debt_service_item("debt_servicing") == true
        
        # Test non-debt service items
        @test is_debt_service_item("Preferred Return") == false
        @test is_debt_service_item("Management Fee") == false
        @test is_debt_service_item("Other Financing") == false
        @test is_debt_service_item("Revenue") == false
    end
    
    @testset "Calculate Debt Metrics" begin
        operating_statements = create_test_operating_statements()
        cash_flow_entries = create_test_cash_flow_entries()
        adjustments = apply_financing_adjustments(operating_statements, cash_flow_entries)
        
        metrics = calculate_debt_metrics(adjustments)
        
        @test haskey(metrics, "aggregate_metrics")
        @test haskey(metrics, "key_ratios")
        @test haskey(metrics, "analysis_metadata")
        
        # Test aggregate metrics
        aggregate = metrics["aggregate_metrics"]
        @test aggregate["total_unlevered_cf"] == 143000.0  # 70000 + 73000
        @test aggregate["total_debt_service"] == 50000.0   # 25000 + 25000
        @test aggregate["total_other_financing"] == 5000.0  # 0 + 5000
        
        # Test key ratios
        ratios = metrics["key_ratios"]
        expected_dscr = 143000.0 / 50000.0
        @test abs(ratios["debt_service_coverage_ratio"] - expected_dscr) < 0.001
        
        expected_leverage_factor = (143000.0 - 55000.0) / 143000.0  # (unlevered - total_financing) / unlevered
        @test abs(ratios["leverage_factor"] - expected_leverage_factor) < 0.001
        
        # Test analysis metadata
        analysis = metrics["analysis_metadata"]
        @test analysis["periods_analyzed"] == 2
        @test analysis["periods_with_debt_service"] == 2
        @test haskey(analysis, "dscr_trend")
    end
    
    @testset "DSCR Trend Analysis" begin
        # Test improving trend
        improving_dscrs = [1.2, 1.4, 1.6, 1.8]
        @test calculate_dscr_trend(improving_dscrs) == "improving"
        
        # Test declining trend
        declining_dscrs = [2.0, 1.8, 1.6, 1.4]
        @test calculate_dscr_trend(declining_dscrs) == "declining"
        
        # Test stable trend
        stable_dscrs = [1.5, 1.6, 1.4, 1.5]
        @test calculate_dscr_trend(stable_dscrs) == "stable"
        
        # Test insufficient data
        single_dscr = [1.5]
        @test calculate_dscr_trend(single_dscr) == "insufficient_data"
        
        empty_dscrs = Float64[]
        @test calculate_dscr_trend(empty_dscrs) == "insufficient_data"
    end
    
    @testset "Summarize Financing Adjustments" begin
        operating_statements = create_test_operating_statements()
        cash_flow_entries = create_test_cash_flow_entries()
        adjustments = apply_financing_adjustments(operating_statements, cash_flow_entries)
        
        summary = summarize_financing_adjustments(adjustments)
        
        @test haskey(summary, "summary_metrics")
        @test haskey(summary, "financing_breakdown")
        @test haskey(summary, "coverage_analysis")
        @test haskey(summary, "period_coverage")
        
        # Test summary metrics
        summary_metrics = summary["summary_metrics"]
        @test summary_metrics["total_adjustments"] == 2
        @test summary_metrics["total_unlevered_cf"] == 143000.0
        @test summary_metrics["leverage_impact"] == 55000.0  # Total financing costs
        
        # Test financing breakdown
        breakdown = summary["financing_breakdown"]
        @test breakdown["total_debt_service"] == 50000.0
        @test breakdown["total_other_financing"] == 5000.0
        
        debt_service_items = breakdown["debt_service_items"]
        @test debt_service_items["Interest Expense"] == 30000.0  # 15000 + 15000
        @test debt_service_items["Principal Payment"] == 20000.0  # 10000 + 10000
        
        other_financing_items = breakdown["other_financing_items"]
        @test other_financing_items["Preferred Return"] == 5000.0
        
        # Test period coverage
        period_coverage = summary["period_coverage"]
        @test period_coverage["start_date"] == Date(2024, 1, 1)
        @test period_coverage["end_date"] == Date(2024, 2, 28)
        @test period_coverage["entities_covered"] == 1
        
        @test summary["stage_completed"] == "financing_adjustments"
    end
    
    @testset "Financing Adjustment Validation" begin
        # Test valid adjustment
        valid_adjustment = FinancingAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("interest" => 15000.0), Dict{String, Float64}(),
            70000.0, 55000.0, Dict{String, Any}()
        )
        
        validation = validate_financing_adjustment(valid_adjustment)
        @test validation["is_valid"] == true
        @test length(validation["errors"]) == 0
        
        # Test adjustment with calculation error
        invalid_adjustment = FinancingAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("interest" => 15000.0), Dict{String, Float64}(),
            70000.0, 60000.0,  # Wrong levered CF (should be 55000)
            Dict{String, Any}()
        )
        
        validation_invalid = validate_financing_adjustment(invalid_adjustment)
        @test validation_invalid["is_valid"] == false
        @test length(validation_invalid["errors"]) > 0
        
        # Test adjustment with low DSCR (should generate warning)
        low_dscr_adjustment = FinancingAdjustment(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("interest" => 60000.0), Dict{String, Float64}(),
            70000.0, 10000.0, Dict{String, Any}()
        )
        
        validation_warning = validate_financing_adjustment(low_dscr_adjustment)
        @test validation_warning["is_valid"] == true  # Valid but with warnings
        @test length(validation_warning["warnings"]) > 0
        @test any(w -> contains(w, "Low DSCR"), validation_warning["warnings"])
    end
    
    @testset "Edge Cases" begin
        # Test with no financing items
        no_financing_entry = CashFlowEntry(
            "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("rent" => 100000.0), Dict{String, Float64}(),  # No financing items
            Dict{String, Float64}(), Dict{String, Float64}(),
            Dict{String, Any}()
        )
        
        operating_stmt = OperatingStatement(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("rent" => 100000.0), Dict{String, Float64}(),
            100000.0, Dict{String, Any}()
        )
        
        adjustments = apply_financing_adjustments([operating_stmt], [no_financing_entry])
        @test length(adjustments) == 1
        
        adj = adjustments[1]
        @test adj.unlevered_cf == 100000.0
        @test adj.levered_cf == 100000.0  # No financing costs
        @test length(adj.debt_service) == 0
        @test length(adj.other_financing) == 0
        
        # Test with empty inputs
        empty_adjustments = apply_financing_adjustments(OperatingStatement[], CashFlowEntry[])
        @test length(empty_adjustments) == 0
        
        empty_metrics = calculate_debt_metrics(FinancingAdjustment[])
        @test haskey(empty_metrics, "message")
        
        empty_summary = summarize_financing_adjustments(FinancingAdjustment[])
        @test haskey(empty_summary, "message")
        
        # Test fallback NOI calculation when no operating statement
        no_op_stmt_entry = CashFlowEntry(
            "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("revenue" => 100000.0, "expense" => -30000.0),
            Dict("interest" => 15000.0), Dict{String, Float64}(), Dict{String, Float64}(),
            Dict{String, Any}()
        )
        
        fallback_adjustments = apply_financing_adjustments(OperatingStatement[], [no_op_stmt_entry])
        @test length(fallback_adjustments) == 1
        @test fallback_adjustments[1].unlevered_cf == 70000.0  # Calculated from entry
    end
    
    @testset "Real Estate Industry Standards" begin
        # Test typical commercial real estate financing structure
        commercial_entry = CashFlowEntry(
            "office_building", "asset", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("Base Rent" => 200000.0, "Operating Expenses" => -80000.0),
            Dict(
                "Interest Expense" => 35000.0,
                "Principal Payment" => 15000.0,
                "Loan Origination Fee" => 2000.0
            ),
            Dict{String, Float64}(), Dict{String, Float64}(),
            Dict{String, Any}()
        )
        
        operating_stmt = OperatingStatement(
            "office_building", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("Base Rent" => 200000.0), Dict("Operating Expenses" => 80000.0),
            120000.0, Dict{String, Any}()
        )
        
        adjustments = apply_financing_adjustments([operating_stmt], [commercial_entry])
        @test length(adjustments) == 1
        
        adj = adjustments[1]
        @test adj.unlevered_cf == 120000.0
        
        # Check debt service items
        @test adj.debt_service["Interest Expense"] == 35000.0
        @test adj.debt_service["Principal Payment"] == 15000.0
        @test adj.debt_service["Loan Origination Fee"] == 2000.0
        
        total_debt_service = 52000.0
        expected_levered_cf = 120000.0 - total_debt_service
        @test adj.levered_cf == expected_levered_cf
        
        # Check DSCR (should be healthy for commercial real estate)
        dscr = adj.metadata["debt_service_coverage_ratio"]
        @test dscr > 1.20  # Typical minimum DSCR for commercial real estate
        @test dscr ≈ 120000.0 / 52000.0 atol=0.001
    end
end

println("✅ Financing Adjustments tests completed")