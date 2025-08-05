# Fund

**Category**: Entity

**Schema File**: `ontology/entity/fund.schema.yaml`

## Description

A collection vehicle that holds Portfolios (or Deals directly), manages capital commitments, and applies fund-level streams (e.g. management fees, carried interest). Funds have participants (GPs, LPs), optional fundType, and lifecycle state.


**Schema ID**: `https://cfdl.dev/ontology/entity/fund.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this fund. |
| `name` | String | ✅ | Human-readable name of the fund. |
| `description` | String | ❌ | Long-form notes or summary of the fund. |
| `fundType` | String | ❌ | High-level classification of the fund vehicle. |
| `portfolios` | Array of Reference: https://cfdl.dev/ontology/entity/portfolio.schema.yaml | ❌ | Portfolios managed by this fund. |
| `streams` | Array of Reference: https://cfdl.dev/ontology/behavior/stream.schema.yaml | ❌ | Fund-level cash-flow streams (management fees, performance fees). |
| `participants` | Array of Object | ❌ | Entities participating in the fund (e.g., GP, LP). |
| `stateConfig` | Object | ✅ | Lifecycle states & transitions for the fund. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints). |


