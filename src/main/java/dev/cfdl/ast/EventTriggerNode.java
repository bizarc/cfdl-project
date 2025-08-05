package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AST node representing a CFDL event trigger definition.
 * Corresponds to ontology/temporal/event-trigger.schema.yaml
 * 
 * Event triggers define event-based triggers for rule blocks or logic blocks.
 * They support assumption changes, value threshold crossings, external events, or custom expressions.
 */
public class EventTriggerNode extends ASTNode {
    private String type;
    private String assumptionId;
    private String streamId;
    private String operator;
    private Double threshold;
    private String externalEventName;
    private String expression;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public EventTriggerNode() {
        super();
    }
    
    public EventTriggerNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/temporal/event-trigger.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (type != null) json.put("type", type);
        if (assumptionId != null) json.put("assumptionId", assumptionId);
        if (streamId != null) json.put("streamId", streamId);
        if (operator != null) json.put("operator", operator);
        if (threshold != null) json.put("threshold", threshold);
        if (externalEventName != null) json.put("externalEventName", externalEventName);
        if (expression != null) json.put("expression", expression);
        
        // Add any additional properties (metadata, etc.)
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getAssumptionId() { return assumptionId; }
    public void setAssumptionId(String assumptionId) { this.assumptionId = assumptionId; }
    
    public String getStreamId() { return streamId; }
    public void setStreamId(String streamId) { this.streamId = streamId; }
    
    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }
    
    public Double getThreshold() { return threshold; }
    public void setThreshold(Double threshold) { this.threshold = threshold; }
    
    public String getExternalEventName() { return externalEventName; }
    public void setExternalEventName(String externalEventName) { this.externalEventName = externalEventName; }
    
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
}