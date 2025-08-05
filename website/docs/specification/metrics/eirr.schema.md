# Expected Internal Rate of Return (eIRR)

**Category**: Metrics

**Schema File**: `ontology/result/metrics/eirr.schema.yaml`

## Description

Schema for the expected IRR metric derived from stochastic (Monte Carlo) analysis, representing the average IRR across simulations.


**Schema ID**: `https://cfdl.dev/ontology/result/metrics/eirr.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `cashFlowSeriesId` | String | ✅ | Reference to the base cash‐flow series template used in simulations. |
| `eirr` | Number | ✅ | Expected (average) internal rate of return per period (decimal). |
| `annualized` | Boolean | ❌ | True if the eIRR has been annualized; false if per‐period rate. |
| `simulations` | Integer | ✅ | Number of simulation iterations used to compute eIRR. |
| `asOfDate` | String | ❌ | Date on which the eIRR calculation is anchored. |
| `metadata` | Object | ❌ | Free-form metadata (e.g. simulation seed, notes). |


