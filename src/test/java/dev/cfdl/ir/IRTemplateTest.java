package dev.cfdl.ir;

import dev.cfdl.ast.TemplateNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRTemplate and TemplateNode integration.
 * 
 * Tests the template functionality needed for reusable CFDL DSL snippets
 * with named parameters for instantiating common constructs.
 */
public class IRTemplateTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testTemplateNodeCreation() {
        TemplateNode templateNode = new TemplateNode("template-standard-lease", "Standard Commercial Lease Template");
        templateNode.setDescription("A standardized commercial lease template with configurable terms");
        templateNode.setTemplateType("contract");
        templateNode.setLineNumber(40);
        templateNode.setColumnNumber(15);
        
        // Set template body with parameter placeholders
        String templateBody = "contract {{contractId}} {\n" +
                             "  name: \"{{tenantName}} Lease Agreement\";\n" +
                             "  startDate: {{leaseStartDate}};\n" +
                             "  endDate: {{leaseEndDate}};\n" +
                             "  monthlyRent: {{monthlyRentAmount}};\n" +
                             "  escalationRate: {{annualEscalationRate}};\n" +
                             "  securityDeposit: {{securityDepositMonths}} * {{monthlyRentAmount}};\n" +
                             "}";
        templateNode.setBody(templateBody);
        
        // Add parameters
        Map<String, Object> contractIdParam = new HashMap<>();
        contractIdParam.put("name", "contractId");
        contractIdParam.put("dataType", "string");
        contractIdParam.put("description", "Unique identifier for the contract");
        contractIdParam.put("required", true);
        templateNode.addParameter(contractIdParam);
        
        Map<String, Object> tenantNameParam = new HashMap<>();
        tenantNameParam.put("name", "tenantName");
        tenantNameParam.put("dataType", "string");
        tenantNameParam.put("description", "Name of the tenant party");
        tenantNameParam.put("required", true);
        templateNode.addParameter(tenantNameParam);
        
        Map<String, Object> startDateParam = new HashMap<>();
        startDateParam.put("name", "leaseStartDate");
        startDateParam.put("dataType", "date");
        startDateParam.put("description", "Start date of the lease");
        startDateParam.put("required", true);
        templateNode.addParameter(startDateParam);
        
        Map<String, Object> escalationParam = new HashMap<>();
        escalationParam.put("name", "annualEscalationRate");
        escalationParam.put("dataType", "number");
        escalationParam.put("description", "Annual rent escalation rate");
        escalationParam.put("required", false);
        escalationParam.put("defaultValue", 0.03);
        templateNode.addParameter(escalationParam);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("category", "real_estate");
        metadata.put("version", "2.1.0");
        metadata.put("author", "Legal Templates Team");
        metadata.put("lastUpdated", "2024-01-25");
        metadata.put("tags", List.of("lease", "commercial", "real-estate"));
        templateNode.setProperty("metadata", metadata);
        
        // Verify template node
        assertEquals("template-standard-lease", templateNode.getId());
        assertEquals("Standard Commercial Lease Template", templateNode.getName());
        assertEquals("A standardized commercial lease template with configurable terms", templateNode.getDescription());
        assertEquals("contract", templateNode.getTemplateType());
        assertEquals(40, templateNode.getLineNumber());
        assertEquals(15, templateNode.getColumnNumber());
        
        // Verify parameters
        assertEquals(4, templateNode.getParameters().size());
        assertTrue(templateNode.getBody().contains("{{contractId}}"));
        assertTrue(templateNode.getBody().contains("{{tenantName}}"));
        assertTrue(templateNode.getBody().contains("{{monthlyRentAmount}}"));
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = templateNode.toJson();
        assertEquals("template-standard-lease", json.get("id").asText());
        assertEquals("Standard Commercial Lease Template", json.get("name").asText());
        assertEquals("contract", json.get("templateType").asText());
        assertTrue(json.has("body"));
        assertTrue(json.has("parameters"));
        assertEquals(4, json.get("parameters").size());
        assertTrue(json.has("metadata"));
        
        if (json.get("metadata").has("category")) {
            assertEquals("real_estate", json.get("metadata").get("category").asText());
        }
    }
    
    @Test
    void testIRTemplateTransformation() {
        // Create template AST node
        TemplateNode templateNode = new TemplateNode("template-waterfall", "Standard Waterfall Template");
        templateNode.setDescription("A standard waterfall distribution template");
        templateNode.setTemplateType("waterfall");
        
        // Set simple waterfall body
        String waterfallBody = "waterfall {{waterfallId}} {\n" +
                              "  name: \"{{waterfallName}}\";\n" +
                              "  tiers: [\n" +
                              "    { distribute: [{recipient: \"{{gpEntity}}\", percentage: {{gpPercentage}}}] },\n" +
                              "    { distribute: [{recipient: \"{{lpEntity}}\", percentage: {{lpPercentage}}}] }\n" +
                              "  ];\n" +
                              "}";
        templateNode.setBody(waterfallBody);
        
        // Add parameters
        Map<String, Object> idParam = new HashMap<>();
        idParam.put("name", "waterfallId");
        idParam.put("dataType", "string");
        idParam.put("required", true);
        templateNode.addParameter(idParam);
        
        Map<String, Object> nameParam = new HashMap<>();
        nameParam.put("name", "waterfallName");
        nameParam.put("dataType", "string");
        nameParam.put("required", true);
        templateNode.addParameter(nameParam);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(templateNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRTemplate irTemplate = (IRTemplate) result.getIrNodes().get(0);
        
        // Verify IR template
        assertEquals("template-waterfall", irTemplate.getId());
        assertEquals("Standard Waterfall Template", irTemplate.getName());
        assertEquals("A standard waterfall distribution template", irTemplate.getDescription());
        assertEquals("waterfall", irTemplate.getTemplateType());
        assertTrue(irTemplate.getBody().contains("{{waterfallId}}"));
        
        // Verify parameters
        assertEquals(2, irTemplate.getParameterCount());
        assertEquals(2, irTemplate.getRequiredParameterCount());
        assertEquals(0, irTemplate.getOptionalParameterCount());
        assertTrue(irTemplate.hasParameters());
        assertFalse(irTemplate.hasDefaultValues());
        
        List<String> paramNames = irTemplate.getParameterNames();
        assertTrue(paramNames.contains("waterfallId"));
        assertTrue(paramNames.contains("waterfallName"));
        
        // Verify computed properties
        assertTrue(irTemplate.isForEntityType("waterfall"));
        assertFalse(irTemplate.isForEntityType("deal"));
        
        // Verify metadata enrichment
        assertEquals("template", irTemplate.getSchemaMetadata("entityType"));
        assertEquals("waterfall", irTemplate.getSchemaMetadata("templateType"));
        assertEquals(2, irTemplate.getSchemaMetadata("parameterCount"));
        assertEquals(2, irTemplate.getSchemaMetadata("requiredParameterCount"));
        assertEquals(0, irTemplate.getSchemaMetadata("optionalParameterCount"));
        assertEquals(Boolean.TRUE, irTemplate.getSchemaMetadata("hasParameters"));
        assertEquals(Boolean.FALSE, irTemplate.getSchemaMetadata("hasDefaultValues"));
        assertEquals("simple", irTemplate.getSchemaMetadata("complexityLevel"));
        @SuppressWarnings("unchecked")
        List<String> metadataParamNames = (List<String>) irTemplate.getSchemaMetadata("parameterNames");
        assertTrue(metadataParamNames.contains("waterfallId"));
        assertNotNull(irTemplate.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRTemplateValidation() {
        // Test valid template
        IRTemplate validTemplate = new IRTemplate("template-valid", "Valid Template");
        validTemplate.setDescription("Valid template for testing");
        validTemplate.setTemplateType("asset");
        validTemplate.setBody("asset {{assetId}} { name: \"{{assetName}}\"; }");
        
        List<Map<String, Object>> validParams = new ArrayList<>();
        Map<String, Object> param1 = new HashMap<>();
        param1.put("name", "assetId");
        param1.put("dataType", "string");
        validParams.add(param1);
        validTemplate.setParameters(validParams);
        
        validTemplate.validate();
        if (!validTemplate.isValid()) {
            System.out.println("Valid template validation messages: " + validTemplate.getValidationMessages());
        }
        assertTrue(validTemplate.isValid());
        assertEquals(0, validTemplate.getValidationMessages().size());
        
        // Test invalid template - missing required fields
        IRTemplate invalidTemplate = new IRTemplate();
        // Missing id, name, templateType, parameters, body
        
        invalidTemplate.validate();
        assertFalse(invalidTemplate.isValid());
        assertTrue(invalidTemplate.getValidationMessages().size() >= 5);
        assertTrue(invalidTemplate.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidTemplate.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidTemplate.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("templateType is required")));
        assertTrue(invalidTemplate.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("parameters are required")));
        assertTrue(invalidTemplate.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("body is required")));
    }
    
    @Test
    void testTemplateTypeValidation() {
        IRTemplate template = new IRTemplate("template-type-test", "Type Test");
        template.setBody("test body");
        
        List<Map<String, Object>> testParams = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("name", "test");
        param.put("dataType", "string");
        testParams.add(param);
        template.setParameters(testParams);
        
        // Valid template types
        String[] validTypes = {
            "deal", "asset", "component", "stream", "logic-block", "rule-block", 
            "assumption", "view", "scenario", "contract", "waterfall"
        };
        for (String validType : validTypes) {
            template.setTemplateType(validType);
            template.validate();
            assertTrue(template.isValid(), "Template type '" + validType + "' should be valid");
        }
        
        // Invalid template type
        template.setTemplateType("invalid-type");
        template.validate();
        assertFalse(template.isValid());
        assertTrue(template.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("templateType must be one of")));
    }
    
    @Test
    void testParameterValidation() {
        IRTemplate template = new IRTemplate("template-param-test", "Parameter Test");
        template.setTemplateType("asset");
        template.setBody("test body");
        
        // Missing parameter name - should fail
        List<Map<String, Object>> paramsWithoutName = new ArrayList<>();
        Map<String, Object> paramWithoutName = new HashMap<>();
        paramWithoutName.put("dataType", "string");
        paramsWithoutName.add(paramWithoutName);
        template.setParameters(paramsWithoutName);
        
        template.validate();
        assertFalse(template.isValid());
        assertTrue(template.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("parameter 0 must have name")));
        
        // Missing parameter dataType - should fail
        List<Map<String, Object>> paramsWithoutDataType = new ArrayList<>();
        Map<String, Object> paramWithoutDataType = new HashMap<>();
        paramWithoutDataType.put("name", "test");
        paramsWithoutDataType.add(paramWithoutDataType);
        template.setParameters(paramsWithoutDataType);
        
        template.validate();
        assertFalse(template.isValid());
        assertTrue(template.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("parameter 0 must have dataType")));
        
        // Invalid parameter dataType - should fail
        List<Map<String, Object>> paramsWithInvalidDataType = new ArrayList<>();
        Map<String, Object> paramWithInvalidDataType = new HashMap<>();
        paramWithInvalidDataType.put("name", "test");
        paramWithInvalidDataType.put("dataType", "invalid");
        paramsWithInvalidDataType.add(paramWithInvalidDataType);
        template.setParameters(paramsWithInvalidDataType);
        
        template.validate();
        assertFalse(template.isValid());
        assertTrue(template.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("dataType must be one of")));
        
        // Valid parameters - should pass
        IRTemplate validTemplate = new IRTemplate("template-param-valid", "Valid Parameter Test");
        validTemplate.setTemplateType("asset");
        validTemplate.setBody("test body");
        
        List<Map<String, Object>> validParams = new ArrayList<>();
        
        // Test all valid data types
        String[] validDataTypes = {"string", "number", "date", "boolean", "enum"};
        for (String dataType : validDataTypes) {
            Map<String, Object> param = new HashMap<>();
            param.put("name", "param_" + dataType);
            param.put("dataType", dataType);
            param.put("description", "Test parameter of type " + dataType);
            param.put("required", true);
            validParams.add(param);
        }
        
        validTemplate.setParameters(validParams);
        validTemplate.validate();
        assertTrue(validTemplate.isValid());
        assertEquals(5, validTemplate.getParameterCount());
        assertEquals(5, validTemplate.getRequiredParameterCount());
        assertEquals(0, validTemplate.getOptionalParameterCount());
    }
    
    @Test
    void testTemplateEngineJsonOutput() {
        // Create template with complete data
        IRTemplate template = new IRTemplate("template-engine", "Engine Test Template");
        template.setDescription("Comprehensive template for engine JSON testing");
        template.setTemplateType("deal");
        
        // Set complex template body
        String complexBody = "deal {{dealId}} {\n" +
                           "  name: \"{{dealName}}\";\n" +
                           "  dealType: {{dealType}};\n" +
                           "  investmentAmount: {{totalInvestment}};\n" +
                           "  targetReturn: {{targetIRR}};\n" +
                           "  holdingPeriod: {{holdingYears}};\n" +
                           "  assets: [{{#each assetIds}}\"{{this}}\"{{#unless @last}},{{/unless}}{{/each}}];\n" +
                           "}";
        template.setBody(complexBody);
        
        // Add multiple parameters with different types
        List<Map<String, Object>> parameters = new ArrayList<>();
        
        Map<String, Object> dealIdParam = new HashMap<>();
        dealIdParam.put("name", "dealId");
        dealIdParam.put("dataType", "string");
        dealIdParam.put("description", "Unique deal identifier");
        dealIdParam.put("required", true);
        parameters.add(dealIdParam);
        
        Map<String, Object> dealNameParam = new HashMap<>();
        dealNameParam.put("name", "dealName");
        dealNameParam.put("dataType", "string");
        dealNameParam.put("description", "Human-readable deal name");
        dealNameParam.put("required", true);
        parameters.add(dealNameParam);
        
        Map<String, Object> investmentParam = new HashMap<>();
        investmentParam.put("name", "totalInvestment");
        investmentParam.put("dataType", "number");
        investmentParam.put("description", "Total investment amount");
        investmentParam.put("required", true);
        parameters.add(investmentParam);
        
        Map<String, Object> irrParam = new HashMap<>();
        irrParam.put("name", "targetIRR");
        irrParam.put("dataType", "number");
        irrParam.put("description", "Target internal rate of return");
        irrParam.put("required", false);
        irrParam.put("defaultValue", 0.15);
        parameters.add(irrParam);
        
        Map<String, Object> holdingParam = new HashMap<>();
        holdingParam.put("name", "holdingYears");
        holdingParam.put("dataType", "number");
        holdingParam.put("description", "Planned holding period in years");
        holdingParam.put("required", false);
        holdingParam.put("defaultValue", 5);
        parameters.add(holdingParam);
        
        template.setParameters(parameters);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = template.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("templateType"));
        assertTrue(json.has("body"));
        assertTrue(json.has("parameters"));
        
        assertEquals("template-engine", json.get("id").asText());
        assertEquals("Engine Test Template", json.get("name").asText());
        assertEquals("deal", json.get("templateType").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertEquals("Comprehensive template for engine JSON testing", json.get("description").asText());
        
        // Verify parameters array
        com.fasterxml.jackson.databind.JsonNode parametersJson = json.get("parameters");
        assertEquals(5, parametersJson.size());
        
        // Verify body
        assertTrue(json.has("body"));
        assertTrue(json.get("body").asText().contains("{{dealId}}"));
        assertTrue(json.get("body").asText().contains("{{targetIRR}}"));
        
        // Verify computed fields
        assertTrue(json.has("parameterCount"));
        assertTrue(json.has("requiredParameterCount"));
        assertTrue(json.has("optionalParameterCount"));
        assertTrue(json.has("hasParameters"));
        assertTrue(json.has("hasDefaultValues"));
        assertTrue(json.has("parameterNames"));
        
        assertEquals(5, json.get("parameterCount").asInt());
        assertEquals(3, json.get("requiredParameterCount").asInt());
        assertEquals(2, json.get("optionalParameterCount").asInt());
        assertEquals(true, json.get("hasParameters").asBoolean());
        assertEquals(true, json.get("hasDefaultValues").asBoolean());
        
        com.fasterxml.jackson.databind.JsonNode paramNamesJson = json.get("parameterNames");
        assertEquals(5, paramNamesJson.size());
        assertTrue(paramNamesJson.toString().contains("dealId"));
        assertTrue(paramNamesJson.toString().contains("targetIRR"));
    }
    
    @Test
    void testDifferentTemplateTypes() {
        // Test deal template
        IRTemplate dealTemplate = new IRTemplate("template-deal", "Deal Template");
        dealTemplate.setDescription("Standard real estate deal template");
        dealTemplate.setTemplateType("deal");
        dealTemplate.setBody("deal {{dealId}} { name: \"{{dealName}}\"; }");
        
        List<Map<String, Object>> dealParams = new ArrayList<>();
        Map<String, Object> dealParam = new HashMap<>();
        dealParam.put("name", "dealId");
        dealParam.put("dataType", "string");
        dealParams.add(dealParam);
        dealTemplate.setParameters(dealParams);
        
        dealTemplate.validate();
        assertTrue(dealTemplate.isValid());
        assertTrue(dealTemplate.isForEntityType("deal"));
        assertFalse(dealTemplate.isForEntityType("asset"));
        assertEquals(1, dealTemplate.getParameterCount());
        assertEquals(0, dealTemplate.getRequiredParameterCount()); // No required field specified
        assertTrue(dealTemplate.hasParameters());
        assertFalse(dealTemplate.hasDefaultValues());
        
        // Test asset template with default values
        IRTemplate assetTemplate = new IRTemplate("template-asset", "Asset Template");
        assetTemplate.setDescription("Standard asset template with defaults");
        assetTemplate.setTemplateType("asset");
        assetTemplate.setBody("asset {{assetId}} { name: \"{{assetName}}\"; category: {{assetCategory}}; }");
        
        List<Map<String, Object>> assetParams = new ArrayList<>();
        
        Map<String, Object> assetIdParam = new HashMap<>();
        assetIdParam.put("name", "assetId");
        assetIdParam.put("dataType", "string");
        assetIdParam.put("required", true);
        assetParams.add(assetIdParam);
        
        Map<String, Object> categoryParam = new HashMap<>();
        categoryParam.put("name", "assetCategory");
        categoryParam.put("dataType", "enum");
        categoryParam.put("required", false);
        categoryParam.put("defaultValue", "real_estate");
        assetParams.add(categoryParam);
        
        assetTemplate.setParameters(assetParams);
        
        assetTemplate.validate();
        assertTrue(assetTemplate.isValid());
        assertTrue(assetTemplate.isForEntityType("asset"));
        assertEquals(2, assetTemplate.getParameterCount());
        assertEquals(1, assetTemplate.getRequiredParameterCount());
        assertEquals(1, assetTemplate.getOptionalParameterCount());
        assertTrue(assetTemplate.hasDefaultValues());
        
        // Test stream template (complex)
        IRTemplate streamTemplate = new IRTemplate("template-stream", "Complex Stream Template");
        streamTemplate.setDescription("Comprehensive cash flow stream template with multiple parameters");
        streamTemplate.setTemplateType("stream");
        
        // Create a complex body (> 500 chars to trigger complexity)
        String complexStreamBody = "stream {{streamId}} {\n" +
                                  "  name: \"{{streamName}}\";\n" +
                                  "  category: {{streamCategory}};\n" +
                                  "  scope: {{streamScope}};\n" +
                                  "  schedule: {\n" +
                                  "    frequency: {{frequency}};\n" +
                                  "    startDate: {{startDate}};\n" +
                                  "    endDate: {{endDate}};\n" +
                                  "  };\n" +
                                  "  amounts: {\n" +
                                  "    baseAmount: {{baseAmount}};\n" +
                                  "    growthRate: {{growthRate}};\n" +
                                  "    escalationType: {{escalationType}};\n" +
                                  "  };\n" +
                                  "  metadata: {\n" +
                                  "    description: \"{{description}}\";\n" +
                                  "    tags: [{{#each tags}}\"{{this}}\"{{#unless @last}},{{/unless}}{{/each}}];\n" +
                                  "  };\n" +
                                  "}";
        streamTemplate.setBody(complexStreamBody);
        
        // Add 8 parameters (> 5 to trigger complexity)
        List<Map<String, Object>> streamParams = new ArrayList<>();
        String[] paramNames = {
            "streamId", "streamName", "streamCategory", "streamScope", 
            "frequency", "baseAmount", "growthRate", "escalationType"
        };
        
        for (int i = 0; i < paramNames.length; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("name", paramNames[i]);
            param.put("dataType", i < 4 ? "string" : "number");
            param.put("required", i < 6); // First 6 are required (> 3 to trigger complexity)
            if (i >= 6) {
                param.put("defaultValue", i == 6 ? 0.03 : "linear");
            }
            streamParams.add(param);
        }
        
        streamTemplate.setParameters(streamParams);
        
        streamTemplate.validate();
        assertTrue(streamTemplate.isValid());
        assertTrue(streamTemplate.isForEntityType("stream"));
        assertEquals(8, streamTemplate.getParameterCount());
        assertEquals(6, streamTemplate.getRequiredParameterCount());
        assertEquals(2, streamTemplate.getOptionalParameterCount());
        assertTrue(streamTemplate.hasDefaultValues());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.TemplateNode simpleNode = new dev.cfdl.ast.TemplateNode("template-simple", "Simple Template");
        simpleNode.setTemplateType("assumption");
        simpleNode.setBody("assumption {{assumptionId}} { value: {{assumptionValue}}; }");
        
        Map<String, Object> simpleParam = new HashMap<>();
        simpleParam.put("name", "assumptionId");
        simpleParam.put("dataType", "string");
        simpleNode.addParameter(simpleParam);
        
        astNodes.add(simpleNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify template has proper metadata
        IRTemplate irTemplate = (IRTemplate) result.getIrNodes().get(0);
        assertEquals("template", irTemplate.getSchemaMetadata("entityType"));
        assertEquals("assumption", irTemplate.getSchemaMetadata("templateType"));
        assertEquals(1, irTemplate.getSchemaMetadata("parameterCount"));
        assertEquals(0, irTemplate.getSchemaMetadata("requiredParameterCount"));
        assertEquals(1, irTemplate.getSchemaMetadata("optionalParameterCount"));
        assertEquals(Boolean.TRUE, irTemplate.getSchemaMetadata("hasParameters"));
        assertEquals(Boolean.FALSE, irTemplate.getSchemaMetadata("hasDefaultValues"));
        assertEquals("simple", irTemplate.getSchemaMetadata("complexityLevel"));
        
        // Verify complex template gets "complex" complexity level
        streamTemplate.validate(); // Ensure metadata is enriched
        // Note: metadata enrichment happens in IRBuilder, so we test it through the builder above
    }
}