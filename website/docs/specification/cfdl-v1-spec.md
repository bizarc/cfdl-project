# CFDL v1.0 Specification

## Overview

**Cash Flow Domain Language (CFDL) v1.0** is a YAML-based domain-specific language for defining financial models. This specification defines the syntax, semantics, and processing rules for CFDL files.

## Language Syntax

### File Format
- **File Extension**: `.cfdl`
- **Syntax**: YAML (Yet Another Markup Language)
- **Encoding**: UTF-8
- **Comments**: Supported using `#` prefix

### Basic Structure

All CFDL files follow this top-level structure:

```yaml
# CFDL v1.0 Model
deal:
  DealIdentifier:
    # Deal properties and nested entities
```

## Entity Hierarchy

CFDL models follow a strict hierarchical structure:

```
Deal (Root)
├── Assets
│   ├── Components
│   └── Asset-level Streams
├── Deal-level Streams
├── Parties
├── Funds
├── Portfolios
├── Capital Stack
├── Contracts
└── Templates
```

## Core Entities

### 1. Deal
The root entity representing a financial structure.

**Schema**: `https://cfdl.dev/ontology/entity/deal.schema.yaml`

**Required Properties**:
- `name`: Human-readable deal name
- `dealType`: Type of deal (e.g., `commercial_real_estate`, `renewable_energy_project`)

**Example**:
```yaml
deal:
  MyDeal:
    name: "Example Real Estate Deal"
    dealType: commercial_real_estate
    currency: "USD"
    entryDate: 2024-01-01
    exitDate: 2029-01-01
```

### 2. Asset
Physical or financial assets within a deal.

**Schema**: `https://cfdl.dev/ontology/entity/asset.schema.yaml`

**Required Properties**:
- `name`: Asset name
- `category`: Asset category (e.g., `real_estate`, `physical_asset`)
- `dealId`: Reference to parent deal

**Example**:
```yaml
assets:
  - asset:
      OfficeBuilding:
        name: "Main Office Building"
        dealId: "MyDeal"
        category: real_estate
```

### 3. Component
Sub-components of assets (units, tranches, etc.).

**Schema**: `https://cfdl.dev/ontology/entity/component.schema.yaml`

**Required Properties**:
- `name`: Component name
- `assetId`: Reference to parent asset

### 4. Stream
Cash flow generators (revenue, expenses, financing).

**Schema**: `https://cfdl.dev/ontology/behavior/stream.schema.yaml`

**Required Properties**:
- `name`: Stream name
- `scope`: Scope level (`deal`, `asset`, `component`)
- `category`: Cash flow category (`Revenue`, `Expense`)
- `schedule`: Timing schedule object
- `amount`: Cash flow amount

**Example**:
```yaml
streams:
  - stream:
      RentalIncome:
        name: "Monthly Rental Income"
        scope: asset
        category: Revenue
        subType: Operating
        amount: 50000
        
        schedule:
          type: recurring
          startDate: 2024-01-01
          recurrenceRule:
            freq: monthly
            interval: 1
```

## Behavioral Elements

### Schedules
Define timing for cash flows and events.

**Schema**: `https://cfdl.dev/ontology/temporal/schedule.schema.yaml`

**Types**:
- `recurring`: Repeating cash flows
- `oneTime`: Single occurrence
- `dateBounded`: Recurring within date range

### Recurrence Rules
Define repetition patterns for recurring schedules.

**Schema**: `https://cfdl.dev/ontology/temporal/recurrence_rule.schema.yaml`

**Frequency Options**:
- `daily`, `weekly`, `monthly`, `quarterly`, `yearly`

### Assumptions
Model parameters and variables.

**Schema**: `https://cfdl.dev/ontology/behavior/assumption.schema.yaml`

## Data Types

### Primitive Types
- **String**: Text values in quotes
- **Number**: Integer or decimal values
- **Boolean**: `true` or `false`
- **Date**: ISO 8601 format (`YYYY-MM-DD`)

### Complex Types
- **Arrays**: Lists of items using YAML array syntax
- **Objects**: Nested property structures
- **References**: Entity ID references using strings

### Enumerations
Many properties use predefined enumerations:

**Deal Types**:
- `commercial_real_estate`
- `renewable_energy_project`
- `infrastructure_project`
- `private_equity_fund`

**Asset Categories**:
- `real_estate`
- `physical_asset`
- `financial_instrument`

**Stream Categories**:
- `Revenue`
- `Expense`

**Stream Sub-Types**:
- `Operating`
- `Financing`
- `Tax`
- `CapEx`
- `Other`

## Validation Rules

### Schema Validation
All entities must conform to their JSON schema definitions located in the ontology directory.

### Required Properties
Each entity type has mandatory properties that must be present.

### Reference Integrity
- Asset `dealId` must reference an existing deal
- Component `assetId` must reference an existing asset
- Stream `scope` must align with placement in hierarchy

### Data Constraints
- Dates must be valid ISO 8601 format
- Amounts must be numeric
- Enumerations must use exact values (case-sensitive)

## Processing Pipeline

### 1. YAML Parsing
CFDL files are parsed using standard YAML processors with these rules:
- Comments are ignored
- Indentation defines structure
- Arrays and objects follow YAML syntax

### 2. AST Generation
Abstract Syntax Trees are built with:
- Universal node structure
- Property maps for all attributes
- Schema type annotations

### 3. Schema Validation
Each node is validated against its JSON schema:
- Property presence validation
- Type checking
- Enumeration validation
- Format validation

### 4. Comprehensive Validation
Additional validation includes:
- Required property checking
- Reference integrity
- Business rule validation

### 5. IR Generation
Intermediate Representation is created with:
- Execution-ready nodes
- Metadata enrichment
- Relationship validation

## File Organization

### Single File Models
Simple models can be defined in a single `.cfdl` file:

```yaml
# simple-deal.cfdl
deal:
  SimpleDeal:
    name: "Simple Example"
    # ... deal definition
```

### Nested Definitions
Complex models use nested YAML structures:

```yaml
deal:
  ComplexDeal:
    assets:
      - asset:
          Asset1:
            components:
              - component:
                  Component1:
                    # ... component definition
```

## Best Practices

### Naming Conventions
- Use descriptive, meaningful names
- Avoid spaces in entity IDs
- Use PascalCase for entity identifiers

### Structure Organization
- Group related entities together
- Use consistent indentation (2 spaces recommended)
- Add comments for complex logic

### Validation Strategy
- Validate early and often during development
- Use schema validation to catch errors
- Test with comprehensive property checking

## Version Compatibility

CFDL v1.0 is the initial stable release. Future versions will maintain backward compatibility with v1.0 models where possible.

## Implementation Status

**Current Implementation**: ✅ Complete
- YAML parsing: Jackson-based parser
- Schema validation: JSON Schema validation
- AST generation: Universal node approach
- IR generation: Execution-ready transformation

**Supported Schemas**: All 29 ontology schemas
- 9 Entity schemas
- 6 Behavior schemas  
- 5 Temporal schemas
- 2 Result schemas
- 7 Metric schemas

## Examples

See the [Examples Gallery](../authoring/examples-gallery.md) for complete, working CFDL models demonstrating various use cases.