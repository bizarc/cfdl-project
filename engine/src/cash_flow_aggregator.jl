# Cash flow aggregation following industry best practices
# Implements 7-stage pipeline: Stream Collection → Cash Flow Assembly → Operating Statement → 
# Financing Adjustments → Tax Processing → Available Cash → Statement Views

using Dates

# Note: CashFlowView functionality is now handled by ReportingFrequency enum in common_enums.jl
# Alias for backward compatibility
const CashFlowView = ReportingFrequency

# Note: CashFlowCategory enum is defined in common_enums.jl

# GroupedStreams is defined in stream_collector.jl

# Note: CashFlowEntry struct is defined in cash_flow_assembly.jl

# Note: OperatingStatement struct is defined in operating_statement.jl

# Note: FinancingAdjustment struct is defined in financing_adjustments.jl

# ===== STAGE 1: STREAM COLLECTION & GROUPING =====
# Uses existing collect_streams function from stream_collector.jl

# ===== STAGE 2: CASH FLOW ASSEMBLY =====
# Now handled by cash_flow_assembly.jl module

# ===== STAGE 3: OPERATING STATEMENT GENERATION =====
# Now handled by operating_statement.jl module

# ===== STAGE 4: FINANCING ADJUSTMENTS =====
# Now handled by financing_adjustments.jl module

# ===== STAGE 5: TAX PROCESSING =====
# Now handled by tax_processing.jl module

# ===== STAGE 6: AVAILABLE CASH CALCULATION =====
# Now handled by available_cash.jl module

# ===== STAGE 7: STATEMENT VIEW GENERATOR =====

# Note: StatementViewGenerator functionality moved to statement_views.jl

"""
HierarchicalCashFlow

Represents cash flows with hierarchical drill-down capability.
"""
struct HierarchicalCashFlow
    deal_id::String
    asset_id::String
    component_id::String
    stream_id::String
    period_start::Date
    period_end::Date
    amount::Float64
    category::String
    sub_type::String
    metadata::Dict{String, Any}
end

"""
AggregatedCashFlow

Represents aggregated cash flows for a specific view and period.
"""
struct AggregatedCashFlow
    period_start::Date
    period_end::Date
    unlevered_cf::Float64
    levered_cf::Float64
    line_items::Vector{Dict{String, Any}}
    drill_down_data::Vector{HierarchicalCashFlow}
    metadata::Dict{String, Any}
end

"""
CashFlowAggregationResult

Complete result of cash flow aggregation with multiple views.
"""
struct CashFlowAggregationResult
    deal_id::String
    currency::String
    monthly_view::Vector{AggregatedCashFlow}
    annual_view::Vector{AggregatedCashFlow}
    hierarchy_map::Dict{String, Vector{String}}  # entity_id => child_entity_ids
    metadata::Dict{String, Any}
end

"""
    aggregate_cash_flows(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid) -> CashFlowAggregationResult

Aggregate stream allocations using industry-standard 7-stage pipeline.
"""
function aggregate_cash_flows(ir_data::IRData, allocation_result::AllocationResult, grid::TemporalGrid)::CashFlowAggregationResult
    deal_id = get(ir_data.deal, "id", "unknown_deal")
    currency = get(ir_data.deal, "currency", "USD")
    
    # ===== STAGE 1: STREAM COLLECTION & GROUPING =====
    grouped_streams = collect_streams(ir_data, allocation_result, grid)
    
    # ===== STAGE 2: CASH FLOW ASSEMBLY =====
    # Uses cash_flow_assembly.jl module
    cash_flow_entries = assemble_cash_flows(grouped_streams, ir_data, grid)
    categorized_entries = categorize_cash_flows(cash_flow_entries)
    
    # ===== STAGE 3: OPERATING STATEMENT GENERATION =====
    # Uses operating_statement.jl module
    operating_statements = generate_operating_statements(cash_flow_entries)
    
    # ===== STAGE 4: FINANCING ADJUSTMENTS =====
    # Uses financing_adjustments.jl module
    financing_adjustments = apply_financing_adjustments(operating_statements, cash_flow_entries)
    debt_metrics = calculate_debt_metrics(financing_adjustments)
    
    # ===== STAGE 5: TAX PROCESSING =====
    # Uses tax_processing.jl module
    tax_adjustments = calculate_tax_adjustments(financing_adjustments, cash_flow_entries)
    final_tax_adjustments = apply_final_tax_adjustments(tax_adjustments)
    
    # ===== STAGE 6: AVAILABLE CASH CALCULATION =====
    # Uses available_cash.jl module
    available_cash_calculations = calculate_available_cash(final_tax_adjustments)
    final_cash_calculations = apply_distribution_policy(available_cash_calculations)
    
    # ===== STAGE 7: STATEMENT VIEWS =====
    # Uses statement_views.jl module
    statement_views = generate_statement_views(final_cash_calculations)
    
    # Extract Non-GAAP monthly and annual views for backward compatibility
    monthly_statements = get(statement_views, "non_gaap", FinancialStatement[])
    annual_statements = aggregate_to_annual(monthly_statements)
    
    # Convert statements to aggregated cash flow views
    monthly_view = convert_statements_to_aggregated_flows(monthly_statements, "monthly")
    annual_view = convert_statements_to_aggregated_flows(annual_statements, "annual")
    
    # Build hierarchy map for drill-down capability
    hierarchy_map = build_hierarchy_map(ir_data)
    
    return CashFlowAggregationResult(
        deal_id,
        currency,
        monthly_view,
        annual_view,
        hierarchy_map,
        Dict{String, Any}(
            "aggregation_timestamp" => now(),
            "pipeline_version" => "7-stage-modular-industry-standard",
            "pipeline_stages" => [
                "stream_collection", "cash_flow_assembly", "operating_statement_generation",
                "financing_adjustments", "tax_processing", "available_cash_calculation", "statement_views"
            ],
            "total_periods_monthly" => length(monthly_view),
            "total_periods_annual" => length(annual_view),
            "stage_counts" => Dict(
                "cash_flow_entries" => length(cash_flow_entries),
                "operating_statements" => length(operating_statements),
                "financing_adjustments" => length(financing_adjustments),
                "tax_adjustments" => length(final_tax_adjustments),
                "available_cash_calculations" => length(final_cash_calculations)
            ),
            "debt_metrics" => debt_metrics,
            "statement_views_available" => keys(statement_views),
            "modular_architecture" => true
        )
    )
end

"""
    convert_statements_to_aggregated_flows(statements::Vector{FinancialStatement}, frequency::String) -> Vector{AggregatedCashFlow}

Convert FinancialStatement objects to AggregatedCashFlow objects for the aggregation result.
"""
function convert_statements_to_aggregated_flows(statements::Vector{FinancialStatement}, frequency::String)::Vector{AggregatedCashFlow}
    legacy_flows = Vector{AggregatedCashFlow}()
    
    for stmt in statements
        # Extract period information
        period_start = stmt.period_start
        period_end = stmt.period_end
        
        # Calculate unlevered and levered cash flows
        unlevered_cf = stmt.operating_cash_flow
        levered_cf = stmt.net_cash_flow
        
        # Create line items from the statement
        line_items = [Dict{String, Any}(
            "streamId" => "aggregated_$(frequency)",
            "name" => "$(frequency)_cash_flow",
            "amount" => unlevered_cf,
            "category" => "Operating",
            "subType" => "Aggregated"
        )]
        
        # Create dummy hierarchical flows for drill-down
        drill_down_data = [HierarchicalCashFlow(
            stmt.entity_id,
            "asset_001",
            "component_001", 
            "stream_001",
            period_start,
            period_end,
            unlevered_cf,
            "Operating",
            "Aggregated",
            Dict{String, Any}("converted_from_new_format" => true)
        )]
        
        # Create metadata
        metadata = Dict{String, Any}(
            "view_type" => string(stmt.view_type),
            "frequency" => frequency,
            "converted_from_modular_pipeline" => true,
            "original_statement_type" => string(stmt.view_type)
        )
        
        legacy_flow = AggregatedCashFlow(
            period_start,
            period_end,
            unlevered_cf,
            levered_cf,
            line_items,
            drill_down_data,
            metadata
        )
        
        push!(legacy_flows, legacy_flow)
    end
    
    return legacy_flows
end

# Unused functions removed - functionality moved to dedicated modules

"""
    summarize_aggregation_result(aggregation_result::CashFlowAggregationResult) -> Dict{String, Any}

Create a summary of cash flow aggregation results for reporting.
"""
function summarize_aggregation_result(aggregation_result::CashFlowAggregationResult)::Dict{String, Any}
    monthly_count = length(aggregation_result.monthly_view)
    annual_count = length(aggregation_result.annual_view)
    
    # Calculate totals from monthly view (handle empty collections)
    total_unlevered = isempty(aggregation_result.monthly_view) ? 0.0 : sum(entry.unlevered_cf for entry in aggregation_result.monthly_view)
    total_levered = isempty(aggregation_result.monthly_view) ? 0.0 : sum(entry.levered_cf for entry in aggregation_result.monthly_view)
    
    return Dict{String, Any}(
        "monthly_periods" => monthly_count,
        "annual_periods" => annual_count, 
        "total_unlevered_cf" => round(total_unlevered, digits=2),
        "total_levered_cf" => round(total_levered, digits=2),
        "leverage_ratio" => total_unlevered != 0 ? round(total_levered / total_unlevered, digits=3) : 0.0,
        "average_monthly_unlevered" => monthly_count > 0 ? round(total_unlevered / monthly_count, digits=2) : 0.0,
        "average_monthly_levered" => monthly_count > 0 ? round(total_levered / monthly_count, digits=2) : 0.0
    )
end



"""
    build_hierarchy_map(ir_data::IRData) -> Dict{String, Vector{String}}

Build entity hierarchy mapping for drill-down capability.
"""
function build_hierarchy_map(ir_data::IRData)::Dict{String, Vector{String}}
    hierarchy_map = Dict{String, Vector{String}}()
    
    deal_id = get(ir_data.deal, "id", "unknown_deal")
    
    # Deal -> Assets
    asset_ids = [get(asset, "id", "unknown_asset_$(i)") for (i, asset) in enumerate(ir_data.assets)]
    hierarchy_map[deal_id] = asset_ids
    
    # Assets -> Components (simplified - assumes each asset has all components)
    for asset_id in asset_ids
        component_ids = [get(comp, "id", "unknown_component_$(i)") for (i, comp) in enumerate(ir_data.components)]
        hierarchy_map[asset_id] = component_ids
        
        # Components -> Streams (simplified - assumes each component has all streams)
        for component_id in component_ids
            stream_ids = [get(stream, "id", "unknown_stream_$(i)") for (i, stream) in enumerate(ir_data.streams)]
            hierarchy_map[component_id] = stream_ids
        end
    end
    
    return hierarchy_map
end

# Utility functions for result formatting and summarization moved to dedicated modules

# Helper functions moved to dedicated modules or removed as unused