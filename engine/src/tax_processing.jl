# Stage 5: Tax Processing
# Applies tax adjustments and calculations (depreciation, amortization, income tax)

using Dates

"""
    TaxAdjustment

Tax calculations and adjustments following real estate industry tax practices.

Key Components:
- Non-cash deductions: Depreciation, amortization
- Taxable income calculation: After non-cash adjustments
- Tax expense: Income tax, property tax
- After-tax cash flow: Final cash flow after all tax considerations
"""
struct TaxAdjustment
    entity_id::String
    period_start::Date
    period_end::Date
    pre_tax_income::Float64
    depreciation::Float64
    amortization::Float64
    other_tax_adjustments::Float64
    taxable_income::Float64
    income_tax_expense::Float64
    after_tax_cf::Float64
    metadata::Dict{String, Any}
end

"""
    calculate_tax_adjustments(financing_adjustments::Vector{FinancingAdjustment}, cash_flow_entries::Vector{CashFlowEntry}) -> Vector{TaxAdjustment}

Calculate taxable income with depreciation and other tax adjustments.

This is Stage 5 of the 7-stage cash flow pipeline:
1. Stream Collection ✓
2. Cash Flow Assembly ✓
3. Operating Statement Generation ✓
4. Financing Adjustments ✓
5. **Tax Processing** ← This stage
6. Available Cash Calculation
7. Statement Views

Tax processing follows real estate industry standards:
```
TAX ADJUSTMENTS:
- Depreciation (non-cash)
- Amortization (non-cash)  
- Taxable Income Calculation
- Income Tax Expense
- Tax Credits

= AFTER-TAX CASH FLOW
```
"""
function calculate_tax_adjustments(financing_adjustments::Vector{FinancingAdjustment}, cash_flow_entries::Vector{CashFlowEntry})::Vector{TaxAdjustment}
    tax_adjustments = Vector{TaxAdjustment}()
    
    # Create lookup for cash flow entries by entity and period
    entry_lookup = Dict{Tuple{String, Date, Date}, CashFlowEntry}()
    for entry in cash_flow_entries
        key = (entry.entity_id, entry.period_start, entry.period_end)
        entry_lookup[key] = entry
    end
    
    for adjustment in financing_adjustments
        key = (adjustment.entity_id, adjustment.period_start, adjustment.period_end)
        cash_flow_entry = get(entry_lookup, key, nothing)
        
        # Start with unlevered cash flow as pre-tax income
        pre_tax_income = adjustment.unlevered_cf
        
        # Extract tax-related adjustments from cash flow entry
        depreciation = 0.0
        amortization = 0.0
        other_tax_adjustments = 0.0
        
        if cash_flow_entry !== nothing
            for (item_name, amount) in cash_flow_entry.tax_items
                if is_depreciation_item(item_name)
                    depreciation += abs(amount)  # Depreciation reduces taxable income
                elseif is_amortization_item(item_name)
                    amortization += abs(amount)  # Amortization reduces taxable income
                else
                    other_tax_adjustments += amount  # Could be positive or negative
                end
            end
        end
        
        # Calculate taxable income
        # Note: Depreciation and amortization are deductible (reduce taxable income)
        taxable_income = pre_tax_income - depreciation - amortization - other_tax_adjustments
        
        # Calculate income tax expense using applicable tax rates
        tax_rates = get_applicable_tax_rates(adjustment.entity_id, adjustment.period_start)
        income_tax_expense = calculate_income_tax(taxable_income, tax_rates)
        
        # Calculate after-tax cash flow
        after_tax_cf = adjustment.levered_cf - income_tax_expense
        
        # Create comprehensive metadata for analysis
        metadata = Dict{String, Any}(
            "pre_tax_income" => pre_tax_income,
            "levered_cf" => adjustment.levered_cf,
            "total_tax_adjustments" => depreciation + amortization + other_tax_adjustments,
            "effective_tax_rate" => pre_tax_income > 0 ? income_tax_expense / pre_tax_income : 0.0,
            "marginal_tax_rate" => tax_rates["combined_rate"],
            "tax_shield_value" => (depreciation + amortization) * tax_rates["combined_rate"],
            "stage" => "tax_processing",
            "tax_calculation_method" => "standard_corporate"
        )
        
        tax_adjustment = TaxAdjustment(
            adjustment.entity_id,
            adjustment.period_start,
            adjustment.period_end,
            pre_tax_income,
            depreciation,
            amortization,
            other_tax_adjustments,
            taxable_income,
            income_tax_expense,
            after_tax_cf,
            metadata
        )
        
        push!(tax_adjustments, tax_adjustment)
    end
    
    return tax_adjustments
end

"""
    apply_final_tax_adjustments(tax_adjustments::Vector{TaxAdjustment}) -> Vector{TaxAdjustment}

Apply final tax adjustments including credits and other modifications.
"""
function apply_final_tax_adjustments(tax_adjustments::Vector{TaxAdjustment})::Vector{TaxAdjustment}
    final_adjustments = Vector{TaxAdjustment}()
    
    for adjustment in tax_adjustments
        # Calculate tax credits (simplified - could be more sophisticated)
        tax_credits = calculate_tax_credits(adjustment)
        
        # Apply tax credits to reduce tax expense
        adjusted_tax_expense = max(0.0, adjustment.income_tax_expense - tax_credits)
        
        # Recalculate final after-tax cash flow
        final_after_tax_cf = adjustment.after_tax_cf + (adjustment.income_tax_expense - adjusted_tax_expense)
        
        # Update metadata with final adjustments
        updated_metadata = copy(adjustment.metadata)
        updated_metadata["tax_credits"] = tax_credits
        updated_metadata["final_tax_expense"] = adjusted_tax_expense
        updated_metadata["tax_credit_benefit"] = tax_credits
        updated_metadata["final_effective_tax_rate"] = adjustment.pre_tax_income > 0 ? adjusted_tax_expense / adjustment.pre_tax_income : 0.0
        
        # Create final adjustment with updated values
        final_adjustment = TaxAdjustment(
            adjustment.entity_id,
            adjustment.period_start,
            adjustment.period_end,
            adjustment.pre_tax_income,
            adjustment.depreciation,
            adjustment.amortization,
            adjustment.other_tax_adjustments,
            adjustment.taxable_income,
            adjusted_tax_expense,  # Updated tax expense
            final_after_tax_cf,    # Updated after-tax cash flow
            updated_metadata
        )
        
        push!(final_adjustments, final_adjustment)
    end
    
    return final_adjustments
end

"""
    calculate_tax_metrics(tax_adjustments::Vector{TaxAdjustment}) -> Dict{String, Any}

Calculate comprehensive tax metrics for analysis and reporting.
"""
function calculate_tax_metrics(tax_adjustments::Vector{TaxAdjustment})::Dict{String, Any}
    if isempty(tax_adjustments)
        return Dict{String, Any}("message" => "No tax adjustments to analyze")
    end
    
    # Aggregate calculations
    total_pre_tax = sum(adj.pre_tax_income for adj in tax_adjustments)
    total_after_tax = sum(adj.after_tax_cf for adj in tax_adjustments)
    total_tax_expense = sum(adj.income_tax_expense for adj in tax_adjustments)
    total_depreciation = sum(adj.depreciation for adj in tax_adjustments)
    total_amortization = sum(adj.amortization for adj in tax_adjustments)
    
    # Tax efficiency metrics
    effective_tax_rate = total_pre_tax > 0 ? total_tax_expense / total_pre_tax : 0.0
    tax_shield_value = total_depreciation + total_amortization
    
    # Period-by-period analysis
    period_effective_rates = [
        adj.pre_tax_income > 0 ? adj.income_tax_expense / adj.pre_tax_income : 0.0 
        for adj in tax_adjustments
    ]
    
    return Dict{String, Any}(
        "aggregate_metrics" => Dict(
            "total_pre_tax_income" => total_pre_tax,
            "total_after_tax_cf" => total_after_tax,
            "total_tax_expense" => total_tax_expense,
            "total_depreciation" => total_depreciation,
            "total_amortization" => total_amortization,
            "net_tax_impact" => total_pre_tax - total_after_tax
        ),
        "tax_efficiency" => Dict(
            "effective_tax_rate" => effective_tax_rate,
            "tax_shield_value" => tax_shield_value,
            "average_effective_rate" => isempty(period_effective_rates) ? 0.0 : sum(period_effective_rates) / length(period_effective_rates),
            "tax_rate_volatility" => calculate_tax_rate_volatility(period_effective_rates)
        ),
        "analysis_metadata" => Dict(
            "periods_analyzed" => length(tax_adjustments),
            "entities_covered" => length(unique(adj.entity_id for adj in tax_adjustments)),
            "stage_completed" => "tax_processing"
        )
    )
end

"""
    summarize_tax_adjustments(adjustments::Vector{TaxAdjustment}) -> Dict{String, Any}

Create a comprehensive summary of tax adjustments for reporting.
"""
function summarize_tax_adjustments(adjustments::Vector{TaxAdjustment})::Dict{String, Any}
    if isempty(adjustments)
        return Dict{String, Any}("message" => "No tax adjustments to summarize")
    end
    
    total_pre_tax = sum(adj.pre_tax_income for adj in adjustments)
    total_after_tax = sum(adj.after_tax_cf for adj in adjustments)
    total_tax_paid = sum(adj.income_tax_expense for adj in adjustments)
    
    return Dict{String, Any}(
        "summary_metrics" => Dict(
            "total_adjustments" => length(adjustments),
            "total_pre_tax_income" => total_pre_tax,
            "total_after_tax_cf" => total_after_tax,
            "total_tax_expense" => total_tax_paid,
            "average_pre_tax" => total_pre_tax / length(adjustments),
            "average_after_tax" => total_after_tax / length(adjustments),
            "overall_effective_rate" => total_pre_tax > 0 ? total_tax_paid / total_pre_tax : 0.0
        ),
        "tax_deductions" => Dict(
            "total_depreciation" => sum(adj.depreciation for adj in adjustments),
            "total_amortization" => sum(adj.amortization for adj in adjustments),
            "total_other_adjustments" => sum(adj.other_tax_adjustments for adj in adjustments),
            "total_deductions" => sum(adj.depreciation + adj.amortization + adj.other_tax_adjustments for adj in adjustments)
        ),
        "period_coverage" => Dict(
            "start_date" => minimum(adj.period_start for adj in adjustments),
            "end_date" => maximum(adj.period_end for adj in adjustments),
            "entities_covered" => length(unique(adj.entity_id for adj in adjustments))
        ),
        "stage_completed" => "tax_processing"
    )
end

# Helper functions

"""
    is_depreciation_item(item_name::String) -> Bool

Determine if a tax item represents depreciation based on naming conventions.
"""
function is_depreciation_item(item_name::String)::Bool
    item_lower = lowercase(item_name)
    depreciation_keywords = ["depreciation", "deprec", "building_depreciation", "ff&e_depreciation"]
    return any(keyword -> contains(item_lower, keyword), depreciation_keywords)
end

"""
    is_amortization_item(item_name::String) -> Bool

Determine if a tax item represents amortization based on naming conventions.
"""
function is_amortization_item(item_name::String)::Bool
    item_lower = lowercase(item_name)
    amortization_keywords = ["amortization", "amort", "loan_amortization", "intangible_amortization"]
    return any(keyword -> contains(item_lower, keyword), amortization_keywords)
end

"""
    get_applicable_tax_rates(entity_id::String, period_start::Date) -> Dict{String, Float64}

Get applicable tax rates for the entity and time period.
This is a simplified implementation - real systems would have complex tax rate lookups.
"""
function get_applicable_tax_rates(entity_id::String, period_start::Date)::Dict{String, Float64}
    # Simplified tax rates - in practice, this would be much more sophisticated
    year = Dates.year(period_start)
    
    # Example corporate tax rates (simplified)
    federal_rate = if year >= 2018
        0.21  # Post-TCJA federal corporate rate
    else
        0.35  # Pre-TCJA federal corporate rate
    end
    
    state_rate = 0.06  # Example state rate (varies by state)
    local_rate = 0.01  # Example local rate
    
    combined_rate = federal_rate + state_rate + local_rate
    
    return Dict{String, Float64}(
        "federal_rate" => federal_rate,
        "state_rate" => state_rate,
        "local_rate" => local_rate,
        "combined_rate" => combined_rate
    )
end

"""
    calculate_income_tax(taxable_income::Float64, tax_rates::Dict{String, Float64}) -> Float64

Calculate income tax expense based on taxable income and applicable rates.
"""
function calculate_income_tax(taxable_income::Float64, tax_rates::Dict{String, Float64})::Float64
    if taxable_income <= 0
        return 0.0
    end
    
    # Apply combined tax rate to positive taxable income
    return taxable_income * tax_rates["combined_rate"]
end

"""
    calculate_tax_credits(adjustment::TaxAdjustment) -> Float64

Calculate applicable tax credits for the adjustment period.
This is a simplified implementation - real systems would have complex credit calculations.
"""
function calculate_tax_credits(adjustment::TaxAdjustment)::Float64
    # Simplified tax credit calculation
    # In practice, this would consider various credits like:
    # - Low-Income Housing Tax Credits (LIHTC)
    # - Historic Tax Credits
    # - Energy Efficiency Credits
    # - Opportunity Zone benefits
    
    base_credit = 0.0
    
    # Example: Small credit based on depreciation (simplified)
    if adjustment.depreciation > 0
        base_credit = adjustment.depreciation * 0.02  # 2% credit on depreciation
    end
    
    return base_credit
end

"""
    calculate_tax_rate_volatility(rates::Vector{Float64}) -> Float64

Calculate the volatility (standard deviation) of effective tax rates across periods.
"""
function calculate_tax_rate_volatility(rates::Vector{Float64})::Float64
    if length(rates) < 2
        return 0.0
    end
    
    mean_rate = sum(rates) / length(rates)
    variance = sum((rate - mean_rate)^2 for rate in rates) / (length(rates) - 1)
    
    return sqrt(variance)
end

"""
    validate_tax_adjustment(adjustment::TaxAdjustment) -> Dict{String, Any}

Validate a tax adjustment for data quality and business logic.
"""
function validate_tax_adjustment(adjustment::TaxAdjustment)::Dict{String, Any}
    warnings = String[]
    errors = String[]
    
    # Check for unusual effective tax rates
    if adjustment.pre_tax_income > 0
        effective_rate = adjustment.income_tax_expense / adjustment.pre_tax_income
        if effective_rate > 0.50
            push!(warnings, "High effective tax rate: $(round(effective_rate * 100, digits=1))%")
        elseif effective_rate < 0
            push!(warnings, "Negative effective tax rate: $(round(effective_rate * 100, digits=1))%")
        end
    end
    
    # Validate taxable income calculation
    expected_taxable = adjustment.pre_tax_income - adjustment.depreciation - adjustment.amortization - adjustment.other_tax_adjustments
    if abs(expected_taxable - adjustment.taxable_income) > 0.01
        push!(errors, "Taxable income calculation error: expected=$expected_taxable, actual=$(adjustment.taxable_income)")
    end
    
    # Check for negative depreciation/amortization (should be positive deductions)
    if adjustment.depreciation < 0
        push!(warnings, "Negative depreciation: $(adjustment.depreciation)")
    end
    if adjustment.amortization < 0
        push!(warnings, "Negative amortization: $(adjustment.amortization)")
    end
    
    return Dict{String, Any}(
        "is_valid" => isempty(errors),
        "warnings" => warnings,
        "errors" => errors,
        "validation_timestamp" => now()
    )
end

"""
    calculate_effective_tax_rate(taxable_income::Float64, total_tax::Float64) -> Float64

Calculate the effective tax rate given taxable income and total tax.
"""
function calculate_effective_tax_rate(taxable_income::Float64, total_tax::Float64)::Float64
    return taxable_income != 0.0 ? total_tax / taxable_income : 0.0
end