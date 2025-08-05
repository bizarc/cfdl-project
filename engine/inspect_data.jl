#!/usr/bin/env julia

using Pkg
Pkg.activate(".")

using CFDLEngine
using JSON3

println("🔍 CFDL Engine Data Flow Inspection")
println("=" ^ 50)

# Load the IR
println("\n📁 Loading IR...")
ir_data = load_ir("examples/sample_ir.json")
println("✅ IR loaded: $(ir_data.deal["name"])")

# Create a deterministic RNG
using Random
rng = MersenneTwister(12345)

# Sample variables (deterministic)
sampled_variables = Dict{String, Any}(
    "rent_growth_rate" => 0.025,
    "vacancy_rate" => 0.05,
    "inflation_rate" => 0.025,
    "interest_rate" => 0.045
)

println("\n🔧 Executing pipeline components...")

# 1. Generate temporal grid
println("\n1️⃣ TEMPORAL GRID GENERATION")
println("-" ^ 30)
grid = generate_temporal_grid(ir_data, sampled_variables)
println("Frequency: $(grid.frequency)")
println("Total Periods: $(grid.total_periods)")
println("Date Range: $(grid.start_date) to $(grid.end_date)")
println("\nFirst 3 periods:")
for (i, period) in enumerate(grid.periods[1:min(3, length(grid.periods))])
    println("  Period $i: $(period.period_start) to $(period.period_end) ($(period.business_days) business days)")
end

# 2. Allocate streams
println("\n2️⃣ STREAM ALLOCATION")
println("-" ^ 30)
allocation_result = allocate_streams(ir_data, grid, sampled_variables, rng)
println("Total Allocations: $(length(allocation_result.allocations))")
println("Logic Blocks Executed: $(length(allocation_result.logic_block_executions))")

println("\nFirst 5 allocations:")
for (i, alloc) in enumerate(allocation_result.allocations[1:min(5, length(allocation_result.allocations))])
    println("  $i. $(alloc.stream_id) - Period $(alloc.period_number): \$$(round(alloc.adjusted_amount, digits=2)) (growth: $(round(alloc.growth_factor, digits=3)))")
end

# 3. Generate complete cash flow result
println("\n3️⃣ COMPLETE CASH FLOW PIPELINE")
println("-" ^ 30)
cash_flows = CFDLEngine.execute_cash_flow_pipeline(ir_data, sampled_variables, rng)
println("Deal ID: $(cash_flows["deal_id"])")
println("Currency: $(cash_flows["currency"])")
println("Frequency: $(cash_flows["frequency"])")
println("Total Periods: $(length(cash_flows["entries"]))")

println("\nFirst 3 cash flow periods:")
for (i, entry) in enumerate(cash_flows["entries"][1:min(3, length(cash_flows["entries"]))])
    println("  Period $i ($(entry["periodStart"]) to $(entry["periodEnd"])):")
    println("    Unlevered CF: \$$(entry["unleveredCF"])")
    println("    Levered CF: \$$(entry["leveredCF"])")
    println("    Line Items: $(length(entry["lineItems"]))")
    for item in entry["lineItems"]
        println("      - $(item["streamId"]) ($(item["category"])): \$$(round(item["amount"], digits=2))")
    end
end

# 4. Show summaries
println("\n4️⃣ SUMMARY DATA")
println("-" ^ 30)
println("Grid Summary:")
grid_summary = cash_flows["grid_summary"]
for (key, value) in grid_summary
    println("  $key: $value")
end

println("\nAllocation Summary:")
alloc_summary = cash_flows["allocation_summary"]
for (key, value) in alloc_summary
    println("  $key: $value")
end

println("\n✨ Data flow inspection complete!")