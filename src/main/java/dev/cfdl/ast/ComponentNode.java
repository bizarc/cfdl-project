package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * AST node representing a CFDL component definition.
 * Corresponds to ontology/entity/component.schema.yaml
 * 
 * Components are cash-flow-producing sub-units of assets (e.g., individual office units,
 * individual loans in a pool, tenant spaces, revenue-generating machines).
 */
public class ComponentNode extends ASTNode {
    private String assetId;
    private String componentType;
    private String description;
    private List<String> streamIds;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public ComponentNode() {
        super();
        this.streamIds = new ArrayList<>();
    }
    
    public ComponentNode(String id, String name) {
        super(id, name);
        this.streamIds = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/component.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (assetId != null) json.put("assetId", assetId);
        if (componentType != null) json.put("componentType", componentType);
        
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
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }
    
    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<String> getStreamIds() { return streamIds; }
    public void setStreamIds(List<String> streamIds) { this.streamIds = streamIds; }
    public void addStreamId(String streamId) { this.streamIds.add(streamId); }
}