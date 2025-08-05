# CFDL v1.2 Demo Execution Plan

This document outlines the roadmap for building a minimum viable CFDL v1.2 demo, using **YAML-based parsing** for robust and maintainable processing.

---

## 1. ✅ Bootstrap the DSL Compilation Pipeline [COMPLETED]

1. **✅ Load & Validate Schemas**  
   - Load all `*.schema.yaml` files from `ontology/`.  
   - Resolve and validate `$ref` links across schemas.  
   - Comprehensive schema validation with detailed error reporting.

2. **✅ YAML-Based Parsing**  
   - **DECISION**: Jackson YAML parsing for reliability.
   - CFDL files use standard YAML syntax: `deal: EntityName:` structure.
   - Eliminates complex grammar maintenance and parsing edge cases.

3. **✅ AST & Schema-Driven Validation**  
   - Generate AST from Jackson JsonNode tree via `CFDLYamlASTBuilder`.  
   - Validate each AST node against its JSON-Schema: required fields, types, enums.  
   - Surface clear errors/warnings with `ComprehensiveSchemaChecker`.

4. **✅ Intermediate Representation (IR) Builder**  
   - Translate AST nodes into a normalized IR (JSON) enriched with schema metadata.  
   - **PROVEN**: Full pipeline works end-to-end with atomic cash flows example.
   - IR contains all data needed by the engine and UI layers.

---

## 2. 🔄 Engine Prototype (Julia) [IN PROGRESS]

**Updated Architecture**: Monte Carlo as orchestrating framework

1. **Monte Carlo Harness** ✅ COMPLETED  
   - ✅ Trial orchestration (deterministic: 1 trial, stochastic: N trials)
   - ✅ Stochastic variable sampling framework (Normal, Uniform, LogNormal, Random Walk)
   - ✅ Seed management and result traceability
   - ✅ Comprehensive testing (31 passing tests)
   - ⏳ Parallel trial execution (sequential fallback implemented)

2. **Temporal Grid Generator** ✅ COMPLETED  
   - ✅ Multi-frequency support (daily, weekly, monthly, quarterly, annual)
   - ✅ Business day conventions (following, preceding, modified-following)
   - ✅ Holiday calendars (US federal holidays, UK bank holidays)
   - ✅ Day count conventions (Actual/365, 30/360, Actual/Actual)
   - ✅ Comprehensive testing (67 passing tests)

3. **Stream Allocator & Logic Block Engine**  
   - Growth factor application (potentially stochastic)
   - Logic block execution for custom overrides
   - Real options capability (dynamic model modification)

4. **Hierarchical Cash-Flow Aggregator**  
   - Monthly detailed view + Annual statement views
   - Drill-down capability: Deal → Asset → Component → Stream
   - Revenue aggregation with source tracking

5. **Metrics Library (NPV, IRR, DSCR, MOIC, Payback, eNPV, eIRR)**  
   - Multi-level metric calculations
   - Stochastic distributions support

6. **Waterfall Distributor**  
   - Sequential tier processing with condition evaluation
   - Capital-stack inheritance and explicit splits

---

## 3. Drag-and-Drop Builder UI

- Auto-generate forms from JSON-Schema.  
- Node-link canvas for deals/assets/components/streams.  
- Round-trip editing and "Export CFDL" functionality.

---

## 4. CLI & Automation

- `cfdl compile`: parse, validate, build IR.  
- `cfdl run`: execute engine on IR → output cash flows + metrics.  
- CI integration for schema linting, DSL tests, and engine smoke tests.

---

## 5. Documentation & Examples

- Author a **Cookbook** with sample deals.  
- Generate **Schema Reference** docs from JSON-Schema.  
- Prepare a **Slide Deck** demonstrating the canvas and engine outputs.

---

*Current Progress:*  
- ✅ **Task 1**: DSL Compilation Pipeline completed
- 🔄 **Task 2**: Engine Prototype - Sub-tasks 2.1-2.3 completed, implementing 2.4 (Cash Flow Aggregator)