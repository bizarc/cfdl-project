package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * AST node representing a CFDL portfolio definition.
 * Corresponds to ontology/entity/portfolio.schema.yaml
 * 
 * A portfolio is a collection of Deals grouped for aggregated analysis,
 * reporting, and governance. Portfolios can define portfolio-level
 * cash-flow streams (e.g., management fees) and lifecycle states.
 */
public class PortfolioNode extends ASTNode {
    private String description;
    private List<String> dealIds;
    private List<String> streamIds;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public PortfolioNode() {
        super();
        this.dealIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    public PortfolioNode(String id, String name) {
        super(id, name);
        this.dealIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/portfolio.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        
        // Deal references
        if (!dealIds.isEmpty()) {
            json.set("deals", objectMapper.valueToTree(dealIds));
        }
        
        // Stream references
        if (!streamIds.isEmpty()) {
            json.set("streams", objectMapper.valueToTree(streamIds));
        }
        
        // Add any additional properties (stateConfig, metadata, etc.)
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
    
    public List<String> getDealIds() { return dealIds; }
    public void setDealIds(List<String> dealIds) { this.dealIds = dealIds; }
    public void addDealId(String dealId) { this.dealIds.add(dealId); }
    
    public List<String> getStreamIds() { return streamIds; }
    public void setStreamIds(List<String> streamIds) { this.streamIds = streamIds; }
    public void addStreamId(String streamId) { this.streamIds.add(streamId); }
}