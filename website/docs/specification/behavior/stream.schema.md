# Stream

**Category**: Behavior

**Schema File**: `ontology/behavior/stream.schema.yaml`

## Description

Defines a cash-flow stream with its scope, scheduling, amount logic, optional growth model, presentation tags, and metadata.


**Schema ID**: `https://cfdl.dev/ontology/behavior/stream.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this stream. |
| `name` | String | ✅ | Human-readable name of the stream. |
| `description` | String | ❌ | Long-form notes about the stream. |
| `scope` | String | ✅ | Scope at which this stream is defined. |
| `category` | String | ✅ | Broad financial statement category. |
| `subType` | String | ❌ | Sub-category aligned with reporting or domain logic (e.g. Operating, Financing, Tax, CapEx, Fee, Other).
 |
| `schedule` | Reference: https://cfdl.dev/ontology/temporal/schedule.schema.yaml | ✅ | Schedule defining when and how often cash flows occur. |
| `amount` | Unknown | ✅ | Base amount or specialized amount definition. Use a plain number for fixed amounts, or a calculator definition (e.g., loan payment).
 |
| `growth` | Object | ❌ | Optional growth model applied to the base amount each period. If omitted, the amount remains static.
 |
| `tags` | Array of String | ❌ | View or presentation tags for filtering (e.g., GAAP, NonGAAP, Forecast, Actual, Tax).
 |
| `metadata` | Object | ❌ | Free-form metadata (UI hints, custom attributes). |


