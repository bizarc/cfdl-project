# Expected Net Present Value (eNPV)

**Category**: Metrics

**Schema File**: `ontology/result/metrics/enpv.schema.yaml`

## Description

Schema for the expected NPV metric derived from stochastic (Monte Carlo) analysis, representing the average NPV across simulations.


**Schema ID**: `https://cfdl.dev/ontology/result/metrics/enpv.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `cashFlowSeriesId` | String | ✅ | Reference to the base cash‐flow series template used in simulations. |
| `discountRate` | Number | ✅ | Per‐period discount rate (decimal). |
| `enpv` | Number | ✅ | Expected (average) net present value across all simulation runs. |
| `simulations` | Integer | ✅ | Number of simulation iterations used to compute eNPV. |
| `asOfDate` | String | ❌ | Date on which the eNPV calculation is anchored. |
| `metadata` | Object | ❌ | Free-form metadata (e.g. simulation seed, notes). |


