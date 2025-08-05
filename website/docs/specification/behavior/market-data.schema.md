# Market Data

**Category**: Behavior

**Schema File**: `ontology/behavior/market-data.schema.yaml`

## Description

Defines an external market‐data feed or reference index that can be  used to populate assumptions, overwrite timeSeries values, or drive dynamic input parameters in streams and logic blocks.


**Schema ID**: `https://cfdl.dev/ontology/behavior/market-data.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this market‐data definition. |
| `name` | String | ✅ | Human‐readable name of the market‐data feed or index. |
| `description` | String | ❌ | Long‐form description of the data source. |
| `dataType` | String | ✅ | Type of data provided. |
| `symbol` | Unknown | ✅ | Symbol(s) or key(s) identifying the specific data series (e.g. 'USGG10YR' for the 10-year Treasury, 'SPX' for the S&P 500).
 |
| `source` | Object | ✅ | Connection details for the data source. |
| `refreshSchedule` | Reference: https://cfdl.dev/ontology/temporal/schedule.schema.yaml | ❌ | Schedule defining how often to pull or refresh the data (e.g. daily at 6am).
 |
| `field` | String | ❌ | Specific field or column to extract from the data (e.g. 'close', 'rate'). |
| `metadata` | Object | ❌ | Free‐form metadata (tags, UI hints, caching policies). |


