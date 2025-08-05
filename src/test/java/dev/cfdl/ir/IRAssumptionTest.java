package dev.cfdl.ir;

import dev.cfdl.ast.AssumptionNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRAssumption and AssumptionNode integration.
 * 
 * Tests the assumption functionality needed for economic modeling,
 * including fixed values, statistical distributions, time series, and expressions.
 */
public class IRAssumptionTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testAssumptionNodeCreation() {
        AssumptionNode assumptionNode = new AssumptionNode("assumption-rent-growth", "Annual Rent Growth Rate");
        assumptionNode.setCategory("revenue");
        assumptionNode.setScope("asset");
        assumptionNode.setType("fixed");
        assumptionNode.setUnit("%");
        assumptionNode.setSource("Market Research Q4 2023");
        assumptionNode.setLineNumber(15);
        assumptionNode.setColumnNumber(4);
        
        // Set fixed value
        assumptionNode.setProperty("value", 3.5); // 3.5% annual growth
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("sensitivity", "high");
        metadata.put("confidenceLevel", 0.85);
        metadata.put("lastUpdated", "2024-01-15");
        assumptionNode.setProperty("metadata", metadata);
        
        // Verify assumption node
        assertEquals("assumption-rent-growth", assumptionNode.getId());
        assertEquals("Annual Rent Growth Rate", assumptionNode.getName());
        assertEquals("revenue", assumptionNode.getCategory());
        assertEquals("asset", assumptionNode.getScope());
        assertEquals("fixed", assumptionNode.getType());
        assertEquals("%", assumptionNode.getUnit());
        assertEquals("Market Research Q4 2023", assumptionNode.getSource());
        assertEquals(15, assumptionNode.getLineNumber());
        assertEquals(4, assumptionNode.getColumnNumber());
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = assumptionNode.toJson();
        assertEquals("assumption-rent-growth", json.get("id").asText());
        assertEquals("revenue", json.get("category").asText());
        assertEquals("fixed", json.get("type").asText());
        assertTrue(json.has("value"));
        assertEquals(3.5, json.get("value").asDouble());
        assertTrue(json.has("metadata"));
        if (json.get("metadata").has("sensitivity")) {
            assertEquals("high", json.get("metadata").get("sensitivity").asText());
        }
    }
    
    @Test
    void testIRAssumptionTransformation() {
        // Create assumption AST node
        AssumptionNode assumptionNode = new AssumptionNode("assumption-cap-rate", "Exit Cap Rate");
        assumptionNode.setCategory("capital");
        assumptionNode.setScope("deal");
        assumptionNode.setType("distribution");
        assumptionNode.setUnit("%");
        assumptionNode.setSource("Industry Analysis");
        
        // Add distribution
        Map<String, Object> distribution = new HashMap<>();
        distribution.put("type", "normal");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mean", 6.5);
        parameters.put("stdDev", 0.75);
        distribution.put("parameters", parameters);
        assumptionNode.setProperty("distribution", distribution);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(assumptionNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRAssumption irAssumption = (IRAssumption) result.getIrNodes().get(0);
        
        // Verify IR assumption
        assertEquals("assumption-cap-rate", irAssumption.getId());
        assertEquals("Exit Cap Rate", irAssumption.getName());
        assertEquals("capital", irAssumption.getCategory());
        assertEquals("deal", irAssumption.getScope());
        assertEquals("distribution", irAssumption.getType());
        assertEquals("%", irAssumption.getUnit());
        assertEquals("Industry Analysis", irAssumption.getSource());
        
        // Verify distribution
        assertNotNull(irAssumption.getDistribution());
        assertEquals("normal", irAssumption.getDistribution().get("type"));
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) irAssumption.getDistribution().get("parameters");
        assertEquals(6.5, params.get("mean"));
        
        // Verify computed properties
        assertTrue(irAssumption.isStochastic());
        assertFalse(irAssumption.isTimeVarying());
        
        // Verify metadata enrichment
        assertEquals("assumption", irAssumption.getSchemaMetadata("entityType"));
        assertEquals("capital", irAssumption.getSchemaMetadata("assumptionCategory"));
        assertEquals("deal", irAssumption.getSchemaMetadata("assumptionScope"));
        assertEquals("distribution", irAssumption.getSchemaMetadata("assumptionType"));
        assertEquals(Boolean.TRUE, irAssumption.getSchemaMetadata("isStochastic"));
        assertEquals(Boolean.FALSE, irAssumption.getSchemaMetadata("isTimeVarying"));
        assertNotNull(irAssumption.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRAssumptionValidation() {
        // Test valid assumption
        IRAssumption validAssumption = new IRAssumption("assumption-valid", "Valid Assumption");
        validAssumption.setCategory("revenue");
        validAssumption.setScope("component");
        validAssumption.setType("fixed");
        validAssumption.setValue(2.5);
        
        validAssumption.validate();
        assertTrue(validAssumption.isValid());
        assertEquals(0, validAssumption.getValidationMessages().size());
        
        // Test invalid assumption - missing required fields
        IRAssumption invalidAssumption = new IRAssumption();
        // Missing id, category, scope, type
        
        invalidAssumption.validate();
        assertFalse(invalidAssumption.isValid());
        assertTrue(invalidAssumption.getValidationMessages().size() >= 4);
        assertTrue(invalidAssumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidAssumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("category is required")));
        assertTrue(invalidAssumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("scope is required")));
        assertTrue(invalidAssumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("type is required")));
    }
    
    @Test
    void testAssumptionCategoryValidation() {
        IRAssumption assumption = new IRAssumption("assumption-category-test", "Category Test");
        assumption.setScope("asset");
        assumption.setType("fixed");
        assumption.setValue(1.0);
        
        // Valid categories
        String[] validCategories = {"revenue", "expense", "capital", "leasing", "timing", "financing", "other"};
        for (String validCategory : validCategories) {
            assumption.setCategory(validCategory);
            assumption.validate();
            assertTrue(assumption.isValid(), "Category '" + validCategory + "' should be valid");
        }
        
        // Invalid category
        assumption.setCategory("invalid-category");
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("category must be one of")));
    }
    
    @Test
    void testAssumptionScopeValidation() {
        IRAssumption assumption = new IRAssumption("assumption-scope-test", "Scope Test");
        assumption.setCategory("revenue");
        assumption.setType("fixed");
        assumption.setValue(1.0);
        
        // Valid scopes
        String[] validScopes = {"component", "asset", "deal", "portfolio", "fund"};
        for (String validScope : validScopes) {
            assumption.setScope(validScope);
            assumption.validate();
            assertTrue(assumption.isValid(), "Scope '" + validScope + "' should be valid");
        }
        
        // Invalid scope
        assumption.setScope("invalid-scope");
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("scope must be one of")));
    }
    
    @Test
    void testAssumptionTypeValidation() {
        IRAssumption assumption = new IRAssumption("assumption-type-test", "Type Test");
        assumption.setCategory("revenue");
        assumption.setScope("asset");
        
        // Valid types
        String[] validTypes = {"fixed", "distribution", "table", "expression"};
        for (String validType : validTypes) {
            assumption.setType(validType);
            if ("distribution".equals(validType)) {
                // Distribution type requires distribution specification
                Map<String, Object> distribution = new HashMap<>();
                distribution.put("type", "normal");
                distribution.put("parameters", Map.of("mean", 1.0, "stdDev", 0.1));
                assumption.setDistribution(distribution);
            } else {
                assumption.setValue(1.0);
                assumption.setDistribution(null);
            }
            
            assumption.validate();
            assertTrue(assumption.isValid(), "Type '" + validType + "' should be valid");
        }
        
        // Invalid type
        assumption.setType("invalid-type");
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("type must be one of")));
    }
    
    @Test
    void testDistributionValidation() {
        IRAssumption assumption = new IRAssumption("assumption-dist-test", "Distribution Test");
        assumption.setCategory("revenue");
        assumption.setScope("asset");
        assumption.setType("distribution");
        
        // Missing distribution
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have distribution specification")));
        
        // Invalid distribution - missing type
        Map<String, Object> invalidDist = new HashMap<>();
        invalidDist.put("parameters", Map.of("mean", 1.0));
        // Missing type
        assumption.setDistribution(invalidDist);
        
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("distribution must have type")));
        
        // Invalid distribution - missing parameters
        invalidDist = new HashMap<>();
        invalidDist.put("type", "normal");
        // Missing parameters
        assumption.setDistribution(invalidDist);
        
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("distribution must have parameters")));
        
        // Valid distribution types
        String[] validDistTypes = {"normal", "uniform", "triangular", "lognormal", "beta", "custom"};
        for (String validDistType : validDistTypes) {
            IRAssumption testAssumption = new IRAssumption("assumption-dist-valid-" + validDistType, "Valid Dist " + validDistType);
            testAssumption.setCategory("revenue");
            testAssumption.setScope("asset");
            testAssumption.setType("distribution");
            
            Map<String, Object> validDist = new HashMap<>();
            validDist.put("type", validDistType);
            validDist.put("parameters", Map.of("param1", 1.0));
            testAssumption.setDistribution(validDist);
            
            testAssumption.validate();
            if (!testAssumption.isValid()) {
                System.out.println("Distribution validation messages for " + validDistType + ": " + testAssumption.getValidationMessages());
            }
            assertTrue(testAssumption.isValid(), "Distribution type '" + validDistType + "' should be valid");
        }
        
        // Invalid distribution type
        Map<String, Object> invalidDistType = new HashMap<>();
        invalidDistType.put("type", "invalid-dist-type");
        invalidDistType.put("parameters", Map.of("param1", 1.0));
        assumption.setDistribution(invalidDistType);
        
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("distribution type must be one of")));
    }
    
    @Test
    void testTimeSeriesValidation() {
        IRAssumption assumption = new IRAssumption("assumption-ts-test", "Time Series Test");
        assumption.setCategory("revenue");
        assumption.setScope("asset");
        assumption.setType("fixed");
        
        // Invalid time series - missing date
        List<Map<String, Object>> invalidTimeSeries = new ArrayList<>();
        Map<String, Object> invalidTimePoint = new HashMap<>();
        invalidTimePoint.put("value", 2.5);
        // Missing date
        invalidTimeSeries.add(invalidTimePoint);
        assumption.setTimeSeries(invalidTimeSeries);
        
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("timeSeries item 0 must have date")));
        
        // Invalid time series - missing value
        invalidTimeSeries = new ArrayList<>();
        invalidTimePoint = new HashMap<>();
        invalidTimePoint.put("date", "2024-01-01");
        // Missing value
        invalidTimeSeries.add(invalidTimePoint);
        assumption.setTimeSeries(invalidTimeSeries);
        
        assumption.validate();
        assertFalse(assumption.isValid());
        assertTrue(assumption.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("timeSeries item 0 must have value")));
        
        // Valid time series
        IRAssumption validAssumption = new IRAssumption("assumption-ts-valid", "Valid Time Series");
        validAssumption.setCategory("revenue");
        validAssumption.setScope("asset");
        validAssumption.setType("fixed");
        
        List<Map<String, Object>> validTimeSeries = new ArrayList<>();
        Map<String, Object> validTimePoint = new HashMap<>();
        validTimePoint.put("date", "2024-01-01");
        validTimePoint.put("value", 2.5);
        validTimeSeries.add(validTimePoint);
        validAssumption.setTimeSeries(validTimeSeries);
        
        validAssumption.validate();
        if (!validAssumption.isValid()) {
            System.out.println("Time series validation messages: " + validAssumption.getValidationMessages());
        }
        assertTrue(validAssumption.isValid());
        assertTrue(validAssumption.isTimeVarying());
    }
    
    @Test
    void testAssumptionEngineJsonOutput() {
        // Create assumption with complete data
        IRAssumption assumption = new IRAssumption("assumption-engine", "Engine Test Assumption");
        assumption.setCategory("expense");
        assumption.setScope("component");
        assumption.setType("distribution");
        assumption.setUnit("$/sf");
        assumption.setSource("Market Analysis");
        assumption.setMarketDataRef("market-data-insurance-rates");
        
        // Distribution
        Map<String, Object> distribution = new HashMap<>();
        distribution.put("type", "triangular");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("min", 2.5);
        parameters.put("mode", 3.0);
        parameters.put("max", 4.0);
        distribution.put("parameters", parameters);
        assumption.setDistribution(distribution);
        
        // Time series override
        List<Map<String, Object>> timeSeries = new ArrayList<>();
        Map<String, Object> override1 = new HashMap<>();
        override1.put("date", "2025-01-01");
        override1.put("value", 3.5);
        timeSeries.add(override1);
        assumption.setTimeSeries(timeSeries);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = assumption.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("category"));
        assertTrue(json.has("scope"));
        assertTrue(json.has("type"));
        
        assertEquals("assumption-engine", json.get("id").asText());
        assertEquals("expense", json.get("category").asText());
        assertEquals("distribution", json.get("type").asText());
        
        // Verify optional fields
        assertTrue(json.has("name"));
        assertTrue(json.has("unit"));
        assertTrue(json.has("source"));
        assertTrue(json.has("marketDataRef"));
        assertTrue(json.has("distribution"));
        assertTrue(json.has("timeSeries"));
        
        // Verify distribution structure
        com.fasterxml.jackson.databind.JsonNode distJson = json.get("distribution");
        assertEquals("triangular", distJson.get("type").asText());
        assertEquals(3.0, distJson.get("parameters").get("mode").asDouble());
        
        // Verify computed fields
        assertTrue(json.has("isStochastic"));
        assertTrue(json.has("isTimeVarying"));
        assertEquals(true, json.get("isStochastic").asBoolean());
        assertEquals(true, json.get("isTimeVarying").asBoolean());
    }
    
    @Test
    void testDifferentAssumptionTypes() {
        // Test fixed assumption
        IRAssumption fixedAssumption = new IRAssumption("assumption-fixed", "Fixed Growth Rate");
        fixedAssumption.setCategory("revenue");
        fixedAssumption.setScope("asset");
        fixedAssumption.setType("fixed");
        fixedAssumption.setValue(2.5);
        fixedAssumption.setUnit("%");
        
        fixedAssumption.validate();
        assertTrue(fixedAssumption.isValid());
        assertFalse(fixedAssumption.isStochastic());
        assertFalse(fixedAssumption.isTimeVarying());
        
        // Test distribution assumption
        IRAssumption distributionAssumption = new IRAssumption("assumption-dist", "Variable Cap Rate");
        distributionAssumption.setCategory("capital");
        distributionAssumption.setScope("deal");
        distributionAssumption.setType("distribution");
        
        Map<String, Object> normalDist = new HashMap<>();
        normalDist.put("type", "normal");
        normalDist.put("parameters", Map.of("mean", 6.0, "stdDev", 0.5));
        distributionAssumption.setDistribution(normalDist);
        
        distributionAssumption.validate();
        assertTrue(distributionAssumption.isValid());
        assertTrue(distributionAssumption.isStochastic());
        assertFalse(distributionAssumption.isTimeVarying());
        
        // Test time-varying assumption
        IRAssumption timeVaryingAssumption = new IRAssumption("assumption-time", "Escalating Rent");
        timeVaryingAssumption.setCategory("revenue");
        timeVaryingAssumption.setScope("component");
        timeVaryingAssumption.setType("fixed");
        timeVaryingAssumption.setValue(100.0); // Base value
        
        List<Map<String, Object>> escalations = new ArrayList<>();
        Map<String, Object> year1 = new HashMap<>();
        year1.put("date", "2025-01-01");
        year1.put("value", 103.0);
        escalations.add(year1);
        
        Map<String, Object> year2 = new HashMap<>();
        year2.put("date", "2026-01-01");
        year2.put("value", 106.09);
        escalations.add(year2);
        
        timeVaryingAssumption.setTimeSeries(escalations);
        
        timeVaryingAssumption.validate();
        assertTrue(timeVaryingAssumption.isValid());
        assertFalse(timeVaryingAssumption.isStochastic());
        assertTrue(timeVaryingAssumption.isTimeVarying());
        
        // Test template-based assumption
        IRAssumption templateAssumption = new IRAssumption("assumption-template", "From Template");
        templateAssumption.setCategory("expense");
        templateAssumption.setScope("asset");
        templateAssumption.setType("fixed");
        templateAssumption.setTemplate("template-operating-expense");
        
        Map<String, Object> overrides = new HashMap<>();
        overrides.put("baseValue", 5.0);
        overrides.put("escalationRate", 0.025);
        templateAssumption.setOverrides(overrides);
        
        templateAssumption.validate();
        if (!templateAssumption.isValid()) {
            System.out.println("Template validation messages: " + templateAssumption.getValidationMessages());
        }
        assertTrue(templateAssumption.isValid());
        
        // Verify dependencies
        assertTrue(templateAssumption.getDependencies().contains("template-operating-expense"));
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.AssumptionNode fixedNode = new dev.cfdl.ast.AssumptionNode("assumption-fixed", "Fixed Growth Rate");
        fixedNode.setCategory("revenue");
        fixedNode.setScope("asset");
        fixedNode.setType("fixed");
        fixedNode.setProperty("value", 2.5);
        astNodes.add(fixedNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify assumption has proper metadata
        IRAssumption irAssumption = (IRAssumption) result.getIrNodes().get(0);
        assertEquals("assumption", irAssumption.getSchemaMetadata("entityType"));
        assertEquals("revenue", irAssumption.getSchemaMetadata("assumptionCategory"));
        assertEquals("asset", irAssumption.getSchemaMetadata("assumptionScope"));
        assertEquals("fixed", irAssumption.getSchemaMetadata("assumptionType"));
        assertEquals(Boolean.FALSE, irAssumption.getSchemaMetadata("isStochastic"));
        assertEquals(Boolean.FALSE, irAssumption.getSchemaMetadata("isTimeVarying"));
    }
}