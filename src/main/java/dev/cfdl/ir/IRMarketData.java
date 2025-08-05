package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Market Data entities.
 * 
 * Corresponds to ontology/behavior/market-data.schema.yaml and contains all
 * normalized market data properties plus schema metadata needed for execution.
 * 
 * Market data defines external market data feeds or reference indices that can be
 * used to populate assumptions, overwrite timeSeries values, or drive dynamic input
 * parameters in streams and logic blocks.
 */
public class IRMarketData extends IRNode {
    private String description;
    private String dataType;
    private Object symbol; // Can be String or List<String>
    private Map<String, Object> source;
    private String refreshScheduleId;
    private String field;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRMarketData() {
        super();
        this.setSchemaType("https://cfdl.dev/ontology/behavior/market-data.schema.yaml");
    }
    
    public IRMarketData(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/behavior/market-data.schema.yaml");
    }
    
    // Getters and setters for market data-specific properties
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { 
        this.dataType = dataType;
        setProperty("dataType", dataType);
    }
    
    public Object getSymbol() { return symbol; }
    public void setSymbol(Object symbol) { 
        this.symbol = symbol;
        setProperty("symbol", symbol);
    }
    
    public Map<String, Object> getSource() { return source; }
    public void setSource(Map<String, Object> source) { 
        this.source = source;
        setProperty("source", source);
    }
    
    public String getRefreshScheduleId() { return refreshScheduleId; }
    public void setRefreshScheduleId(String refreshScheduleId) { 
        this.refreshScheduleId = refreshScheduleId;
        setProperty("refreshScheduleId", refreshScheduleId);
        
        // Add schedule reference as dependency
        if (refreshScheduleId != null) {
            addDependency(refreshScheduleId);
        }
    }
    
    public String getField() { return field; }
    public void setField(String field) { 
        this.field = field;
        setProperty("field", field);
    }
    
    /**
     * Check if this market data has multiple symbols.
     */
    public boolean hasMultipleSymbols() {
        return symbol instanceof List;
    }
    
    /**
     * Get the number of symbols this market data tracks.
     */
    public int getSymbolCount() {
        if (symbol == null) return 0;
        if (symbol instanceof List) {
            return ((List<?>) symbol).size();
        }
        return 1; // Single string symbol
    }
    
    /**
     * Check if this market data is for interest rates.
     */
    public boolean isInterestRate() {
        return "interest_rate".equals(dataType);
    }
    
    /**
     * Check if this market data is for index values.
     */
    public boolean isIndexValue() {
        return "index_value".equals(dataType);
    }
    
    /**
     * Check if this market data is for FX rates.
     */
    public boolean isFxRate() {
        return "fx_rate".equals(dataType);
    }
    
    /**
     * Check if this market data is for inflation indices.
     */
    public boolean isInflationIndex() {
        return "inflation_index".equals(dataType);
    }
    
    /**
     * Check if this market data is custom type.
     */
    public boolean isCustomData() {
        return "custom".equals(dataType);
    }
    
    /**
     * Get the source type (api, database, file, service).
     */
    public String getSourceType() {
        if (source == null) return null;
        return (String) source.get("type");
    }
    
    /**
     * Get the source endpoint.
     */
    public String getSourceEndpoint() {
        if (source == null) return null;
        return (String) source.get("endpoint");
    }
    
    /**
     * Check if source has credentials.
     */
    public boolean hasCredentials() {
        if (source == null) return false;
        return source.containsKey("credentialsRef") && source.get("credentialsRef") != null;
    }
    
    /**
     * Check if source has additional parameters.
     */
    public boolean hasSourceParameters() {
        if (source == null) return false;
        return source.containsKey("parameters") && source.get("parameters") != null;
    }
    
    /**
     * Check if market data has a refresh schedule.
     */
    public boolean hasRefreshSchedule() {
        return refreshScheduleId != null && !refreshScheduleId.trim().isEmpty();
    }
    
    /**
     * Check if market data has a specific field to extract.
     */
    public boolean hasField() {
        return field != null && !field.trim().isEmpty();
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("dataType", dataType);
        
        // Symbol (required)
        if (symbol != null) {
            json.set("symbol", objectMapper.valueToTree(symbol));
        }
        
        // Source (required)
        if (source != null) {
            json.set("source", objectMapper.valueToTree(source));
        }
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // Optional refresh schedule
        if (refreshScheduleId != null) {
            json.put("refreshSchedule", refreshScheduleId);
        }
        
        // Optional field
        if (field != null) {
            json.put("field", field);
        }
        
        // Add computed metadata for engine convenience
        json.put("hasMultipleSymbols", hasMultipleSymbols());
        json.put("symbolCount", getSymbolCount());
        json.put("isInterestRate", isInterestRate());
        json.put("isIndexValue", isIndexValue());
        json.put("isFxRate", isFxRate());
        json.put("isInflationIndex", isInflationIndex());
        json.put("isCustomData", isCustomData());
        json.put("sourceType", getSourceType());
        json.put("hasCredentials", hasCredentials());
        json.put("hasSourceParameters", hasSourceParameters());
        json.put("hasRefreshSchedule", hasRefreshSchedule());
        json.put("hasField", hasField());
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Market data id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Market data name is required");
        }
        
        if (dataType == null || dataType.trim().isEmpty()) {
            addValidationMessage("Market data dataType is required");
        }
        
        if (symbol == null) {
            addValidationMessage("Market data symbol is required");
        }
        
        if (source == null) {
            addValidationMessage("Market data source is required");
        }
        
        // Validate dataType against allowed values
        if (dataType != null) {
            String[] validDataTypes = {
                "interest_rate", "index_value", "fx_rate", "inflation_index", "custom"
            };
            boolean validDataType = false;
            for (String valid : validDataTypes) {
                if (valid.equals(dataType)) {
                    validDataType = true;
                    break;
                }
            }
            if (!validDataType) {
                addValidationMessage("Market data dataType must be one of: interest_rate, index_value, fx_rate, inflation_index, custom");
            }
        }
        
        // Validate symbol content
        if (symbol != null) {
            if (symbol instanceof String) {
                String symbolStr = (String) symbol;
                if (symbolStr.trim().isEmpty()) {
                    addValidationMessage("Market data symbol cannot be empty");
                }
            } else if (symbol instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> symbolList = (List<String>) symbol;
                if (symbolList.isEmpty()) {
                    addValidationMessage("Market data symbol list cannot be empty");
                }
                for (int i = 0; i < symbolList.size(); i++) {
                    String symbolItem = symbolList.get(i);
                    if (symbolItem == null || symbolItem.trim().isEmpty()) {
                        addValidationMessage("Market data symbol " + i + " cannot be null or empty");
                    }
                }
            } else {
                addValidationMessage("Market data symbol must be either a string or a list of strings");
            }
        }
        
        // Validate source object
        if (source != null) {
            if (!source.containsKey("type") || source.get("type") == null) {
                addValidationMessage("Market data source must have type");
            } else {
                String sourceType = (String) source.get("type");
                String[] validSourceTypes = {"api", "database", "file", "service"};
                boolean validSourceType = false;
                for (String valid : validSourceTypes) {
                    if (valid.equals(sourceType)) {
                        validSourceType = true;
                        break;
                    }
                }
                if (!validSourceType) {
                    addValidationMessage("Market data source type must be one of: api, database, file, service");
                }
            }
            
            if (!source.containsKey("endpoint") || source.get("endpoint") == null) {
                addValidationMessage("Market data source must have endpoint");
            } else {
                String endpoint = (String) source.get("endpoint");
                if (endpoint.trim().isEmpty()) {
                    addValidationMessage("Market data source endpoint cannot be empty");
                }
            }
        }
    }
}