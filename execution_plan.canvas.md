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

## 2. Engine Prototype (Julia or Other)

1. **Time-Series Grid Generator**  
2. **Stream Allocator & Temporal Resolution**  
3. **Cash-Flow Aggregator & View Filtering**  
4. **Metrics Library (NPV, IRR, DSCR, MOIC, Payback, eNPV, eIRR)**  
5. **Waterfall Distributor**  
6. **Monte Carlo Harness**

---

## 3. Drag-and-Drop Builder UI

- Auto-generate forms from JSON-Schema.  
- Node-link canvas for deals/assets/components/streams.  
- Round-trip editing and “Export CFDL” functionality.

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

*Next Steps:*  
- Begin with **Step 1.1: Load & Validate Schemas** in the pipeline.
