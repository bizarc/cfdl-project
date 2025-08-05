# engine/src/stochastic_analysis.jl
using Statistics
using StatsBase
using Distributions

"""
    DistributionStats

Statistical analysis of a metric's distribution across Monte Carlo trials.
"""
struct DistributionStats
    mean::Float64
    median::Float64
    std_dev::Float64
    variance::Float64
    skewness::Float64
    kurtosis::Float64
    percentiles::Dict{Float64, Float64}  # e.g., 0.05 => 5th percentile value
    confidence_intervals::Dict{Float64, Tuple{Float64, Float64}}
    var_95::Float64  # Value at Risk (95% confidence)
    cvar_95::Float64  # Conditional Value at Risk
    min_value::Float64
    max_value::Float64
    sample_size::Int
end

"""
    StochasticMetricsResult

Complete stochastic analysis results for all metrics across Monte Carlo trials.
"""
struct StochasticMetricsResult
    # Individual trial metrics (for detailed analysis)
    trial_metrics::Vector{Dict{String, MetricResult}}
    
    # Distribution analysis for each metric
    distributions::Dict{String, DistributionStats}
    
    # Expected values (means) - convenience accessors
    expected_values::Dict{String, Float64}
    
    # Summary statistics
    num_trials::Int
    successful_trials::Int
    calculation_date::Date
end

"""
    analyze_distribution(values::Vector{Float64}) -> DistributionStats

Analyze the statistical distribution of a metric across Monte Carlo trials.

Args:
- values: Vector of metric values from different trials

Returns:
- DistributionStats with complete statistical analysis
"""
function analyze_distribution(values::Vector{Float64})::DistributionStats
    # Filter out invalid values
    valid_values = filter(x -> isfinite(x), values)
    
    if isempty(valid_values)
        # Return empty stats if no valid values
        return DistributionStats(
            NaN, NaN, NaN, NaN, NaN, NaN,
            Dict{Float64, Float64}(),
            Dict{Float64, Tuple{Float64, Float64}}(),
            NaN, NaN, NaN, NaN, 0
        )
    end
    
    # Sort values for percentile calculations
    sorted_values = sort(valid_values)
    n = length(sorted_values)
    
    # Basic statistics
    mean_val = mean(sorted_values)
    median_val = median(sorted_values)
    std_val = std(sorted_values)
    var_val = var(sorted_values)
    
    # Higher-order moments
    skew_val = skewness(sorted_values)
    kurt_val = kurtosis(sorted_values)
    
    # Percentiles (key risk management percentiles)
    percentile_points = [0.01, 0.05, 0.10, 0.25, 0.50, 0.75, 0.90, 0.95, 0.99]
    percentiles = Dict{Float64, Float64}()
    
    for p in percentile_points
        percentiles[p] = quantile(sorted_values, p)
    end
    
    # Confidence intervals (assuming normal distribution for large samples)
    confidence_levels = [0.90, 0.95, 0.99]
    confidence_intervals = Dict{Float64, Tuple{Float64, Float64}}()
    
    if n >= 30  # Use normal approximation for large samples
        for conf_level in confidence_levels
            alpha = 1 - conf_level
            z_score = quantile(Normal(0, 1), 1 - alpha/2)
            margin = z_score * std_val / sqrt(n)
            confidence_intervals[conf_level] = (mean_val - margin, mean_val + margin)
        end
    else
        # Use percentile method for small samples
        for conf_level in confidence_levels
            alpha = 1 - conf_level
            lower_p = alpha / 2
            upper_p = 1 - alpha / 2
            confidence_intervals[conf_level] = (quantile(sorted_values, lower_p), quantile(sorted_values, upper_p))
        end
    end
    
    # Value at Risk (VaR) - 95% confidence (5th percentile for losses)
    var_95 = percentiles[0.05]
    
    # Conditional Value at Risk (CVaR) - expected value of worst 5% outcomes
    var_index = max(1, Int(floor(0.05 * n)))
    cvar_95 = mean(sorted_values[1:var_index])
    
    # Min/Max values
    min_val = minimum(sorted_values)
    max_val = maximum(sorted_values)
    
    return DistributionStats(
        mean_val, median_val, std_val, var_val, skew_val, kurt_val,
        percentiles, confidence_intervals, var_95, cvar_95,
        min_val, max_val, n
    )
end

"""
    calculate_risk_metrics(values::Vector{Float64}, target_value::Float64=0.0) -> Dict{String, Float64}

Calculate additional risk metrics for a distribution.

Args:
- values: Vector of metric values
- target_value: Target/benchmark value (default 0.0 for downside risk)

Returns:
- Dictionary with risk metrics: downside_deviation, probability_of_loss, etc.
"""
function calculate_risk_metrics(values::Vector{Float64}, target_value::Float64=0.0)::Dict{String, Float64}
    valid_values = filter(x -> isfinite(x), values)
    
    if isempty(valid_values)
        return Dict{String, Float64}(
            "downside_deviation" => NaN,
            "probability_of_loss" => NaN,
            "expected_shortfall" => NaN,
            "gain_loss_ratio" => NaN
        )
    end
    
    # Downside deviation (volatility of returns below target)
    downside_values = filter(x -> x < target_value, valid_values)
    downside_dev = isempty(downside_values) ? 0.0 : sqrt(mean((downside_values .- target_value).^2))
    
    # Probability of loss (percentage of trials below target)
    prob_loss = length(downside_values) / length(valid_values)
    
    # Expected shortfall (average loss when below target)
    expected_shortfall = isempty(downside_values) ? 0.0 : mean(target_value .- downside_values)
    
    # Gain/Loss ratio
    upside_values = filter(x -> x > target_value, valid_values)
    avg_gain = isempty(upside_values) ? 0.0 : mean(upside_values .- target_value)
    avg_loss = expected_shortfall
    gain_loss_ratio = (avg_loss == 0.0) ? Inf : avg_gain / avg_loss
    
    return Dict{String, Float64}(
        "downside_deviation" => downside_dev,
        "probability_of_loss" => prob_loss,
        "expected_shortfall" => expected_shortfall,
        "gain_loss_ratio" => gain_loss_ratio
    )
end

"""
    analyze_stochastic_metrics(trial_metrics::Vector{Dict{String, MetricResult}}) -> StochasticMetricsResult

Analyze distributions of all metrics across Monte Carlo trials.

Args:
- trial_metrics: Vector of metric dictionaries from each trial

Returns:
- StochasticMetricsResult with complete distribution analysis
"""
function analyze_stochastic_metrics(trial_metrics::Vector{Dict{String, MetricResult}})::StochasticMetricsResult
    if isempty(trial_metrics)
        return StochasticMetricsResult(
            trial_metrics,
            Dict{String, DistributionStats}(),
            Dict{String, Float64}(),
            0, 0, today()
        )
    end
    
    # Get all metric names from first trial
    metric_names = keys(trial_metrics[1])
    
    # Collect values for each metric across all trials
    metric_values = Dict{String, Vector{Float64}}()
    for metric_name in metric_names
        metric_values[metric_name] = Float64[]
    end
    
    successful_trials = 0
    for trial_result in trial_metrics
        if !isempty(trial_result)
            successful_trials += 1
            for metric_name in metric_names
                if haskey(trial_result, metric_name)
                    push!(metric_values[metric_name], trial_result[metric_name].value)
                end
            end
        end
    end
    
    # Analyze distribution for each metric
    distributions = Dict{String, DistributionStats}()
    expected_values = Dict{String, Float64}()
    
    for metric_name in metric_names
        if haskey(metric_values, metric_name) && !isempty(metric_values[metric_name])
            dist_stats = analyze_distribution(metric_values[metric_name])
            distributions[metric_name] = dist_stats
            expected_values[metric_name] = dist_stats.mean
        end
    end
    
    return StochasticMetricsResult(
        trial_metrics,
        distributions,
        expected_values,
        length(trial_metrics),
        successful_trials,
        today()
    )
end

"""
    generate_distribution_summary(dist_stats::DistributionStats, metric_name::String) -> Dict{String, Any}

Generate a human-readable summary of distribution statistics.

Args:
- dist_stats: DistributionStats to summarize
- metric_name: Name of the metric for context

Returns:
- Dictionary with formatted summary information
"""
function generate_distribution_summary(dist_stats::DistributionStats, metric_name::String)::Dict{String, Any}
    if dist_stats.sample_size == 0
        return Dict{String, Any}(
            "metric_name" => metric_name,
            "status" => "no_valid_data",
            "message" => "No valid values available for analysis"
        )
    end
    
    # Determine distribution characteristics
    is_symmetric = abs(dist_stats.skewness) < 0.5
    is_normal = abs(dist_stats.skewness) < 0.5 && abs(dist_stats.kurtosis) < 3.0
    
    distribution_shape = if is_normal
        "approximately_normal"
    elseif dist_stats.skewness > 0.5
        "right_skewed"
    elseif dist_stats.skewness < -0.5
        "left_skewed"
    else
        "symmetric"
    end
    
    # Risk assessment
    coefficient_of_variation = abs(dist_stats.std_dev / dist_stats.mean)
    risk_level = if coefficient_of_variation < 0.1
        "low_risk"
    elseif coefficient_of_variation < 0.3
        "moderate_risk"
    else
        "high_risk"
    end
    
    return Dict{String, Any}(
        "metric_name" => metric_name,
        "sample_size" => dist_stats.sample_size,
        "central_tendency" => Dict(
            "mean" => round(dist_stats.mean, digits=2),
            "median" => round(dist_stats.median, digits=2)
        ),
        "variability" => Dict(
            "std_dev" => round(dist_stats.std_dev, digits=2),
            "coefficient_of_variation" => round(coefficient_of_variation, digits=3)
        ),
        "distribution_shape" => distribution_shape,
        "risk_level" => risk_level,
        "key_percentiles" => Dict(
            "5th" => round(dist_stats.percentiles[0.05], digits=2),
            "25th" => round(dist_stats.percentiles[0.25], digits=2),
            "75th" => round(dist_stats.percentiles[0.75], digits=2),
            "95th" => round(dist_stats.percentiles[0.95], digits=2)
        ),
        "risk_metrics" => Dict(
            "var_95" => round(dist_stats.var_95, digits=2),
            "cvar_95" => round(dist_stats.cvar_95, digits=2)
        ),
        "range" => Dict(
            "min" => round(dist_stats.min_value, digits=2),
            "max" => round(dist_stats.max_value, digits=2)
        )
    )
end

"""
    compare_distributions(dist1::DistributionStats, dist2::DistributionStats, name1::String, name2::String) -> Dict{String, Any}

Compare two metric distributions for relative analysis.

Args:
- dist1, dist2: DistributionStats to compare
- name1, name2: Names for the distributions

Returns:
- Dictionary with comparison analysis
"""
function compare_distributions(dist1::DistributionStats, dist2::DistributionStats, name1::String, name2::String)::Dict{String, Any}
    return Dict{String, Any}(
        "comparison_type" => "distribution_comparison",
        "distributions" => [name1, name2],
        "mean_difference" => round(dist1.mean - dist2.mean, digits=2),
        "mean_ratio" => dist2.mean != 0 ? round(dist1.mean / dist2.mean, digits=3) : NaN,
        "volatility_comparison" => Dict(
            name1 => round(dist1.std_dev / abs(dist1.mean), digits=3),
            name2 => round(dist2.std_dev / abs(dist2.mean), digits=3)
        ),
        "risk_comparison" => Dict(
            "var_95_difference" => round(dist1.var_95 - dist2.var_95, digits=2),
            "better_downside_protection" => dist1.var_95 > dist2.var_95 ? name1 : name2
        ),
        "dominance_analysis" => Dict(
            "higher_mean" => dist1.mean > dist2.mean ? name1 : name2,
            "lower_volatility" => dist1.std_dev < dist2.std_dev ? name1 : name2,
            "better_var" => dist1.var_95 > dist2.var_95 ? name1 : name2
        )
    )
end