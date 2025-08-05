# Portfolio

**Category**: Entity

**Schema File**: `ontology/entity/portfolio.schema.yaml`

## Description

A collection of Deals grouped for aggregated analysis, reporting, and governance. Portfolios can define portfolio-level cash-flow streams (e.g., management fees), lifecycle states, and metadata.


**Schema ID**: `https://cfdl.dev/ontology/entity/portfolio.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this portfolio. |
| `name` | String | ✅ | Human-readable name of the portfolio. |
| `description` | String | ❌ | Long-form notes or summary of the portfolio. |
| `deals` | Array of Reference: https://cfdl.dev/ontology/entity/deal.schema.yaml | ✅ | The set of deals included in this portfolio. |
| `streams` | Array of Reference: https://cfdl.dev/ontology/behavior/stream.schema.yaml | ❌ | Portfolio-level cash-flow streams (e.g., portfolio fees). |
| `stateConfig` | Object | ❌ | Lifecycle states & transitions for the portfolio. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints, custom attributes). |


