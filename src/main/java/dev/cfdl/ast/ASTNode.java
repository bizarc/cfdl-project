package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Base class for all CFDL AST nodes.
 * Provides common functionality for schema validation and JSON representation.
 */
public abstract class ASTNode {
    protected String id;
    protected String name;
    protected Map<String, Object> properties;
    protected List<ValidationError> validationErrors;
    
    public ASTNode() {
        this.properties = new HashMap<>();
        this.validationErrors = new ArrayList<>();
    }
    
    public ASTNode(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
    
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return this.properties.get(key);
    }
    
    public List<ValidationError> getValidationErrors() { return validationErrors; }
    public void addValidationError(ValidationError error) { this.validationErrors.add(error); }
    public boolean hasValidationErrors() { return !validationErrors.isEmpty(); }
    
    /**
     * Returns the schema type this AST node should be validated against.
     */
    public abstract String getSchemaType();
    
    /**
     * Converts this AST node to a JSON representation for schema validation.
     */
    public abstract JsonNode toJson();
    
    /**
     * Returns the line number where this node was defined in the source.
     */
    public int getLineNumber() {
        return (Integer) properties.getOrDefault("lineNumber", -1);
    }
    
    public void setLineNumber(int lineNumber) {
        this.properties.put("lineNumber", lineNumber);
    }
    
    /**
     * Returns the column number where this node was defined in the source.
     */
    public int getColumnNumber() {
        return (Integer) properties.getOrDefault("columnNumber", -1);
    }
    
    public void setColumnNumber(int columnNumber) {
        this.properties.put("columnNumber", columnNumber);
    }
    
    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s'}", 
            this.getClass().getSimpleName(), id, name);
    }
}