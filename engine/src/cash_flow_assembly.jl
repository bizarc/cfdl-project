# Stage 2: Cash Flow Assembly
# Converts grouped streams into structured cash flows with industry-standard categorization

using Dates

# Note: CashFlowCategory enum is defined in common_enums.jl

"""
    CashFlowEntry

Structured cash flow entry with industry-standard categorization.
"""
struct CashFlowEntry
    entity_id::String
    entity_type::String  # deal, asset, component
    period_start::Date
    period_end::Date
    operating_items::Dict{String, Float64}  # revenue, expenses, etc.
    financing_items::Dict{String, Float64}  # debt service, interest
    investing_items::Dict{String, Float64}  # capex, acquisitions
    tax_items::Dict{String, Float64}       # income tax, property tax
    metadata::Dict{String, Any}
end

"""
    assemble_cash_flows(grouped_streams::GroupedStreams, ir_data::IRData, grid::TemporalGrid) -> Vector{CashFlowEntry}

Convert grouped streams into structured cash flow entries with industry-standard categorization.

This is Stage 2 of the 7-stage cash flow pipeline:
1. Stream Collection ✓
2. **Cash Flow Assembly** ← This stage
3. Operating Statement Generation
4. Financing Adjustments  
5. Tax Processing
6. Available Cash Calculation
7. Statement Views
"""
function assemble_cash_flows(grouped_streams::GroupedStreams, ir_data::IRData, grid::TemporalGrid)::Vector{CashFlowEntry}
    cash_flow_entries = Vector{CashFlowEntry}()
    
    # Process monthly groups from the temporal grouping
    for monthly_group in grouped_streams.temporal.monthly_groups
        if isempty(monthly_group.allocations)
            continue
        end
        
        # Get deal ID as primary entity
        deal_id = get(ir_data.deal, "id", "unknown_deal")
        
        # Determine period boundaries from the group
        period_start = monthly_group.period_start
        period_end = monthly_group.period_end
        
        # Categorize cash flows by industry standards
        operating_items = Dict{String, Float64}()
        financing_items = Dict{String, Float64}()
        investing_items = Dict{String, Float64}()
        tax_items = Dict{String, Float64}()
        
        for allocation in monthly_group.allocations
            stream = find_stream_by_id(ir_data.streams, allocation.stream_id)
            if stream === nothing
                continue
            end
            
            category = categorize_stream(stream)
            stream_name = get(stream, "name", allocation.stream_id)
            amount = allocation.adjusted_amount
            
            if category == OPERATING
                operating_items[stream_name] = get(operating_items, stream_name, 0.0) + amount
            elseif category == FINANCING
                financing_items[stream_name] = get(financing_items, stream_name, 0.0) + amount
            elseif category == INVESTING
                investing_items[stream_name] = get(investing_items, stream_name, 0.0) + amount
                    elseif category == TAX_RELATED
            tax_items[stream_name] = get(tax_items, stream_name, 0.0) + amount
            end
        end
        
        # Create cash flow entry with metadata
        metadata = Dict{String, Any}(
            "group_id" => monthly_group.group_id,
            "allocation_count" => length(monthly_group.allocations),
            "total_amount" => monthly_group.total_amount,
            "stage" => "cash_flow_assembly"
        )
        
        entry = CashFlowEntry(
            deal_id,
            "deal",
            period_start,
            period_end,
            operating_items,
            financing_items,
            investing_items,
            tax_items,
            metadata
        )
        
        push!(cash_flow_entries, entry)
    end
    
    return cash_flow_entries
end

"""
    categorize_cash_flows(entries::Vector{CashFlowEntry}) -> Dict{String, Vector{CashFlowEntry}}

Further categorize cash flow entries by type for downstream processing.
"""
function categorize_cash_flows(entries::Vector{CashFlowEntry})::Dict{String, Vector{CashFlowEntry}}
    categorized = Dict{String, Vector{CashFlowEntry}}()
    
    categorized["operating"] = filter(e -> !isempty(e.operating_items), entries)
    categorized["financing"] = filter(e -> !isempty(e.financing_items), entries)
    categorized["investing"] = filter(e -> !isempty(e.investing_items), entries)
    categorized["tax"] = filter(e -> !isempty(e.tax_items), entries)
    
    return categorized
end

"""
    categorize_stream(stream::Dict{String, Any}) -> CashFlowCategory

Categorize a stream into industry-standard cash flow categories.

Operating: Revenue, operating expenses, NOI-related items
Financing: Debt service, interest, principal payments, preferred returns
Investing: Capital expenditures, acquisitions, dispositions
Tax: Income tax, property tax, tax credits
"""
function categorize_stream(stream::Dict{String, Any})::CashFlowCategory
    stream_category = get(stream, "category", "")
    sub_type = get(stream, "subType", "")
    
    # Operating cash flows - core business operations
    if stream_category in ["Revenue", "Expense"] && sub_type == "Operating"
        return OPERATING
    end
    
    # Financing cash flows - debt and equity financing
    if sub_type in ["Financing", "Debt", "Interest", "Principal", "PreferredReturn"]
        return FINANCING
    end
    
    # Investing cash flows - capital investments
    if sub_type in ["Investing", "CapEx", "Acquisition", "Disposition", "CapitalImprovement"]
        return INVESTING
    end
    
    # Tax cash flows - all tax-related items
    if sub_type in ["Tax", "IncomeTax", "PropertyTax", "TaxCredit"]
        return TAX_RELATED
    end
    
    # Default categorization based on primary category
    if stream_category in ["Revenue", "Expense"]
        return OPERATING
    end
    
    return OPERATING  # Conservative default
end

"""
    summarize_cash_flow_entries(entries::Vector{CashFlowEntry}) -> Dict{String, Any}

Create a summary report of cash flow entries for analysis and debugging.
"""
function summarize_cash_flow_entries(entries::Vector{CashFlowEntry})::Dict{String, Any}
    if isempty(entries)
        return Dict{String, Any}("message" => "No cash flow entries to summarize")
    end
    
    total_operating = sum(sum(values(e.operating_items)) for e in entries)
    total_financing = sum(sum(values(e.financing_items)) for e in entries)
    total_investing = sum(sum(values(e.investing_items)) for e in entries)
    total_tax = sum(sum(values(e.tax_items)) for e in entries)
    
    return Dict{String, Any}(
        "total_entries" => length(entries),
        "period_range" => (minimum(e.period_start for e in entries), maximum(e.period_end for e in entries)),
        "cash_flow_totals" => Dict(
            "operating" => total_operating,
            "financing" => total_financing,
            "investing" => total_investing,
            "tax" => total_tax,
            "net_total" => total_operating + total_financing + total_investing + total_tax
        ),
        "entities_covered" => length(unique(e.entity_id for e in entries)),
        "stage_completed" => "cash_flow_assembly"
    )
end

# Helper functions

"""
    find_stream_by_id(streams::Vector{Dict{String, Any}}, stream_id::String) -> Union{Dict{String, Any}, Nothing}

Find a stream definition by ID in the IR data.
"""
function find_stream_by_id(streams::Vector{Dict{String, Any}}, stream_id::String)::Union{Dict{String, Any}, Nothing}
    for stream in streams
        if get(stream, "id", "") == stream_id
            return stream
        end
    end
    return nothing
end