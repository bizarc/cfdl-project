# Schedule

**Category**: Temporal

**Schema File**: `ontology/temporal/schedule.schema.yaml`

## Description

Defines when and how often an event, stream, or rule fires. Supports one-time, recurring, and date-bounded recurrence patterns.


**Schema ID**: `https://cfdl.dev/ontology/temporal/schedule.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `type` | String | ✅ | Kind of schedule. |
| `date` | String | ❌ | Single date for a oneTime schedule. |
| `recurrenceRule` | Reference: https://cfdl.dev/ontology/temporal/recurrence-rule.schema.yaml | ❌ | Recurrence definition for recurring schedules. |
| `startDate` | String | ❌ | Inclusive start date for dateBounded or recurring schedules. |
| `endDate` | String | ❌ | Inclusive end date for dateBounded schedules. |
| `frequency` | Reference: https://cfdl.dev/ontology/common-types.schema.yaml#/definitions/frequency | ❌ | If present, overrides the default run frequency for this schedule. |
| `count` | Integer | ❌ | Total number of occurrences to generate (exclusive with endDate). |


