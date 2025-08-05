#!/usr/bin/env python3
"""
CFDL Schema Documentation Generator

Automatically generates Markdown documentation from JSON schemas in the ontology directory.
"""

import os
import yaml
import json
from pathlib import Path
from typing import Dict, Any, List

def load_schema(schema_path: Path) -> Dict[str, Any]:
    """Load a YAML schema file."""
    with open(schema_path, 'r') as f:
        return yaml.safe_load(f)

def get_schema_title(schema: Dict[str, Any]) -> str:
    """Extract title from schema, with fallback."""
    return schema.get('title', schema.get('$id', 'Unknown Schema'))

def get_schema_description(schema: Dict[str, Any]) -> str:
    """Extract description from schema."""
    return schema.get('description', 'No description available.')

def format_property_type(prop: Dict[str, Any]) -> str:
    """Format property type information."""
    if 'type' in prop:
        prop_type = prop['type']
        if prop_type == 'array' and 'items' in prop:
            item_type = format_property_type(prop['items'])
            return f"Array of {item_type}"
        elif prop_type == 'object':
            return "Object"
        else:
            return prop_type.capitalize()
    elif 'enum' in prop:
        return f"Enum: {', '.join(prop['enum'])}"
    elif '$ref' in prop:
        ref = prop['$ref']
        if ref.startswith('#/'):
            return f"Reference: {ref[2:]}"
        else:
            return f"Reference: {ref}"
    else:
        return "Unknown"

def generate_properties_table(properties: Dict[str, Any], required: List[str]) -> str:
    """Generate a markdown table for schema properties."""
    if not properties:
        return "No properties defined."
    
    table = "| Property | Type | Required | Description |\n"
    table += "|----------|------|----------|-------------|\n"
    
    for prop_name, prop_def in properties.items():
        prop_type = format_property_type(prop_def)
        is_required = "✅" if prop_name in required else "❌"
        description = prop_def.get('description', 'No description')
        
        table += f"| `{prop_name}` | {prop_type} | {is_required} | {description} |\n"
    
    return table

def generate_schema_doc(schema_path: Path) -> str:
    """Generate markdown documentation for a single schema."""
    schema = load_schema(schema_path)
    
    title = get_schema_title(schema)
    description = get_schema_description(schema)
    
    # Get schema category from path
    category = schema_path.parent.name.replace('-', ' ').title()
    
    doc = f"# {title}\n\n"
    doc += f"**Category**: {category}\n\n"
    doc += f"**Schema File**: `{schema_path}`\n\n"
    doc += f"## Description\n\n{description}\n\n"
    
    # Add schema URL if present
    if '$id' in schema:
        doc += f"**Schema ID**: `{schema['$id']}`\n\n"
    
    # Properties section
    if 'properties' in schema:
        doc += "## Properties\n\n"
        required = schema.get('required', [])
        doc += generate_properties_table(schema['properties'], required)
        doc += "\n\n"
    
    # Examples section
    if 'examples' in schema:
        doc += "## Examples\n\n"
        for i, example in enumerate(schema['examples']):
            doc += f"### Example {i + 1}\n\n"
            doc += "```yaml\n"
            doc += yaml.dump(example, default_flow_style=False)
            doc += "```\n\n"
    
    # Additional constraints
    constraints = []
    if 'minimum' in schema:
        constraints.append(f"Minimum value: {schema['minimum']}")
    if 'maximum' in schema:
        constraints.append(f"Maximum value: {schema['maximum']}")
    if 'minLength' in schema:
        constraints.append(f"Minimum length: {schema['minLength']}")
    if 'maxLength' in schema:
        constraints.append(f"Maximum length: {schema['maxLength']}")
    if 'pattern' in schema:
        constraints.append(f"Pattern: `{schema['pattern']}`")
    
    if constraints:
        doc += "## Constraints\n\n"
        for constraint in constraints:
            doc += f"- {constraint}\n"
        doc += "\n"
    
    return doc

def generate_ontology_overview() -> str:
    """Generate overview documentation for the entire ontology."""
    ontology_path = Path("ontology")
    
    doc = "# CFDL Ontology Reference\n\n"
    doc += "This document provides a complete reference for all CFDL schemas.\n\n"
    
    # Count schemas by category
    categories = {}
    for schema_file in ontology_path.rglob("*.schema.yaml"):
        category = schema_file.parent.name
        if category not in categories:
            categories[category] = []
        categories[category].append(schema_file)
    
    doc += "## Schema Categories\n\n"
    for category, schemas in sorted(categories.items()):
        doc += f"### {category.replace('-', ' ').title()}\n\n"
        doc += f"**{len(schemas)} schemas**\n\n"
        
        for schema_file in sorted(schemas):
            schema = load_schema(schema_file)
            title = get_schema_title(schema)
            doc += f"- **[{title}](./{category}/{schema_file.stem}.md)** - {get_schema_description(schema)[:100]}...\n"
        doc += "\n"
    
    doc += f"## Total Schemas: {sum(len(schemas) for schemas in categories.values())}\n\n"
    
    return doc

def main():
    """Main function to generate all schema documentation."""
    print("🔧 Generating CFDL Schema Documentation...")
    
    ontology_path = Path("ontology")
    docs_path = Path("website/docs/specification")
    
    # Ensure docs directory exists
    docs_path.mkdir(parents=True, exist_ok=True)
    
    # Generate overview
    print("📋 Generating ontology overview...")
    overview_doc = generate_ontology_overview()
    with open(docs_path / "ontology-reference.md", 'w') as f:
        f.write(overview_doc)
    
    # Generate individual schema docs
    schema_count = 0
    for schema_file in ontology_path.rglob("*.schema.yaml"):
        print(f"📄 Processing {schema_file}...")
        
        # Create category directory
        category_dir = docs_path / schema_file.parent.name
        category_dir.mkdir(exist_ok=True)
        
        # Generate documentation
        schema_doc = generate_schema_doc(schema_file)
        
        # Write to file
        output_file = category_dir / f"{schema_file.stem}.md"
        with open(output_file, 'w') as f:
            f.write(schema_doc)
        
        schema_count += 1
    
    print(f"✅ Generated documentation for {schema_count} schemas")
    print(f"📁 Documentation saved to {docs_path}")

if __name__ == "__main__":
    main()