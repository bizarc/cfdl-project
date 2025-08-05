# Core type definitions for CFDL Engine

"""
    StochasticParameter

Represents a parameter that can be either fixed or follow a distribution/process.
"""
abstract type StochasticParameter end

"""
    FixedParameter{T}

A parameter with a fixed value.
"""
struct FixedParameter{T} <: StochasticParameter
    value::T
end

"""
    DistributionParameter

A parameter sampled from a probability distribution.
"""
struct DistributionParameter <: StochasticParameter
    distribution_type::String
    parameters::Dict{String, Any}
end

"""
    RandomWalkParameter

A parameter that follows a random walk process.
"""
struct RandomWalkParameter <: StochasticParameter
    initial_value::Float64
    drift::Float64
    volatility::Float64
end

"""
    IRData

Container for all data loaded from IR JSON file.
"""
struct IRData
    deal::Dict{String, Any}
    assets::Vector{Dict{String, Any}}
    components::Vector{Dict{String, Any}}
    streams::Vector{Dict{String, Any}}
    calendar::Dict{String, Any}
    assumptions::Dict{String, Any}
    logic_blocks::Vector{Dict{String, Any}}
    waterfall::Union{Dict{String, Any}, Nothing}
    stochastic_params::Dict{String, StochasticParameter}
end

"""
    TrialResult

Result from a single Monte Carlo trial execution.
"""
struct TrialResult
    trial_id::Int
    seed::UInt32
    sampled_variables::Dict{String, Any}
    cash_flows::Union{Dict{String, Any}, Nothing}
    metrics::Union{Dict{String, Any}, Nothing}
    distributions::Union{Dict{String, Any}, Nothing}
    execution_time::Float64
    status::Symbol  # :success, :error, :timeout
    error_message::Union{String, Nothing}
end

"""
    MonteCarloResult

Complete result from Monte Carlo simulation.
"""
struct MonteCarloResult
    trials::Vector{TrialResult}
    total_trials::Int
    successful_trials::Int
    total_execution_time::Float64
    summary_statistics::Dict{String, Any}
end