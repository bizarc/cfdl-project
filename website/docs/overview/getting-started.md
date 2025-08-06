# Getting Started with CFDL

Welcome to **Cash Flow Domain Language (CFDL)**! This guide will help you create your first financial model and understand the basics of CFDL syntax.

## Prerequisites

- Basic understanding of YAML syntax
- Familiarity with financial modeling concepts
- Java 11+ (for running the CFDL processor)
- Maven (for building and running)

## Installation

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/cfdl-project.git
cd cfdl-project
```

### 2. Build the Project
```bash
mvn clean compile
```

### 3. Test with Example
```bash
mvn exec:java -Dexec.mainClass="dev.cfdl.Parser" -Dexec.args="examples/office-building-deal.cfdl"
```

You should see output like:
```
🎉 COMPLETE CFDL Pipeline Success!
📊 Pipeline Summary:
  • Parsed 6 AST nodes
  • Built 6 IR nodes
  • All schemas validated successfully
```

## 🚀 **Engine Capabilities (v1.2)**

The CFDL Engine now provides complete financial analysis capabilities:

### Monte Carlo Simulation
- **1,000+ trial capacity** with sub-second execution
- **Stochastic variable sampling** for risk analysis
- **Reproducible results** with seed-based random generation

### Waterfall Distribution
- **Multi-asset class support**: Commercial Real Estate, Private Equity, Infrastructure
- **Industry-standard mechanics**: Preferred returns, catch-ups, carried interest
- **Real-time participant analysis**: MOIC, IRR, profit/loss calculations

### Financial Metrics
- **NPV**: Net Present Value with configurable discount rates
- **IRR**: Internal Rate of Return with precision solving
- **DSCR**: Debt Service Coverage Ratio analysis
- **MOIC**: Multiple of Invested Capital calculations
- **Payback**: Period analysis with detailed timing

### Demo Available
Try the interactive waterfall demo:
```bash
cd engine
julia demo_waterfall.jl
```

This showcases realistic CRE, PE, and Infrastructure waterfall distributions with detailed participant return analysis.

## Your First CFDL Model

Let's create a simple rental property model step by step.

### Step 1: Create the Deal

Create a new file called `my-first-deal.cfdl`:

```yaml
# My First CFDL Model - A Simple Rental Property
deal:
  MyRentalProperty:
    name: "My First Rental Property"
    dealType: commercial_real_estate
    currency: "USD"
    
    entryDate: 2024-01-01
    exitDate: 2029-01-01
    analysisStart: 2024-01-01
    holdingPeriodYears: 5
```

### Step 2: Add an Asset

```yaml
deal:
  MyRentalProperty:
    name: "My First Rental Property"
    dealType: commercial_real_estate
    currency: "USD"
    
    entryDate: 2024-01-01
    exitDate: 2029-01-01
    analysisStart: 2024-01-01
    holdingPeriodYears: 5
    
    # Add the asset
    assets:
      - asset:
          RentalBuilding:
            name: "4-Unit Apartment Building"
            dealId: "MyRentalProperty"
            category: real_estate
            description: "Small apartment building with 4 units"
            
            attributes:
              totalUnits: 4
              totalSqFt: 3200
              yearBuilt: 1995
```

### Step 3: Add Revenue Streams

```yaml
deal:
  MyRentalProperty:
    name: "My First Rental Property"
    dealType: commercial_real_estate
    currency: "USD"
    
    entryDate: 2024-01-01
    exitDate: 2029-01-01
    analysisStart: 2024-01-01
    holdingPeriodYears: 5
    
    assets:
      - asset:
          RentalBuilding:
            name: "4-Unit Apartment Building"
            dealId: "MyRentalProperty"
            category: real_estate
            
            # Add revenue streams
            streams:
              - stream:
                  RentalIncome:
                    name: "Monthly Rental Income"
                    description: "Rent from 4 units at $800/month each"
                    scope: asset
                    category: Revenue
                    subType: Operating
                    
                    schedule:
                      type: recurring
                      startDate: 2024-01-01
                      recurrenceRule:
                        freq: monthly
                        interval: 1
                    
                    amount: 3200  # 4 units × $800/month
                    
                    tags: ["Rental", "Operating"]
```

### Step 4: Add Operating Expenses

```yaml
deal:
  MyRentalProperty:
    # ... previous content ...
    
    assets:
      - asset:
          RentalBuilding:
            # ... previous content ...
            
            streams:
              - stream:
                  RentalIncome:
                    # ... previous content ...
              
              # Add operating expenses
              - stream:
                  PropertyTaxes:
                    name: "Annual Property Taxes"
                    scope: asset
                    category: Expense
                    subType: Operating
                    
                    schedule:
                      type: recurring
                      startDate: 2024-01-01
                      recurrenceRule:
                        freq: yearly
                        interval: 1
                    
                    amount: 4800  # Annual property taxes
                    
                    tags: ["Tax", "Operating"]
              
              - stream:
                  Maintenance:
                    name: "Monthly Maintenance"
                    scope: asset
                    category: Expense
                    subType: Operating
                    
                    schedule:
                      type: recurring
                      startDate: 2024-01-01
                      recurrenceRule:
                        freq: monthly
                        interval: 1
                    
                    amount: 400  # Monthly maintenance
                    
                    tags: ["Maintenance", "Operating"]
```

### Step 5: Add Deal-Level Financing

```yaml
deal:
  MyRentalProperty:
    # ... previous content ...
    
    # Add deal-level financing
    streams:
      - stream:
          MortgagePayment:
            name: "Monthly Mortgage Payment"
            description: "Principal and interest on acquisition loan"
            scope: deal
            category: Expense
            subType: Financing
            
            schedule:
              type: recurring
              startDate: 2024-01-01
              recurrenceRule:
                freq: monthly
                interval: 1
            
            amount: 2400  # Monthly P&I payment
            
            tags: ["Debt", "Financing"]
```

### Step 6: Test Your Model

Save your file as `my-first-deal.cfdl` and test it:

```bash
mvn exec:java -Dexec.mainClass="dev.cfdl.Parser" -Dexec.args="my-first-deal.cfdl"
```

## Understanding the Output

When you run your CFDL model, you'll see:

### 1. Parsing Results
```
📄 STEP 1: CFDL → AST (Abstract Syntax Tree)
Parsed 5 CFDL definitions:
  • ASTNode: MyRentalProperty (My First Rental Property)
  • ASTNode: RentalIncome (Monthly Rental Income)
  • ASTNode: PropertyTaxes (Annual Property Taxes)
  • ASTNode: Maintenance (Monthly Maintenance)
  • ASTNode: RentalBuilding (4-Unit Apartment Building)
  • ASTNode: MortgagePayment (Monthly Mortgage Payment)
```

### 2. Validation Results
```
🔍 STEP 2b: Comprehensive Required Properties Check
📊 Summary: 6/6 entities valid
```

### 3. IR Generation
```
🔧 STEP 3: AST → IR (Intermediate Representation)
📊 IR Validation: 6 nodes, 0 errors
```

## Key CFDL Concepts

### Entity Hierarchy
Your model creates this hierarchy:
```
MyRentalProperty (Deal)
├── RentalBuilding (Asset)
│   ├── RentalIncome (Revenue Stream)
│   ├── PropertyTaxes (Expense Stream)
│   └── Maintenance (Expense Stream)
└── MortgagePayment (Deal-level Expense Stream)
```

### Cash Flow Categories
- **Revenue**: Money coming in (rental income)
- **Expense**: Money going out (taxes, maintenance, financing)

### Scopes
- **asset**: Cash flows that belong to a specific asset
- **deal**: Cash flows that apply to the entire deal

### Schedules
- **recurring**: Repeating cash flows (monthly rent)
- **oneTime**: Single occurrence (closing costs)

## Next Steps

### Explore Examples
Check out more complex examples in the [`examples/`](https://github.com/your-username/cfdl-project/tree/main/examples) directory:

- **Office Building Deal**: Simple commercial real estate
- **Multi-Asset Portfolio**: Complex deal with multiple assets
- **Renewable Energy Project**: Infrastructure with tax benefits
- **Atomic Cash Flows**: Component-level modeling

### Learn Best Practices
Read our [Best Practices Guide](../authoring/best-practices.md) for advanced modeling techniques.

### Understand the Schema
Explore the [Schema Reference](../specification/ontology-reference.md) to understand all available properties and options.

### Join the Community
- Report issues on [GitHub](https://github.com/your-username/cfdl-project/issues)
- Contribute examples and improvements
- Ask questions in discussions

## Common Issues

### Validation Errors
If you see validation errors:
1. Check property names for typos
2. Ensure required properties are present
3. Verify enumeration values are exact (case-sensitive)

### Reference Errors
If you see reference errors:
1. Check that `dealId` matches your deal identifier
2. Ensure `assetId` references exist for components
3. Verify entity IDs don't have spaces or special characters

### YAML Syntax Errors
If you see YAML parsing errors:
1. Check indentation (use 2 spaces consistently)
2. Ensure colons have spaces after them
3. Verify quotes around string values

Need help? Check our [Troubleshooting Guide](../developer/troubleshooting) or [open an issue](https://github.com/bizarc/cfdl-project/issues).

Happy modeling! 🚀