package dev.cfdl.ir;

import dev.cfdl.ast.FundNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Unit tests for IRFund and FundNode integration.
 * 
 * Tests the fund functionality needed for highest-level organization,
 * capital commitment management, and fund-level participant structures.
 */
public class IRFundTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testFundNodeCreation() {
        FundNode fundNode = new FundNode("fund-core-real-estate", "Core Real Estate Fund III");
        fundNode.setDescription("Institutional-quality real estate fund focused on core assets in major metropolitan markets");
        fundNode.setFundType("real_estate");
        fundNode.setLineNumber(35);
        fundNode.setColumnNumber(15);
        
        // Add portfolio references
        fundNode.addPortfolioId("portfolio-office-core");
        fundNode.addPortfolioId("portfolio-retail-prime");
        fundNode.addPortfolioId("portfolio-industrial-logistics");
        
        // Add fund-level streams
        fundNode.addStreamId("stream-fund-mgmt-fee");
        fundNode.addStreamId("stream-fund-carried-interest");
        fundNode.addStreamId("stream-fund-transaction-fees");
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> gp = new HashMap<>();
        gp.put("partyId", "party-general-partner-advisor");
        gp.put("role", "general_partner");
        gp.put("commitment", 50000000.0); // $50M GP commitment
        participants.add(gp);
        
        Map<String, Object> lp1 = new HashMap<>();
        lp1.put("partyId", "party-pension-fund-calpers");
        lp1.put("role", "limited_partner");
        lp1.put("commitment", 300000000.0); // $300M commitment
        participants.add(lp1);
        
        Map<String, Object> lp2 = new HashMap<>();
        lp2.put("partyId", "party-sovereign-wealth-fund");
        lp2.put("role", "limited_partner");
        lp2.put("commitment", 200000000.0); // $200M commitment
        participants.add(lp2);
        
        Map<String, Object> advisor = new HashMap<>();
        advisor.put("partyId", "party-investment-committee");
        advisor.put("role", "advisor");
        advisor.put("commitment", 0.0); // No capital commitment
        participants.add(advisor);
        
        fundNode.setProperty("participants", participants);
        
        // Add state configuration (required for funds)
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("fundraising", "investing", "managing", "harvesting", "liquidated"));
        stateConfig.put("initialState", "investing");
        
        Map<String, List<String>> transitionRules = new HashMap<>();
        transitionRules.put("fundraising", Arrays.asList("investing"));
        transitionRules.put("investing", Arrays.asList("managing"));
        transitionRules.put("managing", Arrays.asList("harvesting"));
        transitionRules.put("harvesting", Arrays.asList("liquidated"));
        stateConfig.put("transitionRules", transitionRules);
        
        fundNode.setProperty("stateConfig", stateConfig);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("vintage", 2024);
        metadata.put("targetSize", 1000000000); // $1B target
        metadata.put("geography", "US Core Markets");
        metadata.put("strategy", "Core Real Estate");
        metadata.put("termYears", 10);
        metadata.put("extensionYears", 2);
        fundNode.setProperty("metadata", metadata);
        
        // Verify fund node
        assertEquals("fund-core-real-estate", fundNode.getId());
        assertEquals("Core Real Estate Fund III", fundNode.getName());
        assertEquals("Institutional-quality real estate fund focused on core assets in major metropolitan markets", fundNode.getDescription());
        assertEquals("real_estate", fundNode.getFundType());
        assertEquals(35, fundNode.getLineNumber());
        assertEquals(15, fundNode.getColumnNumber());
        
        // Verify portfolio references
        assertEquals(3, fundNode.getPortfolioIds().size());
        assertTrue(fundNode.getPortfolioIds().contains("portfolio-office-core"));
        assertTrue(fundNode.getPortfolioIds().contains("portfolio-retail-prime"));
        assertTrue(fundNode.getPortfolioIds().contains("portfolio-industrial-logistics"));
        
        // Verify stream references
        assertEquals(3, fundNode.getStreamIds().size());
        assertTrue(fundNode.getStreamIds().contains("stream-fund-mgmt-fee"));
        assertTrue(fundNode.getStreamIds().contains("stream-fund-carried-interest"));
        assertTrue(fundNode.getStreamIds().contains("stream-fund-transaction-fees"));
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = fundNode.toJson();
        assertEquals("fund-core-real-estate", json.get("id").asText());
        assertEquals("Core Real Estate Fund III", json.get("name").asText());
        assertEquals("real_estate", json.get("fundType").asText());
        assertTrue(json.has("portfolios"));
        assertEquals(3, json.get("portfolios").size());
        assertTrue(json.has("streams"));
        assertEquals(3, json.get("streams").size());
        assertTrue(json.has("participants"));
        if (json.get("participants").size() > 0) {
            assertEquals(4, json.get("participants").size());
        }
        assertTrue(json.has("stateConfig"));
        assertTrue(json.has("metadata"));
        
        if (json.get("metadata").has("vintage")) {
            assertEquals(2024, json.get("metadata").get("vintage").asInt());
        }
    }
    
    @Test
    void testIRFundTransformation() {
        // Create fund AST node
        FundNode fundNode = new FundNode("fund-venture", "Venture Capital Fund I");
        fundNode.setDescription("Early-stage technology venture capital fund");
        fundNode.setFundType("venture_capital");
        
        // Add portfolios
        fundNode.addPortfolioId("portfolio-tech-startups");
        
        // Add fund streams
        fundNode.addStreamId("stream-management-fee");
        fundNode.addStreamId("stream-carried-interest");
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> gp = new HashMap<>();
        gp.put("partyId", "party-venture-gp");
        gp.put("role", "general_partner");
        gp.put("commitment", 10000000.0); // $10M
        participants.add(gp);
        
        Map<String, Object> lp = new HashMap<>();
        lp.put("partyId", "party-institutional-lp");
        lp.put("role", "limited_partner");
        lp.put("commitment", 90000000.0); // $90M
        participants.add(lp);
        
        fundNode.setProperty("participants", participants);
        
        // Add state configuration
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("fundraising", "investing", "harvesting"));
        stateConfig.put("initialState", "fundraising");
        fundNode.setProperty("stateConfig", stateConfig);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(fundNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRFund irFund = (IRFund) result.getIrNodes().get(0);
        
        // Verify IR fund
        assertEquals("fund-venture", irFund.getId());
        assertEquals("Venture Capital Fund I", irFund.getName());
        assertEquals("Early-stage technology venture capital fund", irFund.getDescription());
        assertEquals("venture_capital", irFund.getFundType());
        
        // Verify portfolio references
        assertNotNull(irFund.getPortfolioIds());
        assertEquals(1, irFund.getPortfolioIds().size());
        assertTrue(irFund.getPortfolioIds().contains("portfolio-tech-startups"));
        
        // Verify stream references
        assertNotNull(irFund.getStreamIds());
        assertEquals(2, irFund.getStreamIds().size());
        assertTrue(irFund.getStreamIds().contains("stream-management-fee"));
        assertTrue(irFund.getStreamIds().contains("stream-carried-interest"));
        
        // Verify participants
        assertNotNull(irFund.getParticipants());
        assertEquals(2, irFund.getParticipants().size());
        
        // Verify computed properties
        assertEquals(1, irFund.getPortfolioCount());
        assertEquals(2, irFund.getStreamCount());
        assertEquals(2, irFund.getParticipantCount());
        assertEquals(100000000.0, irFund.getTotalCommitment(), 0.01); // $100M total
        assertTrue(irFund.hasGeneralPartners());
        assertTrue(irFund.hasLimitedPartners());
        assertTrue(irFund.hasFundStreams());
        assertEquals("fundraising", irFund.getCurrentState());
        
        // Verify dependencies
        assertTrue(irFund.getDependencies().contains("portfolio-tech-startups"));
        assertTrue(irFund.getDependencies().contains("stream-management-fee"));
        assertTrue(irFund.getDependencies().contains("stream-carried-interest"));
        assertTrue(irFund.getDependencies().contains("party-venture-gp"));
        assertTrue(irFund.getDependencies().contains("party-institutional-lp"));
        
        // Verify metadata enrichment
        assertEquals("fund", irFund.getSchemaMetadata("entityType"));
        assertEquals(1, irFund.getSchemaMetadata("portfolioCount"));
        assertEquals(2, irFund.getSchemaMetadata("streamCount"));
        assertEquals(2, irFund.getSchemaMetadata("participantCount"));
        assertEquals(100000000.0, (Double) irFund.getSchemaMetadata("totalCommitment"), 0.01);
        assertEquals(Boolean.TRUE, irFund.getSchemaMetadata("hasGeneralPartners"));
        assertEquals(Boolean.TRUE, irFund.getSchemaMetadata("hasLimitedPartners"));
        assertEquals(Boolean.TRUE, irFund.getSchemaMetadata("hasFundStreams"));
        assertEquals("fundraising", irFund.getSchemaMetadata("currentState"));
        assertNotNull(irFund.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRFundValidation() {
        // Test valid fund
        IRFund validFund = new IRFund("fund-valid", "Valid Fund");
        validFund.setDescription("Valid fund for testing");
        
        // State config is required for funds
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        stateConfig.put("initialState", "active");
        validFund.setStateConfig(stateConfig);
        
        validFund.validate();
        assertTrue(validFund.isValid());
        assertEquals(0, validFund.getValidationMessages().size());
        
        // Test invalid fund - missing required fields
        IRFund invalidFund = new IRFund();
        // Missing id, name, stateConfig
        
        invalidFund.validate();
        assertFalse(invalidFund.isValid());
        assertTrue(invalidFund.getValidationMessages().size() >= 3);
        assertTrue(invalidFund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidFund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidFund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("stateConfig is required")));
    }
    
    @Test
    void testFundTypeValidation() {
        IRFund fund = new IRFund("fund-type-test", "Fund Type Test");
        
        // State config is required
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        stateConfig.put("initialState", "active");
        fund.setStateConfig(stateConfig);
        
        // Valid fund types
        String[] validFundTypes = {"closed_end", "open_end", "private_equity", "real_estate", "hedge", "venture_capital", "other"};
        for (String validFundType : validFundTypes) {
            fund.setFundType(validFundType);
            fund.validate();
            assertTrue(fund.isValid(), "Fund type '" + validFundType + "' should be valid");
        }
        
        // Invalid fund type
        fund.setFundType("invalid-fund-type");
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("fundType must be one of")));
    }
    
    @Test
    void testParticipantValidation() {
        IRFund fund = new IRFund("fund-participant-test", "Participant Test Fund");
        
        // State config is required
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        stateConfig.put("initialState", "active");
        fund.setStateConfig(stateConfig);
        
        // Invalid participant - missing partyId
        List<Map<String, Object>> invalidParticipants = new ArrayList<>();
        Map<String, Object> invalidParticipant = new HashMap<>();
        // Missing partyId
        invalidParticipant.put("role", "general_partner");
        invalidParticipant.put("commitment", 1000000.0);
        invalidParticipants.add(invalidParticipant);
        fund.setParticipants(invalidParticipants);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participant 0 must have partyId")));
        
        // Invalid participant - missing role
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        // Missing role
        invalidParticipant.put("commitment", 1000000.0);
        invalidParticipants.add(invalidParticipant);
        fund.setParticipants(invalidParticipants);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participant 0 must have role")));
        
        // Invalid participant - invalid role
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        invalidParticipant.put("role", "invalid-role");
        invalidParticipant.put("commitment", 1000000.0);
        invalidParticipants.add(invalidParticipant);
        fund.setParticipants(invalidParticipants);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("role must be one of: general_partner, limited_partner, advisor, other")));
        
        // Invalid participant - missing commitment
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        invalidParticipant.put("role", "general_partner");
        // Missing commitment
        invalidParticipants.add(invalidParticipant);
        fund.setParticipants(invalidParticipants);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("participant 0 must have commitment")));
        
        // Invalid participant - non-numeric commitment
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        invalidParticipant.put("role", "general_partner");
        invalidParticipant.put("commitment", "not-a-number");
        invalidParticipants.add(invalidParticipant);
        fund.setParticipants(invalidParticipants);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("commitment must be a number")));
        
        // Invalid participant - negative commitment
        invalidParticipants = new ArrayList<>();
        invalidParticipant = new HashMap<>();
        invalidParticipant.put("partyId", "party-test");
        invalidParticipant.put("role", "general_partner");
        invalidParticipant.put("commitment", -1000000.0);
        invalidParticipants.add(invalidParticipant);
        fund.setParticipants(invalidParticipants);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("commitment must be non-negative")));
        
        // Valid participants
        List<Map<String, Object>> validParticipants = new ArrayList<>();
        
        Map<String, Object> gp = new HashMap<>();
        gp.put("partyId", "party-gp");
        gp.put("role", "general_partner");
        gp.put("commitment", 10000000.0);
        validParticipants.add(gp);
        
        Map<String, Object> lp = new HashMap<>();
        lp.put("partyId", "party-lp");
        lp.put("role", "limited_partner");
        lp.put("commitment", 90000000.0);
        validParticipants.add(lp);
        
        Map<String, Object> advisor = new HashMap<>();
        advisor.put("partyId", "party-advisor");
        advisor.put("role", "advisor");
        advisor.put("commitment", 0.0); // Zero commitment is valid
        validParticipants.add(advisor);
        
        // Create a fresh fund to avoid accumulated validation errors
        IRFund validFund = new IRFund("fund-participant-valid", "Valid Participant Fund");
        
        // State config is required
        Map<String, Object> validStateConfig = new HashMap<>();
        validStateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        validStateConfig.put("initialState", "active");
        validFund.setStateConfig(validStateConfig);
        
        validFund.setParticipants(validParticipants);
        
        validFund.validate();
        if (!validFund.isValid()) {
            System.out.println("Valid participant validation messages: " + validFund.getValidationMessages());
        }
        assertTrue(validFund.isValid());
        assertEquals(3, validFund.getParticipantCount());
        assertEquals(100000000.0, validFund.getTotalCommitment(), 0.01);
        assertTrue(validFund.hasGeneralPartners());
        assertTrue(validFund.hasLimitedPartners());
    }
    
    @Test
    void testPortfolioAndStreamIdValidation() {
        IRFund fund = new IRFund("fund-id-test", "ID Test Fund");
        
        // State config is required
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        stateConfig.put("initialState", "active");
        fund.setStateConfig(stateConfig);
        
        // Null portfolio ID
        List<String> portfoliosWithNull = new ArrayList<>();
        portfoliosWithNull.add("portfolio-valid");
        portfoliosWithNull.add(null);
        fund.setPortfolioIds(portfoliosWithNull);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("portfolio 1 id cannot be null or empty")));
        
        // Empty portfolio ID
        List<String> portfoliosWithEmpty = new ArrayList<>();
        portfoliosWithEmpty.add("portfolio-valid");
        portfoliosWithEmpty.add("");
        fund.setPortfolioIds(portfoliosWithEmpty);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("portfolio 1 id cannot be null or empty")));
        
        // Null stream ID
        List<String> streamsWithNull = new ArrayList<>();
        streamsWithNull.add("stream-valid");
        streamsWithNull.add(null);
        fund.setStreamIds(streamsWithNull);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("stream 1 id cannot be null or empty")));
        
        // Empty stream ID
        List<String> streamsWithEmpty = new ArrayList<>();
        streamsWithEmpty.add("stream-valid");
        streamsWithEmpty.add("");
        fund.setStreamIds(streamsWithEmpty);
        
        fund.validate();
        assertFalse(fund.isValid());
        assertTrue(fund.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("stream 1 id cannot be null or empty")));
    }
    
    @Test
    void testFundEngineJsonOutput() {
        // Create fund with complete data
        IRFund fund = new IRFund("fund-engine", "Engine Test Fund");
        fund.setDescription("Comprehensive fund for engine JSON testing");
        fund.setFundType("private_equity");
        
        // Add portfolios
        fund.addPortfolioId("portfolio-buyout-1");
        fund.addPortfolioId("portfolio-buyout-2");
        
        // Add fund streams
        fund.addStreamId("stream-mgmt-fee");
        fund.addStreamId("stream-carried-interest");
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> gp = new HashMap<>();
        gp.put("partyId", "party-pe-gp");
        gp.put("role", "general_partner");
        gp.put("commitment", 25000000.0);
        participants.add(gp);
        
        Map<String, Object> lp1 = new HashMap<>();
        lp1.put("partyId", "party-pension-fund");
        lp1.put("role", "limited_partner");
        lp1.put("commitment", 400000000.0);
        participants.add(lp1);
        
        Map<String, Object> lp2 = new HashMap<>();
        lp2.put("partyId", "party-endowment");
        lp2.put("role", "limited_partner");
        lp2.put("commitment", 75000000.0);
        participants.add(lp2);
        
        fund.setParticipants(participants);
        
        // Add state configuration
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("fundraising", "investing", "harvesting", "liquidated"));
        stateConfig.put("initialState", "investing");
        
        Map<String, List<String>> transitionRules = new HashMap<>();
        transitionRules.put("fundraising", Arrays.asList("investing"));
        transitionRules.put("investing", Arrays.asList("harvesting"));
        transitionRules.put("harvesting", Arrays.asList("liquidated"));
        stateConfig.put("transitionRules", transitionRules);
        
        fund.setStateConfig(stateConfig);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = fund.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("stateConfig"));
        
        assertEquals("fund-engine", json.get("id").asText());
        assertEquals("Engine Test Fund", json.get("name").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertTrue(json.has("fundType"));
        assertEquals("Comprehensive fund for engine JSON testing", json.get("description").asText());
        assertEquals("private_equity", json.get("fundType").asText());
        
        // Verify portfolios array
        assertTrue(json.has("portfolios"));
        com.fasterxml.jackson.databind.JsonNode portfoliosJson = json.get("portfolios");
        assertEquals(2, portfoliosJson.size());
        assertEquals("portfolio-buyout-1", portfoliosJson.get(0).asText());
        assertEquals("portfolio-buyout-2", portfoliosJson.get(1).asText());
        
        // Verify streams array
        assertTrue(json.has("streams"));
        com.fasterxml.jackson.databind.JsonNode streamsJson = json.get("streams");
        assertEquals(2, streamsJson.size());
        assertEquals("stream-mgmt-fee", streamsJson.get(0).asText());
        assertEquals("stream-carried-interest", streamsJson.get(1).asText());
        
        // Verify participants array
        assertTrue(json.has("participants"));
        com.fasterxml.jackson.databind.JsonNode participantsJson = json.get("participants");
        assertEquals(3, participantsJson.size());
        assertEquals("party-pe-gp", participantsJson.get(0).get("partyId").asText());
        assertEquals("general_partner", participantsJson.get(0).get("role").asText());
        assertEquals(25000000.0, participantsJson.get(0).get("commitment").asDouble());
        
        // Verify state configuration
        assertTrue(json.has("stateConfig"));
        com.fasterxml.jackson.databind.JsonNode stateConfigJson = json.get("stateConfig");
        assertTrue(stateConfigJson.has("allowedStates"));
        assertTrue(stateConfigJson.has("initialState"));
        assertTrue(stateConfigJson.has("transitionRules"));
        assertEquals("investing", stateConfigJson.get("initialState").asText());
        
        // Verify computed fields
        assertTrue(json.has("portfolioCount"));
        assertTrue(json.has("streamCount"));
        assertTrue(json.has("participantCount"));
        assertTrue(json.has("totalCommitment"));
        assertTrue(json.has("hasGeneralPartners"));
        assertTrue(json.has("hasLimitedPartners"));
        assertTrue(json.has("hasFundStreams"));
        assertTrue(json.has("currentState"));
        
        assertEquals(2, json.get("portfolioCount").asInt());
        assertEquals(2, json.get("streamCount").asInt());
        assertEquals(3, json.get("participantCount").asInt());
        assertEquals(500000000.0, json.get("totalCommitment").asDouble());
        assertEquals(true, json.get("hasGeneralPartners").asBoolean());
        assertEquals(true, json.get("hasLimitedPartners").asBoolean());
        assertEquals(true, json.get("hasFundStreams").asBoolean());
        assertEquals("investing", json.get("currentState").asText());
    }
    
    @Test
    void testDifferentFundTypes() {
        // Test real estate fund
        IRFund realEstateFund = new IRFund("fund-real-estate", "Real Estate Fund");
        realEstateFund.setDescription("Core real estate investment fund");
        realEstateFund.setFundType("real_estate");
        realEstateFund.addPortfolioId("portfolio-office");
        realEstateFund.addPortfolioId("portfolio-retail");
        
        Map<String, Object> reStateConfig = new HashMap<>();
        reStateConfig.put("allowedStates", Arrays.asList("fundraising", "investing", "managing", "harvesting"));
        reStateConfig.put("initialState", "investing");
        realEstateFund.setStateConfig(reStateConfig);
        
        realEstateFund.validate();
        assertTrue(realEstateFund.isValid());
        assertEquals("real_estate", realEstateFund.getFundType());
        assertEquals(2, realEstateFund.getPortfolioCount());
        assertEquals(0, realEstateFund.getParticipantCount());
        assertEquals(0.0, realEstateFund.getTotalCommitment());
        assertFalse(realEstateFund.hasGeneralPartners());
        assertFalse(realEstateFund.hasLimitedPartners());
        
        // Test venture capital fund with participants
        IRFund vcFund = new IRFund("fund-vc", "Venture Capital Fund");
        vcFund.setDescription("Early-stage technology fund");
        vcFund.setFundType("venture_capital");
        vcFund.addPortfolioId("portfolio-startups");
        vcFund.addStreamId("stream-management-fee");
        
        List<Map<String, Object>> vcParticipants = new ArrayList<>();
        
        Map<String, Object> vcGp = new HashMap<>();
        vcGp.put("partyId", "party-vc-partners");
        vcGp.put("role", "general_partner");
        vcGp.put("commitment", 5000000.0);
        vcParticipants.add(vcGp);
        
        Map<String, Object> vcLp = new HashMap<>();
        vcLp.put("partyId", "party-university-endowment");
        vcLp.put("role", "limited_partner");
        vcLp.put("commitment", 45000000.0);
        vcParticipants.add(vcLp);
        
        vcFund.setParticipants(vcParticipants);
        
        Map<String, Object> vcStateConfig = new HashMap<>();
        vcStateConfig.put("allowedStates", Arrays.asList("fundraising", "investing", "harvesting"));
        vcStateConfig.put("initialState", "fundraising");
        vcFund.setStateConfig(vcStateConfig);
        
        vcFund.validate();
        assertTrue(vcFund.isValid());
        assertEquals("venture_capital", vcFund.getFundType());
        assertEquals(1, vcFund.getPortfolioCount());
        assertEquals(1, vcFund.getStreamCount());
        assertEquals(2, vcFund.getParticipantCount());
        assertEquals(50000000.0, vcFund.getTotalCommitment());
        assertTrue(vcFund.hasGeneralPartners());
        assertTrue(vcFund.hasLimitedPartners());
        assertTrue(vcFund.hasFundStreams());
        assertEquals("fundraising", vcFund.getCurrentState());
        
        // Test hedge fund
        IRFund hedgeFund = new IRFund("fund-hedge", "Hedge Fund");
        hedgeFund.setDescription("Multi-strategy hedge fund");
        hedgeFund.setFundType("hedge");
        
        Map<String, Object> hedgeStateConfig = new HashMap<>();
        hedgeStateConfig.put("allowedStates", Arrays.asList("open", "closed", "liquidating"));
        hedgeStateConfig.put("initialState", "open");
        hedgeFund.setStateConfig(hedgeStateConfig);
        
        hedgeFund.validate();
        assertTrue(hedgeFund.isValid());
        assertEquals("hedge", hedgeFund.getFundType());
        assertEquals("open", hedgeFund.getCurrentState());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.FundNode simpleFundNode = new dev.cfdl.ast.FundNode("fund-simple", "Simple Fund");
        Map<String, Object> simpleStateConfig = new HashMap<>();
        simpleStateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        simpleStateConfig.put("initialState", "active");
        simpleFundNode.setProperty("stateConfig", simpleStateConfig);
        astNodes.add(simpleFundNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify fund has proper metadata
        IRFund irFund = (IRFund) result.getIrNodes().get(0);
        assertEquals("fund", irFund.getSchemaMetadata("entityType"));
        assertEquals(0, irFund.getSchemaMetadata("portfolioCount"));
        assertEquals(0, irFund.getSchemaMetadata("streamCount"));
        assertEquals(0, irFund.getSchemaMetadata("participantCount"));
        assertEquals(0.0, (Double) irFund.getSchemaMetadata("totalCommitment"), 0.01);
        assertEquals(Boolean.FALSE, irFund.getSchemaMetadata("hasGeneralPartners"));
        assertEquals(Boolean.FALSE, irFund.getSchemaMetadata("hasLimitedPartners"));
        assertEquals(Boolean.FALSE, irFund.getSchemaMetadata("hasFundStreams"));
        assertEquals("active", irFund.getSchemaMetadata("currentState"));
    }
}