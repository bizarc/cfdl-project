package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AST node representing a CFDL capital stack definition.
 * Corresponds to ontology/entity/capital-stack.schema.yaml
 * 
 * Capital stacks define who contributed capital to a deal and how available cash
 * is distributed back via waterfall rules. They separate contributions (participants)
 * from distribution logic (waterfall).
 */
public class CapitalStackNode extends ASTNode {
    private String waterfallId;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public CapitalStackNode() {
        super();
    }
    
    public CapitalStackNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/capital-stack.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (waterfallId != null) json.put("waterfallId", waterfallId);
        
        // Add any additional properties (participants, metadata, etc.)
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getWaterfallId() { return waterfallId; }
    public void setWaterfallId(String waterfallId) { this.waterfallId = waterfallId; }
}