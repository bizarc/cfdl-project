package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * AST node representing a CFDL contract definition.
 * Corresponds to ontology/entity/contract.schema.yaml
 * 
 * Contracts represent legal or financial agreements tied to deals, assets, or components
 * (e.g., leases, PPAs, service agreements). They carry payment streams, parties, and lifecycle states.
 */
public class ContractNode extends ASTNode {
    private String dealId;
    private String assetId;
    private String componentId;
    private String contractType;
    private String description;
    private String startDate;
    private String endDate;
    private List<String> streamIds;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public ContractNode() {
        super();
        this.streamIds = new ArrayList<>();
    }
    
    public ContractNode(String id, String name) {
        super(id, name);
        this.streamIds = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/contract.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (dealId != null) json.put("dealId", dealId);
        if (assetId != null) json.put("assetId", assetId);
        if (componentId != null) json.put("componentId", componentId);
        if (contractType != null) json.put("contractType", contractType);
        if (startDate != null) json.put("startDate", startDate);
        if (endDate != null) json.put("endDate", endDate);
        
        if (!streamIds.isEmpty()) {
            var streamsArray = json.putArray("streams");
            streamIds.forEach(streamsArray::add);
        }
        
        // Add any additional properties (parties, terms, stateConfig, metadata, etc.)
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
    
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }
    
    public String getComponentId() { return componentId; }
    public void setComponentId(String componentId) { this.componentId = componentId; }
    
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    
    public List<String> getStreamIds() { return streamIds; }
    public void setStreamIds(List<String> streamIds) { this.streamIds = streamIds; }
    public void addStreamId(String streamId) { this.streamIds.add(streamId); }
}