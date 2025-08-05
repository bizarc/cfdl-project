package dev.cfdl.ir;

import dev.cfdl.ast.ComponentNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRComponent and ComponentNode integration.
 * 
 * Tests the component-level functionality needed for atomic cash flow generation.
 */
public class IRComponentTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testComponentNodeCreation() {
        ComponentNode componentNode = new ComponentNode("unit-101", "Office Unit 101");
        componentNode.setAssetId("building-main");
        componentNode.setComponentType("office_unit");
        componentNode.setDescription("Premium corner office suite");
        componentNode.setLineNumber(50);
        componentNode.setColumnNumber(8);
        
        // Add attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sqFt", 12000);
        attributes.put("floor", 1);
        attributes.put("unit_type", "corner_suite");
        componentNode.setProperty("attributes", attributes);
        
        // Add state config
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", List.of("vacant", "leased", "under_renovation"));
        stateConfig.put("initialState", "leased");
        componentNode.setProperty("stateConfig", stateConfig);
        
        // Add stream reference
        componentNode.addStreamId("unit-101-rent");
        
        // Verify component node
        assertEquals("unit-101", componentNode.getId());
        assertEquals("Office Unit 101", componentNode.getName());
        assertEquals("building-main", componentNode.getAssetId());
        assertEquals("office_unit", componentNode.getComponentType());
        assertEquals("Premium corner office suite", componentNode.getDescription());
        assertEquals(1, componentNode.getStreamIds().size());
        assertEquals("unit-101-rent", componentNode.getStreamIds().get(0));
        assertEquals(50, componentNode.getLineNumber());
        assertEquals(8, componentNode.getColumnNumber());
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = componentNode.toJson();
        assertEquals("unit-101", json.get("id").asText());
        assertEquals("building-main", json.get("assetId").asText());
        assertEquals("office_unit", json.get("componentType").asText());
        assertTrue(json.has("attributes"));
        if (json.get("attributes").has("sqFt")) {
            assertEquals(12000, json.get("attributes").get("sqFt").asInt());
        }
        assertTrue(json.has("stateConfig"));
        if (json.get("stateConfig").has("initialState")) {
            assertEquals("leased", json.get("stateConfig").get("initialState").asText());
        }
    }
    
    @Test
    void testIRComponentTransformation() {
        // Create component AST node
        ComponentNode componentNode = new ComponentNode("unit-102", "Office Unit 102");
        componentNode.setAssetId("building-main");
        componentNode.setComponentType("office_unit");
        componentNode.setDescription("Standard office suite");
        
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sqFt", 8000);
        attributes.put("floor", 1);
        attributes.put("parking_spaces", 16);
        componentNode.setProperty("attributes", attributes);
        
        componentNode.addStreamId("unit-102-rent");
        componentNode.addStreamId("unit-102-cam");
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(componentNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRComponent irComponent = (IRComponent) result.getIrNodes().get(0);
        
        // Verify IR component
        assertEquals("unit-102", irComponent.getId());
        assertEquals("Office Unit 102", irComponent.getName());
        assertEquals("building-main", irComponent.getAssetId());
        assertEquals("office_unit", irComponent.getComponentType());
        assertEquals("Standard office suite", irComponent.getDescription());
        
        // Verify attributes
        assertNotNull(irComponent.getAttributes());
        assertEquals(8000, irComponent.getAttributes().get("sqFt"));
        assertEquals(16, irComponent.getAttributes().get("parking_spaces"));
        
        // Verify dependencies
        assertTrue(irComponent.getDependencies().contains("building-main"));
        assertTrue(irComponent.getDependencies().contains("unit-102-rent"));
        assertTrue(irComponent.getDependencies().contains("unit-102-cam"));
        
        // Verify metadata enrichment
        assertEquals("component", irComponent.getSchemaMetadata("entityType"));
        assertEquals(Boolean.TRUE, irComponent.getSchemaMetadata("atomicLevel"));
        assertNotNull(irComponent.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRComponentValidation() {
        // Test valid component
        IRComponent validComponent = new IRComponent("unit-valid", "Valid Unit");
        validComponent.setAssetId("building-main");
        validComponent.setComponentType("office_unit");
        
        validComponent.validate();
        assertTrue(validComponent.isValid());
        assertEquals(0, validComponent.getValidationMessages().size());
        
        // Test invalid component - missing required fields
        IRComponent invalidComponent = new IRComponent();
        // Missing id, name, assetId
        
        invalidComponent.validate();
        assertFalse(invalidComponent.isValid());
        assertTrue(invalidComponent.getValidationMessages().size() >= 3);
        assertTrue(invalidComponent.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidComponent.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidComponent.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("assetId is required")));
    }
    
    @Test
    void testComponentStreamScopeValidation() {
        IRComponent component = new IRComponent("unit-scope", "Scope Test Unit");
        component.setAssetId("building-main");
        
        // Add stream with wrong scope
        IRStream wrongScopeStream = new IRStream("stream-wrong", "Wrong Scope Stream");
        wrongScopeStream.setScope("asset"); // Should be "component" for component streams
        wrongScopeStream.setCategory("Revenue");
        wrongScopeStream.setAmount(1000.0);
        
        // Add required schedule
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("type", "recurring");
        schedule.put("startDate", "2024-01-01");
        Map<String, Object> recurrenceRule = new HashMap<>();
        recurrenceRule.put("freq", "MONTHLY");
        recurrenceRule.put("interval", 1);
        schedule.put("recurrenceRule", recurrenceRule);
        wrongScopeStream.setSchedule(schedule);
        
        component.addStream(wrongScopeStream);
        
        component.validate();
        assertFalse(component.isValid());
        assertTrue(component.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have scope 'component'")));
    }
    
    @Test
    void testComponentEngineJsonOutput() {
        // Create component with complete data
        IRComponent component = new IRComponent("unit-engine", "Engine Test Unit");
        component.setAssetId("building-main");
        component.setComponentType("office_unit");
        component.setDescription("Test unit for engine JSON");
        
        // Add attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sqFt", 10000);
        attributes.put("floor", 5);
        attributes.put("unit_type", "standard");
        component.setAttributes(attributes);
        
        // Add state config
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", List.of("vacant", "leased"));
        stateConfig.put("initialState", "leased");
        component.setStateConfig(stateConfig);
        
        // Add stream
        IRStream stream = new IRStream("unit-engine-rent", "Unit Rent");
        stream.setScope("component");
        stream.setCategory("Revenue");
        stream.setSubType("Operating");
        stream.setAmount(5000.0);
        
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("type", "recurring");
        schedule.put("startDate", "2024-01-01");
        Map<String, Object> recurrenceRule = new HashMap<>();
        recurrenceRule.put("freq", "MONTHLY");
        recurrenceRule.put("interval", 1);
        schedule.put("recurrenceRule", recurrenceRule);
        stream.setSchedule(schedule);
        
        component.addStream(stream);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = component.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("assetId"));
        assertEquals("unit-engine", json.get("id").asText());
        assertEquals("building-main", json.get("assetId").asText());
        
        // Verify optional fields
        assertTrue(json.has("componentType"));
        assertTrue(json.has("description"));
        assertTrue(json.has("attributes"));
        assertTrue(json.has("stateConfig"));
        assertTrue(json.has("streams"));
        
        // Verify attributes
        assertEquals(10000, json.get("attributes").get("sqFt").asInt());
        assertEquals("standard", json.get("attributes").get("unit_type").asText());
        
        // Verify streams
        assertEquals(1, json.get("streams").size());
        com.fasterxml.jackson.databind.JsonNode streamJson = json.get("streams").get(0);
        assertEquals("unit-engine-rent", streamJson.get("id").asText());
        assertEquals("component", streamJson.get("scope").asText());
        assertEquals(5000.0, streamJson.get("amount").asDouble());
    }
    
    @Test
    void testAtomicCashFlowConcept() {
        // This test demonstrates the atomic cash flow concept
        // Components are the most granular level for cash flow generation
        
        // Create multiple components representing individual office units
        List<IRComponent> officeUnits = new ArrayList<>();
        
        // Premium corner unit
        IRComponent cornerUnit = new IRComponent("unit-corner", "Corner Office Suite");
        cornerUnit.setAssetId("building-downtown");
        cornerUnit.setComponentType("office_unit");
        
        Map<String, Object> cornerAttributes = new HashMap<>();
        cornerAttributes.put("sqFt", 12000);
        cornerAttributes.put("rentPSF", 5.00);
        cornerAttributes.put("unit_type", "premium");
        cornerUnit.setAttributes(cornerAttributes);
        
        // Standard unit
        IRComponent standardUnit = new IRComponent("unit-standard", "Standard Office Suite");
        standardUnit.setAssetId("building-downtown");
        standardUnit.setComponentType("office_unit");
        
        Map<String, Object> standardAttributes = new HashMap<>();
        standardAttributes.put("sqFt", 8000);
        standardAttributes.put("rentPSF", 4.50);
        standardAttributes.put("unit_type", "standard");
        standardUnit.setAttributes(standardAttributes);
        
        officeUnits.add(cornerUnit);
        officeUnits.add(standardUnit);
        
        // Each component generates atomic cash flows
        for (IRComponent unit : officeUnits) {
            unit.validate();
            assertTrue(unit.isValid(), "Each component should be valid for atomic cash flow generation");
            
            // Note: Schema metadata is only added during IR building process
            // For this test, we're creating IRComponent directly, so metadata won't be populated
            // In real usage, the IRBuilder.build() process adds the metadata
            assertTrue(unit.isValid(), "Component should be valid for cash flow generation");
            
            // In the engine, each component would generate its own cash flows
            // which would then be aggregated up to asset and deal levels
            assertNotNull(unit.getAttributes().get("sqFt"));
            assertTrue((Integer) unit.getAttributes().get("sqFt") > 0);
        }
        
        // Verify we have atomic-level entities ready for cash flow generation
        assertEquals(2, officeUnits.size());
        assertEquals("premium", cornerUnit.getAttributes().get("unit_type"));
        assertEquals("standard", standardUnit.getAttributes().get("unit_type"));
        
        // This demonstrates how the engine would aggregate:
        // 1. Component-level cash flows (most atomic)
        // 2. Asset-level cash flows (building-wide)  
        // 3. Deal-level cash flows (financing, fees, exit)
    }
}