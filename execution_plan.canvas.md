# CFDL v1.0 Demo Execution Plan

This document outlines the roadmap for building a minimum viable CFDL v1.0 demo, following **Option A**: starting with schema loading and validation.

---

## 1. Bootstrap the DSL Compilation Pipeline

1. **Load & Validate Schemas**  
   - Load all `*.schema.yaml` files from `ontology/`.  
   - Resolve and validate `$ref` links across schemas.  
   - Unit-test each schema for syntax and completeness.

2. **Define / Polish Grammar**  
   - Finalize `dsl/grammar/cfdl.g4` with rules for all entity, behavior, temporal, and result constructs.  
   - Embed enums (`dealType`, `assetCategory`, `logicBlockType`, etc.) directly in the grammar.

3. **AST & Schema-Driven Validation**  
   - Generate an AST via ANTLR visitor.  
   - Validate each AST node against its JSON-Schema: required fields, types, enums.  
   - Surface clear errors/warnings for any mismatches.

4. **Intermediate Representation (IR) Builder**  
   - Translate AST nodes into a normalized IR (JSON) enriched with schema metadata.  
   - Ensure the IR contains all data needed by the engine and UI layers.

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
