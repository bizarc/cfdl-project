package dev.cfdl.ir;

import dev.cfdl.ast.PartyNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRParty and PartyNode integration.
 * 
 * Tests the party-level functionality needed for deal participants,
 * sponsors, lenders, and other financial counterparties.
 */
public class IRPartyTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testPartyNodeCreation() {
        PartyNode partyNode = new PartyNode("party-sponsor-001", "Premium Real Estate Sponsor LLC");
        partyNode.setPartyType("organization");
        partyNode.setDescription("Leading commercial real estate sponsor and operator");
        partyNode.setLineNumber(25);
        partyNode.setColumnNumber(4);
        
        // Add contact info
        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("email", "contact@premiumrealestate.com");
        contactInfo.put("phone", "+1-555-123-4567");
        
        Map<String, Object> address = new HashMap<>();
        address.put("streetAddress", "123 Finance Street");
        address.put("city", "New York");
        address.put("state", "NY");
        address.put("postalCode", "10001");
        address.put("country", "USA");
        contactInfo.put("address", address);
        
        partyNode.setProperty("contactInfo", contactInfo);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("industry", "commercial_real_estate");
        metadata.put("yearsInBusiness", 15);
        metadata.put("aum", 2500000000L); // $2.5B AUM
        partyNode.setProperty("metadata", metadata);
        
        // Verify party node
        assertEquals("party-sponsor-001", partyNode.getId());
        assertEquals("Premium Real Estate Sponsor LLC", partyNode.getName());
        assertEquals("organization", partyNode.getPartyType());
        assertEquals("Leading commercial real estate sponsor and operator", partyNode.getDescription());
        assertEquals(25, partyNode.getLineNumber());
        assertEquals(4, partyNode.getColumnNumber());
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = partyNode.toJson();
        assertEquals("party-sponsor-001", json.get("id").asText());
        assertEquals("organization", json.get("partyType").asText());
        assertTrue(json.has("contactInfo"));
        if (json.get("contactInfo").has("email")) {
            assertEquals("contact@premiumrealestate.com", json.get("contactInfo").get("email").asText());
        }
        assertTrue(json.has("metadata"));
        if (json.get("metadata").has("industry")) {
            assertEquals("commercial_real_estate", json.get("metadata").get("industry").asText());
        }
    }
    
    @Test
    void testIRPartyTransformation() {
        // Create party AST node
        PartyNode partyNode = new PartyNode("party-lender-001", "Metropolitan Bank");
        partyNode.setPartyType("organization");
        partyNode.setDescription("Major commercial real estate lender");
        
        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("email", "cre-lending@metrobank.com");
        
        Map<String, Object> address = new HashMap<>();
        address.put("streetAddress", "456 Banking Plaza");
        address.put("city", "Chicago");
        address.put("state", "IL");
        address.put("postalCode", "60601");
        address.put("country", "USA");
        contactInfo.put("address", address);
        
        partyNode.setProperty("contactInfo", contactInfo);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(partyNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRParty irParty = (IRParty) result.getIrNodes().get(0);
        
        // Verify IR party
        assertEquals("party-lender-001", irParty.getId());
        assertEquals("Metropolitan Bank", irParty.getName());
        assertEquals("organization", irParty.getPartyType());
        assertEquals("Major commercial real estate lender", irParty.getDescription());
        
        // Verify contact info
        assertNotNull(irParty.getContactInfo());
        assertEquals("cre-lending@metrobank.com", irParty.getContactInfo().get("email"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> addressResult = (Map<String, Object>) irParty.getContactInfo().get("address");
        assertNotNull(addressResult);
        assertEquals("Chicago", addressResult.get("city"));
        assertEquals("USA", addressResult.get("country"));
        
        // Verify metadata enrichment
        assertEquals("party", irParty.getSchemaMetadata("entityType"));
        assertEquals(Boolean.TRUE, irParty.getSchemaMetadata("hasContactInfo"));
        assertEquals("organization", irParty.getSchemaMetadata("partyClassification"));
        assertNotNull(irParty.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRPartyValidation() {
        // Test valid party
        IRParty validParty = new IRParty("party-valid", "Valid Party");
        validParty.setPartyType("organization");
        validParty.setDescription("A valid party for testing");
        
        validParty.validate();
        assertTrue(validParty.isValid());
        assertEquals(0, validParty.getValidationMessages().size());
        
        // Test invalid party - missing required fields
        IRParty invalidParty = new IRParty();
        // Missing id, name, partyType
        
        invalidParty.validate();
        assertFalse(invalidParty.isValid());
        assertTrue(invalidParty.getValidationMessages().size() >= 3);
        assertTrue(invalidParty.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidParty.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidParty.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("partyType is required")));
    }
    
    @Test
    void testPartyTypeValidation() {
        IRParty party = new IRParty("party-type-test", "Type Test Party");
        
        // Valid party type
        party.setPartyType("organization");
        party.validate();
        assertTrue(party.isValid());
        
        // Invalid party type
        party = new IRParty("party-type-invalid", "Invalid Type Party");
        party.setPartyType("invalid_type");
        party.validate();
        assertFalse(party.isValid());
        assertTrue(party.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("partyType must be one of")));
        
        // Test all valid party types
        String[] validTypes = {"individual", "organization", "government", "trust", "fund", "other"};
        for (String validType : validTypes) {
            IRParty testParty = new IRParty("party-" + validType, "Test " + validType);
            testParty.setPartyType(validType);
            testParty.validate();
            assertTrue(testParty.isValid(), "Party type '" + validType + "' should be valid");
        }
    }
    
    @Test
    void testContactInfoValidation() {
        IRParty party = new IRParty("party-contact", "Contact Test Party");
        party.setPartyType("organization");
        
        // Invalid email format
        Map<String, Object> invalidContactInfo = new HashMap<>();
        invalidContactInfo.put("email", "invalid-email-format");
        party.setContactInfo(invalidContactInfo);
        
        party.validate();
        assertFalse(party.isValid());
        assertTrue(party.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("email must be a valid email format")));
        
        // Valid email format
        party = new IRParty("party-contact-valid", "Valid Contact Party");
        party.setPartyType("organization");
        Map<String, Object> validContactInfo = new HashMap<>();
        validContactInfo.put("email", "valid@example.com");
        party.setContactInfo(validContactInfo);
        
        party.validate();
        assertTrue(party.isValid());
        
        // Invalid address (missing required fields)
        party = new IRParty("party-address-invalid", "Invalid Address Party");
        party.setPartyType("organization");
        Map<String, Object> contactWithInvalidAddress = new HashMap<>();
        Map<String, Object> invalidAddress = new HashMap<>();
        invalidAddress.put("streetAddress", "123 Main St");
        // Missing city and country
        contactWithInvalidAddress.put("address", invalidAddress);
        party.setContactInfo(contactWithInvalidAddress);
        
        party.validate();
        assertFalse(party.isValid());
        assertTrue(party.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("address must include streetAddress, city, and country")));
    }
    
    @Test
    void testPartyEngineJsonOutput() {
        // Create party with complete data
        IRParty party = new IRParty("party-engine", "Engine Test Party");
        party.setPartyType("organization");
        party.setDescription("Test party for engine JSON output");
        
        // Add contact info
        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("email", "engine@test.com");
        contactInfo.put("phone", "+1-555-999-0000");
        
        Map<String, Object> address = new HashMap<>();
        address.put("streetAddress", "789 Engine St");
        address.put("city", "Test City");
        address.put("state", "TS");
        address.put("postalCode", "12345");
        address.put("country", "USA");
        contactInfo.put("address", address);
        
        party.setContactInfo(contactInfo);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = party.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("partyType"));
        assertEquals("party-engine", json.get("id").asText());
        assertEquals("organization", json.get("partyType").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertTrue(json.has("contactInfo"));
        
        // Verify contact info structure
        com.fasterxml.jackson.databind.JsonNode contactJson = json.get("contactInfo");
        assertEquals("engine@test.com", contactJson.get("email").asText());
        assertEquals("+1-555-999-0000", contactJson.get("phone").asText());
        
        com.fasterxml.jackson.databind.JsonNode addressJson = contactJson.get("address");
        assertEquals("Test City", addressJson.get("city").asText());
        assertEquals("USA", addressJson.get("country").asText());
    }
    
    @Test
    void testDifferentPartyTypes() {
        // Test individual party
        IRParty individual = new IRParty("party-individual", "John Smith");
        individual.setPartyType("individual");
        individual.setDescription("High net worth individual investor");
        
        individual.validate();
        assertTrue(individual.isValid());
        assertEquals("individual", individual.getPartyType());
        
        // Test fund party
        IRParty fund = new IRParty("party-fund", "Opportunity Zone Fund I");
        fund.setPartyType("fund");
        fund.setDescription("Qualified Opportunity Zone investment fund");
        
        fund.validate();
        assertTrue(fund.isValid());
        assertEquals("fund", fund.getPartyType());
        
        // Test government party
        IRParty government = new IRParty("party-govt", "City Housing Authority");
        government.setPartyType("government");
        government.setDescription("Municipal housing development authority");
        
        government.validate();
        assertTrue(government.isValid());
        assertEquals("government", government.getPartyType());
        
        // Verify all parties have proper metadata when processed through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new java.util.ArrayList<>();
        
        // Create AST nodes (would normally come from parser)
        dev.cfdl.ast.PartyNode individualNode = new dev.cfdl.ast.PartyNode("party-individual", "John Smith");
        individualNode.setPartyType("individual");
        astNodes.add(individualNode);
        
        dev.cfdl.ast.PartyNode fundNode = new dev.cfdl.ast.PartyNode("party-fund", "Opportunity Zone Fund I");
        fundNode.setPartyType("fund");
        astNodes.add(fundNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(2, result.getNodeCount());
        
        // Verify both parties have proper metadata
        for (IRNode irNode : result.getIrNodes()) {
            IRParty irParty = (IRParty) irNode;
            assertEquals("party", irParty.getSchemaMetadata("entityType"));
            assertNotNull(irParty.getSchemaMetadata("partyClassification"));
        }
    }
}