# Template

**Category**: Entity

**Schema File**: `ontology/entity/template.schema.yaml`

## Description

Defines a reusable CFDL template (DSL snippet) with named parameters for instantiating common constructs (e.g., standard leases, loans, waterfalls).


**Schema ID**: `https://cfdl.dev/ontology/entity/template.schema.yaml`

## Properties

| Property | Type | Required | Description |
|----------|------|----------|-------------|
| `id` | String | ✅ | Unique identifier for this template. |
| `name` | String | ✅ | Human-readable name of the template. |
| `description` | String | ❌ | Long-form description of what this template generates. |
| `templateType` | String | ✅ | The DSL construct this template applies to. |
| `parameters` | Array of Object | ✅ | Parameters required by this template; referenced in the DSL body. |
| `body` | String | ✅ | The CFDL code snippet, using parameter placeholders (e.g. `{{paramName}}`). Should compile to valid CFDL when parameters are substituted. |
| `metadata` | Object | ❌ | Free-form metadata (tags, UI hints, versioning). |


