# Tests for Statement Views Module (Stage 7)

using Test
using Dates
using CFDLEngine

@testset "Statement Views Tests" begin
    
    # Test data setup
    function create_test_available_cash_calculations()
        return [
            AvailableCashCalculation(
                "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
                75000.0, 2250.0, 0.0, 375.0, 72375.0,
                Dict{String, Any}()
            ),
            AvailableCashCalculation(
                "deal1", Date(2024, 2, 1), Date(2024, 2, 28),
                78000.0, 2340.0, 0.0, 390.0, 75270.0,
                Dict{String, Any}()
            ),
            AvailableCashCalculation(
                "deal1", Date(2024, 3, 1), Date(2024, 3, 31),
                80000.0, 2400.0, 0.0, 400.0, 77200.0,
                Dict{String, Any}()
            )
        ]
    end
    
    @testset "Enum Definitions" begin
        @test GAAP isa StatementView
        @test NON_GAAP isa StatementView
        @test TAX isa StatementView
        @test MANAGEMENT isa StatementView
        
        @test MONTHLY isa ReportingFrequency
        @test QUARTERLY isa ReportingFrequency
        @test ANNUAL isa ReportingFrequency
        @test CUMULATIVE isa ReportingFrequency
    end
    
    @testset "FinancialStatement Creation" begin
        statement = FinancialStatement(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            GAAP, MONTHLY, 100000.0, -25000.0, -5000.0, 70000.0,
            Dict("noi" => 120000.0, "dscr" => 2.4),
            Dict("revenue" => 150000.0, "expenses" => 50000.0),
            Dict("test" => true)
        )
        
        @test statement.entity_id == "deal1"
        @test statement.period_start == Date(2024, 1, 1)
        @test statement.period_end == Date(2024, 1, 31)
        @test statement.view_type == GAAP
        @test statement.frequency == MONTHLY
        @test statement.operating_cash_flow == 100000.0
        @test statement.financing_cash_flow == -25000.0
        @test statement.investing_cash_flow == -5000.0
        @test statement.net_cash_flow == 70000.0
        @test statement.key_metrics["noi"] == 120000.0
        @test statement.line_items["revenue"] == 150000.0
        @test statement.metadata["test"] == true
    end
    
    @testset "Generate Statement Views" begin
        calculations = create_test_available_cash_calculations()
        statement_views = generate_statement_views(calculations)
        
        @test haskey(statement_views, "gaap")
        @test haskey(statement_views, "non_gaap")
        @test haskey(statement_views, "tax")
        @test haskey(statement_views, "management")
        
        # Each view should have statements for each calculation
        @test length(statement_views["gaap"]) == 3
        @test length(statement_views["non_gaap"]) == 3
        @test length(statement_views["tax"]) == 3
        @test length(statement_views["management"]) == 3
        
        # Check that each view has correct view type
        @test all(s.view_type == GAAP for s in statement_views["gaap"])
        @test all(s.view_type == NON_GAAP for s in statement_views["non_gaap"])
        @test all(s.view_type == TAX for s in statement_views["tax"])
        @test all(s.view_type == MANAGEMENT for s in statement_views["management"])
    end
    
    @testset "Generate GAAP Statements" begin
        calculations = create_test_available_cash_calculations()
        gaap_statements = generate_gaap_statements(calculations)
        
        @test length(gaap_statements) == 3
        
        # Test first statement
        stmt1 = gaap_statements[1]
        @test stmt1.entity_id == "deal1"
        @test stmt1.view_type == GAAP
        @test stmt1.frequency == MONTHLY
        
        # Check cash flow categorization
        @test stmt1.operating_cash_flow == 75000.0  # After-tax CF
        @test stmt1.financing_cash_flow == 0.0      # Simplified
        @test stmt1.investing_cash_flow == -2250.0  # Capital reserves (negative for outflows)
        
        # Check key metrics
        @test haskey(stmt1.key_metrics, "net_income")
        @test haskey(stmt1.key_metrics, "operating_cash_flow")
        @test haskey(stmt1.key_metrics, "free_cash_flow")
        @test stmt1.key_metrics["free_cash_flow"] == 72375.0  # Available for distribution
        
        # Check line items
        @test haskey(stmt1.line_items, "cash_flows_from_operating_activities")
        @test haskey(stmt1.line_items, "capital_expenditures")
        @test stmt1.line_items["capital_expenditures"] == 2250.0
        
        # Check metadata
        @test stmt1.metadata["gaap_compliant"] == true
        @test stmt1.metadata["includes_non_cash_items"] == true
        @test stmt1.metadata["stage"] == "statement_views_gaap"
    end
    
    @testset "Generate Non-GAAP Statements" begin
        calculations = create_test_available_cash_calculations()
        non_gaap_statements = generate_non_gaap_statements(calculations)
        
        @test length(non_gaap_statements) == 3
        
        # Test first statement
        stmt1 = non_gaap_statements[1]
        @test stmt1.view_type == NON_GAAP
        @test stmt1.operating_cash_flow == 75000.0
        @test stmt1.net_cash_flow == 72375.0  # Available for distribution
        
        # Check Non-GAAP specific metrics
        @test haskey(stmt1.key_metrics, "net_operating_income")
        @test haskey(stmt1.key_metrics, "available_for_distribution")
        @test haskey(stmt1.key_metrics, "funds_from_operations")
        @test haskey(stmt1.key_metrics, "adjusted_funds_from_operations")
        @test haskey(stmt1.key_metrics, "cash_on_cash_return")
        @test haskey(stmt1.key_metrics, "distribution_coverage_ratio")
        
        @test stmt1.key_metrics["available_for_distribution"] == 72375.0
        @test stmt1.key_metrics["funds_from_operations"] == 75000.0
        @test stmt1.key_metrics["adjusted_funds_from_operations"] == 72375.0
        
        # Check line items
        @test haskey(stmt1.line_items, "funds_from_operations")
        @test haskey(stmt1.line_items, "distributions_to_investors")
        @test stmt1.line_items["distributions_to_investors"] == 72375.0
        
        # Check metadata
        @test stmt1.metadata["excludes_non_cash_items"] == true
        @test stmt1.metadata["economic_view"] == true
        @test stmt1.metadata["real_estate_focused"] == true
        @test stmt1.metadata["stage"] == "statement_views_non_gaap"
    end
    
    @testset "Generate Tax Statements" begin
        calculations = create_test_available_cash_calculations()
        tax_statements = generate_tax_statements(calculations)
        
        @test length(tax_statements) == 3
        
        # Test first statement
        stmt1 = tax_statements[1]
        @test stmt1.view_type == TAX
        @test stmt1.operating_cash_flow == 75000.0
        @test stmt1.net_cash_flow == 75000.0  # After-tax CF
        
        # Check tax-specific metrics
        @test haskey(stmt1.key_metrics, "taxable_income")
        @test haskey(stmt1.key_metrics, "tax_expense")
        @test haskey(stmt1.key_metrics, "effective_tax_rate")
        @test haskey(stmt1.key_metrics, "tax_shield_benefit")
        @test haskey(stmt1.key_metrics, "after_tax_cash_flow")
        
        @test stmt1.key_metrics["after_tax_cash_flow"] == 75000.0
        
        # Check tax line items
        @test haskey(stmt1.line_items, "pre_tax_income")
        @test haskey(stmt1.line_items, "depreciation_deduction")
        @test haskey(stmt1.line_items, "taxable_income")
        @test haskey(stmt1.line_items, "income_tax_expense")
        
        # Check metadata
        @test stmt1.metadata["tax_focused"] == true
        @test stmt1.metadata["includes_tax_benefits"] == true
        @test stmt1.metadata["stage"] == "statement_views_tax"
    end
    
    @testset "Generate Management Statements" begin
        calculations = create_test_available_cash_calculations()
        management_statements = generate_management_statements(calculations)
        
        @test length(management_statements) == 3
        
        # Test first statement
        stmt1 = management_statements[1]
        @test stmt1.view_type == MANAGEMENT
        @test stmt1.operating_cash_flow == 75000.0
        @test stmt1.net_cash_flow == 72375.0  # Available for distribution
        
        # Check management-specific metrics
        @test haskey(stmt1.key_metrics, "available_for_distribution")
        @test haskey(stmt1.key_metrics, "cash_flow_stability")
        @test haskey(stmt1.key_metrics, "reserve_adequacy")
        @test haskey(stmt1.key_metrics, "operational_efficiency")
        @test haskey(stmt1.key_metrics, "liquidity_ratio")
        
        @test stmt1.key_metrics["available_for_distribution"] == 72375.0
        
        # Check management line items
        @test haskey(stmt1.line_items, "operating_cash_generation")
        @test haskey(stmt1.line_items, "required_reserves")
        @test haskey(stmt1.line_items, "discretionary_cash")
        @test stmt1.line_items["discretionary_cash"] == 72375.0
        @test stmt1.line_items["required_reserves"] == 2250.0
        
        # Check metadata
        @test stmt1.metadata["internal_reporting"] == true
        @test stmt1.metadata["management_focused"] == true
        @test stmt1.metadata["stage"] == "statement_views_management"
    end
    
    @testset "Aggregate to Annual" begin
        calculations = create_test_available_cash_calculations()
        monthly_statements = generate_non_gaap_statements(calculations)
        annual_statements = aggregate_to_annual(monthly_statements)
        
        @test length(annual_statements) == 1  # All statements are from 2024
        
        annual_stmt = annual_statements[1]
        @test annual_stmt.entity_id == "deal1"
        @test annual_stmt.view_type == NON_GAAP
        @test annual_stmt.frequency == ANNUAL
        @test annual_stmt.period_start == Date(2024, 1, 1)
        @test annual_stmt.period_end == Date(2024, 12, 31)
        
        # Check aggregated cash flows
        expected_operating = 75000.0 + 78000.0 + 80000.0
        @test annual_stmt.operating_cash_flow == expected_operating
        
        expected_net = 72375.0 + 75270.0 + 77200.0
        @test annual_stmt.net_cash_flow == expected_net
        
        # Check aggregated metrics
        @test annual_stmt.key_metrics["available_for_distribution"] == expected_net
        @test annual_stmt.key_metrics["funds_from_operations"] == expected_operating
        
        # Check metadata
        @test annual_stmt.metadata["annual_aggregation"] == true
        @test annual_stmt.metadata["periods_included"] == 3
        @test annual_stmt.metadata["year"] == 2024
    end
    
    @testset "Helper Functions" begin
        calc = create_test_available_cash_calculations()[1]
        
        # Test cash-on-cash return calculation
        cash_on_cash = calculate_cash_on_cash_return(calc)
        @test cash_on_cash == 72375.0  # Simplified implementation
        
        # Test distribution coverage calculation
        coverage = calculate_distribution_coverage(calc)
        expected_coverage = 72375.0 / 75000.0
        @test abs(coverage - expected_coverage) < 0.001
        
        # Test taxable income estimation
        taxable = estimate_taxable_income(calc)
        expected_taxable = 75000.0 / 0.75  # Assuming 25% tax rate
        @test abs(taxable - expected_taxable) < 0.001
        
        # Test tax expense estimation
        tax_expense = estimate_tax_expense(calc)
        expected_tax = taxable * 0.25
        @test abs(tax_expense - expected_tax) < 0.001
        
        # Test effective tax rate calculation
        effective_rate = calculate_effective_tax_rate(calc)
        @test abs(effective_rate - 0.25) < 0.001
    end
    
    @testset "Summarize Statement Views" begin
        calculations = create_test_available_cash_calculations()
        statement_views = generate_statement_views(calculations)
        summary = summarize_statement_views(statement_views)
        
        @test haskey(summary, "gaap")
        @test haskey(summary, "non_gaap")
        @test haskey(summary, "tax")
        @test haskey(summary, "management")
        @test haskey(summary, "stage_completed")
        @test haskey(summary, "total_view_types")
        
        @test summary["stage_completed"] == "statement_views"
        @test summary["total_view_types"] == 4
        
        # Check individual view summaries
        gaap_summary = summary["gaap"]
        @test gaap_summary["statement_count"] == 3
        @test gaap_summary["total_operating_cf"] == 233000.0  # 75000 + 78000 + 80000
        @test gaap_summary["entities_covered"] == 1
        @test haskey(gaap_summary, "period_coverage")
        
        period_coverage = gaap_summary["period_coverage"]
        @test period_coverage["start_date"] == Date(2024, 1, 1)
        @test period_coverage["end_date"] == Date(2024, 3, 31)
    end
    
    @testset "Edge Cases" begin
        # Test with empty calculations
        empty_views = generate_statement_views(AvailableCashCalculation[])
        @test haskey(empty_views, "gaap")
        @test length(empty_views["gaap"]) == 0
        @test length(empty_views["non_gaap"]) == 0
        @test length(empty_views["tax"]) == 0
        @test length(empty_views["management"]) == 0
        
        empty_summary = summarize_statement_views(empty_views)
        @test empty_summary["gaap"]["message"] == "No statements available"
        
        # Test annual aggregation with empty statements
        empty_annual = aggregate_to_annual(FinancialStatement[])
        @test length(empty_annual) == 0
        
        # Test annual aggregation with mixed years
        mixed_year_calcs = [
            AvailableCashCalculation(
                "deal1", Date(2023, 12, 1), Date(2023, 12, 31),
                50000.0, 1500.0, 0.0, 250.0, 48250.0, Dict{String, Any}()
            ),
            AvailableCashCalculation(
                "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
                75000.0, 2250.0, 0.0, 375.0, 72375.0, Dict{String, Any}()
            )
        ]
        
        mixed_statements = generate_non_gaap_statements(mixed_year_calcs)
        mixed_annual = aggregate_to_annual(mixed_statements)
        
        @test length(mixed_annual) == 2  # One for 2023, one for 2024
        @test any(s.period_start == Date(2023, 1, 1) for s in mixed_annual)
        @test any(s.period_start == Date(2024, 1, 1) for s in mixed_annual)
    end
    
    @testset "Multi-Entity Scenarios" begin
        # Test with multiple entities
        multi_entity_calcs = [
            AvailableCashCalculation(
                "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
                75000.0, 2250.0, 0.0, 375.0, 72375.0, Dict{String, Any}()
            ),
            AvailableCashCalculation(
                "deal2", Date(2024, 1, 1), Date(2024, 1, 31),
                50000.0, 1500.0, 0.0, 250.0, 48250.0, Dict{String, Any}()
            )
        ]
        
        multi_views = generate_statement_views(multi_entity_calcs)
        
        # Each view should have statements for both entities
        @test length(multi_views["gaap"]) == 2
        @test length(multi_views["non_gaap"]) == 2
        
        # Check that both entities are represented
        gaap_entities = Set(s.entity_id for s in multi_views["gaap"])
        @test "deal1" in gaap_entities
        @test "deal2" in gaap_entities
        
        # Test annual aggregation with multiple entities
        multi_annual = aggregate_to_annual(multi_views["non_gaap"])
        @test length(multi_annual) == 2  # One for each entity
        
        # Check that entities are kept separate
        annual_entities = Set(s.entity_id for s in multi_annual)
        @test "deal1" in annual_entities
        @test "deal2" in annual_entities
    end
    
    @testset "Real Estate Industry Views" begin
        # Test with realistic commercial real estate numbers
        commercial_calc = AvailableCashCalculation(
            "office_building", Date(2024, 1, 1), Date(2024, 1, 31),
            150000.0, 4500.0, 0.0, 750.0, 144750.0,
            Dict{String, Any}()
        )
        
        views = generate_statement_views([commercial_calc])
        
        # Test Non-GAAP view (most relevant for real estate investors)
        non_gaap = views["non_gaap"][1]
        
        # Check key real estate metrics
        @test non_gaap.key_metrics["net_operating_income"] > 0
        @test non_gaap.key_metrics["funds_from_operations"] == 150000.0
        @test non_gaap.key_metrics["adjusted_funds_from_operations"] == 144750.0
        @test non_gaap.key_metrics["available_for_distribution"] == 144750.0
        
        # Distribution coverage should be reasonable for commercial real estate
        coverage = non_gaap.key_metrics["distribution_coverage_ratio"]
        @test coverage > 0.9 && coverage < 1.0  # Typical range after reserves
        
        # Test Management view (internal reporting)
        management = views["management"][1]
        
        # Should have operational efficiency metrics
        @test haskey(management.key_metrics, "operational_efficiency")
        @test haskey(management.key_metrics, "liquidity_ratio")
        @test haskey(management.key_metrics, "reserve_adequacy")
        
        # Reserve adequacy should be reasonable
        reserve_adequacy = management.key_metrics["reserve_adequacy"]
        @test reserve_adequacy > 0.02 && reserve_adequacy < 0.05  # 2-5% is typical
    end
end

println("✅ Statement Views tests completed")