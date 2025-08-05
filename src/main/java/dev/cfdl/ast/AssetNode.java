package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * AST node representing a CFDL asset definition.
 * Corresponds to ontology/entity/asset.schema.yaml
 */
public class AssetNode extends ASTNode {
    private String dealId;
    private String category;
    private String description;
    private String state;
    private List<String> componentIds;
    private List<String> streamIds;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public AssetNode() {
        super();
        this.componentIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    public AssetNode(String id, String name) {
        super(id, name);
        this.componentIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/asset.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (dealId != null) json.put("dealId", dealId);
        if (category != null) json.put("category", category);
        if (description != null) json.put("description", description);
        if (state != null) json.put("state", state);
        
        if (!componentIds.isEmpty()) {
            var componentsArray = json.putArray("components");
            componentIds.forEach(componentsArray::add);
        }
        
        if (!streamIds.isEmpty()) {
            var streamsArray = json.putArray("streams");
            streamIds.forEach(streamsArray::add);
        }
        
        // Add any additional properties
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getDealId() { return dealId; }
    public void setDealId(String dealId) { this.dealId = dealId; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public List<String> getComponentIds() { return componentIds; }
    public void setComponentIds(List<String> componentIds) { this.componentIds = componentIds; }
    public void addComponentId(String componentId) { this.componentIds.add(componentId); }
    
    public List<String> getStreamIds() { return streamIds; }
    public void setStreamIds(List<String> streamIds) { this.streamIds = streamIds; }
    public void addStreamId(String streamId) { this.streamIds.add(streamId); }
}