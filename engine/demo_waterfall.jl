#!/usr/bin/env julia
# Waterfall Distribution Visualization Demo

using Pkg
Pkg.activate(@__DIR__)
using CFDLEngine
using Random
using Dates
using Printf

function main()
    println("🌊 CFDL Waterfall Distribution Demo")
    println("=====================================")
    
    # Create sample available cash representing FINAL EXIT DISTRIBUTION
    # In reality, waterfalls typically apply at exit/disposition, not period-by-period
    # Using $60M exit proceeds to show realistic PE waterfall with positive returns
    final_exit_cash = [
        AvailableCashCalculation("deal_exit", Date(2024, 12, 31), Date(2024, 12, 31), 60000000.0, 0.0, 0.0, 0.0, 60000000.0, Dict{String, Any}("event" => "asset_disposition", "exit_multiple" => 2.0))
    ]
    
    # Demo 1: Commercial Real Estate Waterfall
    println("\n🏢 DEMO 1: COMMERCIAL REAL ESTATE WATERFALL")
    println("=" ^ 50)
    
    cre_waterfall = create_cre_waterfall()
    cre_ir_data = IRData(
        Dict{String, Any}("id" => "cre_deal", "name" => "Downtown Office Building"),
        [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], cre_waterfall, Dict{String, Any}()
    )
    
    cre_distributions = execute_waterfall_distribution(final_exit_cash, cre_ir_data)
    visualize_waterfall_distributions("Commercial Real Estate", cre_distributions, 2000000.0)  # $2M initial investment
    
    # Demo 2: Private Equity Waterfall
    println("\n💼 DEMO 2: PRIVATE EQUITY WATERFALL") 
    println("=" ^ 50)
    
    pe_waterfall = create_pe_waterfall()
    pe_capital_stack = create_pe_capital_stack()
    pe_ir_data = IRData(
        Dict{String, Any}("id" => "pe_deal", "name" => "Growth Equity Investment", "capitalStack" => pe_capital_stack),
        [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], pe_waterfall, Dict{String, Any}()
    )
    
    pe_distributions = execute_waterfall_distribution(final_exit_cash, pe_ir_data)
    visualize_waterfall_distributions("Private Equity", pe_distributions, 30000000.0)  # $30M initial investment
    
    # Demo 3: Infrastructure Project Finance
    println("\n🏗️  DEMO 3: INFRASTRUCTURE PROJECT FINANCE")
    println("=" ^ 50)
    
    infra_waterfall = create_infrastructure_waterfall()
    infra_capital_stack = create_infrastructure_capital_stack()
    infra_ir_data = IRData(
        Dict{String, Any}("id" => "infra_deal", "name" => "Solar Power Project", "capitalStack" => infra_capital_stack),
        [], [], [], Dict{String, Any}(), Dict{String, Any}(), [], infra_waterfall, Dict{String, Any}()
    )
    
    infra_distributions = execute_waterfall_distribution(final_exit_cash, infra_ir_data)
    visualize_waterfall_distributions("Infrastructure", infra_distributions, 20000000.0)  # $20M initial investment
    
    # Summary across all waterfalls
    println("\n📊 SUMMARY COMPARISON")
    println("=" ^ 50)
    
    all_distributions = [
        ("Commercial Real Estate", cre_distributions),
        ("Private Equity", pe_distributions), 
        ("Infrastructure", infra_distributions)
    ]
    
    compare_waterfall_efficiency(all_distributions)
    
    println("\n✨ Waterfall visualization demo completed!")
    return 0
end

function create_cre_waterfall()
    return Dict{String, Any}(
        "id" => "cre_pref_promote",
        "description" => "Commercial real estate preferred return + promote structure",
        "tiers" => [
            Dict{String, Any}(
                "id" => "preferred_return",
                "description" => "8% preferred return to LP investors",
                "condition" => "totalDistributed < 500000",
                "distribute" => [Dict{String, Any}("recipient" => "limited_partner", "percentage" => 1.0)]
            ),
            Dict{String, Any}(
                "id" => "promote_tier_1", 
                "description" => "70/30 split up to 12% IRR",
                "condition" => "totalDistributed < 1000000",
                "distribute" => [
                    Dict{String, Any}("recipient" => "limited_partner", "percentage" => 0.7),
                    Dict{String, Any}("recipient" => "general_partner", "percentage" => 0.3)
                ]
            ),
            Dict{String, Any}(
                "id" => "promote_tier_2",
                "description" => "50/50 split thereafter", 
                "condition" => "remainingCapital > 0",
                "distribute" => [
                    Dict{String, Any}("recipient" => "limited_partner", "percentage" => 0.5),
                    Dict{String, Any}("recipient" => "general_partner", "percentage" => 0.5)
                ]
            )
        ]
    )
end

function create_pe_waterfall()
    # Standard PE European Waterfall based on $30M fund with $27M LP / $3M GP
    # Generally accepted financial rules:
    # 1. Return of Capital: 100% to LPs until all invested capital returned
    # 2. Preferred Return: 8% annual return to LPs on unreturned capital  
    # 3. GP Catch-up: GP gets 100% until they have 20% of total distributions
    # 4. Carried Interest: 80/20 split thereafter
    
    lp_invested_capital = 27000000.0  # $27M LP investment
    annual_pref_rate = 0.08  # 8% annual preferred return
    years_held = 3.0  # Assume 3-year hold period
    
    # Calculate tier amounts based on standard PE waterfall mechanics
    return_of_capital_amount = lp_invested_capital  # $27M - return all LP capital
    preferred_return_amount = lp_invested_capital * annual_pref_rate * years_held  # $27M * 8% * 3 = $6.48M
    
    # GP catch-up calculation: GP should have 20% of (Return of Capital + Preferred Return + GP Catch-up)
    # Let X = GP catch-up amount
    # GP gets 20% of ($27M + $6.48M + X) = 0.2 * ($33.48M + X)
    # GP gets X, so: X = 0.2 * ($33.48M + X)
    # X = 0.2 * $33.48M + 0.2X
    # 0.8X = 0.2 * $33.48M
    # X = 0.25 * $33.48M = $8.37M
    gp_catchup_amount = 0.25 * (return_of_capital_amount + preferred_return_amount)
    
    total_before_carry = return_of_capital_amount + preferred_return_amount + gp_catchup_amount
    
    return Dict{String, Any}(
        "id" => "pe_european_style",
        "description" => "European-style PE waterfall with catch-up",
        "tiers" => [
            Dict{String, Any}(
                "id" => "return_of_capital",
                "description" => "Return invested capital to LPs (100% to LPs until \$27M returned)",
                "condition" => "totalDistributed < $(return_of_capital_amount)",
                "distribute" => [Dict{String, Any}("recipient" => "limited_partners", "percentage" => 1.0)]
            ),
            Dict{String, Any}(
                "id" => "preferred_return",
                "description" => "8% annual preferred return to LPs (\$6.48M over 3 years)",
                "condition" => "totalDistributed < $(return_of_capital_amount + preferred_return_amount)",
                "distribute" => [Dict{String, Any}("recipient" => "limited_partners", "percentage" => 1.0)]
            ),
            Dict{String, Any}(
                "id" => "gp_catch_up",
                "description" => "GP catch-up (100% to GP until GP has 20% of total distributions)",
                "condition" => "totalDistributed < $(total_before_carry)",
                "distribute" => [Dict{String, Any}("recipient" => "general_partner", "percentage" => 1.0)]
            ),
            Dict{String, Any}(
                "id" => "carried_interest",
                "description" => "80/20 carried interest split on remaining proceeds",
                "condition" => "remainingCapital > 0",
                "distribute" => [
                    Dict{String, Any}("recipient" => "limited_partners", "percentage" => 0.8),
                    Dict{String, Any}("recipient" => "general_partner", "percentage" => 0.2)
                ]
            )
        ]
    )
end

function create_infrastructure_waterfall()
    return Dict{String, Any}(
        "id" => "project_finance",
        "description" => "Infrastructure project finance waterfall",
        "tiers" => [
            Dict{String, Any}(
                "id" => "senior_debt_service", 
                "description" => "Senior debt principal and interest",
                "condition" => "totalDistributed < 800000",
                "distribute" => [Dict{String, Any}("recipient" => "senior_lender", "percentage" => 1.0)]
            ),
            Dict{String, Any}(
                "id" => "mezzanine_debt_service",
                "description" => "Mezzanine debt service",
                "condition" => "totalDistributed < 1200000", 
                "distribute" => [Dict{String, Any}("recipient" => "mezzanine_lender", "percentage" => 1.0)]
            ),
            Dict{String, Any}(
                "id" => "equity_distributions",
                "description" => "Equity returns to sponsors",
                "condition" => "remainingCapital > 0",
                "distribute" => [
                    Dict{String, Any}("fromCapitalStack" => true, "layerName" => "equity")
                ]
            )
        ]
    )
end

function create_pe_capital_stack()
    return Dict{String, Any}(
        "id" => "pe_capital_stack",
        "participants" => [
            Dict{String, Any}("partyId" => "institutional_lp_1", "amount" => 15000000.0),
            Dict{String, Any}("partyId" => "institutional_lp_2", "amount" => 10000000.0),
            Dict{String, Any}("partyId" => "fund_sponsor", "amount" => 5000000.0)
        ]
    )
end

function create_infrastructure_capital_stack()
    return Dict{String, Any}(
        "id" => "infra_capital_stack", 
        "participants" => [
            Dict{String, Any}("partyId" => "infrastructure_sponsor", "amount" => 8000000.0),
            Dict{String, Any}("partyId" => "pension_fund_investor", "amount" => 12000000.0)
        ]
    )
end

function visualize_waterfall_distributions(waterfall_name::String, distributions::Vector{WaterfallDistribution}, initial_investment::Float64 = 0.0)
    println("\\n📈 $waterfall_name Waterfall Results:")
    println("-" ^ 50)
    
    total_available = sum(d.total_available for d in distributions)
    total_distributed = sum(d.total_distributed for d in distributions)
    
    println(@sprintf("💰 Exit Proceeds Available: \$%s", format_currency(total_available)))
    println(@sprintf("📤 Total Distributed: \$%s", format_currency(total_distributed)))
    if initial_investment > 0
        exit_multiple = total_available / initial_investment
        println(@sprintf("📊 Exit Multiple: %.2fx (from \$%s initial investment)", exit_multiple, format_currency(initial_investment)))
    end
    
    # TIER-BY-TIER WATERFALL ANALYSIS (Primary Focus)
    if !isempty(distributions)
        dist = distributions[1]  # Single exit distribution
        println("\\n🌊 WATERFALL TIER ANALYSIS:")
        println("=" ^ 50)
        
        running_total = 0.0
        for (i, tier) in enumerate(dist.tier_distributions)
            status = tier.condition_met ? "✅" : "❌"
            println(@sprintf("\\n🎯 TIER %d: %s %s", i, uppercase(tier.tier_id), status))
            println(@sprintf("   Description: %s", tier.tier_description))
            println(@sprintf("   Condition: %s", tier.condition_value))
            println(@sprintf("   Cash Allocated: \$%s", format_currency(tier.cash_allocated)))
            
            if tier.condition_met && tier.cash_allocated > 0
                println("   📋 Recipients:")
                for recipient in tier.recipient_distributions
                    println(@sprintf("     • %s: \$%s (%.1f%% of tier)", 
                            recipient.recipient_id, format_currency(recipient.amount), 
                            recipient.percentage_of_tier * 100))
                end
                running_total += tier.cash_allocated
                println(@sprintf("   📈 Cumulative Distributed: \$%s", format_currency(running_total)))
            end
            println("-" ^ 40)
        end
    end
    
    # PARTICIPANT RETURN ANALYSIS
    recipient_totals = Dict{String, Float64}()
    for dist in distributions
        for tier in dist.tier_distributions
            for recipient in tier.recipient_distributions
                recipient_id = recipient.recipient_id
                recipient_totals[recipient_id] = get(recipient_totals, recipient_id, 0.0) + recipient.amount
            end
        end
    end
    
    if !isempty(recipient_totals) && initial_investment > 0
        println("\\n💎 PARTICIPANT RETURN ANALYSIS:")
        println("=" ^ 50)
        
        sorted_recipients = sort(collect(recipient_totals), by=x->x[2], rev=true)
        total_return_multiple = 0.0
        
        for (recipient, amount) in sorted_recipients
            percentage_of_exit = (amount / total_distributed) * 100
            
            # Estimate participant's initial investment (simplified)
            estimated_investment = estimate_participant_investment(recipient, initial_investment, recipient_totals)
            
            if estimated_investment > 0
                # CORRECT MOIC: Total Return ÷ Initial Investment
                # Total Return = Initial Investment + Profit/Loss = Initial Investment + (Distribution - Initial Investment) = Distribution
                # So MOIC = Distribution ÷ Initial Investment (this was actually correct)
                # BUT we need to be clear about what this represents
                
                total_return = amount  # This is the total cash received
                moic = total_return / estimated_investment
                
                # For IRR: calculate based on initial investment and final return
                participant_irr = calculate_simple_irr(estimated_investment, total_return, 3.0)
                
                println(@sprintf("👤 %s:", uppercase(recipient)))
                println(@sprintf("   Initial Investment: \$%s", format_currency(estimated_investment)))
                println(@sprintf("   Total Cash Received: \$%s (%.1f%% of exit proceeds)", format_currency(amount), percentage_of_exit))
                println(@sprintf("   MOIC (Multiple of Invested Capital): %.2fx", moic))
                println(@sprintf("   Estimated IRR: %.1f%%", participant_irr * 100))
                
                # Show the economics clearly
                profit_loss = total_return - estimated_investment
                if profit_loss >= 0
                    println(@sprintf("   Profit: \$%s", format_currency(profit_loss)))
                else
                    println(@sprintf("   Loss: \$%s", format_currency(abs(profit_loss))))
                end
                println()
            else
                println(@sprintf("👤 %s: \$%s (%.1f%% of exit)", uppercase(recipient), format_currency(amount), percentage_of_exit))
            end
        end
    end
end

function compare_waterfall_efficiency(waterfall_data::Vector{Tuple{String, Vector{WaterfallDistribution}}})
    println("\\n🔍 Waterfall Efficiency Comparison:")
    println(@sprintf("%-25s %12s %12s %12s %8s", "Waterfall Type", "Available", "Distributed", "Remaining", "Efficiency"))
    println("-" ^ 70)
    
    for (name, distributions) in waterfall_data
        total_available = sum(d.total_available for d in distributions)
        total_distributed = sum(d.total_distributed for d in distributions)
        total_remaining = sum(d.remaining_cash for d in distributions)
        efficiency = (total_distributed / total_available) * 100
        
        println(@sprintf("%-25s %12s %12s %12s %7.1f%%", 
                name, 
                format_currency_short(total_available),
                format_currency_short(total_distributed), 
                format_currency_short(total_remaining),
                efficiency))
    end
    
    println("\\n📊 Key Insights:")
    println("• Higher efficiency = more cash distributed vs. retained")
    println("• Different structures optimize for different investor types")
    println("• PE waterfalls typically have more complex tier structures")
    println("• Infrastructure focuses on debt service priority")
end

function format_currency(amount::Float64)::String
    if amount >= 1_000_000
        return @sprintf("%.1fM", amount / 1_000_000)
    elseif amount >= 1_000
        return @sprintf("%.0fK", amount / 1_000)
    else
        return @sprintf("%.0f", amount)
    end
end

function format_currency_short(amount::Float64)::String
    if amount >= 1_000_000
        return @sprintf("\$%.1fM", amount / 1_000_000)
    elseif amount >= 1_000
        return @sprintf("\$%.0fK", amount / 1_000)
    else
        return @sprintf("\$%.0f", amount)
    end
end

"""
Estimate participant's initial investment based on waterfall recipient name and total investment.
This is a simplified estimation for demo purposes.
"""
function estimate_participant_investment(recipient::String, total_investment::Float64, recipient_totals::Dict{String, Float64})::Float64
    recipient_lower = lowercase(recipient)
    
    # For Private Equity structures
    if contains(recipient_lower, "limited_partners") || contains(recipient_lower, "lp")
        return total_investment * 0.90  # LPs typically provide 90% of capital
    elseif contains(recipient_lower, "general_partner") || contains(recipient_lower, "gp")
        return total_investment * 0.10  # GP typically provides 10% of capital
    
    # For CRE structures  
    elseif contains(recipient_lower, "limited_partner")
        return total_investment * 0.80  # LP in CRE typically 80%
    
    # For Infrastructure/Project Finance
    elseif contains(recipient_lower, "infrastructure_sponsor")
        return total_investment * 0.40  # Sponsor equity
    elseif contains(recipient_lower, "pension_fund")
        return total_investment * 0.60  # Institutional equity
    
    # For debt holders - no initial equity investment
    elseif contains(recipient_lower, "lender") || contains(recipient_lower, "debt")
        return 0.0
    
    # Default: assume equal split if multiple equity participants
    else
        equity_participants = count(k -> !contains(lowercase(k), "lender") && !contains(lowercase(k), "debt"), keys(recipient_totals))
        return equity_participants > 0 ? total_investment / equity_participants : 0.0
    end
end

"""
Calculate simple IRR for a single cash flow (investment → exit).
Using simplified formula: IRR = (Exit Value / Initial Investment)^(1/years) - 1
"""
function calculate_simple_irr(initial_investment::Float64, exit_value::Float64, years::Float64)::Float64
    if initial_investment <= 0 || exit_value <= 0 || years <= 0
        return 0.0
    end
    
    return (exit_value / initial_investment)^(1.0 / years) - 1.0
end

# Run the demo if called directly
if abspath(PROGRAM_FILE) == @__FILE__
    exit(main())
end