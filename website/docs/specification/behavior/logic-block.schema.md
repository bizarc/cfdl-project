# Logic Block

**Category**: Behavior

**Schema File**: `ontology/behavior/logic-block.schema.yaml`

## Description

Encapsulates a custom calculation, validation, trigger, or data-generation step during model execution.


**Schema ID**: `https://cfdl.dev/ontology/behavior/logic-block.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this logic block. |
| `name` | String | ✅ | Human-readable name of the logic block. |
| `description` | String | ❌ | Detailed explanation of the block’s purpose or algorithm. |
| `scope` | String | ✅ | Hierarchy level where this block runs. |
| `type` | Reference: https://cfdl.dev/ontology/common-types.schema.yaml#/definitions/logicBlockType | ✅ | Function category of this block (calculation, trigger, etc.). |
| `inputs` | Array of String | ❌ | Optional list of input IDs this block consumes (streams, assumptions, or other blocks).
 |
| `outputs` | Array of String | ❌ | Optional list of output IDs produced by this block (new streams or variables).
 |
| `executionOrder` | Integer | ❌ | Optional ordering index among peer blocks at the same scope (lower numbers run earlier).
 |
| `code` | String | ✅ | The code snippet implementing the logic block. It should reference inputs and assign to outputs or perform its side effect.
 |
| `language` | String | ❌ | Language of the code snippet (e.g. 'julia', 'python'). |
| `metadata` | Object | ❌ | Free-form metadata for UI hints, versioning, or tags. |


