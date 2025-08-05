package dev.cfdl.ir;

import dev.cfdl.ast.PortfolioNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Unit tests for IRPortfolio and PortfolioNode integration.
 * 
 * Tests the portfolio functionality needed for multi-asset analysis,
 * fund management, and aggregated reporting across multiple deals.
 */
public class IRPortfolioTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testPortfolioNodeCreation() {
        PortfolioNode portfolioNode = new PortfolioNode("portfolio-reit-core", "Core REIT Portfolio");
        portfolioNode.setDescription("Core real estate investment trust portfolio focused on Class A office buildings");
        portfolioNode.setLineNumber(25);
        portfolioNode.setColumnNumber(8);
        
        // Add deal references
        portfolioNode.addDealId("deal-office-tower-nyc");
        portfolioNode.addDealId("deal-office-complex-sf");
        portfolioNode.addDealId("deal-office-campus-austin");
        
        // Add portfolio-level streams (management fees)
        portfolioNode.addStreamId("stream-portfolio-mgmt-fee");
        portfolioNode.addStreamId("stream-portfolio-performance-fee");
        
        // Add state configuration
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("drafting", "active", "harvesting", "closed"));
        stateConfig.put("initialState", "active");
        
        Map<String, List<String>> transitionRules = new HashMap<>();
        transitionRules.put("drafting", Arrays.asList("active"));
        transitionRules.put("active", Arrays.asList("harvesting", "closed"));
        transitionRules.put("harvesting", Arrays.asList("closed"));
        stateConfig.put("transitionRules", transitionRules);
        
        portfolioNode.setProperty("stateConfig", stateConfig);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("assetClass", "office");
        metadata.put("geography", "US Core Markets");
        metadata.put("vintage", 2023);
        metadata.put("targetSize", 500000000); // $500M
        metadata.put("leverageTarget", 0.6);
        portfolioNode.setProperty("metadata", metadata);
        
        // Verify portfolio node
        assertEquals("portfolio-reit-core", portfolioNode.getId());
        assertEquals("Core REIT Portfolio", portfolioNode.getName());
        assertEquals("Core real estate investment trust portfolio focused on Class A office buildings", portfolioNode.getDescription());
        assertEquals(25, portfolioNode.getLineNumber());
        assertEquals(8, portfolioNode.getColumnNumber());
        
        // Verify deal references
        assertEquals(3, portfolioNode.getDealIds().size());
        assertTrue(portfolioNode.getDealIds().contains("deal-office-tower-nyc"));
        assertTrue(portfolioNode.getDealIds().contains("deal-office-complex-sf"));
        assertTrue(portfolioNode.getDealIds().contains("deal-office-campus-austin"));
        
        // Verify stream references
        assertEquals(2, portfolioNode.getStreamIds().size());
        assertTrue(portfolioNode.getStreamIds().contains("stream-portfolio-mgmt-fee"));
        assertTrue(portfolioNode.getStreamIds().contains("stream-portfolio-performance-fee"));
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = portfolioNode.toJson();
        assertEquals("portfolio-reit-core", json.get("id").asText());
        assertEquals("Core REIT Portfolio", json.get("name").asText());
        assertTrue(json.has("deals"));
        assertEquals(3, json.get("deals").size());
        assertTrue(json.has("streams"));
        assertEquals(2, json.get("streams").size());
        assertTrue(json.has("stateConfig"));
        assertTrue(json.has("metadata"));
        
        if (json.get("metadata").has("assetClass")) {
            assertEquals("office", json.get("metadata").get("assetClass").asText());
        }
    }
    
    @Test
    void testIRPortfolioTransformation() {
        // Create portfolio AST node
        PortfolioNode portfolioNode = new PortfolioNode("portfolio-diversified", "Diversified Fund Portfolio");
        portfolioNode.setDescription("Multi-strategy portfolio with mixed asset classes");
        
        // Add deals
        portfolioNode.addDealId("deal-retail-center");
        portfolioNode.addDealId("deal-industrial-warehouse");
        
        // Add portfolio streams
        portfolioNode.addStreamId("stream-management-fee");
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(portfolioNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRPortfolio irPortfolio = (IRPortfolio) result.getIrNodes().get(0);
        
        // Verify IR portfolio
        assertEquals("portfolio-diversified", irPortfolio.getId());
        assertEquals("Diversified Fund Portfolio", irPortfolio.getName());
        assertEquals("Multi-strategy portfolio with mixed asset classes", irPortfolio.getDescription());
        
        // Verify deal references
        assertNotNull(irPortfolio.getDealIds());
        assertEquals(2, irPortfolio.getDealIds().size());
        assertTrue(irPortfolio.getDealIds().contains("deal-retail-center"));
        assertTrue(irPortfolio.getDealIds().contains("deal-industrial-warehouse"));
        
        // Verify stream references
        assertNotNull(irPortfolio.getStreamIds());
        assertEquals(1, irPortfolio.getStreamIds().size());
        assertTrue(irPortfolio.getStreamIds().contains("stream-management-fee"));
        
        // Verify computed properties
        assertEquals(2, irPortfolio.getDealCount());
        assertEquals(1, irPortfolio.getStreamCount());
        assertFalse(irPortfolio.hasStateManagement());
        assertTrue(irPortfolio.hasPortfolioStreams());
        assertNull(irPortfolio.getCurrentState());
        
        // Verify dependencies
        assertTrue(irPortfolio.getDependencies().contains("deal-retail-center"));
        assertTrue(irPortfolio.getDependencies().contains("deal-industrial-warehouse"));
        assertTrue(irPortfolio.getDependencies().contains("stream-management-fee"));
        
        // Verify metadata enrichment
        assertEquals("portfolio", irPortfolio.getSchemaMetadata("entityType"));
        assertEquals(2, irPortfolio.getSchemaMetadata("dealCount"));
        assertEquals(1, irPortfolio.getSchemaMetadata("streamCount"));
        assertEquals(Boolean.FALSE, irPortfolio.getSchemaMetadata("hasStateManagement"));
        assertEquals(Boolean.TRUE, irPortfolio.getSchemaMetadata("hasPortfolioStreams"));
        assertNotNull(irPortfolio.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRPortfolioValidation() {
        // Test valid portfolio
        IRPortfolio validPortfolio = new IRPortfolio("portfolio-valid", "Valid Portfolio");
        validPortfolio.setDescription("Valid portfolio for testing");
        validPortfolio.addDealId("deal-test-1");
        validPortfolio.addDealId("deal-test-2");
        
        validPortfolio.validate();
        assertTrue(validPortfolio.isValid());
        assertEquals(0, validPortfolio.getValidationMessages().size());
        
        // Test invalid portfolio - missing required fields
        IRPortfolio invalidPortfolio = new IRPortfolio();
        // Missing id, name, deals
        
        invalidPortfolio.validate();
        assertFalse(invalidPortfolio.isValid());
        assertTrue(invalidPortfolio.getValidationMessages().size() >= 3);
        assertTrue(invalidPortfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidPortfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidPortfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("deals array is required")));
    }
    
    @Test
    void testDealIdValidation() {
        IRPortfolio portfolio = new IRPortfolio("portfolio-deal-test", "Deal ID Test Portfolio");
        
        // Empty deals array
        portfolio.setDealIds(new ArrayList<>());
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("deals array is required and must not be empty")));
        
        // Null deal ID
        List<String> dealsWithNull = new ArrayList<>();
        dealsWithNull.add("deal-valid");
        dealsWithNull.add(null);
        portfolio.setDealIds(dealsWithNull);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("deal 1 id cannot be null or empty")));
        
        // Empty deal ID
        List<String> dealsWithEmpty = new ArrayList<>();
        dealsWithEmpty.add("deal-valid");
        dealsWithEmpty.add("");
        portfolio.setDealIds(dealsWithEmpty);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("deal 1 id cannot be null or empty")));
    }
    
    @Test
    void testStreamIdValidation() {
        IRPortfolio portfolio = new IRPortfolio("portfolio-stream-test", "Stream ID Test Portfolio");
        portfolio.addDealId("deal-test"); // Required for validation
        
        // Null stream ID
        List<String> streamsWithNull = new ArrayList<>();
        streamsWithNull.add("stream-valid");
        streamsWithNull.add(null);
        portfolio.setStreamIds(streamsWithNull);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("stream 1 id cannot be null or empty")));
        
        // Empty stream ID
        List<String> streamsWithEmpty = new ArrayList<>();
        streamsWithEmpty.add("stream-valid");
        streamsWithEmpty.add("");
        portfolio.setStreamIds(streamsWithEmpty);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("stream 1 id cannot be null or empty")));
    }
    
    @Test
    void testStateConfigValidation() {
        IRPortfolio portfolio = new IRPortfolio("portfolio-state-test", "State Config Test Portfolio");
        portfolio.addDealId("deal-test"); // Required for validation
        
        // Missing allowedStates
        Map<String, Object> invalidStateConfig = new HashMap<>();
        invalidStateConfig.put("initialState", "active");
        // Missing allowedStates
        portfolio.setStateConfig(invalidStateConfig);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("stateConfig must have allowedStates")));
        
        // Missing initialState
        invalidStateConfig = new HashMap<>();
        invalidStateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        // Missing initialState
        portfolio.setStateConfig(invalidStateConfig);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("stateConfig must have initialState")));
        
        // InitialState not in allowedStates
        invalidStateConfig = new HashMap<>();
        invalidStateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        invalidStateConfig.put("initialState", "invalid-state");
        portfolio.setStateConfig(invalidStateConfig);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("initialState 'invalid-state' must be in allowedStates")));
        
        // Invalid transition rule - from state not in allowedStates
        Map<String, Object> validStateConfig = new HashMap<>();
        validStateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        validStateConfig.put("initialState", "active");
        
        Map<String, List<String>> invalidTransitionRules = new HashMap<>();
        invalidTransitionRules.put("invalid-from-state", Arrays.asList("closed"));
        validStateConfig.put("transitionRules", invalidTransitionRules);
        
        portfolio.setStateConfig(validStateConfig);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("transition rule from state 'invalid-from-state' must be in allowedStates")));
        
        // Invalid transition rule - to state not in allowedStates
        validStateConfig = new HashMap<>();
        validStateConfig.put("allowedStates", Arrays.asList("active", "closed"));
        validStateConfig.put("initialState", "active");
        
        invalidTransitionRules = new HashMap<>();
        invalidTransitionRules.put("active", Arrays.asList("invalid-to-state"));
        validStateConfig.put("transitionRules", invalidTransitionRules);
        
        portfolio.setStateConfig(validStateConfig);
        
        portfolio.validate();
        assertFalse(portfolio.isValid());
        assertTrue(portfolio.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("transition rule to state 'invalid-to-state' must be in allowedStates")));
        
        // Valid state configuration - create fresh portfolio to avoid accumulating validation errors
        IRPortfolio validPortfolio = new IRPortfolio("portfolio-state-valid", "Valid State Config Portfolio");
        validPortfolio.addDealId("deal-test"); // Required for validation
        
        Map<String, Object> correctStateConfig = new HashMap<>();
        correctStateConfig.put("allowedStates", Arrays.asList("drafting", "active", "closed"));
        correctStateConfig.put("initialState", "drafting");
        
        Map<String, List<String>> correctTransitionRules = new HashMap<>();
        correctTransitionRules.put("drafting", Arrays.asList("active"));
        correctTransitionRules.put("active", Arrays.asList("closed"));
        correctStateConfig.put("transitionRules", correctTransitionRules);
        
        validPortfolio.setStateConfig(correctStateConfig);
        
        validPortfolio.validate();
        if (!validPortfolio.isValid()) {
            System.out.println("Valid state config validation messages: " + validPortfolio.getValidationMessages());
        }
        assertTrue(validPortfolio.isValid());
        assertTrue(validPortfolio.hasStateManagement());
        assertEquals("drafting", validPortfolio.getCurrentState());
    }
    
    @Test
    void testPortfolioEngineJsonOutput() {
        // Create portfolio with complete data
        IRPortfolio portfolio = new IRPortfolio("portfolio-engine", "Engine Test Portfolio");
        portfolio.setDescription("Comprehensive portfolio for engine JSON testing");
        
        // Add deals
        portfolio.addDealId("deal-office-1");
        portfolio.addDealId("deal-retail-2");
        portfolio.addDealId("deal-industrial-3");
        
        // Add portfolio streams
        portfolio.addStreamId("stream-mgmt-fee");
        portfolio.addStreamId("stream-performance-fee");
        
        // Add state configuration
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("drafting", "active", "harvesting", "closed"));
        stateConfig.put("initialState", "active");
        
        Map<String, List<String>> transitionRules = new HashMap<>();
        transitionRules.put("drafting", Arrays.asList("active"));
        transitionRules.put("active", Arrays.asList("harvesting", "closed"));
        transitionRules.put("harvesting", Arrays.asList("closed"));
        stateConfig.put("transitionRules", transitionRules);
        
        portfolio.setStateConfig(stateConfig);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = portfolio.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("deals"));
        
        assertEquals("portfolio-engine", json.get("id").asText());
        assertEquals("Engine Test Portfolio", json.get("name").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertEquals("Comprehensive portfolio for engine JSON testing", json.get("description").asText());
        
        // Verify deals array
        com.fasterxml.jackson.databind.JsonNode dealsJson = json.get("deals");
        assertEquals(3, dealsJson.size());
        assertEquals("deal-office-1", dealsJson.get(0).asText());
        assertEquals("deal-retail-2", dealsJson.get(1).asText());
        assertEquals("deal-industrial-3", dealsJson.get(2).asText());
        
        // Verify streams array
        assertTrue(json.has("streams"));
        com.fasterxml.jackson.databind.JsonNode streamsJson = json.get("streams");
        assertEquals(2, streamsJson.size());
        assertEquals("stream-mgmt-fee", streamsJson.get(0).asText());
        assertEquals("stream-performance-fee", streamsJson.get(1).asText());
        
        // Verify state configuration
        assertTrue(json.has("stateConfig"));
        com.fasterxml.jackson.databind.JsonNode stateConfigJson = json.get("stateConfig");
        assertTrue(stateConfigJson.has("allowedStates"));
        assertTrue(stateConfigJson.has("initialState"));
        assertTrue(stateConfigJson.has("transitionRules"));
        assertEquals("active", stateConfigJson.get("initialState").asText());
        
        // Verify computed fields
        assertTrue(json.has("dealCount"));
        assertTrue(json.has("streamCount"));
        assertTrue(json.has("hasStateManagement"));
        assertTrue(json.has("hasPortfolioStreams"));
        assertTrue(json.has("currentState"));
        
        assertEquals(3, json.get("dealCount").asInt());
        assertEquals(2, json.get("streamCount").asInt());
        assertEquals(true, json.get("hasStateManagement").asBoolean());
        assertEquals(true, json.get("hasPortfolioStreams").asBoolean());
        assertEquals("active", json.get("currentState").asText());
    }
    
    @Test
    void testDifferentPortfolioTypes() {
        // Test simple portfolio (minimal)
        IRPortfolio simplePortfolio = new IRPortfolio("portfolio-simple", "Simple Portfolio");
        simplePortfolio.setDescription("Basic portfolio with just deals");
        simplePortfolio.addDealId("deal-single");
        
        simplePortfolio.validate();
        assertTrue(simplePortfolio.isValid());
        assertEquals(1, simplePortfolio.getDealCount());
        assertEquals(0, simplePortfolio.getStreamCount());
        assertFalse(simplePortfolio.hasStateManagement());
        assertFalse(simplePortfolio.hasPortfolioStreams());
        assertNull(simplePortfolio.getCurrentState());
        
        // Test portfolio with streams but no state management
        IRPortfolio streamPortfolio = new IRPortfolio("portfolio-streams", "Portfolio with Streams");
        streamPortfolio.setDescription("Portfolio with management fees but no lifecycle states");
        streamPortfolio.addDealId("deal-1");
        streamPortfolio.addDealId("deal-2");
        streamPortfolio.addStreamId("stream-base-fee");
        streamPortfolio.addStreamId("stream-performance-fee");
        
        streamPortfolio.validate();
        assertTrue(streamPortfolio.isValid());
        assertEquals(2, streamPortfolio.getDealCount());
        assertEquals(2, streamPortfolio.getStreamCount());
        assertFalse(streamPortfolio.hasStateManagement());
        assertTrue(streamPortfolio.hasPortfolioStreams());
        assertNull(streamPortfolio.getCurrentState());
        
        // Test portfolio with state management but no streams
        IRPortfolio statePortfolio = new IRPortfolio("portfolio-states", "Portfolio with States");
        statePortfolio.setDescription("Portfolio with lifecycle management");
        statePortfolio.addDealId("deal-1");
        
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", Arrays.asList("setup", "investment", "harvest", "liquidation"));
        stateConfig.put("initialState", "setup");
        statePortfolio.setStateConfig(stateConfig);
        
        statePortfolio.validate();
        assertTrue(statePortfolio.isValid());
        assertEquals(1, statePortfolio.getDealCount());
        assertEquals(0, statePortfolio.getStreamCount());
        assertTrue(statePortfolio.hasStateManagement());
        assertFalse(statePortfolio.hasPortfolioStreams());
        assertEquals("setup", statePortfolio.getCurrentState());
        
        // Test complex portfolio (all features)
        IRPortfolio complexPortfolio = new IRPortfolio("portfolio-complex", "Complex Portfolio");
        complexPortfolio.setDescription("Full-featured portfolio with deals, streams, and state management");
        
        // Multiple deals
        complexPortfolio.addDealId("deal-office-nyc");
        complexPortfolio.addDealId("deal-retail-la");
        complexPortfolio.addDealId("deal-industrial-chicago");
        
        // Multiple streams
        complexPortfolio.addStreamId("stream-base-mgmt-fee");
        complexPortfolio.addStreamId("stream-performance-fee");
        complexPortfolio.addStreamId("stream-acquisition-fee");
        
        // Complex state configuration
        Map<String, Object> complexStateConfig = new HashMap<>();
        complexStateConfig.put("allowedStates", Arrays.asList("fundraising", "investing", "managing", "harvesting", "liquidated"));
        complexStateConfig.put("initialState", "investing");
        
        Map<String, List<String>> transitionRules = new HashMap<>();
        transitionRules.put("fundraising", Arrays.asList("investing"));
        transitionRules.put("investing", Arrays.asList("managing"));
        transitionRules.put("managing", Arrays.asList("harvesting"));
        transitionRules.put("harvesting", Arrays.asList("liquidated"));
        complexStateConfig.put("transitionRules", transitionRules);
        
        complexPortfolio.setStateConfig(complexStateConfig);
        
        complexPortfolio.validate();
        assertTrue(complexPortfolio.isValid());
        assertEquals(3, complexPortfolio.getDealCount());
        assertEquals(3, complexPortfolio.getStreamCount());
        assertTrue(complexPortfolio.hasStateManagement());
        assertTrue(complexPortfolio.hasPortfolioStreams());
        assertEquals("investing", complexPortfolio.getCurrentState());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.PortfolioNode simpleNode = new dev.cfdl.ast.PortfolioNode("portfolio-simple", "Simple Portfolio");
        simpleNode.addDealId("deal-single");
        astNodes.add(simpleNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify portfolio has proper metadata
        IRPortfolio irPortfolio = (IRPortfolio) result.getIrNodes().get(0);
        assertEquals("portfolio", irPortfolio.getSchemaMetadata("entityType"));
        assertEquals(1, irPortfolio.getSchemaMetadata("dealCount"));
        assertEquals(0, irPortfolio.getSchemaMetadata("streamCount"));
        assertEquals(Boolean.FALSE, irPortfolio.getSchemaMetadata("hasStateManagement"));
        assertEquals(Boolean.FALSE, irPortfolio.getSchemaMetadata("hasPortfolioStreams"));
    }
}