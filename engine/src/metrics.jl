# engine/src/metrics.jl
using Dates
using Statistics

"""
    MetricResult

Represents the result of a single financial metric calculation.
"""
struct MetricResult
    metric_name::String
    value::Float64
    calculation_date::Date
    assumptions::Dict{String, Any}
    metadata::Dict{String, Any}
end

"""
    calculate_npv(cash_flows::Vector{Float64}, discount_rate::Float64, initial_investment::Float64) -> Float64

Calculate Net Present Value (NPV) of a cash flow series.

NPV = -Initial_Investment + PV(cash_flows)

Args:
- cash_flows: Vector of periodic cash flows (excluding initial investment)
- discount_rate: Annual discount rate (e.g., 0.10 for 10%)
- initial_investment: Initial capital outlay (positive number)

Returns:
- NPV value (positive indicates profitable investment)
"""
function calculate_npv(cash_flows::Vector{Float64}, discount_rate::Float64, initial_investment::Float64)::Float64
    if isempty(cash_flows)
        return -initial_investment
    end
    
    # Filter out non-finite values with warning
    valid_cfs = Float64[]
    
    for (t, cf) in enumerate(cash_flows)
        if !isfinite(cf)
            @warn "Non-finite cash flow at period $t: $cf"
            continue
        end
        push!(valid_cfs, cf)
    end
    
    if isempty(valid_cfs)
        return -initial_investment
    end
    
    # Calculate present value manually
    pv_future_flows = 0.0
    for (t, cf) in enumerate(valid_cfs)
        pv_future_flows += cf / (1 + discount_rate)^t
    end
    
    return pv_future_flows - initial_investment
end

"""
    calculate_irr(cash_flows::Vector{Float64}, initial_investment::Float64; max_iterations::Int=100, tolerance::Float64=1e-6) -> Float64

Calculate Internal Rate of Return (IRR) using Newton-Raphson method.

IRR is the discount rate that makes NPV = 0.

Args:
- cash_flows: Vector of periodic cash flows (excluding initial investment)
- initial_investment: Initial capital outlay (positive number)
- max_iterations: Maximum iterations for solver
- tolerance: Convergence tolerance

Returns:
- IRR as decimal (e.g., 0.15 for 15%), or NaN if no solution found
"""
function calculate_irr(cash_flows::Vector{Float64}, initial_investment::Float64; max_iterations::Int=100, tolerance::Float64=1e-6)::Float64
    if isempty(cash_flows)
        return NaN
    end
    
    # Filter out non-finite values
    valid_cfs = Float64[]
    
    for (t, cf) in enumerate(cash_flows)
        if !isfinite(cf)
            @warn "Non-finite cash flow at period $t: $cf"
            continue
        end
        push!(valid_cfs, cf)
    end
    
    if isempty(valid_cfs)
        return NaN
    end
    
    # Use custom Newton-Raphson implementation
    return calculate_irr_custom(valid_cfs, initial_investment, max_iterations, tolerance)
end

"""
    calculate_irr_custom(cash_flows::Vector{Float64}, initial_investment::Float64, max_iterations::Int, tolerance::Float64) -> Float64

Custom IRR calculation fallback using Newton-Raphson method.
"""
function calculate_irr_custom(cash_flows::Vector{Float64}, initial_investment::Float64, max_iterations::Int, tolerance::Float64)::Float64
    # Initial guess: simple approximation
    total_cf = sum(cash_flows)
    if total_cf <= initial_investment
        return NaN  # No positive return possible
    end
    
    # Initial guess based on simple payback
    guess = (total_cf / initial_investment - 1) / length(cash_flows)
    guess = max(guess, 0.01)  # Ensure positive starting point
    
    # Newton-Raphson iteration
    for iteration in 1:max_iterations
        # Calculate NPV and its derivative at current guess
        npv = -initial_investment
        npv_derivative = 0.0
        
        for (t, cf) in enumerate(cash_flows)
            discount_factor = (1 + guess) ^ t
            npv += cf / discount_factor
            npv_derivative -= t * cf / (discount_factor * (1 + guess))
        end
        
        # Check convergence
        if abs(npv) < tolerance
            return guess
        end
        
        # Check for problematic derivative
        if abs(npv_derivative) < 1e-10
            @warn "IRR calculation: derivative too small, may not converge"
            break
        end
        
        # Newton-Raphson update
        new_guess = guess - npv / npv_derivative
        
        # Ensure reasonable bounds
        if new_guess < -0.99 || new_guess > 10.0  # -99% to 1000%
            # Try bisection method fallback
            return calculate_irr_bisection(cash_flows, initial_investment)
        end
        
        # Check for convergence in guess
        if abs(new_guess - guess) < tolerance
            return new_guess
        end
        
        guess = new_guess
    end
    
    @warn "IRR calculation did not converge after $max_iterations iterations"
    return NaN
end

"""
    calculate_irr_bisection(cash_flows::Vector{Float64}, initial_investment::Float64) -> Float64

Fallback IRR calculation using bisection method for problematic cases.
"""
function calculate_irr_bisection(cash_flows::Vector{Float64}, initial_investment::Float64)::Float64
    # Define NPV function
    function npv_at_rate(rate::Float64)::Float64
        return calculate_npv(cash_flows, rate, initial_investment)
    end
    
    # Find bounds where NPV changes sign
    low_rate = -0.99
    high_rate = 10.0
    
    npv_low = npv_at_rate(low_rate)
    npv_high = npv_at_rate(high_rate)
    
    # Check if solution exists in bounds
    if npv_low * npv_high > 0
        return NaN  # No sign change, no solution in bounds
    end
    
    # Bisection method
    for _ in 1:100
        mid_rate = (low_rate + high_rate) / 2
        npv_mid = npv_at_rate(mid_rate)
        
        if abs(npv_mid) < 1e-6
            return mid_rate
        end
        
        if npv_low * npv_mid < 0
            high_rate = mid_rate
            npv_high = npv_mid
        else
            low_rate = mid_rate
            npv_low = npv_mid
        end
        
        if abs(high_rate - low_rate) < 1e-6
            return (low_rate + high_rate) / 2
        end
    end
    
    return NaN
end

"""
    calculate_dscr(operating_cash_flows::Vector{Float64}, debt_service::Vector{Float64}) -> Vector{Float64}

Calculate Debt Service Coverage Ratio (DSCR) for each period.

DSCR = Operating Cash Flow / Debt Service

Args:
- operating_cash_flows: Net operating cash flows for each period
- debt_service: Total debt service (principal + interest) for each period

Returns:
- Vector of DSCR values for each period (>1.0 indicates adequate coverage)
"""
function calculate_dscr(operating_cash_flows::Vector{Float64}, debt_service::Vector{Float64})::Vector{Float64}
    if length(operating_cash_flows) != length(debt_service)
        throw(ArgumentError("Operating cash flows and debt service vectors must have same length"))
    end
    
    dscr_values = Float64[]
    
    for (ocf, ds) in zip(operating_cash_flows, debt_service)
        if ds == 0.0
            # No debt service, DSCR is undefined (use Inf to indicate no debt burden)
            push!(dscr_values, Inf)
        else
            push!(dscr_values, ocf / ds)
        end
    end
    
    return dscr_values
end

"""
    calculate_moic(total_distributions::Float64, initial_investment::Float64) -> Float64

Calculate Multiple on Invested Capital (MOIC).

MOIC = Total Distributions / Initial Investment

Args:
- total_distributions: Sum of all cash distributions to investors
- initial_investment: Initial capital invested

Returns:
- MOIC value (e.g., 2.5 means 2.5x return on investment)
"""
function calculate_moic(total_distributions::Float64, initial_investment::Float64)::Float64
    if initial_investment == 0.0
        return NaN
    end
    
    return total_distributions / initial_investment
end

"""
    calculate_payback_period(cash_flows::Vector{Float64}, initial_investment::Float64) -> Float64

Calculate payback period in number of periods.

Payback period is the time required to recover the initial investment.

Args:
- cash_flows: Vector of periodic cash flows (excluding initial investment)
- initial_investment: Initial capital outlay (positive number)

Returns:
- Payback period in periods (fractional values indicate partial period recovery)
- Returns Inf if investment is never recovered
"""
function calculate_payback_period(cash_flows::Vector{Float64}, initial_investment::Float64)::Float64
    if isempty(cash_flows) || initial_investment <= 0.0
        return Inf
    end
    
    cumulative_cf = 0.0
    
    for (period, cf) in enumerate(cash_flows)
        cumulative_cf += cf
        
        if cumulative_cf >= initial_investment
            # Investment recovered in this period
            if cf == 0.0
                return Float64(period)
            else
                # Calculate fractional period
                excess = cumulative_cf - initial_investment
                fraction_of_period = 1.0 - (excess / cf)
                return Float64(period - 1) + fraction_of_period
            end
        end
    end
    
    # Investment never recovered
    return Inf
end

"""
    calculate_all_metrics(cash_flows::Vector{Float64}, assumptions::Dict{String, Any}) -> Dict{String, MetricResult}

Calculate all financial metrics for a cash flow series.

Args:
- cash_flows: Vector of periodic cash flows (excluding initial investment)
- assumptions: Dictionary containing discount_rate, initial_investment, debt_service, etc.

Returns:
- Dictionary mapping metric names to MetricResult structs
"""
function calculate_all_metrics(cash_flows::Vector{Float64}, assumptions::Dict{String, Any})::Dict{String, MetricResult}
    results = Dict{String, MetricResult}()
    calc_date = today()
    
    # Extract assumptions with defaults
    discount_rate = get(assumptions, "discount_rate", 0.10)
    initial_investment = get(assumptions, "initial_investment", 0.0)
    debt_service = get(assumptions, "debt_service", zeros(length(cash_flows)))
    
    # Ensure debt_service is the right length
    if length(debt_service) != length(cash_flows)
        debt_service = zeros(length(cash_flows))
    end
    
    # Calculate NPV
    npv_value = calculate_npv(cash_flows, discount_rate, initial_investment)
            results["npv"] = MetricResult(
        "npv", npv_value, calc_date,
        Dict("discount_rate" => discount_rate, "initial_investment" => initial_investment),
        Dict("calculation_method" => "manual_pv")
    )
    
    # Calculate IRR
    irr_value = calculate_irr(cash_flows, initial_investment)
    results["irr"] = MetricResult(
        "irr", irr_value, calc_date,
        Dict("initial_investment" => initial_investment),
        Dict("calculation_method" => "newton_raphson", "convergence_tolerance" => 1e-6)
    )
    
    # Calculate DSCR (average)
    if !all(ds -> ds == 0.0, debt_service)
        dscr_values = calculate_dscr(cash_flows, debt_service)
        avg_dscr = mean(filter(x -> isfinite(x), dscr_values))
        results["dscr"] = MetricResult(
            "dscr", avg_dscr, calc_date,
            Dict("debt_service_total" => sum(debt_service)),
            Dict("calculation_method" => "average_periodic", "periods" => length(dscr_values))
        )
    end
    
    # Calculate MOIC
    total_distributions = sum(cash_flows)
    moic_value = calculate_moic(total_distributions, initial_investment)
    results["moic"] = MetricResult(
        "moic", moic_value, calc_date,
        Dict("total_distributions" => total_distributions, "initial_investment" => initial_investment),
        Dict("calculation_method" => "total_return_multiple")
    )
    
    # Calculate Payback Period
    payback_value = calculate_payback_period(cash_flows, initial_investment)
    results["payback"] = MetricResult(
        "payback", payback_value, calc_date,
        Dict("initial_investment" => initial_investment),
        Dict("calculation_method" => "cumulative_cash_flow", "units" => "periods")
    )
    
    return results
end