# Stage 7: Waterfall Distribution
# Implements sequential tier processing with condition evaluation for capital distribution

using Dates
using JSON3

"""
    RecipientDistribution

Represents cash distribution to a specific recipient.
"""
struct RecipientDistribution
    recipient_id::String
    recipient_type::String  # "party_uri", "named_group", "participant_index"
    amount::Float64
    percentage_of_tier::Float64
    metadata::Dict{String, Any}
end

"""
    TierDistribution

Represents the distribution result for a single waterfall tier.
"""
struct TierDistribution
    tier_id::String
    tier_description::String
    condition_type::String  # "condition", "until", "prefRate"
    condition_value::Any
    condition_met::Bool
    cash_available_to_tier::Float64
    cash_allocated::Float64
    recipient_distributions::Vector{RecipientDistribution}
    metadata::Dict{String, Any}
end

"""
    WaterfallDistribution

Complete result of waterfall distribution for a specific period.
"""
struct WaterfallDistribution
    entity_id::String
    waterfall_id::String
    period_start::Date
    period_end::Date
    total_available::Float64
    tier_distributions::Vector{TierDistribution}
    total_distributed::Float64
    remaining_cash::Float64
    capital_stack_data::Dict{String, Any}
    metadata::Dict{String, Any}
end

"""
    evaluate_tier_condition(tier::Dict{String, Any}, context::Dict{String, Any}) -> Bool

Evaluate whether a waterfall tier's condition is met.

Supports three condition types:
- `condition`: Boolean or arithmetic expression (e.g., "remainingCapital > 0")
- `until`: Shorthand boolean expression (equivalent to condition)
- `prefRate`: Preferred return rate threshold (e.g., 0.08 for 8% IRR)
"""
function evaluate_tier_condition(tier::Dict, context::Dict)::Bool
    # Check for condition field
    if haskey(tier, "condition")
        condition_expr = tier["condition"]
        return evaluate_expression(condition_expr, context)
    end
    
    # Check for until field (equivalent to condition)
    if haskey(tier, "until")
        until_expr = tier["until"]
        return evaluate_expression(until_expr, context)
    end
    
    # Check for prefRate field
    if haskey(tier, "prefRate")
        pref_rate = tier["prefRate"]
        current_irr = get(context, "current_irr", 0.0)
        return current_irr >= pref_rate
    end
    
    # If no condition specified, tier is always active
    return true
end

"""
    evaluate_expression(expr::String, context::Dict{String, Any}) -> Bool

Evaluate a boolean or arithmetic expression against the current context.

Supports basic arithmetic and comparison operations:
- Variables: remainingCapital, totalDistributed, currentIRR, etc.
- Operators: +, -, *, /, >, <, >=, <=, ==, !=
- Functions: max, min, abs

This is a simplified implementation. In production, consider using a proper
expression parser or domain-specific language.
"""
function evaluate_expression(expr::String, context::Dict)::Bool
    # Simplified expression evaluation
    # Replace context variables
    evaluated_expr = expr
    
    for (key, value) in context
        # Convert camelCase to snake_case for variable matching
        snake_case_key = replace(key, r"([a-z])([A-Z])" => s"\1_\2") |> lowercase
        
        # Replace both original and snake_case versions
        evaluated_expr = replace(evaluated_expr, key => string(value))
        evaluated_expr = replace(evaluated_expr, snake_case_key => string(value))
    end
    
    # For now, use a simple evaluation approach
    # In production, this should use a proper expression parser
    try
        # Handle simple comparison cases
        if occursin(">=", evaluated_expr)
            parts = split(evaluated_expr, ">=")
            if length(parts) == 2
                left = parse(Float64, strip(parts[1]))
                right = parse(Float64, strip(parts[2]))
                return left >= right
            end
        elseif occursin("<=", evaluated_expr)
            parts = split(evaluated_expr, "<=")
            if length(parts) == 2
                left = parse(Float64, strip(parts[1]))
                right = parse(Float64, strip(parts[2]))
                return left <= right
            end
        elseif occursin(">", evaluated_expr)
            parts = split(evaluated_expr, ">")
            if length(parts) == 2
                left = parse(Float64, strip(parts[1]))
                right = parse(Float64, strip(parts[2]))
                return left > right
            end
        elseif occursin("<", evaluated_expr)
            parts = split(evaluated_expr, "<")
            if length(parts) == 2
                left = parse(Float64, strip(parts[1]))
                right = parse(Float64, strip(parts[2]))
                return left < right
            end
        elseif occursin("==", evaluated_expr)
            parts = split(evaluated_expr, "==")
            if length(parts) == 2
                left = parse(Float64, strip(parts[1]))
                right = parse(Float64, strip(parts[2]))
                return left == right
            end
        end
        
        # If no comparison operator, try to parse as boolean
        if evaluated_expr == "true"
            return true
        elseif evaluated_expr == "false"
            return false
        else
            # Try to parse as number and check if > 0
            value = parse(Float64, evaluated_expr)
            return value > 0.0
        end
    catch e
        @warn "Failed to evaluate expression: $expr" error=e
        return false
    end
end

"""
    calculate_tier_amount(tier::Dict, context::Dict{String, Any}) -> Float64

Calculate the amount this tier should receive based on its condition.
This properly evaluates conditions from the IR schema like "totalDistributed < 27000000".
"""
function calculate_tier_amount(tier::Dict, context::Dict{String, Any})::Float64
    current_total_distributed = get(context, "totalDistributed", 0.0)
    
    # Handle "totalDistributed < X" conditions (most common case)
    if haskey(tier, "condition")
        condition_str = string(tier["condition"])
        
        if occursin("totalDistributed", condition_str) && occursin("<", condition_str)
            parts = split(condition_str, "<")
            if length(parts) == 2 && strip(parts[1]) == "totalDistributed"
                try
                    limit = parse(Float64, strip(parts[2]))
                    return max(0.0, limit - current_total_distributed)
                catch e
                    @warn "Failed to parse condition limit: $condition_str" error=e
                    return 0.0
                end
            end
        end
        
        # Handle "remainingCapital > 0" - tier can take all remaining cash
        if occursin("remainingCapital", condition_str) && occursin(">", condition_str)
            return Float64(1e12)  # Large number representing "unlimited"
        end
    end
    
    # Handle prefRate conditions
    if haskey(tier, "prefRate")
        # For preferred rate tiers, tier can take unlimited amount
        # (subject to available cash constraints)
        return Float64(1e12)
    end
    
    # Default: tier can take unlimited amount
    return Float64(1e12)
end

"""
    resolve_recipients(distribute_config::Vector{Dict{String, Any}}, capital_stack::Dict{String, Any}) -> Vector{Dict{String, Any}}

Resolve distribution recipients from either explicit splits or capital stack inheritance.
"""
function resolve_recipients(distribute_config::Vector, capital_stack::Dict)::Vector{Dict{String, Any}}
    recipients = Vector{Dict{String, Any}}()
    
    for dist in distribute_config
        if haskey(dist, "fromCapitalStack") && dist["fromCapitalStack"] == true
            # Inherit from capital stack
            layer_name = get(dist, "layerName", "")
            participants = get(capital_stack, "participants", [])
            
            # Calculate total capital for pro-rata distribution
            total_capital = sum(p -> get(p, "amount", 0.0), participants)
            
            for participant in participants
                if total_capital > 0
                    pro_rata_percentage = get(participant, "amount", 0.0) / total_capital
                    push!(recipients, Dict(
                        "recipient" => get(participant, "partyId", "unknown"),
                        "percentage" => pro_rata_percentage,
                        "source" => "capital_stack",
                        "layer" => layer_name
                    ))
                end
            end
        else
            # Explicit recipient specification
            push!(recipients, Dict(
                "recipient" => get(dist, "recipient", "unknown"),
                "percentage" => get(dist, "percentage", 0.0),
                "source" => "explicit"
            ))
        end
    end
    
    return recipients
end

"""
    distribute_to_recipients(available_cash::Float64, recipients::Vector{Dict{String, Any}}) -> Vector{RecipientDistribution}

Distribute available cash to recipients according to their percentages.
"""
function distribute_to_recipients(available_cash::Float64, recipients::Vector)::Vector{RecipientDistribution}
    distributions = Vector{RecipientDistribution}()
    
    # Validate percentages sum to approximately 1.0
    total_percentage = sum(r -> get(r, "percentage", 0.0), recipients)
    if abs(total_percentage - 1.0) > 1e-6
        @warn "Recipient percentages sum to $total_percentage, not 1.0. Normalizing."
        # Normalize percentages
        for recipient in recipients
            recipient["percentage"] = get(recipient, "percentage", 0.0) / total_percentage
        end
    end
    
    for recipient in recipients
        recipient_id = string(get(recipient, "recipient", "unknown"))
        percentage = get(recipient, "percentage", 0.0)
        amount = available_cash * percentage
        
        # Determine recipient type
        recipient_type = if isa(get(recipient, "recipient", ""), Int)
            "participant_index"
        elseif startswith(recipient_id, "http")
            "party_uri"
        else
            "named_group"
        end
        
        push!(distributions, RecipientDistribution(
            recipient_id,
            recipient_type,
            amount,
            percentage,
            Dict(
                "source" => get(recipient, "source", "unknown"),
                "layer" => get(recipient, "layer", "")
            )
        ))
    end
    
    return distributions
end

"""
    execute_waterfall_distribution(available_cash_results::Vector{AvailableCashCalculation}, ir_data::IRData) -> Vector{WaterfallDistribution}

Execute waterfall distribution for available cash across all periods.

This is the main entry point for waterfall distribution processing.
Processes each period's available cash through the defined waterfall tiers.
"""
function execute_waterfall_distribution(
    available_cash_results::Vector{AvailableCashCalculation}, 
    ir_data::IRData
)::Vector{WaterfallDistribution}
    
    if ir_data.waterfall === nothing
        @debug "No waterfall configuration found, skipping distribution"
        return Vector{WaterfallDistribution}()
    end
    
    waterfall_config = ir_data.waterfall
    waterfall_id = get(waterfall_config, "id", "default_waterfall")
    tiers = get(waterfall_config, "tiers", [])
    
    # Extract capital stack information
    capital_stack = extract_capital_stack(ir_data)
    
    distributions = Vector{WaterfallDistribution}()
    
    # Process each period's available cash
    for cash_calc in available_cash_results
        distribution = process_period_distribution(
            cash_calc,
            waterfall_id,
            tiers,
            capital_stack,
            ir_data
        )
        push!(distributions, distribution)
    end
    
    return distributions
end

"""
    process_period_distribution(cash_calc::AvailableCashCalculation, waterfall_id::String, tiers::Vector, capital_stack::Dict{String, Any}, ir_data::IRData) -> WaterfallDistribution

Process waterfall distribution for a single period.
"""
function process_period_distribution(
    cash_calc::AvailableCashCalculation,
    waterfall_id::String,
    tiers::Vector,
    capital_stack::Dict{String, Any},
    ir_data::IRData
)::WaterfallDistribution
    
    available_cash = cash_calc.available_for_distribution
    remaining_cash = available_cash
    tier_distributions = Vector{TierDistribution}()
    total_distributed = 0.0
    
    # Build context for condition evaluation
    context = Dict{String, Any}(
        "remainingCapital" => remaining_cash,
        "totalDistributed" => total_distributed,
        "availableCash" => available_cash,
        "periodStart" => cash_calc.period_start,
        "periodEnd" => cash_calc.period_end
    )
    
    # Process tiers sequentially
    for (tier_idx, tier) in enumerate(tiers)
        if remaining_cash <= 0
            break  # No more cash to distribute
        end
        
        tier_id = get(tier, "id", "tier_$tier_idx")
        tier_description = get(tier, "description", "")
        
        # Determine condition type and value
        condition_type, condition_value = if haskey(tier, "condition")
            ("condition", tier["condition"])
        elseif haskey(tier, "until")
            ("until", tier["until"])
        elseif haskey(tier, "prefRate")
            ("prefRate", tier["prefRate"])
        else
            ("always", true)
        end
        
        # Evaluate tier condition
        condition_met = evaluate_tier_condition(tier, context)
        
        tier_cash_allocated = 0.0
        recipient_distributions = Vector{RecipientDistribution}()
        
        if condition_met && remaining_cash > 0
            # Calculate the amount this tier should receive based on its definition
            tier_amount = calculate_tier_amount(tier, context)
            
            # Apply your logic: compare available_cash to tier_amount
            cash_to_allocate = if remaining_cash <= tier_amount
                # Tier gets all remaining cash
                remaining_cash
            else
                # Tier gets its calculated amount
                tier_amount
            end
            
            if cash_to_allocate > 0
                # Resolve recipients for this tier
                distribute_config = get(tier, "distribute", [])
                recipients = resolve_recipients(distribute_config, capital_stack)
                
                # Distribute the calculated amount to recipients
                if !isempty(recipients)
                    recipient_distributions = distribute_to_recipients(cash_to_allocate, recipients)
                    tier_cash_allocated = sum(rd -> rd.amount, recipient_distributions)
                    
                    # Update remaining cash and totals
                    remaining_cash -= tier_cash_allocated
                    total_distributed += tier_cash_allocated
                    
                    # Update context for next tier
                    context["remainingCapital"] = remaining_cash
                    context["totalDistributed"] = total_distributed
                end
            end
        end
        
        # Record tier distribution result
        push!(tier_distributions, TierDistribution(
            tier_id,
            tier_description,
            string(condition_type),
            condition_value,
            condition_met,
            remaining_cash + tier_cash_allocated,  # Cash available when tier was processed
            tier_cash_allocated,
            recipient_distributions,
            Dict{String, Any}(
                "tier_index" => tier_idx,
                "processing_timestamp" => now()
            )
        ))
    end
    
    return WaterfallDistribution(
        cash_calc.entity_id,
        waterfall_id,
        cash_calc.period_start,
        cash_calc.period_end,
        available_cash,
        tier_distributions,
        total_distributed,
        remaining_cash,
        capital_stack,
        Dict{String, Any}(
            "processing_timestamp" => now(),
            "total_tiers_processed" => length(tier_distributions),
            "distribution_efficiency" => total_distributed / max(available_cash, 1.0)
        )
    )
end

"""
    extract_capital_stack(ir_data::IRData) -> Dict{String, Any}

Extract capital stack information from IR data.
"""
function extract_capital_stack(ir_data::IRData)::Dict{String, Any}
    # Look for capital stack in deal data
    deal_capital_stack = get(ir_data.deal, "capitalStack", nothing)
    if deal_capital_stack !== nothing
        return deal_capital_stack
    end
    
    # Look for capital stack in assets
    for asset in ir_data.assets
        asset_capital_stack = get(asset, "capitalStack", nothing)
        if asset_capital_stack !== nothing
            return asset_capital_stack
        end
    end
    
    # Return empty capital stack if none found
    return Dict{String, Any}(
        "participants" => [],
        "id" => "default_capital_stack"
    )
end

"""
    summarize_waterfall_distribution(distributions::Vector{WaterfallDistribution}) -> Dict{String, Any}

Generate summary statistics for waterfall distributions.
"""
function summarize_waterfall_distribution(distributions::Vector)::Dict{String, Any}
    if isempty(distributions)
        return Dict{String, Any}(
            "total_periods" => 0,
            "total_available" => 0.0,
            "total_distributed" => 0.0,
            "distribution_efficiency" => 0.0
        )
    end
    
    total_available = sum(d -> d.total_available, distributions)
    total_distributed = sum(d -> d.total_distributed, distributions)
    total_remaining = sum(d -> d.remaining_cash, distributions)
    
    # Calculate recipient summary
    recipient_totals = Dict{String, Float64}()
    for dist in distributions
        for tier in dist.tier_distributions
            for recipient in tier.recipient_distributions
                recipient_id = recipient.recipient_id
                recipient_totals[recipient_id] = get(recipient_totals, recipient_id, 0.0) + recipient.amount
            end
        end
    end
    
    return Dict{String, Any}(
        "total_periods" => length(distributions),
        "total_available" => total_available,
        "total_distributed" => total_distributed,
        "total_remaining" => total_remaining,
        "distribution_efficiency" => total_distributed / max(total_available, 1.0),
        "recipient_totals" => recipient_totals,
        "average_per_period" => total_distributed / length(distributions),
        "periods_with_distributions" => count(d -> d.total_distributed > 0, distributions)
    )
end

"""
    validate_waterfall_distribution(distribution::WaterfallDistribution) -> Vector{String}

Validate a waterfall distribution result for consistency and correctness.
"""
function validate_waterfall_distribution(distribution::WaterfallDistribution)::Vector{String}
    errors = Vector{String}()
    
    # Check cash conservation
    calculated_total = sum(tier -> tier.cash_allocated, distribution.tier_distributions)
    if abs(calculated_total - distribution.total_distributed) > 1e-6
        push!(errors, "Total distributed ($calculated_total) does not match sum of tier allocations ($(distribution.total_distributed))")
    end
    
    # Check remaining cash
    expected_remaining = distribution.total_available - distribution.total_distributed
    if abs(expected_remaining - distribution.remaining_cash) > 1e-6
        push!(errors, "Remaining cash ($(distribution.remaining_cash)) does not match expected ($(expected_remaining))")
    end
    
    # Validate each tier
    for (idx, tier) in enumerate(distribution.tier_distributions)
        tier_total = sum(rd -> rd.amount, tier.recipient_distributions)
        if abs(tier_total - tier.cash_allocated) > 1e-6
            push!(errors, "Tier $(tier.tier_id) allocated cash ($tier.cash_allocated) does not match recipient sum ($(tier_total))")
        end
        
        # Check recipient percentages
        total_percentage = sum(rd -> rd.percentage_of_tier, tier.recipient_distributions)
        if !isempty(tier.recipient_distributions) && abs(total_percentage - 1.0) > 1e-6
            push!(errors, "Tier $(tier.tier_id) recipient percentages sum to $(total_percentage), not 1.0")
        end
    end
    
    return errors
end