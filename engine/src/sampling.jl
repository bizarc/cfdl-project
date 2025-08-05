# Stochastic parameter sampling functionality

"""
    sample_variables(ir_data::IRData, trial_id::Int, rng::AbstractRNG) -> Dict{String, Any}

Sample all stochastic variables for a single trial.
"""
function sample_variables(ir_data::IRData, trial_id::Int, rng::AbstractRNG)::Dict{String, Any}
    sampled_vars = Dict{String, Any}()
    
    for (param_name, param) in ir_data.stochastic_params
        sampled_value = sample_parameter(param, rng)
        sampled_vars[param_name] = sampled_value
    end
    
    return sampled_vars
end

"""
    sample_parameter(param::StochasticParameter, rng::AbstractRNG) -> Any

Sample a single parameter based on its type.
"""
function sample_parameter(param::FixedParameter, rng::AbstractRNG)
    return param.value
end

function sample_parameter(param::DistributionParameter, rng::AbstractRNG)
    if param.distribution_type == "Normal"
        mean = param.parameters["mean"]
        std = param.parameters["std"]
        return rand(rng, Normal(mean, std))
    
    elseif param.distribution_type == "Uniform"
        min_val = param.parameters["min"]
        max_val = param.parameters["max"]
        return rand(rng, Uniform(min_val, max_val))
    
    elseif param.distribution_type == "LogNormal"
        meanlog = param.parameters["meanlog"]
        stdlog = param.parameters["stdlog"]
        return rand(rng, LogNormal(meanlog, stdlog))
    
    else
        throw(ArgumentError("Unsupported distribution type: $(param.distribution_type)"))
    end
end

function sample_parameter(param::RandomWalkParameter, rng::AbstractRNG)
    # For random walk, return the initial value
    # The time evolution will be handled in the temporal grid/stream allocator
    return param.initial_value
end

"""
    evolve_random_walk(param::RandomWalkParameter, current_value::Float64, dt::Float64, rng::AbstractRNG) -> Float64

Evolve a random walk parameter by one time step.
"""
function evolve_random_walk(param::RandomWalkParameter, current_value::Float64, dt::Float64, rng::AbstractRNG)::Float64
    # Geometric Brownian Motion: dS = μS*dt + σS*dW
    drift_term = param.drift * current_value * dt
    diffusion_term = param.volatility * current_value * sqrt(dt) * randn(rng)
    
    return current_value + drift_term + diffusion_term
end

"""
    generate_trial_seed(trial_id::Int, base_seed::UInt32) -> UInt32

Generate a unique, reproducible seed for each trial.
"""
function generate_trial_seed(trial_id::Int, base_seed::UInt32)::UInt32
    # Create reproducible but unique seed for each trial
    return hash(trial_id, UInt64(base_seed)) % UInt32
end

"""
    create_trial_rng(trial_seed::UInt32) -> MersenneTwister

Create a random number generator for a specific trial.
"""
function create_trial_rng(trial_seed::UInt32)::MersenneTwister
    return MersenneTwister(trial_seed)
end

"""
    get_parameter_summary_stats(results::Vector{TrialResult}, param_name::String) -> Dict{String, Float64}

Calculate summary statistics for a sampled parameter across all trials.
"""
function get_parameter_summary_stats(results::Vector{TrialResult}, param_name::String)::Dict{String, Float64}
    values = Float64[]
    
    for result in results
        if result.status == :success && haskey(result.sampled_variables, param_name)
            value = result.sampled_variables[param_name]
            if isa(value, Number)
                push!(values, Float64(value))
            end
        end
    end
    
    if isempty(values)
        return Dict{String, Float64}()
    end
    
    return Dict(
        "mean" => mean(values),
        "std" => std(values),
        "min" => minimum(values),
        "max" => maximum(values),
        "median" => median(values),
        "p25" => quantile(values, 0.25),
        "p75" => quantile(values, 0.75),
        "count" => Float64(length(values))
    )
end