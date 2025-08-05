package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * AST node representing a CFDL fund definition.
 * Corresponds to ontology/entity/fund.schema.yaml
 * 
 * A fund is a collection vehicle that holds Portfolios (or Deals directly),
 * manages capital commitments, and applies fund-level streams
 * (e.g. management fees, carried interest). Funds have participants
 * (GPs, LPs), optional fundType, and lifecycle state.
 */
public class FundNode extends ASTNode {
    private String description;
    private String fundType;
    private List<String> portfolioIds;
    private List<String> streamIds;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public FundNode() {
        super();
        this.portfolioIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    public FundNode(String id, String name) {
        super(id, name);
        this.portfolioIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/fund.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (fundType != null) json.put("fundType", fundType);
        
        // Portfolio references
        if (!portfolioIds.isEmpty()) {
            json.set("portfolios", objectMapper.valueToTree(portfolioIds));
        }
        
        // Stream references
        if (!streamIds.isEmpty()) {
            json.set("streams", objectMapper.valueToTree(streamIds));
        }
        
        // Add any additional properties (participants, stateConfig, metadata, etc.)
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
    
    public String getFundType() { return fundType; }
    public void setFundType(String fundType) { this.fundType = fundType; }
    
    public List<String> getPortfolioIds() { return portfolioIds; }
    public void setPortfolioIds(List<String> portfolioIds) { this.portfolioIds = portfolioIds; }
    public void addPortfolioId(String portfolioId) { this.portfolioIds.add(portfolioId); }
    
    public List<String> getStreamIds() { return streamIds; }
    public void setStreamIds(List<String> streamIds) { this.streamIds = streamIds; }
    public void addStreamId(String streamId) { this.streamIds.add(streamId); }
}