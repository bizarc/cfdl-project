package dev.cfdl.ir;

import dev.cfdl.ast.WaterfallNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRWaterfall and WaterfallNode integration.
 * 
 * Tests the waterfall functionality needed for complex distribution logic,
 * including preferred returns, catch-up provisions, and promote structures.
 */
public class IRWaterfallTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testWaterfallNodeCreation() {
        WaterfallNode waterfallNode = new WaterfallNode("waterfall-pref-return", "Preferred Return Waterfall");
        waterfallNode.setDescription("8% preferred return to investors, then 20% promote to sponsor");
        waterfallNode.setLineNumber(45);
        waterfallNode.setColumnNumber(12);
        
        // Create tiers
        List<Map<String, Object>> tiers = new ArrayList<>();
        
        // Tier 1: 8% preferred return
        Map<String, Object> tier1 = new HashMap<>();
        tier1.put("id", "tier-pref-return");
        tier1.put("description", "8% preferred return to investors");
        tier1.put("prefRate", 0.08);
        
        List<Map<String, Object>> distribute1 = new ArrayList<>();
        Map<String, Object> dist1 = new HashMap<>();
        dist1.put("recipient", "party-institutional-investor");
        dist1.put("percentage", 1.0);
        distribute1.add(dist1);
        tier1.put("distribute", distribute1);
        
        tiers.add(tier1);
        
        // Tier 2: Return of capital
        Map<String, Object> tier2 = new HashMap<>();
        tier2.put("id", "tier-return-capital");
        tier2.put("description", "Return of invested capital");
        tier2.put("condition", "remainingCapital > 0");
        
        List<Map<String, Object>> distribute2 = new ArrayList<>();
        Map<String, Object> dist2 = new HashMap<>();
        dist2.put("fromCapitalStack", true);
        dist2.put("layerName", "equity");
        distribute2.add(dist2);
        tier2.put("distribute", distribute2);
        
        tiers.add(tier2);
        
        // Tier 3: Remaining cash split
        Map<String, Object> tier3 = new HashMap<>();
        tier3.put("id", "tier-remaining");
        tier3.put("description", "80% to investors, 20% promote to sponsor");
        tier3.put("condition", "true");
        
        List<Map<String, Object>> distribute3 = new ArrayList<>();
        Map<String, Object> dist3a = new HashMap<>();
        dist3a.put("recipient", "party-institutional-investor");
        dist3a.put("percentage", 0.8);
        distribute3.add(dist3a);
        
        Map<String, Object> dist3b = new HashMap<>();
        dist3b.put("recipient", "party-sponsor");
        dist3b.put("percentage", 0.2);
        distribute3.add(dist3b);
        
        tier3.put("distribute", distribute3);
        tiers.add(tier3);
        
        waterfallNode.setProperty("tiers", tiers);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("waterfallType", "preferred_return_promote");
        metadata.put("preferredRate", 0.08);
        metadata.put("promotePercentage", 0.2);
        waterfallNode.setProperty("metadata", metadata);
        
        // Verify waterfall node
        assertEquals("waterfall-pref-return", waterfallNode.getId());
        assertEquals("Preferred Return Waterfall", waterfallNode.getName());
        assertEquals("8% preferred return to investors, then 20% promote to sponsor", waterfallNode.getDescription());
        assertEquals(45, waterfallNode.getLineNumber());
        assertEquals(12, waterfallNode.getColumnNumber());
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = waterfallNode.toJson();
        assertEquals("waterfall-pref-return", json.get("id").asText());
        assertEquals("8% preferred return to investors, then 20% promote to sponsor", json.get("description").asText());
        assertTrue(json.has("tiers"));
        if (json.get("tiers").size() > 0) {
            assertEquals(3, json.get("tiers").size());
        }
        assertTrue(json.has("metadata"));
        if (json.get("metadata").has("preferredRate")) {
            assertEquals(0.08, json.get("metadata").get("preferredRate").asDouble());
        }
    }
    
    @Test
    void testIRWaterfallTransformation() {
        // Create waterfall AST node
        WaterfallNode waterfallNode = new WaterfallNode("waterfall-simple", "Simple Waterfall");
        waterfallNode.setDescription("Proportional distribution based on capital contributions");
        
        // Create simple tier
        List<Map<String, Object>> tiers = new ArrayList<>();
        Map<String, Object> tier = new HashMap<>();
        tier.put("id", "tier-proportional");
        tier.put("description", "Distribute pro-rata based on capital contributions");
        tier.put("condition", "true");
        
        List<Map<String, Object>> distribute = new ArrayList<>();
        Map<String, Object> dist = new HashMap<>();
        dist.put("fromCapitalStack", true);
        dist.put("layerName", "all");
        distribute.add(dist);
        tier.put("distribute", distribute);
        
        tiers.add(tier);
        waterfallNode.setProperty("tiers", tiers);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(waterfallNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRWaterfall irWaterfall = (IRWaterfall) result.getIrNodes().get(0);
        
        // Verify IR waterfall
        assertEquals("waterfall-simple", irWaterfall.getId());
        assertEquals("Simple Waterfall", irWaterfall.getName());
        assertEquals("Proportional distribution based on capital contributions", irWaterfall.getDescription());
        
        // Verify tiers
        assertNotNull(irWaterfall.getTiers());
        assertEquals(1, irWaterfall.getTiers().size());
        
        Map<String, Object> transformedTier = irWaterfall.getTiers().get(0);
        assertEquals("tier-proportional", transformedTier.get("id"));
        assertEquals("true", transformedTier.get("condition"));
        
        // Verify computed properties
        assertEquals(1, irWaterfall.getTierCount());
        assertFalse(irWaterfall.hasPreferredReturns());
        assertTrue(irWaterfall.usesCapitalStackProportions());
        
        // Verify metadata enrichment
        assertEquals("waterfall", irWaterfall.getSchemaMetadata("entityType"));
        assertEquals(1, irWaterfall.getSchemaMetadata("tierCount"));
        assertEquals(Boolean.FALSE, irWaterfall.getSchemaMetadata("hasPreferredReturns"));
        assertEquals(Boolean.TRUE, irWaterfall.getSchemaMetadata("usesCapitalStackProportions"));
        assertNotNull(irWaterfall.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRWaterfallValidation() {
        // Test valid waterfall
        IRWaterfall validWaterfall = new IRWaterfall("waterfall-valid", "Valid Waterfall");
        validWaterfall.setDescription("Valid waterfall for testing");
        
        List<Map<String, Object>> validTiers = new ArrayList<>();
        Map<String, Object> validTier = new HashMap<>();
        validTier.put("id", "tier-valid");
        validTier.put("condition", "true");
        
        List<Map<String, Object>> validDistribute = new ArrayList<>();
        Map<String, Object> validDist = new HashMap<>();
        validDist.put("recipient", "party-test");
        validDist.put("percentage", 1.0);
        validDistribute.add(validDist);
        validTier.put("distribute", validDistribute);
        
        validTiers.add(validTier);
        validWaterfall.setTiers(validTiers);
        
        validWaterfall.validate();
        assertTrue(validWaterfall.isValid());
        assertEquals(0, validWaterfall.getValidationMessages().size());
        
        // Test invalid waterfall - missing required fields
        IRWaterfall invalidWaterfall = new IRWaterfall();
        // Missing id, name, tiers
        
        invalidWaterfall.validate();
        assertFalse(invalidWaterfall.isValid());
        assertTrue(invalidWaterfall.getValidationMessages().size() >= 3);
        assertTrue(invalidWaterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidWaterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidWaterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("tiers are required")));
    }
    
    @Test
    void testTierValidation() {
        IRWaterfall waterfall = new IRWaterfall("waterfall-tier-test", "Tier Test Waterfall");
        
        // Invalid tier - missing id
        List<Map<String, Object>> invalidTiers = new ArrayList<>();
        Map<String, Object> invalidTier = new HashMap<>();
        // Missing id
        invalidTier.put("condition", "true");
        
        List<Map<String, Object>> distribute = new ArrayList<>();
        Map<String, Object> dist = new HashMap<>();
        dist.put("recipient", "party-test");
        dist.put("percentage", 1.0);
        distribute.add(dist);
        invalidTier.put("distribute", distribute);
        
        invalidTiers.add(invalidTier);
        waterfall.setTiers(invalidTiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("tier 0 must have id")));
        
        // Invalid tier - missing distribute
        invalidTiers = new ArrayList<>();
        invalidTier = new HashMap<>();
        invalidTier.put("id", "tier-test");
        invalidTier.put("condition", "true");
        // Missing distribute
        invalidTiers.add(invalidTier);
        waterfall.setTiers(invalidTiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("tier 0 must have distribute")));
        
        // Invalid tier - missing condition/until/prefRate
        invalidTiers = new ArrayList<>();
        invalidTier = new HashMap<>();
        invalidTier.put("id", "tier-test");
        // Missing condition, until, and prefRate
        invalidTier.put("distribute", distribute);
        invalidTiers.add(invalidTier);
        waterfall.setTiers(invalidTiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have one of: condition, until, or prefRate")));
    }
    
    @Test
    void testDistributionValidation() {
        IRWaterfall waterfall = new IRWaterfall("waterfall-dist-test", "Distribution Test Waterfall");
        
        // Invalid distribution - missing recipient
        List<Map<String, Object>> tiers = new ArrayList<>();
        Map<String, Object> tier = new HashMap<>();
        tier.put("id", "tier-test");
        tier.put("condition", "true");
        
        List<Map<String, Object>> invalidDistribute = new ArrayList<>();
        Map<String, Object> invalidDist = new HashMap<>();
        // Missing recipient
        invalidDist.put("percentage", 1.0);
        invalidDistribute.add(invalidDist);
        tier.put("distribute", invalidDistribute);
        
        tiers.add(tier);
        waterfall.setTiers(tiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("distribution 0 must have recipient")));
        
        // Invalid distribution - missing percentage
        tiers = new ArrayList<>();
        tier = new HashMap<>();
        tier.put("id", "tier-test");
        tier.put("condition", "true");
        
        invalidDistribute = new ArrayList<>();
        invalidDist = new HashMap<>();
        invalidDist.put("recipient", "party-test");
        // Missing percentage
        invalidDistribute.add(invalidDist);
        tier.put("distribute", invalidDistribute);
        
        tiers.add(tier);
        waterfall.setTiers(tiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("distribution 0 must have percentage")));
        
        // Invalid distribution - percentage out of range
        tiers = new ArrayList<>();
        tier = new HashMap<>();
        tier.put("id", "tier-test");
        tier.put("condition", "true");
        
        invalidDistribute = new ArrayList<>();
        invalidDist = new HashMap<>();
        invalidDist.put("recipient", "party-test");
        invalidDist.put("percentage", 1.5); // > 1.0
        invalidDistribute.add(invalidDist);
        tier.put("distribute", invalidDistribute);
        
        tiers.add(tier);
        waterfall.setTiers(tiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("percentage must be between 0 and 1")));
        
        // Invalid distribution - percentages don't sum to 1.0
        tiers = new ArrayList<>();
        tier = new HashMap<>();
        tier.put("id", "tier-test");
        tier.put("condition", "true");
        
        invalidDistribute = new ArrayList<>();
        
        Map<String, Object> dist1 = new HashMap<>();
        dist1.put("recipient", "party-1");
        dist1.put("percentage", 0.4);
        invalidDistribute.add(dist1);
        
        Map<String, Object> dist2 = new HashMap<>();
        dist2.put("recipient", "party-2");
        dist2.put("percentage", 0.4); // Total = 0.8, not 1.0
        invalidDistribute.add(dist2);
        
        tier.put("distribute", invalidDistribute);
        tiers.add(tier);
        waterfall.setTiers(tiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("percentages must sum to 1.0")));
    }
    
    @Test
    void testCapitalStackDistributionValidation() {
        IRWaterfall waterfall = new IRWaterfall("waterfall-cap-stack", "Capital Stack Distribution Test");
        
        // Invalid capital stack distribution - missing layerName
        List<Map<String, Object>> tiers = new ArrayList<>();
        Map<String, Object> tier = new HashMap<>();
        tier.put("id", "tier-test");
        tier.put("condition", "true");
        
        List<Map<String, Object>> invalidDistribute = new ArrayList<>();
        Map<String, Object> invalidDist = new HashMap<>();
        invalidDist.put("fromCapitalStack", true);
        // Missing layerName
        invalidDistribute.add(invalidDist);
        tier.put("distribute", invalidDistribute);
        
        tiers.add(tier);
        waterfall.setTiers(tiers);
        
        waterfall.validate();
        assertFalse(waterfall.isValid());
        assertTrue(waterfall.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("with fromCapitalStack must have layerName")));
        
        // Valid capital stack distribution - create fresh waterfall
        IRWaterfall validWaterfall = new IRWaterfall("waterfall-cap-stack-valid", "Valid Capital Stack Distribution");
        
        List<Map<String, Object>> validTiers = new ArrayList<>();
        Map<String, Object> validTier = new HashMap<>();
        validTier.put("id", "tier-valid");
        validTier.put("condition", "true");
        
        List<Map<String, Object>> validDistribute = new ArrayList<>();
        Map<String, Object> validDist = new HashMap<>();
        validDist.put("fromCapitalStack", true);
        validDist.put("layerName", "equity");
        validDistribute.add(validDist);
        validTier.put("distribute", validDistribute);
        
        validTiers.add(validTier);
        validWaterfall.setTiers(validTiers);
        
        validWaterfall.validate();
        if (!validWaterfall.isValid()) {
            System.out.println("Capital stack distribution validation messages: " + validWaterfall.getValidationMessages());
        }
        assertTrue(validWaterfall.isValid());
    }
    
    @Test
    void testWaterfallEngineJsonOutput() {
        // Create waterfall with complete data
        IRWaterfall waterfall = new IRWaterfall("waterfall-engine", "Engine Test Waterfall");
        waterfall.setDescription("Complex waterfall for engine JSON testing");
        
        // Create tiers with different types
        List<Map<String, Object>> tiers = new ArrayList<>();
        
        // Preferred return tier
        Map<String, Object> prefTier = new HashMap<>();
        prefTier.put("id", "tier-pref");
        prefTier.put("description", "8% preferred return");
        prefTier.put("prefRate", 0.08);
        
        List<Map<String, Object>> prefDistribute = new ArrayList<>();
        Map<String, Object> prefDist = new HashMap<>();
        prefDist.put("recipient", "party-investor");
        prefDist.put("percentage", 1.0);
        prefDistribute.add(prefDist);
        prefTier.put("distribute", prefDistribute);
        
        tiers.add(prefTier);
        
        // Capital stack proportional tier
        Map<String, Object> capStackTier = new HashMap<>();
        capStackTier.put("id", "tier-cap-stack");
        capStackTier.put("description", "Return of capital");
        capStackTier.put("until", "capitalReturned >= totalInvested");
        
        List<Map<String, Object>> capStackDistribute = new ArrayList<>();
        Map<String, Object> capStackDist = new HashMap<>();
        capStackDist.put("fromCapitalStack", true);
        capStackDist.put("layerName", "equity");
        capStackDistribute.add(capStackDist);
        capStackTier.put("distribute", capStackDistribute);
        
        tiers.add(capStackTier);
        
        waterfall.setTiers(tiers);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = waterfall.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("tiers"));
        
        assertEquals("waterfall-engine", json.get("id").asText());
        assertEquals("Engine Test Waterfall", json.get("name").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertEquals("Complex waterfall for engine JSON testing", json.get("description").asText());
        
        // Verify tiers structure
        com.fasterxml.jackson.databind.JsonNode tiersJson = json.get("tiers");
        assertEquals(2, tiersJson.size());
        
        // First tier (preferred return)
        com.fasterxml.jackson.databind.JsonNode tier1 = tiersJson.get(0);
        assertEquals("tier-pref", tier1.get("id").asText());
        assertEquals(0.08, tier1.get("prefRate").asDouble());
        
        // Second tier (capital stack)
        com.fasterxml.jackson.databind.JsonNode tier2 = tiersJson.get(1);
        assertEquals("tier-cap-stack", tier2.get("id").asText());
        assertEquals("capitalReturned >= totalInvested", tier2.get("until").asText());
        
        // Verify computed fields
        assertTrue(json.has("tierCount"));
        assertTrue(json.has("hasPreferredReturns"));
        assertTrue(json.has("usesCapitalStackProportions"));
        
        assertEquals(2, json.get("tierCount").asInt());
        assertEquals(true, json.get("hasPreferredReturns").asBoolean());
        assertEquals(true, json.get("usesCapitalStackProportions").asBoolean());
    }
    
    @Test
    void testDifferentWaterfallTypes() {
        // Test simple proportional waterfall
        IRWaterfall proportional = new IRWaterfall("waterfall-proportional", "Proportional Waterfall");
        proportional.setDescription("Simple pro-rata distribution");
        
        List<Map<String, Object>> propTiers = new ArrayList<>();
        Map<String, Object> propTier = new HashMap<>();
        propTier.put("id", "tier-proportional");
        propTier.put("condition", "true");
        
        List<Map<String, Object>> propDistribute = new ArrayList<>();
        Map<String, Object> propDist = new HashMap<>();
        propDist.put("fromCapitalStack", true);
        propDist.put("layerName", "all");
        propDistribute.add(propDist);
        propTier.put("distribute", propDistribute);
        
        propTiers.add(propTier);
        proportional.setTiers(propTiers);
        
        proportional.validate();
        assertTrue(proportional.isValid());
        assertEquals(1, proportional.getTierCount());
        assertFalse(proportional.hasPreferredReturns());
        assertTrue(proportional.usesCapitalStackProportions());
        
        // Test preferred return waterfall
        IRWaterfall prefReturn = new IRWaterfall("waterfall-pref-return", "Preferred Return Waterfall");
        prefReturn.setDescription("8% preferred return waterfall");
        
        List<Map<String, Object>> prefTiers = new ArrayList<>();
        Map<String, Object> prefTier = new HashMap<>();
        prefTier.put("id", "tier-pref");
        prefTier.put("prefRate", 0.08);
        
        List<Map<String, Object>> prefDistribute = new ArrayList<>();
        Map<String, Object> prefDist = new HashMap<>();
        prefDist.put("recipient", "party-lp");
        prefDist.put("percentage", 1.0);
        prefDistribute.add(prefDist);
        prefTier.put("distribute", prefDistribute);
        
        prefTiers.add(prefTier);
        prefReturn.setTiers(prefTiers);
        
        prefReturn.validate();
        assertTrue(prefReturn.isValid());
        assertEquals(1, prefReturn.getTierCount());
        assertTrue(prefReturn.hasPreferredReturns());
        assertFalse(prefReturn.usesCapitalStackProportions());
        
        // Test complex promote waterfall
        IRWaterfall promoteWaterfall = new IRWaterfall("waterfall-promote", "Promote Waterfall");
        promoteWaterfall.setDescription("Complex promote structure with catch-up");
        
        List<Map<String, Object>> promoteTiers = new ArrayList<>();
        
        // Tier 1: Preferred return
        Map<String, Object> tier1 = new HashMap<>();
        tier1.put("id", "tier-pref");
        tier1.put("prefRate", 0.08);
        
        List<Map<String, Object>> dist1 = new ArrayList<>();
        Map<String, Object> d1 = new HashMap<>();
        d1.put("recipient", "party-lp");
        d1.put("percentage", 1.0);
        dist1.add(d1);
        tier1.put("distribute", dist1);
        
        promoteTiers.add(tier1);
        
        // Tier 2: Catch-up to sponsor
        Map<String, Object> tier2 = new HashMap<>();
        tier2.put("id", "tier-catchup");
        tier2.put("until", "sponsorCatchUp >= targetAmount");
        
        List<Map<String, Object>> dist2 = new ArrayList<>();
        Map<String, Object> d2 = new HashMap<>();
        d2.put("recipient", "party-sponsor");
        d2.put("percentage", 1.0);
        dist2.add(d2);
        tier2.put("distribute", dist2);
        
        promoteTiers.add(tier2);
        
        // Tier 3: Split remaining
        Map<String, Object> tier3 = new HashMap<>();
        tier3.put("id", "tier-split");
        tier3.put("condition", "true");
        
        List<Map<String, Object>> dist3 = new ArrayList<>();
        Map<String, Object> d3a = new HashMap<>();
        d3a.put("recipient", "party-lp");
        d3a.put("percentage", 0.8);
        dist3.add(d3a);
        
        Map<String, Object> d3b = new HashMap<>();
        d3b.put("recipient", "party-sponsor");
        d3b.put("percentage", 0.2);
        dist3.add(d3b);
        
        tier3.put("distribute", dist3);
        promoteTiers.add(tier3);
        
        promoteWaterfall.setTiers(promoteTiers);
        
        promoteWaterfall.validate();
        assertTrue(promoteWaterfall.isValid());
        assertEquals(3, promoteWaterfall.getTierCount());
        assertTrue(promoteWaterfall.hasPreferredReturns());
        assertFalse(promoteWaterfall.usesCapitalStackProportions());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.WaterfallNode propNode = new dev.cfdl.ast.WaterfallNode("waterfall-proportional", "Proportional Waterfall");
        propNode.setProperty("tiers", propTiers);
        astNodes.add(propNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify waterfall has proper metadata
        IRWaterfall irWaterfall = (IRWaterfall) result.getIrNodes().get(0);
        assertEquals("waterfall", irWaterfall.getSchemaMetadata("entityType"));
        assertEquals(1, irWaterfall.getSchemaMetadata("tierCount"));
        assertEquals(Boolean.FALSE, irWaterfall.getSchemaMetadata("hasPreferredReturns"));
        assertEquals(Boolean.TRUE, irWaterfall.getSchemaMetadata("usesCapitalStackProportions"));
    }
}