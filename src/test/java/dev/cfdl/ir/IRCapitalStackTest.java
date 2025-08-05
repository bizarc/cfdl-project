package dev.cfdl.ir;

import dev.cfdl.ast.CapitalStackNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRCapitalStack and CapitalStackNode integration.
 * 
 * Tests the capital stack functionality needed for financing structure
 * modeling, including participants and waterfall distribution logic.
 */
public class IRCapitalStackTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testCapitalStackNodeCreation() {
        CapitalStackNode capitalStackNode = new CapitalStackNode("capital-stack-001", "Office Building Capital Stack");
        capitalStackNode.setWaterfallId("waterfall-pref-return");
        capitalStackNode.setLineNumber(30);
        capitalStackNode.setColumnNumber(8);
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> sponsor = new HashMap<>();
        sponsor.put("partyId", "party-sponsor");
        sponsor.put("amount", 5000000.0); // $5M equity
        participants.add(sponsor);
        
        Map<String, Object> investor = new HashMap<>();
        investor.put("partyId", "party-institutional-investor");
        investor.put("amount", 15000000.0); // $15M equity
        participants.add(investor);
        
        Map<String, Object> lender = new HashMap<>();
        lender.put("partyId", "party-bank-lender");
        lender.put("amount", 30000000.0); // $30M debt
        participants.add(lender);
        
        capitalStackNode.setProperty("participants", participants);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("ltv", 0.6); // 60% LTV
        metadata.put("totalCapitalization", 50000000.0); // $50M total
        metadata.put("debtToEquity", 1.5); // 1.5:1 debt to equity
        capitalStackNode.setProperty("metadata", metadata);
        
        // Verify capital stack node
        assertEquals("capital-stack-001", capitalStackNode.getId());
        assertEquals("Office Building Capital Stack", capitalStackNode.getName());
        assertEquals("waterfall-pref-return", capitalStackNode.getWaterfallId());
        assertEquals(30, capitalStackNode.getLineNumber());
        assertEquals(8, capitalStackNode.getColumnNumber());
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = capitalStackNode.toJson();
        assertEquals("capital-stack-001", json.get("id").asText());
        assertEquals("waterfall-pref-return", json.get("waterfallId").asText());
        assertTrue(json.has("participants"));
        if (json.get("participants").size() > 0) {
            assertEquals(3, json.get("participants").size());
        }
        assertTrue(json.has("metadata"));
        if (json.get("metadata").has("ltv")) {
            assertEquals(0.6, json.get("metadata").get("ltv").asDouble());
        }
    }
    
    @Test
    void testIRCapitalStackTransformation() {
        // Create capital stack AST node
        CapitalStackNode capitalStackNode = new CapitalStackNode("capital-stack-002", "Solar Project Capital Stack");
        capitalStackNode.setWaterfallId("waterfall-solar-distribution");
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> developer = new HashMap<>();
        developer.put("partyId", "party-solar-developer");
        developer.put("amount", 2000000.0); // $2M equity
        participants.add(developer);
        
        Map<String, Object> taxEquityInvestor = new HashMap<>();
        taxEquityInvestor.put("partyId", "party-tax-equity-investor");
        taxEquityInvestor.put("amount", 8000000.0); // $8M tax equity
        participants.add(taxEquityInvestor);
        
        Map<String, Object> constructionLender = new HashMap<>();
        constructionLender.put("partyId", "party-construction-lender");
        constructionLender.put("amount", 15000000.0); // $15M construction loan
        participants.add(constructionLender);
        
        capitalStackNode.setProperty("participants", participants);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(capitalStackNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRCapitalStack irCapitalStack = (IRCapitalStack) result.getIrNodes().get(0);
        
        // Verify IR capital stack
        assertEquals("capital-stack-002", irCapitalStack.getId());
        assertEquals("Solar Project Capital Stack", irCapitalStack.getName());
        assertEquals("waterfall-solar-distribution", irCapitalStack.getWaterfallId());
        
        // Verify participants
        assertNotNull(irCapitalStack.getParticipants());
        assertEquals(3, irCapitalStack.getParticipants().size());
        assertEquals("party-solar-developer", irCapitalStack.getParticipants().get(0).get("partyId"));
        assertEquals(2000000.0, irCapitalStack.getParticipants().get(0).get("amount"));
        
        // Verify computed values
        assertEquals(25000000.0, irCapitalStack.getTotalCapital()); // $25M total
        assertEquals(3, irCapitalStack.getParticipantCount());
        
        // Verify dependencies
        assertTrue(irCapitalStack.getDependencies().contains("waterfall-solar-distribution"));
        assertTrue(irCapitalStack.getDependencies().contains("party-solar-developer"));
        assertTrue(irCapitalStack.getDependencies().contains("party-tax-equity-investor"));
        assertTrue(irCapitalStack.getDependencies().contains("party-construction-lender"));
        
        // Verify metadata enrichment
        assertEquals("capitalStack", irCapitalStack.getSchemaMetadata("entityType"));
        assertEquals(3, irCapitalStack.getSchemaMetadata("participantCount"));
        assertEquals(25000000.0, irCapitalStack.getSchemaMetadata("totalCapital"));
        assertEquals(Boolean.TRUE, irCapitalStack.getSchemaMetadata("hasWaterfall"));
        assertNotNull(irCapitalStack.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRCapitalStackValidation() {
        // Test valid capital stack
        IRCapitalStack validCapitalStack = new IRCapitalStack("capital-stack-valid", "Valid Capital Stack");
        validCapitalStack.setWaterfallId("waterfall-test");
        
        List<Map<String, Object>> participants = new ArrayList<>();
        Map<String, Object> participant = new HashMap<>();
        participant.put("partyId", "party-test");
        participant.put("amount", 1000000.0);
        participants.add(participant);
        validCapitalStack.setParticipants(participants);
        
        validCapitalStack.validate();
        assertTrue(validCapitalStack.isValid());
        assertEquals(0, validCapitalStack.getValidationMessages().size());
        
        // Test invalid capital stack - missing required fields
        IRCapitalStack invalidCapitalStack = new IRCapitalStack();
        // Missing id, name, waterfallId, participants
        
        invalidCapitalStack.validate();
        assertFalse(invalidCapitalStack.isValid());
        assertTrue(invalidCapitalStack.getValidationMessages().size() >= 4);
        assertTrue(invalidCapitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidCapitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidCapitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("waterfallId is required")));
        assertTrue(invalidCapitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participants are required")));
    }
    
    @Test
    void testParticipantValidation() {
        IRCapitalStack capitalStack = new IRCapitalStack("capital-stack-participant-test", "Participant Test");
        capitalStack.setWaterfallId("waterfall-test");
        
        // Invalid participants - missing partyId
        List<Map<String, Object>> invalidParticipants = new ArrayList<>();
        Map<String, Object> invalidParticipant = new HashMap<>();
        invalidParticipant.put("amount", 1000000.0);
        // Missing partyId
        invalidParticipants.add(invalidParticipant);
        capitalStack.setParticipants(invalidParticipants);
        
        capitalStack.validate();
        assertFalse(capitalStack.isValid());
        assertTrue(capitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participant 0 must have partyId")));
        
        // Invalid participants - missing amount
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        // Missing amount
        invalidParticipants.add(invalidParticipant);
        capitalStack.setParticipants(invalidParticipants);
        
        capitalStack.validate();
        assertFalse(capitalStack.isValid());
        assertTrue(capitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participant 0 must have amount")));
        
        // Invalid participants - negative amount
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        invalidParticipant.put("amount", -1000.0); // Negative amount
        invalidParticipants.add(invalidParticipant);
        capitalStack.setParticipants(invalidParticipants);
        
        capitalStack.validate();
        assertFalse(capitalStack.isValid());
        assertTrue(capitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participant 0 amount must be positive")));
        
        // Invalid participants - non-numeric amount
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        invalidParticipant.put("amount", "not-a-number"); // Non-numeric
        invalidParticipants.add(invalidParticipant);
        capitalStack.setParticipants(invalidParticipants);
        
        capitalStack.validate();
        assertFalse(capitalStack.isValid());
        assertTrue(capitalStack.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participant 0 amount must be a number")));
    }
    
    @Test
    void testCapitalStackCalculations() {
        IRCapitalStack capitalStack = new IRCapitalStack("capital-stack-calc", "Calculation Test");
        capitalStack.setWaterfallId("waterfall-test");
        
        // Test empty participants
        assertEquals(0.0, capitalStack.getTotalCapital());
        assertEquals(0, capitalStack.getParticipantCount());
        
        // Add participants with various amounts
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> participant1 = new HashMap<>();
        participant1.put("partyId", "party-1");
        participant1.put("amount", 5000000.0); // $5M
        participants.add(participant1);
        
        Map<String, Object> participant2 = new HashMap<>();
        participant2.put("partyId", "party-2");
        participant2.put("amount", 3500000.0); // $3.5M
        participants.add(participant2);
        
        Map<String, Object> participant3 = new HashMap<>();
        participant3.put("partyId", "party-3");
        participant3.put("amount", 1500000.0); // $1.5M
        participants.add(participant3);
        
        capitalStack.setParticipants(participants);
        
        // Verify calculations
        assertEquals(10000000.0, capitalStack.getTotalCapital()); // $10M total
        assertEquals(3, capitalStack.getParticipantCount());
    }
    
    @Test
    void testCapitalStackEngineJsonOutput() {
        // Create capital stack with complete data
        IRCapitalStack capitalStack = new IRCapitalStack("capital-stack-engine", "Engine Test Capital Stack");
        capitalStack.setWaterfallId("waterfall-engine");
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> equity = new HashMap<>();
        equity.put("partyId", "party-equity-investor");
        equity.put("amount", 10000000.0);
        participants.add(equity);
        
        Map<String, Object> debt = new HashMap<>();
        debt.put("partyId", "party-debt-lender");
        debt.put("amount", 20000000.0);
        participants.add(debt);
        
        capitalStack.setParticipants(participants);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = capitalStack.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("participants"));
        assertTrue(json.has("waterfallId"));
        
        assertEquals("capital-stack-engine", json.get("id").asText());
        assertEquals("waterfall-engine", json.get("waterfallId").asText());
        
        // Verify participants structure
        com.fasterxml.jackson.databind.JsonNode participantsJson = json.get("participants");
        assertEquals(2, participantsJson.size());
        assertEquals("party-equity-investor", participantsJson.get(0).get("partyId").asText());
        assertEquals(10000000.0, participantsJson.get(0).get("amount").asDouble());
        
        // Verify computed fields
        assertTrue(json.has("totalCapital"));
        assertTrue(json.has("participantCount"));
        assertEquals(30000000.0, json.get("totalCapital").asDouble());
        assertEquals(2, json.get("participantCount").asInt());
    }
    
    @Test
    void testDifferentCapitalStackStructures() {
        // Test simple equity-only structure
        IRCapitalStack equityOnly = new IRCapitalStack("capital-stack-equity", "Equity Only Stack");
        equityOnly.setWaterfallId("waterfall-equity");
        
        List<Map<String, Object>> equityParticipants = new ArrayList<>();
        Map<String, Object> equityInvestor = new HashMap<>();
        equityInvestor.put("partyId", "party-equity");
        equityInvestor.put("amount", 25000000.0);
        equityParticipants.add(equityInvestor);
        equityOnly.setParticipants(equityParticipants);
        
        equityOnly.validate();
        assertTrue(equityOnly.isValid());
        assertEquals(25000000.0, equityOnly.getTotalCapital());
        assertEquals(1, equityOnly.getParticipantCount());
        
        // Test complex multi-tier structure
        IRCapitalStack multiTier = new IRCapitalStack("capital-stack-multi", "Multi-Tier Stack");
        multiTier.setWaterfallId("waterfall-multi");
        
        List<Map<String, Object>> multiParticipants = new ArrayList<>();
        
        // Senior debt
        Map<String, Object> seniorDebt = new HashMap<>();
        seniorDebt.put("partyId", "party-senior-lender");
        seniorDebt.put("amount", 40000000.0);
        multiParticipants.add(seniorDebt);
        
        // Mezzanine debt
        Map<String, Object> mezzDebt = new HashMap<>();
        mezzDebt.put("partyId", "party-mezz-lender");
        mezzDebt.put("amount", 10000000.0);
        multiParticipants.add(mezzDebt);
        
        // Preferred equity
        Map<String, Object> prefEquity = new HashMap<>();
        prefEquity.put("partyId", "party-pref-investor");
        prefEquity.put("amount", 8000000.0);
        multiParticipants.add(prefEquity);
        
        // Common equity
        Map<String, Object> commonEquity = new HashMap<>();
        commonEquity.put("partyId", "party-sponsor");
        commonEquity.put("amount", 2000000.0);
        multiParticipants.add(commonEquity);
        
        multiTier.setParticipants(multiParticipants);
        
        multiTier.validate();
        assertTrue(multiTier.isValid());
        assertEquals(60000000.0, multiTier.getTotalCapital()); // $60M total
        assertEquals(4, multiTier.getParticipantCount());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.CapitalStackNode equityNode = new dev.cfdl.ast.CapitalStackNode("capital-stack-equity", "Equity Only Stack");
        equityNode.setWaterfallId("waterfall-equity");
        equityNode.setProperty("participants", equityParticipants);
        astNodes.add(equityNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify capital stack has proper metadata
        IRCapitalStack irCapitalStack = (IRCapitalStack) result.getIrNodes().get(0);
        assertEquals("capitalStack", irCapitalStack.getSchemaMetadata("entityType"));
        assertEquals(1, irCapitalStack.getSchemaMetadata("participantCount"));
        assertEquals(25000000.0, irCapitalStack.getSchemaMetadata("totalCapital"));
        assertEquals(Boolean.TRUE, irCapitalStack.getSchemaMetadata("hasWaterfall"));
    }
}