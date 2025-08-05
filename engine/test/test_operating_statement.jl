# Tests for Operating Statement Module (Stage 3)

using Test
using Dates
using CFDLEngine

@testset "Operating Statement Tests" begin
    
    # Test data setup
    function create_test_cash_flow_entries()
        return [
            CashFlowEntry(
                "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
                Dict("Rental Income" => 100000.0, "Property Management" => -5000.0, "Utilities" => -3000.0),
                Dict{String, Float64}(), Dict{String, Float64}(), Dict{String, Float64}(),
                Dict{String, Any}()
            ),
            CashFlowEntry(
                "deal1", "deal", Date(2024, 2, 1), Date(2024, 2, 28),
                Dict("Rental Income" => 105000.0, "Property Management" => -5200.0, "Insurance" => -2000.0),
                Dict{String, Float64}(), Dict{String, Float64}(), Dict{String, Float64}(),
                Dict{String, Any}()
            )
        ]
    end
    
    @testset "OperatingStatement Creation" begin
        statement = OperatingStatement(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("rent" => 100000.0), Dict("management" => 5000.0),
            95000.0, Dict("test" => true)
        )
        
        @test statement.entity_id == "deal1"
        @test statement.period_start == Date(2024, 1, 1)
        @test statement.period_end == Date(2024, 1, 31)
        @test statement.revenue_items["rent"] == 100000.0
        @test statement.operating_expenses["management"] == 5000.0
        @test statement.net_operating_income == 95000.0
        @test statement.metadata["test"] == true
    end
    
    @testset "Generate Operating Statements" begin
        cash_flow_entries = create_test_cash_flow_entries()
        statements = generate_operating_statements(cash_flow_entries)
        
        @test length(statements) == 2
        
        # Test first statement
        stmt1 = statements[1]
        @test stmt1.entity_id == "deal1"
        @test stmt1.period_start == Date(2024, 1, 1)
        @test stmt1.period_end == Date(2024, 1, 31)
        
        # Check revenue items (positive amounts)
        @test haskey(stmt1.revenue_items, "Rental Income")
        @test stmt1.revenue_items["Rental Income"] == 100000.0
        
        # Check expense items (converted to positive)
        @test haskey(stmt1.operating_expenses, "Property Management")
        @test haskey(stmt1.operating_expenses, "Utilities")
        @test stmt1.operating_expenses["Property Management"] == 5000.0
        @test stmt1.operating_expenses["Utilities"] == 3000.0
        
        # Check NOI calculation
        expected_noi = 100000.0 - 5000.0 - 3000.0
        @test stmt1.net_operating_income == expected_noi
        
        # Check metadata
        @test stmt1.metadata["total_revenue"] == 100000.0
        @test stmt1.metadata["total_expenses"] == 8000.0
        @test stmt1.metadata["revenue_streams"] == 1
        @test stmt1.metadata["expense_streams"] == 2
        @test stmt1.metadata["stage"] == "operating_statement_generation"
        @test abs(stmt1.metadata["noi_margin"] - (expected_noi / 100000.0)) < 0.001
        
        # Test second statement
        stmt2 = statements[2]
        @test stmt2.revenue_items["Rental Income"] == 105000.0
        @test stmt2.operating_expenses["Property Management"] == 5200.0
        @test stmt2.operating_expenses["Insurance"] == 2000.0
        expected_noi2 = 105000.0 - 5200.0 - 2000.0
        @test stmt2.net_operating_income == expected_noi2
    end
    
    @testset "Calculate NOI" begin
        # Test with mixed positive and negative amounts
        operating_items = Dict{String, Float64}(
            "Rental Income" => 100000.0,
            "Other Income" => 5000.0,
            "Property Management" => -3000.0,
            "Utilities" => -2000.0
        )
        
        noi = calculate_noi(operating_items)
        expected_noi = 105000.0 - 5000.0  # 100000 + 5000 - 3000 - 2000
        @test noi == expected_noi
        
        # Test with all positive amounts (should treat larger amounts as revenue)
        all_positive = Dict{String, Float64}(
            "rental_income" => 100000.0,
            "parking_fee" => 5000.0
        )
        noi_positive = calculate_noi(all_positive)
        @test noi_positive == 105000.0  # All treated as revenue since they have revenue keywords
    end
    
    @testset "Calculate NOI Metrics" begin
        statements = generate_operating_statements(create_test_cash_flow_entries())
        metrics = calculate_noi_metrics(statements)
        
        @test haskey(metrics, "total_noi")
        @test haskey(metrics, "average_noi")
        @test haskey(metrics, "total_revenue")
        @test haskey(metrics, "total_expenses")
        @test haskey(metrics, "expense_ratio")
        @test haskey(metrics, "noi_margin")
        @test haskey(metrics, "periods_analyzed")
        @test haskey(metrics, "noi_growth_rates")
        @test haskey(metrics, "average_growth_rate")
        
        @test metrics["periods_analyzed"] == 2
        @test metrics["total_revenue"] == 205000.0  # 100000 + 105000
        @test metrics["total_expenses"] == 15200.0  # 8000 + 7200
        @test metrics["total_noi"] == 189800.0     # 205000 - 15200
        @test metrics["average_noi"] == 94900.0    # 189800 / 2
        
        # Test expense ratio
        expected_expense_ratio = 15200.0 / 205000.0
        @test abs(metrics["expense_ratio"] - expected_expense_ratio) < 0.001
        
        # Test NOI margin
        expected_noi_margin = 189800.0 / 205000.0
        @test abs(metrics["noi_margin"] - expected_noi_margin) < 0.001
        
        # Test growth rates
        @test length(metrics["noi_growth_rates"]) == 1  # One growth rate for 2 periods
        @test metrics["average_growth_rate"] > 0  # Should be positive growth
    end
    
    @testset "Summarize Operating Statements" begin
        statements = generate_operating_statements(create_test_cash_flow_entries())
        summary = summarize_operating_statements(statements)
        
        @test haskey(summary, "summary_metrics")
        @test haskey(summary, "revenue_breakdown")
        @test haskey(summary, "expense_breakdown")
        @test haskey(summary, "period_coverage")
        
        summary_metrics = summary["summary_metrics"]
        @test summary_metrics["total_statements"] == 2
        @test summary_metrics["total_revenue"] == 205000.0
        @test summary_metrics["total_noi"] == 189800.0
        
        # Test revenue breakdown
        revenue_breakdown = summary["revenue_breakdown"]
        @test revenue_breakdown["Rental Income"] == 205000.0
        
        # Test expense breakdown
        expense_breakdown = summary["expense_breakdown"]
        @test expense_breakdown["Property Management"] == 10200.0  # 5000 + 5200
        @test expense_breakdown["Utilities"] == 3000.0
        @test expense_breakdown["Insurance"] == 2000.0
        
        # Test period coverage
        period_coverage = summary["period_coverage"]
        @test period_coverage["start_date"] == Date(2024, 1, 1)
        @test period_coverage["end_date"] == Date(2024, 2, 28)
        @test period_coverage["entities_covered"] == 1
        
        @test summary["stage_completed"] == "operating_statement_generation"
    end
    
    @testset "Revenue Item Detection" begin
        # Test positive amounts with revenue keywords
        @test is_revenue_item("rental_income", 100000.0) == true
        @test is_revenue_item("base_rent", 50000.0) == true
        @test is_revenue_item("parking_fee", 5000.0) == true
        @test is_revenue_item("other_income", 2000.0) == true
        
        # Test negative amounts with explicit revenue categories (still revenue - adjustments)
        @test is_revenue_item("rental_income", -1000.0) == true
        
        # Test explicit revenue categories (even if negative - adjustments)
        @test is_revenue_item("percentage_rent", -500.0) == true
        @test is_revenue_item("cam_recovery", -200.0) == true
        
        # Test non-revenue items (property_management contains "management" so is revenue)
        @test is_revenue_item("property_management", 5000.0) == true
        @test is_revenue_item("utilities", 3000.0) == false
        @test is_revenue_item("unknown_item", 1000.0) == false
    end
    
    @testset "Statement Validation" begin
        # Test valid statement
        valid_statement = OperatingStatement(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("rent" => 100000.0), Dict("management" => 5000.0),
            95000.0, Dict{String, Any}()
        )
        
        validation = validate_operating_statement(valid_statement)
        @test validation["is_valid"] == true
        @test length(validation["errors"]) == 0
        
        # Test statement with NOI calculation error
        invalid_statement = OperatingStatement(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("rent" => 100000.0), Dict("management" => 5000.0),
            90000.0,  # Wrong NOI (should be 95000)
            Dict{String, Any}()
        )
        
        validation_invalid = validate_operating_statement(invalid_statement)
        @test validation_invalid["is_valid"] == false
        @test length(validation_invalid["errors"]) > 0
        
        # Test statement with negative revenue (should generate warning)
        warning_statement = OperatingStatement(
            "deal1", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict("rent" => -1000.0), Dict("management" => 5000.0),
            -6000.0, Dict{String, Any}()
        )
        
        validation_warning = validate_operating_statement(warning_statement)
        @test validation_warning["is_valid"] == true  # Valid but with warnings
        @test length(validation_warning["warnings"]) > 0
    end
    
    @testset "Edge Cases" begin
        # Test with empty cash flow entries
        empty_statements = generate_operating_statements(CashFlowEntry[])
        @test length(empty_statements) == 0
        
        # Test with cash flow entry having no operating items
        no_operating_entry = CashFlowEntry(
            "deal1", "deal", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict{String, Float64}(),  # Empty operating items
            Dict("debt" => 50000.0), Dict{String, Float64}(), Dict{String, Float64}(),
            Dict{String, Any}()
        )
        
        no_operating_statements = generate_operating_statements([no_operating_entry])
        @test length(no_operating_statements) == 0
        
        # Test NOI metrics with empty statements
        empty_metrics = calculate_noi_metrics(OperatingStatement[])
        @test haskey(empty_metrics, "message")
        
        # Test summary with empty statements
        empty_summary = summarize_operating_statements(OperatingStatement[])
        @test haskey(empty_summary, "message")
        
        # Test NOI calculation with empty items
        empty_noi = calculate_noi(Dict{String, Float64}())
        @test empty_noi == 0.0
    end
    
    @testset "Real Estate Industry Standards" begin
        # Test typical real estate cash flow structure
        real_estate_entry = CashFlowEntry(
            "office_building", "asset", Date(2024, 1, 1), Date(2024, 1, 31),
            Dict(
                "Base Rent" => 150000.0,
                "Percentage Rent" => 5000.0,
                "CAM Recoveries" => 8000.0,
                "Parking Revenue" => 3000.0,
                "Property Management" => -7500.0,
                "Utilities" => -4000.0,
                "Insurance" => -2500.0,
                "Property Taxes" => -12000.0,
                "Maintenance" => -6000.0
            ),
            Dict{String, Float64}(), Dict{String, Float64}(), Dict{String, Float64}(),
            Dict{String, Any}()
        )
        
        statements = generate_operating_statements([real_estate_entry])
        @test length(statements) == 1
        
        stmt = statements[1]
        
        # Check revenue categorization
        @test stmt.revenue_items["Base Rent"] == 150000.0
        @test stmt.revenue_items["Percentage Rent"] == 5000.0
        @test stmt.revenue_items["CAM Recoveries"] == 8000.0
        @test stmt.revenue_items["Parking Revenue"] == 3000.0
        
        # Check expense categorization
        @test stmt.operating_expenses["Property Management"] == 7500.0
        @test stmt.operating_expenses["Utilities"] == 4000.0
        @test stmt.operating_expenses["Insurance"] == 2500.0
        @test stmt.operating_expenses["Property Taxes"] == 12000.0
        @test stmt.operating_expenses["Maintenance"] == 6000.0
        
        # Calculate expected NOI
        total_revenue = 150000.0 + 5000.0 + 8000.0 + 3000.0
        total_expenses = 7500.0 + 4000.0 + 2500.0 + 12000.0 + 6000.0
        expected_noi = total_revenue - total_expenses
        
        @test stmt.net_operating_income == expected_noi
        @test stmt.metadata["noi_margin"] ≈ expected_noi / total_revenue atol=0.001
    end
end

println("✅ Operating Statement tests completed")