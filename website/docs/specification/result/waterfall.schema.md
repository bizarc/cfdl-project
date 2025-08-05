# Waterfall

**Category**: Result

**Schema File**: `ontology/result/waterfall.schema.yaml`

## Description

Defines an ordered, tiered distribution of available cash. Cash-in contributions (participants) are defined in the capital‐stack; each tier here governs how cash‐out is allocated, via explicit splits or by inheriting participant proportions.


**Schema ID**: `https://cfdl.dev/ontology/result/waterfall.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this waterfall definition. |
| `description` | String | ❌ | Long-form description of the overall waterfall logic. |
| `tiers` | Array of Object | ✅ | Ordered list of distribution tiers.  Each tier must have an `id`,  and either a `condition`, an `until`, or a `prefRate`.  The engine processes tiers in sequence, evaluating their trigger and then  allocating cash according to `distribute`.
 |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints, custom properties). |


