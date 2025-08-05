# Component

**Category**: Entity

**Schema File**: `ontology/entity/component.schema.yaml`

## Description

Represents a cash‐flow‐producing sub‐unit of an Asset (e.g., a tenant suite, a revenue‐generating machine). Components have cash‐flow streams and lifecycle states, while non‐revenue characteristics live in `attributes`.


**Schema ID**: `https://cfdl.dev/ontology/entity/component.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this component. |
| `name` | String | ✅ | Human-readable name of the component. |
| `description` | String | ❌ | Long-form notes about the component. |
| `assetId` | String | ✅ | Reference to the parent Asset (asset.schema.yaml). |
| `componentType` | String | ❌ | Classification (e.g., 'unit', 'machine', 'tenant-space'). |
| `attributes` | Object | ❌ | Structured properties for non‐cash‐flow characteristics (e.g., building identifier, floor number, square footage).
 |
| `streams` | Array of Reference: https://cfdl.dev/ontology/behavior/stream.schema.yaml | ❌ | Cash‐flow streams defined at the component level. |
| `stateConfig` | Object | ❌ | Lifecycle states & transitions for the component. |
| `metadata` | Object | ❌ | Free‐form metadata (tags, UI hints, custom attributes). |


