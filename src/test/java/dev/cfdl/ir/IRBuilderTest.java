package dev.cfdl.ir;

import dev.cfdl.ast.*;
import dev.cfdl.validation.SchemaValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Comprehensive unit tests for IRBuilder.
 * 
 * Tests all phases of IR building:
 * - AST to IR transformation
 * - Schema metadata enrichment
 * - Reference resolution
 * - Validation
 */
public class IRBuilderTest {
    
    private IRBuilder irBuilder;
    private SchemaValidator mockSchemaValidator;
    
    @BeforeEach
    void setUp() {
        mockSchemaValidator = new SchemaValidator();
        irBuilder = new IRBuilder(mockSchemaValidator);
    }
    
    @Test
    void testBuildWithEmptyInput() {
        List<ASTNode> emptyList = new ArrayList<>();
        IRBuildResult result = irBuilder.build(emptyList);
        
        assertFalse(result.isBuildSuccess());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).contains("No AST nodes provided"));
        assertEquals(0, result.getNodeCount());
    }
    
    @Test
    void testBuildWithNullInput() {
        IRBuildResult result = irBuilder.build(null);
        
        assertFalse(result.isBuildSuccess());
        assertEquals(1, result.getErrors().size());
        assertTrue(result.getErrors().get(0).contains("No AST nodes provided"));
        assertEquals(0, result.getNodeCount());
    }
    
    @Test
    void testTransformSimpleDealNode() {
        // Create a complete DealNode with all required fields
        DealNode dealNode = new DealNode("deal-001", "Test Deal");
        dealNode.setDealType("commercial_real_estate");
        dealNode.setCurrency("USD");
        dealNode.setEntryDate("2024-01-01");
        dealNode.setExitDate("2029-01-01");
        dealNode.setAnalysisStart("2024-01-01");
        dealNode.setHoldingPeriodYears(5.0);
        dealNode.setLineNumber(10);
        dealNode.setColumnNumber(5);
        
        List<ASTNode> astNodes = List.of(dealNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRNode irNode = result.getIrNodes().get(0);
        assertTrue(irNode instanceof IRDeal);
        
        IRDeal irDeal = (IRDeal) irNode;
        assertEquals("deal-001", irDeal.getId());
        assertEquals("Test Deal", irDeal.getName());
        assertEquals("commercial_real_estate", irDeal.getDealType());
        assertEquals("USD", irDeal.getCurrency());
        assertEquals("2024-01-01", irDeal.getEntryDate());
        assertEquals("2029-01-01", irDeal.getExitDate());
        assertEquals("2024-01-01", irDeal.getAnalysisStart());
        assertEquals(Integer.valueOf(5), irDeal.getHoldingPeriodYears());
        assertEquals(10, irDeal.getLineNumber());
        assertEquals(5, irDeal.getColumnNumber());
    }
    
    @Test
    void testTransformDealNodeWithInvalidData() {
        // Create a DealNode missing required fields
        DealNode dealNode = new DealNode("deal-002", "Invalid Deal");
        // Missing required fields: dealType, currency, dates, holdingPeriodYears
        
        List<ASTNode> astNodes = List.of(dealNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        // Build should succeed but validation should fail
        assertFalse(result.isBuildSuccess());
        assertTrue(result.getErrors().size() > 0);
        assertEquals(1, result.getNodeCount());
        
        IRDeal irDeal = (IRDeal) result.getIrNodes().get(0);
        assertFalse(irDeal.isValid());
        assertTrue(irDeal.getValidationMessages().size() > 0);
    }
    
    @Test
    void testTransformSimpleAssetNode() {
        AssetNode assetNode = new AssetNode("asset-001", "Test Asset");
        assetNode.setDealId("deal-001");
        assetNode.setCategory("real_estate");
        assetNode.setDescription("Test property");
        assetNode.setLineNumber(20);
        assetNode.setColumnNumber(8);
        
        List<ASTNode> astNodes = List.of(assetNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRAsset irAsset = (IRAsset) result.getIrNodes().get(0);
        assertEquals("asset-001", irAsset.getId());
        assertEquals("Test Asset", irAsset.getName());
        assertEquals("deal-001", irAsset.getDealId());
        assertEquals("real_estate", irAsset.getCategory());
        assertEquals("Test property", irAsset.getDescription());
        assertEquals(20, irAsset.getLineNumber());
        assertEquals(8, irAsset.getColumnNumber());
        
        // Should have dependency on deal
        assertTrue(irAsset.getDependencies().contains("deal-001"));
    }
    
    @Test
    void testTransformAssetNodeWithInvalidCategory() {
        AssetNode assetNode = new AssetNode("asset-002", "Invalid Asset");
        assetNode.setDealId("deal-001");
        assetNode.setCategory("invalid_category"); // Invalid category
        
        List<ASTNode> astNodes = List.of(assetNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertFalse(result.isBuildSuccess());
        assertTrue(result.getErrors().size() > 0);
        
        IRAsset irAsset = (IRAsset) result.getIrNodes().get(0);
        assertFalse(irAsset.isValid());
        assertTrue(irAsset.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("category must be one of")));
    }
    
    @Test
    void testTransformSimpleStreamNode() {
        StreamNode streamNode = new StreamNode("stream-001", "Test Stream");
        streamNode.setScope("asset");
        streamNode.setCategory("Revenue");
        streamNode.setSubType("Operating");
        streamNode.setAmount(100000.0);
        streamNode.setDescription("Monthly rental income");
        streamNode.setLineNumber(30);
        streamNode.setColumnNumber(12);
        
        // Add required schedule
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("type", "recurring");
        schedule.put("startDate", "2024-01-01");
        Map<String, Object> recurrenceRule = new HashMap<>();
        recurrenceRule.put("freq", "MONTHLY");
        recurrenceRule.put("interval", 1);
        schedule.put("recurrenceRule", recurrenceRule);
        streamNode.setProperty("schedule", schedule);
        
        List<ASTNode> astNodes = List.of(streamNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRStream irStream = (IRStream) result.getIrNodes().get(0);
        assertEquals("stream-001", irStream.getId());
        assertEquals("Test Stream", irStream.getName());
        assertEquals("asset", irStream.getScope());
        assertEquals("Revenue", irStream.getCategory());
        assertEquals("Operating", irStream.getSubType());
        assertEquals(100000.0, irStream.getAmount());
        assertEquals("Monthly rental income", irStream.getDescription());
        assertNotNull(irStream.getSchedule());
        assertEquals(30, irStream.getLineNumber());
        assertEquals(12, irStream.getColumnNumber());
    }
    
    @Test
    void testTransformStreamNodeWithInvalidScope() {
        StreamNode streamNode = new StreamNode("stream-002", "Invalid Stream");
        streamNode.setScope("invalid_scope"); // Invalid scope
        streamNode.setCategory("Revenue");
        streamNode.setAmount(50000.0);
        
        List<ASTNode> astNodes = List.of(streamNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertFalse(result.isBuildSuccess());
        assertTrue(result.getErrors().size() > 0);
        
        IRStream irStream = (IRStream) result.getIrNodes().get(0);
        assertFalse(irStream.isValid());
        assertTrue(irStream.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("scope must be one of")));
    }
    
    @Test
    void testTransformComplexDealWithAssetsAndStreams() {
        // Create a complex deal with nested assets and streams
        DealNode dealNode = new DealNode("deal-complex", "Complex Deal");
        dealNode.setDealType("commercial_real_estate");
        dealNode.setCurrency("USD");
        dealNode.setEntryDate("2024-01-01");
        dealNode.setExitDate("2029-01-01");
        dealNode.setAnalysisStart("2024-01-01");
        dealNode.setHoldingPeriodYears(5.0);
        
        // Add calendar
        Map<String, Object> calendar = new HashMap<>();
        calendar.put("frequency", "monthly");
        calendar.put("businessDayConvention", "following");
        calendar.put("dayCount", "actual/365");
        dealNode.setProperty("calendar", calendar);
        
        // Add asset
        AssetNode assetNode = new AssetNode("asset-complex", "Complex Asset");
        assetNode.setDealId("deal-complex");
        assetNode.setCategory("real_estate");
        dealNode.addAssetId(assetNode.getId());
        
        // Add stream to asset
        StreamNode assetStream = new StreamNode("stream-asset", "Asset Stream");
        assetStream.setScope("asset");
        assetStream.setCategory("Revenue");
        assetStream.setAmount(75000.0);
        Map<String, Object> schedule1 = new HashMap<>();
        schedule1.put("type", "recurring");
        schedule1.put("startDate", "2024-01-01");
        Map<String, Object> recRule1 = new HashMap<>();
        recRule1.put("freq", "MONTHLY");
        recRule1.put("interval", 1);
        schedule1.put("recurrenceRule", recRule1);
        assetStream.setProperty("schedule", schedule1);
        assetNode.addStreamId(assetStream.getId());
        
        // Add deal-level stream
        StreamNode dealStream = new StreamNode("stream-deal", "Deal Stream");
        dealStream.setScope("deal");
        dealStream.setCategory("Expense");
        dealStream.setSubType("Financing");
        dealStream.setAmount(25000.0);
        Map<String, Object> schedule2 = new HashMap<>();
        schedule2.put("type", "recurring");
        schedule2.put("startDate", "2024-01-01");
        Map<String, Object> recRule2 = new HashMap<>();
        recRule2.put("freq", "MONTHLY");
        recRule2.put("interval", 1);
        schedule2.put("recurrenceRule", recRule2);
        dealStream.setProperty("schedule", schedule2);
        dealNode.addStreamId(dealStream.getId());
        
        List<ASTNode> astNodes = List.of(dealNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRDeal irDeal = (IRDeal) result.getIrNodes().get(0);
        
        // Verify dependencies were added (AST stores references as IDs)
        assertTrue(irDeal.getDependencies().contains("asset-complex"));
        assertTrue(irDeal.getDependencies().contains("stream-deal"));
        
        // The IR builder processes individual nodes, so assets and streams
        // would be separate IR nodes in the result, not nested objects
        assertEquals(0, irDeal.getAssets().size()); // No nested objects in this test
        assertEquals(0, irDeal.getStreams().size()); // No nested objects in this test
    }
    
    @Test
    void testSchemaMetadataEnrichment() {
        DealNode dealNode = new DealNode("deal-meta", "Metadata Test");
        dealNode.setDealType("commercial_real_estate");
        dealNode.setCurrency("USD");
        dealNode.setEntryDate("2024-01-01");
        dealNode.setExitDate("2029-01-01");
        dealNode.setAnalysisStart("2024-01-01");
        dealNode.setHoldingPeriodYears(5.0);
        
        List<ASTNode> astNodes = List.of(dealNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        
        IRDeal irDeal = (IRDeal) result.getIrNodes().get(0);
        Map<String, Object> metadata = irDeal.getSchemaMetadata();
        
        assertEquals("deal", metadata.get("entityType"));
        assertEquals("https://cfdl.dev/ontology/entity/deal.schema.yaml", metadata.get("schemaType"));
        assertFalse((Boolean) metadata.get("hasAssets"));
        assertFalse((Boolean) metadata.get("hasStreams"));
        assertEquals(0, metadata.get("participantCount"));
        assertNotNull(metadata.get("required"));
        assertNotNull(metadata.get("optional"));
        assertNotNull(metadata.get("enrichmentTimestamp"));
    }
    
    @Test
    void testReferenceResolution() {
        // Create deal and asset with reference relationship
        DealNode dealNode = new DealNode("deal-ref", "Reference Test");
        dealNode.setDealType("commercial_real_estate");
        dealNode.setCurrency("USD");
        dealNode.setEntryDate("2024-01-01");
        dealNode.setExitDate("2029-01-01");
        dealNode.setAnalysisStart("2024-01-01");
        dealNode.setHoldingPeriodYears(5.0);
        dealNode.setProperty("capitalStackId", "capital-stack-001");
        
        AssetNode assetNode = new AssetNode("asset-ref", "Referenced Asset");
        assetNode.setDealId("deal-ref");
        assetNode.setCategory("real_estate");
        
        List<ASTNode> astNodes = List.of(dealNode, assetNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(2, result.getNodeCount());
        
        // Should have warning about unresolved capital stack reference
        assertTrue(result.getWarnings().stream()
            .anyMatch(w -> w.contains("capital-stack-001")));
        
        // Asset should resolve reference to deal
        IRAsset irAsset = (IRAsset) result.getIrNodes().stream()
            .filter(n -> n.getId().equals("asset-ref"))
            .findFirst().orElse(null);
        assertNotNull(irAsset);
        assertTrue(irAsset.getDependencies().contains("deal-ref"));
    }
    
    @Test
    void testScheduleValidation() {
        StreamNode streamNode = new StreamNode("stream-schedule", "Schedule Test");
        streamNode.setScope("asset");
        streamNode.setCategory("Revenue");
        streamNode.setAmount(100000.0);
        
        // Invalid schedule - missing required fields
        Map<String, Object> invalidSchedule = new HashMap<>();
        invalidSchedule.put("type", "recurring");
        // Missing startDate and recurrenceRule
        streamNode.setProperty("schedule", invalidSchedule);
        
        List<ASTNode> astNodes = List.of(streamNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertFalse(result.isBuildSuccess());
        assertTrue(result.getErrors().size() > 0);
        
        IRStream irStream = (IRStream) result.getIrNodes().get(0);
        assertFalse(irStream.isValid());
        assertTrue(irStream.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("schedule must have")));
    }
    
    @Test
    void testIRBuildResultSummary() {
        DealNode dealNode = new DealNode("deal-summary", "Summary Test");
        dealNode.setDealType("commercial_real_estate");
        dealNode.setCurrency("USD");
        dealNode.setEntryDate("2024-01-01");
        dealNode.setExitDate("2029-01-01");
        dealNode.setAnalysisStart("2024-01-01");
        dealNode.setHoldingPeriodYears(5.0);
        
        List<ASTNode> astNodes = List.of(dealNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertTrue(result.isFullyValid());
        assertFalse(result.hasIssues());
        
        String summary = result.getSummary();
        assertTrue(summary.contains("success=true"));
        assertTrue(summary.contains("nodes=1"));
        assertTrue(summary.contains("errors=0"));
        
        String detailedReport = result.getDetailedReport();
        assertTrue(detailedReport.contains("Build Success: true"));
        assertTrue(detailedReport.contains("Nodes Built: 1"));
    }
    
    @Test
    void testEngineJsonOutput() {
        // Create a complete deal with all data types
        DealNode dealNode = new DealNode("deal-engine", "Engine Test");
        dealNode.setDealType("commercial_real_estate");
        dealNode.setCurrency("USD");
        dealNode.setEntryDate("2024-01-01");
        dealNode.setExitDate("2029-01-01");
        dealNode.setAnalysisStart("2024-01-01");
        dealNode.setHoldingPeriodYears(5.0);
        
        // Add calendar
        Map<String, Object> calendar = new HashMap<>();
        calendar.put("frequency", "monthly");
        calendar.put("businessDayConvention", "following");
        dealNode.setProperty("calendar", calendar);
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        Map<String, Object> participant = new HashMap<>();
        participant.put("partyId", "party-001");
        participant.put("role", "sponsor");
        participant.put("amount", 1000000);
        participants.add(participant);
        dealNode.setProperty("participants", participants);
        
        List<ASTNode> astNodes = List.of(dealNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        
        IRDeal irDeal = (IRDeal) result.getIrNodes().get(0);
        com.fasterxml.jackson.databind.JsonNode engineJson = irDeal.toEngineJson();
        
        assertTrue(engineJson.has("id"));
        assertTrue(engineJson.has("name"));
        assertTrue(engineJson.has("dealType"));
        assertTrue(engineJson.has("currency"));
        assertTrue(engineJson.has("calendar"));
        assertTrue(engineJson.has("participants"));
        assertEquals("deal-engine", engineJson.get("id").asText());
        assertEquals("commercial_real_estate", engineJson.get("dealType").asText());
        assertEquals(1, engineJson.get("participants").size());
    }
}