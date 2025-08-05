package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Stream entities.
 * 
 * Corresponds to ontology/behavior/stream.schema.yaml and contains all
 * normalized stream properties plus schema metadata needed for execution.
 */
public class IRStream extends IRNode {
    private String description;
    private String scope;
    private String category;
    private String subType;
    private Map<String, Object> schedule;
    private Object amount; // Can be number or calculator object
    private Map<String, Object> growth;
    private List<String> tags;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRStream() {
        super();
        this.tags = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/behavior/stream.schema.yaml");
    }
    
    public IRStream(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/behavior/stream.schema.yaml");
        this.tags = new ArrayList<>();
    }
    
    // Getters and setters for stream-specific properties
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
    
    public String getCategory() { return category; }
    public void setCategory(String category) { 
        this.category = category;
        setProperty("category", category);
    }
    
    public String getSubType() { return subType; }
    public void setSubType(String subType) { 
        this.subType = subType;
        setProperty("subType", subType);
    }
    
    public Map<String, Object> getSchedule() { return schedule; }
    public void setSchedule(Map<String, Object> schedule) { 
        this.schedule = schedule;
        setProperty("schedule", schedule);
    }
    
    public Object getAmount() { return amount; }
    public void setAmount(Object amount) { 
        this.amount = amount;
        setProperty("amount", amount);
    }
    
    public Map<String, Object> getGrowth() { return growth; }
    public void setGrowth(Map<String, Object> growth) { 
        this.growth = growth;
        setProperty("growth", growth);
    }
    
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { 
        this.tags = tags != null ? tags : new ArrayList<>();
        setProperty("tags", this.tags);
    }
    
    public void addTag(String tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
            setProperty("tags", this.tags);
        }
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("scope", scope);
        json.put("category", category);
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // SubType (optional but recommended)
        if (subType != null) {
            json.put("subType", subType);
        }
        
        // Schedule - required for engine
        if (schedule != null) {
            json.set("schedule", objectMapper.valueToTree(schedule));
        }
        
        // Amount - required for engine (can be number or calculator)
        if (amount != null) {
            if (amount instanceof Number) {
                json.put("amount", ((Number) amount).doubleValue());
            } else {
                json.set("amount", objectMapper.valueToTree(amount));
            }
        }
        
        // Growth model (optional)
        if (growth != null) {
            json.set("growth", objectMapper.valueToTree(growth));
        }
        
        // Tags (optional but useful for filtering)
        if (!tags.isEmpty()) {
            json.set("tags", objectMapper.valueToTree(tags));
        }
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Stream id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Stream name is required");
        }
        
        if (scope == null || scope.trim().isEmpty()) {
            addValidationMessage("Stream scope is required");
        }
        
        if (category == null || category.trim().isEmpty()) {
            addValidationMessage("Stream category is required");
        }
        
        if (schedule == null) {
            addValidationMessage("Stream schedule is required");
        }
        
        if (amount == null) {
            addValidationMessage("Stream amount is required");
        }
        
        // Validate scope against allowed values
        if (scope != null) {
            String[] validScopes = {"component", "asset", "deal", "portfolio", "fund"};
            boolean validScope = false;
            for (String valid : validScopes) {
                if (valid.equals(scope)) {
                    validScope = true;
                    break;
                }
            }
            if (!validScope) {
                addValidationMessage("Stream scope must be one of: component, asset, deal, portfolio, fund");
            }
        }
        
        // Validate category against allowed values
        if (category != null) {
            String[] validCategories = {"Revenue", "Expense", "OtherIncome"};
            boolean validCategory = false;
            for (String valid : validCategories) {
                if (valid.equals(category)) {
                    validCategory = true;
                    break;
                }
            }
            if (!validCategory) {
                addValidationMessage("Stream category must be one of: Revenue, Expense, OtherIncome");
            }
        }
        
        // Validate subType against allowed values (if present)
        if (subType != null) {
            String[] validSubTypes = {"Operating", "Financing", "Tax", "CapEx", "Fee", "Other"};
            boolean validSubType = false;
            for (String valid : validSubTypes) {
                if (valid.equals(subType)) {
                    validSubType = true;
                    break;
                }
            }
            if (!validSubType) {
                addValidationMessage("Stream subType must be one of: Operating, Financing, Tax, CapEx, Fee, Other");
            }
        }
        
        // Validate schedule structure if present
        if (schedule != null) {
            String scheduleType = (String) schedule.get("type");
            if (scheduleType == null) {
                addValidationMessage("Stream schedule must have a 'type' field");
            } else {
                switch (scheduleType) {
                    case "oneTime":
                        if (!schedule.containsKey("date")) {
                            addValidationMessage("oneTime schedule must have a 'date' field");
                        }
                        break;
                    case "recurring":
                    case "dateBounded":
                        if (!schedule.containsKey("recurrenceRule") || !schedule.containsKey("startDate")) {
                            addValidationMessage(scheduleType + " schedule must have 'recurrenceRule' and 'startDate' fields");
                        }
                        if ("dateBounded".equals(scheduleType) && !schedule.containsKey("endDate")) {
                            addValidationMessage("dateBounded schedule must have an 'endDate' field");
                        }
                        break;
                    default:
                        addValidationMessage("Schedule type must be one of: oneTime, recurring, dateBounded");
                }
            }
        }
    }
}