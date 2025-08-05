#!/usr/bin/env julia
# Simple CLI demo for CFDL Engine Monte Carlo functionality

using Pkg
Pkg.activate(@__DIR__)
using CFDLEngine
using Random

function main()
    println("🚀 CFDL Engine Monte Carlo Demo")
    println("================================")
    
    # Load sample IR file
    ir_file = joinpath(@__DIR__, "examples", "sample_ir.json")
    println("📁 Loading IR file: $ir_file")
    
    try
        ir_data = load_ir(ir_file)
        println("✅ IR loaded successfully")
        println("   - Deal: $(ir_data.deal["name"])")
        println("   - Currency: $(ir_data.deal["currency"])")
        println("   - Frequency: $(ir_data.calendar["frequency"])")
        println("   - Stochastic parameters: $(length(ir_data.stochastic_params))")
        
        # Display stochastic parameters
        if !isempty(ir_data.stochastic_params)
            println("\n📊 Stochastic Parameters:")
            for (name, param) in ir_data.stochastic_params
                if isa(param, DistributionParameter)
                    println("   - $name: $(param.distribution_type) $(param.parameters)")
                elseif isa(param, RandomWalkParameter)
                    println("   - $name: RandomWalk (initial=$(param.initial_value), drift=$(param.drift), vol=$(param.volatility))")
                else
                    println("   - $name: Fixed ($(param.value))")
                end
            end
        end
        
        # Create Monte Carlo engine
        println("\n🔧 Creating Monte Carlo engine...")
        engine = MonteCarloEngine(ir_data, UInt32(42))  # Fixed seed for reproducibility
        
        # Run deterministic simulation (1 trial)
        println("\n🎯 Running deterministic simulation (1 trial)...")
        deterministic_result = run_monte_carlo(engine, 1)
        
        println("   - Execution time: $(round(deterministic_result.total_execution_time, digits=3))s")
        println("   - Status: $(deterministic_result.successful_trials)/$(deterministic_result.total_trials) successful")
        
        # Display sampled variables for deterministic run
        if !isempty(deterministic_result.trials)
            trial = deterministic_result.trials[1]
            if !isempty(trial.sampled_variables)
                println("   - Sampled variables:")
                for (name, value) in trial.sampled_variables
                    println("     * $name: $(round(value, digits=4))")
                end
            end
        end

        # Show detailed cash flow data for deterministic trial
        println("\n📊 DETAILED CASH FLOW DATA (Deterministic Trial)")
        println("=" ^ 50)
        
        if !isempty(deterministic_result.trials)
            trial = deterministic_result.trials[1]
            rng = MersenneTwister(trial.seed)
            cash_flows = CFDLEngine.execute_cash_flow_pipeline(ir_data, trial.sampled_variables, rng)
            
            println("🏢 Deal: $(cash_flows["deal_id"]) ($(cash_flows["currency"]))")
            println("📅 Frequency: $(cash_flows["frequency"]) | Periods: $(length(cash_flows["entries"]))")
            
            println("\n🕐 TEMPORAL GRID:")
            grid_summary = cash_flows["grid_summary"]
            println("   - Date Range: $(grid_summary["start_date"]) to $(grid_summary["end_date"])")
            println("   - Total Periods: $(grid_summary["total_periods"])")
            println("   - Business Days: $(grid_summary["total_business_days"])")
            println("   - Calendar Days: $(grid_summary["total_calendar_days"])")
            println("   - Year Fraction: $(round(grid_summary["total_year_fraction"], digits=2))")
            
            println("\n💰 STREAM ALLOCATION:")
            alloc_summary = cash_flows["allocation_summary"]
            println("   - Total Allocations: $(alloc_summary["total_allocations"])")
            println("   - Streams Processed: $(alloc_summary["streams"])")
            println("   - Total Amount: \$$(Int(round(alloc_summary["total_amount"])))")
            println("   - Revenue Total: \$$(Int(round(alloc_summary["categories"]["Revenue"])))")
            println("   - Expense Total: \$$(Int(round(alloc_summary["categories"]["Expense"])))")
            
            println("\n📈 FIRST 3 PERIODS DETAIL:")
            for (i, entry) in enumerate(cash_flows["entries"][1:min(3, length(cash_flows["entries"]))])
                println("   Period $i ($(entry["periodStart"]) to $(entry["periodEnd"])):")
                println("     📊 Unlevered CF: \$$(Int(round(entry["unleveredCF"])))")
                println("     📊 Levered CF: \$$(Int(round(entry["leveredCF"])))")
                println("     📋 Line Items:")
                for item in entry["lineItems"]
                    category_icon = item["category"] == "Revenue" ? "💵" : "💸"
                    println("       $category_icon $(item["streamId"]): \$$(Int(round(item["amount"]))) (growth: $(round(item["growthFactor"], digits=3)))")
                end
                println()
            end
            
            println("\n📊 HIERARCHICAL CASH FLOW AGGREGATION")
            println("=" ^ 50)
            
            # Show aggregation summary
            if haskey(cash_flows, "aggregation_summary")
                agg_summary = cash_flows["aggregation_summary"]
                println("🏢 Deal: $(agg_summary["deal_id"]) ($(agg_summary["currency"]))")
                println("📅 Views Available:")
                println("   - Monthly Periods: $(agg_summary["monthly_periods"])")
                println("   - Annual Periods: $(agg_summary["annual_periods"])")
                println("   - Drill-down: $(agg_summary["drill_down_available"] ? "✅ Available" : "❌ Not Available")")
                println("   - Total CF (Monthly): \$$(Int(round(agg_summary["total_unlevered_cf_monthly"])))")
                println("   - Total CF (Annual): \$$(Int(round(agg_summary["total_unlevered_cf_annual"])))")
            end
            
            # Show annual view
            if haskey(cash_flows, "annual_cash_flows") && haskey(cash_flows["annual_cash_flows"], "entries")
                annual_entries = cash_flows["annual_cash_flows"]["entries"]
                println("\n📅 ANNUAL CASH FLOW VIEW:")
                for (i, entry) in enumerate(annual_entries[1:min(3, length(annual_entries))])
                    year_start = entry["periodStart"]
                    year_end = entry["periodEnd"]
                    println("   Year $i ($year_start to $year_end):")
                    println("     📊 Annual Unlevered CF: \$$(Int(round(entry["unleveredCF"])))")
                    println("     📊 Annual Levered CF: \$$(Int(round(entry["leveredCF"])))")
                    println("     📋 Annual Line Items: $(length(entry["lineItems"])) streams")
                    
                    # Show category totals
                    revenue_total = sum(item["amount"] for item in entry["lineItems"] if item["category"] == "Revenue"; init=0.0)
                    expense_total = sum(item["amount"] for item in entry["lineItems"] if item["category"] == "Expense"; init=0.0)
                    println("       💵 Total Revenue: \$$(Int(round(revenue_total)))")
                    println("       💸 Total Expenses: \$$(Int(round(expense_total)))")
                    println()
                end
            end
            
            # Show drill-down capability
            println("\n🔍 DRILL-DOWN CAPABILITY:")
            println("   📊 Deal → Asset → Component → Stream hierarchy available")
            println("   📈 Monthly view: Detailed period-by-period analysis")
            println("   📅 Annual view: Aggregated yearly statements")
            println("   🎯 Line item traceability: Track revenue sources to originating streams")
        end
        
        # Run stochastic simulation (100 trials)
        println("\n🎲 Running stochastic simulation (100 trials)...")
        stochastic_result = run_monte_carlo(engine, 100)
        
        println("   - Execution time: $(round(stochastic_result.total_execution_time, digits=3))s")
        println("   - Status: $(stochastic_result.successful_trials)/$(stochastic_result.total_trials) successful")
        
        # Display parameter statistics
        if haskey(stochastic_result.summary_statistics, "parameters")
            println("   - Parameter statistics:")
            for (param_name, stats) in stochastic_result.summary_statistics["parameters"]
                if haskey(stats, "mean")
                    println("     * $param_name: mean=$(round(stats["mean"], digits=4)), std=$(round(stats["std"], digits=4))")
                end
            end
        end
        
        # Save detailed results
        output_file = joinpath(@__DIR__, "demo_results.json")
        save_results(stochastic_result, output_file)
        println("\n💾 Detailed results saved to: $output_file")
        
        println("\n✨ Demo completed successfully!")
        
    catch e
        println("❌ Error: $e")
        return 1
    end
    
    return 0
end

# Run the demo if called directly
if abspath(PROGRAM_FILE) == @__FILE__
    exit(main())
end