package dev.cfdl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * YAML-Based AST Node
 * 
 * Designed specifically for YAML parsing approach where ALL data
 * is stored in the properties Map, not individual fields.
 */
public class ASTNode {
    protected String id;
    protected String name;
    protected String schemaType;
    protected Map<String, Object> properties;
    protected List<ValidationError> validationErrors;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public ASTNode(String id, String name, String schemaType) {
        this.id = id;
        this.name = name;
        this.schemaType = schemaType;
        this.properties = new HashMap<>();
        this.validationErrors = new ArrayList<>();
    }
    
    // Core getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSchemaType() { return schemaType; }
    public Map<String, Object> getProperties() { return properties; }
    
    // Property management
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
    
    public Object getProperty(String key) {
        return this.properties.get(key);
    }
    
    public String getStringProperty(String key) {
        Object value = getProperty(key);
        return value != null ? value.toString() : null;
    }
    
    public Double getDoubleProperty(String key) {
        Object value = getProperty(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }
    
    // Validation
    public List<ValidationError> getValidationErrors() { return validationErrors; }
    public void addValidationError(ValidationError error) { this.validationErrors.add(error); }
    public boolean hasValidationErrors() { return !validationErrors.isEmpty(); }
    
    /**
     * YAML-Compatible JSON Serialization
     * 
     * Serializes ALL properties from the properties Map,
     * which is where YAML parser stores data.
     */
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Always include core fields
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        
        // Serialize ALL properties from properties Map
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                if (value instanceof Number) {
                    json.put(key, ((Number) value).doubleValue());
                } else if (value instanceof String) {
                    json.put(key, (String) value);
                } else if (value instanceof Boolean) {
                    json.put(key, (Boolean) value);
                } else if (value == null) {
                    json.putNull(key);
                } else {
                    // Complex objects (schedules, etc.)
                    json.putPOJO(key, value);
                }
            }
        });
        
        return json;
    }
    
    @Override
    public String toString() {
        return String.format("ASTNode{id='%s', name='%s', schema='%s'}", 
            id, name, schemaType);
    }
}