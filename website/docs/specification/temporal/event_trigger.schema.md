# Event Trigger

**Category**: Temporal

**Schema File**: `ontology/temporal/event_trigger.schema.yaml`

## Description

Defines an event-based trigger for rule blocks or logic blocks. Supports assumption changes, value threshold crossings, external events, or custom expressions.


**Schema ID**: `https://cfdl.dev/ontology/temporal/event-trigger.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `type` | String | ✅ | Type of event trigger. |
| `assumptionId` | String | ❌ | Identifier of the assumption to monitor (for assumption_change). |
| `streamId` | String | ❌ | Identifier of the stream to monitor (for stream_threshold). |
| `operator` | String | ❌ | Comparison operator for threshold triggers. |
| `threshold` | Number | ❌ | Numeric threshold value to compare (for stream_threshold). |
| `externalEventName` | String | ❌ | Name or identifier of the external event to listen for (for external_event). |
| `expression` | String | ❌ | Custom boolean expression to evaluate (for custom triggers). |
| `metadata` | Object | ❌ | Free-form metadata for UI hints, logging, or integration. |


