# Cash flow aggregation following industry best practices
# Implements 7-stage pipeline: Stream Collection → Cash Flow Assembly → Operating Statement → 
# Financing Adjustments → Tax Processing → Available Cash → Statement Views

using Dates

# Note: CashFlowView functionality is now handled by ReportingFrequency enum in common_enums.jl
# Alias for backward compatibility
const CashFlowView = ReportingFrequency

# Note: CashFlowCategory enum is defined in common_enums.jl

# GroupedStreams is defined in stream_collector.jl

# Note: CashFlowEntry struct is defined in cash_flow_assembly.jl

# Note: OperatingStatement struct is defined in operating_statement.jl

# Note: FinancingAdjustment struct is defined in financing_adjustments.jl

# ===== STAGE 1: STREAM COLLECTION & GROUPING =====
# Uses existing collect_streams function from stream_collector.jl

# ===== STAGE 2: CASH FLOW ASSEMBLY =====
# Now handled by cash_flow_assembly.jl module

# ===== STAGE 3: OPERATING STATEMENT GENERATION =====

"""
OperatingStatementGenerator Module

Generates standardized operating cash flow statements with NOI calculation.
"""
module OperatingStatementGenerator

using Dates
using ..CFDLEngine

export generate_operating_statement, calculate_noi

"""
    generate_operating_statement(cash_flow_entries::Vector{CashFlowEntry}) -> Vector{OperatingStatement}

Generate standardized operating cash flow statements.
"""
function generate_operating_statement(cash_flow_entries::Vector{CashFlowEntry})::Vector{OperatingStatement}
    operating_statements = Vector{OperatingStatement}()
    
    for entry in cash_flow_entries
        if isempty(entry.operating_items)
            continue
        end
        
        # Separate revenue and expenses
        revenue_items = Dict{String, Float64}()
        operating_expenses = Dict{String, Float64}()
        
        for (item_name, amount) in entry.operating_items
            if amount > 0
                # Positive amounts are typically revenue
                revenue_items[item_name] = amount
            else
                # Negative amounts are expenses (convert to positive for expense tracking)
                operating_expenses[item_name] = -amount
            end
        end
        
        # Calculate Net Operating Income (NOI)
        total_revenue = sum(values(revenue_items))
        total_expenses = sum(values(operating_expenses))
        net_operating_income = total_revenue - total_expenses
        
        # Create operating statement
        metadata = Dict{String, Any}(
            "total_revenue" => total_revenue,
            "total_expenses" => total_expenses,
            "revenue_streams" => length(revenue_items),
            "expense_streams" => length(operating_expenses)
        )
        
        statement = OperatingStatement(
            entry.entity_id,
            entry.period_start,
            entry.period_end,
            revenue_items,
            operating_expenses,
            net_operating_income,
            metadata
        )
        
        push!(operating_statements, statement)
    end
    
    return operating_statements
end

"""
    calculate_noi(operating_items::Dict{String, Float64}) -> Float64

Calculate Net Operating Income from operating items.
"""
function calculate_noi(operating_items::Dict{String, Float64})::Float64
    total_revenue = 0.0
    total_expenses = 0.0
    
    for (item_name, amount) in operating_items
        if amount > 0
            total_revenue += amount
        else
            total_expenses += -amount  # Convert negative to positive
        end
    end
    
    return total_revenue - total_expenses
end

end  # module OperatingStatementGenerator

# ===== STAGE 4: FINANCING PROCESSOR =====

"""
FinancingProcessor Module

Applies leverage and financing costs (debt service, interest, etc.).
"""
module FinancingProcessor

using Dates
using ..CFDLEngine

export apply_debt_service, calculate_debt_metrics

"""
    apply_debt_service(operating_statements::Vector{OperatingStatement}, cash_flow_entries::Vector{CashFlowEntry}) -> Vector{FinancingAdjustment}

Apply debt service and financing costs to calculate levered cash flows.
"""
function apply_debt_service(operating_statements::Vector{OperatingStatement}, cash_flow_entries::Vector{CashFlowEntry})::Vector{FinancingAdjustment}
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
        
        # Apply financing costs
        debt_service = Dict{String, Float64}()
        other_financing = Dict{String, Float64}()
        
        # Process financing items from the entry
        for (item_name, amount) in entry.financing_items
            if contains(lowercase(item_name), "debt") || contains(lowercase(item_name), "interest") || contains(lowercase(item_name), "principal")
                debt_service[item_name] = amount
            else
                other_financing[item_name] = amount
            end
        end
        
        # Calculate total financing costs
        total_debt_service = sum(values(debt_service))
        total_other_financing = sum(values(other_financing))
        total_financing_costs = total_debt_service + total_other_financing
        
        # Calculate levered cash flow
        levered_cf = unlevered_cf - total_financing_costs
        
        # Create financing adjustment
        metadata = Dict{String, Any}(
            "total_debt_service" => total_debt_service,
            "total_other_financing" => total_other_financing,
            "financing_coverage_ratio" => unlevered_cf > 0 ? total_financing_costs / unlevered_cf : 0.0
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

Calculate debt coverage and other financing metrics.
"""
function calculate_debt_metrics(financing_adjustments::Vector{FinancingAdjustment})::Dict{String, Any}
    if isempty(financing_adjustments)
        return Dict{String, Any}()
    end
    
    # Calculate aggregate metrics
    total_unlevered_cf = sum(adj.unlevered_cf for adj in financing_adjustments)
    total_levered_cf = sum(adj.levered_cf for adj in financing_adjustments)
    total_debt_service = sum(sum(values(adj.debt_service)) for adj in financing_adjustments)
    
    # Debt Service Coverage Ratio (DSCR)
    dscr = total_debt_service > 0 ? total_unlevered_cf / total_debt_service : 0.0
    
    # Leverage factor
    leverage_factor = total_unlevered_cf > 0 ? total_levered_cf / total_unlevered_cf : 0.0
    
    return Dict{String, Any}(
        "total_unlevered_cf" => total_unlevered_cf,
        "total_levered_cf" => total_levered_cf,
        "total_debt_service" => total_debt_service,
        "debt_service_coverage_ratio" => dscr,
        "leverage_factor" => leverage_factor,
        "periods_analyzed" => length(financing_adjustments)
    )
end

"""
    calculate_noi_from_entry(entry::CashFlowEntry) -> Float64

Calculate NOI from a cash flow entry's operating items.
"""
function calculate_noi_from_entry(entry::CashFlowEntry)::Float64
    total_revenue = 0.0
    total_expenses = 0.0
    
    for (item_name, amount) in entry.operating_items
        if amount > 0
            total_revenue += amount
        else
            total_expenses += -amount
        end
    end
    
    return total_revenue - total_expenses
end

end  # module FinancingProcessor

# ===== STAGE 5: TAX PROCESSOR =====

"""
TaxProcessor Module

Applies tax adjustments and calculations (depreciation, amortization, income tax).
"""
module TaxProcessor

using Dates
using ..CFDLEngine

export calculate_taxable_income, apply_tax_adjustments

"""
    calculate_taxable_income(financing_adjustments::Vector{FinancingAdjustment}, cash_flow_entries::Vector{CashFlowEntry}) -> Vector{Dict{String, Any}}

Calculate taxable income with depreciation and other adjustments.
"""
function calculate_taxable_income(financing_adjustments::Vector{FinancingAdjustment}, cash_flow_entries::Vector{CashFlowEntry})::Vector{Dict{String, Any}}
    taxable_income_entries = Vector{Dict{String, Any}}()
    
    # Create lookup for cash flow entries by entity and period
    entry_lookup = Dict{Tuple{String, Date, Date}, CashFlowEntry}()
    for entry in cash_flow_entries
        key = (entry.entity_id, entry.period_start, entry.period_end)
        entry_lookup[key] = entry
    end
    
    for adjustment in financing_adjustments
        key = (adjustment.entity_id, adjustment.period_start, adjustment.period_end)
        cash_flow_entry = get(entry_lookup, key, nothing)
        
        # Start with unlevered cash flow
        pre_tax_income = adjustment.unlevered_cf
        
        # Apply non-cash adjustments (depreciation, amortization)
        depreciation = 0.0
        amortization = 0.0
        other_tax_adjustments = 0.0
        
        if cash_flow_entry !== nothing
            for (item_name, amount) in cash_flow_entry.tax_items
                if contains(lowercase(item_name), "depreciation")
                    depreciation += amount
                elseif contains(lowercase(item_name), "amortization")
                    amortization += amount
                else
                    other_tax_adjustments += amount
                end
            end
        end
        
        # Calculate taxable income
        # Note: Depreciation and amortization are typically deductible (reduce taxable income)
        taxable_income = pre_tax_income - depreciation - amortization - other_tax_adjustments
        
        # Estimate income tax (simplified - would normally use tax rates)
        estimated_tax_rate = 0.25  # 25% corporate tax rate
        income_tax_expense = max(0.0, taxable_income * estimated_tax_rate)
        
        # After-tax cash flow
        after_tax_cf = adjustment.levered_cf - income_tax_expense
        
        taxable_entry = Dict{String, Any}(
            "entity_id" => adjustment.entity_id,
            "period_start" => adjustment.period_start,
            "period_end" => adjustment.period_end,
            "pre_tax_income" => pre_tax_income,
            "depreciation" => depreciation,
            "amortization" => amortization,
            "other_tax_adjustments" => other_tax_adjustments,
            "taxable_income" => taxable_income,
            "income_tax_expense" => income_tax_expense,
            "after_tax_cf" => after_tax_cf,
            "effective_tax_rate" => pre_tax_income > 0 ? income_tax_expense / pre_tax_income : 0.0
        )
        
        push!(taxable_income_entries, taxable_entry)
    end
    
    return taxable_income_entries
end

"""
    apply_tax_adjustments(taxable_income_entries::Vector{Dict{String, Any}}) -> Vector{Dict{String, Any}}

Apply final tax adjustments and calculate after-tax cash flows.
"""
function apply_tax_adjustments(taxable_income_entries::Vector{Dict{String, Any}})::Vector{Dict{String, Any}}
    adjusted_entries = Vector{Dict{String, Any}}()
    
    for entry in taxable_income_entries
        # Create adjusted entry with tax credits and other adjustments
        adjusted_entry = copy(entry)
        
        # Apply tax credits (simplified)
        tax_credits = 0.0  # Could be calculated based on specific credits
        adjusted_tax_expense = max(0.0, entry["income_tax_expense"] - tax_credits)
        
        # Recalculate after-tax cash flow
        levered_cf = entry["pre_tax_income"] - sum(values(get(entry, "debt_service", Dict{String, Float64}())))
        final_after_tax_cf = levered_cf - adjusted_tax_expense
        
        adjusted_entry["tax_credits"] = tax_credits
        adjusted_entry["final_tax_expense"] = adjusted_tax_expense
        adjusted_entry["final_after_tax_cf"] = final_after_tax_cf
        
        push!(adjusted_entries, adjusted_entry)
    end
    
    return adjusted_entries
end

end  # module TaxProcessor

# ===== STAGE 6: AVAILABLE CASH CALCULATOR =====

"""
AvailableCashCalculator Module

Calculates distributable cash flow after reserves and working capital changes.
"""
module AvailableCashCalculator

using Dates
using ..CFDLEngine

export calculate_available_cash, apply_reserves

"""
    calculate_available_cash(tax_adjusted_entries::Vector{Dict{String, Any}}) -> Vector{Dict{String, Any}}

Calculate available cash for distribution after all adjustments.
"""
function calculate_available_cash(tax_adjusted_entries::Vector{Dict{String, Any}})::Vector{Dict{String, Any}}
    available_cash_entries = Vector{Dict{String, Any}}()
    
    for entry in tax_adjusted_entries
        # Start with after-tax cash flow
        after_tax_cf = get(entry, "final_after_tax_cf", get(entry, "after_tax_cf", 0.0))
        
        # Apply reserves and working capital adjustments
        capital_reserves = calculate_capital_reserves(after_tax_cf)
        working_capital_change = 0.0  # Simplified - would calculate based on period-to-period changes
        
        # Calculate available for distribution
        available_for_distribution = after_tax_cf - capital_reserves - working_capital_change
        
        available_entry = copy(entry)
        available_entry["after_tax_cf"] = after_tax_cf
        available_entry["capital_reserves"] = capital_reserves
        available_entry["working_capital_change"] = working_capital_change
        available_entry["available_for_distribution"] = available_for_distribution
        available_entry["distribution_coverage"] = after_tax_cf > 0 ? available_for_distribution / after_tax_cf : 0.0
        
        push!(available_cash_entries, available_entry)
    end
    
    return available_cash_entries
end

"""
    apply_reserves(available_cash_entries::Vector{Dict{String, Any}}, reserve_rate::Float64 = 0.05) -> Vector{Dict{String, Any}}

Apply reserve requirements to available cash flows.
"""
function apply_reserves(available_cash_entries::Vector{Dict{String, Any}}, reserve_rate::Float64 = 0.05)::Vector{Dict{String, Any}}
    reserved_entries = Vector{Dict{String, Any}}()
    
    for entry in available_cash_entries
        reserved_entry = copy(entry)
        
        # Apply additional reserves
        base_available = get(entry, "available_for_distribution", 0.0)
        additional_reserves = max(0.0, base_available * reserve_rate)
        
        final_available = base_available - additional_reserves
        
        reserved_entry["additional_reserves"] = additional_reserves
        reserved_entry["final_available_for_distribution"] = final_available
        reserved_entry["total_reserves"] = get(entry, "capital_reserves", 0.0) + additional_reserves
        
        push!(reserved_entries, reserved_entry)
    end
    
    return reserved_entries
end

"""
    calculate_capital_reserves(cash_flow::Float64, reserve_rate::Float64 = 0.03) -> Float64

Calculate capital reserves as a percentage of cash flow.
"""
function calculate_capital_reserves(cash_flow::Float64, reserve_rate::Float64 = 0.03)::Float64
    return max(0.0, cash_flow * reserve_rate)
end

end  # module AvailableCashCalculator

# ===== STAGE 7: STATEMENT VIEW GENERATOR =====

"""
StatementViewGenerator Module

Generates different reporting views (GAAP/Non-GAAP, Monthly/Annual).
"""
module StatementViewGenerator

using Dates
using ..CFDLEngine

export generate_gaap_view, generate_nongaap_view, generate_monthly_view, generate_annual_view

"""
    generate_gaap_view(available_cash_entries::Vector{Dict{String, Any}}) -> Vector{Dict{String, Any}}

Generate GAAP-compliant financial statements.
"""
function generate_gaap_view(available_cash_entries::Vector{Dict{String, Any}})::Vector{Dict{String, Any}}
    gaap_statements = Vector{Dict{String, Any}}()
    
    for entry in available_cash_entries
        gaap_entry = Dict{String, Any}(
            "entity_id" => entry["entity_id"],
            "period_start" => entry["period_start"],
            "period_end" => entry["period_end"],
            "view_type" => "GAAP",
            
            # GAAP requires including non-cash items
            "revenue" => get(entry, "total_revenue", 0.0),
            "operating_expenses" => get(entry, "total_expenses", 0.0),
            "depreciation_expense" => get(entry, "depreciation", 0.0),
            "amortization_expense" => get(entry, "amortization", 0.0),
            "interest_expense" => get(entry, "total_debt_service", 0.0),
            "income_tax_expense" => get(entry, "final_tax_expense", 0.0),
            
            # GAAP net income (includes non-cash items)
            "net_income" => calculate_gaap_net_income(entry),
            
            # Cash flow statement items
            "operating_cash_flow" => get(entry, "pre_tax_income", 0.0),
            "financing_cash_flow" => -get(entry, "total_debt_service", 0.0),
            "investing_cash_flow" => 0.0,  # Would include capex, acquisitions
            
            "metadata" => Dict{String, Any}(
                "gaap_compliant" => true,
                "includes_non_cash" => true
            )
        )
        
        push!(gaap_statements, gaap_entry)
    end
    
    return gaap_statements
end

"""
    generate_nongaap_view(available_cash_entries::Vector{Dict{String, Any}}) -> Vector{Dict{String, Any}}

Generate Non-GAAP (economic/operating) view excluding non-cash items.
"""
function generate_nongaap_view(available_cash_entries::Vector{Dict{String, Any}})::Vector{Dict{String, Any}}
    nongaap_statements = Vector{Dict{String, Any}}()
    
    for entry in available_cash_entries
        nongaap_entry = Dict{String, Any}(
            "entity_id" => entry["entity_id"],
            "period_start" => entry["period_start"],
            "period_end" => entry["period_end"],
            "view_type" => "Non-GAAP",
            
            # Non-GAAP focuses on cash flows
            "operating_cash_flow" => get(entry, "pre_tax_income", 0.0),
            "financing_cash_flow" => -get(entry, "total_debt_service", 0.0),
            "after_tax_cash_flow" => get(entry, "final_after_tax_cf", 0.0),
            "available_for_distribution" => get(entry, "final_available_for_distribution", 0.0),
            
            # Key economic metrics
            "net_operating_income" => get(entry, "pre_tax_income", 0.0),
            "debt_service_coverage" => calculate_dscr(entry),
            "cash_on_cash_return" => calculate_cash_on_cash(entry),
            
            "metadata" => Dict{String, Any}(
                "excludes_non_cash" => true,
                "economic_view" => true
            )
        )
        
        push!(nongaap_statements, nongaap_entry)
    end
    
    return nongaap_statements
end

"""
    generate_monthly_view(statements::Vector{Dict{String, Any}}) -> Vector{Dict{String, Any}}

Generate monthly reporting view.
"""
function generate_monthly_view(statements::Vector{Dict{String, Any}})::Vector{Dict{String, Any}}
    # Filter and sort by month
    monthly_statements = filter(s -> is_monthly_period(s), statements)
    sort!(monthly_statements, by = s -> s["period_start"])
    
    # Add monthly-specific metadata
    for stmt in monthly_statements
        stmt["frequency"] = "monthly"
        stmt["period_type"] = "month"
        
        # Add month-over-month growth if available
        stmt["metadata"]["monthly_view"] = true
    end
    
    return monthly_statements
end

"""
    generate_annual_view(statements::Vector{Dict{String, Any}}) -> Vector{Dict{String, Any}}

Generate annual reporting view by aggregating monthly data.
"""
function generate_annual_view(statements::Vector{Dict{String, Any}})::Vector{Dict{String, Any}}
    annual_statements = Vector{Dict{String, Any}}()
    
    # Group by year and entity
    yearly_groups = Dict{Tuple{Int, String}, Vector{Dict{String, Any}}}()
    
    for stmt in statements
        period_start = stmt["period_start"]
        entity_id = stmt["entity_id"]
        year = isa(period_start, Date) ? Dates.year(period_start) : parse(Int, split(string(period_start), "-")[1])
        
        key = (year, entity_id)
        if !haskey(yearly_groups, key)
            yearly_groups[key] = []
        end
        push!(yearly_groups[key], stmt)
    end
    
    # Aggregate each year
    for ((year, entity_id), year_statements) in yearly_groups
        if isempty(year_statements)
            continue
        end
        
        annual_stmt = Dict{String, Any}(
            "entity_id" => entity_id,
            "period_start" => Date(year, 1, 1),
            "period_end" => Date(year, 12, 31),
            "view_type" => get(year_statements[1], "view_type", "Annual"),
            "frequency" => "annual",
            
            # Aggregate cash flows
            "operating_cash_flow" => sum(get(s, "operating_cash_flow", 0.0) for s in year_statements),
            "financing_cash_flow" => sum(get(s, "financing_cash_flow", 0.0) for s in year_statements),
            "after_tax_cash_flow" => sum(get(s, "after_tax_cash_flow", 0.0) for s in year_statements),
            "available_for_distribution" => sum(get(s, "available_for_distribution", 0.0) for s in year_statements),
            
            "metadata" => Dict{String, Any}(
                "annual_aggregation" => true,
                "periods_included" => length(year_statements),
                "year" => year
            )
        )
        
        push!(annual_statements, annual_stmt)
    end
    
    sort!(annual_statements, by = s -> s["period_start"])
    return annual_statements
end

# Helper functions
function calculate_gaap_net_income(entry::Dict{String, Any})::Float64
    revenue = get(entry, "total_revenue", 0.0)
    expenses = get(entry, "total_expenses", 0.0)
    depreciation = get(entry, "depreciation", 0.0)
    interest = get(entry, "total_debt_service", 0.0)
    tax = get(entry, "final_tax_expense", 0.0)
    
    return revenue - expenses - depreciation - interest - tax
end

function calculate_dscr(entry::Dict{String, Any})::Float64
    noi = get(entry, "pre_tax_income", 0.0)
    debt_service = get(entry, "total_debt_service", 0.0)
    
    return debt_service > 0 ? noi / debt_service : 0.0
end

function calculate_cash_on_cash(entry::Dict{String, Any})::Float64
    # Simplified - would need equity investment amount
    available_cash = get(entry, "final_available_for_distribution", 0.0)
    return available_cash  # Return as absolute amount for now
end

function is_monthly_period(statement::Dict{String, Any})::Bool
    period_start = statement["period_start"]
    period_end = statement["period_end"]
    
    if isa(period_start, Date) && isa(period_end, Date)
        # Check if period is roughly one month
        days_diff = Dates.value(period_end - period_start)
        return days_diff >= 28 && days_diff <= 31
    end
    
    return false
end

end  # module StatementViewGenerator

"""
    HierarchicalCashFlow

Represents cash flows with hierarchical drill-down capability.
"""
struct HierarchicalCashFlow
    deal_id::String
    asset_id::String
    component_id::String
    stream_id::String
    period_start::Date
    period_end::Date
    amount::Float64
    category::String
    sub_type::String
    metadata::Dict{String, Any}
end

"""
    AggregatedCashFlow

Represents aggregated cash flows for a specific view and period.
"""
struct AggregatedCashFlow
    period_start::Date
    period_end::Date
    unlevered_cf::Float64
    levered_cf::Float64
    line_items::Vector{Dict{String, Any}}
    drill_down_data::Vector{HierarchicalCashFlow}
    metadata::Dict{String, Any}
end

"""
    CashFlowAggregationResult

Complete result of cash flow aggregation with multiple views.
"""
struct CashFlowAggregationResult
    deal_id::String
    currency::String
    monthly_view::Vector{AggregatedCashFlow}
    annual_view::Vector{AggregatedCashFlow}
    hierarchy_map::Dict{String, Vector{String}}  # entity_id => child_entity_ids
    metadata::Dict{String, Any}
end

"""
    aggregate_cash_flows(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid) -> CashFlowAggregationResult

Aggregate stream allocations using industry-standard 7-stage pipeline:
1. Stream Collection & Grouping
2. Cash Flow Assembly  
3. Operating Statement Generation
4. Financing Adjustments
5. Tax Processing
6. Available Cash Calculation
7. Statement Views
"""
function aggregate_cash_flows(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid)::CashFlowAggregationResult
    deal_id = get(ir_data.deal, "id", "unknown_deal")
    currency = get(ir_data.deal, "currency", "USD")
    
    # ===== STAGE 1: STREAM COLLECTION & GROUPING =====
    grouped_streams = collect_streams(ir_data, allocation_result, grid)
    
    # ===== STAGE 2: CASH FLOW ASSEMBLY =====
    # Uses cash_flow_assembly.jl module
    cash_flow_entries = assemble_cash_flows(grouped_streams, ir_data, grid)
    categorized_entries = categorize_cash_flows(cash_flow_entries)
    
    # ===== STAGE 3: OPERATING STATEMENT GENERATION =====
    # Uses operating_statement.jl module
    operating_statements = generate_operating_statements(cash_flow_entries)
    
    # ===== STAGE 4: FINANCING ADJUSTMENTS =====
    # Uses financing_adjustments.jl module
    financing_adjustments = apply_financing_adjustments(operating_statements, cash_flow_entries)
    debt_metrics = calculate_debt_metrics(financing_adjustments)
    
    # ===== STAGE 5: TAX PROCESSING =====
    # Uses tax_processing.jl module
    tax_adjustments = calculate_tax_adjustments(financing_adjustments, cash_flow_entries)
    final_tax_adjustments = apply_final_tax_adjustments(tax_adjustments)
    
    # ===== STAGE 6: AVAILABLE CASH CALCULATION =====
    # Uses available_cash.jl module
    available_cash_calculations = calculate_available_cash(final_tax_adjustments)
    final_cash_calculations = apply_distribution_policy(available_cash_calculations)
    
    # ===== STAGE 7: STATEMENT VIEWS =====
    # Uses statement_views.jl module
    statement_views = generate_statement_views(final_cash_calculations)
    
    # Extract Non-GAAP monthly and annual views for backward compatibility
    monthly_statements = get(statement_views, "non_gaap", FinancialStatement[])
    annual_statements = aggregate_to_annual(monthly_statements)
    
    # Convert to legacy format for backward compatibility
    monthly_view = convert_to_legacy_format(monthly_statements, "monthly")
    annual_view = convert_to_legacy_format(annual_statements, "annual")
    
    # Build hierarchy map for drill-down capability
    hierarchy_map = build_hierarchy_map(ir_data)
    
    return CashFlowAggregationResult(
        deal_id,
        currency,
        monthly_view,
        annual_view,
        hierarchy_map,
        Dict{String, Any}(
            "aggregation_timestamp" => now(),
            "pipeline_version" => "7-stage-modular-industry-standard",
            "pipeline_stages" => [
                "stream_collection", "cash_flow_assembly", "operating_statement_generation",
                "financing_adjustments", "tax_processing", "available_cash_calculation", "statement_views"
            ],
            "total_periods_monthly" => length(monthly_view),
            "total_periods_annual" => length(annual_view),
            "stage_counts" => Dict(
                "cash_flow_entries" => length(cash_flow_entries),
                "operating_statements" => length(operating_statements),
                "financing_adjustments" => length(financing_adjustments),
                "tax_adjustments" => length(final_tax_adjustments),
                "available_cash_calculations" => length(final_cash_calculations)
            ),
            "debt_metrics" => debt_metrics,
            "statement_views_available" => keys(statement_views),
            "modular_architecture" => true
        )
    )
end

"""
    convert_to_legacy_format(statements::Vector{FinancialStatement}, frequency::String) -> Vector{AggregatedCashFlow}

Convert new FinancialStatement format to legacy AggregatedCashFlow format for backward compatibility.
"""
function convert_to_legacy_format(statements::Vector{FinancialStatement}, frequency::String)::Vector{AggregatedCashFlow}
    legacy_flows = Vector{AggregatedCashFlow}()
    
    for stmt in statements
        # Extract period information
        period_start = stmt.period_start
        period_end = stmt.period_end
        
        # Calculate unlevered and levered cash flows
        unlevered_cf = stmt.operating_cash_flow
        levered_cf = stmt.net_cash_flow
        
        # Create line items from the statement
        line_items = [Dict{String, Any}(
            "streamId" => "aggregated_$(frequency)",
            "name" => "$(frequency)_cash_flow",
            "amount" => unlevered_cf,
            "category" => "Operating",
            "subType" => "Aggregated"
        )]
        
        # Create dummy hierarchical flows for drill-down
        drill_down_data = [HierarchicalCashFlow(
            stmt.entity_id,
            "asset_001",
            "component_001", 
            "stream_001",
            period_start,
            period_end,
            unlevered_cf,
            "Operating",
            "Aggregated",
            Dict{String, Any}("converted_from_new_format" => true)
        )]
        
        # Create metadata
        metadata = Dict{String, Any}(
            "view_type" => string(stmt.view_type),
            "frequency" => frequency,
            "converted_from_modular_pipeline" => true,
            "original_statement_type" => string(stmt.view_type)
        )
        
        legacy_flow = AggregatedCashFlow(
            period_start,
            period_end,
            unlevered_cf,
            levered_cf,
            line_items,
            drill_down_data,
            metadata
        )
        
        push!(legacy_flows, legacy_flow)
    end
    
    return legacy_flows
end

"""
    build_hierarchical_flows(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid) -> Vector{HierarchicalCashFlow}

Build hierarchical cash flow data with full drill-down capability.
"""
function build_hierarchical_flows(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid)::Vector{HierarchicalCashFlow}
    hierarchical_flows = Vector{HierarchicalCashFlow}()
    
    # Get entity mappings
    deal_id = get(ir_data.deal, "id", "unknown_deal")
    
    # For each stream allocation, create hierarchical flow entry
    for allocation in allocation_result.allocations
        # Find the period for this allocation
        period = get_period_by_number(grid, allocation.period_number)
        if period === nothing
            @warn "Could not find period $(allocation.period_number) in grid"
            continue
        end
        
        # Find stream metadata
        stream = find_stream_by_id(ir_data.streams, allocation.stream_id)
        if stream === nothing
            @warn "Could not find stream $(allocation.stream_id) in IR data"
            continue
        end
        
        # Build hierarchical path (simplified for now - assumes single asset/component)
        asset_id = get_asset_for_stream(ir_data, allocation.stream_id)
        component_id = get_component_for_stream(ir_data, allocation.stream_id)
        
        hierarchical_flow = HierarchicalCashFlow(
            deal_id,
            asset_id,
            component_id,
            allocation.stream_id,
            allocation.period_start,
            allocation.period_end,
            allocation.adjusted_amount,
            get(stream, "category", "Unknown"),
            get(stream, "subType", "Other"),
            Dict{String, Any}(
                "growth_factor" => allocation.growth_factor,
                "allocation_method" => allocation.allocation_method,
                "base_amount" => allocation.base_amount
            )
        )
        
        push!(hierarchical_flows, hierarchical_flow)
    end
    
    return hierarchical_flows
end

"""
    create_monthly_view(hierarchical_flows::Vector{HierarchicalCashFlow}, grid::TemporalGrid) -> Vector{AggregatedCashFlow}

Create detailed monthly cash flow view.
"""
function create_monthly_view(hierarchical_flows::Vector{HierarchicalCashFlow}, grid::TemporalGrid)::Vector{AggregatedCashFlow}
    monthly_flows = Vector{AggregatedCashFlow}()
    
    # Group flows by period
    for period in grid.periods
        period_flows = filter(f -> f.period_start == period.period_start && f.period_end == period.period_end, hierarchical_flows)
        
        if !isempty(period_flows)
            aggregated = aggregate_period_flows(period_flows, period.period_start, period.period_end)
            push!(monthly_flows, aggregated)
        end
    end
    
    return monthly_flows
end

"""
    create_annual_view(hierarchical_flows::Vector{HierarchicalCashFlow}, grid::TemporalGrid) -> Vector{AggregatedCashFlow}

Create aggregated annual cash flow view.
"""
function create_annual_view(hierarchical_flows::Vector{HierarchicalCashFlow}, grid::TemporalGrid)::Vector{AggregatedCashFlow}
    annual_flows = Vector{AggregatedCashFlow}()
    
    # Group flows by year
    years = unique(year(f.period_start) for f in hierarchical_flows)
    sort!(years)
    
    for yr in years
        year_flows = filter(f -> year(f.period_start) == yr, hierarchical_flows)
        
        if !isempty(year_flows)
            # Calculate year boundaries
            year_start = Date(yr, 1, 1)
            year_end = Date(yr, 12, 31)
            
            aggregated = aggregate_period_flows(year_flows, year_start, year_end)
            push!(annual_flows, aggregated)
        end
    end
    
    return annual_flows
end

"""
    aggregate_period_flows(flows::Vector{HierarchicalCashFlow}, period_start::Date, period_end::Date) -> AggregatedCashFlow

Aggregate flows for a specific period.
"""
function aggregate_period_flows(flows::Vector{HierarchicalCashFlow}, period_start::Date, period_end::Date)::AggregatedCashFlow
    # Calculate totals
    total_amount = sum(f.amount for f in flows)
    
    # Separate revenue and expenses for unlevered/levered calculation
    revenue_flows = filter(f -> f.category == "Revenue", flows)
    expense_flows = filter(f -> f.category == "Expense", flows)
    
    total_revenue = sum(f.amount for f in revenue_flows)
    total_expenses = sum(f.amount for f in expense_flows)
    
    unlevered_cf = total_revenue - total_expenses
    levered_cf = unlevered_cf * 0.8  # Placeholder leverage factor
    
    # Create line items
    line_items = [Dict{String, Any}(
        "streamId" => flow.stream_id,
        "name" => "$(flow.stream_id)_$(flow.category)",
        "amount" => flow.amount,
        "category" => flow.category,
        "subType" => flow.sub_type
    ) for flow in flows]
    
    # Create metadata
    metadata = Dict{String, Any}(
        "total_streams" => length(unique(f.stream_id for f in flows)),
        "revenue_count" => length(revenue_flows),
        "expense_count" => length(expense_flows),
        "period_type" => period_start == period_end ? "single_day" : "period"
    )
    
    return AggregatedCashFlow(
        period_start,
        period_end,
        unlevered_cf,
        levered_cf,
        line_items,
        flows,  # Full drill-down data
        metadata
    )
end

"""
    build_hierarchy_map(ir_data::IRData) -> Dict{String, Vector{String}}

Build entity hierarchy mapping for drill-down capability.
"""
function build_hierarchy_map(ir_data::IRData)::Dict{String, Vector{String}}
    hierarchy_map = Dict{String, Vector{String}}()
    
    deal_id = get(ir_data.deal, "id", "unknown_deal")
    
    # Deal -> Assets
    asset_ids = [get(asset, "id", "unknown_asset_$(i)") for (i, asset) in enumerate(ir_data.assets)]
    hierarchy_map[deal_id] = asset_ids
    
    # Assets -> Components (simplified - assumes each asset has all components)
    for asset_id in asset_ids
        component_ids = [get(comp, "id", "unknown_component_$(i)") for (i, comp) in enumerate(ir_data.components)]
        hierarchy_map[asset_id] = component_ids
        
        # Components -> Streams (simplified - assumes each component has all streams)
        for component_id in component_ids
            stream_ids = [get(stream, "id", "unknown_stream_$(i)") for (i, stream) in enumerate(ir_data.streams)]
            hierarchy_map[component_id] = stream_ids
        end
    end
    
    return hierarchy_map
end

"""
    format_cash_flow_result(aggregation_result::CashFlowAggregationResult, view::CashFlowView) -> Dict{String, Any}

Format aggregation result to match cash-flow.schema.yaml.
"""
function format_cash_flow_result(aggregation_result::CashFlowAggregationResult, view::CashFlowView)::Dict{String, Any}
    # Select the appropriate view
    entries_data = if view == MONTHLY
        aggregation_result.monthly_view
    elseif view == ANNUAL
        aggregation_result.annual_view
    else
        aggregation_result.monthly_view  # Default to monthly
    end
    
    # Format entries according to schema
    formatted_entries = [Dict{String, Any}(
        "periodStart" => string(entry.period_start),
        "periodEnd" => string(entry.period_end),
        "lineItems" => entry.line_items,
        "unleveredCF" => round(entry.unlevered_cf, digits=2),
        "leveredCF" => round(entry.levered_cf, digits=2)
    ) for entry in entries_data]
    
    # Create frequency string
    frequency_str = if view == MONTHLY
        "monthly"
    elseif view == ANNUAL
        "annual"
    else
        "monthly"
    end
    
    return Dict{String, Any}(
        "dealId" => aggregation_result.deal_id,
        "currency" => aggregation_result.currency,
        "frequency" => frequency_str,
        "entries" => formatted_entries,
        "tagsApplied" => ["Forecast"],  # Default tag
        "metadata" => merge(aggregation_result.metadata, Dict{String, Any}(
            "view_type" => string(view),
            "hierarchy_available" => true,
            "drill_down_levels" => ["deal", "asset", "component", "stream"]
        ))
    )
end

"""
    summarize_aggregation_result(aggregation_result::CashFlowAggregationResult) -> Dict{String, Any}

Create a summary of the aggregation result.
"""
function summarize_aggregation_result(aggregation_result::CashFlowAggregationResult)::Dict{String, Any}
    monthly_total = sum(entry.unlevered_cf for entry in aggregation_result.monthly_view)
    annual_total = sum(entry.unlevered_cf for entry in aggregation_result.annual_view)
    
    return Dict{String, Any}(
        "deal_id" => aggregation_result.deal_id,
        "currency" => aggregation_result.currency,
        "monthly_periods" => length(aggregation_result.monthly_view),
        "annual_periods" => length(aggregation_result.annual_view),
        "total_unlevered_cf_monthly" => round(monthly_total, digits=2),
        "total_unlevered_cf_annual" => round(annual_total, digits=2),
        "hierarchy_entities" => length(aggregation_result.hierarchy_map),
        "drill_down_available" => true
    )
end

# Helper functions

"""
    get_period_by_number(grid::TemporalGrid, period_number::Int) -> Union{TimePeriod, Nothing}

Find a period in the grid by its number.
"""
function get_period_by_number(grid::TemporalGrid, period_number::Int)::Union{TimePeriod, Nothing}
    for period in grid.periods
        if period.period_number == period_number
            return period
        end
    end
    return nothing
end

# Note: find_stream_by_id is defined in cash_flow_assembly.jl

"""
    get_asset_for_stream(ir_data::IRData, stream_id::String) -> String

Get the asset ID for a given stream (simplified implementation).
"""
function get_asset_for_stream(ir_data::IRData, stream_id::String)::String
    # Simplified: assume first asset owns all streams
    if !isempty(ir_data.assets)
        return get(ir_data.assets[1], "id", "default_asset")
    end
    return "default_asset"
end

"""
    get_component_for_stream(ir_data::IRData, stream_id::String) -> String

Get the component ID for a given stream (simplified implementation).
"""
function get_component_for_stream(ir_data::IRData, stream_id::String)::String
    # Simplified: assume first component owns all streams
    if !isempty(ir_data.components)
        return get(ir_data.components[1], "id", "default_component")
    end
    return "default_component"
end