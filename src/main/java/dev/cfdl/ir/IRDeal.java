package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Deal entities.
 * 
 * Corresponds to ontology/entity/deal.schema.yaml and contains all
 * normalized deal properties plus schema metadata needed for execution.
 */
public class IRDeal extends IRNode {
    private String dealType;
    private String currency;
    private String entryDate;
    private String exitDate;
    private String analysisStart;
    private Integer holdingPeriodYears;
    private Map<String, Object> calendar;
    private List<Map<String, Object>> participants;
    private String capitalStackId;
    private Map<String, Object> stateConfig;
    
    // Related entities
    private List<IRAsset> assets;
    private List<IRStream> streams;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRDeal() {
        super();
        this.assets = new ArrayList<>();
        this.streams = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/deal.schema.yaml");
    }
    
    public IRDeal(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/deal.schema.yaml");
        this.assets = new ArrayList<>();
        this.streams = new ArrayList<>();
        this.participants = new ArrayList<>();
    }
    
    // Getters and setters for deal-specific properties
    public String getDealType() { return dealType; }
    public void setDealType(String dealType) { 
        this.dealType = dealType;
        setProperty("dealType", dealType);
    }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { 
        this.currency = currency;
        setProperty("currency", currency);
    }
    
    public String getEntryDate() { return entryDate; }
    public void setEntryDate(String entryDate) { 
        this.entryDate = entryDate;
        setProperty("entryDate", entryDate);
    }
    
    public String getExitDate() { return exitDate; }
    public void setExitDate(String exitDate) { 
        this.exitDate = exitDate;
        setProperty("exitDate", exitDate);
    }
    
    public String getAnalysisStart() { return analysisStart; }
    public void setAnalysisStart(String analysisStart) { 
        this.analysisStart = analysisStart;
        setProperty("analysisStart", analysisStart);
    }
    
    public Integer getHoldingPeriodYears() { return holdingPeriodYears; }
    public void setHoldingPeriodYears(Integer holdingPeriodYears) { 
        this.holdingPeriodYears = holdingPeriodYears;
        setProperty("holdingPeriodYears", holdingPeriodYears);
    }
    
    public Map<String, Object> getCalendar() { return calendar; }
    public void setCalendar(Map<String, Object> calendar) { 
        this.calendar = calendar;
        setProperty("calendar", calendar);
    }
    
    public List<Map<String, Object>> getParticipants() { return participants; }
    public void setParticipants(List<Map<String, Object>> participants) { 
        this.participants = participants;
        setProperty("participants", participants);
    }
    
    public String getCapitalStackId() { return capitalStackId; }
    public void setCapitalStackId(String capitalStackId) { 
        this.capitalStackId = capitalStackId;
        setProperty("capitalStackId", capitalStackId);
        if (capitalStackId != null) {
            addDependency(capitalStackId);
        }
    }
    
    public Map<String, Object> getStateConfig() { return stateConfig; }
    public void setStateConfig(Map<String, Object> stateConfig) { 
        this.stateConfig = stateConfig;
        setProperty("stateConfig", stateConfig);
    }
    
    public List<IRAsset> getAssets() { return assets; }
    public void addAsset(IRAsset asset) { 
        this.assets.add(asset);
        addDependency(asset.getId());
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
        json.put("dealType", dealType);
        json.put("currency", currency);
        json.put("entryDate", entryDate);
        json.put("exitDate", exitDate);
        json.put("analysisStart", analysisStart);
        json.put("holdingPeriodYears", holdingPeriodYears);
        
        // Calendar configuration
        if (calendar != null) {
            json.set("calendar", objectMapper.valueToTree(calendar));
        }
        
        // Participants
        if (participants != null && !participants.isEmpty()) {
            json.set("participants", objectMapper.valueToTree(participants));
        }
        
        // Capital stack reference
        if (capitalStackId != null) {
            json.put("capitalStackId", capitalStackId);
        }
        
        // State configuration
        if (stateConfig != null) {
            json.set("stateConfig", objectMapper.valueToTree(stateConfig));
        }
        
        // Assets
        if (!assets.isEmpty()) {
            ArrayNode assetsArray = objectMapper.createArrayNode();
            for (IRAsset asset : assets) {
                assetsArray.add(asset.toEngineJson());
            }
            json.set("assets", assetsArray);
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
            addValidationMessage("Deal id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Deal name is required");
        }
        
        if (dealType == null || dealType.trim().isEmpty()) {
            addValidationMessage("Deal dealType is required");
        }
        
        if (currency == null || currency.trim().isEmpty()) {
            addValidationMessage("Deal currency is required");
        }
        
        if (entryDate == null || entryDate.trim().isEmpty()) {
            addValidationMessage("Deal entryDate is required");
        }
        
        if (exitDate == null || exitDate.trim().isEmpty()) {
            addValidationMessage("Deal exitDate is required");
        }
        
        if (analysisStart == null || analysisStart.trim().isEmpty()) {
            addValidationMessage("Deal analysisStart is required");
        }
        
        if (holdingPeriodYears == null || holdingPeriodYears <= 0) {
            addValidationMessage("Deal holdingPeriodYears is required and must be positive");
        }
        
        // Validate nested assets and streams
        for (IRAsset asset : assets) {
            asset.validate();
            if (!asset.isValid()) {
                addValidationMessage("Asset " + asset.getId() + " has validation errors");
            }
        }
        
        for (IRStream stream : streams) {
            stream.validate();
            if (!stream.isValid()) {
                addValidationMessage("Stream " + stream.getId() + " has validation errors");
            }
        }
    }
}