# Stream allocation and cash flow generation

using Dates
using Distributions
using Random

"""
    StreamAllocation

Represents a cash flow allocation for a specific stream in a specific period.
"""
struct StreamAllocation
    stream_id::String
    period_number::Int
    period_start::Date
    period_end::Date
    base_amount::Float64
    growth_factor::Float64
    adjusted_amount::Float64
    allocation_method::String
    metadata::Dict{String, Any}
end

"""
    AllocationResult

Contains the results of stream allocation for a single trial.
"""
struct AllocationResult
    allocations::Vector{StreamAllocation}
    total_by_stream::Dict{String, Float64}
    total_by_period::Dict{Int, Float64}
    total_by_category::Dict{String, Float64}
    logic_block_executions::Vector{Dict{String, Any}}
    metadata::Dict{String, Any}
end

"""
    allocate_streams(ir_data::IRData, grid::TemporalGrid, sampled_variables::Dict{String, Any}, rng::AbstractRNG) -> AllocationResult

Allocate all streams from the IR to the temporal grid periods.
"""
function allocate_streams(ir_data::IRData, grid::TemporalGrid, sampled_variables::Dict{String, Any}, rng::AbstractRNG)::AllocationResult
    allocations = Vector{StreamAllocation}()
    logic_block_executions = Vector{Dict{String, Any}}()
    
    # Process each stream
    for stream in ir_data.streams
        try
            stream_allocations = allocate_single_stream(stream, grid, sampled_variables, rng)
            append!(allocations, stream_allocations)
        catch e
            @warn "Failed to allocate stream $(get(stream, "id", "unknown")): $e"
        end
    end
    
    # Execute logic blocks for overrides (if any exist)
    if !isempty(ir_data.logic_blocks)
        execute_logic_blocks!(allocations, ir_data.logic_blocks, sampled_variables, logic_block_executions, rng)
    end
    
    # Calculate aggregations
    total_by_stream = calculate_stream_totals(allocations)
    total_by_period = calculate_period_totals(allocations)
    total_by_category = calculate_category_totals(allocations, ir_data.streams)
    
    return AllocationResult(
        allocations,
        total_by_stream,
        total_by_period,
        total_by_category,
        logic_block_executions,
        Dict{String, Any}(
            "total_streams" => length(ir_data.streams),
            "total_periods" => grid.total_periods,
            "total_allocations" => length(allocations)
        )
    )
end

"""
    allocate_single_stream(stream::Dict{String, Any}, grid::TemporalGrid, sampled_variables::Dict{String, Any}, rng::AbstractRNG) -> Vector{StreamAllocation}

Allocate a single stream across all relevant periods in the temporal grid.
"""
function allocate_single_stream(stream::Dict{String, Any}, grid::TemporalGrid, sampled_variables::Dict{String, Any}, rng::AbstractRNG)::Vector{StreamAllocation}
    allocations = Vector{StreamAllocation}()
    
    stream_id = get(stream, "id", "unknown_stream")
    base_amount = get_stream_base_amount(stream, sampled_variables)
    
    # Determine which periods this stream applies to
    applicable_periods = get_applicable_periods(stream, grid)
    
    for (period_idx, period) in enumerate(applicable_periods)
        # Calculate growth factor for this period (ensure period_idx is positive)
        growth_factor = calculate_growth_factor(stream, max(1, period_idx), sampled_variables, rng)
        
        # Apply growth to base amount
        adjusted_amount = base_amount * growth_factor
        
        # Create allocation
        allocation = StreamAllocation(
            stream_id,
            period.period_number,
            period.period_start,
            period.period_end,
            base_amount,
            growth_factor,
            adjusted_amount,
            "standard", # TODO: Support different allocation methods
            Dict{String, Any}(
                "category" => get(stream, "category", "Unknown"),
                "subType" => get(stream, "subType", "Other"),
                "year_fraction" => period.year_fraction
            )
        )
        
        push!(allocations, allocation)
    end
    
    return allocations
end

"""
    get_stream_base_amount(stream::Dict{String, Any}, sampled_variables::Dict{String, Any}) -> Float64

Extract the base amount for a stream, handling both fixed amounts and calculator references.
"""
function get_stream_base_amount(stream::Dict{String, Any}, sampled_variables::Dict{String, Any})::Float64
    amount = get(stream, "amount", 0.0)
    
    if isa(amount, Number)
        return Float64(amount)
    elseif isa(amount, Dict)
        # Handle calculator definitions (placeholder implementation)
        calculator_type = get(amount, "type", "unknown")
        @warn "Calculator type '$calculator_type' not yet implemented, using default amount"
        return 100000.0  # Placeholder
    else
        @warn "Unknown amount type for stream $(get(stream, "id", "unknown")), using default"
        return 100000.0  # Placeholder
    end
end

"""
    get_applicable_periods(stream::Dict{String, Any}, grid::TemporalGrid) -> Vector{TimePeriod}

Determine which periods in the temporal grid apply to this stream based on its schedule.
"""
function get_applicable_periods(stream::Dict{String, Any}, grid::TemporalGrid)::Vector{TimePeriod}
    # For now, assume all streams apply to all periods
    # TODO: Implement schedule-based filtering
    schedule = get(stream, "schedule", Dict())
    
    if isempty(schedule)
        # No schedule specified, apply to all periods
        return grid.periods
    else
        # TODO: Parse schedule and filter periods accordingly
        @debug "Schedule parsing not yet implemented, using all periods"
        return grid.periods
    end
end

"""
    calculate_growth_factor(stream::Dict{String, Any}, period_index::Int, sampled_variables::Dict{String, Any}, rng::AbstractRNG) -> Float64

Calculate the growth factor for a stream in a specific period.
"""
function calculate_growth_factor(stream::Dict{String, Any}, period_index::Int, sampled_variables::Dict{String, Any}, rng::AbstractRNG)::Float64
    growth = get(stream, "growth", Dict())
    
    if isempty(growth)
        return 1.0  # No growth
    end
    
    growth_type = get(growth, "type", "fixed")
    
    if growth_type == "fixed"
        rate = get(growth, "rate", 0.0)
        exponent = max(0, period_index - 1)  # Ensure non-negative exponent
        return (1.0 + rate) ^ exponent  # Compound growth
        
    elseif growth_type == "expression"
        # TODO: Implement expression evaluation
        expr = get(growth, "expr", "0")
        @warn "Expression growth not yet implemented: $expr"
        return 1.0
        
    elseif growth_type == "distribution"
        # For distribution growth, use the mean value for deterministic behavior
        dist_params = get(growth, "distribution", Dict())
        mean_rate = get(dist_params, "mean", 0.03)  # Use mean of distribution
        exponent = max(0, period_index - 1)  # Ensure non-negative exponent
        return (1.0 + mean_rate) ^ exponent  # Compound growth using mean
        
    elseif growth_type == "randomWalk"
        # TODO: Implement random walk growth
        @warn "Random walk growth not yet implemented"
        return 1.0
        
    else
        @warn "Unknown growth type: $growth_type"
        return 1.0
    end
end

"""
    sample_growth_distribution(params::Dict{String, Any}, rng::AbstractRNG) -> Float64

Sample a growth rate from a distribution.
"""
function sample_growth_distribution(params::Dict{String, Any}, rng::AbstractRNG)::Float64
    dist_type = get(params, "type", "Normal")
    
    if dist_type == "Normal"
        mean = get(params, "mean", 0.03)
        std = get(params, "std", 0.01)
        rate = rand(rng, Distributions.Normal(mean, std))
        return 1.0 + rate
        
    elseif dist_type == "Uniform"
        min_val = get(params, "min", 0.0)
        max_val = get(params, "max", 0.05)
        rate = rand(rng, Distributions.Uniform(min_val, max_val))
        return 1.0 + rate
        
    else
        @warn "Unknown distribution type for growth: $dist_type"
        return 1.0
    end
end

"""
    execute_logic_blocks!(allocations::Vector{StreamAllocation}, logic_blocks::Vector{Dict{String, Any}}, 
                         sampled_variables::Dict{String, Any}, executions::Vector{Dict{String, Any}}, rng::AbstractRNG)

Execute logic blocks to override or modify stream allocations.
"""
function execute_logic_blocks!(allocations::Vector{StreamAllocation}, logic_blocks::Vector{Dict{String, Any}}, 
                               sampled_variables::Dict{String, Any}, executions::Vector{Dict{String, Any}}, rng::AbstractRNG)
    
    # Sort logic blocks by execution order
    sorted_blocks = sort(logic_blocks, by = block -> get(block, "executionOrder", 999))
    
    for block in sorted_blocks
        try
            execute_single_logic_block!(allocations, block, sampled_variables, executions, rng)
        catch e
            execution_record = Dict{String, Any}(
                "block_id" => get(block, "id", "unknown"),
                "status" => "error",
                "error" => string(e),
                "timestamp" => now()
            )
            push!(executions, execution_record)
            @warn "Logic block execution failed: $(get(block, "id", "unknown")) - $e"
        end
    end
end

"""
    execute_single_logic_block!(allocations::Vector{StreamAllocation}, block::Dict{String, Any}, 
                                sampled_variables::Dict{String, Any}, executions::Vector{Dict{String, Any}}, rng::AbstractRNG)

Execute a single logic block.
"""
function execute_single_logic_block!(allocations::Vector{StreamAllocation}, block::Dict{String, Any}, 
                                    sampled_variables::Dict{String, Any}, executions::Vector{Dict{String, Any}}, rng::AbstractRNG)
    
    block_id = get(block, "id", "unknown")
    block_type = get(block, "type", "calculation")
    language = get(block, "language", "julia")
    code = get(block, "code", "")
    
    if language != "julia"
        @warn "Only Julia logic blocks are currently supported, skipping $block_id"
        return
    end
    
    if isempty(code)
        @warn "Empty code in logic block $block_id"
        return
    end
    
    # Create execution context
    context = create_logic_block_context(allocations, sampled_variables, rng)
    
    # Execute code (placeholder - would need proper sandboxing in production)
    execution_start = now()
    
    try
        # This is a simplified execution - in production would need proper sandboxing
        # For now, we'll just log that we would execute the code
        @debug "Would execute logic block $block_id: $code"
        
        # Placeholder: Apply a simple modification for demonstration
        if block_type == "calculation" && contains(code, "rent_growth")
            apply_rent_growth_override!(allocations, context)
        end
        
        execution_record = Dict{String, Any}(
            "block_id" => block_id,
            "status" => "success", 
            "execution_time" => Dates.value(now() - execution_start) / 1000.0,
            "timestamp" => execution_start,
            "modifications" => 0  # Would track actual modifications
        )
        push!(executions, execution_record)
        
    catch e
        execution_record = Dict{String, Any}(
            "block_id" => block_id,
            "status" => "error",
            "error" => string(e),
            "execution_time" => Dates.value(now() - execution_start) / 1000.0,
            "timestamp" => execution_start
        )
        push!(executions, execution_record)
        rethrow(e)
    end
end

"""
    create_logic_block_context(allocations::Vector{StreamAllocation}, sampled_variables::Dict{String, Any}, rng::AbstractRNG) -> Dict{String, Any}

Create execution context for logic blocks.
"""
function create_logic_block_context(allocations::Vector{StreamAllocation}, sampled_variables::Dict{String, Any}, rng::AbstractRNG)::Dict{String, Any}
    return Dict{String, Any}(
        "allocations" => allocations,
        "variables" => sampled_variables,
        "rng" => rng,
        "current_period" => 1,
        "helper_functions" => Dict{String, Any}()
    )
end

"""
    apply_rent_growth_override!(allocations::Vector{StreamAllocation}, context::Dict{String, Any})

Example logic block implementation - apply rent growth override.
"""
function apply_rent_growth_override!(allocations::Vector{StreamAllocation}, context::Dict{String, Any})
    rent_growth = get(context["variables"], "rent_growth_rate", 0.03)
    
    for (i, allocation) in enumerate(allocations)
        if allocation.metadata["category"] == "Revenue"
            # Apply additional rent growth
            new_amount = allocation.adjusted_amount * (1.0 + rent_growth * 0.1)  # 10% of rent growth rate
            
            # Create new allocation (since structs are immutable)
            allocations[i] = StreamAllocation(
                allocation.stream_id,
                allocation.period_number,
                allocation.period_start,
                allocation.period_end,
                allocation.base_amount,
                allocation.growth_factor,
                new_amount,
                "logic_block_override",
                merge(allocation.metadata, Dict("logic_block_applied" => true))
            )
        end
    end
end

"""
    calculate_stream_totals(allocations::Vector{StreamAllocation}) -> Dict{String, Float64}

Calculate total amounts by stream ID.
"""
function calculate_stream_totals(allocations::Vector{StreamAllocation})::Dict{String, Float64}
    totals = Dict{String, Float64}()
    
    for allocation in allocations
        if haskey(totals, allocation.stream_id)
            totals[allocation.stream_id] += allocation.adjusted_amount
        else
            totals[allocation.stream_id] = allocation.adjusted_amount
        end
    end
    
    return totals
end

"""
    calculate_period_totals(allocations::Vector{StreamAllocation}) -> Dict{Int, Float64}

Calculate total amounts by period number.
"""
function calculate_period_totals(allocations::Vector{StreamAllocation})::Dict{Int, Float64}
    totals = Dict{Int, Float64}()
    
    for allocation in allocations
        if haskey(totals, allocation.period_number)
            totals[allocation.period_number] += allocation.adjusted_amount
        else
            totals[allocation.period_number] = allocation.adjusted_amount
        end
    end
    
    return totals
end

"""
    calculate_category_totals(allocations::Vector{StreamAllocation}, streams::Vector{Dict{String, Any}}) -> Dict{String, Float64}

Calculate total amounts by category.
"""
function calculate_category_totals(allocations::Vector{StreamAllocation}, streams::Vector{Dict{String, Any}})::Dict{String, Float64}
    totals = Dict{String, Float64}()
    
    for allocation in allocations
        category = get(allocation.metadata, "category", "Unknown")
        
        if haskey(totals, category)
            totals[category] += allocation.adjusted_amount
        else
            totals[category] = allocation.adjusted_amount
        end
    end
    
    return totals
end

"""
    summarize_allocation_result(result::AllocationResult) -> Dict{String, Any}

Create a summary of allocation results for debugging/logging.
"""
function summarize_allocation_result(result::AllocationResult)::Dict{String, Any}
    total_amount = sum(values(result.total_by_stream))
    
    return Dict{String, Any}(
        "total_allocations" => length(result.allocations),
        "total_amount" => round(total_amount, digits=2),
        "streams" => length(result.total_by_stream),
        "periods" => length(result.total_by_period),
        "categories" => result.total_by_category,
        "logic_blocks_executed" => length(result.logic_block_executions),
        "successful_logic_blocks" => count(ex -> get(ex, "status", "") == "success", result.logic_block_executions)
    )
end