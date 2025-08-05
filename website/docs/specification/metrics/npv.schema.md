# Net Present Value (NPV)

**Category**: Metrics

**Schema File**: `ontology/result/metrics/npv.schema.yaml`

## Description

Schema for the Net Present Value metric computed over a cash-flow series. NPV is the sum of each period’s cash flow discounted by the discount rate.


**Schema ID**: `https://cfdl.dev/ontology/result/metrics/npv.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `cashFlowSeriesId` | String | ✅ | Reference to the cash‐flow series (cash‐flow.schema.yaml#entries). |
| `discountRate` | Number | ✅ | Per‐period discount rate (decimal, e.g. 0.08 for 8%). |
| `npv` | Number | ✅ | Computed net present value of the cash‐flow series. |
| `asOfDate` | String | ❌ | Date on which the NPV calculation is anchored. |
| `metadata` | Object | ❌ | Free-form metadata (e.g. calculation parameters, notes). |


