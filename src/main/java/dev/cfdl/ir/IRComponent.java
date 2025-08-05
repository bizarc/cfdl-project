package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Component entities.
 * 
 * Corresponds to ontology/entity/component.schema.yaml and contains all
 * normalized component properties plus schema metadata needed for execution.
 * 
 * Components are the most atomic level for cash flow generation and represent
 * individual cash-flow-producing sub-units (e.g., individual loans, office units).
 */
public class IRComponent extends IRNode {
    private String assetId;
    private String componentType;
    private String description;
    private Map<String, Object> attributes;
    private Map<String, Object> stateConfig;
    
    // Related entities
    private List<IRStream> streams;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRComponent() {
        super();
        this.streams = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/component.schema.yaml");
    }
    
    public IRComponent(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/component.schema.yaml");
        this.streams = new ArrayList<>();
    }
    
    // Getters and setters for component-specific properties
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { 
        this.assetId = assetId;
        setProperty("assetId", assetId);
        if (assetId != null) {
            addDependency(assetId);
        }
    }
    
    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { 
        this.componentType = componentType;
        setProperty("componentType", componentType);
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
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
    
    public List<IRStream> getStreams() { return streams; }
    public void addStream(IRStream stream) { 
        this.streams.add(stream);
        addDependency(stream.getId());
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("assetId", assetId);
        
        // Optional fields
        if (componentType != null) {
            json.put("componentType", componentType);
        }
        
        if (description != null) {
            json.put("description", description);
        }
        
        if (attributes != null) {
            json.set("attributes", objectMapper.valueToTree(attributes));
        }
        
        if (stateConfig != null) {
            json.set("stateConfig", objectMapper.valueToTree(stateConfig));
        }
        
        // Streams - this is critical for atomic cash flow generation
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
            addValidationMessage("Component id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Component name is required");
        }
        
        if (assetId == null || assetId.trim().isEmpty()) {
            addValidationMessage("Component assetId is required");
        }
        
        // Validate nested streams
        for (IRStream stream : streams) {
            stream.validate();
            if (!stream.isValid()) {
                addValidationMessage("Stream " + stream.getId() + " has validation errors");
            }
            
            // Ensure component streams have component scope
            if (!"component".equals(stream.getScope())) {
                addValidationMessage("Component stream " + stream.getId() + " must have scope 'component'");
            }
        }
    }
    
    /**
     * Gets the total cash flow from all component streams for a given period.
     * This is used for rolling up to asset level.
     */
    public double getTotalCashFlow(String periodStart, String periodEnd) {
        // This would be implemented by the engine, but shows the concept
        // of how component-level cash flows aggregate
        return streams.stream()
            .mapToDouble(stream -> {
                // Engine would calculate actual cash flow for the period
                // based on stream schedule, amount, and growth
                return 0.0; // Placeholder
            })
            .sum();
    }
}