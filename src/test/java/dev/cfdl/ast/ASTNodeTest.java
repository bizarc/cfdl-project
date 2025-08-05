package dev.cfdl.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AST node classes.
 */
public class ASTNodeTest {
    
    @Test
    public void testDealNodeCreation() {
        DealNode deal = new DealNode("deal1", "Test Deal");
        
        assertEquals("deal1", deal.getId());
        assertEquals("Test Deal", deal.getName());
        assertEquals("https://cfdl.dev/ontology/entity/deal.schema.yaml", deal.getSchemaType());
        assertFalse(deal.hasValidationErrors());
    }
    
    @Test
    public void testDealNodeProperties() {
        DealNode deal = new DealNode("deal1", "Test Deal");
        
        deal.setDealType("commercial_real_estate");
        deal.setEntryDate("2023-01-01");
        deal.setExitDate("2028-01-01");
        deal.setCurrency("USD");
        deal.setHoldingPeriodYears(5.0);
        
        assertEquals("commercial_real_estate", deal.getDealType());
        assertEquals("2023-01-01", deal.getEntryDate());
        assertEquals("2028-01-01", deal.getExitDate());
        assertEquals("USD", deal.getCurrency());
        assertEquals(5.0, deal.getHoldingPeriodYears());
    }
    
    @Test
    public void testDealNodeAssetReferences() {
        DealNode deal = new DealNode("deal1", "Test Deal");
        
        deal.addAssetId("asset1");
        deal.addAssetId("asset2");
        
        assertEquals(2, deal.getAssetIds().size());
        assertTrue(deal.getAssetIds().contains("asset1"));
        assertTrue(deal.getAssetIds().contains("asset2"));
    }
    
    @Test
    public void testDealNodeJsonSerialization() {
        DealNode deal = new DealNode("deal1", "Test Deal");
        deal.setDealType("commercial_real_estate");
        deal.setCurrency("USD");
        deal.addAssetId("asset1");
        
        var json = deal.toJson();
        
        assertEquals("deal1", json.get("id").asText());
        assertEquals("Test Deal", json.get("name").asText());
        assertEquals("commercial_real_estate", json.get("dealType").asText());
        assertEquals("USD", json.get("currency").asText());
        assertEquals("asset1", json.get("assets").get(0).asText());
    }
    
    @Test
    public void testAssetNodeCreation() {
        AssetNode asset = new AssetNode("asset1", "Test Asset");
        
        assertEquals("asset1", asset.getId());
        assertEquals("Test Asset", asset.getName());
        assertEquals("https://cfdl.dev/ontology/entity/asset.schema.yaml", asset.getSchemaType());
        assertFalse(asset.hasValidationErrors());
    }
    
    @Test
    public void testAssetNodeProperties() {
        AssetNode asset = new AssetNode("asset1", "Test Asset");
        
        asset.setDealId("deal1");
        asset.setCategory("real_estate");
        asset.setDescription("A test asset");
        asset.setState("operational");
        
        assertEquals("deal1", asset.getDealId());
        assertEquals("real_estate", asset.getCategory());
        assertEquals("A test asset", asset.getDescription());
        assertEquals("operational", asset.getState());
    }
    
    @Test
    public void testStreamNodeCreation() {
        StreamNode stream = new StreamNode("stream1", "Test Stream");
        
        assertEquals("stream1", stream.getId());
        assertEquals("Test Stream", stream.getName());
        assertEquals("https://cfdl.dev/ontology/behavior/stream.schema.yaml", stream.getSchemaType());
        assertFalse(stream.hasValidationErrors());
    }
    
    @Test
    public void testStreamNodeProperties() {
        StreamNode stream = new StreamNode("stream1", "Test Stream");
        
        stream.setScope("asset");
        stream.setCategory("Revenue");
        stream.setSubType("Operating");
        stream.setAmount(1000.0);
        stream.setAmountType("amount_fixed");
        
        assertEquals("asset", stream.getScope());
        assertEquals("Revenue", stream.getCategory());
        assertEquals("Operating", stream.getSubType());
        assertEquals(1000.0, stream.getAmount());
        assertEquals("amount_fixed", stream.getAmountType());
    }
    
    @Test
    public void testValidationErrorHandling() {
        DealNode deal = new DealNode("deal1", "Test Deal");
        
        ValidationError error = new ValidationError(
            ValidationError.Severity.ERROR,
            "Missing required field: dealType"
        );
        
        deal.addValidationError(error);
        
        assertTrue(deal.hasValidationErrors());
        assertEquals(1, deal.getValidationErrors().size());
        assertEquals("Missing required field: dealType", deal.getValidationErrors().get(0).getMessage());
        assertEquals(ValidationError.Severity.ERROR, deal.getValidationErrors().get(0).getSeverity());
    }
    
    @Test
    public void testSourceLocation() {
        DealNode deal = new DealNode("deal1", "Test Deal");
        
        deal.setLineNumber(10);
        deal.setColumnNumber(5);
        
        assertEquals(10, deal.getLineNumber());
        assertEquals(5, deal.getColumnNumber());
    }
    
    @Test
    public void testGenericProperties() {
        DealNode deal = new DealNode("deal1", "Test Deal");
        
        deal.setProperty("customField", "customValue");
        deal.setProperty("numericField", 42);
        
        assertEquals("customValue", deal.getProperty("customField"));
        assertEquals(42, deal.getProperty("numericField"));
    }
}