# Troubleshooting Guide

Common issues and solutions when working with CFDL.

## Build Issues

### Schema Validation Errors

If you encounter schema validation errors:

1. Check that all required fields are present
2. Verify data types match the schema definitions
3. Ensure references point to existing entities

### Parse Errors

Common parsing issues:

- **Missing quotes**: String values must be quoted
- **Invalid identifiers**: Use alphanumeric characters and underscores
- **Incorrect indentation**: YAML requires consistent indentation

## Runtime Issues

### Missing Dependencies

Ensure all referenced entities are defined before use:

```yaml
# Define assumption first
assumption rental_growth:
  type: percentage
  value: 0.03

# Then reference it
stream rent:
  amount: base_rent * (1 + rental_growth)
```

### Circular References

Avoid circular dependencies between entities:

```yaml
# BAD - circular reference
asset building:
  components: [unit_a]

component unit_a:
  parent_asset: building  # Creates circular reference
```

## Common Solutions

### Check Schema Documentation

- Review the [Schema Reference](../specification/ontology-reference.md)
- Check [Best Practices](../authoring/best-practices.md)

### Validate Your Model

Run validation before execution:

```bash
# Validate schema compliance
mvn test -Dtest=SchemaValidatorTest

# Check for parsing issues  
mvn test -Dtest=ParserTest
```

### Debug Output

Enable verbose logging for detailed error information.

## Getting Help

If you're still experiencing issues:

1. Check the [examples](https://github.com/bizarc/cfdl-project/tree/main/examples) for working models
2. Review similar patterns in the codebase
3. [Open an issue](https://github.com/bizarc/cfdl-project/issues) with your specific error

## Resources

- [CFDL Specification](../specification/cfdl-v1-spec)
- [Getting Started Guide](../overview/getting-started)
- [Examples Gallery](../authoring/examples-gallery)