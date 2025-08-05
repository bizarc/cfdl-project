# Capital Stack

**Category**: Entity

**Schema File**: `ontology/entity/capital-stack.schema.yaml`

## Description

Defines who contributed capital to a deal and how available cash is distributed back via waterfall rules.  Contributions (participants) are separate from distribution logic (waterfall).


**Schema ID**: `https://cfdl.dev/ontology/entity/capital-stack.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this capital stack instance. |
| `participants` | Array of Object | ✅ | Entities contributing capital to the deal. |
| `waterfall` | Reference: https://cfdl.dev/ontology/result/waterfall.schema.yaml | ✅ | Ordered distribution rules: tiers defining who gets paid and in what priority/split as cash becomes available.
 |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints, custom properties). |


