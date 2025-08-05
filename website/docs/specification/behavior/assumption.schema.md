# Assumption

**Category**: Behavior

**Schema File**: `ontology/behavior/assumption.schema.yaml`

## Description

Represents an economic or operational input used in modeling, such as growth rates, default probabilities, or cost escalators.

**Schema ID**: `https://cfdl.dev/ontology/behavior/assumption.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for the assumption within its scope. |
| `name` | String | ❌ | Optional human-readable name for clarity in reports and user interfaces. |
| `category` | String | ✅ | Logical grouping used for organizing assumptions in reports, sensitivity analysis, and user interfaces. |
| `scope` | String | ✅ | Level in the CFDL hierarchy where this assumption is defined and applies. |
| `type` | String | ✅ | Method by which the value is defined and how it should be evaluated. |
| `value` | Unknown | ❌ | Base value for fixed, table, or expression type assumptions. |
| `distribution` | Object | ❌ | Statistical distribution specification for stochastic modeling. |
| `timeSeries` | Array of Object | ❌ | Optional overrides that apply at specific times, regardless of assumption type. |
| `unit` | String | ❌ | Optional unit specification (e.g. '%', 'bps', '$/sf'). |
| `template` | String | ❌ | Optional reference to a reusable assumption template. |
| `overrides` | Object | ❌ | Field-level overrides when using a referenced template, allowing customization. |
| `source` | String | ❌ | Optional reference to the data source or authority for the assumption value. |
| `marketDataRef` | String | ❌ | Optional reference to a market data entity for dynamic updates. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints). |


