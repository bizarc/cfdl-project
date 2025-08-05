package dev.cfdl.ir;

import dev.cfdl.ast.EventTriggerNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IREventTrigger and EventTriggerNode integration.
 * 
 * Tests the event trigger functionality needed for event-based triggers
 * supporting assumption changes, stream thresholds, external events, and custom expressions.
 */
public class IREventTriggerTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testEventTriggerNodeCreation() {
        EventTriggerNode eventTriggerNode = new EventTriggerNode("event-trigger-occupancy", "Occupancy Drop Trigger");
        eventTriggerNode.setType("stream_threshold");
        eventTriggerNode.setStreamId("stream-occupancy-rate");
        eventTriggerNode.setOperator("lt");
        eventTriggerNode.setThreshold(0.80);
        eventTriggerNode.setLineNumber(60);
        eventTriggerNode.setColumnNumber(25);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("category", "property_management");
        metadata.put("severity", "high");
        metadata.put("alert_level", 2);
        metadata.put("description_text", "Triggers when property occupancy falls below 80%");
        eventTriggerNode.setProperty("metadata", metadata);
        
        // Verify event trigger node
        assertEquals("event-trigger-occupancy", eventTriggerNode.getId());
        assertEquals("Occupancy Drop Trigger", eventTriggerNode.getName());
        assertEquals("stream_threshold", eventTriggerNode.getType());
        assertEquals("stream-occupancy-rate", eventTriggerNode.getStreamId());
        assertEquals("lt", eventTriggerNode.getOperator());
        assertEquals(0.80, eventTriggerNode.getThreshold());
        assertEquals(60, eventTriggerNode.getLineNumber());
        assertEquals(25, eventTriggerNode.getColumnNumber());
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = eventTriggerNode.toJson();
        assertEquals("event-trigger-occupancy", json.get("id").asText());
        assertEquals("Occupancy Drop Trigger", json.get("name").asText());
        assertEquals("stream_threshold", json.get("type").asText());
        assertEquals("stream-occupancy-rate", json.get("streamId").asText());
        assertEquals("lt", json.get("operator").asText());
        assertEquals(0.80, json.get("threshold").asDouble());
        assertTrue(json.has("metadata"));
        
        if (json.get("metadata").has("category")) {
            assertEquals("property_management", json.get("metadata").get("category").asText());
        }
    }
    
    @Test
    void testIREventTriggerTransformation() {
        // Create event trigger AST node
        EventTriggerNode eventTriggerNode = new EventTriggerNode("event-trigger-assumption", "Interest Rate Change");
        eventTriggerNode.setType("assumption_change");
        eventTriggerNode.setAssumptionId("assumption-interest-rate");
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(eventTriggerNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IREventTrigger irEventTrigger = (IREventTrigger) result.getIrNodes().get(0);
        
        // Verify IR event trigger
        assertEquals("event-trigger-assumption", irEventTrigger.getId());
        assertEquals("Interest Rate Change", irEventTrigger.getName());
        assertEquals("assumption_change", irEventTrigger.getType());
        assertEquals("assumption-interest-rate", irEventTrigger.getAssumptionId());
        
        // Verify computed properties
        assertTrue(irEventTrigger.isAssumptionChangeTrigger());
        assertFalse(irEventTrigger.isStreamThresholdTrigger());
        assertFalse(irEventTrigger.isExternalEventTrigger());
        assertFalse(irEventTrigger.isCustomTrigger());
        assertEquals("Assumption 'assumption-interest-rate' changes", irEventTrigger.getTriggerDescription());
        
        // Verify dependencies
        assertTrue(irEventTrigger.getDependencies().contains("assumption-interest-rate"));
        
        // Verify metadata enrichment
        assertEquals("eventTrigger", irEventTrigger.getSchemaMetadata("entityType"));
        assertEquals("assumption_change", irEventTrigger.getSchemaMetadata("triggerType"));
        assertEquals(Boolean.TRUE, irEventTrigger.getSchemaMetadata("isAssumptionChangeTrigger"));
        assertEquals(Boolean.FALSE, irEventTrigger.getSchemaMetadata("isStreamThresholdTrigger"));
        assertEquals(Boolean.FALSE, irEventTrigger.getSchemaMetadata("isExternalEventTrigger"));
        assertEquals(Boolean.FALSE, irEventTrigger.getSchemaMetadata("isCustomTrigger"));
        assertEquals("Assumption 'assumption-interest-rate' changes", irEventTrigger.getSchemaMetadata("triggerDescription"));
        assertEquals("assumption-interest-rate", irEventTrigger.getSchemaMetadata("assumptionId"));
        assertNotNull(irEventTrigger.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIREventTriggerValidation() {
        // Test valid event trigger
        IREventTrigger validEventTrigger = new IREventTrigger("event-trigger-valid", "Valid Event Trigger");
        validEventTrigger.setType("external_event");
        validEventTrigger.setExternalEventName("market_crash_detected");
        
        validEventTrigger.validate();
        assertTrue(validEventTrigger.isValid());
        assertEquals(0, validEventTrigger.getValidationMessages().size());
        
        // Test invalid event trigger - missing required fields
        IREventTrigger invalidEventTrigger = new IREventTrigger();
        // Missing type and type-specific fields
        
        invalidEventTrigger.validate();
        assertFalse(invalidEventTrigger.isValid());
        assertTrue(invalidEventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("type is required")));
    }
    
    @Test
    void testEventTriggerTypeValidation() {
        IREventTrigger eventTrigger = new IREventTrigger("event-trigger-type-test", "Type Test");
        
        // Valid types
        String[] validTypes = {"assumption_change", "stream_threshold", "external_event", "custom"};
        for (String validType : validTypes) {
            eventTrigger.setType(validType);
            
            // Set required fields for each type
            if ("assumption_change".equals(validType)) {
                eventTrigger.setAssumptionId("assumption-test");
            } else if ("stream_threshold".equals(validType)) {
                eventTrigger.setStreamId("stream-test");
                eventTrigger.setOperator("gt");
                eventTrigger.setThreshold(100.0);
            } else if ("external_event".equals(validType)) {
                eventTrigger.setExternalEventName("test-event");
            } else if ("custom".equals(validType)) {
                eventTrigger.setExpression("condition == true");
            }
            
            eventTrigger.validate();
            assertTrue(eventTrigger.isValid(), "Type '" + validType + "' should be valid");
            
            // Reset for next iteration
            eventTrigger = new IREventTrigger("event-trigger-type-test", "Type Test");
        }
        
        // Invalid type
        eventTrigger.setType("invalid-type");
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("type must be one of")));
    }
    
    @Test
    void testAssumptionChangeValidation() {
        IREventTrigger eventTrigger = new IREventTrigger("event-trigger-assumption", "Assumption Change");
        eventTrigger.setType("assumption_change");
        
        // Missing assumptionId - should fail
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have assumptionId")));
        
        // Valid assumptionId - should pass (fresh object to avoid accumulated validation messages)
        IREventTrigger validAssumptionTrigger = new IREventTrigger("event-trigger-assumption-valid", "Valid Assumption Change");
        validAssumptionTrigger.setType("assumption_change");
        validAssumptionTrigger.setAssumptionId("assumption-interest-rate");
        validAssumptionTrigger.validate();
        assertTrue(validAssumptionTrigger.isValid());
        assertTrue(validAssumptionTrigger.isAssumptionChangeTrigger());
        assertEquals("Assumption 'assumption-interest-rate' changes", validAssumptionTrigger.getTriggerDescription());
    }
    
    @Test
    void testStreamThresholdValidation() {
        IREventTrigger eventTrigger = new IREventTrigger("event-trigger-stream", "Stream Threshold");
        eventTrigger.setType("stream_threshold");
        
        // Missing streamId - should fail
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have streamId")));
        
        // Missing operator - should fail
        eventTrigger.setStreamId("stream-revenue");
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have operator")));
        
        // Invalid operator - should fail
        eventTrigger.setOperator("invalid-op");
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("operator must be one of")));
        
        // Missing threshold - should fail
        eventTrigger.setOperator("gte");
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have threshold")));
        
        // Valid stream threshold - should pass (fresh object to avoid accumulated validation messages)
        IREventTrigger validStreamTrigger = new IREventTrigger("event-trigger-stream-valid", "Valid Stream Threshold");
        validStreamTrigger.setType("stream_threshold");
        validStreamTrigger.setStreamId("stream-revenue");
        validStreamTrigger.setOperator("gte");
        validStreamTrigger.setThreshold(1000000.0);
        validStreamTrigger.validate();
        assertTrue(validStreamTrigger.isValid());
        assertTrue(validStreamTrigger.isStreamThresholdTrigger());
        assertEquals("Stream 'stream-revenue' gte 1000000.0", validStreamTrigger.getTriggerDescription());
        
        // Test all valid operators
        String[] validOperators = {"eq", "ne", "lt", "lte", "gt", "gte"};
        for (String operator : validOperators) {
            IREventTrigger operatorTrigger = new IREventTrigger("event-trigger-operator-" + operator, "Operator Test");
            operatorTrigger.setType("stream_threshold");
            operatorTrigger.setStreamId("stream-test");
            operatorTrigger.setOperator(operator);
            operatorTrigger.setThreshold(100.0);
            operatorTrigger.validate();
            assertTrue(operatorTrigger.isValid(), "Operator '" + operator + "' should be valid");
        }
    }
    
    @Test
    void testExternalEventValidation() {
        IREventTrigger eventTrigger = new IREventTrigger("event-trigger-external", "External Event");
        eventTrigger.setType("external_event");
        
        // Missing externalEventName - should fail
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have externalEventName")));
        
        // Valid externalEventName - should pass (fresh object to avoid accumulated validation messages)
        IREventTrigger validExternalTrigger = new IREventTrigger("event-trigger-external-valid", "Valid External Event");
        validExternalTrigger.setType("external_event");
        validExternalTrigger.setExternalEventName("tenant_lease_expiring");
        validExternalTrigger.validate();
        assertTrue(validExternalTrigger.isValid());
        assertTrue(validExternalTrigger.isExternalEventTrigger());
        assertEquals("External event 'tenant_lease_expiring' occurs", validExternalTrigger.getTriggerDescription());
    }
    
    @Test
    void testCustomExpressionValidation() {
        IREventTrigger eventTrigger = new IREventTrigger("event-trigger-custom", "Custom Expression");
        eventTrigger.setType("custom");
        
        // Missing expression - should fail
        eventTrigger.validate();
        assertFalse(eventTrigger.isValid());
        assertTrue(eventTrigger.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have expression")));
        
        // Valid expression - should pass (fresh object to avoid accumulated validation messages)
        IREventTrigger validCustomTrigger = new IREventTrigger("event-trigger-custom-valid", "Valid Custom Expression");
        validCustomTrigger.setType("custom");
        validCustomTrigger.setExpression("(noi_growth < 0.02) && (vacancy_rate > 0.15)");
        validCustomTrigger.validate();
        assertTrue(validCustomTrigger.isValid());
        assertTrue(validCustomTrigger.isCustomTrigger());
        assertEquals("Custom condition: (noi_growth < 0.02) && (vacancy_rate > 0.15)", validCustomTrigger.getTriggerDescription());
    }
    
    @Test
    void testEventTriggerEngineJsonOutput() {
        // Create event trigger with complete data
        IREventTrigger eventTrigger = new IREventTrigger("event-trigger-engine", "Engine Test Event Trigger");
        eventTrigger.setType("stream_threshold");
        eventTrigger.setStreamId("stream-debt-service-coverage");
        eventTrigger.setOperator("lt");
        eventTrigger.setThreshold(1.25);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = eventTrigger.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("type"));
        assertEquals("stream_threshold", json.get("type").asText());
        
        // Verify optional fields
        assertTrue(json.has("streamId"));
        assertTrue(json.has("operator"));
        assertTrue(json.has("threshold"));
        assertEquals("stream-debt-service-coverage", json.get("streamId").asText());
        assertEquals("lt", json.get("operator").asText());
        assertEquals(1.25, json.get("threshold").asDouble());
        
        // Verify computed fields
        assertTrue(json.has("isAssumptionChangeTrigger"));
        assertTrue(json.has("isStreamThresholdTrigger"));
        assertTrue(json.has("isExternalEventTrigger"));
        assertTrue(json.has("isCustomTrigger"));
        assertTrue(json.has("triggerDescription"));
        
        assertEquals(false, json.get("isAssumptionChangeTrigger").asBoolean());
        assertEquals(true, json.get("isStreamThresholdTrigger").asBoolean());
        assertEquals(false, json.get("isExternalEventTrigger").asBoolean());
        assertEquals(false, json.get("isCustomTrigger").asBoolean());
        assertEquals("Stream 'stream-debt-service-coverage' lt 1.25", json.get("triggerDescription").asText());
    }
    
    @Test
    void testDifferentEventTriggerTypes() {
        // Test assumption change trigger
        IREventTrigger assumptionTrigger = new IREventTrigger("event-trigger-assumption", "Assumption Change");
        assumptionTrigger.setType("assumption_change");
        assumptionTrigger.setAssumptionId("assumption-market-volatility");
        
        assumptionTrigger.validate();
        assertTrue(assumptionTrigger.isValid());
        assertTrue(assumptionTrigger.isAssumptionChangeTrigger());
        assertFalse(assumptionTrigger.isStreamThresholdTrigger());
        assertFalse(assumptionTrigger.isExternalEventTrigger());
        assertFalse(assumptionTrigger.isCustomTrigger());
        assertEquals("Assumption 'assumption-market-volatility' changes", assumptionTrigger.getTriggerDescription());
        
        // Test stream threshold trigger
        IREventTrigger streamTrigger = new IREventTrigger("event-trigger-stream", "Stream Threshold");
        streamTrigger.setType("stream_threshold");
        streamTrigger.setStreamId("stream-cash-flow");
        streamTrigger.setOperator("lte");
        streamTrigger.setThreshold(50000.0);
        
        streamTrigger.validate();
        assertTrue(streamTrigger.isValid());
        assertFalse(streamTrigger.isAssumptionChangeTrigger());
        assertTrue(streamTrigger.isStreamThresholdTrigger());
        assertFalse(streamTrigger.isExternalEventTrigger());
        assertFalse(streamTrigger.isCustomTrigger());
        assertEquals("Stream 'stream-cash-flow' lte 50000.0", streamTrigger.getTriggerDescription());
        
        // Test external event trigger
        IREventTrigger externalTrigger = new IREventTrigger("event-trigger-external", "External Event");
        externalTrigger.setType("external_event");
        externalTrigger.setExternalEventName("regulatory_change_notification");
        
        externalTrigger.validate();
        assertTrue(externalTrigger.isValid());
        assertFalse(externalTrigger.isAssumptionChangeTrigger());
        assertFalse(externalTrigger.isStreamThresholdTrigger());
        assertTrue(externalTrigger.isExternalEventTrigger());
        assertFalse(externalTrigger.isCustomTrigger());
        assertEquals("External event 'regulatory_change_notification' occurs", externalTrigger.getTriggerDescription());
        
        // Test custom expression trigger
        IREventTrigger customTrigger = new IREventTrigger("event-trigger-custom", "Custom Expression");
        customTrigger.setType("custom");
        customTrigger.setExpression("portfolio_beta > 1.5 && market_downturn_detected");
        
        customTrigger.validate();
        assertTrue(customTrigger.isValid());
        assertFalse(customTrigger.isAssumptionChangeTrigger());
        assertFalse(customTrigger.isStreamThresholdTrigger());
        assertFalse(customTrigger.isExternalEventTrigger());
        assertTrue(customTrigger.isCustomTrigger());
        assertEquals("Custom condition: portfolio_beta > 1.5 && market_downturn_detected", customTrigger.getTriggerDescription());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = List.of();
        
        dev.cfdl.ast.EventTriggerNode simpleNode = new dev.cfdl.ast.EventTriggerNode("event-trigger-simple", "Simple Trigger");
        simpleNode.setType("external_event");
        simpleNode.setExternalEventName("simple_event");
        astNodes = List.of(simpleNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify event trigger has proper metadata
        IREventTrigger irEventTrigger = (IREventTrigger) result.getIrNodes().get(0);
        assertEquals("eventTrigger", irEventTrigger.getSchemaMetadata("entityType"));
        assertEquals("external_event", irEventTrigger.getSchemaMetadata("triggerType"));
        assertEquals(Boolean.FALSE, irEventTrigger.getSchemaMetadata("isAssumptionChangeTrigger"));
        assertEquals(Boolean.FALSE, irEventTrigger.getSchemaMetadata("isStreamThresholdTrigger"));
        assertEquals(Boolean.TRUE, irEventTrigger.getSchemaMetadata("isExternalEventTrigger"));
        assertEquals(Boolean.FALSE, irEventTrigger.getSchemaMetadata("isCustomTrigger"));
        assertEquals("External event 'simple_event' occurs", irEventTrigger.getSchemaMetadata("triggerDescription"));
        assertEquals("simple_event", irEventTrigger.getSchemaMetadata("externalEventName"));
    }
}