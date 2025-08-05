# Guide to interpreting metrics and distribution results
using Pkg
Pkg.activate(".")
using CFDLEngine
using Printf

println("📖 Guide to Interpreting Metrics & Distribution Results")
println("=" ^ 60)

# Helper function
function format_currency(amount::Float64)
    if abs(amount) >= 1_000_000
        return @sprintf "\$%.1fM" (amount / 1_000_000)
    elseif abs(amount) >= 1_000
        return @sprintf "\$%.0fK" (amount / 1_000)
    else
        return @sprintf "\$%.0f" amount
    end
end

println("\n🏢 Sample Investment Analysis")
println("-" ^ 40)

# Sample real estate investment
cash_flows = [100_000.0, 110_000.0, 120_000.0, 130_000.0, 140_000.0 + 1_800_000.0]
assumptions = Dict{String, Any}(
    "discount_rate" => 0.09,
    "initial_investment" => 1_400_000.0,
    "debt_service" => [75_000.0, 75_000.0, 75_000.0, 75_000.0, 75_000.0]
)

results = CFDLEngine.calculate_all_metrics(cash_flows, assumptions)

println("Investment Details:")
println("  Initial Investment: $(format_currency(assumptions["initial_investment"]))")
println("  Required Return: $(assumptions["discount_rate"] * 100)%")
println("  5-Year Cash Flows: $(format_currency(sum(cash_flows)))")

println("\n📊 METRIC INTERPRETATIONS:")
println("=" ^ 50)

# NPV Interpretation
npv = results["npv"].value
println("\n💰 Net Present Value (NPV): $(format_currency(npv))")
if npv > 0
    println("   ✅ POSITIVE NPV = Good Investment")
    println("   💡 Interpretation: The investment creates $(format_currency(npv)) of value")
    println("      above the required $(assumptions["discount_rate"] * 100)% return.")
elseif npv < 0
    println("   ❌ NEGATIVE NPV = Poor Investment")
    println("   💡 Interpretation: The investment destroys $(format_currency(abs(npv))) of value.")
else
    println("   ⚖️  ZERO NPV = Breakeven Investment")
    println("   💡 Interpretation: The investment exactly meets the required return.")
end

# IRR Interpretation
irr = results["irr"].value * 100
required_return = assumptions["discount_rate"] * 100
println("\n📈 Internal Rate of Return (IRR): $(@sprintf "%.2f%%" irr)")
if irr > required_return
    println("   ✅ IRR > Required Return = Good Investment")
    println("   💡 Interpretation: The investment returns $(@sprintf "%.2f%%" irr), which is")
    println("      $(@sprintf "%.2f" (irr - required_return)) percentage points above the $(@sprintf "%.1f%%" required_return) hurdle rate.")
elseif irr < required_return
    println("   ❌ IRR < Required Return = Poor Investment")
    println("   💡 Interpretation: The investment only returns $(@sprintf "%.2f%%" irr), which is")
    println("      $(@sprintf "%.2f" (required_return - irr)) percentage points below the $(@sprintf "%.1f%%" required_return) hurdle rate.")
else
    println("   ⚖️  IRR = Required Return = Breakeven Investment")
end

# MOIC Interpretation
moic = results["moic"].value
println("\n💵 Multiple on Invested Capital (MOIC): $(@sprintf "%.2fx" moic)")
if moic > 2.0
    println("   ✅ MOIC > 2.0x = Strong Return")
    println("   💡 Interpretation: For every \$1 invested, you get \$$(@sprintf "%.2f" moic) back.")
elseif moic > 1.0
    println("   ⚖️  MOIC > 1.0x = Positive Return")
    println("   💡 Interpretation: For every \$1 invested, you get \$$(@sprintf "%.2f" moic) back.")
else
    println("   ❌ MOIC < 1.0x = Loss")
    println("   💡 Interpretation: You lose money - only get \$$(@sprintf "%.2f" moic) for every \$1 invested.")
end

# DSCR Interpretation
dscr = results["dscr"].value
println("\n🏦 Debt Service Coverage Ratio (DSCR): $(@sprintf "%.2f" dscr)")
if dscr > 1.25
    println("   ✅ DSCR > 1.25 = Strong Debt Coverage")
    println("   💡 Interpretation: Operating income is $(@sprintf "%.2fx" dscr) the debt payments.")
    println("      Lenders typically require DSCR > 1.25 for commercial loans.")
elseif dscr > 1.0
    println("   ⚠️  DSCR > 1.0 = Adequate Debt Coverage")
    println("   💡 Interpretation: Operating income covers debt, but with little cushion.")
else
    println("   ❌ DSCR < 1.0 = Insufficient Debt Coverage")
    println("   💡 Interpretation: Operating income cannot cover debt payments.")
end

# Payback Period Interpretation
payback = results["payback"].value
println("\n⏱️  Payback Period: $(@sprintf "%.1f years" payback)")
if payback < 5
    println("   ✅ Payback < 5 years = Quick Recovery")
    println("   💡 Interpretation: You recover your initial investment in $(@sprintf "%.1f years" payback).")
elseif payback < 10
    println("   ⚖️  Payback < 10 years = Reasonable Recovery")
    println("   💡 Interpretation: Moderate time to recover initial investment.")
else
    println("   ❌ Payback > 10 years = Slow Recovery")
    println("   💡 Interpretation: Long time to recover initial investment.")
end

# Monte Carlo Analysis
println("\n\n🎲 MONTE CARLO STOCHASTIC ANALYSIS")
println("=" ^ 50)

println("Running Monte Carlo simulation to understand risk...")

# Generate 500 trials with uncertainty
trial_metrics = Vector{Dict{String, CFDLEngine.MetricResult}}()

for trial in 1:500
    # Add uncertainty: ±20% to cash flows, ±1.5% to discount rate
    uncertain_cfs = cash_flows .* (1.0 .+ randn(5) * 0.20)
    uncertain_rate = assumptions["discount_rate"] + randn() * 0.015
    
    trial_assumptions = Dict{String, Any}(
        "discount_rate" => max(0.05, min(0.15, uncertain_rate)),
        "initial_investment" => assumptions["initial_investment"],
        "debt_service" => assumptions["debt_service"]
    )
    
    metrics = CFDLEngine.calculate_all_metrics(uncertain_cfs, trial_assumptions)
    push!(trial_metrics, metrics)
end

stochastic_result = CFDLEngine.analyze_stochastic_metrics(trial_metrics)

println("✅ Completed $(stochastic_result.successful_trials) Monte Carlo trials")

# Interpret distributions
for metric_name in ["npv", "irr"]
    if haskey(stochastic_result.distributions, metric_name)
        dist = stochastic_result.distributions[metric_name]
        
        println("\n📊 $(uppercase(metric_name)) Risk Analysis:")
        
        if metric_name == "npv"
            println("   Expected NPV: $(format_currency(dist.mean))")
            println("   Worst Case (5th percentile): $(format_currency(dist.percentiles[0.05]))")
            println("   Best Case (95th percentile): $(format_currency(dist.percentiles[0.95]))")
            
            # Calculate probability of loss
            loss_count = count(x -> x < 0, [trial["npv"].value for trial in trial_metrics])
            prob_loss = loss_count / length(trial_metrics) * 100
            
            println("   📉 Risk Assessment:")
            if prob_loss < 10
                println("      ✅ Low Risk: $(@sprintf "%.1f%%" prob_loss) chance of loss")
            elseif prob_loss < 25
                println("      ⚠️  Medium Risk: $(@sprintf "%.1f%%" prob_loss) chance of loss")
            else
                println("      ❌ High Risk: $(@sprintf "%.1f%%" prob_loss) chance of loss")
            end
            
        else  # IRR
            println("   Expected IRR: $(@sprintf "%.2f%%" (dist.mean * 100))")
            println("   Worst Case (5th percentile): $(@sprintf "%.2f%%" (dist.percentiles[0.05] * 100))")
            println("   Best Case (95th percentile): $(@sprintf "%.2f%%" (dist.percentiles[0.95] * 100))")
            
            # Calculate probability of underperforming
            target_return = assumptions["discount_rate"]
            underperform_count = count(x -> x < target_return, [trial["irr"].value for trial in trial_metrics if isfinite(trial["irr"].value)])
            prob_underperform = underperform_count / length(trial_metrics) * 100
            
            println("   📉 Risk Assessment:")
            if prob_underperform < 15
                println("      ✅ Low Risk: $(@sprintf "%.1f%%" prob_underperform) chance of underperforming")
            elseif prob_underperform < 35
                println("      ⚠️  Medium Risk: $(@sprintf "%.1f%%" prob_underperform) chance of underperforming")
            else
                println("      ❌ High Risk: $(@sprintf "%.1f%%" prob_underperform) chance of underperforming")
            end
        end
        
        # Distribution shape interpretation
        if abs(dist.skewness) < 0.5
            println("   📈 Distribution: Symmetric (balanced upside/downside)")
        elseif dist.skewness > 0.5
            println("   📈 Distribution: Right-skewed (more upside potential)")
        else
            println("   📈 Distribution: Left-skewed (more downside risk)")
        end
    end
end

println("\n\n🎯 INVESTMENT DECISION FRAMEWORK")
println("=" ^ 50)

# Decision framework
npv_score = npv > 0 ? 1 : 0
irr_score = irr > required_return ? 1 : 0
moic_score = moic > 1.5 ? 1 : 0
dscr_score = dscr > 1.25 ? 1 : 0

total_score = npv_score + irr_score + moic_score + dscr_score

println("Investment Scorecard:")
println("  NPV Positive: $(npv_score == 1 ? "✅" : "❌")")
println("  IRR > Hurdle Rate: $(irr_score == 1 ? "✅" : "❌")")  
println("  MOIC > 1.5x: $(moic_score == 1 ? "✅" : "❌")")
println("  DSCR > 1.25: $(dscr_score == 1 ? "✅" : "❌")")
println("  Score: $total_score/4")

println("\n🏆 RECOMMENDATION:")
if total_score >= 3
    println("   ✅ STRONG BUY - Investment meets most criteria")
elseif total_score >= 2
    println("   ⚖️  CONSIDER - Investment has mixed signals, analyze risk tolerance")
else
    println("   ❌ AVOID - Investment fails most criteria")
end

println("\n📚 Key Takeaways:")
println("   • Use multiple metrics together, not individually")
println("   • Consider risk tolerance and market conditions") 
println("   • Monte Carlo analysis reveals potential outcomes")
println("   • Always validate assumptions and sensitivity")
println("   • Higher returns typically come with higher risk")