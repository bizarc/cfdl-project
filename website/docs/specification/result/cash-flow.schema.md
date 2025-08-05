# Cash Flow

**Category**: Result

**Schema File**: `ontology/result/cash-flow.schema.yaml`

## Description

Represents the aggregated periodic cash-flow results for a Deal, including all line-item breakdowns, unlevered and levered series, and the view tags applied.


**Schema ID**: `https://cfdl.dev/ontology/result/cash-flow.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `dealId` | String | ✅ | Reference to the Deal these cash flows belong to. |
| `currency` | Reference: https://cfdl.dev/ontology/common-types.schema.yaml#/definitions/currencyCode | ❌ | Currency code for all amounts. |
| `frequency` | Reference: https://cfdl.dev/ontology/common-types.schema.yaml#/definitions/frequency | ✅ | Time‐series frequency used (e.g. monthly). |
| `entries` | Array of Reference: definitions/cashFlowEntry | ✅ | Ordered list of period‐level cash‐flow entries. |
| `tagsApplied` | Array of String | ❌ | Presentation tags used for this series (e.g. [GAAP, Forecast]). |
| `metadata` | Object | ❌ | Free‐form metadata (UI hints, versioning). |


