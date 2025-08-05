# Tag Definition

**Category**: Result

**Schema File**: `ontology/result/tag-definition.schema.yaml`

## Description

Defines a presentation or view tag that can be applied to streams, assumptions, or results to filter, group, or style outputs.


**Schema ID**: `https://cfdl.dev/ontology/result/tag-definition.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `name` | String | ✅ | Tag name (e.g. GAAP, NonGAAP, Forecast, Actual, Tax). |
| `category` | String | ✅ | Tag dimension (e.g. 'presentation', 'timing', 'source'). |
| `description` | String | ❌ | Human-readable explanation of this tag’s meaning. |


