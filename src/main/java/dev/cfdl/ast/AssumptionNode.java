package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AST node representing a CFDL assumption definition.
 * Corresponds to ontology/behavior/assumption.schema.yaml
 * 
 * Assumptions represent economic or operational inputs used in modeling,
 * such as growth rates, default probabilities, or cost escalators.
 * They support fixed values, statistical distributions, time series, and expressions.
 */
public class AssumptionNode extends ASTNode {
    private String category;
    private String scope;
    private String type;
    private String unit;
    private String template;
    private String source;
    private String marketDataRef;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public AssumptionNode() {
        super();
    }
    
    public AssumptionNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/behavior/assumption.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (category != null) json.put("category", category);
        if (scope != null) json.put("scope", scope);
        if (type != null) json.put("type", type);
        if (unit != null) json.put("unit", unit);
        if (template != null) json.put("template", template);
        if (source != null) json.put("source", source);
        if (marketDataRef != null) json.put("marketDataRef", marketDataRef);
        
        // Add any additional properties (value, distribution, timeSeries, overrides, metadata, etc.)
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getMarketDataRef() { return marketDataRef; }
    public void setMarketDataRef(String marketDataRef) { this.marketDataRef = marketDataRef; }
}