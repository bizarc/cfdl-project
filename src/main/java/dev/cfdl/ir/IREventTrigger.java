package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Intermediate Representation for CFDL Event Trigger entities.
 * 
 * Corresponds to ontology/temporal/event-trigger.schema.yaml and contains all
 * normalized event trigger properties plus schema metadata needed for execution.
 * 
 * Event triggers define event-based triggers that support assumption changes,
 * stream threshold crossings, external events, or custom expressions.
 */
public class IREventTrigger extends IRNode {
    private String type;
    private String assumptionId;
    private String streamId;
    private String operator;
    private Double threshold;
    private String externalEventName;
    private String expression;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IREventTrigger() {
        super();
        this.setSchemaType("https://cfdl.dev/ontology/temporal/event-trigger.schema.yaml");
    }
    
    public IREventTrigger(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/temporal/event-trigger.schema.yaml");
    }
    
    // Getters and setters for event trigger-specific properties
    public String getType() { return type; }
    public void setType(String type) { 
        this.type = type;
        setProperty("type", type);
    }
    
    public String getAssumptionId() { return assumptionId; }
    public void setAssumptionId(String assumptionId) { 
        this.assumptionId = assumptionId;
        setProperty("assumptionId", assumptionId);
        
        // Add assumption reference as dependency
        if (assumptionId != null) {
            addDependency(assumptionId);
        }
    }
    
    public String getStreamId() { return streamId; }
    public void setStreamId(String streamId) { 
        this.streamId = streamId;
        setProperty("streamId", streamId);
        
        // Add stream reference as dependency
        if (streamId != null) {
            addDependency(streamId);
        }
    }
    
    public String getOperator() { return operator; }
    public void setOperator(String operator) { 
        this.operator = operator;
        setProperty("operator", operator);
    }
    
    public Double getThreshold() { return threshold; }
    public void setThreshold(Double threshold) { 
        this.threshold = threshold;
        setProperty("threshold", threshold);
    }
    
    public String getExternalEventName() { return externalEventName; }
    public void setExternalEventName(String externalEventName) { 
        this.externalEventName = externalEventName;
        setProperty("externalEventName", externalEventName);
    }
    
    public String getExpression() { return expression; }
    public void setExpression(String expression) { 
        this.expression = expression;
        setProperty("expression", expression);
    }
    
    /**
     * Determine if this is an assumption change trigger.
     */
    public boolean isAssumptionChangeTrigger() {
        return "assumption_change".equals(type);
    }
    
    /**
     * Determine if this is a stream threshold trigger.
     */
    public boolean isStreamThresholdTrigger() {
        return "stream_threshold".equals(type);
    }
    
    /**
     * Determine if this is an external event trigger.
     */
    public boolean isExternalEventTrigger() {
        return "external_event".equals(type);
    }
    
    /**
     * Determine if this is a custom expression trigger.
     */
    public boolean isCustomTrigger() {
        return "custom".equals(type);
    }
    
    /**
     * Get a human-readable description of this trigger.
     */
    public String getTriggerDescription() {
        if (isAssumptionChangeTrigger()) {
            return "Assumption '" + assumptionId + "' changes";
        } else if (isStreamThresholdTrigger()) {
            return "Stream '" + streamId + "' " + operator + " " + threshold;
        } else if (isExternalEventTrigger()) {
            return "External event '" + externalEventName + "' occurs";
        } else if (isCustomTrigger()) {
            return "Custom condition: " + expression;
        }
        return "Unknown trigger type: " + type;
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required field for engine
        json.put("type", type);
        
        // Optional fields based on trigger type
        if (assumptionId != null) {
            json.put("assumptionId", assumptionId);
        }
        
        if (streamId != null) {
            json.put("streamId", streamId);
        }
        
        if (operator != null) {
            json.put("operator", operator);
        }
        
        if (threshold != null) {
            json.put("threshold", threshold);
        }
        
        if (externalEventName != null) {
            json.put("externalEventName", externalEventName);
        }
        
        if (expression != null) {
            json.put("expression", expression);
        }
        
        // Add computed metadata for engine convenience
        json.put("isAssumptionChangeTrigger", isAssumptionChangeTrigger());
        json.put("isStreamThresholdTrigger", isStreamThresholdTrigger());
        json.put("isExternalEventTrigger", isExternalEventTrigger());
        json.put("isCustomTrigger", isCustomTrigger());
        json.put("triggerDescription", getTriggerDescription());
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (type == null || type.trim().isEmpty()) {
            addValidationMessage("Event trigger type is required");
        }
        
        // Validate type against allowed values
        if (type != null) {
            String[] validTypes = {
                "assumption_change", "stream_threshold", "external_event", "custom"
            };
            boolean validType = false;
            for (String valid : validTypes) {
                if (valid.equals(type)) {
                    validType = true;
                    break;
                }
            }
            if (!validType) {
                addValidationMessage("Event trigger type must be one of: assumption_change, stream_threshold, external_event, custom");
            }
        }
        
        // Validate type-specific required fields
        if ("assumption_change".equals(type)) {
            if (assumptionId == null || assumptionId.trim().isEmpty()) {
                addValidationMessage("Event trigger of type 'assumption_change' must have assumptionId");
            }
        } else if ("stream_threshold".equals(type)) {
            if (streamId == null || streamId.trim().isEmpty()) {
                addValidationMessage("Event trigger of type 'stream_threshold' must have streamId");
            }
            if (operator == null || operator.trim().isEmpty()) {
                addValidationMessage("Event trigger of type 'stream_threshold' must have operator");
            } else {
                String[] validOperators = {"eq", "ne", "lt", "lte", "gt", "gte"};
                boolean validOperator = false;
                for (String valid : validOperators) {
                    if (valid.equals(operator)) {
                        validOperator = true;
                        break;
                    }
                }
                if (!validOperator) {
                    addValidationMessage("Event trigger operator must be one of: eq, ne, lt, lte, gt, gte");
                }
            }
            if (threshold == null) {
                addValidationMessage("Event trigger of type 'stream_threshold' must have threshold");
            }
        } else if ("external_event".equals(type)) {
            if (externalEventName == null || externalEventName.trim().isEmpty()) {
                addValidationMessage("Event trigger of type 'external_event' must have externalEventName");
            }
        } else if ("custom".equals(type)) {
            if (expression == null || expression.trim().isEmpty()) {
                addValidationMessage("Event trigger of type 'custom' must have expression");
            }
        }
    }
}