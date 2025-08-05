package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Contract entities.
 * 
 * Corresponds to ontology/entity/contract.schema.yaml and contains all
 * normalized contract properties plus schema metadata needed for execution.
 * 
 * Contracts represent legal or financial agreements that generate cash flows
 * (leases, PPAs, service agreements, loan agreements, etc.).
 */
public class IRContract extends IRNode {
    private String dealId;
    private String assetId;
    private String componentId;
    private String contractType;
    private String description;
    private String startDate;
    private String endDate;
    private List<Map<String, Object>> parties;
    private Map<String, Object> terms;
    private Map<String, Object> stateConfig;
    
    // Related entities
    private List<IRStream> streams;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRContract() {
        super();
        this.parties = new ArrayList<>();
        this.streams = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/contract.schema.yaml");
    }
    
    public IRContract(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/contract.schema.yaml");
        this.parties = new ArrayList<>();
        this.streams = new ArrayList<>();
    }
    
    // Getters and setters for contract-specific properties
    public String getDealId() { return dealId; }
    public void setDealId(String dealId) { 
        this.dealId = dealId;
        setProperty("dealId", dealId);
        if (dealId != null) {
            addDependency(dealId);
        }
    }
    
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { 
        this.assetId = assetId;
        setProperty("assetId", assetId);
        if (assetId != null) {
            addDependency(assetId);
        }
    }
    
    public String getComponentId() { return componentId; }
    public void setComponentId(String componentId) { 
        this.componentId = componentId;
        setProperty("componentId", componentId);
        if (componentId != null) {
            addDependency(componentId);
        }
    }
    
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { 
        this.contractType = contractType;
        setProperty("contractType", contractType);
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { 
        this.startDate = startDate;
        setProperty("startDate", startDate);
    }
    
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { 
        this.endDate = endDate;
        setProperty("endDate", endDate);
    }
    
    public List<Map<String, Object>> getParties() { return parties; }
    public void setParties(List<Map<String, Object>> parties) { 
        this.parties = parties;
        setProperty("parties", parties);
        
        // Add party dependencies
        if (parties != null) {
            for (Map<String, Object> party : parties) {
                String partyId = (String) party.get("partyId");
                if (partyId != null) {
                    addDependency(partyId);
                }
            }
        }
    }
    
    public Map<String, Object> getTerms() { return terms; }
    public void setTerms(Map<String, Object> terms) { 
        this.terms = terms;
        setProperty("terms", terms);
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
        json.put("dealId", dealId);
        json.put("contractType", contractType);
        json.put("startDate", startDate);
        json.put("endDate", endDate);
        
        // Parties are required
        if (parties != null && !parties.isEmpty()) {
            json.set("parties", objectMapper.valueToTree(parties));
        }
        
        // Optional fields
        if (description != null) {
            json.put("description", description);
        }
        
        if (assetId != null) {
            json.put("assetId", assetId);
        }
        
        if (componentId != null) {
            json.put("componentId", componentId);
        }
        
        if (terms != null) {
            json.set("terms", objectMapper.valueToTree(terms));
        }
        
        if (stateConfig != null) {
            json.set("stateConfig", objectMapper.valueToTree(stateConfig));
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
            addValidationMessage("Contract id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Contract name is required");
        }
        
        if (dealId == null || dealId.trim().isEmpty()) {
            addValidationMessage("Contract dealId is required");
        }
        
        if (contractType == null || contractType.trim().isEmpty()) {
            addValidationMessage("Contract contractType is required");
        }
        
        if (startDate == null || startDate.trim().isEmpty()) {
            addValidationMessage("Contract startDate is required");
        }
        
        if (endDate == null || endDate.trim().isEmpty()) {
            addValidationMessage("Contract endDate is required");
        }
        
        if (parties == null || parties.isEmpty()) {
            addValidationMessage("Contract parties are required");
        }
        
        // Validate contractType against allowed values
        if (contractType != null) {
            String[] validContractTypes = {
                "lease", "power-purchase-agreement", "loan-agreement", 
                "service-agreement", "royalty-agreement", "management-agreement", 
                "sale-leaseback", "other"
            };
            boolean validContractType = false;
            for (String valid : validContractTypes) {
                if (valid.equals(contractType)) {
                    validContractType = true;
                    break;
                }
            }
            if (!validContractType) {
                addValidationMessage("Contract contractType must be one of: lease, power-purchase-agreement, loan-agreement, service-agreement, royalty-agreement, management-agreement, sale-leaseback, other");
            }
        }
        
        // Validate parties structure
        if (parties != null) {
            for (int i = 0; i < parties.size(); i++) {
                Map<String, Object> party = parties.get(i);
                if (!party.containsKey("partyId") || party.get("partyId") == null) {
                    addValidationMessage("Contract party " + i + " must have partyId");
                }
                if (!party.containsKey("role") || party.get("role") == null) {
                    addValidationMessage("Contract party " + i + " must have role");
                }
            }
        }
        
        // Validate date order
        if (startDate != null && endDate != null) {
            // Simple string comparison should work for YYYY-MM-DD format
            if (startDate.compareTo(endDate) >= 0) {
                addValidationMessage("Contract endDate must be after startDate");
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