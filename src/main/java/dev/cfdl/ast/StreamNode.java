package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AST node representing a CFDL stream definition.
 * Corresponds to ontology/behavior/stream.schema.yaml
 */
public class StreamNode extends ASTNode {
    private String scope;
    private String category;
    private String subType;
    private String scheduleId;
    private Object amount; // Can be number or reference
    private String amountType;
    private String description;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public StreamNode() {
        super();
    }
    
    public StreamNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/behavior/stream.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (scope != null) json.put("scope", scope);
        if (category != null) json.put("category", category);
        if (subType != null) json.put("subType", subType);
        if (scheduleId != null) json.put("schedule", scheduleId);
        if (amount != null) json.putPOJO("amount", amount);
        if (amountType != null) json.put("amountType", amountType);
        
        // Add any additional properties
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getSubType() { return subType; }
    public void setSubType(String subType) { this.subType = subType; }
    
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    
    public Object getAmount() { return amount; }
    public void setAmount(Object amount) { this.amount = amount; }
    
    public String getAmountType() { return amountType; }
    public void setAmountType(String amountType) { this.amountType = amountType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}