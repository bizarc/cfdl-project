# Deal

**Category**: Entity

**Schema File**: `ontology/entity/deal.schema.yaml`

## Description

Top-level container for a CFDL model. Captures entry/exit terms, calendar rules, participants, assets, streams, and classification.


**Schema ID**: `https://cfdl.dev/ontology/entity/deal.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this deal. |
| `name` | String | ✅ | Human-readable name of the deal. |
| `description` | String | ❌ | Long-form notes about the deal. |
| `dealType` | Reference: https://cfdl.dev/ontology/common-types.schema.yaml#/definitions/dealType | ✅ | Industry-focused classification of this deal. |
| `currency` | Reference: https://cfdl.dev/ontology/common-types.schema.yaml#/definitions/currencyCode | ✅ | Currency for all monetary values. |
| `entryDate` | String | ✅ | Effective start/acquisition date. |
| `exitDate` | String | ✅ | Disposition or end date. |
| `analysisStart` | String | ✅ | Date to begin cash-flow analysis. |
| `holdingPeriodYears` | Integer | ✅ | Analysis horizon (in whole years). |
| `calendar` | Object | ❌ | Time-series generation rules. |
| `participants` | Array of Object | ❌ | Stakeholders in the deal (equity, sponsor, lender, etc.). |
| `capitalStackId` | String | ❌ | Reference to capital-stack.schema.yaml. |
| `assets` | Array of Reference: https://cfdl.dev/ontology/entity/asset.schema.yaml | ❌ | Assets under this deal. |
| `streams` | Array of Reference: https://cfdl.dev/ontology/behavior/stream.schema.yaml | ❌ | Deal-level cash-flow streams (fees, financing, one-time proceeds). |
| `stateConfig` | Object | ❌ | Lifecycle states & transitions for the deal. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints). |


