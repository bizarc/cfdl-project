# Internal Rate of Return (IRR)

**Category**: Metrics

**Schema File**: `ontology/result/metrics/irr.schema.yaml`

## Description

Schema for the Internal Rate of Return metric computed over a cash-flow series. IRR is the rate that sets the net present value of the cash flows to zero.


**Schema ID**: `https://cfdl.dev/ontology/result/metrics/irr.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `cashFlowSeriesId` | String | ✅ | Reference to the cash‐flow series (cash‐flow.schema.yaml#entries). |
| `irr` | Number | ✅ | Computed internal rate of return (per period, decimal). |
| `annualized` | Boolean | ❌ | True if the IRR has been annualized; false if period rate. |
| `asOfDate` | String | ❌ | Date on which the IRR calculation is anchored. |
| `metadata` | Object | ❌ | Free-form metadata (e.g. calculation parameters, iteration count). |


