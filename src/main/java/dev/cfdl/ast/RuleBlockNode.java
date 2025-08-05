package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AST node representing a CFDL rule block definition.
 * Corresponds to ontology/behavior/rule-block.schema.yaml
 * 
 * Rule blocks define conditional or event-driven operations that fire during model execution.
 * They can be triggered by a schedule, an external event, or an evaluated condition,
 * and execute one or more actions (e.g., assigning values, logging, or invoking external services).
 */
public class RuleBlockNode extends ASTNode {
    private String description;
    private String scope;
    private String scheduleId;
    private String eventTriggerId;
    private String condition;
    private Object action; // Can be String or List<String>
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public RuleBlockNode() {
        super();
    }
    
    public RuleBlockNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/behavior/rule-block.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (scope != null) json.put("scope", scope);
        if (scheduleId != null) json.put("schedule", scheduleId);
        if (eventTriggerId != null) json.put("eventTrigger", eventTriggerId);
        if (condition != null) json.put("condition", condition);
        
        // Action can be string or array
        if (action != null) {
            json.set("action", objectMapper.valueToTree(action));
        }
        
        // Add any additional properties (metadata, etc.)
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
    
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }
    
    public String getEventTriggerId() { return eventTriggerId; }
    public void setEventTriggerId(String eventTriggerId) { this.eventTriggerId = eventTriggerId; }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    
    public Object getAction() { return action; }
    public void setAction(Object action) { this.action = action; }
}