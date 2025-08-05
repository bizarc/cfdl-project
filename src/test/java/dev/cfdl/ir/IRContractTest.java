package dev.cfdl.ir;

import dev.cfdl.ast.ContractNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRContract and ContractNode integration.
 * 
 * Tests the contract-level functionality needed for leases, PPAs,
 * service agreements, and other cash flow generating contracts.
 */
public class IRContractTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testContractNodeCreation() {
        ContractNode contractNode = new ContractNode("contract-lease-001", "Office Lease - Unit 101");
        contractNode.setDealId("deal-office-building");
        contractNode.setAssetId("asset-office-building");
        contractNode.setComponentId("component-unit-101");
        contractNode.setContractType("lease");
        contractNode.setDescription("10-year office lease with annual escalations");
        contractNode.setStartDate("2024-01-01");
        contractNode.setEndDate("2033-12-31");
        contractNode.setLineNumber(45);
        contractNode.setColumnNumber(12);
        
        // Add parties
        List<Map<String, Object>> parties = new ArrayList<>();
        Map<String, Object> lessor = new HashMap<>();
        lessor.put("partyId", "party-landlord");
        lessor.put("role", "lessor");
        parties.add(lessor);
        
        Map<String, Object> lessee = new HashMap<>();
        lessee.put("partyId", "party-tenant");
        lessee.put("role", "lessee");
        parties.add(lessee);
        
        contractNode.setProperty("parties", parties);
        
        // Add terms
        Map<String, Object> terms = new HashMap<>();
        terms.put("baseRent", 50000.0);
        terms.put("rentEscalation", 0.03); // 3% annual
        terms.put("securityDeposit", 100000.0);
        terms.put("renewalOptions", 2);
        contractNode.setProperty("terms", terms);
        
        // Add state config
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", List.of("pending", "active", "terminated", "expired"));
        stateConfig.put("initialState", "active");
        contractNode.setProperty("stateConfig", stateConfig);
        
        // Add stream references
        contractNode.addStreamId("stream-base-rent");
        contractNode.addStreamId("stream-cam-charges");
        
        // Verify contract node
        assertEquals("contract-lease-001", contractNode.getId());
        assertEquals("Office Lease - Unit 101", contractNode.getName());
        assertEquals("deal-office-building", contractNode.getDealId());
        assertEquals("asset-office-building", contractNode.getAssetId());
        assertEquals("component-unit-101", contractNode.getComponentId());
        assertEquals("lease", contractNode.getContractType());
        assertEquals("10-year office lease with annual escalations", contractNode.getDescription());
        assertEquals("2024-01-01", contractNode.getStartDate());
        assertEquals("2033-12-31", contractNode.getEndDate());
        assertEquals(2, contractNode.getStreamIds().size());
        assertEquals(45, contractNode.getLineNumber());
        assertEquals(12, contractNode.getColumnNumber());
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = contractNode.toJson();
        assertEquals("contract-lease-001", json.get("id").asText());
        assertEquals("lease", json.get("contractType").asText());
        assertEquals("2024-01-01", json.get("startDate").asText());
        assertTrue(json.has("parties"));
        if (json.get("parties").size() > 0) {
            assertEquals(2, json.get("parties").size());
        }
        assertTrue(json.has("terms"));
        if (json.get("terms").has("baseRent")) {
            assertEquals(50000.0, json.get("terms").get("baseRent").asDouble());
        }
    }
    
    @Test
    void testIRContractTransformation() {
        // Create contract AST node
        ContractNode contractNode = new ContractNode("contract-ppa-001", "Solar PPA");
        contractNode.setDealId("deal-solar-farm");
        contractNode.setAssetId("asset-solar-array");
        contractNode.setContractType("power-purchase-agreement");
        contractNode.setDescription("25-year power purchase agreement with utility");
        contractNode.setStartDate("2024-06-01");
        contractNode.setEndDate("2049-05-31");
        
        // Add parties
        List<Map<String, Object>> parties = new ArrayList<>();
        Map<String, Object> seller = new HashMap<>();
        seller.put("partyId", "party-solar-developer");
        seller.put("role", "seller");
        parties.add(seller);
        
        Map<String, Object> buyer = new HashMap<>();
        buyer.put("partyId", "party-utility-company");
        buyer.put("role", "buyer");
        parties.add(buyer);
        
        contractNode.setProperty("parties", parties);
        
        // Add terms
        Map<String, Object> terms = new HashMap<>();
        terms.put("pricePerMWh", 65.0);
        terms.put("escalationRate", 0.025); // 2.5% annual
        terms.put("minimumTake", 0.8); // 80% minimum take
        terms.put("capacityMW", 100);
        contractNode.setProperty("terms", terms);
        
        contractNode.addStreamId("stream-ppa-revenue");
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(contractNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRContract irContract = (IRContract) result.getIrNodes().get(0);
        
        // Verify IR contract
        assertEquals("contract-ppa-001", irContract.getId());
        assertEquals("Solar PPA", irContract.getName());
        assertEquals("deal-solar-farm", irContract.getDealId());
        assertEquals("asset-solar-array", irContract.getAssetId());
        assertEquals("power-purchase-agreement", irContract.getContractType());
        assertEquals("25-year power purchase agreement with utility", irContract.getDescription());
        assertEquals("2024-06-01", irContract.getStartDate());
        assertEquals("2049-05-31", irContract.getEndDate());
        
        // Verify parties
        assertNotNull(irContract.getParties());
        assertEquals(2, irContract.getParties().size());
        assertEquals("party-solar-developer", irContract.getParties().get(0).get("partyId"));
        assertEquals("seller", irContract.getParties().get(0).get("role"));
        
        // Verify terms
        assertNotNull(irContract.getTerms());
        assertEquals(65.0, irContract.getTerms().get("pricePerMWh"));
        assertEquals(100, irContract.getTerms().get("capacityMW"));
        
        // Verify dependencies
        assertTrue(irContract.getDependencies().contains("deal-solar-farm"));
        assertTrue(irContract.getDependencies().contains("asset-solar-array"));
        assertTrue(irContract.getDependencies().contains("party-solar-developer"));
        assertTrue(irContract.getDependencies().contains("party-utility-company"));
        assertTrue(irContract.getDependencies().contains("stream-ppa-revenue"));
        
        // Verify metadata enrichment
        assertEquals("contract", irContract.getSchemaMetadata("entityType"));
        assertEquals("power-purchase-agreement", irContract.getSchemaMetadata("contractClassification"));
        assertEquals(2, irContract.getSchemaMetadata("partyCount"));
        assertEquals(Boolean.TRUE, irContract.getSchemaMetadata("hasTerms"));
        assertEquals(Boolean.TRUE, irContract.getSchemaMetadata("isAssetLevel"));
        assertNotNull(irContract.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRContractValidation() {
        // Test valid contract
        IRContract validContract = new IRContract("contract-valid", "Valid Contract");
        validContract.setDealId("deal-test");
        validContract.setContractType("lease");
        validContract.setStartDate("2024-01-01");
        validContract.setEndDate("2025-01-01");
        
        List<Map<String, Object>> parties = new ArrayList<>();
        Map<String, Object> party = new HashMap<>();
        party.put("partyId", "party-test");
        party.put("role", "lessor");
        parties.add(party);
        validContract.setParties(parties);
        
        validContract.validate();
        assertTrue(validContract.isValid());
        assertEquals(0, validContract.getValidationMessages().size());
        
        // Test invalid contract - missing required fields
        IRContract invalidContract = new IRContract();
        // Missing id, name, dealId, contractType, parties, startDate, endDate
        
        invalidContract.validate();
        assertFalse(invalidContract.isValid());
        assertTrue(invalidContract.getValidationMessages().size() >= 7);
        assertTrue(invalidContract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidContract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("dealId is required")));
        assertTrue(invalidContract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("contractType is required")));
        assertTrue(invalidContract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("parties are required")));
    }
    
    @Test
    void testContractTypeValidation() {
        IRContract contract = new IRContract("contract-type-test", "Type Test Contract");
        contract.setDealId("deal-test");
        contract.setStartDate("2024-01-01");
        contract.setEndDate("2025-01-01");
        
        List<Map<String, Object>> parties = new ArrayList<>();
        Map<String, Object> party = new HashMap<>();
        party.put("partyId", "party-test");
        party.put("role", "lessor");
        parties.add(party);
        contract.setParties(parties);
        
        // Valid contract type
        contract.setContractType("lease");
        contract.validate();
        assertTrue(contract.isValid());
        
        // Invalid contract type
        contract = new IRContract("contract-type-invalid", "Invalid Type Contract");
        contract.setDealId("deal-test");
        contract.setContractType("invalid-type");
        contract.setStartDate("2024-01-01");
        contract.setEndDate("2025-01-01");
        contract.setParties(parties);
        
        contract.validate();
        assertFalse(contract.isValid());
        assertTrue(contract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("contractType must be one of")));
        
        // Test all valid contract types
        String[] validTypes = {
            "lease", "power-purchase-agreement", "loan-agreement", 
            "service-agreement", "royalty-agreement", "management-agreement", 
            "sale-leaseback", "other"
        };
        for (String validType : validTypes) {
            IRContract testContract = new IRContract("contract-" + validType, "Test " + validType);
            testContract.setDealId("deal-test");
            testContract.setContractType(validType);
            testContract.setStartDate("2024-01-01");
            testContract.setEndDate("2025-01-01");
            testContract.setParties(parties);
            
            testContract.validate();
            assertTrue(testContract.isValid(), "Contract type '" + validType + "' should be valid");
        }
    }
    
    @Test
    void testPartyValidation() {
        IRContract contract = new IRContract("contract-party-test", "Party Test Contract");
        contract.setDealId("deal-test");
        contract.setContractType("lease");
        contract.setStartDate("2024-01-01");
        contract.setEndDate("2025-01-01");
        
        // Invalid parties - missing partyId
        List<Map<String, Object>> invalidParties = new ArrayList<>();
        Map<String, Object> invalidParty = new HashMap<>();
        invalidParty.put("role", "lessor");
        // Missing partyId
        invalidParties.add(invalidParty);
        contract.setParties(invalidParties);
        
        contract.validate();
        assertFalse(contract.isValid());
        assertTrue(contract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("party 0 must have partyId")));
        
        // Invalid parties - missing role
        invalidParties = new ArrayList<>();
        invalidParty = new HashMap<>();
        invalidParty.put("partyId", "party-test");
        // Missing role
        invalidParties.add(invalidParty);
        contract.setParties(invalidParties);
        
        contract.validate();
        assertFalse(contract.isValid());
        assertTrue(contract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("party 0 must have role")));
    }
    
    @Test
    void testDateValidation() {
        IRContract contract = new IRContract("contract-date-test", "Date Test Contract");
        contract.setDealId("deal-test");
        contract.setContractType("lease");
        
        List<Map<String, Object>> parties = new ArrayList<>();
        Map<String, Object> party = new HashMap<>();
        party.put("partyId", "party-test");
        party.put("role", "lessor");
        parties.add(party);
        contract.setParties(parties);
        
        // Invalid dates - endDate before startDate
        contract.setStartDate("2025-01-01");
        contract.setEndDate("2024-01-01"); // Before start date
        
        contract.validate();
        assertFalse(contract.isValid());
        assertTrue(contract.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("endDate must be after startDate")));
        
        // Valid dates
        contract = new IRContract("contract-date-valid", "Valid Date Contract");
        contract.setDealId("deal-test");
        contract.setContractType("lease");
        contract.setParties(parties);
        contract.setStartDate("2024-01-01");
        contract.setEndDate("2025-01-01");
        
        contract.validate();
        if (!contract.isValid()) {
            System.out.println("Validation messages: " + contract.getValidationMessages());
        }
        assertTrue(contract.isValid());
    }
    
    @Test
    void testContractEngineJsonOutput() {
        // Create contract with complete data
        IRContract contract = new IRContract("contract-engine", "Engine Test Contract");
        contract.setDealId("deal-engine");
        contract.setAssetId("asset-engine");
        contract.setContractType("lease");
        contract.setDescription("Test contract for engine JSON output");
        contract.setStartDate("2024-01-01");
        contract.setEndDate("2029-12-31");
        
        // Add parties
        List<Map<String, Object>> parties = new ArrayList<>();
        Map<String, Object> lessor = new HashMap<>();
        lessor.put("partyId", "party-lessor");
        lessor.put("role", "lessor");
        parties.add(lessor);
        
        Map<String, Object> lessee = new HashMap<>();
        lessee.put("partyId", "party-lessee");
        lessee.put("role", "lessee");
        parties.add(lessee);
        
        contract.setParties(parties);
        
        // Add terms
        Map<String, Object> terms = new HashMap<>();
        terms.put("monthlyRent", 10000.0);
        terms.put("escalationRate", 0.03);
        terms.put("securityDeposit", 20000.0);
        contract.setTerms(terms);
        
        // Add state config
        Map<String, Object> stateConfig = new HashMap<>();
        stateConfig.put("allowedStates", List.of("active", "terminated"));
        stateConfig.put("initialState", "active");
        contract.setStateConfig(stateConfig);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = contract.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("dealId"));
        assertTrue(json.has("contractType"));
        assertTrue(json.has("startDate"));
        assertTrue(json.has("endDate"));
        assertTrue(json.has("parties"));
        
        assertEquals("contract-engine", json.get("id").asText());
        assertEquals("lease", json.get("contractType").asText());
        assertEquals("2024-01-01", json.get("startDate").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertTrue(json.has("assetId"));
        assertTrue(json.has("terms"));
        assertTrue(json.has("stateConfig"));
        
        // Verify parties structure
        com.fasterxml.jackson.databind.JsonNode partiesJson = json.get("parties");
        assertEquals(2, partiesJson.size());
        assertEquals("party-lessor", partiesJson.get(0).get("partyId").asText());
        assertEquals("lessor", partiesJson.get(0).get("role").asText());
        
        // Verify terms
        com.fasterxml.jackson.databind.JsonNode termsJson = json.get("terms");
        assertEquals(10000.0, termsJson.get("monthlyRent").asDouble());
        assertEquals(0.03, termsJson.get("escalationRate").asDouble());
    }
    
    @Test
    void testDifferentContractTypes() {
        // Test lease contract
        IRContract lease = new IRContract("contract-lease", "Office Lease");
        lease.setDealId("deal-office");
        lease.setContractType("lease");
        lease.setStartDate("2024-01-01");
        lease.setEndDate("2034-01-01");
        
        List<Map<String, Object>> leaseParties = new ArrayList<>();
        Map<String, Object> lessor = new HashMap<>();
        lessor.put("partyId", "party-landlord");
        lessor.put("role", "lessor");
        leaseParties.add(lessor);
        lease.setParties(leaseParties);
        
        lease.validate();
        assertTrue(lease.isValid());
        assertEquals("lease", lease.getContractType());
        
        // Test PPA contract
        IRContract ppa = new IRContract("contract-ppa", "Solar PPA");
        ppa.setDealId("deal-solar");
        ppa.setContractType("power-purchase-agreement");
        ppa.setStartDate("2024-06-01");
        ppa.setEndDate("2049-06-01");
        
        List<Map<String, Object>> ppaParties = new ArrayList<>();
        Map<String, Object> generator = new HashMap<>();
        generator.put("partyId", "party-solar-generator");
        generator.put("role", "seller");
        ppaParties.add(generator);
        ppa.setParties(ppaParties);
        
        ppa.validate();
        assertTrue(ppa.isValid());
        assertEquals("power-purchase-agreement", ppa.getContractType());
        
        // Test service agreement
        IRContract service = new IRContract("contract-service", "Property Management Agreement");
        service.setDealId("deal-property");
        service.setContractType("service-agreement");
        service.setStartDate("2024-01-01");
        service.setEndDate("2026-01-01");
        
        List<Map<String, Object>> serviceParties = new ArrayList<>();
        Map<String, Object> provider = new HashMap<>();
        provider.put("partyId", "party-property-manager");
        provider.put("role", "service_provider");
        serviceParties.add(provider);
        service.setParties(serviceParties);
        
        service.validate();
        assertTrue(service.isValid());
        assertEquals("service-agreement", service.getContractType());
        
        // Verify all contracts have proper metadata when processed through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        // Create AST nodes (would normally come from parser)
        dev.cfdl.ast.ContractNode leaseNode = new dev.cfdl.ast.ContractNode("contract-lease", "Office Lease");
        leaseNode.setDealId("deal-office");
        leaseNode.setContractType("lease");
        leaseNode.setStartDate("2024-01-01");
        leaseNode.setEndDate("2034-01-01");
        leaseNode.setProperty("parties", leaseParties);
        astNodes.add(leaseNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify contract has proper metadata
        IRContract irContract = (IRContract) result.getIrNodes().get(0);
        assertEquals("contract", irContract.getSchemaMetadata("entityType"));
        assertEquals("lease", irContract.getSchemaMetadata("contractClassification"));
        assertNotNull(irContract.getSchemaMetadata("partyCount"));
    }
}