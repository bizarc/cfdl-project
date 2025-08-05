# Contract

**Category**: Entity

**Schema File**: `ontology/entity/contract.schema.yaml`

## Description

Legal or financial agreement tied to a Deal, Asset, or Component (e.g., lease, PPA, service agreement). Contracts carry their own payment streams, parties, and lifecycle states.


**Schema ID**: `https://cfdl.dev/ontology/entity/contract.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this contract instance. |
| `name` | String | ✅ | Human-readable name of the contract. |
| `description` | String | ❌ | Long-form notes or summary of the contract. |
| `dealId` | String | ✅ | Reference to the parent Deal (deal.schema.yaml). |
| `assetId` | String | ❌ | Optional reference to an Asset this contract pertains to. |
| `componentId` | String | ❌ | Optional reference to a Component this contract pertains to. |
| `contractType` | String | ✅ | Classification of the contract. |
| `parties` | Array of Object | ✅ | Roles and references to all parties in this contract. |
| `startDate` | String | ✅ | Contract effective date. |
| `endDate` | String | ✅ | Contract termination or expiration date. |
| `terms` | Object | ❌ | Key-value map of additional contract terms (e.g., rentEscalation, feeRates). |
| `streams` | Array of Reference: https://cfdl.dev/ontology/behavior/stream.schema.yaml | ❌ | Cash-flow streams defined by this contract (e.g., rent payments). |
| `stateConfig` | Object | ❌ | Lifecycle states & transitions for the contract. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints). |


