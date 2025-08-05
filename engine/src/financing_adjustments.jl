# Stage 4: Financing Adjustments
# Applies leverage and financing costs (debt service, interest, etc.)

using Dates

"""
    FinancingAdjustment

Financing costs and leverage adjustments following real estate industry standards.

Key Components:
- Debt Service: Principal payments, interest expense, loan fees
- Other Financing: Preferred returns, financing costs
- Unlevered vs Levered Cash Flow: Critical distinction for valuation
"""
struct FinancingAdjustment
    entity_id::String
    period_start::Date
    period_end::Date
    debt_service::Dict{String, Float64}
    other_financing::Dict{String, Float64}
    unlevered_cf::Float64
    levered_cf::Float64
    metadata::Dict{String, Any}
end

"""
    apply_financing_adjustments(operating_statements::Vector{OperatingStatement}, cash_flow_entries::Vector{CashFlowEntry}) -> Vector{FinancingAdjustment}

Apply debt service and financing costs to calculate levered cash flows.

This is Stage 4 of the 7-stage cash flow pipeline:
1. Stream Collection ✓
2. Cash Flow Assembly ✓
3. Operating Statement Generation ✓
4. **Financing Adjustments** ← This stage
5. Tax Processing
6. Available Cash Calculation
7. Statement Views

Financing structure follows real estate industry standards:
```
DEBT SERVICE:
- Principal Payments
- Interest Expense  
- Loan Fees

OTHER FINANCING:
- Preferred Returns
- Financing Costs

= UNLEVERED vs LEVERED CASH FLOW
```
"""
function apply_financing_adjustments(operating_statements::Vector{OperatingStatement}, cash_flow_entries::Vector{CashFlowEntry})::Vector{FinancingAdjustment}
    financing_adjustments = Vector{FinancingAdjustment}()
    
    # Create a lookup for operating statements by entity and period
    operating_lookup = Dict{Tuple{String, Date, Date}, OperatingStatement}()
    for stmt in operating_statements
        key = (stmt.entity_id, stmt.period_start, stmt.period_end)
        operating_lookup[key] = stmt
    end
    
    # Process each cash flow entry for financing items
    for entry in cash_flow_entries
        # Find corresponding operating statement
        key = (entry.entity_id, entry.period_start, entry.period_end)
        operating_stmt = get(operating_lookup, key, nothing)
        
        # Start with NOI (unlevered cash flow)
        unlevered_cf = if operating_stmt !== nothing
            operating_stmt.net_operating_income
        else
            # Fallback: calculate from operating items
            calculate_noi_from_entry(entry)
        end
        
        # Process financing items with industry-standard categorization
        debt_service = Dict{String, Float64}()
        other_financing = Dict{String, Float64}()
        
        for (item_name, amount) in entry.financing_items
            if is_debt_service_item(item_name)
                debt_service[item_name] = amount
            else
                other_financing[item_name] = amount
            end
        end
        
        # Calculate total financing costs
        total_debt_service = sum(values(debt_service))
        total_other_financing = sum(values(other_financing))
        total_financing_costs = total_debt_service + total_other_financing
        
        # Calculate levered cash flow (NOI minus financing costs)
        levered_cf = unlevered_cf - total_financing_costs
        
        # Create comprehensive metadata for analysis
        metadata = Dict{String, Any}(
            "total_debt_service" => total_debt_service,
            "total_other_financing" => total_other_financing,
            "total_financing_costs" => total_financing_costs,
            "debt_service_coverage_ratio" => unlevered_cf > 0 ? unlevered_cf / max(total_debt_service, 0.001) : 0.0,
            "financing_coverage_ratio" => unlevered_cf > 0 ? total_financing_costs / unlevered_cf : 0.0,
            "leverage_impact" => unlevered_cf > 0 ? (unlevered_cf - levered_cf) / unlevered_cf : 0.0,
            "stage" => "financing_adjustments",
            "debt_service_items" => length(debt_service),
            "other_financing_items" => length(other_financing)
        )
        
        adjustment = FinancingAdjustment(
            entry.entity_id,
            entry.period_start,
            entry.period_end,
            debt_service,
            other_financing,
            unlevered_cf,
            levered_cf,
            metadata
        )
        
        push!(financing_adjustments, adjustment)
    end
    
    return financing_adjustments
end

"""
    calculate_debt_metrics(financing_adjustments::Vector{FinancingAdjustment}) -> Dict{String, Any}

Calculate comprehensive debt coverage and financing metrics for analysis.

Key metrics include:
- Debt Service Coverage Ratio (DSCR): Critical for lending decisions
- Leverage Factor: Impact of debt on returns
- Financing Cost Analysis: Breakdown of financing expenses
"""
function calculate_debt_metrics(financing_adjustments::Vector{FinancingAdjustment})::Dict{String, Any}
    if isempty(financing_adjustments)
        return Dict{String, Any}("message" => "No financing adjustments to analyze")
    end
    
    # Calculate aggregate metrics
    total_unlevered_cf = sum(adj.unlevered_cf for adj in financing_adjustments)
    total_levered_cf = sum(adj.levered_cf for adj in financing_adjustments)
    total_debt_service = sum(sum(values(adj.debt_service)) for adj in financing_adjustments)
    total_other_financing = sum(sum(values(adj.other_financing)) for adj in financing_adjustments)
    total_financing_costs = total_debt_service + total_other_financing
    
    # Key real estate metrics
    dscr = total_debt_service > 0 ? total_unlevered_cf / total_debt_service : 0.0
    leverage_factor = total_unlevered_cf > 0 ? total_levered_cf / total_unlevered_cf : 0.0
    financing_burden = total_unlevered_cf > 0 ? total_financing_costs / total_unlevered_cf : 0.0
    
    # Period-by-period DSCR analysis
    period_dscrs = Float64[]
    for adj in financing_adjustments
        period_debt_service = sum(values(adj.debt_service))
        if period_debt_service > 0
            period_dscr = adj.unlevered_cf / period_debt_service
            push!(period_dscrs, period_dscr)
        end
    end
    
    return Dict{String, Any}(
        "aggregate_metrics" => Dict(
            "total_unlevered_cf" => total_unlevered_cf,
            "total_levered_cf" => total_levered_cf,
            "total_debt_service" => total_debt_service,
            "total_other_financing" => total_other_financing,
            "total_financing_costs" => total_financing_costs
        ),
        "key_ratios" => Dict(
            "debt_service_coverage_ratio" => dscr,
            "leverage_factor" => leverage_factor,
            "financing_burden_ratio" => financing_burden,
            "average_period_dscr" => isempty(period_dscrs) ? 0.0 : sum(period_dscrs) / length(period_dscrs),
            "minimum_period_dscr" => isempty(period_dscrs) ? 0.0 : minimum(period_dscrs),
            "maximum_period_dscr" => isempty(period_dscrs) ? 0.0 : maximum(period_dscrs)
        ),
        "analysis_metadata" => Dict(
            "periods_analyzed" => length(financing_adjustments),
            "periods_with_debt_service" => length(period_dscrs),
            "dscr_trend" => calculate_dscr_trend(period_dscrs),
            "stage_completed" => "financing_adjustments"
        )
    )
end

"""
    summarize_financing_adjustments(adjustments::Vector{FinancingAdjustment}) -> Dict{String, Any}

Create a comprehensive summary of financing adjustments for reporting.
"""
function summarize_financing_adjustments(adjustments::Vector{FinancingAdjustment})::Dict{String, Any}
    if isempty(adjustments)
        return Dict{String, Any}("message" => "No financing adjustments to summarize")
    end
    
    # Aggregate all debt service items
    all_debt_service = Dict{String, Float64}()
    all_other_financing = Dict{String, Float64}()
    
    for adj in adjustments
        for (item, amount) in adj.debt_service
            all_debt_service[item] = get(all_debt_service, item, 0.0) + amount
        end
        for (item, amount) in adj.other_financing
            all_other_financing[item] = get(all_other_financing, item, 0.0) + amount
        end
    end
    
    total_unlevered = sum(adj.unlevered_cf for adj in adjustments)
    total_levered = sum(adj.levered_cf for adj in adjustments)
    
    return Dict{String, Any}(
        "summary_metrics" => Dict(
            "total_adjustments" => length(adjustments),
            "total_unlevered_cf" => total_unlevered,
            "total_levered_cf" => total_levered,
            "leverage_impact" => total_unlevered - total_levered,
            "average_unlevered_cf" => total_unlevered / length(adjustments),
            "average_levered_cf" => total_levered / length(adjustments)
        ),
        "financing_breakdown" => Dict(
            "debt_service_items" => all_debt_service,
            "other_financing_items" => all_other_financing,
            "total_debt_service" => sum(values(all_debt_service)),
            "total_other_financing" => sum(values(all_other_financing))
        ),
        "coverage_analysis" => calculate_debt_metrics(adjustments)["key_ratios"],
        "period_coverage" => Dict(
            "start_date" => minimum(adj.period_start for adj in adjustments),
            "end_date" => maximum(adj.period_end for adj in adjustments),
            "entities_covered" => length(unique(adj.entity_id for adj in adjustments))
        ),
        "stage_completed" => "financing_adjustments"
    )
end

# Helper functions

"""
    calculate_noi_from_entry(entry::CashFlowEntry) -> Float64

Calculate NOI from a cash flow entry's operating items as fallback.
"""
function calculate_noi_from_entry(entry::CashFlowEntry)::Float64
    total_revenue = 0.0
    total_expenses = 0.0
    
    for (item_name, amount) in entry.operating_items
        if amount > 0
            total_revenue += amount
        else
            total_expenses += abs(amount)
        end
    end
    
    return total_revenue - total_expenses
end

"""
    is_debt_service_item(item_name::String) -> Bool

Determine if a financing item represents debt service based on industry naming conventions.
"""
function is_debt_service_item(item_name::String)::Bool
    item_lower = lowercase(item_name)
    
    debt_service_keywords = [
        "debt", "interest", "principal", "loan", "mortgage", 
        "debt_service", "interest_expense", "principal_payment",
        "loan_fee", "origination", "servicing"
    ]
    
    return any(keyword -> contains(item_lower, keyword), debt_service_keywords)
end

"""
    calculate_dscr_trend(dscrs::Vector{Float64}) -> String

Analyze the trend in Debt Service Coverage Ratios over time.
"""
function calculate_dscr_trend(dscrs::Vector{Float64})::String
    if length(dscrs) < 2
        return "insufficient_data"
    end
    
    # Trend analysis based on overall direction and volatility
    first_value = dscrs[1]
    last_value = dscrs[end]
    
    # Calculate the overall change
    overall_change = last_value - first_value
    change_threshold = 0.05  # 5% threshold for significant change
    
    # Count period-by-period changes for volatility assessment
    increasing_periods = 0
    decreasing_periods = 0
    
    for i in 2:length(dscrs)
        if dscrs[i] > dscrs[i-1]
            increasing_periods += 1
        elseif dscrs[i] < dscrs[i-1]
            decreasing_periods += 1
        end
    end
    
    # If overall change is minimal and there's mixed volatility, it's stable
    if abs(overall_change) < change_threshold && increasing_periods > 0 && decreasing_periods > 0
        return "stable"
    elseif overall_change > change_threshold
        return "improving"
    elseif overall_change < -change_threshold
        return "declining"
    else
        return "stable"
    end
end

"""
    validate_financing_adjustment(adjustment::FinancingAdjustment) -> Dict{String, Any}

Validate a financing adjustment for data quality and business logic.
"""
function validate_financing_adjustment(adjustment::FinancingAdjustment)::Dict{String, Any}
    warnings = String[]
    errors = String[]
    
    # Check for negative unlevered cash flow (unusual but possible)
    if adjustment.unlevered_cf < 0
        push!(warnings, "Negative unlevered cash flow: $(adjustment.unlevered_cf)")
    end
    
    # Validate levered calculation
    total_financing = sum(values(adjustment.debt_service)) + sum(values(adjustment.other_financing))
    expected_levered = adjustment.unlevered_cf - total_financing
    
    if abs(expected_levered - adjustment.levered_cf) > 0.01
        push!(errors, "Levered CF calculation error: expected=$expected_levered, actual=$(adjustment.levered_cf)")
    end
    
    # Check DSCR thresholds (typical minimum is 1.20x for commercial real estate)
    debt_service_total = sum(values(adjustment.debt_service))
    if debt_service_total > 0
        dscr = adjustment.unlevered_cf / debt_service_total
        if dscr < 1.20
            push!(warnings, "Low DSCR: $(round(dscr, digits=2))x (typical minimum: 1.20x)")
        end
    end
    
    return Dict{String, Any}(
        "is_valid" => isempty(errors),
        "warnings" => warnings,
        "errors" => errors,
        "validation_timestamp" => now()
    )
end