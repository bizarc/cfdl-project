package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Base class for all Intermediate Representation (IR) nodes.
 * 
 * IR nodes are the normalized, schema-enriched representation of CFDL
 * entities that can be consumed by the execution engine and UI layers.
 * They contain all original AST data plus:
 * - Schema metadata (types, validation rules, defaults)
 * - Resolved references and dependencies
 * - Normalized property names and values
 * - Engine-ready data structures
 */
public abstract class IRNode {
    protected String id;
    protected String name;
    protected String schemaType;
    protected Map<String, Object> properties;
    protected Map<String, Object> schemaMetadata;
    protected List<String> dependencies;
    protected boolean isValid;
    protected List<String> validationMessages;
    
    // Source location information
    protected int lineNumber;
    protected int columnNumber;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRNode() {
        this.properties = new HashMap<>();
        this.schemaMetadata = new HashMap<>();
        this.dependencies = new ArrayList<>();
        this.validationMessages = new ArrayList<>();
        this.isValid = true;
        this.lineNumber = -1;
        this.columnNumber = -1;
    }
    
    public IRNode(String id, String name, String schemaType) {
        this();
        this.id = id;
        this.name = name;
        this.schemaType = schemaType;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSchemaType() { return schemaType; }
    public void setSchemaType(String schemaType) { this.schemaType = schemaType; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return this.properties.get(key);
    }
    
    public Map<String, Object> getSchemaMetadata() { return schemaMetadata; }
    public void setSchemaMetadata(Map<String, Object> metadata) { this.schemaMetadata = metadata; }
    
    public void setSchemaMetadata(String key, Object value) {
        this.schemaMetadata.put(key, value);
    }
    
    public Object getSchemaMetadata(String key) {
        return this.schemaMetadata.get(key);
    }
    
    public List<String> getDependencies() { return dependencies; }
    public void addDependency(String dependency) { this.dependencies.add(dependency); }
    
    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { this.isValid = valid; }
    
    public List<String> getValidationMessages() { return validationMessages; }
    public void addValidationMessage(String message) { 
        this.validationMessages.add(message);
        this.isValid = false;
    }
    
    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }
    
    public int getColumnNumber() { return columnNumber; }
    public void setColumnNumber(int columnNumber) { this.columnNumber = columnNumber; }
    
    /**
     * Converts this IR node to a JSON representation.
     * This is the normalized format consumed by the execution engine.
     */
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Core properties
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (schemaType != null) json.put("$schema", schemaType);
        
        // Add all custom properties
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value instanceof String) {
                json.put(key, (String) value);
            } else if (value instanceof Number) {
                json.put(key, ((Number) value).doubleValue());
            } else if (value instanceof Boolean) {
                json.put(key, (Boolean) value);
            } else if (value instanceof List) {
                json.set(key, objectMapper.valueToTree(value));
            } else if (value instanceof Map) {
                json.set(key, objectMapper.valueToTree(value));
            }
        }
        
        // Add metadata section for debugging/tooling
        if (!schemaMetadata.isEmpty() || !dependencies.isEmpty() || !isValid) {
            ObjectNode metadata = objectMapper.createObjectNode();
            
            if (!schemaMetadata.isEmpty()) {
                metadata.set("schema", objectMapper.valueToTree(schemaMetadata));
            }
            
            if (!dependencies.isEmpty()) {
                metadata.set("dependencies", objectMapper.valueToTree(dependencies));
            }
            
            metadata.put("valid", isValid);
            
            if (!validationMessages.isEmpty()) {
                metadata.set("validationMessages", objectMapper.valueToTree(validationMessages));
            }
            
            if (lineNumber >= 0) {
                metadata.put("lineNumber", lineNumber);
            }
            
            if (columnNumber >= 0) {
                metadata.put("columnNumber", columnNumber);
            }
            
            json.set("$metadata", metadata);
        }
        
        return json;
    }
    
    /**
     * Returns a compact JSON representation suitable for engine consumption.
     * This excludes metadata and focuses on just the business data.
     */
    public abstract JsonNode toEngineJson();
    
    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s', valid=%s}", 
            this.getClass().getSimpleName(), id, name, isValid);
    }
}