# Examples Gallery

Collection of complete CFDL models demonstrating various use cases and patterns.

## Basic Examples

### Simple Rental Property

A basic single-asset rental property model:

```yaml
# Simple office building rental
deal office_rental:
  name: "Downtown Office Building"
  inception_date: "2024-01-01"
  
  assets:
    - building:
        name: "Main Building"
        asset_type: commercial_real_estate
        
        streams:
          - rent:
              name: "Base Rent"
              amount: 50000
              frequency: monthly
              start_date: "2024-01-01"
              
  assumptions:
    - rental_growth:
        type: percentage
        value: 0.03
        description: "Annual rent escalation"
```

### Multi-Tenant Property

A property with multiple tenant units:

```yaml
deal multi_tenant:
  name: "Multi-Tenant Office Complex"
  
  assets:
    - building:
        components:
          - unit_a:
              name: "Suite 100"
              rentable_area: 2500
              streams:
                - rent:
                    amount: 25000
                    frequency: monthly
          - unit_b:
              name: "Suite 200" 
              rentable_area: 3000
              streams:
                - rent:
                    amount: 30000
                    frequency: monthly
```

## Advanced Examples

### Development Project

A development project with construction and lease-up phases:

```yaml
deal development_project:
  name: "New Office Development"
  phases:
    - construction:
        start_date: "2024-01-01"
        duration: "18M"
        streams:
          - construction_costs:
              amount: -2000000
              frequency: monthly
    - lease_up:
        start_date: "2025-07-01"
        streams:
          - rental_income:
              amount: 100000
              frequency: monthly
              growth_rate: 0.025
```

### Portfolio Analysis

Multiple properties in a portfolio:

```yaml
portfolio real_estate_fund:
  name: "Core Real Estate Fund"
  
  deals:
    - office_building_a:
        asset_class: office
        market: "New York"
        acquisition_cost: 50000000
        
    - retail_center_b:
        asset_class: retail
        market: "Los Angeles" 
        acquisition_cost: 25000000
        
    - warehouse_c:
        asset_class: industrial
        market: "Chicago"
        acquisition_cost: 15000000
```

## Specialized Examples

### Renewable Energy Project

Solar project with PPA structure:

```yaml
deal solar_project:
  name: "Utility Scale Solar"
  
  assets:
    - solar_farm:
        capacity_mw: 100
        
        contracts:
          - ppa:
              counterparty: "Utility Company"
              term_years: 25
              price_per_mwh: 45
              
        streams:
          - energy_revenue:
              calculation: capacity_mw * generation_factor * ppa.price_per_mwh
              frequency: monthly
```

### Fund with Waterfall

Private equity fund with carried interest:

```yaml
fund growth_fund:
  name: "Growth Equity Fund III"
  
  capital_stack:
    - lp_capital:
        amount: 500000000
        return_preference: 0.08
    - gp_capital:
        amount: 25000000
        
  waterfall:
    - tier_1:
        distribution: return_of_capital
        allocation: 100% to lp_capital
    - tier_2:
        distribution: preferred_return
        rate: 0.08
        allocation: 100% to lp_capital
    - tier_3:
        distribution: carried_interest
        allocation:
          lp_capital: 80%
          gp_capital: 20%
```

## Live Examples

See the [`examples/`](https://github.com/bizarc/cfdl-project/tree/main/examples) directory for complete, runnable models:

- **[Office Building Deal](https://github.com/bizarc/cfdl-project/blob/main/examples/office-building-deal.cfdl)** - Commercial real estate acquisition
- **[Renewable Energy Project](https://github.com/bizarc/cfdl-project/blob/main/examples/renewable-energy-project.cfdl)** - Solar development with PPA
- **[Multi-Asset Portfolio](https://github.com/bizarc/cfdl-project/blob/main/examples/multi-asset-portfolio.cfdl)** - Diversified real estate portfolio
- **[Atomic Cash Flows](https://github.com/bizarc/cfdl-project/blob/main/examples/atomic-cash-flows-commercial-real-estate.cfdl)** - Detailed cash flow modeling

## Usage Tips

### Model Organization

- Start with simple structures and add complexity gradually
- Use consistent naming conventions
- Document assumptions and business logic
- Group related streams and assumptions

### Best Practices

- Validate models frequently during development
- Use meaningful entity names and descriptions
- Break complex models into reusable components
- Test edge cases and boundary conditions

## Contributing Examples

Have a great CFDL model to share? 

1. Fork the repository
2. Add your example to the [`examples/`](https://github.com/bizarc/cfdl-project/tree/main/examples) directory
3. Include documentation and comments
4. Submit a pull request

## Resources

- [Best Practices Guide](./best-practices) - Detailed authoring guidelines
- [Schema Reference](../specification/ontology-reference) - Complete schema documentation
- [Troubleshooting Guide](../developer/troubleshooting) - Common issues and solutions