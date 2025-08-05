package dev.cfdl.ir;

import dev.cfdl.ast.MarketDataNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Unit tests for IRMarketData and MarketDataNode integration.
 * 
 * Tests the market data functionality needed for external market data feeds
 * or reference indices that can be used to populate assumptions, overwrite
 * timeSeries values, or drive dynamic input parameters.
 */
public class IRMarketDataTest {
    
    private IRBuilder irBuilder;
    
    @BeforeEach
    void setUp() {
        irBuilder = new IRBuilder();
    }
    
    @Test
    void testMarketDataNodeCreation() {
        MarketDataNode marketDataNode = new MarketDataNode("market-data-treasury-10yr", "10-Year US Treasury Rate");
        marketDataNode.setDescription("US Treasury 10-year constant maturity rate from Federal Reserve");
        marketDataNode.setDataType("interest_rate");
        marketDataNode.setSymbol("USGG10YR");
        marketDataNode.setField("rate");
        marketDataNode.setRefreshScheduleId("schedule-daily-6am");
        marketDataNode.setLineNumber(55);
        marketDataNode.setColumnNumber(8);
        
        // Set source configuration
        Map<String, Object> source = new HashMap<>();
        source.put("type", "api");
        source.put("endpoint", "https://api.federalreserve.gov/v1/data/FRED/series");
        source.put("credentialsRef", "fred-api-key");
        
        Map<String, Object> params = new HashMap<>();
        params.put("series_id", "DGS10");
        params.put("format", "json");
        params.put("limit", "1");
        source.put("parameters", params);
        
        marketDataNode.setSource(source);
        
        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("category", "government_bonds");
        metadata.put("country", "US");
        metadata.put("maturity_years", 10);
        metadata.put("data_provider", "Federal Reserve");
        metadata.put("update_frequency", "daily");
        metadata.put("tags", List.of("treasury", "government", "benchmark", "interest-rate"));
        marketDataNode.setProperty("metadata", metadata);
        
        // Verify market data node
        assertEquals("market-data-treasury-10yr", marketDataNode.getId());
        assertEquals("10-Year US Treasury Rate", marketDataNode.getName());
        assertEquals("US Treasury 10-year constant maturity rate from Federal Reserve", marketDataNode.getDescription());
        assertEquals("interest_rate", marketDataNode.getDataType());
        assertEquals("USGG10YR", marketDataNode.getSymbol());
        assertEquals("rate", marketDataNode.getField());
        assertEquals("schedule-daily-6am", marketDataNode.getRefreshScheduleId());
        assertEquals(55, marketDataNode.getLineNumber());
        assertEquals(8, marketDataNode.getColumnNumber());
        
        // Verify source configuration
        assertNotNull(marketDataNode.getSource());
        assertEquals("api", marketDataNode.getSource().get("type"));
        assertEquals("https://api.federalreserve.gov/v1/data/FRED/series", marketDataNode.getSource().get("endpoint"));
        assertEquals("fred-api-key", marketDataNode.getSource().get("credentialsRef"));
        assertTrue(marketDataNode.getSource().containsKey("parameters"));
        
        // Verify JSON output
        com.fasterxml.jackson.databind.JsonNode json = marketDataNode.toJson();
        assertEquals("market-data-treasury-10yr", json.get("id").asText());
        assertEquals("10-Year US Treasury Rate", json.get("name").asText());
        assertEquals("interest_rate", json.get("dataType").asText());
        assertEquals("USGG10YR", json.get("symbol").asText());
        assertEquals("rate", json.get("field").asText());
        assertEquals("schedule-daily-6am", json.get("refreshSchedule").asText());
        assertTrue(json.has("source"));
        assertTrue(json.has("metadata"));
        
        if (json.get("source").has("type")) {
            assertEquals("api", json.get("source").get("type").asText());
        }
        if (json.get("metadata").has("category")) {
            assertEquals("government_bonds", json.get("metadata").get("category").asText());
        }
    }
    
    @Test
    void testIRMarketDataTransformation() {
        // Create market data AST node for equity index
        MarketDataNode marketDataNode = new MarketDataNode("market-data-sp500", "S&P 500 Index");
        marketDataNode.setDescription("Standard & Poor's 500 stock market index");
        marketDataNode.setDataType("index_value");
        marketDataNode.setSymbol("SPX");
        marketDataNode.setField("close");
        
        // Set multiple symbols example
        List<String> symbols = List.of("SPX", "^GSPC", "SP500");
        marketDataNode.setSymbol(symbols);
        
        // Set source configuration
        Map<String, Object> source = new HashMap<>();
        source.put("type", "service");
        source.put("endpoint", "bloomberg-terminal://equity-indices");
        
        Map<String, Object> params = new HashMap<>();
        params.put("fields", List.of("PX_LAST", "VOLUME", "PX_HIGH", "PX_LOW"));
        params.put("currency", "USD");
        source.put("parameters", params);
        
        marketDataNode.setSource(source);
        
        // Transform to IR
        List<dev.cfdl.ast.ASTNode> astNodes = List.of(marketDataNode);
        IRBuildResult result = irBuilder.build(astNodes);
        
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        IRMarketData irMarketData = (IRMarketData) result.getIrNodes().get(0);
        
        // Verify IR market data
        assertEquals("market-data-sp500", irMarketData.getId());
        assertEquals("S&P 500 Index", irMarketData.getName());
        assertEquals("Standard & Poor's 500 stock market index", irMarketData.getDescription());
        assertEquals("index_value", irMarketData.getDataType());
        assertEquals("close", irMarketData.getField());
        
        // Verify symbol handling
        assertTrue(irMarketData.hasMultipleSymbols());
        assertEquals(3, irMarketData.getSymbolCount());
        
        // Verify data type checks
        assertFalse(irMarketData.isInterestRate());
        assertTrue(irMarketData.isIndexValue());
        assertFalse(irMarketData.isFxRate());
        assertFalse(irMarketData.isInflationIndex());
        assertFalse(irMarketData.isCustomData());
        
        // Verify source properties
        assertEquals("service", irMarketData.getSourceType());
        assertEquals("bloomberg-terminal://equity-indices", irMarketData.getSourceEndpoint());
        assertFalse(irMarketData.hasCredentials());
        assertTrue(irMarketData.hasSourceParameters());
        assertFalse(irMarketData.hasRefreshSchedule());
        assertTrue(irMarketData.hasField());
        
        // Verify metadata enrichment
        assertEquals("marketData", irMarketData.getSchemaMetadata("entityType"));
        assertEquals("index_value", irMarketData.getSchemaMetadata("dataType"));
        assertEquals(Boolean.TRUE, irMarketData.getSchemaMetadata("hasMultipleSymbols"));
        assertEquals(3, irMarketData.getSchemaMetadata("symbolCount"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("isInterestRate"));
        assertEquals(Boolean.TRUE, irMarketData.getSchemaMetadata("isIndexValue"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("isFxRate"));
        assertEquals("service", irMarketData.getSchemaMetadata("sourceType"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("hasCredentials"));
        assertEquals(Boolean.TRUE, irMarketData.getSchemaMetadata("hasSourceParameters"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("hasRefreshSchedule"));
        assertEquals(Boolean.TRUE, irMarketData.getSchemaMetadata("hasField"));
        assertEquals("close", irMarketData.getSchemaMetadata("field"));
        assertNotNull(irMarketData.getSchemaMetadata("enrichmentTimestamp"));
    }
    
    @Test
    void testIRMarketDataValidation() {
        // Test valid market data
        IRMarketData validMarketData = new IRMarketData("market-data-valid", "Valid Market Data");
        validMarketData.setDescription("Valid market data for testing");
        validMarketData.setDataType("fx_rate");
        validMarketData.setSymbol("EURUSD");
        
        Map<String, Object> validSource = new HashMap<>();
        validSource.put("type", "api");
        validSource.put("endpoint", "https://api.exchangerates.com/v1/latest");
        validMarketData.setSource(validSource);
        
        validMarketData.validate();
        assertTrue(validMarketData.isValid());
        assertEquals(0, validMarketData.getValidationMessages().size());
        
        // Test invalid market data - missing required fields
        IRMarketData invalidMarketData = new IRMarketData();
        // Missing id, name, dataType, symbol, source
        
        invalidMarketData.validate();
        assertFalse(invalidMarketData.isValid());
        assertTrue(invalidMarketData.getValidationMessages().size() >= 5);
        assertTrue(invalidMarketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("id is required")));
        assertTrue(invalidMarketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("name is required")));
        assertTrue(invalidMarketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("dataType is required")));
        assertTrue(invalidMarketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("symbol is required")));
        assertTrue(invalidMarketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("source is required")));
    }
    
    @Test
    void testDataTypeValidation() {
        IRMarketData marketData = new IRMarketData("market-data-type-test", "Type Test");
        marketData.setSymbol("TEST");
        
        Map<String, Object> testSource = new HashMap<>();
        testSource.put("type", "api");
        testSource.put("endpoint", "https://api.test.com");
        marketData.setSource(testSource);
        
        // Valid data types
        String[] validDataTypes = {
            "interest_rate", "index_value", "fx_rate", "inflation_index", "custom"
        };
        for (String validDataType : validDataTypes) {
            marketData.setDataType(validDataType);
            marketData.validate();
            assertTrue(marketData.isValid(), "Data type '" + validDataType + "' should be valid");
        }
        
        // Invalid data type
        marketData.setDataType("invalid-type");
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("dataType must be one of")));
    }
    
    @Test
    void testSymbolValidation() {
        IRMarketData marketData = new IRMarketData("market-data-symbol-test", "Symbol Test");
        marketData.setDataType("custom");
        
        Map<String, Object> testSource = new HashMap<>();
        testSource.put("type", "api");
        testSource.put("endpoint", "https://api.test.com");
        marketData.setSource(testSource);
        
        // Empty string symbol - should fail
        marketData.setSymbol("");
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("symbol cannot be empty")));
        
        // Empty list symbol - should fail
        List<String> emptySymbols = new ArrayList<>();
        marketData.setSymbol(emptySymbols);
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("symbol list cannot be empty")));
        
        // List with null/empty symbols - should fail
        List<String> badSymbols = new ArrayList<>();
        badSymbols.add("VALID");
        badSymbols.add(null);
        badSymbols.add("");
        marketData.setSymbol(badSymbols);
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("symbol 1 cannot be null or empty")));
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("symbol 2 cannot be null or empty")));
        
        // Invalid symbol type - should fail
        marketData.setSymbol(123); // Integer instead of String or List
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("symbol must be either a string or a list of strings")));
        
        // Valid single string symbol - should pass
        IRMarketData validSingleSymbol = new IRMarketData("market-data-single", "Single Symbol");
        validSingleSymbol.setDataType("custom");
        validSingleSymbol.setSource(testSource);
        validSingleSymbol.setSymbol("VALID_SYMBOL");
        validSingleSymbol.validate();
        assertTrue(validSingleSymbol.isValid());
        assertFalse(validSingleSymbol.hasMultipleSymbols());
        assertEquals(1, validSingleSymbol.getSymbolCount());
        
        // Valid multiple symbols - should pass
        IRMarketData validMultipleSymbols = new IRMarketData("market-data-multiple", "Multiple Symbols");
        validMultipleSymbols.setDataType("custom");
        validMultipleSymbols.setSource(testSource);
        validMultipleSymbols.setSymbol(List.of("SYMBOL1", "SYMBOL2", "SYMBOL3"));
        validMultipleSymbols.validate();
        assertTrue(validMultipleSymbols.isValid());
        assertTrue(validMultipleSymbols.hasMultipleSymbols());
        assertEquals(3, validMultipleSymbols.getSymbolCount());
    }
    
    @Test
    void testSourceValidation() {
        IRMarketData marketData = new IRMarketData("market-data-source-test", "Source Test");
        marketData.setDataType("custom");
        marketData.setSymbol("TEST");
        
        // Missing source type - should fail
        Map<String, Object> sourceWithoutType = new HashMap<>();
        sourceWithoutType.put("endpoint", "https://api.test.com");
        marketData.setSource(sourceWithoutType);
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("source must have type")));
        
        // Invalid source type - should fail
        Map<String, Object> sourceWithInvalidType = new HashMap<>();
        sourceWithInvalidType.put("type", "invalid");
        sourceWithInvalidType.put("endpoint", "https://api.test.com");
        marketData.setSource(sourceWithInvalidType);
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("source type must be one of")));
        
        // Missing endpoint - should fail
        Map<String, Object> sourceWithoutEndpoint = new HashMap<>();
        sourceWithoutEndpoint.put("type", "api");
        marketData.setSource(sourceWithoutEndpoint);
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("source must have endpoint")));
        
        // Empty endpoint - should fail
        Map<String, Object> sourceWithEmptyEndpoint = new HashMap<>();
        sourceWithEmptyEndpoint.put("type", "api");
        sourceWithEmptyEndpoint.put("endpoint", "");
        marketData.setSource(sourceWithEmptyEndpoint);
        marketData.validate();
        assertFalse(marketData.isValid());
        assertTrue(marketData.getValidationMessages().stream()
            .anyMatch(msg -> msg.contains("endpoint cannot be empty")));
        
        // Valid sources for all source types - should pass
        String[] validSourceTypes = {"api", "database", "file", "service"};
        for (String sourceType : validSourceTypes) {
            IRMarketData validMarketData = new IRMarketData("market-data-" + sourceType, "Valid " + sourceType);
            validMarketData.setDataType("custom");
            validMarketData.setSymbol("TEST");
            
            Map<String, Object> validSource = new HashMap<>();
            validSource.put("type", sourceType);
            validSource.put("endpoint", "test-endpoint-for-" + sourceType);
            validSource.put("credentialsRef", "credentials-ref");
            validSource.put("parameters", Map.of("param1", "value1"));
            validMarketData.setSource(validSource);
            
            validMarketData.validate();
            assertTrue(validMarketData.isValid(), "Source type '" + sourceType + "' should be valid");
            assertEquals(sourceType, validMarketData.getSourceType());
            assertEquals("test-endpoint-for-" + sourceType, validMarketData.getSourceEndpoint());
            assertTrue(validMarketData.hasCredentials());
            assertTrue(validMarketData.hasSourceParameters());
        }
    }
    
    @Test
    void testMarketDataEngineJsonOutput() {
        // Create market data with complete data
        IRMarketData marketData = new IRMarketData("market-data-engine", "Engine Test Market Data");
        marketData.setDescription("Comprehensive market data for engine JSON testing");
        marketData.setDataType("inflation_index");
        marketData.setSymbol("CPIAUCSL");
        marketData.setField("value");
        marketData.setRefreshScheduleId("schedule-monthly");
        
        // Set comprehensive source
        Map<String, Object> source = new HashMap<>();
        source.put("type", "database");
        source.put("endpoint", "postgresql://marketdata.example.com:5432/fred");
        source.put("credentialsRef", "postgresql-credentials");
        
        Map<String, Object> params = new HashMap<>();
        params.put("table", "economic_indicators");
        params.put("series_column", "series_id");
        params.put("value_column", "value");
        params.put("date_column", "observation_date");
        source.put("parameters", params);
        
        marketData.setSource(source);
        
        // Get engine JSON
        com.fasterxml.jackson.databind.JsonNode json = marketData.toEngineJson();
        
        // Verify required fields
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("dataType"));
        assertTrue(json.has("symbol"));
        assertTrue(json.has("source"));
        
        assertEquals("market-data-engine", json.get("id").asText());
        assertEquals("Engine Test Market Data", json.get("name").asText());
        assertEquals("inflation_index", json.get("dataType").asText());
        assertEquals("CPIAUCSL", json.get("symbol").asText());
        
        // Verify optional fields
        assertTrue(json.has("description"));
        assertTrue(json.has("refreshSchedule"));
        assertTrue(json.has("field"));
        assertEquals("Comprehensive market data for engine JSON testing", json.get("description").asText());
        assertEquals("schedule-monthly", json.get("refreshSchedule").asText());
        assertEquals("value", json.get("field").asText());
        
        // Verify source object
        com.fasterxml.jackson.databind.JsonNode sourceJson = json.get("source");
        assertEquals("database", sourceJson.get("type").asText());
        assertEquals("postgresql://marketdata.example.com:5432/fred", sourceJson.get("endpoint").asText());
        assertEquals("postgresql-credentials", sourceJson.get("credentialsRef").asText());
        assertTrue(sourceJson.has("parameters"));
        
        // Verify computed fields
        assertTrue(json.has("hasMultipleSymbols"));
        assertTrue(json.has("symbolCount"));
        assertTrue(json.has("isInterestRate"));
        assertTrue(json.has("isIndexValue"));
        assertTrue(json.has("isFxRate"));
        assertTrue(json.has("isInflationIndex"));
        assertTrue(json.has("isCustomData"));
        assertTrue(json.has("sourceType"));
        assertTrue(json.has("hasCredentials"));
        assertTrue(json.has("hasSourceParameters"));
        assertTrue(json.has("hasRefreshSchedule"));
        assertTrue(json.has("hasField"));
        
        assertEquals(false, json.get("hasMultipleSymbols").asBoolean());
        assertEquals(1, json.get("symbolCount").asInt());
        assertEquals(false, json.get("isInterestRate").asBoolean());
        assertEquals(false, json.get("isIndexValue").asBoolean());
        assertEquals(false, json.get("isFxRate").asBoolean());
        assertEquals(true, json.get("isInflationIndex").asBoolean());
        assertEquals(false, json.get("isCustomData").asBoolean());
        assertEquals("database", json.get("sourceType").asText());
        assertEquals(true, json.get("hasCredentials").asBoolean());
        assertEquals(true, json.get("hasSourceParameters").asBoolean());
        assertEquals(true, json.get("hasRefreshSchedule").asBoolean());
        assertEquals(true, json.get("hasField").asBoolean());
    }
    
    @Test
    void testDifferentMarketDataTypes() {
        // Test interest rate market data
        IRMarketData interestRateData = new IRMarketData("market-data-libor", "LIBOR 3-Month USD");
        interestRateData.setDescription("3-month USD LIBOR interest rate");
        interestRateData.setDataType("interest_rate");
        interestRateData.setSymbol("USD3MLIBOR");
        interestRateData.setField("rate");
        
        Map<String, Object> apiSource = new HashMap<>();
        apiSource.put("type", "api");
        apiSource.put("endpoint", "https://api.quandl.com/v3/datasets/FRED/USD3MLIBOR");
        interestRateData.setSource(apiSource);
        
        interestRateData.validate();
        assertTrue(interestRateData.isValid());
        assertTrue(interestRateData.isInterestRate());
        assertFalse(interestRateData.isIndexValue());
        assertFalse(interestRateData.hasMultipleSymbols());
        assertEquals(1, interestRateData.getSymbolCount());
        assertEquals("api", interestRateData.getSourceType());
        assertTrue(interestRateData.hasField());
        
        // Test FX rate market data with multiple symbols
        IRMarketData fxRateData = new IRMarketData("market-data-eur-rates", "EUR Exchange Rates");
        fxRateData.setDescription("EUR exchange rates against major currencies");
        fxRateData.setDataType("fx_rate");
        fxRateData.setSymbol(List.of("EURUSD", "EURGBP", "EURJPY", "EURCHF"));
        
        Map<String, Object> fileSource = new HashMap<>();
        fileSource.put("type", "file");
        fileSource.put("endpoint", "/data/fx-rates/eur-daily.csv");
        fxRateData.setSource(fileSource);
        
        fxRateData.validate();
        assertTrue(fxRateData.isValid());
        assertFalse(fxRateData.isInterestRate());
        assertTrue(fxRateData.isFxRate());
        assertTrue(fxRateData.hasMultipleSymbols());
        assertEquals(4, fxRateData.getSymbolCount());
        assertEquals("file", fxRateData.getSourceType());
        assertFalse(fxRateData.hasField());
        assertFalse(fxRateData.hasRefreshSchedule());
        
        // Test custom market data with refresh schedule
        IRMarketData customData = new IRMarketData("market-data-vix", "CBOE Volatility Index");
        customData.setDescription("Chicago Board Options Exchange Volatility Index");
        customData.setDataType("custom");
        customData.setSymbol("VIX");
        customData.setField("close");
        customData.setRefreshScheduleId("schedule-intraday-15min");
        
        Map<String, Object> serviceSource = new HashMap<>();
        serviceSource.put("type", "service");
        serviceSource.put("endpoint", "cboe-market-data-service://volatility-indices");
        serviceSource.put("credentialsRef", "cboe-api-credentials");
        customData.setSource(serviceSource);
        
        customData.validate();
        assertTrue(customData.isValid());
        assertFalse(customData.isInterestRate());
        assertFalse(customData.isIndexValue());
        assertTrue(customData.isCustomData());
        assertFalse(customData.hasMultipleSymbols());
        assertEquals(1, customData.getSymbolCount());
        assertEquals("service", customData.getSourceType());
        assertTrue(customData.hasCredentials());
        assertFalse(customData.hasSourceParameters());
        assertTrue(customData.hasRefreshSchedule());
        assertTrue(customData.hasField());
        
        // Verify through IR builder
        List<dev.cfdl.ast.ASTNode> astNodes = new ArrayList<>();
        
        dev.cfdl.ast.MarketDataNode simpleNode = new dev.cfdl.ast.MarketDataNode("market-data-simple", "Simple Market Data");
        simpleNode.setDataType("index_value");
        simpleNode.setSymbol("SIMPLE");
        
        Map<String, Object> simpleSource = new HashMap<>();
        simpleSource.put("type", "api");
        simpleSource.put("endpoint", "https://api.simple.com");
        simpleNode.setSource(simpleSource);
        
        astNodes.add(simpleNode);
        
        IRBuildResult result = irBuilder.build(astNodes);
        assertTrue(result.isBuildSuccess());
        assertEquals(1, result.getNodeCount());
        
        // Verify market data has proper metadata
        IRMarketData irMarketData = (IRMarketData) result.getIrNodes().get(0);
        assertEquals("marketData", irMarketData.getSchemaMetadata("entityType"));
        assertEquals("index_value", irMarketData.getSchemaMetadata("dataType"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("hasMultipleSymbols"));
        assertEquals(1, irMarketData.getSchemaMetadata("symbolCount"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("isInterestRate"));
        assertEquals(Boolean.TRUE, irMarketData.getSchemaMetadata("isIndexValue"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("isFxRate"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("isInflationIndex"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("isCustomData"));
        assertEquals("api", irMarketData.getSchemaMetadata("sourceType"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("hasCredentials"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("hasSourceParameters"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("hasRefreshSchedule"));
        assertEquals(Boolean.FALSE, irMarketData.getSchemaMetadata("hasField"));
    }
}