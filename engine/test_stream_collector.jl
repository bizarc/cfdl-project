# Test script for Stream Collector
using Pkg
Pkg.activate(@__DIR__)
using CFDLEngine
using Random
using Dates

function test_stream_collector()
    println("🧪 Testing Stream Collector (Stage 1 of Cash Flow Restructure)")
    println("=" ^ 60)
    
    # Load sample IR data
    ir_file = joinpath(@__DIR__, "examples", "sample_ir.json")
    ir_data = CFDLEngine.load_ir_from_file(ir_file)
    println("✅ Loaded IR data: $(get(ir_data.deal, "id", "unknown"))")
    
    # Generate temporal grid
    sampled_variables = Dict{String, Any}()
    grid = CFDLEngine.generate_temporal_grid(ir_data, sampled_variables)
    println("✅ Generated temporal grid: $(grid.total_periods) periods")
    
    # Allocate streams
    rng = Random.MersenneTwister(42)
    allocation_result = CFDLEngine.allocate_streams(ir_data, grid, sampled_variables, rng)
    println("✅ Allocated streams: $(length(allocation_result.allocations)) allocations")
    
    # Test Stream Collector
    println("\n📦 TESTING STREAM COLLECTOR")
    println("-" ^ 40)
    
    grouped_streams = CFDLEngine.collect_streams(ir_data, allocation_result, grid)
    
    # Display results
    println("📊 Collection Summary:")
    summary = CFDLEngine.summarize_grouped_streams(grouped_streams)
    
    println("   Total Streams: $(summary["total_streams"])")
    println("   Total Amount: \$$(Int(round(summary["total_amount"])))")
    println("   Period Range: $(summary["period_range"]["start"]) to $(summary["period_range"]["end"])")
    
    println("\n🏢 Hierarchical Groups:")
    hier_summary = summary["hierarchical_summary"]
    println("   Deal Groups: $(hier_summary["deal_groups"])")
    println("   Asset Groups: $(hier_summary["asset_groups"])")
    println("   Component Groups: $(hier_summary["component_groups"])")
    
    println("\n📅 Temporal Groups:")
    temp_summary = summary["temporal_summary"]
    println("   Monthly Groups: $(temp_summary["monthly_groups"])")
    println("   Quarterly Groups: $(temp_summary["quarterly_groups"])")
    println("   Annual Groups: $(temp_summary["annual_groups"])")
    
    # Show first few monthly groups in detail
    if !isempty(grouped_streams.temporal.monthly_groups)
        println("\n📋 First 3 Monthly Groups Detail:")
        for (i, group) in enumerate(grouped_streams.temporal.monthly_groups[1:min(3, end)])
            println("   Month $i ($(group.period_start) to $(group.period_end)):")
            println("     Entity: $(group.entity_id) ($(group.entity_type))")
            println("     Total Amount: \$$(Int(round(group.total_amount)))")
            println("     Streams: $(length(group.streams))")
            
            # Show categories
            for (category, amount) in group.categories
                category_icon = category == "Revenue" ? "💵" : "💸"
                println("       $category_icon $category: \$$(Int(round(amount)))")
            end
            println()
        end
    end
    
    # Show annual groups
    if !isempty(grouped_streams.temporal.annual_groups)
        println("📅 Annual Groups Detail:")
        for (i, group) in enumerate(grouped_streams.temporal.annual_groups)
            year = Dates.year(group.period_start)
            println("   Year $year ($(group.period_start) to $(group.period_end)):")
            println("     Total Amount: \$$(Int(round(group.total_amount)))")
            println("     Streams: $(length(group.streams))")
            
            # Show categories
            for (category, amount) in group.categories
                category_icon = category == "Revenue" ? "💵" : "💸"
                println("       $category_icon $category: \$$(Int(round(amount)))")
            end
            println()
        end
    end
    
    println("✅ Stream Collector test completed successfully!")
    return grouped_streams
end

# Run the test
test_stream_collector()