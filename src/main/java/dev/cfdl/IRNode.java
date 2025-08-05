package dev.cfdl;

import java.util.Map;
import java.util.HashMap;

/**
 * Intermediate Representation Node
 * 
 * Represents a processed and validated CFDL entity ready for execution.
 */
public class IRNode {
    private final String id;
    private final String name;
    private final String schemaType;
    private final Map<String, Object> properties;
    
    public IRNode(String id, String name, String schemaType) {
        this.id = id;
        this.name = name;
        this.schemaType = schemaType;
        this.properties = new HashMap<>();
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
    
    @Override
    public String toString() {
        return String.format("IRNode{id='%s', name='%s', schema='%s', properties=%d}", 
            id, name, schemaType, properties.size());
    }
}