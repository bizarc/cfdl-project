package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * AST node representing a CFDL deal definition.
 * Corresponds to ontology/entity/deal.schema.yaml
 */
public class DealNode extends ASTNode {
    private String dealType;
    private String entryDate;
    private String exitDate;
    private String analysisStart;
    private String currency;
    private Double holdingPeriodYears;
    private List<String> assetIds;
    private List<String> streamIds;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public DealNode() {
        super();
        this.assetIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    public DealNode(String id, String name) {
        super(id, name);
        this.assetIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/deal.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (dealType != null) json.put("dealType", dealType);
        if (entryDate != null) json.put("entryDate", entryDate);
        if (exitDate != null) json.put("exitDate", exitDate);
        if (analysisStart != null) json.put("analysisStart", analysisStart);
        if (currency != null) json.put("currency", currency);
        if (holdingPeriodYears != null) json.put("holdingPeriodYears", holdingPeriodYears);
        
        if (!assetIds.isEmpty()) {
            var assetsArray = json.putArray("assets");
            assetIds.forEach(assetsArray::add);
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
    public String getDealType() { return dealType; }
    public void setDealType(String dealType) { this.dealType = dealType; }
    
    public String getEntryDate() { return entryDate; }
    public void setEntryDate(String entryDate) { this.entryDate = entryDate; }
    
    public String getExitDate() { return exitDate; }
    public void setExitDate(String exitDate) { this.exitDate = exitDate; }
    
    public String getAnalysisStart() { return analysisStart; }
    public void setAnalysisStart(String analysisStart) { this.analysisStart = analysisStart; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public Double getHoldingPeriodYears() { return holdingPeriodYears; }
    public void setHoldingPeriodYears(Double holdingPeriodYears) { this.holdingPeriodYears = holdingPeriodYears; }
    
    public List<String> getAssetIds() { return assetIds; }
    public void setAssetIds(List<String> assetIds) { this.assetIds = assetIds; }
    public void addAssetId(String assetId) { this.assetIds.add(assetId); }
    
    public List<String> getStreamIds() { return streamIds; }
    public void setStreamIds(List<String> streamIds) { this.streamIds = streamIds; }
    public void addStreamId(String streamId) { this.streamIds.add(streamId); }
}