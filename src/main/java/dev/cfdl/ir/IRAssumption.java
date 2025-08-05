package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Assumption entities.
 * 
 * Corresponds to ontology/behavior/assumption.schema.yaml and contains all
 * normalized assumption properties plus schema metadata needed for execution.
 * 
 * Assumptions represent economic or operational inputs used in modeling,
 * supporting fixed values, statistical distributions, time series, and expressions.
 */
public class IRAssumption extends IRNode {
    private String category;
    private String scope;
    private String type;
    private Object value;
    private Map<String, Object> distribution;
    private List<Map<String, Object>> timeSeries;
    private String unit;
    private String template;
    private Map<String, Object> overrides;
    private String source;
    private String marketDataRef;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRAssumption() {
        super();
        this.setSchemaType("https://cfdl.dev/ontology/behavior/assumption.schema.yaml");
    }
    
    public IRAssumption(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/behavior/assumption.schema.yaml");
    }
    
    // Getters and setters for assumption-specific properties
    public String getCategory() { return category; }
    public void setCategory(String category) { 
        this.category = category;
        setProperty("category", category);
    }
    
    public String getScope() { return scope; }
    public void setScope(String scope) { 
        this.scope = scope;
        setProperty("scope", scope);
    }
    
    public String getType() { return type; }
    public void setType(String type) { 
        this.type = type;
        setProperty("type", type);
    }
    
    public Object getValue() { return value; }
    public void setValue(Object value) { 
        this.value = value;
        setProperty("value", value);
    }
    
    public Map<String, Object> getDistribution() { return distribution; }
    public void setDistribution(Map<String, Object> distribution) { 
        this.distribution = distribution;
        setProperty("distribution", distribution);
    }
    
    public List<Map<String, Object>> getTimeSeries() { return timeSeries; }
    public void setTimeSeries(List<Map<String, Object>> timeSeries) { 
        this.timeSeries = timeSeries;
        setProperty("timeSeries", timeSeries);
    }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { 
        this.unit = unit;
        setProperty("unit", unit);
    }
    
    public String getTemplate() { return template; }
    public void setTemplate(String template) { 
        this.template = template;
        setProperty("template", template);
        if (template != null) {
            addDependency(template);
        }
    }
    
    public Map<String, Object> getOverrides() { return overrides; }
    public void setOverrides(Map<String, Object> overrides) { 
        this.overrides = overrides;
        setProperty("overrides", overrides);
    }
    
    public String getSource() { return source; }
    public void setSource(String source) { 
        this.source = source;
        setProperty("source", source);
    }
    
    public String getMarketDataRef() { return marketDataRef; }
    public void setMarketDataRef(String marketDataRef) { 
        this.marketDataRef = marketDataRef;
        setProperty("marketDataRef", marketDataRef);
        if (marketDataRef != null) {
            addDependency(marketDataRef);
        }
    }
    
    /**
     * Determine if this assumption uses stochastic modeling.
     */
    public boolean isStochastic() {
        return "distribution".equals(type) && distribution != null;
    }
    
    /**
     * Determine if this assumption has time-varying values.
     */
    public boolean isTimeVarying() {
        return timeSeries != null && !timeSeries.isEmpty();
    }
    
    /**
     * Get the effective value for the assumption, considering type and overrides.
     */
    public Object getEffectiveValue() {
        // If using template with overrides, merge them
        if (template != null && overrides != null) {
            // This would normally resolve the template and apply overrides
            // For now, just return the base value
            return value;
        }
        return value;
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("category", category);
        json.put("scope", scope);
        json.put("type", type);
        
        // Optional name
        if (name != null) json.put("name", name);
        
        // Value (depends on type)
        if (value != null) {
            json.set("value", objectMapper.valueToTree(value));
        }
        
        // Distribution for stochastic modeling
        if (distribution != null) {
            json.set("distribution", objectMapper.valueToTree(distribution));
        }
        
        // Time series data
        if (timeSeries != null && !timeSeries.isEmpty()) {
            json.set("timeSeries", objectMapper.valueToTree(timeSeries));
        }
        
        // Optional fields
        if (unit != null) json.put("unit", unit);
        if (template != null) json.put("template", template);
        if (source != null) json.put("source", source);
        if (marketDataRef != null) json.put("marketDataRef", marketDataRef);
        
        if (overrides != null) {
            json.set("overrides", objectMapper.valueToTree(overrides));
        }
        
        // Add computed metadata for engine convenience
        json.put("isStochastic", isStochastic());
        json.put("isTimeVarying", isTimeVarying());
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Assumption id is required");
        }
        
        if (category == null || category.trim().isEmpty()) {
            addValidationMessage("Assumption category is required");
        }
        
        if (scope == null || scope.trim().isEmpty()) {
            addValidationMessage("Assumption scope is required");
        }
        
        if (type == null || type.trim().isEmpty()) {
            addValidationMessage("Assumption type is required");
        }
        
        // Validate category against allowed values
        if (category != null) {
            String[] validCategories = {
                "revenue", "expense", "capital", "leasing", "timing", "financing", "other"
            };
            boolean validCategory = false;
            for (String valid : validCategories) {
                if (valid.equals(category)) {
                    validCategory = true;
                    break;
                }
            }
            if (!validCategory) {
                addValidationMessage("Assumption category must be one of: revenue, expense, capital, leasing, timing, financing, other");
            }
        }
        
        // Validate scope against allowed values
        if (scope != null) {
            String[] validScopes = {
                "component", "asset", "deal", "portfolio", "fund"
            };
            boolean validScope = false;
            for (String valid : validScopes) {
                if (valid.equals(scope)) {
                    validScope = true;
                    break;
                }
            }
            if (!validScope) {
                addValidationMessage("Assumption scope must be one of: component, asset, deal, portfolio, fund");
            }
        }
        
        // Validate type against allowed values
        if (type != null) {
            String[] validTypes = {
                "fixed", "distribution", "table", "expression"
            };
            boolean validType = false;
            for (String valid : validTypes) {
                if (valid.equals(type)) {
                    validType = true;
                    break;
                }
            }
            if (!validType) {
                addValidationMessage("Assumption type must be one of: fixed, distribution, table, expression");
            }
        }
        
        // Validate distribution if present
        if ("distribution".equals(type)) {
            if (distribution == null || distribution.isEmpty()) {
                addValidationMessage("Assumption with type 'distribution' must have distribution specification");
            } else {
                if (!distribution.containsKey("type") || distribution.get("type") == null) {
                    addValidationMessage("Assumption distribution must have type");
                }
                if (!distribution.containsKey("parameters") || distribution.get("parameters") == null) {
                    addValidationMessage("Assumption distribution must have parameters");
                }
                
                // Validate distribution type
                String distType = (String) distribution.get("type");
                if (distType != null) {
                    String[] validDistTypes = {
                        "normal", "uniform", "triangular", "lognormal", "beta", "custom"
                    };
                    boolean validDistType = false;
                    for (String valid : validDistTypes) {
                        if (valid.equals(distType)) {
                            validDistType = true;
                            break;
                        }
                    }
                    if (!validDistType) {
                        addValidationMessage("Assumption distribution type must be one of: normal, uniform, triangular, lognormal, beta, custom");
                    }
                }
            }
        }
        
        // Validate time series if present
        if (timeSeries != null) {
            for (int i = 0; i < timeSeries.size(); i++) {
                Map<String, Object> timePoint = timeSeries.get(i);
                if (!timePoint.containsKey("date") || timePoint.get("date") == null) {
                    addValidationMessage("Assumption timeSeries item " + i + " must have date");
                }
                if (!timePoint.containsKey("value") || timePoint.get("value") == null) {
                    addValidationMessage("Assumption timeSeries item " + i + " must have value");
                }
            }
        }
        
        // Validate that value exists for non-distribution types (unless using template)
        if (!"distribution".equals(type) && value == null && (timeSeries == null || timeSeries.isEmpty()) && template == null) {
            addValidationMessage("Assumption must have either value, timeSeries data, or template");
        }
    }
}