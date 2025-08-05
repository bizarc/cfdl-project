# Rule Block

**Category**: Behavior

**Schema File**: `ontology/behavior/rule_block.schema.yaml`

## Description

Defines a conditional or event-driven operation that fires during model execution. A Rule Block can be triggered by a schedule, an external event, or an evaluated condition, and executes one or more actions (e.g., assigning values, logging, or invoking external services).


**Schema ID**: `https://cfdl.dev/ontology/behavior/rule-block.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this rule block. |
| `name` | String | ✅ | Human-readable name of the rule block. |
| `description` | String | ❌ | Detailed explanation of the rule’s purpose or logic. |
| `scope` | String | ❌ | Hierarchy level where this rule runs. |
| `schedule` | Reference: https://cfdl.dev/ontology/temporal/schedule.schema.yaml | ❌ | Optional schedule that triggers the rule periodically. |
| `eventTrigger` | Reference: https://cfdl.dev/ontology/temporal/event-trigger.schema.yaml | ❌ | Optional event definition that triggers the rule when an event occurs. |
| `condition` | String | ❌ | Optional boolean expression; when it evaluates to true (at or after the scheduled time or event), the action(s) execute.
 |
| `action` | Unknown | ✅ | Code snippet or list of statements to execute when the rule fires. Can reference model variables, streams, or external functions.
 |
| `metadata` | Object | ❌ | Free-form metadata for UI hints, tagging, or versioning. |


