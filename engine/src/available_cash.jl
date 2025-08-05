# Stage 6: Available Cash Calculation
# Calculates distributable cash flow after reserves and working capital changes

using Dates

"""
    AvailableCashCalculation

Available cash calculation following real estate industry distribution practices.

Key Components:
- After-tax cash flow: Starting point from tax processing
- Capital reserves: Funds set aside for future capital expenditures
- Working capital changes: Changes in working capital requirements
- Available for distribution: Final distributable cash flow
"""
struct AvailableCashCalculation
    entity_id::String
    period_start::Date
    period_end::Date
    after_tax_cf::Float64
    capital_reserves::Float64
    working_capital_change::Float64
    other_adjustments::Float64
    available_for_distribution::Float64
    metadata::Dict{String, Any}
end

"""
    calculate_available_cash(tax_adjustments::Vector{TaxAdjustment}) -> Vector{AvailableCashCalculation}

Calculate available cash for distribution after all adjustments.

This is Stage 6 of the 7-stage cash flow pipeline:
1. Stream Collection ✓
2. Cash Flow Assembly ✓
3. Operating Statement Generation ✓
4. Financing Adjustments ✓
5. Tax Processing ✓
6. **Available Cash Calculation** ← This stage
7. Statement Views

Available cash calculation follows real estate industry standards:
```
Operating Income
- Debt Service
- Tax Expense  
- Capital Reserves
- Working Capital Changes
= AVAILABLE FOR DISTRIBUTION
```
"""
function calculate_available_cash(tax_adjustments::Vector{TaxAdjustment})::Vector{AvailableCashCalculation}
    available_cash_calculations = Vector{AvailableCashCalculation}()
    
    for adjustment in tax_adjustments
        # Start with after-tax cash flow
        after_tax_cf = adjustment.after_tax_cf
        
        # Calculate capital reserves based on industry standards
        capital_reserves = calculate_capital_reserves(after_tax_cf, adjustment.period_start)
        
        # Calculate working capital changes (simplified)
        working_capital_change = calculate_working_capital_change(adjustment)
        
        # Other adjustments (tenant improvements, leasing costs, etc.)
        other_adjustments = calculate_other_adjustments(adjustment)
        
        # Calculate final available for distribution
        available_for_distribution = after_tax_cf - capital_reserves - working_capital_change - other_adjustments
        
        # Create comprehensive metadata for analysis
        metadata = Dict{String, Any}(
            "after_tax_cf" => after_tax_cf,
            "reserve_rate" => calculate_reserve_rate(adjustment.period_start),
            "distribution_coverage" => after_tax_cf > 0 ? available_for_distribution / after_tax_cf : 0.0,
            "reserve_coverage_months" => capital_reserves > 0 ? after_tax_cf / (capital_reserves * 12) : 0.0,
            "cash_flow_stability" => assess_cash_flow_stability(adjustment),
            "stage" => "available_cash_calculation",
            "calculation_method" => "industry_standard"
        )
        
        calculation = AvailableCashCalculation(
            adjustment.entity_id,
            adjustment.period_start,
            adjustment.period_end,
            after_tax_cf,
            capital_reserves,
            working_capital_change,
            other_adjustments,
            available_for_distribution,
            metadata
        )
        
        push!(available_cash_calculations, calculation)
    end
    
    return available_cash_calculations
end

"""
    apply_distribution_policy(calculations::Vector{AvailableCashCalculation}, policy::Dict{String, Any}) -> Vector{AvailableCashCalculation}

Apply distribution policy constraints to available cash calculations.

Distribution policies may include:
- Minimum cash retention requirements
- Distribution frequency constraints
- Investor class priorities
- Performance-based distribution adjustments
"""
function apply_distribution_policy(calculations::Vector{AvailableCashCalculation}, policy::Dict{String, Any} = Dict{String, Any}())::Vector{AvailableCashCalculation}
    adjusted_calculations = Vector{AvailableCashCalculation}()
    
    # Default policy parameters
    min_cash_retention = get(policy, "min_cash_retention_months", 3.0)
    additional_reserve_rate = get(policy, "additional_reserve_rate", 0.05)
    distribution_frequency = get(policy, "distribution_frequency", "monthly")
    
    for calc in calculations
        # Apply minimum cash retention policy
        monthly_operating_expense = estimate_monthly_operating_expense(calc)
        min_retention_amount = monthly_operating_expense * min_cash_retention
        
        # Apply additional reserves based on policy
        additional_reserves = calc.available_for_distribution * additional_reserve_rate
        
        # Calculate policy-adjusted available distribution
        policy_adjusted_available = calc.available_for_distribution - min_retention_amount - additional_reserves
        policy_adjusted_available = max(0.0, policy_adjusted_available)  # Cannot distribute negative amounts
        
        # Update metadata with policy information
        updated_metadata = copy(calc.metadata)
        updated_metadata["policy_applied"] = true
        updated_metadata["min_retention_amount"] = min_retention_amount
        updated_metadata["additional_reserves"] = additional_reserves
        updated_metadata["policy_adjustment"] = calc.available_for_distribution - policy_adjusted_available
        updated_metadata["distribution_frequency"] = distribution_frequency
        
        # Create adjusted calculation
        adjusted_calc = AvailableCashCalculation(
            calc.entity_id,
            calc.period_start,
            calc.period_end,
            calc.after_tax_cf,
            calc.capital_reserves + additional_reserves,
            calc.working_capital_change,
            calc.other_adjustments + min_retention_amount,
            policy_adjusted_available,
            updated_metadata
        )
        
        push!(adjusted_calculations, adjusted_calc)
    end
    
    return adjusted_calculations
end

"""
    calculate_distribution_metrics(calculations::Vector{AvailableCashCalculation}) -> Dict{String, Any}

Calculate comprehensive distribution metrics for analysis and reporting.
"""
function calculate_distribution_metrics(calculations::Vector{AvailableCashCalculation})::Dict{String, Any}
    if isempty(calculations)
        return Dict{String, Any}("message" => "No available cash calculations to analyze")
    end
    
    # Aggregate calculations
    total_after_tax = sum(calc.after_tax_cf for calc in calculations)
    total_available = sum(calc.available_for_distribution for calc in calculations)
    total_reserves = sum(calc.capital_reserves for calc in calculations)
    total_wc_changes = sum(calc.working_capital_change for calc in calculations)
    total_other_adjustments = sum(calc.other_adjustments for calc in calculations)
    
    # Distribution efficiency metrics
    distribution_rate = total_after_tax > 0 ? total_available / total_after_tax : 0.0
    reserve_rate = total_after_tax > 0 ? total_reserves / total_after_tax : 0.0
    
    # Cash flow stability analysis
    available_amounts = [calc.available_for_distribution for calc in calculations]
    cash_flow_volatility = calculate_cash_flow_volatility(available_amounts)
    
    # Distribution coverage analysis
    coverage_ratios = [
        calc.after_tax_cf > 0 ? calc.available_for_distribution / calc.after_tax_cf : 0.0
        for calc in calculations
    ]
    
    return Dict{String, Any}(
        "aggregate_metrics" => Dict(
            "total_after_tax_cf" => total_after_tax,
            "total_available_for_distribution" => total_available,
            "total_capital_reserves" => total_reserves,
            "total_working_capital_changes" => total_wc_changes,
            "total_other_adjustments" => total_other_adjustments,
            "net_adjustments" => total_reserves + total_wc_changes + total_other_adjustments
        ),
        "distribution_efficiency" => Dict(
            "distribution_rate" => distribution_rate,
            "reserve_rate" => reserve_rate,
            "average_coverage_ratio" => isempty(coverage_ratios) ? 0.0 : sum(coverage_ratios) / length(coverage_ratios),
            "minimum_coverage_ratio" => isempty(coverage_ratios) ? 0.0 : minimum(coverage_ratios),
            "cash_flow_volatility" => cash_flow_volatility
        ),
        "analysis_metadata" => Dict(
            "periods_analyzed" => length(calculations),
            "entities_covered" => length(unique(calc.entity_id for calc in calculations)),
            "stage_completed" => "available_cash_calculation"
        )
    )
end

"""
    summarize_available_cash(calculations::Vector{AvailableCashCalculation}) -> Dict{String, Any}

Create a comprehensive summary of available cash calculations for reporting.
"""
function summarize_available_cash(calculations::Vector{AvailableCashCalculation})::Dict{String, Any}
    if isempty(calculations)
        return Dict{String, Any}("message" => "No available cash calculations to summarize")
    end
    
    total_after_tax = sum(calc.after_tax_cf for calc in calculations)
    total_available = sum(calc.available_for_distribution for calc in calculations)
    
    return Dict{String, Any}(
        "summary_metrics" => Dict(
            "total_calculations" => length(calculations),
            "total_after_tax_cf" => total_after_tax,
            "total_available_for_distribution" => total_available,
            "average_after_tax" => total_after_tax / length(calculations),
            "average_available" => total_available / length(calculations),
            "overall_distribution_rate" => total_after_tax > 0 ? total_available / total_after_tax : 0.0
        ),
        "adjustment_breakdown" => Dict(
            "total_capital_reserves" => sum(calc.capital_reserves for calc in calculations),
            "total_working_capital_changes" => sum(calc.working_capital_change for calc in calculations),
            "total_other_adjustments" => sum(calc.other_adjustments for calc in calculations),
            "average_reserve_rate" => calculate_average_reserve_rate(calculations)
        ),
        "period_coverage" => Dict(
            "start_date" => minimum(calc.period_start for calc in calculations),
            "end_date" => maximum(calc.period_end for calc in calculations),
            "entities_covered" => length(unique(calc.entity_id for calc in calculations))
        ),
        "stage_completed" => "available_cash_calculation"
    )
end

# Helper functions

"""
    calculate_capital_reserves(cash_flow::Float64, period_start::Date, reserve_rate::Float64 = nothing) -> Float64

Calculate capital reserves based on cash flow and industry standards.

Capital reserves are typically 3-5% of gross revenue or 1-3% of property value,
but simplified here as a percentage of cash flow.
"""
function calculate_capital_reserves(cash_flow::Float64, period_start::Date, reserve_rate::Float64 = nothing)::Float64
    if reserve_rate === nothing
        reserve_rate = calculate_reserve_rate(period_start)
    end
    
    # Only apply reserves to positive cash flows
    return max(0.0, cash_flow * reserve_rate)
end

"""
    calculate_reserve_rate(period_start::Date) -> Float64

Calculate appropriate reserve rate based on property type and market conditions.
This is simplified - real implementations would consider property age, type, market conditions.
"""
function calculate_reserve_rate(period_start::Date)::Float64
    # Simplified reserve rate calculation
    # In practice, this would vary by:
    # - Property type (office, retail, multifamily, etc.)
    # - Property age and condition
    # - Market conditions
    # - Historical capital expenditure patterns
    
    base_rate = 0.03  # 3% base rate
    
    # Adjust for property age (simplified)
    year = Dates.year(period_start)
    if year >= 2020
        base_rate * 1.1  # Slightly higher for newer analysis (inflation considerations)
    else
        base_rate
    end
end

"""
    calculate_working_capital_change(adjustment::TaxAdjustment) -> Float64

Calculate working capital changes (simplified implementation).

In practice, this would consider:
- Changes in accounts receivable (tenant receivables)
- Changes in accounts payable (vendor payables)
- Changes in prepaid expenses
- Changes in accrued expenses
"""
function calculate_working_capital_change(adjustment::TaxAdjustment)::Float64
    # Simplified working capital calculation
    # In practice, this would require detailed balance sheet analysis
    
    # Assume minimal working capital changes for real estate (typically asset-light operations)
    return 0.0
end

"""
    calculate_other_adjustments(adjustment::TaxAdjustment) -> Float64

Calculate other adjustments such as tenant improvements, leasing costs, etc.

In practice, this would include:
- Tenant improvement allowances
- Leasing commissions
- Major maintenance items not covered by reserves
- One-time expenses or income
"""
function calculate_other_adjustments(adjustment::TaxAdjustment)::Float64
    # Simplified other adjustments
    # In practice, this would be based on specific property requirements
    
    # Small adjustment for miscellaneous items
    return max(0.0, adjustment.after_tax_cf * 0.005)  # 0.5% of after-tax cash flow
end

"""
    assess_cash_flow_stability(adjustment::TaxAdjustment) -> String

Assess the stability of cash flows for distribution planning.
"""
function assess_cash_flow_stability(adjustment::TaxAdjustment)::String
    # Simplified stability assessment
    # In practice, this would consider historical volatility, tenant quality, lease terms, etc.
    
    if adjustment.after_tax_cf > 0
        if adjustment.after_tax_cf > 100000  # Arbitrary threshold
            "stable"
        else
            "moderate"
        end
    else
        "unstable"
    end
end

"""
    estimate_monthly_operating_expense(calc::AvailableCashCalculation) -> Float64

Estimate monthly operating expenses for minimum cash retention calculations.
"""
function estimate_monthly_operating_expense(calc::AvailableCashCalculation)::Float64
    # Simplified estimation
    # In practice, this would use detailed operating expense analysis
    
    # Assume operating expenses are roughly 40% of gross revenue
    # and estimate based on after-tax cash flow
    estimated_monthly_expense = abs(calc.after_tax_cf) * 0.4 / 12
    
    return max(10000.0, estimated_monthly_expense)  # Minimum threshold
end

"""
    calculate_cash_flow_volatility(amounts::Vector{Float64}) -> Float64

Calculate the volatility (coefficient of variation) of cash flow amounts.
"""
function calculate_cash_flow_volatility(amounts::Vector{Float64})::Float64
    if length(amounts) < 2
        return 0.0
    end
    
    mean_amount = sum(amounts) / length(amounts)
    if mean_amount == 0
        return 0.0
    end
    
    variance = sum((amount - mean_amount)^2 for amount in amounts) / (length(amounts) - 1)
    std_dev = sqrt(variance)
    
    # Return coefficient of variation (standard deviation / mean)
    return std_dev / abs(mean_amount)
end

"""
    calculate_average_reserve_rate(calculations::Vector{AvailableCashCalculation}) -> Float64

Calculate the average reserve rate across all calculations.
"""
function calculate_average_reserve_rate(calculations::Vector{AvailableCashCalculation})::Float64
    if isempty(calculations)
        return 0.0
    end
    
    total_after_tax = sum(calc.after_tax_cf for calc in calculations if calc.after_tax_cf > 0)
    total_reserves = sum(calc.capital_reserves for calc in calculations)
    
    return total_after_tax > 0 ? total_reserves / total_after_tax : 0.0
end

"""
    validate_available_cash_calculation(calculation::AvailableCashCalculation) -> Dict{String, Any}

Validate an available cash calculation for data quality and business logic.
"""
function validate_available_cash_calculation(calculation::AvailableCashCalculation)::Dict{String, Any}
    warnings = String[]
    errors = String[]
    
    # Check for negative available cash (concerning but possible)
    if calculation.available_for_distribution < 0
        push!(warnings, "Negative available for distribution: $(calculation.available_for_distribution)")
    end
    
    # Validate calculation logic
    expected_available = calculation.after_tax_cf - calculation.capital_reserves - 
                        calculation.working_capital_change - calculation.other_adjustments
    
    if abs(expected_available - calculation.available_for_distribution) > 0.01
        push!(errors, "Available cash calculation error: expected=$expected_available, actual=$(calculation.available_for_distribution)")
    end
    
    # Check for excessive reserves (might indicate error)
    if calculation.after_tax_cf > 0 && calculation.capital_reserves / calculation.after_tax_cf > 0.20
        push!(warnings, "High reserve rate: $(round(calculation.capital_reserves / calculation.after_tax_cf * 100, digits=1))%")
    end
    
    return Dict{String, Any}(
        "is_valid" => isempty(errors),
        "warnings" => warnings,
        "errors" => errors,
        "validation_timestamp" => now()
    )
end