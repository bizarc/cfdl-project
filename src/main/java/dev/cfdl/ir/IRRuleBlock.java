package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

/**
 * Intermediate Representation for CFDL Rule Block entities.
 * 
 * Corresponds to ontology/behavior/rule-block.schema.yaml and contains all
 * normalized rule block properties plus schema metadata needed for execution.
 * 
 * Rule blocks define conditional or event-driven operations that fire during model execution
 * with triggers based on schedules, events, or conditions.
 */
public class IRRuleBlock extends IRNode {
    private String description;
    private String scope;
    private String scheduleId;
    private String eventTriggerId;
    private String condition;
    private Object action; // Can be String or List<String>
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRRuleBlock() {
        super();
        this.setSchemaType("https://cfdl.dev/ontology/behavior/rule-block.schema.yaml");
    }
    
    public IRRuleBlock(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/behavior/rule-block.schema.yaml");
    }
    
    // Getters and setters for rule block-specific properties
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public String getScope() { return scope; }
    public void setScope(String scope) { 
        this.scope = scope;
        setProperty("scope", scope);
    }
    
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { 
        this.scheduleId = scheduleId;
        setProperty("scheduleId", scheduleId);
        
        // Add schedule reference as dependency
        if (scheduleId != null) {
            addDependency(scheduleId);
        }
    }
    
    public String getEventTriggerId() { return eventTriggerId; }
    public void setEventTriggerId(String eventTriggerId) { 
        this.eventTriggerId = eventTriggerId;
        setProperty("eventTriggerId", eventTriggerId);
        
        // Add event trigger reference as dependency
        if (eventTriggerId != null) {
            addDependency(eventTriggerId);
        }
    }
    
    public String getCondition() { return condition; }
    public void setCondition(String condition) { 
        this.condition = condition;
        setProperty("condition", condition);
    }
    
    public Object getAction() { return action; }
    public void setAction(Object action) { 
        this.action = action;
        setProperty("action", action);
    }
    
    /**
     * Determine if this rule block is triggered by a schedule.
     */
    public boolean hasScheduleTrigger() {
        return scheduleId != null && !scheduleId.trim().isEmpty();
    }
    
    /**
     * Determine if this rule block is triggered by an event.
     */
    public boolean hasEventTrigger() {
        return eventTriggerId != null && !eventTriggerId.trim().isEmpty();
    }
    
    /**
     * Determine if this rule block has a condition.
     */
    public boolean hasCondition() {
        return condition != null && !condition.trim().isEmpty();
    }
    
    /**
     * Determine if this rule block has multiple actions.
     */
    public boolean hasMultipleActions() {
        return action instanceof List;
    }
    
    /**
     * Get the number of actions in this rule block.
     */
    public int getActionCount() {
        if (action == null) return 0;
        if (action instanceof List) {
            return ((List<?>) action).size();
        }
        return 1; // Single string action
    }
    
    /**
     * Get the trigger type for this rule block.
     */
    public String getTriggerType() {
        if (hasScheduleTrigger()) return "schedule";
        if (hasEventTrigger()) return "event";
        if (hasCondition()) return "condition";
        return "none";
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("scope", scope);
        
        // Action is required
        if (action != null) {
            json.set("action", objectMapper.valueToTree(action));
        }
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // Trigger references
        if (scheduleId != null) {
            json.put("schedule", scheduleId);
        }
        
        if (eventTriggerId != null) {
            json.put("eventTrigger", eventTriggerId);
        }
        
        // Optional condition
        if (condition != null) {
            json.put("condition", condition);
        }
        
        // Add computed metadata for engine convenience
        json.put("hasScheduleTrigger", hasScheduleTrigger());
        json.put("hasEventTrigger", hasEventTrigger());
        json.put("hasCondition", hasCondition());
        json.put("hasMultipleActions", hasMultipleActions());
        json.put("actionCount", getActionCount());
        json.put("triggerType", getTriggerType());
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Rule block id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Rule block name is required");
        }
        
        if (scope == null || scope.trim().isEmpty()) {
            addValidationMessage("Rule block scope is required");
        }
        
        if (action == null) {
            addValidationMessage("Rule block action is required");
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
                addValidationMessage("Rule block scope must be one of: component, asset, deal, portfolio, fund");
            }
        }
        
        // Validate that at least one trigger type is specified (schedule, event, or condition)
        if (!hasScheduleTrigger() && !hasEventTrigger() && !hasCondition()) {
            addValidationMessage("Rule block must have at least one trigger: schedule, eventTrigger, or condition");
        }
        
        // Validate action content
        if (action != null) {
            if (action instanceof String) {
                String actionStr = (String) action;
                if (actionStr.trim().isEmpty()) {
                    addValidationMessage("Rule block action cannot be empty");
                }
            } else if (action instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> actionList = (List<String>) action;
                if (actionList.isEmpty()) {
                    addValidationMessage("Rule block action list cannot be empty");
                }
                for (int i = 0; i < actionList.size(); i++) {
                    String actionItem = actionList.get(i);
                    if (actionItem == null || actionItem.trim().isEmpty()) {
                        addValidationMessage("Rule block action " + i + " cannot be null or empty");
                    }
                }
            } else {
                addValidationMessage("Rule block action must be either a string or a list of strings");
            }
        }
    }
}