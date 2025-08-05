#!/usr/bin/env python3
import sys
import os
import glob
import yaml
import jsonschema
from jsonschema import Draft7Validator, RefResolver

def load_yaml(path):
    with open(path, 'r') as f:
        return yaml.safe_load(f)

def validate_schema(schema, schema_path):
    # Use Draft-07 meta-schema
    meta_schema = Draft7Validator.META_SCHEMA
    resolver = RefResolver(
        base_uri=f'file://{os.path.dirname(os.path.abspath(schema_path))}/',
        referrer=schema
    )
    validator = Draft7Validator(meta_schema, resolver=resolver)
    errors = sorted(validator.iter_errors(schema), key=lambda e: e.path)
    return errors

def main():
    root = os.path.join(os.path.dirname(__file__), '..', 'ontology')
    patterns = [os.path.join(root, '**', '*.schema.yaml')]
    all_errors = False

    for pattern in patterns:
        for filepath in glob.iglob(pattern, recursive=True):
            print(f'→ Validating {os.path.relpath(filepath)}')
            try:
                schema = load_yaml(filepath)
            except Exception as e:
                print(f'  ✖ Failed to parse YAML: {e}')
                all_errors = True
                continue

            errors = validate_schema(schema, filepath)
            if errors:
                all_errors = True
                for err in errors:
                    # err.absolute_path shows where in the schema the error occurred
                    location = '.'.join(str(p) for p in err.absolute_path)
                    print(f'  ✖ {location or "<root>"}: {err.message}')
            else:
                print('  ✔ OK')

    if all_errors:
        print('\nSchema validation FAILED.')
        sys.exit(1)
    else:
        print('\nAll schemas validated successfully.')
        sys.exit(0)

if __name__ == '__main__':
    main()