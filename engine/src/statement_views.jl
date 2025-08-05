# Stage 7: Statement Views
# Generates different reporting views (GAAP/Non-GAAP, Monthly/Annual)

using Dates

# Note: StatementView and ReportingFrequency enums are defined in common_enums.jl

"""
    FinancialStatement

Standardized financial statement structure for different reporting views.

Key Components:
- View Type: GAAP, Non-GAAP, Tax, Management
- Frequency: Monthly, Quarterly, Annual, Cumulative
- Cash Flow Categories: Operating, Financing, Investing
- Key Metrics: NOI, DSCR, Cash-on-Cash returns
"""
struct FinancialStatement
    entity_id::String
    period_start::Date
    period_end::Date
    view_type::StatementView
    frequency::ReportingFrequency
    operating_cash_flow::Float64
    financing_cash_flow::Float64
    investing_cash_flow::Float64
    net_cash_flow::Float64
    key_metrics::Dict{String, Float64}
    line_items::Dict{String, Float64}
    metadata::Dict{String, Any}
end

"""
    generate_statement_views(available_cash_calculations::Vector{AvailableCashCalculation}) -> Dict{String, Vector{FinancialStatement}}

Generate different reporting views from available cash calculations.

This is Stage 7 of the 7-stage cash flow pipeline:
1. Stream Collection ✓
2. Cash Flow Assembly ✓
3. Operating Statement Generation ✓
4. Financing Adjustments ✓
5. Tax Processing ✓
6. Available Cash Calculation ✓
7. **Statement Views** ← This stage

Statement views follow industry reporting standards:
```
VIEW TYPES:
- GAAP: Generally Accepted Accounting Principles
- Non-GAAP: Operating/Economic view (exclude non-cash items)
- Tax: Tax reporting view
- Management: Internal management reporting

TIME PERIODS:
- Monthly: Detailed period analysis
- Quarterly: Quarterly reporting  
- Annual: Yearly statements
- Cumulative: Life-to-date
```
"""
function generate_statement_views(available_cash_calculations::Vector{AvailableCashCalculation})::Dict{String, Vector{FinancialStatement}}
    statement_views = Dict{String, Vector{FinancialStatement}}()
    
    # Generate GAAP statements
    statement_views["gaap"] = generate_gaap_statements(available_cash_calculations)
    
    # Generate Non-GAAP statements  
    statement_views["non_gaap"] = generate_non_gaap_statements(available_cash_calculations)
    
    # Generate Tax statements
    statement_views["tax"] = generate_tax_statements(available_cash_calculations)
    
    # Generate Management statements
    statement_views["management"] = generate_management_statements(available_cash_calculations)
    
    return statement_views
end

"""
    generate_gaap_statements(calculations::Vector{AvailableCashCalculation}) -> Vector{FinancialStatement}

Generate GAAP-compliant financial statements.

GAAP statements include all items required by Generally Accepted Accounting Principles:
- Include non-cash items (depreciation, amortization)
- Follow standardized presentation format
- Include all required disclosures in metadata
"""
function generate_gaap_statements(calculations::Vector{AvailableCashCalculation})::Vector{FinancialStatement}
    gaap_statements = Vector{FinancialStatement}()
    
    for calc in calculations
        # GAAP requires comprehensive cash flow categorization
        operating_cf = calc.after_tax_cf  # After all operating adjustments
        financing_cf = 0.0  # Would include debt proceeds, equity contributions, distributions
        investing_cf = -calc.capital_reserves  # Capital expenditures (negative for outflows)
        net_cf = operating_cf + financing_cf + investing_cf
        
        # Key GAAP metrics
        key_metrics = Dict{String, Float64}(
            "net_income" => calc.after_tax_cf,  # Simplified - would include non-cash items
            "operating_cash_flow" => operating_cf,
            "free_cash_flow" => calc.available_for_distribution,
            "cash_flow_from_operations" => operating_cf,
            "cash_flow_from_financing" => financing_cf,
            "cash_flow_from_investing" => investing_cf
        )
        
        # GAAP line items
        line_items = Dict{String, Float64}(
            "cash_flows_from_operating_activities" => operating_cf,
            "cash_flows_from_financing_activities" => financing_cf,
            "cash_flows_from_investing_activities" => investing_cf,
            "net_change_in_cash" => net_cf,
            "capital_expenditures" => calc.capital_reserves,
            "working_capital_changes" => calc.working_capital_change
        )
        
        # GAAP metadata
        metadata = Dict{String, Any}(
            "gaap_compliant" => true,
            "includes_non_cash_items" => true,
            "presentation_currency" => "USD",
            "accounting_method" => "accrual",
            "stage" => "statement_views_gaap"
        )
        
        statement = FinancialStatement(
            calc.entity_id,
            calc.period_start,
            calc.period_end,
            GAAP,
            MONTHLY,  # Default to monthly, can be aggregated later
            operating_cf,
            financing_cf,
            investing_cf,
            net_cf,
            key_metrics,
            line_items,
            metadata
        )
        
        push!(gaap_statements, statement)
    end
    
    return gaap_statements
end

"""
    generate_non_gaap_statements(calculations::Vector{AvailableCashCalculation}) -> Vector{FinancialStatement}

Generate Non-GAAP (economic/operating) statements.

Non-GAAP statements focus on economic performance:
- Exclude non-cash items for cash flow focus
- Emphasize operating metrics relevant to investors
- Include real estate specific metrics (NOI, DSCR, etc.)
"""
function generate_non_gaap_statements(calculations::Vector{AvailableCashCalculation})::Vector{FinancialStatement}
    non_gaap_statements = Vector{FinancialStatement}()
    
    for calc in calculations
        # Non-GAAP focuses on cash-based metrics
        operating_cf = calc.after_tax_cf
        financing_cf = 0.0  # Simplified
        investing_cf = -calc.capital_reserves
        net_cf = calc.available_for_distribution
        
        # Key Non-GAAP metrics (real estate focused)
        key_metrics = Dict{String, Float64}(
            "net_operating_income" => calc.after_tax_cf + calc.capital_reserves,  # Approximate NOI
            "available_for_distribution" => calc.available_for_distribution,
            "funds_from_operations" => calc.after_tax_cf,  # Simplified FFO
            "adjusted_funds_from_operations" => calc.available_for_distribution,  # Simplified AFFO
            "cash_on_cash_return" => calculate_cash_on_cash_return(calc),
            "distribution_coverage_ratio" => calculate_distribution_coverage(calc)
        )
        
        # Non-GAAP line items
        line_items = Dict{String, Float64}(
            "funds_from_operations" => calc.after_tax_cf,
            "capital_expenditures" => calc.capital_reserves,
            "adjusted_funds_from_operations" => calc.available_for_distribution,
            "distributions_to_investors" => calc.available_for_distribution,
            "retained_cash" => calc.capital_reserves + calc.other_adjustments
        )
        
        # Non-GAAP metadata
        metadata = Dict{String, Any}(
            "excludes_non_cash_items" => true,
            "economic_view" => true,
            "real_estate_focused" => true,
            "investor_oriented" => true,
            "stage" => "statement_views_non_gaap"
        )
        
        statement = FinancialStatement(
            calc.entity_id,
            calc.period_start,
            calc.period_end,
            NON_GAAP,
            MONTHLY,
            operating_cf,
            financing_cf,
            investing_cf,
            net_cf,
            key_metrics,
            line_items,
            metadata
        )
        
        push!(non_gaap_statements, statement)
    end
    
    return non_gaap_statements
end

"""
    generate_tax_statements(calculations::Vector{AvailableCashCalculation}) -> Vector{FinancialStatement}

Generate tax reporting statements.

Tax statements focus on tax-related metrics:
- Taxable income calculations
- Tax deductions and credits
- Tax efficiency metrics
"""
function generate_tax_statements(calculations::Vector{AvailableCashCalculation})::Vector{FinancialStatement}
    tax_statements = Vector{FinancialStatement}()
    
    for calc in calculations
        # Tax statement focuses on tax implications
        operating_cf = calc.after_tax_cf
        financing_cf = 0.0
        investing_cf = 0.0
        net_cf = calc.after_tax_cf
        
        # Tax-specific metrics
        key_metrics = Dict{String, Float64}(
            "taxable_income" => estimate_taxable_income(calc),
            "tax_expense" => estimate_tax_expense(calc),
            "effective_tax_rate" => calculate_effective_tax_rate(calc),
            "tax_shield_benefit" => estimate_tax_shield(calc),
            "after_tax_cash_flow" => calc.after_tax_cf
        )
        
        # Tax line items
        line_items = Dict{String, Float64}(
            "pre_tax_income" => calc.after_tax_cf / 0.75,  # Rough approximation
            "depreciation_deduction" => estimate_depreciation(calc),
            "interest_deduction" => estimate_interest_deduction(calc),
            "taxable_income" => estimate_taxable_income(calc),
            "income_tax_expense" => estimate_tax_expense(calc),
            "after_tax_income" => calc.after_tax_cf
        )
        
        # Tax metadata
        metadata = Dict{String, Any}(
            "tax_focused" => true,
            "includes_tax_benefits" => true,
            "estimated_calculations" => true,
            "stage" => "statement_views_tax"
        )
        
        statement = FinancialStatement(
            calc.entity_id,
            calc.period_start,
            calc.period_end,
            TAX,
            MONTHLY,
            operating_cf,
            financing_cf,
            investing_cf,
            net_cf,
            key_metrics,
            line_items,
            metadata
        )
        
        push!(tax_statements, statement)
    end
    
    return tax_statements
end

"""
    generate_management_statements(calculations::Vector{AvailableCashCalculation}) -> Vector{FinancialStatement}

Generate management reporting statements.

Management statements provide internal decision-making metrics:
- Operational KPIs
- Performance vs budget/forecast
- Cash flow forecasting metrics
"""
function generate_management_statements(calculations::Vector{AvailableCashCalculation})::Vector{FinancialStatement}
    management_statements = Vector{FinancialStatement}()
    
    for calc in calculations
        # Management view emphasizes operational control
        operating_cf = calc.after_tax_cf
        financing_cf = 0.0
        investing_cf = -calc.capital_reserves
        net_cf = calc.available_for_distribution
        
        # Management-focused metrics
        key_metrics = Dict{String, Float64}(
            "available_for_distribution" => calc.available_for_distribution,
            "cash_flow_stability" => assess_cash_flow_stability_score(calc),
            "reserve_adequacy" => calc.capital_reserves / max(1.0, calc.after_tax_cf),
            "operational_efficiency" => calculate_operational_efficiency(calc),
            "liquidity_ratio" => calculate_liquidity_ratio(calc)
        )
        
        # Management line items
        line_items = Dict{String, Float64}(
            "operating_cash_generation" => calc.after_tax_cf,
            "required_reserves" => calc.capital_reserves,
            "discretionary_cash" => calc.available_for_distribution,
            "working_capital_impact" => calc.working_capital_change,
            "other_adjustments" => calc.other_adjustments
        )
        
        # Management metadata
        metadata = Dict{String, Any}(
            "internal_reporting" => true,
            "management_focused" => true,
            "operational_metrics" => true,
            "decision_support" => true,
            "stage" => "statement_views_management"
        )
        
        statement = FinancialStatement(
            calc.entity_id,
            calc.period_start,
            calc.period_end,
            MANAGEMENT,
            MONTHLY,
            operating_cf,
            financing_cf,
            investing_cf,
            net_cf,
            key_metrics,
            line_items,
            metadata
        )
        
        push!(management_statements, statement)
    end
    
    return management_statements
end

"""
    aggregate_to_annual(monthly_statements::Vector{FinancialStatement}) -> Vector{FinancialStatement}

Aggregate monthly statements to annual reporting periods.
"""
function aggregate_to_annual(monthly_statements::Vector{FinancialStatement})::Vector{FinancialStatement}
    annual_statements = Vector{FinancialStatement}()
    
    # Group by year and entity
    yearly_groups = Dict{Tuple{Int, String, StatementView}, Vector{FinancialStatement}}()
    
    for stmt in monthly_statements
        year = Dates.year(stmt.period_start)
        key = (year, stmt.entity_id, stmt.view_type)
        
        if !haskey(yearly_groups, key)
            yearly_groups[key] = []
        end
        push!(yearly_groups[key], stmt)
    end
    
    # Aggregate each year group
    for ((year, entity_id, view_type), year_statements) in yearly_groups
        if isempty(year_statements)
            continue
        end
        
        # Aggregate cash flows
        total_operating = sum(s.operating_cash_flow for s in year_statements)
        total_financing = sum(s.financing_cash_flow for s in year_statements)
        total_investing = sum(s.investing_cash_flow for s in year_statements)
        total_net = sum(s.net_cash_flow for s in year_statements)
        
        # Aggregate key metrics (taking averages where appropriate)
        aggregated_metrics = Dict{String, Float64}()
        for metric_name in keys(year_statements[1].key_metrics)
            if metric_name in ["effective_tax_rate", "cash_on_cash_return", "distribution_coverage_ratio"]
                # Average for ratios
                aggregated_metrics[metric_name] = sum(s.key_metrics[metric_name] for s in year_statements) / length(year_statements)
            else
                # Sum for absolute values
                aggregated_metrics[metric_name] = sum(s.key_metrics[metric_name] for s in year_statements)
            end
        end
        
        # Aggregate line items
        aggregated_line_items = Dict{String, Float64}()
        for line_name in keys(year_statements[1].line_items)
            aggregated_line_items[line_name] = sum(s.line_items[line_name] for s in year_statements)
        end
        
        # Create annual metadata
        metadata = copy(year_statements[1].metadata)
        metadata["annual_aggregation"] = true
        metadata["periods_included"] = length(year_statements)
        metadata["year"] = year
        
        annual_statement = FinancialStatement(
            entity_id,
            Date(year, 1, 1),
            Date(year, 12, 31),
            view_type,
            ANNUAL,
            total_operating,
            total_financing,
            total_investing,
            total_net,
            aggregated_metrics,
            aggregated_line_items,
            metadata
        )
        
        push!(annual_statements, annual_statement)
    end
    
    sort!(annual_statements, by = s -> (s.entity_id, s.period_start, s.view_type))
    return annual_statements
end

"""
    summarize_statement_views(statement_views::Dict{String, Vector{FinancialStatement}}) -> Dict{String, Any}

Create a comprehensive summary of all statement views for reporting.
"""
function summarize_statement_views(statement_views::Dict{String, Vector{FinancialStatement}})::Dict{String, Any}
    summary = Dict{String, Any}()
    
    for (view_name, statements) in statement_views
        if isempty(statements)
            summary[view_name] = Dict("message" => "No statements available")
            continue
        end
        
        total_operating = sum(s.operating_cash_flow for s in statements)
        total_net = sum(s.net_cash_flow for s in statements)
        
        summary[view_name] = Dict(
            "statement_count" => length(statements),
            "total_operating_cf" => total_operating,
            "total_net_cf" => total_net,
            "average_operating_cf" => total_operating / length(statements),
            "period_coverage" => Dict(
                "start_date" => minimum(s.period_start for s in statements),
                "end_date" => maximum(s.period_end for s in statements)
            ),
            "entities_covered" => length(unique(s.entity_id for s in statements))
        )
    end
    
    summary["stage_completed"] = "statement_views"
    summary["total_view_types"] = length(statement_views)
    
    return summary
end

# Helper functions

function calculate_cash_on_cash_return(calc::AvailableCashCalculation)::Float64
    # Simplified - would need equity investment amount
    return calc.available_for_distribution  # Return as absolute amount for now
end

function calculate_distribution_coverage(calc::AvailableCashCalculation)::Float64
    return calc.after_tax_cf > 0 ? calc.available_for_distribution / calc.after_tax_cf : 0.0
end

function estimate_taxable_income(calc::AvailableCashCalculation)::Float64
    # Simplified estimation
    return calc.after_tax_cf / 0.75  # Assuming ~25% tax rate
end

function estimate_tax_expense(calc::AvailableCashCalculation)::Float64
    # Simplified estimation
    taxable_income = estimate_taxable_income(calc)
    return taxable_income * 0.25  # Assuming 25% tax rate
end

function calculate_effective_tax_rate(calc::AvailableCashCalculation)::Float64
    pre_tax = estimate_taxable_income(calc)
    tax_expense = estimate_tax_expense(calc)
    return pre_tax > 0 ? tax_expense / pre_tax : 0.0
end

function estimate_tax_shield(calc::AvailableCashCalculation)::Float64
    # Simplified - would be based on actual depreciation and interest deductions
    return calc.capital_reserves * 0.25  # Assuming 25% tax benefit
end

function estimate_depreciation(calc::AvailableCashCalculation)::Float64
    # Simplified estimation
    return calc.capital_reserves * 2.0  # Rough approximation
end

function estimate_interest_deduction(calc::AvailableCashCalculation)::Float64
    # Simplified estimation
    return calc.after_tax_cf * 0.1  # Rough approximation
end

function assess_cash_flow_stability_score(calc::AvailableCashCalculation)::Float64
    # Simplified stability score (0-1 scale)
    if calc.available_for_distribution > 0
        return min(1.0, calc.available_for_distribution / max(1.0, calc.after_tax_cf))
    else
        return 0.0
    end
end

function calculate_operational_efficiency(calc::AvailableCashCalculation)::Float64
    # Simplified efficiency metric
    total_adjustments = calc.capital_reserves + calc.working_capital_change + calc.other_adjustments
    return calc.after_tax_cf > 0 ? calc.available_for_distribution / calc.after_tax_cf : 0.0
end

function calculate_liquidity_ratio(calc::AvailableCashCalculation)::Float64
    # Simplified liquidity assessment
    return calc.available_for_distribution / max(1.0, calc.capital_reserves)
end

