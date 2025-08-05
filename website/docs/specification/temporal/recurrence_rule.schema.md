# Recurrence Rule

**Category**: Temporal

**Schema File**: `ontology/temporal/recurrence_rule.schema.yaml`

## Description

Defines recurrence details for a recurring schedule, following RFC‐5545 semantics. Use in conjunction with a Schedule (recurrenceRule).


**Schema ID**: `https://cfdl.dev/ontology/temporal/recurrence-rule.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `freq` | String | ✅ | Recurrence frequency. |
| `interval` | Integer | ❌ | Interval between recurrences (e.g. every 2 weeks). |
| `byDay` | Array of String | ❌ | Specifies days of the week on which to recur. Use two-letter abbreviations: MO, TU, WE, TH, FR, SA, SU.
 |
| `byMonth` | Array of Integer | ❌ | Specifies months of the year (1 = January … 12 = December). |
| `byMonthDay` | Array of Integer | ❌ | Specifies days of the month (1–31 or negative for counting from end, e.g. -1 = last day of month).
 |
| `count` | Integer | ❌ | Total number of occurrences to generate (exclusive with `until`). |
| `until` | String | ❌ | End date/time of the recurrence (exclusive). Must be after the schedule’s startDate.
 |
| `wkst` | String | ❌ | Week start day, used when `freq` = WEEKLY (default SU). |


