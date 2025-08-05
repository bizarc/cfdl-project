package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Party entities.
 * 
 * Corresponds to ontology/entity/party.schema.yaml and contains all
 * normalized party properties plus schema metadata needed for execution.
 * 
 * Parties represent persons or organizations that participate in deals,
 * contracts, or other financial/legal arrangements.
 */
public class IRParty extends IRNode {
    private String partyType;
    private String description;
    private Map<String, Object> contactInfo;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRParty() {
        super();
        this.setSchemaType("https://cfdl.dev/ontology/entity/party.schema.yaml");
    }
    
    public IRParty(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/party.schema.yaml");
    }
    
    // Getters and setters for party-specific properties
    public String getPartyType() { return partyType; }
    public void setPartyType(String partyType) { 
        this.partyType = partyType;
        setProperty("partyType", partyType);
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public Map<String, Object> getContactInfo() { return contactInfo; }
    public void setContactInfo(Map<String, Object> contactInfo) { 
        this.contactInfo = contactInfo;
        setProperty("contactInfo", contactInfo);
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("partyType", partyType);
        
        // Optional fields
        if (description != null) {
            json.put("description", description);
        }
        
        if (contactInfo != null) {
            json.set("contactInfo", objectMapper.valueToTree(contactInfo));
        }
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Party id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Party name is required");
        }
        
        if (partyType == null || partyType.trim().isEmpty()) {
            addValidationMessage("Party partyType is required");
        }
        
        // Validate partyType against allowed values
        if (partyType != null) {
            String[] validPartyTypes = {
                "individual", "organization", "government", 
                "trust", "fund", "other"
            };
            boolean validPartyType = false;
            for (String valid : validPartyTypes) {
                if (valid.equals(partyType)) {
                    validPartyType = true;
                    break;
                }
            }
            if (!validPartyType) {
                addValidationMessage("Party partyType must be one of: individual, organization, government, trust, fund, other");
            }
        }
        
        // Validate contactInfo structure if present
        if (contactInfo != null) {
            // Validate email format if present
            if (contactInfo.containsKey("email")) {
                String email = (String) contactInfo.get("email");
                if (email != null && !email.contains("@")) {
                    addValidationMessage("Party email must be a valid email format");
                }
            }
            
            // Validate address structure if present
            if (contactInfo.containsKey("address")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> address = (Map<String, Object>) contactInfo.get("address");
                if (address != null) {
                    if (!address.containsKey("streetAddress") || 
                        !address.containsKey("city") || 
                        !address.containsKey("country")) {
                        addValidationMessage("Party address must include streetAddress, city, and country");
                    }
                }
            }
        }
    }
}