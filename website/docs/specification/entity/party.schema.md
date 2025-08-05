# Party

**Category**: Entity

**Schema File**: `ontology/entity/party.schema.yaml`

## Description

Represents a person or organization that participates in deals, contracts, or other financial/legal arrangements.


**Schema ID**: `https://cfdl.dev/ontology/entity/party.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this party. |
| `name` | String | ✅ | Legal name of the person or organization. |
| `partyType` | String | ✅ | Classification of this party. |
| `description` | String | ❌ | Long-form notes or background on the party. |
| `contactInfo` | Object | ❌ | Contact details for this party. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints, integration keys). |


