# CFDL Documentation

This directory contains comprehensive documentation for the CFDL (Cash Flow Definition Language) project.

## Documents

### [execution_plan.canvas.md](./execution_plan.canvas.md)
The main project roadmap outlining all phases from DSL compilation through UI development.

### [engine-design.md](./engine-design.md)
Detailed technical design document for the CFDL Engine (Task 2), including:
- Architecture principles and component design
- Monte Carlo orchestration framework
- Stochastic variable handling
- Real options and extensibility planning
- Implementation specifications

## Project Overview

CFDL is a domain-specific language for financial cash flow modeling, designed for:
- Commercial real estate analysis
- Multi-asset portfolio modeling  
- Complex waterfall distributions
- Monte Carlo scenario analysis
- Real options modeling

## Architecture

```
CFDL Files (.cfdl) 
    ↓ [Java Pipeline]
IR (JSON) 
    ↓ [Julia Engine]
Cash Flows + Metrics + Distributions
    ↓ [UI/CLI]
Reports + Visualizations
```

## Current Status

- ✅ **Task 1**: Java-based compilation pipeline (YAML parsing → AST → IR)
- 🔄 **Task 2**: Julia-based execution engine (detailed design completed)
- ⏳ **Task 3**: Drag-and-drop UI builder
- ⏳ **Task 4**: CLI and automation tools
- ⏳ **Task 5**: Documentation and examples

See [execution_plan.canvas.md](./execution_plan.canvas.md) for detailed progress tracking.