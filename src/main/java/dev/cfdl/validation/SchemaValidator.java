package dev.cfdl.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import dev.cfdl.ast.ASTNode;
import dev.cfdl.ast.ValidationError;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Validates CFDL AST nodes against their corresponding JSON schemas.
 */
public class SchemaValidator {
    
    private final Map<String, JsonSchema> schemaCache = new HashMap<>();
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
    // Remove unused jsonMapper field
    private final JsonSchemaFactory schemaFactory = JsonSchemaFactory.byDefault();
    
    /**
     * Validates an AST node against its corresponding schema.
     */
    public List<ValidationError> validateNode(ASTNode node) {
        List<ValidationError> errors = new ArrayList<>();
        
        try {
            // Get the schema for this node type
            JsonSchema schema = getSchema(node.getSchemaType());
            
            // Convert AST node to JSON for validation
            JsonNode nodeJson = node.toJson();
            
            // Perform validation
            ProcessingReport report = schema.validate(nodeJson);
            
            // Convert processing messages to validation errors
            for (ProcessingMessage message : report) {
                ValidationError.Severity severity = message.getLogLevel().toString().equals("ERROR") 
                    ? ValidationError.Severity.ERROR 
                    : ValidationError.Severity.WARNING;
                
                ValidationError error = new ValidationError(
                    severity,
                    message.getMessage(),
                    message.asJson().path("schema").path("pointer").asText(),
                    message.asJson().path("instance").path("pointer").asText(),
                    node.getLineNumber(),
                    node.getColumnNumber()
                );
                errors.add(error);
            }
            
        } catch (Exception e) {
            ValidationError error = new ValidationError(
                ValidationError.Severity.ERROR,
                "Schema validation failed: " + e.getMessage(),
                node.getSchemaType(),
                null,
                node.getLineNumber(),
                node.getColumnNumber()
            );
            errors.add(error);
        }
        
        return errors;
    }
    
    /**
     * Gets or loads a JSON schema from the cache.
     */
    private JsonSchema getSchema(String schemaUrl) throws Exception {
        JsonSchema schema = schemaCache.get(schemaUrl);
        if (schema == null) {
            schema = loadSchema(schemaUrl);
            schemaCache.put(schemaUrl, schema);
        }
        return schema;
    }
    
    /**
     * Loads a JSON schema from the ontology directory.
     */
    private JsonSchema loadSchema(String schemaUrl) throws Exception {
        // Convert schema URL to file path
        String schemaPath = convertUrlToPath(schemaUrl);
        
        // Load schema from resources or file system
        JsonNode schemaNode = loadSchemaFile(schemaPath);
        
        // Create and return the schema
        return schemaFactory.getJsonSchema(schemaNode);
    }
    
    /**
     * Converts a schema URL to a file path.
     */
    private String convertUrlToPath(String schemaUrl) {
        // Convert https://cfdl.dev/ontology/entity/deal.schema.yaml to ontology/entity/deal.schema.yaml
        if (schemaUrl.startsWith("https://cfdl.dev/")) {
            return schemaUrl.substring("https://cfdl.dev/".length());
        }
        return schemaUrl;
    }
    
    /**
     * Loads a schema file from the file system.
     */
    private JsonNode loadSchemaFile(String schemaPath) throws IOException {
        // Try to load from classpath first
        InputStream stream = getClass().getClassLoader().getResourceAsStream(schemaPath);
        if (stream != null) {
            return yamlMapper.readTree(stream);
        }
        
        // Try to load from file system
        try {
            return yamlMapper.readTree(new java.io.File(schemaPath));
        } catch (IOException e) {
            throw new IOException("Could not load schema file: " + schemaPath, e);
        }
    }
    
    /**
     * Validates a list of AST nodes and returns all validation errors.
     */
    public List<ValidationError> validateNodes(List<ASTNode> nodes) {
        List<ValidationError> allErrors = new ArrayList<>();
        
        for (ASTNode node : nodes) {
            List<ValidationError> nodeErrors = validateNode(node);
            allErrors.addAll(nodeErrors);
            
            // Add errors to the node as well
            nodeErrors.forEach(node::addValidationError);
        }
        
        return allErrors;
    }
    
    /**
     * Returns true if all nodes pass validation (no errors, warnings are OK).
     */
    public boolean isValid(List<ASTNode> nodes) {
        List<ValidationError> errors = validateNodes(nodes);
        return errors.stream().noneMatch(error -> error.getSeverity() == ValidationError.Severity.ERROR);
    }
}