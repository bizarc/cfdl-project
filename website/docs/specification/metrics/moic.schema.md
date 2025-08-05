# Multiple on Invested Capital (MOIC)

**Category**: Metrics

**Schema File**: `ontology/result/metrics/moic.schema.yaml`

## Description

Schema for the MOIC metric, defined as the ratio of total cash receipts to total capital invested.


**Schema ID**: `https://cfdl.dev/ontology/result/metrics/moic.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `cashFlowSeriesId` | String | ✅ | Reference to the equity cash‐flow series (post-investment). |
| `moic` | Number | ✅ | Computed multiple on invested capital (total distributions ÷ invested capital). |
| `asOfDate` | String | ❌ | Date on which the MOIC calculation is anchored. |
| `metadata` | Object | ❌ | Free-form metadata (e.g. notes, calculation basis). |


