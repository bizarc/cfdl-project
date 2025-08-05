package dev.cfdl.ir;

import dev.cfdl.ast.RuleBlockNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * Unit tests for IRRuleBlock and RuleBlockNode integration.
 * 
 * Tests the rule block functionality needed for conditional and event-driven
 * operations that fire during model execution with various trigger types.
 */
public class IRRuleBlockTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testRuleBlockNodeCreation() {
        RuleBlockNode ruleBlockNode = new RuleBlockNode("rule-block-rebalance", "Portfolio Rebalancing Rule");
        ruleBlockNode.setDescription("Automatically rebalances portfolio when asset allocation drifts beyond thresholds");
        ruleBlockNode.setScope("portfolio");
        ruleBlockNode.setScheduleId("schedule-quarterly-review");
        ruleBlockNode.setCondition("allocation_drift > 0.05");
        ruleBlockNode.setLineNumber(75);
        ruleBlockNode.setColumnNumber(10);
        
        // Set multiple actions
        List<String> actions = Arrays.asList(
            "log('Portfolio rebalancing triggered')",
            "new_allocation = calculate_target_allocation()",
            "rebalance_portfolio(new_allocation)",
            "notify_manager('Rebalancing completed')"
        );
        ruleBlockNode.setAction(actions);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("category", "portfolio_management");
        metadata.put("priority", "high");
        metadata.put("version", "2.1.0");
        metadata.put("lastUpdated", "2024-01-20");
        metadata.put("author", "Portfolio Management Team");
        ruleBlockNode.setProperty("metadata", metadata);
        
        // Verify rule block node
        assertEquals("rule-block-rebalance", ruleBlockNode.getId());
        assertEquals("Portfolio Rebalancing Rule", ruleBlockNode.getName());
        assertEquals("Automatically rebalances portfolio when asset allocation drifts beyond thresholds", ruleBlockNode.getDescription());
        assertEquals("portfolio", ruleBlockNode.getScope());
        assertEquals("schedule-quarterly-review", ruleBlockNode.getScheduleId());
        assertEquals("allocation_drift > 0.05", ruleBlockNode.getCondition());
        assertEquals(75, ruleBlockNode.getLineNumber());
        assertEquals(10, ruleBlockNode.getColumnNumber());
        
        // Verify actions
        assertTrue(ruleBlockNode.getAction() instanceof List);
        @SuppressWarnings("unchecked")
        List<String> nodeActions = (List<String>) ruleBlockNode.getAction();
        assertEquals(4, nodeActions.size());
        assertTrue(nodeActions.contains("log('Portfolio rebalancing triggered')"));
        assertTrue(nodeActions.contains("rebalance_portfolio(new_allocation)"));
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = ruleBlockNode.toJson();
        assertEquals("rule-block-rebalance", json.get("id").asText());
        assertEquals("Portfolio Rebalancing Rule", json.get("name").asText());
        assertEquals("portfolio", json.get("scope").asText());
        assertEquals("schedule-quarterly-review", json.get("schedule").asText());
        assertEquals("allocation_drift > 0.05", json.get("condition").asText());
        assertTrue(json.has("action"));
        assertTrue(json.get("action").isArray());
        assertEquals(4, json.get("action").size());
        assertTrue(json.has("metadata"));
        
        if (json.get("metadata").has("category")) {
            assertEquals("portfolio_management", json.get("metadata").get("category").asText());
        }
    }
    
    @Test
    void testIRRuleBlockTransformation() {
        // Create rule block AST node
        RuleBlockNode ruleBlockNode = new RuleBlockNode("rule-block-alert", "Cash Flow Alert Rule");
        ruleBlockNode.setDescription("Sends alert when cash flow drops below minimum threshold");
        ruleBlockNode.setScope("deal");
        ruleBlockNode.setEventTriggerId("event-cash-flow-updated");
        ruleBlockNode.setCondition("current_cash_flow < minimum_threshold");
        ruleBlockNode.setAction("send_alert('Cash flow below threshold: ' + current_cash_flow)");
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(ruleBlockNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRRuleBlock irRuleBlock = (IRRuleBlock) result.getIrNodes().get(0);
        
        // Verify IR rule block
        assertEquals("rule-block-alert", irRuleBlock.getId());
        assertEquals("Cash Flow Alert Rule", irRuleBlock.getName());
        assertEquals("Sends alert when cash flow drops below minimum threshold", irRuleBlock.getDescription());
        assertEquals("deal", irRuleBlock.getScope());
        assertEquals("event-cash-flow-updated", irRuleBlock.getEventTriggerId());
        assertEquals("current_cash_flow < minimum_threshold", irRuleBlock.getCondition());
        assertEquals("send_alert('Cash flow below threshold: ' + current_cash_flow)", irRuleBlock.getAction());
        
        // Verify computed properties
        assertFalse(irRuleBlock.hasScheduleTrigger());
        assertTrue(irRuleBlock.hasEventTrigger());
        assertTrue(irRuleBlock.hasCondition());
        assertFalse(irRuleBlock.hasMultipleActions());
        assertEquals(1, irRuleBlock.getActionCount());
        assertEquals("event", irRuleBlock.getTriggerType());
        
        // Verify dependencies
        assertTrue(irRuleBlock.getDependencies().contains("event-cash-flow-updated"));
        
        // Verify metadata enrichment
        assertEquals("ruleBlock", irRuleBlock.getSchemaMetadata("entityType"));
        assertEquals("deal", irRuleBlock.getSchemaMetadata("blockScope"));
        assertEquals("event", irRuleBlock.getSchemaMetadata("triggerType"));
        assertEquals(Boolean.FALSE, irRuleBlock.getSchemaMetadata("hasScheduleTrigger"));
        assertEquals(Boolean.TRUE, irRuleBlock.getSchemaMetadata("hasEventTrigger"));
        assertEquals(Boolean.TRUE, irRuleBlock.getSchemaMetadata("hasCondition"));
        assertEquals(Boolean.FALSE, irRuleBlock.getSchemaMetadata("hasMultipleActions"));
        assertEquals(1, irRuleBlock.getSchemaMetadata("actionCount"));
        assertEquals("event-cash-flow-updated", irRuleBlock.getSchemaMetadata("eventTriggerId"));
        assertNotNull(irRuleBlock.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRRuleBlockValidation() {
        // Test valid rule block
        IRRuleBlock validRuleBlock = new IRRuleBlock("rule-block-valid", "Valid Rule Block");
        validRuleBlock.setDescription("Valid rule block for testing");
        validRuleBlock.setScope("asset");
        validRuleBlock.setCondition("value > threshold");
        validRuleBlock.setAction("update_status('active')");
        
        validRuleBlock.validate();
        assertTrue(validRuleBlock.isValid());
        assertEquals(0, validRuleBlock.getValidationMessages().size());
        
        // Test invalid rule block - missing required fields
        IRRuleBlock invalidRuleBlock = new IRRuleBlock();
        // Missing id, name, scope, action, and any trigger
        
        invalidRuleBlock.validate();
        assertFalse(invalidRuleBlock.isValid());
        assertTrue(invalidRuleBlock.getValidationMessages().size() >= 5);
        assertTrue(invalidRuleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidRuleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidRuleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("scope is required")));
        assertTrue(invalidRuleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("action is required")));
        assertTrue(invalidRuleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have at least one trigger")));
    }
    
    @Test
    void testRuleBlockScopeValidation() {
        IRRuleBlock ruleBlock = new IRRuleBlock("rule-block-scope-test", "Scope Test");
        ruleBlock.setCondition("true");
        ruleBlock.setAction("log('test')");
        
        // Valid scopes
        String[] validScopes = {"component", "asset", "deal", "portfolio", "fund"};
        for (String validScope : validScopes) {
            ruleBlock.setScope(validScope);
            ruleBlock.validate();
            assertTrue(ruleBlock.isValid(), "Scope '" + validScope + "' should be valid");
        }
        
        // Invalid scope
        ruleBlock.setScope("invalid-scope");
        ruleBlock.validate();
        assertFalse(ruleBlock.isValid());
        assertTrue(ruleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("scope must be one of")));
    }
    
    @Test
    void testTriggerValidation() {
        IRRuleBlock ruleBlock = new IRRuleBlock("rule-block-trigger-test", "Trigger Test");
        ruleBlock.setScope("asset");
        ruleBlock.setAction("log('triggered')");
        
        // No triggers - should fail
        ruleBlock.validate();
        assertFalse(ruleBlock.isValid());
        assertTrue(ruleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("must have at least one trigger")));
        
        // Schedule trigger only - should pass (fresh object to avoid accumulated validation messages)
        IRRuleBlock scheduleRuleBlock = new IRRuleBlock("rule-block-schedule", "Schedule Rule");
        scheduleRuleBlock.setScope("asset");
        scheduleRuleBlock.setAction("log('scheduled trigger')");
        scheduleRuleBlock.setScheduleId("schedule-daily");
        scheduleRuleBlock.validate();
        if (!scheduleRuleBlock.isValid()) {
            System.out.println("Schedule trigger validation messages: " + scheduleRuleBlock.getValidationMessages());
        }
        assertTrue(scheduleRuleBlock.isValid());
        assertTrue(scheduleRuleBlock.hasScheduleTrigger());
        assertFalse(scheduleRuleBlock.hasEventTrigger());
        assertFalse(scheduleRuleBlock.hasCondition());
        assertEquals("schedule", scheduleRuleBlock.getTriggerType());
        
        // Event trigger only - should pass
        IRRuleBlock eventRuleBlock = new IRRuleBlock("rule-block-event", "Event Rule");
        eventRuleBlock.setScope("asset");
        eventRuleBlock.setAction("log('event triggered')");
        eventRuleBlock.setEventTriggerId("event-market-update");
        eventRuleBlock.validate();
        assertTrue(eventRuleBlock.isValid());
        assertFalse(eventRuleBlock.hasScheduleTrigger());
        assertTrue(eventRuleBlock.hasEventTrigger());
        assertFalse(eventRuleBlock.hasCondition());
        assertEquals("event", eventRuleBlock.getTriggerType());
        
        // Condition trigger only - should pass
        IRRuleBlock conditionRuleBlock = new IRRuleBlock("rule-block-condition", "Condition Rule");
        conditionRuleBlock.setScope("asset");
        conditionRuleBlock.setAction("log('condition met')");
        conditionRuleBlock.setCondition("balance > 1000");
        conditionRuleBlock.validate();
        assertTrue(conditionRuleBlock.isValid());
        assertFalse(conditionRuleBlock.hasScheduleTrigger());
        assertFalse(conditionRuleBlock.hasEventTrigger());
        assertTrue(conditionRuleBlock.hasCondition());
        assertEquals("condition", conditionRuleBlock.getTriggerType());
        
        // Multiple triggers - should pass
        IRRuleBlock multiTriggerRuleBlock = new IRRuleBlock("rule-block-multi", "Multi Trigger Rule");
        multiTriggerRuleBlock.setScope("asset");
        multiTriggerRuleBlock.setAction("log('multi trigger')");
        multiTriggerRuleBlock.setScheduleId("schedule-monthly");
        multiTriggerRuleBlock.setCondition("ready_for_rebalance == true");
        multiTriggerRuleBlock.validate();
        assertTrue(multiTriggerRuleBlock.isValid());
        assertTrue(multiTriggerRuleBlock.hasScheduleTrigger());
        assertFalse(multiTriggerRuleBlock.hasEventTrigger());
        assertTrue(multiTriggerRuleBlock.hasCondition());
        assertEquals("schedule", multiTriggerRuleBlock.getTriggerType()); // First trigger type found
    }
    
    @Test
    void testActionValidation() {
        IRRuleBlock ruleBlock = new IRRuleBlock("rule-block-action-test", "Action Test");
        ruleBlock.setScope("asset");
        ruleBlock.setCondition("true");
        
        // Empty string action - should fail
        ruleBlock.setAction("");
        ruleBlock.validate();
        assertFalse(ruleBlock.isValid());
        assertTrue(ruleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("action cannot be empty")));
        
        // Valid string action - should pass (fresh object to avoid accumulated validation messages)
        IRRuleBlock validStringRuleBlock = new IRRuleBlock("rule-block-valid-string", "Valid String Action Rule");
        validStringRuleBlock.setScope("asset");
        validStringRuleBlock.setCondition("true");
        validStringRuleBlock.setAction("log('valid action')");
        validStringRuleBlock.validate();
        assertTrue(validStringRuleBlock.isValid());
        assertFalse(validStringRuleBlock.hasMultipleActions());
        assertEquals(1, validStringRuleBlock.getActionCount());
        
        // Empty action list - should fail
        ruleBlock.setAction(new ArrayList<String>());
        ruleBlock.validate();
        assertFalse(ruleBlock.isValid());
        assertTrue(ruleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("action list cannot be empty")));
        
        // Action list with null item - should fail
        List<String> actionsWithNull = new ArrayList<>();
        actionsWithNull.add("valid_action()");
        actionsWithNull.add(null);
        ruleBlock.setAction(actionsWithNull);
        ruleBlock.validate();
        assertFalse(ruleBlock.isValid());
        assertTrue(ruleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("action 1 cannot be null or empty")));
        
        // Action list with empty item - should fail
        List<String> actionsWithEmpty = new ArrayList<>();
        actionsWithEmpty.add("valid_action()");
        actionsWithEmpty.add("");
        ruleBlock.setAction(actionsWithEmpty);
        ruleBlock.validate();
        assertFalse(ruleBlock.isValid());
        assertTrue(ruleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("action 1 cannot be null or empty")));
        
        // Valid action list - should pass (fresh object to avoid accumulated validation messages)
        IRRuleBlock validActionRuleBlock = new IRRuleBlock("rule-block-valid-actions", "Valid Actions Rule");
        validActionRuleBlock.setScope("asset");
        validActionRuleBlock.setCondition("true");
        List<String> validActions = Arrays.asList(
            "step1()",
            "step2()",
            "step3()"
        );
        validActionRuleBlock.setAction(validActions);
        validActionRuleBlock.validate();
        if (!validActionRuleBlock.isValid()) {
            System.out.println("Valid action list validation messages: " + validActionRuleBlock.getValidationMessages());
        }
        assertTrue(validActionRuleBlock.isValid());
        assertTrue(validActionRuleBlock.hasMultipleActions());
        assertEquals(3, validActionRuleBlock.getActionCount());
        
        // Invalid action type (not string or list) - should fail
        ruleBlock.setAction(42); // Integer instead of string/list
        ruleBlock.validate();
        assertFalse(ruleBlock.isValid());
        assertTrue(ruleBlock.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("action must be either a string or a list of strings")));
    }
    
    @Test
    void testRuleBlockEngineJsonOutput() {
        // Create rule block with complete data
        IRRuleBlock ruleBlock = new IRRuleBlock("rule-block-engine", "Engine Test Rule Block");
        ruleBlock.setDescription("Comprehensive rule block for engine JSON testing");
        ruleBlock.setScope("fund");
        ruleBlock.setScheduleId("schedule-annual-review");
        ruleBlock.setEventTriggerId("event-market-crash");
        ruleBlock.setCondition("portfolio_value_drop > 0.20");
        
        // Set multiple actions
        List<String> actions = Arrays.asList(
            "log('Market crash detected, portfolio value dropped by ' + portfolio_value_drop)",
            "freeze_new_investments()",
            "trigger_emergency_meeting()",
            "notify_limited_partners('Emergency procedures activated')",
            "initiate_risk_mitigation_strategy()"
        );
        ruleBlock.setAction(actions);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = ruleBlock.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("scope"));
        assertTrue(json.has("action"));
        
        assertEquals("rule-block-engine", json.get("id").asText());
        assertEquals("Engine Test Rule Block", json.get("name").asText());
        assertEquals("fund", json.get("scope").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertTrue(json.has("schedule"));
        assertTrue(json.has("eventTrigger"));
        assertTrue(json.has("condition"));
        assertEquals("Comprehensive rule block for engine JSON testing", json.get("description").asText());
        assertEquals("schedule-annual-review", json.get("schedule").asText());
        assertEquals("event-market-crash", json.get("eventTrigger").asText());
        assertEquals("portfolio_value_drop > 0.20", json.get("condition").asText());
        
        // Verify action array
        assertTrue(json.has("action"));
        com.fasterxml.jackson.databind.JsonNode actionJson = json.get("action");
        assertTrue(actionJson.isArray());
        assertEquals(5, actionJson.size());
        assertEquals("log('Market crash detected, portfolio value dropped by ' + portfolio_value_drop)", actionJson.get(0).asText());
        assertEquals("notify_limited_partners('Emergency procedures activated')", actionJson.get(3).asText());
        
        // Verify computed fields
        assertTrue(json.has("hasScheduleTrigger"));
        assertTrue(json.has("hasEventTrigger"));
        assertTrue(json.has("hasCondition"));
        assertTrue(json.has("hasMultipleActions"));
        assertTrue(json.has("actionCount"));
        assertTrue(json.has("triggerType"));
        
        assertEquals(true, json.get("hasScheduleTrigger").asBoolean());
        assertEquals(true, json.get("hasEventTrigger").asBoolean());
        assertEquals(true, json.get("hasCondition").asBoolean());
        assertEquals(true, json.get("hasMultipleActions").asBoolean());
        assertEquals(5, json.get("actionCount").asInt());
        assertEquals("schedule", json.get("triggerType").asText()); // First trigger type found
    }
    
    @Test
    void testDifferentRuleBlockTypes() {
        // Test schedule-triggered rule block
        IRRuleBlock scheduleRule = new IRRuleBlock("rule-block-schedule", "Scheduled Rule");
        scheduleRule.setDescription("Monthly portfolio review rule");
        scheduleRule.setScope("portfolio");
        scheduleRule.setScheduleId("schedule-monthly");
        scheduleRule.setAction("generate_monthly_report()");
        
        scheduleRule.validate();
        assertTrue(scheduleRule.isValid());
        assertTrue(scheduleRule.hasScheduleTrigger());
        assertFalse(scheduleRule.hasEventTrigger());
        assertFalse(scheduleRule.hasCondition());
        assertEquals("schedule", scheduleRule.getTriggerType());
        assertEquals(1, scheduleRule.getActionCount());
        assertFalse(scheduleRule.hasMultipleActions());
        
        // Test event-triggered rule block
        IRRuleBlock eventRule = new IRRuleBlock("rule-block-event", "Event Rule");
        eventRule.setDescription("Responds to tenant lease expiration events");
        eventRule.setScope("asset");
        eventRule.setEventTriggerId("event-lease-expiring");
        eventRule.setAction("initiate_lease_renewal_process()");
        
        eventRule.validate();
        assertTrue(eventRule.isValid());
        assertFalse(eventRule.hasScheduleTrigger());
        assertTrue(eventRule.hasEventTrigger());
        assertFalse(eventRule.hasCondition());
        assertEquals("event", eventRule.getTriggerType());
        assertEquals(1, eventRule.getActionCount());
        
        // Test condition-triggered rule block
        IRRuleBlock conditionRule = new IRRuleBlock("rule-block-condition", "Condition Rule");
        conditionRule.setDescription("Triggers when occupancy drops below 80%");
        conditionRule.setScope("asset");
        conditionRule.setCondition("occupancy_rate < 0.80");
        conditionRule.setAction("increase_marketing_budget()");
        
        conditionRule.validate();
        assertTrue(conditionRule.isValid());
        assertFalse(conditionRule.hasScheduleTrigger());
        assertFalse(conditionRule.hasEventTrigger());
        assertTrue(conditionRule.hasCondition());
        assertEquals("condition", conditionRule.getTriggerType());
        assertEquals(1, conditionRule.getActionCount());
        
        // Test complex rule block with multiple actions
        IRRuleBlock complexRule = new IRRuleBlock("rule-block-complex", "Complex Rule");
        complexRule.setDescription("Complex deal management rule with multiple triggers and actions");
        complexRule.setScope("deal");
        complexRule.setScheduleId("schedule-quarterly");
        complexRule.setCondition("deal_irr < target_irr * 0.9");
        
        List<String> complexActions = Arrays.asList(
            "log('Deal underperforming: IRR ' + deal_irr + ' vs target ' + target_irr)",
            "analyze_performance_factors()",
            "create_action_plan()",
            "schedule_investor_call()",
            "update_deal_status('under_review')"
        );
        complexRule.setAction(complexActions);
        
        complexRule.validate();
        assertTrue(complexRule.isValid());
        assertTrue(complexRule.hasScheduleTrigger());
        assertFalse(complexRule.hasEventTrigger());
        assertTrue(complexRule.hasCondition());
        assertEquals("schedule", complexRule.getTriggerType());
        assertEquals(5, complexRule.getActionCount());
        assertTrue(complexRule.hasMultipleActions());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.RuleBlockNode simpleNode = new dev.cfdl.ast.RuleBlockNode("rule-block-simple", "Simple Rule");
        simpleNode.setScope("component");
        simpleNode.setCondition("value > 0");
        simpleNode.setAction("activate()");
        astNodes.add(simpleNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify rule block has proper metadata
        IRRuleBlock irRuleBlock = (IRRuleBlock) result.getIrNodes().get(0);
        assertEquals("ruleBlock", irRuleBlock.getSchemaMetadata("entityType"));
        assertEquals("component", irRuleBlock.getSchemaMetadata("blockScope"));
        assertEquals("condition", irRuleBlock.getSchemaMetadata("triggerType"));
        assertEquals(Boolean.FALSE, irRuleBlock.getSchemaMetadata("hasScheduleTrigger"));
        assertEquals(Boolean.FALSE, irRuleBlock.getSchemaMetadata("hasEventTrigger"));
        assertEquals(Boolean.TRUE, irRuleBlock.getSchemaMetadata("hasCondition"));
        assertEquals(Boolean.FALSE, irRuleBlock.getSchemaMetadata("hasMultipleActions"));
        assertEquals(1, irRuleBlock.getSchemaMetadata("actionCount"));
    }
}