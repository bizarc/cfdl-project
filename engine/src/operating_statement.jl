# Stage 3: Operating Statement Generation
# Generates standardized operating cash flow statements with NOI calculation

using Dates

"""
    OperatingStatement

Standard operating cash flow statement structure following real estate industry best practices.

Key Components:
- Revenue Items: Rental income, other operating income, management fees
- Operating Expenses: Property management, maintenance, utilities, insurance, property taxes, G&A
- Net Operating Income (NOI): Total revenue minus operating expenses
"""
struct OperatingStatement
    entity_id::String
    period_start::Date
    period_end::Date
    revenue_items::Dict{String, Float64}
    operating_expenses::Dict{String, Float64}
    net_operating_income::Float64
    metadata::Dict{String, Any}
end

"""
    generate_operating_statements(cash_flow_entries::Vector{CashFlowEntry}) -> Vector{OperatingStatement}

Generate standardized operating cash flow statements with NOI calculation.

This is Stage 3 of the 7-stage cash flow pipeline:
1. Stream Collection ✓
2. Cash Flow Assembly ✓
3. **Operating Statement Generation** ← This stage
4. Financing Adjustments  
5. Tax Processing
6. Available Cash Calculation
7. Statement Views

The operating statement follows real estate industry standards:
```
REVENUE ITEMS:
- Rental Income
- Other Operating Income  
- Management Fees (if applicable)

OPERATING EXPENSES:
- Property Management
- Maintenance & Repairs
- Utilities
- Insurance
- Property Taxes
- General & Administrative

= NET OPERATING INCOME (NOI)
```
"""
function generate_operating_statements(cash_flow_entries::Vector{CashFlowEntry})::Vector{OperatingStatement}
    operating_statements = Vector{OperatingStatement}()
    
    for entry in cash_flow_entries
        if isempty(entry.operating_items)
            continue
        end
        
        # Separate revenue and expenses using industry-standard logic
        revenue_items = Dict{String, Float64}()
        operating_expenses = Dict{String, Float64}()
        
        for (item_name, amount) in entry.operating_items
            if is_revenue_item(item_name, amount)
                revenue_items[item_name] = amount
            else
                # Convert negative amounts to positive for expense tracking
                operating_expenses[item_name] = abs(amount)
            end
        end
        
        # Calculate Net Operating Income (NOI) - key real estate metric
        total_revenue = sum(values(revenue_items))
        total_expenses = sum(values(operating_expenses))
        net_operating_income = total_revenue - total_expenses
        
        # Create comprehensive metadata for analysis
        metadata = Dict{String, Any}(
            "total_revenue" => total_revenue,
            "total_expenses" => total_expenses,
            "revenue_streams" => length(revenue_items),
            "expense_streams" => length(operating_expenses),
            "noi_margin" => total_revenue > 0 ? net_operating_income / total_revenue : 0.0,
            "stage" => "operating_statement_generation",
            "period_days" => Dates.value(entry.period_end - entry.period_start) + 1
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

NOI is a fundamental real estate metric representing the property's ability to generate 
cash flow from operations before financing and tax considerations.
"""
function calculate_noi(operating_items::Dict{String, Float64})::Float64
    total_revenue = 0.0
    total_expenses = 0.0
    
    for (item_name, amount) in operating_items
        if is_revenue_item(item_name, amount)
            total_revenue += amount
        else
            total_expenses += abs(amount)  # Ensure expenses are positive
        end
    end
    
    return total_revenue - total_expenses
end

"""
    calculate_noi_metrics(statements::Vector{OperatingStatement}) -> Dict{String, Any}

Calculate key NOI-based metrics for performance analysis.

Returns metrics including:
- Total NOI across all periods
- Average NOI per period
- NOI growth rates
- Expense ratios
- Revenue concentration
"""
function calculate_noi_metrics(statements::Vector{OperatingStatement})::Dict{String, Any}
    if isempty(statements)
        return Dict{String, Any}("message" => "No operating statements to analyze")
    end
    
    # Sort by period for growth calculations
    sorted_statements = sort(statements, by = s -> s.period_start)
    
    total_noi = sum(s.net_operating_income for s in statements)
    total_revenue = sum(sum(values(s.revenue_items)) for s in statements)
    total_expenses = sum(sum(values(s.operating_expenses)) for s in statements)
    
    # Calculate period-over-period growth
    noi_growth_rates = Float64[]
    if length(sorted_statements) > 1
        for i in 2:length(sorted_statements)
            prev_noi = sorted_statements[i-1].net_operating_income
            curr_noi = sorted_statements[i].net_operating_income
            if prev_noi != 0
                growth_rate = (curr_noi - prev_noi) / abs(prev_noi)
                push!(noi_growth_rates, growth_rate)
            end
        end
    end
    
    return Dict{String, Any}(
        "total_noi" => total_noi,
        "average_noi" => total_noi / length(statements),
        "total_revenue" => total_revenue,
        "total_expenses" => total_expenses,
        "expense_ratio" => total_revenue > 0 ? total_expenses / total_revenue : 0.0,
        "noi_margin" => total_revenue > 0 ? total_noi / total_revenue : 0.0,
        "periods_analyzed" => length(statements),
        "noi_growth_rates" => noi_growth_rates,
        "average_growth_rate" => isempty(noi_growth_rates) ? 0.0 : sum(noi_growth_rates) / length(noi_growth_rates),
        "period_range" => (minimum(s.period_start for s in statements), maximum(s.period_end for s in statements))
    )
end

"""
    summarize_operating_statements(statements::Vector{OperatingStatement}) -> Dict{String, Any}

Create a comprehensive summary of operating statements for reporting and analysis.
"""
function summarize_operating_statements(statements::Vector{OperatingStatement})::Dict{String, Any}
    if isempty(statements)
        return Dict{String, Any}("message" => "No operating statements to summarize")
    end
    
    # Basic aggregations
    total_noi = sum(s.net_operating_income for s in statements)
    total_revenue = sum(sum(values(s.revenue_items)) for s in statements)
    total_expenses = sum(sum(values(s.operating_expenses)) for s in statements)
    
    # Revenue analysis
    all_revenue_items = Dict{String, Float64}()
    for stmt in statements
        for (item, amount) in stmt.revenue_items
            all_revenue_items[item] = get(all_revenue_items, item, 0.0) + amount
        end
    end
    
    # Expense analysis
    all_expense_items = Dict{String, Float64}()
    for stmt in statements
        for (item, amount) in stmt.operating_expenses
            all_expense_items[item] = get(all_expense_items, item, 0.0) + amount
        end
    end
    
    return Dict{String, Any}(
        "summary_metrics" => Dict(
            "total_statements" => length(statements),
            "total_noi" => total_noi,
            "total_revenue" => total_revenue,
            "total_expenses" => total_expenses,
            "average_noi" => total_noi / length(statements),
            "noi_margin" => total_revenue > 0 ? total_noi / total_revenue : 0.0,
            "expense_ratio" => total_revenue > 0 ? total_expenses / total_revenue : 0.0
        ),
        "revenue_breakdown" => all_revenue_items,
        "expense_breakdown" => all_expense_items,
        "period_coverage" => Dict(
            "start_date" => minimum(s.period_start for s in statements),
            "end_date" => maximum(s.period_end for s in statements),
            "entities_covered" => length(unique(s.entity_id for s in statements))
        ),
        "stage_completed" => "operating_statement_generation"
    )
end

# Helper functions

"""
    is_revenue_item(item_name::String, amount::Float64) -> Bool

Determine if a cash flow item represents revenue based on name and amount.

Uses industry-standard naming conventions and positive amount logic.
"""
function is_revenue_item(item_name::String, amount::Float64)::Bool
    item_lower = lowercase(item_name)
    
    # Positive amounts with revenue-indicating names
    if amount > 0 && any(keyword -> contains(item_lower, keyword), 
                        ["rent", "revenue", "income", "fee", "management", "parking", "other"])
        return true
    end
    
    # Explicit revenue categories (even if negative, which might indicate adjustments)
    if any(keyword -> contains(item_lower, keyword), 
           ["rental_income", "base_rent", "percentage_rent", "cam", "ancillary"])
        return true
    end
    
    return false
end

"""
    validate_operating_statement(statement::OperatingStatement) -> Dict{String, Any}

Validate an operating statement for data quality and business logic.

Returns validation results with any warnings or errors found.
"""
function validate_operating_statement(statement::OperatingStatement)::Dict{String, Any}
    warnings = String[]
    errors = String[]
    
    # Check for negative revenue (unusual but possible with adjustments)
    for (item, amount) in statement.revenue_items
        if amount < 0
            push!(warnings, "Negative revenue item: $item = $amount")
        end
    end
    
    # Check for negative expenses (should be positive in our model)
    for (item, amount) in statement.operating_expenses
        if amount < 0
            push!(warnings, "Negative expense item: $item = $amount")
        end
    end
    
    # Validate NOI calculation
    calculated_noi = sum(values(statement.revenue_items)) - sum(values(statement.operating_expenses))
    if abs(calculated_noi - statement.net_operating_income) > 0.01
        push!(errors, "NOI calculation mismatch: calculated=$calculated_noi, stored=$(statement.net_operating_income)")
    end
    
    # Check for empty statements
    if isempty(statement.revenue_items) && isempty(statement.operating_expenses)
        push!(warnings, "Operating statement has no revenue or expense items")
    end
    
    return Dict{String, Any}(
        "is_valid" => isempty(errors),
        "warnings" => warnings,
        "errors" => errors,
        "validation_timestamp" => now()
    )
end