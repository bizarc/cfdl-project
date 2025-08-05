# Cash Flow Aggregation Restructure Plan

## Overview
Restructure the cash flow aggregation to follow industry best practices for financial modeling, cash flow statements, and valuation methodologies.

## Current vs. Proposed Architecture

### Current (Incorrect)
```
Stream Allocation → View Generation
```

### Proposed (Industry Standard)
```
1. Stream Collection
2. Cash Flow Assembly (by entity/period)
3. Operating Statement Generation
4. Financing Adjustments
5. Tax Processing
6. Available Cash Calculation
7. Statement Views (GAAP/Non-GAAP, Monthly/Annual)
```

## Detailed Architecture

### Stage 1: Stream Collection & Grouping
**Purpose**: Collect allocated streams and group by entity hierarchy and time periods

**Input**: Stream allocations from Stream Allocator
**Process**:
- Group streams by: Deal → Asset → Component → Stream
- Group by time period: Monthly, Quarterly, Annual
- Categorize by cash flow type: Operating, Financing, Investing, Tax

**Output**: Hierarchical stream collections

### Stage 2: Cash Flow Assembly
**Purpose**: Convert grouped streams into structured cash flows

**Process**:
```julia
struct CashFlowEntry
    entity_id::String
    entity_type::String  # deal, asset, component
    period_start::Date
    period_end::Date
    operating_items::Dict{String, Float64}  # revenue, expenses, etc.
    financing_items::Dict{String, Float64}  # debt service, interest
    investing_items::Dict{String, Float64}  # capex, acquisitions
    tax_items::Dict{String, Float64}       # income tax, property tax
end
```

### Stage 3: Operating Statement Generation
**Purpose**: Create standardized operating cash flow statements

**Operating Cash Flow Categories**:
```
REVENUE ITEMS:
- Rental Income
- Other Operating Income
- Management Fees (if applicable)

OPERATING EXPENSES:
- Property Management
- Maintenance & Repairs
- Utilities
- Insurance
- Property Taxes
- General & Administrative

= NET OPERATING INCOME (NOI)
```

### Stage 4: Financing Adjustments
**Purpose**: Apply leverage and financing costs

**Financing Categories**:
```
DEBT SERVICE:
- Principal Payments
- Interest Expense
- Loan Fees

OTHER FINANCING:
- Preferred Returns
- Financing Costs

= UNLEVERED vs LEVERED CASH FLOW
```

### Stage 5: Tax Processing
**Purpose**: Apply tax adjustments and calculations

**Tax Categories**:
```
TAX ADJUSTMENTS:
- Depreciation (non-cash)
- Amortization (non-cash)
- Taxable Income Calculation
- Income Tax Expense
- Tax Credits

= AFTER-TAX CASH FLOW
```

### Stage 6: Available Cash Calculation
**Purpose**: Calculate distributable cash flow

```
Operating Income
- Debt Service
- Tax Expense
- Capital Reserves
- Working Capital Changes
= AVAILABLE FOR DISTRIBUTION
```

### Stage 7: Statement Views
**Purpose**: Generate different reporting views

**View Types**:
- **GAAP**: Generally Accepted Accounting Principles
- **Non-GAAP**: Operating/Economic view (exclude non-cash items)
- **Tax**: Tax reporting view
- **Management**: Internal management reporting

**Time Periods**:
- Monthly: Detailed period analysis
- Quarterly: Quarterly reporting
- Annual: Yearly statements
- Cumulative: Life-to-date

## Implementation Structure

### Core Modules

```julia
# 1. Stream Collector
module StreamCollector
    collect_streams(allocations) -> GroupedStreams
    group_by_entity(streams) -> HierarchicalGroups
    group_by_period(streams) -> TemporalGroups
end

# 2. Cash Flow Assembler  
module CashFlowAssembler
    assemble_cash_flows(grouped_streams) -> CashFlowEntries
    categorize_cash_flows(entries) -> CategorizedCashFlows
end

# 3. Operating Statement Generator
module OperatingStatementGenerator
    generate_operating_statement(cash_flows) -> OperatingStatement
    calculate_noi(operating_items) -> NetOperatingIncome
end

# 4. Financing Processor
module FinancingProcessor
    apply_debt_service(cash_flows, debt_terms) -> LeveredCashFlows
    calculate_debt_metrics(cash_flows) -> DebtMetrics
end

# 5. Tax Processor
module TaxProcessor
    calculate_taxable_income(cash_flows) -> TaxableIncome
    apply_tax_adjustments(cash_flows) -> AfterTaxCashFlows
end

# 6. Available Cash Calculator
module AvailableCashCalculator
    calculate_available_cash(cash_flows) -> AvailableCash
    apply_reserves(cash_flows) -> AdjustedCashFlows
end

# 7. Statement View Generator
module StatementViewGenerator
    generate_gaap_view(cash_flows) -> GAAPStatements
    generate_nongaap_view(cash_flows) -> NonGAAPStatements
    generate_monthly_view(cash_flows) -> MonthlyStatements
    generate_annual_view(cash_flows) -> AnnualStatements
end
```

## Industry Alignment

### Real Estate Best Practices
- **NOI Calculation**: Standard real estate metric
- **Debt Service Coverage**: Critical for lending
- **Cash-on-Cash Returns**: Key investor metric
- **Levered vs Unlevered**: Standard comparison

### Financial Modeling Standards
- **Operating vs Financing vs Investing**: Cash flow statement categories
- **GAAP Compliance**: Required for institutional investors
- **Tax Considerations**: Essential for accurate valuation

### Valuation Methodologies
- **Unlevered DCF**: Uses unlevered cash flows
- **Levered DCF**: Uses levered cash flows
- **Multiple Scenarios**: Different leverage assumptions

## Schema Alignment

Ensure output conforms to:
- `ontology/result/cash-flow.schema.yaml`
- Industry standard cash flow statement formats
- Investment committee reporting requirements

## Future Extensions

### Waterfall Processing (Later Task)
```
Available Cash → Waterfall Engine → Participant Distributions
```

### Portfolio Aggregation (Later Task)
```
Deal Cash Flows → Portfolio Aggregator → Fund-Level Statements
```

### GAAP vs Non-GAAP Views (This Task)
```
Base Cash Flows → View Processors → Multiple Reporting Views
```

## Implementation Priority

1. **Stream Collector** - Group streams properly
2. **Cash Flow Assembler** - Create structured cash flows
3. **Operating Statement Generator** - Standard NOI calculation
4. **Financing Processor** - Debt service integration
5. **Available Cash Calculator** - Distributable cash flow
6. **Statement Views** - Multiple reporting formats

This structure provides the foundation for accurate NPV/IRR calculations and industry-standard reporting.