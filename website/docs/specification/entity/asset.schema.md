# Asset

**Category**: Entity

**Schema File**: `ontology/entity/asset.schema.yaml`

## Description

Represents a cash-flowing asset (e.g. a building, property, or facility) within a deal.  Assets can have components, streams, contracts, and lifecycle state.


**Schema ID**: `https://cfdl.dev/ontology/entity/asset.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this asset instance. |
| `name` | String | ✅ | Human-readable name of the asset. |
| `dealId` | String | ✅ | Reference to the parent deal (deal.schema.yaml). |
| `category` | String | ✅ | High-level classification of the asset type. |
| `description` | String | ❌ | Long-form description or notes about the asset. |
| `location` | Object | ❌ | Geographic and address information. |
| `attributes` | Object | ❌ | Custom key-value attributes (e.g., sqft, yearBuilt). |
| `components` | Array of Reference: https://cfdl.dev/ontology/entity/component.schema.yaml | ❌ | Sub-assets or units within this asset. |
| `contracts` | Array of Reference: https://cfdl.dev/ontology/entity/contract.schema.yaml | ❌ | Contracts tied to this asset (leases, service agreements). |
| `streams` | Array of Reference: https://cfdl.dev/ontology/behavior/stream.schema.yaml | ❌ | Cash-flow streams defined at the asset level. |
| `stateConfig` | Object | ❌ | Lifecycle state machine configuration. |
| `history` | Array of Object | ❌ | Chronological record of state changes. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints). |


