package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Asset entities.
 * 
 * Corresponds to ontology/entity/asset.schema.yaml and contains all
 * normalized asset properties plus schema metadata needed for execution.
 */
public class IRAsset extends IRNode {
    private String dealId;
    private String category;
    private String description;
    private Map<String, Object> location;
    private Map<String, Object> attributes;
    private Map<String, Object> stateConfig;
    private List<Map<String, Object>> history;
    
    // Related entities
    private List<IRStream> streams;
    private List<String> contractIds;
    private List<String> componentIds;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRAsset() {
        super();
        this.streams = new ArrayList<>();
        this.contractIds = new ArrayList<>();
        this.componentIds = new ArrayList<>();
        this.history = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/asset.schema.yaml");
    }
    
    public IRAsset(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/asset.schema.yaml");
        this.streams = new ArrayList<>();
        this.contractIds = new ArrayList<>();
        this.componentIds = new ArrayList<>();
        this.history = new ArrayList<>();
    }
    
    // Getters and setters for asset-specific properties
    public String getDealId() { return dealId; }
    public void setDealId(String dealId) { 
        this.dealId = dealId;
        setProperty("dealId", dealId);
        if (dealId != null) {
            addDependency(dealId);
        }
    }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { 
        this.category = category;
        setProperty("category", category);
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public Map<String, Object> getLocation() { return location; }
    public void setLocation(Map<String, Object> location) { 
        this.location = location;
        setProperty("location", location);
    }
    
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { 
        this.attributes = attributes;
        setProperty("attributes", attributes);
    }
    
    public Map<String, Object> getStateConfig() { return stateConfig; }
    public void setStateConfig(Map<String, Object> stateConfig) { 
        this.stateConfig = stateConfig;
        setProperty("stateConfig", stateConfig);
    }
    
    public List<Map<String, Object>> getHistory() { return history; }
    public void setHistory(List<Map<String, Object>> history) { 
        this.history = history;
        setProperty("history", history);
    }
    
    public List<IRStream> getStreams() { return streams; }
    public void addStream(IRStream stream) { 
        this.streams.add(stream);
        addDependency(stream.getId());
    }
    
    public List<String> getContractIds() { return contractIds; }
    public void addContractId(String contractId) { 
        this.contractIds.add(contractId);
        addDependency(contractId);
    }
    
    public List<String> getComponentIds() { return componentIds; }
    public void addComponentId(String componentId) { 
        this.componentIds.add(componentId);
        addDependency(componentId);
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("dealId", dealId);
        json.put("category", category);
        
        // Optional fields
        if (description != null) {
            json.put("description", description);
        }
        
        if (location != null) {
            json.set("location", objectMapper.valueToTree(location));
        }
        
        if (attributes != null) {
            json.set("attributes", objectMapper.valueToTree(attributes));
        }
        
        if (stateConfig != null) {
            json.set("stateConfig", objectMapper.valueToTree(stateConfig));
        }
        
        if (history != null && !history.isEmpty()) {
            json.set("history", objectMapper.valueToTree(history));
        }
        
        // Contract references
        if (!contractIds.isEmpty()) {
            json.set("contracts", objectMapper.valueToTree(contractIds));
        }
        
        // Component references
        if (!componentIds.isEmpty()) {
            json.set("components", objectMapper.valueToTree(componentIds));
        }
        
        // Streams
        if (!streams.isEmpty()) {
            ArrayNode streamsArray = objectMapper.createArrayNode();
            for (IRStream stream : streams) {
                streamsArray.add(stream.toEngineJson());
            }
            json.set("streams", streamsArray);
        }
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Asset id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Asset name is required");
        }
        
        if (dealId == null || dealId.trim().isEmpty()) {
            addValidationMessage("Asset dealId is required");
        }
        
        if (category == null || category.trim().isEmpty()) {
            addValidationMessage("Asset category is required");
        }
        
        // Validate category against allowed values
        if (category != null) {
            String[] validCategories = {
                "real_estate", "financial_asset", "physical_asset", 
                "legal_right", "operating_entity", "contract_bundle", 
                "mixed", "other"
            };
            boolean validCategory = false;
            for (String valid : validCategories) {
                if (valid.equals(category)) {
                    validCategory = true;
                    break;
                }
            }
            if (!validCategory) {
                addValidationMessage("Asset category must be one of: real_estate, financial_asset, physical_asset, legal_right, operating_entity, contract_bundle, mixed, other");
            }
        }
        
        // Validate nested streams
        for (IRStream stream : streams) {
            stream.validate();
            if (!stream.isValid()) {
                addValidationMessage("Stream " + stream.getId() + " has validation errors");
            }
        }
    }
}