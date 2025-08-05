package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;

/**
 * AST node representing a CFDL market data definition.
 * Corresponds to ontology/behavior/market-data.schema.yaml
 * 
 * Market data defines external market data feeds or reference indices that can be
 * used to populate assumptions, overwrite timeSeries values, or drive dynamic input
 * parameters in streams and logic blocks.
 */
public class MarketDataNode extends ASTNode {
    private String description;
    private String dataType;
    private Object symbol; // Can be String or List<String>
    private Map<String, Object> source;
    private String refreshScheduleId;
    private String field;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public MarketDataNode() {
        super();
    }
    
    public MarketDataNode(String id, String name) {
        super(id, name);
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/behavior/market-data.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (dataType != null) json.put("dataType", dataType);
        if (field != null) json.put("field", field);
        if (refreshScheduleId != null) json.put("refreshSchedule", refreshScheduleId);
        
        // Symbol can be string or array
        if (symbol != null) {
            json.set("symbol", objectMapper.valueToTree(symbol));
        }
        
        // Source object
        if (source != null) {
            json.set("source", objectMapper.valueToTree(source));
        }
        
        // Add any additional properties (metadata, etc.)
        properties.forEach((key, value) -> {
            if (!key.equals("lineNumber") && !key.equals("columnNumber")) {
                json.putPOJO(key, value);
            }
        });
        
        return json;
    }
    
    // Getters and setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    
    public Object getSymbol() { return symbol; }
    public void setSymbol(Object symbol) { this.symbol = symbol; }
    
    public Map<String, Object> getSource() { return source; }
    public void setSource(Map<String, Object> source) { this.source = source; }
    
    public String getRefreshScheduleId() { return refreshScheduleId; }
    public void setRefreshScheduleId(String refreshScheduleId) { this.refreshScheduleId = refreshScheduleId; }
    
    public String getField() { return field; }
    public void setField(String field) { this.field = field; }
}