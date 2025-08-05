# CFDL Modeling Best Practices

This guide covers best practices for creating maintainable, accurate, and professional CFDL models.

## File Organization

### Naming Conventions

#### Entity Identifiers
```yaml
# ✅ Good - Descriptive, no spaces, PascalCase
deal:
  OfficeBuilding_Downtown_2024:
    name: "Downtown Office Building Acquisition"

# ❌ Bad - Generic, spaces, unclear
deal:
  Deal1:
    name: "My Deal"
```

#### Stream Naming
```yaml
# ✅ Good - Clear purpose and scope
streams:
  - stream:
      Unit101_BaseRent:
        name: "Unit 101 - Base Rent"
      
  - stream:
      AssetLevel_PropertyTax:
        name: "Property Tax - Annual"

# ❌ Bad - Unclear scope and purpose
streams:
  - stream:
      Rent:
        name: "Rent"
```

### File Structure

#### Single File Approach
Use for simple models with < 5 assets:
```yaml
# simple-rental.cfdl
deal:
  SimpleRental:
    # All content in one file
```

#### Hierarchical Organization
For complex models, use clear nesting:
```yaml
deal:
  ComplexPortfolio:
    assets:
      - asset:
          Asset_Region1:
            components:
              - component:
                  Building_A:
                    # Clear hierarchy
```

## Entity Design Patterns

### Deal-Level Structure

#### Required Properties
Always include these deal properties:
```yaml
deal:
  MyDeal:
    name: "Descriptive Deal Name"           # Required
    dealType: commercial_real_estate        # Required
    currency: "USD"                         # Recommended
    
    entryDate: 2024-01-01                  # Recommended
    exitDate: 2029-01-01                   # Recommended
    analysisStart: 2024-01-01              # Recommended
    holdingPeriodYears: 5                  # Recommended
```

#### Deal Metadata
Include business context:
```yaml
deal:
  MyDeal:
    # Basic info...
    
    description: "Detailed description of the investment thesis"
    
    participants:
      - partyId: "sponsor-entity"
        role: sponsor
        amount: 10000000
      - partyId: "lender-entity"  
        role: lender
        amount: 40000000
    
    stateConfig:
      allowedStates: ["active", "disposition", "closed"]
      initialState: "active"
```

### Asset Design

#### Asset Categories
Choose appropriate categories:
```yaml
# Commercial Real Estate
assets:
  - asset:
      OfficeBuilding:
        category: real_estate
        
# Infrastructure  
assets:
  - asset:
      SolarArray:
        category: physical_asset
        
# Financial Instruments
assets:
  - asset:
      LoanPortfolio:
        category: financial_instrument
```

#### Asset Attributes
Include relevant operational data:
```yaml
assets:
  - asset:
      OfficeBuilding:
        attributes:
          totalSqFt: 100000
          yearBuilt: 2020
          occupancyRate: 0.95
          avgRentPSF: 45.00
          anchorTenants: 2
```

### Component Design

#### When to Use Components
Use components for:
- Individual rental units
- Equipment pieces  
- Portfolio tranches
- Separate operational units

```yaml
# Multi-unit property
assets:
  - asset:
      ApartmentBuilding:
        components:
          - component:
              Unit_101:
                name: "Unit 101 - 2BR/2BA"
                componentType: "residential_unit"
                attributes:
                  sqFt: 1200
                  bedrooms: 2
                  bathrooms: 2
```

#### Component Hierarchy
Maintain clear parent-child relationships:
```yaml
components:
  - component:
      Unit_101:
        assetId: "ApartmentBuilding"  # Must reference parent asset
        attributes:
          floor: 1
          premium: false
```

## Stream Design Patterns

### Revenue Streams

#### Operating Revenue
```yaml
streams:
  - stream:
      BaseRent:
        name: "Base Rental Income"
        scope: asset                    # or component/deal
        category: Revenue
        subType: Operating
        
        schedule:
          type: recurring
          startDate: 2024-01-01
          recurrenceRule:
            freq: monthly
            interval: 1
        
        amount: 50000
        
        growth:
          type: fixed
          rate: 0.025                   # 2.5% annual growth
        
        tags: ["GAAP", "Forecast", "BaseRent"]
```

#### Other Revenue
```yaml
streams:
  - stream:
      ParkingRevenue:
        name: "Parking Revenue"
        category: Revenue
        subType: Other                  # Not operating income
        amount: 5000
```

### Expense Streams

#### Operating Expenses
```yaml
streams:
  - stream:
      PropertyTax:
        name: "Annual Property Tax"
        category: Expense
        subType: Operating
        
        schedule:
          type: recurring
          startDate: 2024-01-01
          recurrenceRule:
            freq: yearly               # Annual payment
            interval: 1
        
        amount: 120000
```

#### Capital Expenditures
```yaml
streams:
  - stream:
      RoofReplacement:
        name: "Roof Replacement - Year 10"
        category: Expense
        subType: CapEx
        
        schedule:
          type: oneTime                # Single occurrence
          date: 2034-06-01
        
        amount: 250000
```

#### Financing Costs
```yaml
streams:
  - stream:
      DebtService:
        name: "Monthly Debt Service"
        category: Expense
        subType: Financing
        
        amount: 
          loanPayment:                 # Use calculator
            principal: 50000000
            rate: 0.055
            termMonths: 360
```

### Schedule Patterns

#### Recurring Schedules
```yaml
# Monthly
schedule:
  type: recurring
  startDate: 2024-01-01
  recurrenceRule:
    freq: monthly
    interval: 1

# Quarterly  
schedule:
  type: recurring
  startDate: 2024-03-31
  recurrenceRule:
    freq: monthly
    interval: 3

# Annual
schedule:
  type: recurring
  startDate: 2024-12-31
  recurrenceRule:
    freq: yearly
    interval: 1
```

#### Date-Bounded Schedules
```yaml
# Limited time recurring
schedule:
  type: dateBounded
  startDate: 2024-01-01
  endDate: 2029-12-31
  recurrenceRule:
    freq: monthly
    interval: 1
```

#### One-Time Events
```yaml
# Single occurrence
schedule:
  type: oneTime
  date: 2029-01-15
```

## Growth and Escalation

### Fixed Growth
```yaml
growth:
  type: fixed
  rate: 0.025                         # 2.5% annual growth
```

### CPI-Based Growth
```yaml
growth:
  type: cpi
  floor: 0.02                         # 2% minimum
  ceiling: 0.05                       # 5% maximum
```

### Custom Expressions
```yaml
growth:
  type: expression
  expr: "base_amount * (1 + inflation_rate)^year * occupancy_factor"
```

## Validation Strategies

### Required Property Checklist

#### Deal Level
- [ ] `name` - Descriptive deal name
- [ ] `dealType` - Valid enumeration value

#### Asset Level  
- [ ] `name` - Asset name
- [ ] `category` - Valid category
- [ ] `dealId` - References existing deal

#### Stream Level
- [ ] `name` - Stream description
- [ ] `scope` - Correct scope (deal/asset/component)
- [ ] `category` - Revenue or Expense
- [ ] `schedule` - Valid schedule object
- [ ] `amount` - Numeric value or calculator

### Reference Integrity
```yaml
# ✅ Good - Consistent references
deal:
  MyDeal:                             # Deal ID
    assets:
      - asset:
          MyAsset:                    # Asset ID
            dealId: "MyDeal"          # ✅ Matches deal ID
            components:
              - component:
                  MyComponent:
                    assetId: "MyAsset" # ✅ Matches asset ID
```

### Enumeration Values
Always use exact enumeration values:
```yaml
# ✅ Good - Exact case
dealType: commercial_real_estate
category: Revenue
freq: monthly

# ❌ Bad - Wrong case
dealType: Commercial_Real_Estate
category: revenue  
freq: Monthly
```

## Testing Your Models

### Validation Testing
```bash
# Test your model
mvn exec:java -Dexec.mainClass="dev.cfdl.Parser" -Dexec.args="my-model.cfdl"

# Look for:
# ✅ All entities parsed
# ✅ Schema validation passes  
# ✅ Comprehensive validation passes
# ✅ IR generation succeeds
```

### Common Error Patterns

#### Missing Required Properties
```
❌ Deal 'MyDeal' missing required property: name
```
**Fix**: Add the required property

#### Reference Errors  
```
❌ Asset 'MyAsset' references non-existent deal: WrongDealId
```
**Fix**: Ensure `dealId` matches actual deal identifier

#### Enumeration Errors
```
❌ Schema validation failed: 'Monthly' is not a valid enum value
```
**Fix**: Use lowercase: `monthly`

## Performance Considerations

### Model Size
- **Small models** (< 10 entities): Single file approach
- **Medium models** (10-50 entities): Organized nesting
- **Large models** (50+ entities): Consider breaking into logical groupings

### Stream Optimization
- Group similar streams when possible
- Use consistent naming patterns
- Avoid duplicate stream definitions

### Memory Usage
- Large models with many streams may require JVM tuning
- Consider batch processing for portfolio-level models

## Documentation Standards

### Comments
```yaml
# Deal Overview: Multi-asset retail portfolio
# Investment Period: 2024-2031 (7 years)
# Target IRR: 15%
deal:
  RetailPortfolio:
    name: "Regional Retail Portfolio 2024"
```

### Inline Documentation
```yaml
streams:
  - stream:
      RentalIncome:
        name: "Base Rental Income"
        description: "Monthly base rent from anchor tenant under 10-year lease"
        # Lease expires 2034-01-01, market rent expected +15%
        amount: 75000
```

### Version Control
- Use meaningful commit messages
- Tag releases with version numbers
- Document model changes in commit messages

## Advanced Patterns

### Calculator Integration
```yaml
amount:
  loanPayment:
    principal: 50000000
    rate: 0.055
    termMonths: 360
    
  # Or depreciation
  straightLineDepreciation:
    cost: 100000000
    salvage: 10000000
    lifePeriods: 39
```

### Conditional Logic
```yaml
# Future: Conditional streams based on assumptions
assumptions:
  - assumption:
      RefinanceAssumption:
        name: "Refinance in Year 5"
        value: true
        
# Stream that depends on assumption
streams:
  - stream:
      RefinanceProceeds:
        # conditionalOn: "RefinanceAssumption"
        schedule:
          type: oneTime
          date: 2029-01-01
```

### Multi-Currency Support
```yaml
deal:
  InternationalDeal:
    currency: "USD"                   # Base currency
    
    streams:
      - stream:
          EuroRevenue:
            amount: 100000
            currency: "EUR"           # Stream-level currency
            exchangeRate: 1.10        # EUR/USD rate
```

## Troubleshooting

### Debugging Workflow
1. **Check syntax**: Ensure valid YAML structure
2. **Validate schemas**: Look for missing required properties
3. **Check references**: Verify entity ID references
4. **Test incremental**: Start with simple model, add complexity

### Common Fixes
- **YAML indentation**: Use 2 spaces consistently
- **Quote strings**: Use quotes for string values with special characters
- **Date formats**: Use ISO 8601 format (YYYY-MM-DD)
- **Number formats**: Use numeric values, not strings, for amounts

Need help? Check our [Troubleshooting Guide](../developer/troubleshooting) or [Examples Gallery](./examples-gallery) for working models.