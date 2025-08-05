# engine/src/stream_collector.jl
using Dates

"""
    StreamGroup

Represents a collection of streams grouped by entity and time period.
"""
struct StreamGroup
    entity_id::String
    entity_type::String  # deal, asset, component
    period_start::Date
    period_end::Date
    streams::Vector{Dict{String, Any}}  # Stream allocation entries
    total_amount::Float64
    categories::Dict{String, Float64}  # Revenue, Expense, etc.
end

"""
    HierarchicalStreamGroups

Represents streams organized by entity hierarchy.
"""
struct HierarchicalStreamGroups
    deal_groups::Vector{StreamGroup}
    asset_groups::Vector{StreamGroup}
    component_groups::Vector{StreamGroup}
    metadata::Dict{String, Any}
end

"""
    TemporalStreamGroups

Represents streams organized by time periods.
"""
struct TemporalStreamGroups
    monthly_groups::Vector{StreamGroup}
    quarterly_groups::Vector{StreamGroup}
    annual_groups::Vector{StreamGroup}
    metadata::Dict{String, Any}
end

"""
    GroupedStreams

Complete collection of streams grouped by both hierarchy and time.
"""
struct GroupedStreams
    hierarchical::HierarchicalStreamGroups
    temporal::TemporalStreamGroups
    total_streams::Int
    total_amount::Float64
    period_range::Tuple{Date, Date}
end

"""
    collect_streams(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid) -> GroupedStreams

Collect and group allocated streams by entity hierarchy and time periods.
This is Stage 1 of the cash flow restructure plan.
"""
function collect_streams(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid)::GroupedStreams
    @debug "Starting stream collection with $(length(allocation_result.allocations)) allocations"
    
    # Group by entity hierarchy
    hierarchical_groups = group_by_entity_hierarchy(ir_data, allocation_result, grid)
    
    # Group by time periods
    temporal_groups = group_by_time_periods(allocation_result, grid)
    
    # Calculate totals
    total_streams = length(allocation_result.allocations)
    total_amount = isempty(allocation_result.allocations) ? 0.0 : sum(alloc.adjusted_amount for alloc in allocation_result.allocations)
    
    # Determine period range
    if !isempty(grid.periods)
        period_range = (grid.periods[1].period_start, grid.periods[end].period_end)
    else
        period_range = (Date(1900, 1, 1), Date(1900, 1, 1))
    end
    
    return GroupedStreams(
        hierarchical_groups,
        temporal_groups, 
        total_streams,
        total_amount,
        period_range
    )
end

"""
    group_by_entity_hierarchy(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid) -> HierarchicalStreamGroups

Group streams by Deal → Asset → Component hierarchy.
"""
function group_by_entity_hierarchy(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid)::HierarchicalStreamGroups
    # For now, we'll work primarily at the deal level since that's our starting point
    # Future enhancement: Support multi-asset, multi-component grouping
    
    deal_groups = group_streams_by_entity(ir_data, allocation_result, grid, "deal")
    asset_groups = group_streams_by_entity(ir_data, allocation_result, grid, "asset") 
    component_groups = group_streams_by_entity(ir_data, allocation_result, grid, "component")
    
    metadata = Dict{String, Any}(
        "deal_count" => length(deal_groups),
        "asset_count" => length(asset_groups),
        "component_count" => length(component_groups),
        "hierarchy_levels" => ["deal", "asset", "component"]
    )
    
    return HierarchicalStreamGroups(
        deal_groups,
        asset_groups,
        component_groups,
        metadata
    )
end

"""
    group_streams_by_entity(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid, entity_type::String) -> Vector{StreamGroup}

Group streams by a specific entity type (deal, asset, component).
"""
function group_streams_by_entity(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid, entity_type::String)::Vector{StreamGroup}
    groups = StreamGroup[]
    
    # Get entity ID based on type
    entity_id = if entity_type == "deal"
        get(ir_data.deal, "id", "unknown_deal")
    elseif entity_type == "asset" && !isempty(ir_data.assets)
        get(ir_data.assets[1], "id", "unknown_asset")  # For now, take first asset
    elseif entity_type == "component" && !isempty(ir_data.components)
        get(ir_data.components[1], "id", "unknown_component")  # For now, take first component
    else
        "unknown_$(entity_type)"
    end
    
    # Group allocations by period for this entity
    period_groups = Dict{Int, Vector{StreamAllocation}}()
    for allocation in allocation_result.allocations
        period_num = allocation.period_number
        if !haskey(period_groups, period_num)
            period_groups[period_num] = StreamAllocation[]
        end
        push!(period_groups[period_num], allocation)
    end
    
    # Create StreamGroup for each period
    for (period_num, period_allocations) in period_groups
        # Find corresponding period in grid
        period = findfirst(p -> p.period_number == period_num, grid.periods)
        if period === nothing
            @warn "Could not find period $period_num in temporal grid"
            continue
        end
        
        grid_period = grid.periods[period]
        
        # Convert allocations to stream dictionaries
        streams = [Dict{String, Any}(
            "stream_id" => alloc.stream_id,
            "amount" => alloc.adjusted_amount,
            "growth_factor" => alloc.growth_factor,
            "allocation_method" => alloc.allocation_method,
            "metadata" => alloc.metadata
        ) for alloc in period_allocations]
        
        # Calculate totals and categories
        total_amount = sum(alloc.adjusted_amount for alloc in period_allocations)
        categories = Dict{String, Float64}()
        
        for allocation in period_allocations
            category = get(allocation.metadata, "category", "Unknown")
            categories[category] = get(categories, category, 0.0) + allocation.adjusted_amount
        end
        
        push!(groups, StreamGroup(
            entity_id,
            entity_type,
            grid_period.period_start,
            grid_period.period_end,
            streams,
            total_amount,
            categories
        ))
    end
    
    return groups
end

"""
    group_by_time_periods(allocation_result::AllocationResult, grid::TemporalGrid) -> TemporalStreamGroups

Group streams by time periods (monthly, quarterly, annual).
"""
function group_by_time_periods(allocation_result::AllocationResult, grid::TemporalGrid)::TemporalStreamGroups
    # For now, we'll create monthly groups based on the existing grid
    # Future enhancement: Support quarterly and annual groupings
    
    monthly_groups = create_monthly_groups(allocation_result, grid)
    quarterly_groups = StreamGroup[]  # Placeholder for future implementation
    annual_groups = create_annual_groups(allocation_result, grid)
    
    metadata = Dict{String, Any}(
        "monthly_periods" => length(monthly_groups),
        "quarterly_periods" => length(quarterly_groups),
        "annual_periods" => length(annual_groups),
        "base_frequency" => grid.frequency
    )
    
    return TemporalStreamGroups(
        monthly_groups,
        quarterly_groups,
        annual_groups,
        metadata
    )
end

"""
    create_monthly_groups(allocation_result::AllocationResult, grid::TemporalGrid) -> Vector{StreamGroup}

Create monthly stream groups from allocations.
"""
function create_monthly_groups(allocation_result::AllocationResult, grid::TemporalGrid)::Vector{StreamGroup}
    groups = StreamGroup[]
    
    # Group allocations by period
    period_groups = Dict{Int, Vector{StreamAllocation}}()
    for allocation in allocation_result.allocations
        period_num = allocation.period_number
        if !haskey(period_groups, period_num)
            period_groups[period_num] = StreamAllocation[]
        end
        push!(period_groups[period_num], allocation)
    end
    
    # Create monthly groups
    for (period_num, period_allocations) in sort(collect(period_groups))
        # Find corresponding period in grid
        period_idx = findfirst(p -> p.period_number == period_num, grid.periods)
        if period_idx === nothing
            @warn "Could not find period $period_num in temporal grid"
            continue
        end
        
        grid_period = grid.periods[period_idx]
        
        # Convert allocations to stream dictionaries
        streams = [Dict{String, Any}(
            "stream_id" => alloc.stream_id,
            "amount" => alloc.adjusted_amount,
            "growth_factor" => alloc.growth_factor,
            "allocation_method" => alloc.allocation_method,
            "category" => get(alloc.metadata, "category", "Unknown"),
            "metadata" => alloc.metadata
        ) for alloc in period_allocations]
        
        # Calculate totals and categories
        total_amount = sum(alloc.adjusted_amount for alloc in period_allocations)
        categories = Dict{String, Float64}()
        
        for allocation in period_allocations
            category = get(allocation.metadata, "category", "Unknown")
            categories[category] = get(categories, category, 0.0) + allocation.adjusted_amount
        end
        
        push!(groups, StreamGroup(
            "monthly_group_$period_num",
            "monthly",
            grid_period.period_start,
            grid_period.period_end,
            streams,
            total_amount,
            categories
        ))
    end
    
    return groups
end

"""
    create_annual_groups(allocation_result::AllocationResult, grid::TemporalGrid) -> Vector{StreamGroup}

Create annual stream groups by aggregating monthly allocations.
"""
function create_annual_groups(allocation_result::AllocationResult, grid::TemporalGrid)::Vector{StreamGroup}
    if isempty(grid.periods)
        return StreamGroup[]
    end
    
    groups = StreamGroup[]
    
    # Group allocations by year
    year_groups = Dict{Int, Vector{StreamAllocation}}()
    for allocation in allocation_result.allocations
        # Find the corresponding period to get the year
        period_idx = findfirst(p -> p.period_number == allocation.period_number, grid.periods)
        if period_idx === nothing
            continue
        end
        
        year = Dates.year(grid.periods[period_idx].period_start)
        if !haskey(year_groups, year)
            year_groups[year] = StreamAllocation[]
        end
        push!(year_groups[year], allocation)
    end
    
    # Create annual groups
    for (year, year_allocations) in sort(collect(year_groups))
        # Find year boundaries
        year_periods = filter(p -> Dates.year(p.period_start) == year, grid.periods)
        if isempty(year_periods)
            continue
        end
        
        year_start = minimum(p.period_start for p in year_periods)
        year_end = maximum(p.period_end for p in year_periods)
        
        # Aggregate streams by stream_id within the year
        stream_aggregates = Dict{String, Dict{String, Any}}()
        
        for allocation in year_allocations
            stream_id = allocation.stream_id
            if !haskey(stream_aggregates, stream_id)
                stream_aggregates[stream_id] = Dict{String, Any}(
                    "stream_id" => stream_id,
                    "amount" => 0.0,
                    "allocation_count" => 0,
                    "category" => get(allocation.metadata, "category", "Unknown"),
                    "metadata" => allocation.metadata
                )
            end
            
            stream_aggregates[stream_id]["amount"] += allocation.adjusted_amount
            stream_aggregates[stream_id]["allocation_count"] += 1
        end
        
        # Convert to streams array
        streams = collect(values(stream_aggregates))
        
        # Calculate totals and categories
        total_amount = sum(s["amount"] for s in streams)
        categories = Dict{String, Float64}()
        
        for stream in streams
            category = stream["category"]
            categories[category] = get(categories, category, 0.0) + stream["amount"]
        end
        
        push!(groups, StreamGroup(
            "annual_group_$year",
            "annual",
            year_start,
            year_end,
            streams,
            total_amount,
            categories
        ))
    end
    
    return groups
end

"""
    summarize_grouped_streams(grouped_streams::GroupedStreams) -> Dict{String, Any}

Create a summary of the grouped streams for reporting and debugging.
"""
function summarize_grouped_streams(grouped_streams::GroupedStreams)::Dict{String, Any}
    return Dict{String, Any}(
        "total_streams" => grouped_streams.total_streams,
        "total_amount" => grouped_streams.total_amount,
        "period_range" => Dict(
            "start" => string(grouped_streams.period_range[1]),
            "end" => string(grouped_streams.period_range[2])
        ),
        "hierarchical_summary" => Dict(
            "deal_groups" => length(grouped_streams.hierarchical.deal_groups),
            "asset_groups" => length(grouped_streams.hierarchical.asset_groups),
            "component_groups" => length(grouped_streams.hierarchical.component_groups)
        ),
        "temporal_summary" => Dict(
            "monthly_groups" => length(grouped_streams.temporal.monthly_groups),
            "quarterly_groups" => length(grouped_streams.temporal.quarterly_groups),
            "annual_groups" => length(grouped_streams.temporal.annual_groups)
        )
    )
end