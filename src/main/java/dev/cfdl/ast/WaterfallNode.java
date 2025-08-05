package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AST node representing a CFDL waterfall definition.
 * Corresponds to ontology/result/waterfall.schema.yaml
 * 
 * Waterfalls define ordered, tiered distribution of available cash.
 * Cash-in contributions (participants) are defined in the capital-stack;
 * each tier here governs how cash-out is allocated, via explicit splits
 * or by inheriting participant proportions.
 */
public class WaterfallNode extends ASTNode {
    private String description;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public WaterfallNode() {
        super();
    }
    
    public WaterfallNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/result/waterfall.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        
        // Add any additional properties (tiers, metadata, etc.)
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}