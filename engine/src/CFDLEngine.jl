module CFDLEngine

using JSON3
using Random
using Dates
using Statistics
using Distributions
using StatsBase

# Include common enums first
include("common_enums.jl")

# Export main functions and types
export MonteCarloEngine, run_monte_carlo, load_ir, sample_variables
export StochasticParameter, FixedParameter, DistributionParameter, RandomWalkParameter
export IRData, TrialResult, MonteCarloResult
export sample_parameter, generate_trial_seed, create_trial_rng, save_results, evolve_random_walk
export TimePeriod, TemporalGrid, generate_temporal_grid, summarize_grid
export parse_date, calculate_period_end, is_business_day, adjust_for_business_day
export calculate_year_fraction, generate_periods, count_business_days
export get_period_by_date, get_periods_in_range
export StreamAllocation, AllocationResult, allocate_streams, summarize_allocation_result
export CashFlowView  # Alias for ReportingFrequency
export HierarchicalCashFlow, AggregatedCashFlow, CashFlowAggregationResult
export aggregate_cash_flows, summarize_aggregation_result
export StreamGroup, HierarchicalStreamGroups, TemporalStreamGroups, GroupedStreams
export collect_streams, summarize_grouped_streams

# Export common enums
export CashFlowCategory, OPERATING, FINANCING, INVESTING, TAX_RELATED
export StatementView, GAAP, NON_GAAP, TAX, MANAGEMENT
export ReportingFrequency, MONTHLY, QUARTERLY, ANNUAL, CUMULATIVE
export EntityType, DEAL, ASSET, COMPONENT, STREAM
export ProcessingStage, STREAM_COLLECTION, CASH_FLOW_ASSEMBLY, OPERATING_STATEMENT_GENERATION, FINANCING_ADJUSTMENTS, TAX_PROCESSING, AVAILABLE_CASH_CALCULATION, STATEMENT_VIEWS
export ValidationSeverity, INFO, WARNING, ERROR, CRITICAL

# Export modular cash flow pipeline functions
export CashFlowEntry, assemble_cash_flows, categorize_cash_flows, summarize_cash_flow_entries, categorize_stream, find_stream_by_id
export OperatingStatement, generate_operating_statements, calculate_noi, calculate_noi_metrics, summarize_operating_statements, is_revenue_item, validate_operating_statement
export FinancingAdjustment, apply_financing_adjustments, calculate_debt_metrics, calculate_dscr_trend, summarize_financing_adjustments, validate_financing_adjustment, is_debt_service_item
export TaxAdjustment, calculate_tax_adjustments, apply_final_tax_adjustments, calculate_tax_metrics, summarize_tax_adjustments, is_depreciation_item, is_amortization_item, get_applicable_tax_rates, calculate_income_tax, calculate_tax_credits, calculate_tax_rate_volatility, validate_tax_adjustment
export AvailableCashCalculation, calculate_available_cash, apply_distribution_policy, calculate_distribution_metrics, calculate_capital_reserves, calculate_cash_flow_volatility, summarize_available_cash, estimate_monthly_operating_expense, validate_available_cash_calculation
export FinancialStatement, generate_statement_views, aggregate_to_annual, summarize_statement_views, generate_gaap_statements, generate_non_gaap_statements, generate_tax_statements, generate_management_statements, calculate_cash_on_cash_return, calculate_distribution_coverage, estimate_taxable_income, estimate_tax_expense, calculate_effective_tax_rate

# Include component modules
include("types.jl")
include("ir_loader.jl")
include("temporal_grid.jl")
include("stream_allocator.jl")
include("stream_collector.jl")

# Include modular cash flow pipeline stages
include("cash_flow_assembly.jl")
include("operating_statement.jl")
include("financing_adjustments.jl")
include("tax_processing.jl")
include("available_cash.jl")
include("statement_views.jl")

# Include main aggregator (uses the modular stages)
include("cash_flow_aggregator.jl")

# Include waterfall distribution library (needed by monte_carlo.jl)
include("waterfall_distributor.jl")

# Include analysis modules
include("monte_carlo.jl")
include("sampling.jl")

# Include metrics library
include("metrics.jl")

# Include stochastic analysis library  
include("stochastic_analysis.jl")

# Export metrics functions
export MetricResult, calculate_npv, calculate_irr, calculate_dscr, calculate_moic, calculate_payback_period, calculate_all_metrics

# Export stochastic analysis functions
export DistributionStats, StochasticMetricsResult, analyze_distribution, calculate_risk_metrics, analyze_stochastic_metrics, generate_distribution_summary, compare_distributions

# Export waterfall distribution functions
export WaterfallDistribution, TierDistribution, RecipientDistribution
export execute_waterfall_distribution, summarize_waterfall_distribution, validate_waterfall_distribution

end # module CFDLEngine