# Debt Service Coverage Ratio (DSCR)

**Category**: Metrics

**Schema File**: `ontology/result/metrics/dscr.schema.yaml`

## Description

Schema for the Debt Service Coverage Ratio metric, defined as the ratio of net operating income to debt service for a cash‐flow series.


**Schema ID**: `https://cfdl.dev/ontology/result/metrics/dscr.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `cashFlowSeriesId` | String | ✅ | Reference to the cash‐flow series (cash‐flow.schema.yaml#entries). |
| `debtServiceSeriesId` | String | ✅ | Reference to the debt‐service cash‐flow series. |
| `dscr` | Number | ✅ | Computed Debt Service Coverage Ratio (NOI ÷ Debt Service). |
| `asOfDate` | String | ❌ | Date on which the DSCR calculation is anchored. |
| `metadata` | Object | ❌ | Free-form metadata (e.g. calculation parameters, notes). |


