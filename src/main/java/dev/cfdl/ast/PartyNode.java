package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * AST node representing a CFDL party definition.
 * Corresponds to ontology/entity/party.schema.yaml
 * 
 * Parties represent persons or organizations that participate in deals,
 * contracts, or other financial/legal arrangements (sponsors, lenders, operators, etc.).
 */
public class PartyNode extends ASTNode {
    private String partyType;
    private String description;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public PartyNode() {
        super();
    }
    
    public PartyNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/party.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (partyType != null) json.put("partyType", partyType);
        if (description != null) json.put("description", description);
        
        // Add any additional properties (contactInfo, metadata, etc.)
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getPartyType() { return partyType; }
    public void setPartyType(String partyType) { this.partyType = partyType; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}