package dev.cfdl.ir;

import dev.cfdl.ast.LogicBlockNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRLogicBlock and LogicBlockNode integration.
 * 
 * Tests the logic block functionality needed for custom calculations,
 * validations, triggers, and data generation in financial models.
 */
public class IRLogicBlockTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testLogicBlockNodeCreation() {
        LogicBlockNode logicBlockNode = new LogicBlockNode("logic-block-noi-calc", "Net Operating Income Calculator");
        logicBlockNode.setDescription("Calculates net operating income by aggregating revenues and subtracting operating expenses");
        logicBlockNode.setScope("asset");
        logicBlockNode.setType("calculation");
        logicBlockNode.setLanguage("julia");
        logicBlockNode.setExecutionOrder(10);
        logicBlockNode.setLineNumber(50);
        logicBlockNode.setColumnNumber(20);
        
        // Add inputs
        logicBlockNode.addInput("stream-rental-revenue");
        logicBlockNode.addInput("stream-parking-revenue");
        logicBlockNode.addInput("stream-operating-expenses");
        logicBlockNode.addInput("stream-property-taxes");
        
        // Add outputs
        logicBlockNode.addOutput("stream-net-operating-income");
        
        // Set code
        String code = "# Calculate Net Operating Income\n" +
                      "total_revenue = stream_rental_revenue + stream_parking_revenue\n" +
                      "total_expenses = stream_operating_expenses + stream_property_taxes\n" +
                      "stream_net_operating_income = total_revenue - total_expenses";
        logicBlockNode.setCode(code);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("category", "financial_calculations");
        metadata.put("version", "1.2.0");
        metadata.put("author", "Financial Modeling Team");
        metadata.put("lastUpdated", "2024-01-15");
        metadata.put("dependencies", List.of("revenue_streams", "expense_streams"));
        logicBlockNode.setProperty("metadata", metadata);
        
        // Verify logic block node
        assertEquals("logic-block-noi-calc", logicBlockNode.getId());
        assertEquals("Net Operating Income Calculator", logicBlockNode.getName());
        assertEquals("Calculates net operating income by aggregating revenues and subtracting operating expenses", logicBlockNode.getDescription());
        assertEquals("asset", logicBlockNode.getScope());
        assertEquals("calculation", logicBlockNode.getType());
        assertEquals("julia", logicBlockNode.getLanguage());
        assertEquals(Integer.valueOf(10), logicBlockNode.getExecutionOrder());
        assertEquals(50, logicBlockNode.getLineNumber());
        assertEquals(20, logicBlockNode.getColumnNumber());
        
        // Verify inputs
        assertEquals(4, logicBlockNode.getInputs().size());
        assertTrue(logicBlockNode.getInputs().contains("stream-rental-revenue"));
        assertTrue(logicBlockNode.getInputs().contains("stream-parking-revenue"));
        assertTrue(logicBlockNode.getInputs().contains("stream-operating-expenses"));
        assertTrue(logicBlockNode.getInputs().contains("stream-property-taxes"));
        
        // Verify outputs
        assertEquals(1, logicBlockNode.getOutputs().size());
        assertTrue(logicBlockNode.getOutputs().contains("stream-net-operating-income"));
        
        // Verify code
        assertTrue(logicBlockNode.getCode().contains("total_revenue = stream_rental_revenue + stream_parking_revenue"));
        assertTrue(logicBlockNode.getCode().contains("stream_net_operating_income = total_revenue - total_expenses"));
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = logicBlockNode.toJson();
        assertEquals("logic-block-noi-calc", json.get("id").asText());
        assertEquals("Net Operating Income Calculator", json.get("name").asText());
        assertEquals("asset", json.get("scope").asText());
        assertEquals("calculation", json.get("type").asText());
        assertEquals("julia", json.get("language").asText());
        assertEquals(10, json.get("executionOrder").asInt());
        assertTrue(json.has("inputs"));
        assertEquals(4, json.get("inputs").size());
        assertTrue(json.has("outputs"));
        assertEquals(1, json.get("outputs").size());
        assertTrue(json.has("code"));
        assertTrue(json.has("metadata"));
        
        if (json.get("metadata").has("category")) {
            assertEquals("financial_calculations", json.get("metadata").get("category").asText());
        }
    }
    
    @Test
    void testIRLogicBlockTransformation() {
        // Create logic block AST node
        LogicBlockNode logicBlockNode = new LogicBlockNode("logic-block-validation", "Cash Flow Validation");
        logicBlockNode.setDescription("Validates that cash flows are within expected ranges");
        logicBlockNode.setScope("deal");
        logicBlockNode.setType("validation");
        logicBlockNode.setLanguage("python");
        logicBlockNode.setExecutionOrder(5);
        
        // Add inputs
        logicBlockNode.addInput("stream-total-cash-flow");
        logicBlockNode.addInput("assumption-min-cash-flow");
        logicBlockNode.addInput("assumption-max-cash-flow");
        
        // Set validation code
        String validationCode = "if stream_total_cash_flow < assumption_min_cash_flow:\n" +
                               "    raise ValidationError(\"Cash flow below minimum threshold\")\n" +
                               "if stream_total_cash_flow > assumption_max_cash_flow:\n" +
                               "    raise ValidationError(\"Cash flow above maximum threshold\")";
        logicBlockNode.setCode(validationCode);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(logicBlockNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRLogicBlock irLogicBlock = (IRLogicBlock) result.getIrNodes().get(0);
        
        // Verify IR logic block
        assertEquals("logic-block-validation", irLogicBlock.getId());
        assertEquals("Cash Flow Validation", irLogicBlock.getName());
        assertEquals("Validates that cash flows are within expected ranges", irLogicBlock.getDescription());
        assertEquals("deal", irLogicBlock.getScope());
        assertEquals("validation", irLogicBlock.getType());
        assertEquals("python", irLogicBlock.getLanguage());
        assertEquals(Integer.valueOf(5), irLogicBlock.getExecutionOrder());
        
        // Verify inputs
        assertNotNull(irLogicBlock.getInputs());
        assertEquals(3, irLogicBlock.getInputs().size());
        assertTrue(irLogicBlock.getInputs().contains("stream-total-cash-flow"));
        assertTrue(irLogicBlock.getInputs().contains("assumption-min-cash-flow"));
        assertTrue(irLogicBlock.getInputs().contains("assumption-max-cash-flow"));
        
        // Verify outputs
        assertNotNull(irLogicBlock.getOutputs());
        assertEquals(0, irLogicBlock.getOutputs().size()); // Validation blocks typically don't produce outputs
        
        // Verify code
        assertTrue(irLogicBlock.getCode().contains("ValidationError"));
        assertTrue(irLogicBlock.getCode().contains("stream_total_cash_flow < assumption_min_cash_flow"));
        
        // Verify computed properties
        assertEquals(3, irLogicBlock.getInputCount());
        assertEquals(0, irLogicBlock.getOutputCount());
        assertTrue(irLogicBlock.hasExecutionOrder());
        assertTrue(irLogicBlock.hasLanguage());
        assertFalse(irLogicBlock.isCalculation());
        assertTrue(irLogicBlock.isValidation());
        assertFalse(irLogicBlock.isTrigger());
        
        // Verify dependencies
        assertTrue(irLogicBlock.getDependencies().contains("stream-total-cash-flow"));
        assertTrue(irLogicBlock.getDependencies().contains("assumption-min-cash-flow"));
        assertTrue(irLogicBlock.getDependencies().contains("assumption-max-cash-flow"));
        
        // Verify metadata enrichment
        assertEquals("logicBlock", irLogicBlock.getSchemaMetadata("entityType"));
        assertEquals("deal", irLogicBlock.getSchemaMetadata("blockScope"));
        assertEquals("validation", irLogicBlock.getSchemaMetadata("blockType"));
        assertEquals(3, irLogicBlock.getSchemaMetadata("inputCount"));
        assertEquals(0, irLogicBlock.getSchemaMetadata("outputCount"));
        assertEquals(Boolean.TRUE, irLogicBlock.getSchemaMetadata("hasExecutionOrder"));
        assertEquals(Boolean.TRUE, irLogicBlock.getSchemaMetadata("hasLanguage"));
        assertEquals(Boolean.FALSE, irLogicBlock.getSchemaMetadata("isCalculation"));
        assertEquals(Boolean.TRUE, irLogicBlock.getSchemaMetadata("isValidation"));
        assertEquals(Boolean.FALSE, irLogicBlock.getSchemaMetadata("isTrigger"));
        assertEquals(5, irLogicBlock.getSchemaMetadata("executionOrder"));
        assertEquals("python", irLogicBlock.getSchemaMetadata("language"));
        assertNotNull(irLogicBlock.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRLogicBlockValidation() {
        // Test valid logic block
        IRLogicBlock validLogicBlock = new IRLogicBlock("logic-block-valid", "Valid Logic Block");
        validLogicBlock.setDescription("Valid logic block for testing");
        validLogicBlock.setScope("component");
        validLogicBlock.setType("calculation");
        validLogicBlock.setCode("result = input1 + input2");
        
        validLogicBlock.validate();
        assertTrue(validLogicBlock.isValid());
        assertEquals(0, validLogicBlock.getValidationMessages().size());
        
        // Test invalid logic block - missing required fields
        IRLogicBlock invalidLogicBlock = new IRLogicBlock();
        // Missing id, name, scope, type, code
        
        invalidLogicBlock.validate();
        assertFalse(invalidLogicBlock.isValid());
        assertTrue(invalidLogicBlock.getValidationMessages().size() >= 5);
        assertTrue(invalidLogicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidLogicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidLogicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("scope is required")));
        assertTrue(invalidLogicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("type is required")));
        assertTrue(invalidLogicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("code is required")));
    }
    
    @Test
    void testLogicBlockScopeValidation() {
        IRLogicBlock logicBlock = new IRLogicBlock("logic-block-scope-test", "Scope Test");
        logicBlock.setType("calculation");
        logicBlock.setCode("result = 1");
        
        // Valid scopes
        String[] validScopes = {"component", "asset", "deal", "portfolio", "fund"};
        for (String validScope : validScopes) {
            logicBlock.setScope(validScope);
            logicBlock.validate();
            assertTrue(logicBlock.isValid(), "Scope '" + validScope + "' should be valid");
        }
        
        // Invalid scope
        logicBlock.setScope("invalid-scope");
        logicBlock.validate();
        assertFalse(logicBlock.isValid());
        assertTrue(logicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("scope must be one of")));
    }
    
    @Test
    void testLogicBlockTypeValidation() {
        IRLogicBlock logicBlock = new IRLogicBlock("logic-block-type-test", "Type Test");
        logicBlock.setScope("asset");
        logicBlock.setCode("result = 1");
        
        // Valid types
        String[] validTypes = {"calculation", "aggregation", "validation", "trigger", "generator", "custom"};
        for (String validType : validTypes) {
            logicBlock.setType(validType);
            logicBlock.validate();
            assertTrue(logicBlock.isValid(), "Type '" + validType + "' should be valid");
        }
        
        // Invalid type
        logicBlock.setType("invalid-type");
        logicBlock.validate();
        assertFalse(logicBlock.isValid());
        assertTrue(logicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("type must be one of")));
    }
    
    @Test
    void testInputOutputValidation() {
        IRLogicBlock logicBlock = new IRLogicBlock("logic-block-io-test", "Input Output Test");
        logicBlock.setScope("asset");
        logicBlock.setType("calculation");
        logicBlock.setCode("result = 1");
        
        // Null input
        List<String> inputsWithNull = new ArrayList<>();
        inputsWithNull.add("valid-input");
        inputsWithNull.add(null);
        logicBlock.setInputs(inputsWithNull);
        
        logicBlock.validate();
        assertFalse(logicBlock.isValid());
        assertTrue(logicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("input 1 cannot be null or empty")));
        
        // Empty input
        List<String> inputsWithEmpty = new ArrayList<>();
        inputsWithEmpty.add("valid-input");
        inputsWithEmpty.add("");
        logicBlock.setInputs(inputsWithEmpty);
        
        logicBlock.validate();
        assertFalse(logicBlock.isValid());
        assertTrue(logicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("input 1 cannot be null or empty")));
        
        // Null output
        List<String> outputsWithNull = new ArrayList<>();
        outputsWithNull.add("valid-output");
        outputsWithNull.add(null);
        logicBlock.setOutputs(outputsWithNull);
        
        logicBlock.validate();
        assertFalse(logicBlock.isValid());
        assertTrue(logicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("output 1 cannot be null or empty")));
        
        // Empty output
        List<String> outputsWithEmpty = new ArrayList<>();
        outputsWithEmpty.add("valid-output");
        outputsWithEmpty.add("");
        logicBlock.setOutputs(outputsWithEmpty);
        
        logicBlock.validate();
        assertFalse(logicBlock.isValid());
        assertTrue(logicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("output 1 cannot be null or empty")));
    }
    
    @Test
    void testExecutionOrderValidation() {
        IRLogicBlock logicBlock = new IRLogicBlock("logic-block-exec-test", "Execution Order Test");
        logicBlock.setScope("asset");
        logicBlock.setType("calculation");
        logicBlock.setCode("result = 1");
        
        // Valid execution orders
        logicBlock.setExecutionOrder(0);
        logicBlock.validate();
        assertTrue(logicBlock.isValid());
        
        logicBlock.setExecutionOrder(100);
        logicBlock.validate();
        assertTrue(logicBlock.isValid());
        
        // Invalid execution order (negative)
        logicBlock.setExecutionOrder(-1);
        logicBlock.validate();
        assertFalse(logicBlock.isValid());
        assertTrue(logicBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("executionOrder must be non-negative")));
    }
    
    @Test
    void testLogicBlockEngineJsonOutput() {
        // Create logic block with complete data
        IRLogicBlock logicBlock = new IRLogicBlock("logic-block-engine", "Engine Test Logic Block");
        logicBlock.setDescription("Complex logic block for engine JSON testing");
        logicBlock.setScope("portfolio");
        logicBlock.setType("aggregation");
        logicBlock.setLanguage("julia");
        logicBlock.setExecutionOrder(15);
        
        // Add inputs
        logicBlock.addInput("stream-asset-1-noi");
        logicBlock.addInput("stream-asset-2-noi");
        logicBlock.addInput("stream-asset-3-noi");
        
        // Add outputs
        logicBlock.addOutput("stream-portfolio-total-noi");
        logicBlock.addOutput("stream-portfolio-avg-noi");
        
        // Set code
        String aggregationCode = "# Portfolio-level NOI aggregation\n" +
                                "total_noi = stream_asset_1_noi + stream_asset_2_noi + stream_asset_3_noi\n" +
                                "avg_noi = total_noi / 3\n" +
                                "\n" +
                                "stream_portfolio_total_noi = total_noi\n" +
                                "stream_portfolio_avg_noi = avg_noi";
        logicBlock.setCode(aggregationCode);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = logicBlock.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("scope"));
        assertTrue(json.has("type"));
        assertTrue(json.has("code"));
        
        assertEquals("logic-block-engine", json.get("id").asText());
        assertEquals("Engine Test Logic Block", json.get("name").asText());
        assertEquals("portfolio", json.get("scope").asText());
        assertEquals("aggregation", json.get("type").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertTrue(json.has("language"));
        assertTrue(json.has("executionOrder"));
        assertEquals("Complex logic block for engine JSON testing", json.get("description").asText());
        assertEquals("julia", json.get("language").asText());
        assertEquals(15, json.get("executionOrder").asInt());
        
        // Verify inputs array
        assertTrue(json.has("inputs"));
        com.fasterxml.jackson.databind.JsonNode inputsJson = json.get("inputs");
        assertEquals(3, inputsJson.size());
        assertEquals("stream-asset-1-noi", inputsJson.get(0).asText());
        assertEquals("stream-asset-2-noi", inputsJson.get(1).asText());
        assertEquals("stream-asset-3-noi", inputsJson.get(2).asText());
        
        // Verify outputs array
        assertTrue(json.has("outputs"));
        com.fasterxml.jackson.databind.JsonNode outputsJson = json.get("outputs");
        assertEquals(2, outputsJson.size());
        assertEquals("stream-portfolio-total-noi", outputsJson.get(0).asText());
        assertEquals("stream-portfolio-avg-noi", outputsJson.get(1).asText());
        
        // Verify code
        assertTrue(json.has("code"));
        assertTrue(json.get("code").asText().contains("total_noi = stream_asset_1_noi + stream_asset_2_noi + stream_asset_3_noi"));
        
        // Verify computed fields
        assertTrue(json.has("inputCount"));
        assertTrue(json.has("outputCount"));
        assertTrue(json.has("hasExecutionOrder"));
        assertTrue(json.has("hasLanguage"));
        assertTrue(json.has("isCalculation"));
        assertTrue(json.has("isValidation"));
        assertTrue(json.has("isTrigger"));
        
        assertEquals(3, json.get("inputCount").asInt());
        assertEquals(2, json.get("outputCount").asInt());
        assertEquals(true, json.get("hasExecutionOrder").asBoolean());
        assertEquals(true, json.get("hasLanguage").asBoolean());
        assertEquals(false, json.get("isCalculation").asBoolean());
        assertEquals(false, json.get("isValidation").asBoolean());
        assertEquals(false, json.get("isTrigger").asBoolean());
    }
    
    @Test
    void testDifferentLogicBlockTypes() {
        // Test calculation logic block
        IRLogicBlock calculationBlock = new IRLogicBlock("logic-block-calc", "Calculation Block");
        calculationBlock.setDescription("Calculates weighted average cap rate");
        calculationBlock.setScope("fund");
        calculationBlock.setType("calculation");
        calculationBlock.setCode("weighted_cap_rate = (asset1_value * asset1_cap_rate + asset2_value * asset2_cap_rate) / (asset1_value + asset2_value)");
        calculationBlock.addInput("asset1-value");
        calculationBlock.addInput("asset1-cap-rate");
        calculationBlock.addInput("asset2-value");
        calculationBlock.addInput("asset2-cap-rate");
        calculationBlock.addOutput("fund-weighted-cap-rate");
        
        calculationBlock.validate();
        assertTrue(calculationBlock.isValid());
        assertEquals("calculation", calculationBlock.getType());
        assertTrue(calculationBlock.isCalculation());
        assertFalse(calculationBlock.isValidation());
        assertFalse(calculationBlock.isTrigger());
        assertEquals(4, calculationBlock.getInputCount());
        assertEquals(1, calculationBlock.getOutputCount());
        
        // Test trigger logic block
        IRLogicBlock triggerBlock = new IRLogicBlock("logic-block-trigger", "Rebalancing Trigger");
        triggerBlock.setDescription("Triggers portfolio rebalancing when allocation drifts");
        triggerBlock.setScope("portfolio");
        triggerBlock.setType("trigger");
        triggerBlock.setLanguage("python");
        triggerBlock.setExecutionOrder(1);
        triggerBlock.setCode("if abs(current_allocation - target_allocation) > drift_threshold: trigger_rebalance()");
        triggerBlock.addInput("current-allocation");
        triggerBlock.addInput("target-allocation");
        triggerBlock.addInput("drift-threshold");
        
        triggerBlock.validate();
        assertTrue(triggerBlock.isValid());
        assertEquals("trigger", triggerBlock.getType());
        assertFalse(triggerBlock.isCalculation());
        assertFalse(triggerBlock.isValidation());
        assertTrue(triggerBlock.isTrigger());
        assertEquals(3, triggerBlock.getInputCount());
        assertEquals(0, triggerBlock.getOutputCount());
        assertTrue(triggerBlock.hasExecutionOrder());
        assertTrue(triggerBlock.hasLanguage());
        
        // Test generator logic block
        IRLogicBlock generatorBlock = new IRLogicBlock("logic-block-generator", "Scenario Generator");
        generatorBlock.setDescription("Generates Monte Carlo scenarios for stress testing");
        generatorBlock.setScope("deal");
        generatorBlock.setType("generator");
        generatorBlock.setLanguage("julia");
        generatorBlock.setCode("scenarios = generate_monte_carlo_scenarios(n_scenarios, volatility_params)");
        generatorBlock.addInput("volatility-params");
        generatorBlock.addOutput("monte-carlo-scenarios");
        
        generatorBlock.validate();
        assertTrue(generatorBlock.isValid());
        assertEquals("generator", generatorBlock.getType());
        assertFalse(generatorBlock.isCalculation());
        assertFalse(generatorBlock.isValidation());
        assertFalse(generatorBlock.isTrigger());
        assertEquals(1, generatorBlock.getInputCount());
        assertEquals(1, generatorBlock.getOutputCount());
        
        // Test custom logic block without execution order or language
        IRLogicBlock customBlock = new IRLogicBlock("logic-block-custom", "Custom Block");
        customBlock.setDescription("Custom business logic for specific client requirements");
        customBlock.setScope("component");
        customBlock.setType("custom");
        customBlock.setCode("// Custom implementation specific to client XYZ");
        
        customBlock.validate();
        assertTrue(customBlock.isValid());
        assertEquals("custom", customBlock.getType());
        assertFalse(customBlock.isCalculation());
        assertFalse(customBlock.isValidation());
        assertFalse(customBlock.isTrigger());
        assertEquals(0, customBlock.getInputCount());
        assertEquals(0, customBlock.getOutputCount());
        assertFalse(customBlock.hasExecutionOrder());
        assertFalse(customBlock.hasLanguage());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.LogicBlockNode simpleNode = new dev.cfdl.ast.LogicBlockNode("logic-block-simple", "Simple Block");
        simpleNode.setScope("asset");
        simpleNode.setType("calculation");
        simpleNode.setCode("result = input + 1");
        astNodes.add(simpleNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify logic block has proper metadata
        IRLogicBlock irLogicBlock = (IRLogicBlock) result.getIrNodes().get(0);
        assertEquals("logicBlock", irLogicBlock.getSchemaMetadata("entityType"));
        assertEquals("asset", irLogicBlock.getSchemaMetadata("blockScope"));
        assertEquals("calculation", irLogicBlock.getSchemaMetadata("blockType"));
        assertEquals(0, irLogicBlock.getSchemaMetadata("inputCount"));
        assertEquals(0, irLogicBlock.getSchemaMetadata("outputCount"));
        assertEquals(Boolean.FALSE, irLogicBlock.getSchemaMetadata("hasExecutionOrder"));
        assertEquals(Boolean.FALSE, irLogicBlock.getSchemaMetadata("hasLanguage"));
        assertEquals(Boolean.TRUE, irLogicBlock.getSchemaMetadata("isCalculation"));
        assertEquals(Boolean.FALSE, irLogicBlock.getSchemaMetadata("isValidation"));
        assertEquals(Boolean.FALSE, irLogicBlock.getSchemaMetadata("isTrigger"));
    }
}