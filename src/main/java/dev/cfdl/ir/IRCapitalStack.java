package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Capital Stack entities.
 * 
 * Corresponds to ontology/entity/capital-stack.schema.yaml and contains all
 * normalized capital stack properties plus schema metadata needed for execution.
 * 
 * Capital stacks define who contributed capital to a deal and how available cash
 * is distributed back via waterfall rules.
 */
public class IRCapitalStack extends IRNode {
    private List<Map<String, Object>> participants;
    private String waterfallId;
    private IRWaterfall waterfall; // Linked waterfall entity
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRCapitalStack() {
        super();
        this.participants = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/capital-stack.schema.yaml");
    }
    
    public IRCapitalStack(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/capital-stack.schema.yaml");
        this.participants = new ArrayList<>();
    }
    
    // Getters and setters for capital stack-specific properties
    public List<Map<String, Object>> getParticipants() { return participants; }
    public void setParticipants(List<Map<String, Object>> participants) { 
        this.participants = participants;
        setProperty("participants", participants);
        
        // Add participant dependencies
        if (participants != null) {
            for (Map<String, Object> participant : participants) {
                String partyId = (String) participant.get("partyId");
                if (partyId != null) {
                    addDependency(partyId);
                }
            }
        }
    }
    
    public String getWaterfallId() { return waterfallId; }
    public void setWaterfallId(String waterfallId) { 
        this.waterfallId = waterfallId;
        setProperty("waterfallId", waterfallId);
        if (waterfallId != null) {
            addDependency(waterfallId);
        }
    }
    
    public IRWaterfall getWaterfall() { return waterfall; }
    public void setWaterfall(IRWaterfall waterfall) { 
        this.waterfall = waterfall;
        if (waterfall != null) {
            this.waterfallId = waterfall.getId();
            addDependency(waterfall.getId());
        }
    }
    
    /**
     * Calculate total capital raised from all participants.
     */
    public double getTotalCapital() {
        if (participants == null) return 0.0;
        
        return participants.stream()
            .mapToDouble(p -> {
                Object amount = p.get("amount");
                if (amount instanceof Number) {
                    return ((Number) amount).doubleValue();
                }
                return 0.0;
            })
            .sum();
    }
    
    /**
     * Get the number of capital participants.
     */
    public int getParticipantCount() {
        return participants != null ? participants.size() : 0;
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        
        // Participants are required
        if (participants != null && !participants.isEmpty()) {
            json.set("participants", objectMapper.valueToTree(participants));
        }
        
        // Waterfall reference
        if (waterfallId != null) {
            json.put("waterfallId", waterfallId);
        }
        
        // Include linked waterfall if available
        if (waterfall != null) {
            json.set("waterfall", waterfall.toEngineJson());
        }
        
        // Add computed fields for engine convenience
        json.put("totalCapital", getTotalCapital());
        json.put("participantCount", getParticipantCount());
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Capital stack id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Capital stack name is required");
        }
        
        if (participants == null || participants.isEmpty()) {
            addValidationMessage("Capital stack participants are required");
        }
        
        if (waterfallId == null || waterfallId.trim().isEmpty()) {
            addValidationMessage("Capital stack waterfallId is required");
        }
        
        // Validate participants structure
        if (participants != null) {
            for (int i = 0; i < participants.size(); i++) {
                Map<String, Object> participant = participants.get(i);
                
                if (!participant.containsKey("partyId") || participant.get("partyId") == null) {
                    addValidationMessage("Capital stack participant " + i + " must have partyId");
                }
                
                if (!participant.containsKey("amount") || participant.get("amount") == null) {
                    addValidationMessage("Capital stack participant " + i + " must have amount");
                }
                
                // Validate amount is a positive number
                Object amount = participant.get("amount");
                if (amount instanceof Number) {
                    double amountValue = ((Number) amount).doubleValue();
                    if (amountValue <= 0) {
                        addValidationMessage("Capital stack participant " + i + " amount must be positive");
                    }
                } else if (amount != null) {
                    addValidationMessage("Capital stack participant " + i + " amount must be a number");
                }
            }
        }
        
        // Validate linked waterfall if present
        if (waterfall != null) {
            waterfall.validate();
            if (!waterfall.isValid()) {
                addValidationMessage("Waterfall " + waterfall.getId() + " has validation errors");
            }
        }
    }
}