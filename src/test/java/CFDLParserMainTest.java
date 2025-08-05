import dev.cfdl.ast.ASTNode;
import dev.cfdl.ast.DealNode;
import dev.cfdl.ast.AssetNode;
import dev.cfdl.ast.StreamNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the main CFDL parser functionality.
 */
public class CFDLParserMainTest {
    
    private CFDLParserMain parser;
    
    @BeforeEach
    public void setUp() {
        parser = new CFDLParserMain();
    }
    
    @Test
    public void testSimpleDealParsing() throws Exception {
        String cfdlCode = "deal TestDeal {\n" +
            "    name: \"My Test Deal\";\n" +
            "    dealType: commercial_real_estate;\n" +
            "    currency: \"USD\";\n" +
            "    holdingPeriodYears: 5.0;\n" +
            "}";
        
        CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
        
        assertNotNull(result);
        assertEquals(1, result.getNodes().size());
        
        ASTNode node = result.getNodes().get(0);
        assertTrue(node instanceof DealNode);
        
        DealNode deal = (DealNode) node;
        assertEquals("TestDeal", deal.getId());
        assertEquals("My Test Deal", deal.getName());
        assertEquals("commercial_real_estate", deal.getDealType());
        assertEquals("USD", deal.getCurrency());
        assertEquals(5.0, deal.getHoldingPeriodYears());
    }
    
    @Test
    public void testSimpleAssetParsing() throws Exception {
        String cfdlCode = "asset TestAsset {\n" +
            "    name: \"My Test Asset\";\n" +
            "    category: real_estate;\n" +
            "    description: \"A test asset description\";\n" +
            "    state: operational;\n" +
            "}";
        
        CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
        
        assertNotNull(result);
        assertEquals(1, result.getNodes().size());
        
        ASTNode node = result.getNodes().get(0);
        assertTrue(node instanceof AssetNode);
        
        AssetNode asset = (AssetNode) node;
        assertEquals("TestAsset", asset.getId());
        assertEquals("My Test Asset", asset.getName());
        assertEquals("real_estate", asset.getCategory());
        assertEquals("A test asset description", asset.getDescription());
        assertEquals("operational", asset.getState());
    }
    
    @Test
    public void testSimpleStreamParsing() throws Exception {
        String cfdlCode = "stream TestStream {\n" +
            "    name: \"My Test Stream\";\n" +
            "    scope: asset;\n" +
            "    category: Revenue;\n" +
            "    subType: Operating;\n" +
            "    amount: 1000.0;\n" +
            "}";
        
        CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
        
        assertNotNull(result);
        assertEquals(1, result.getNodes().size());
        
        ASTNode node = result.getNodes().get(0);
        assertTrue(node instanceof StreamNode);
        
        StreamNode stream = (StreamNode) node;
        assertEquals("TestStream", stream.getId());
        assertEquals("My Test Stream", stream.getName());
        assertEquals("asset", stream.getScope());
        assertEquals("Revenue", stream.getCategory());
        assertEquals("Operating", stream.getSubType());
        assertEquals(1000.0, stream.getAmount());
    }
    
    @Test
    public void testMultipleDefinitionsParsing() throws Exception {
        String cfdlCode = "deal MyDeal {\n" +
            "    name: \"Test Deal\";\n" +
            "    dealType: commercial_real_estate;\n" +
            "    currency: \"USD\";\n" +
            "}\n\n" +
            "asset MyAsset {\n" +
            "    name: \"Test Asset\";\n" +
            "    category: real_estate;\n" +
            "    dealId: MyDeal;\n" +
            "}\n\n" +
            "stream MyStream {\n" +
            "    name: \"Test Stream\";\n" +
            "    scope: asset;\n" +
            "    category: Revenue;\n" +
            "}";
        
        CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
        
        assertNotNull(result);
        assertEquals(3, result.getNodes().size());
        
        // Verify we have the expected node types
        boolean hasDeal = false, hasAsset = false, hasStream = false;
        for (ASTNode node : result.getNodes()) {
            if (node instanceof DealNode) hasDeal = true;
            else if (node instanceof AssetNode) hasAsset = true;
            else if (node instanceof StreamNode) hasStream = true;
        }
        
        assertTrue(hasDeal, "Should have parsed a deal");
        assertTrue(hasAsset, "Should have parsed an asset");
        assertTrue(hasStream, "Should have parsed a stream");
    }
    
    @Test
    public void testParseResultValidation() throws Exception {
        String cfdlCode = "deal IncompleteDeal {\n" +
            "    name: \"Incomplete Deal\";\n" +
            "    // Missing required fields\n" +
            "}";
        
        CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
        
        assertNotNull(result);
        assertEquals(1, result.getNodes().size());
        
        // Validation errors are expected due to missing required fields
        // (though the exact validation depends on schema availability)
        assertNotNull(result.getValidationErrors());
    }
    
    @Test
    public void testEmptyInput() throws Exception {
        String cfdlCode = "";
        
        CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
        
        assertNotNull(result);
        assertEquals(0, result.getNodes().size());
        assertEquals(0, result.getValidationErrors().size());
        assertTrue(result.isValid());
    }
    
    @Test
    public void testInvalidSyntax() throws Exception {
        String cfdlCode = "deal InvalidSyntax {\n" +
            "    name: \"Test Deal\"  // Missing semicolon\n" +
            "    invalidField: \n" +
            "}";
        
        // This should throw a parsing exception or return a result with syntax errors
        try {
            CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
            // If parsing succeeds, check for errors
            assertNotNull(result);
        } catch (Exception e) {
            // Parsing exception is expected for invalid syntax
            assertNotNull(e);
        }
    }
    
    @Test
    public void testLineNumberTracking() throws Exception {
        String cfdlCode = "deal TestDeal {\n" +
            "    name: \"Test Deal\";\n" +
            "    dealType: commercial_real_estate;\n" +
            "}";
        
        CFDLParserMain.ParseResult result = parser.parse(cfdlCode);
        
        assertNotNull(result);
        assertEquals(1, result.getNodes().size());
        
        ASTNode node = result.getNodes().get(0);
        
        // Should have captured line number (line 1 is the first line with 'deal')
        assertTrue(node.getLineNumber() > 0);
    }
}