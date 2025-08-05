package dev.cfdl.ir;

import dev.cfdl.ast.ASTNode;
import dev.cfdl.ast.DealNode;
import dev.cfdl.ast.AssetNode;
import dev.cfdl.ast.StreamNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Integration tests for IRBuilder using realistic CFDL data structures.
 * 
 * These tests demonstrate the IR Builder working with complete,
 * realistic deal structures similar to the example CFDL files.
 */
public class IRBuilderIntegrationTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testCompleteOfficeBuilding() {
        // Create a realistic office building deal similar to examples/office-building-deal.cfdl
        List<ASTNode> astNodes = createOfficeBuilding();
        
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess(), "IR build should succeed for complete office building");
        assertEquals(3, result.getNodeCount(), "Should have deal, asset, and stream nodes");
        assertTrue(result.isFullyValid(), "All nodes should be valid");
        
        // Verify the deal IR
        IRDeal deal = findDealInResult(result, "office-building-123");
        assertNotNull(deal, "Should find the deal IR node");
        assertEquals("commercial_real_estate", deal.getDealType());
        assertEquals("USD", deal.getCurrency());
        assertEquals(Integer.valueOf(5), deal.getHoldingPeriodYears());
        assertNotNull(deal.getCalendar());
        
        // Verify metadata enrichment
        assertEquals("deal", deal.getSchemaMetadata("entityType"));
        assertNotNull(deal.getSchemaMetadata("enrichmentTimestamp"));
        
        // Verify the asset IR
        IRAsset asset = findAssetInResult(result, "office-building-main");
        assertNotNull(asset, "Should find the asset IR node");
        assertEquals("real_estate", asset.getCategory());
        assertEquals("office-building-123", asset.getDealId());
        assertNotNull(asset.getLocation());
        
        // Verify the stream IR
        IRStream stream = findStreamInResult(result, "rental-income");
        assertNotNull(stream, "Should find the stream IR node");
        assertEquals("asset", stream.getScope());
        assertEquals("Revenue", stream.getCategory());
        assertEquals("Operating", stream.getSubType());
        assertEquals(750000.0, stream.getAmount());
        assertNotNull(stream.getSchedule());
        assertNotNull(stream.getGrowth());
        
        // Verify engine JSON output
        com.fasterxml.jackson.databind.JsonNode dealJson = deal.toEngineJson();
        assertTrue(dealJson.has("id"));
        assertTrue(dealJson.has("dealType"));
        assertTrue(dealJson.has("calendar"));
        
        com.fasterxml.jackson.databind.JsonNode streamJson = stream.toEngineJson();
        assertTrue(streamJson.has("schedule"));
        assertTrue(streamJson.has("growth"));
        assertEquals("MONTHLY", streamJson.get("schedule").get("recurrenceRule").get("freq").asText());
        
        // Verify no critical errors
        assertFalse(result.hasIssues() && !result.getErrors().isEmpty(), 
            "Should not have critical errors: " + result.getDetailedReport());
    }
    
    @Test
    void testPortfolioWithMultipleAssets() {
        // Create a realistic portfolio similar to examples/multi-asset-portfolio.cfdl
        List<ASTNode> astNodes = createMultiAssetPortfolio();
        
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess(), "IR build should succeed for multi-asset portfolio");
        assertTrue(result.isFullyValid(), "All nodes should be valid");
        
        // Should have: 1 deal + 2 assets = 3 nodes (streams would be separate if included)
        assertTrue(result.getNodeCount() >= 3, "Should have multiple IR nodes for portfolio");
        
        IRDeal deal = findDealInResult(result, "retail-portfolio-2024");
        assertNotNull(deal);
        assertEquals("commercial_real_estate", deal.getDealType());
        
        // Verify multiple participants
        assertNotNull(deal.getParticipants());
        assertEquals(3, deal.getParticipants().size()); // sponsor, senior lender, mezz lender
        
        // Verify assets
        IRAsset shoppingCenter = findAssetInResult(result, "shopping-center-a");
        IRAsset stripMall = findAssetInResult(result, "strip-mall-b");
        assertNotNull(shoppingCenter);
        assertNotNull(stripMall);
        
        // Verify both assets have proper location data
        assertNotNull(shoppingCenter.getLocation());
        assertNotNull(stripMall.getLocation());
        
        Map<String, Object> location1 = shoppingCenter.getLocation();
        @SuppressWarnings("unchecked")
        Map<String, Object> address1 = (Map<String, Object>) location1.get("address");
        assertEquals("Westfield", address1.get("city"));
        assertEquals("Dallas-Fort Worth", location1.get("market"));
    }
    
    @Test
    void testRenewableEnergyProject() {
        // Create a realistic renewable energy project similar to examples/renewable-energy-project.cfdl
        List<ASTNode> astNodes = createRenewableEnergyProject();
        
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess(), "IR build should succeed for renewable energy project");
        assertTrue(result.isFullyValid(), "All nodes should be valid");
        
        IRDeal deal = findDealInResult(result, "solar-farm-nevada-2024");
        assertNotNull(deal);
        assertEquals("renewable_energy_project", deal.getDealType());
        assertEquals(Integer.valueOf(25), deal.getHoldingPeriodYears()); // Long-term project
        
        IRAsset asset = findAssetInResult(result, "solar-array-main");
        assertNotNull(asset);
        assertEquals("physical_asset", asset.getCategory());
        
        // Verify complex attributes
        assertNotNull(asset.getAttributes());
        Map<String, Object> attributes = asset.getAttributes();
        assertEquals(100, attributes.get("capacity_mw_dc"));
        assertEquals(0.005, attributes.get("annual_degradation_rate"));
        
        // Find PPA revenue stream
        IRStream ppaStream = findStreamInResult(result, "ppa-revenue");
        assertNotNull(ppaStream);
        assertEquals("Revenue", ppaStream.getCategory());
        assertEquals("Operating", ppaStream.getSubType());
        
        // Verify complex growth model
        assertNotNull(ppaStream.getGrowth());
        Map<String, Object> growth = ppaStream.getGrowth();
        assertEquals("expression", growth.get("type"));
        assertNotNull(growth.get("expr"));
        
        // Find one-time tax credit stream
        IRStream itcStream = findStreamInResult(result, "itc-benefit");
        assertNotNull(itcStream);
        assertEquals("Revenue", itcStream.getCategory());
        assertEquals("Tax", itcStream.getSubType());
        assertEquals(48000000.0, itcStream.getAmount()); // Large tax credit
        
        // Verify one-time schedule
        Map<String, Object> schedule = itcStream.getSchedule();
        assertEquals("oneTime", schedule.get("type"));
        assertEquals("2024-12-31", schedule.get("date"));
    }
    
    // Helper methods to create realistic test data
    
    private List<ASTNode> createOfficeBuilding() {
        List<ASTNode> nodes = new ArrayList<>();
        
        // Create deal
        DealNode deal = new DealNode("office-building-123", "Downtown Office Building");
        deal.setDealType("commercial_real_estate");
        deal.setCurrency("USD");
        deal.setEntryDate("2024-01-15");
        deal.setExitDate("2029-01-15");
        deal.setAnalysisStart("2024-01-01");
        deal.setHoldingPeriodYears(5.0);
        
        // Add calendar
        Map<String, Object> calendar = new HashMap<>();
        calendar.put("frequency", "monthly");
        calendar.put("businessDayConvention", "following");
        calendar.put("dayCount", "actual/365");
        deal.setProperty("calendar", calendar);
        
        // Add participants
        List<Map<String, Object>> participants = new ArrayList<>();
        Map<String, Object> sponsor = new HashMap<>();
        sponsor.put("partyId", "equity-sponsor");
        sponsor.put("role", "sponsor");
        sponsor.put("amount", 8000000);
        participants.add(sponsor);
        deal.setProperty("participants", participants);
        
        nodes.add(deal);
        
        // Create asset
        AssetNode asset = new AssetNode("office-building-main", "Main Office Building");
        asset.setDealId("office-building-123");
        asset.setCategory("real_estate");
        asset.setDescription("200,000 SF Class A office space");
        
        // Add location
        Map<String, Object> location = new HashMap<>();
        Map<String, Object> address = new HashMap<>();
        address.put("street_address", "123 Business Way");
        address.put("city", "Metro City");
        address.put("state", "CA");
        address.put("country", "USA");
        location.put("address", address);
        location.put("market", "Downtown Metro City");
        asset.setProperty("location", location);
        
        nodes.add(asset);
        
        // Create stream
        StreamNode stream = new StreamNode("rental-income", "Rental Income");
        stream.setScope("asset");
        stream.setCategory("Revenue");
        stream.setSubType("Operating");
        stream.setAmount(750000.0);
        stream.setDescription("Monthly rental income from office tenants");
        
        // Add schedule
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("type", "recurring");
        schedule.put("startDate", "2024-01-01");
        Map<String, Object> recurrenceRule = new HashMap<>();
        recurrenceRule.put("freq", "MONTHLY");
        recurrenceRule.put("interval", 1);
        schedule.put("recurrenceRule", recurrenceRule);
        stream.setProperty("schedule", schedule);
        
        // Add growth
        Map<String, Object> growth = new HashMap<>();
        growth.put("type", "fixed");
        growth.put("rate", 0.025);
        stream.setProperty("growth", growth);
        
        // Add tags
        List<String> tags = List.of("GAAP", "Forecast", "NOI");
        stream.setProperty("tags", tags);
        
        nodes.add(stream);
        
        return nodes;
    }
    
    private List<ASTNode> createMultiAssetPortfolio() {
        List<ASTNode> nodes = new ArrayList<>();
        
        // Create deal
        DealNode deal = new DealNode("retail-portfolio-2024", "Regional Retail Portfolio");
        deal.setDealType("commercial_real_estate");
        deal.setCurrency("USD");
        deal.setEntryDate("2024-03-01");
        deal.setExitDate("2031-03-01");
        deal.setAnalysisStart("2024-01-01");
        deal.setHoldingPeriodYears(7.0);
        
        // Add multiple participants
        List<Map<String, Object>> participants = new ArrayList<>();
        
        Map<String, Object> sponsor = new HashMap<>();
        sponsor.put("partyId", "reit-sponsor");
        sponsor.put("role", "sponsor");
        sponsor.put("amount", 15000000);
        participants.add(sponsor);
        
        Map<String, Object> seniorLender = new HashMap<>();
        seniorLender.put("partyId", "insurance-lender");
        seniorLender.put("role", "lender");
        seniorLender.put("amount", 45000000);
        participants.add(seniorLender);
        
        Map<String, Object> mezzLender = new HashMap<>();
        mezzLender.put("partyId", "mezzanine-fund");
        mezzLender.put("role", "lender");
        mezzLender.put("amount", 10000000);
        participants.add(mezzLender);
        
        deal.setProperty("participants", participants);
        nodes.add(deal);
        
        // Create first asset
        AssetNode asset1 = new AssetNode("shopping-center-a", "Westfield Shopping Center");
        asset1.setDealId("retail-portfolio-2024");
        asset1.setCategory("real_estate");
        
        Map<String, Object> location1 = new HashMap<>();
        Map<String, Object> address1 = new HashMap<>();
        address1.put("street_address", "456 Commerce Blvd");
        address1.put("city", "Westfield");
        address1.put("state", "TX");
        address1.put("country", "USA");
        location1.put("address", address1);
        location1.put("market", "Dallas-Fort Worth");
        asset1.setProperty("location", location1);
        nodes.add(asset1);
        
        // Create second asset
        AssetNode asset2 = new AssetNode("strip-mall-b", "Eastgate Strip Mall");
        asset2.setDealId("retail-portfolio-2024");
        asset2.setCategory("real_estate");
        
        Map<String, Object> location2 = new HashMap<>();
        Map<String, Object> address2 = new HashMap<>();
        address2.put("street_address", "789 Main Street");
        address2.put("city", "Eastgate");
        address2.put("state", "TX");
        address2.put("country", "USA");
        location2.put("address", address2);
        location2.put("market", "Dallas-Fort Worth");
        asset2.setProperty("location", location2);
        nodes.add(asset2);
        
        return nodes;
    }
    
    private List<ASTNode> createRenewableEnergyProject() {
        List<ASTNode> nodes = new ArrayList<>();
        
        // Create deal
        DealNode deal = new DealNode("solar-farm-nevada-2024", "Desert Solar Farm Project");
        deal.setDealType("renewable_energy_project");
        deal.setCurrency("USD");
        deal.setEntryDate("2024-06-01");
        deal.setExitDate("2049-12-31");
        deal.setAnalysisStart("2024-01-01");
        deal.setHoldingPeriodYears(25.0); // Long-term project
        nodes.add(deal);
        
        // Create solar asset
        AssetNode asset = new AssetNode("solar-array-main", "Main Solar Array");
        asset.setDealId("solar-farm-nevada-2024");
        asset.setCategory("physical_asset");
        asset.setDescription("100MW DC solar photovoltaic array with tracking systems");
        
        // Add complex attributes
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("capacity_mw_dc", 100);
        attributes.put("capacity_mw_ac", 80);
        attributes.put("panel_count", 312500);
        attributes.put("annual_degradation_rate", 0.005);
        attributes.put("project_life_years", 25);
        asset.setProperty("attributes", attributes);
        nodes.add(asset);
        
        // Create PPA revenue stream
        StreamNode ppaStream = new StreamNode("ppa-revenue", "PPA Revenue");
        ppaStream.setScope("asset");
        ppaStream.setCategory("Revenue");
        ppaStream.setSubType("Operating");
        ppaStream.setAmount(625000.0);
        
        // Add complex growth model
        Map<String, Object> growth = new HashMap<>();
        growth.put("type", "expression");
        growth.put("expr", "base_amount * (1 + escalation_rate)^year * (1 - degradation_rate)^year");
        ppaStream.setProperty("growth", growth);
        
        // Add recurring schedule
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("type", "recurring");
        schedule.put("startDate", "2024-12-01");
        Map<String, Object> recurrenceRule = new HashMap<>();
        recurrenceRule.put("freq", "MONTHLY");
        recurrenceRule.put("interval", 1);
        schedule.put("recurrenceRule", recurrenceRule);
        ppaStream.setProperty("schedule", schedule);
        nodes.add(ppaStream);
        
        // Create ITC stream (one-time tax benefit)
        StreamNode itcStream = new StreamNode("itc-benefit", "Investment Tax Credit");
        itcStream.setScope("deal");
        itcStream.setCategory("Revenue");
        itcStream.setSubType("Tax");
        itcStream.setAmount(48000000.0); // 30% of $160M project cost
        
        // Add one-time schedule
        Map<String, Object> itcSchedule = new HashMap<>();
        itcSchedule.put("type", "oneTime");
        itcSchedule.put("date", "2024-12-31");
        itcStream.setProperty("schedule", itcSchedule);
        nodes.add(itcStream);
        
        return nodes;
    }
    
    // Helper methods to find specific IR nodes in results
    
    private IRDeal findDealInResult(IRBuildResult result, String dealId) {
        return result.getIrNodes().stream()
            .filter(node -> node instanceof IRDeal && dealId.equals(node.getId()))
            .map(node -> (IRDeal) node)
            .findFirst()
            .orElse(null);
    }
    
    private IRAsset findAssetInResult(IRBuildResult result, String assetId) {
        return result.getIrNodes().stream()
            .filter(node -> node instanceof IRAsset && assetId.equals(node.getId()))
            .map(node -> (IRAsset) node)
            .findFirst()
            .orElse(null);
    }
    
    private IRStream findStreamInResult(IRBuildResult result, String streamId) {
        return result.getIrNodes().stream()
            .filter(node -> node instanceof IRStream && streamId.equals(node.getId()))
            .map(node -> (IRStream) node)
            .findFirst()
            .orElse(null);
    }
}