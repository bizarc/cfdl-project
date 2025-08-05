# Payback Period

**Category**: Metrics

**Schema File**: `ontology/result/metrics/payback.schema.yaml`

## Description

Schema for the Payback Period metric, defined as the number of periods required to recover the initial investment from cumulative cash flows.


**Schema ID**: `https://cfdl.dev/ontology/result/metrics/payback.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `cashFlowSeriesId` | String | ✅ | Reference to the cash‐flow series (cash‐flow.schema.yaml#entries). |
| `paybackPeriod` | Number | ✅ | Number of periods until cumulative cash flows ≥ initial investment. |
| `periodUnit` | String | ❌ | Unit of the payback period. |
| `paybackDate` | String | ❌ | Date when payback occurs (cumulative cash flows cross zero). |
| `metadata` | Object | ❌ | Free-form metadata (e.g. interpolation method, notes). |


