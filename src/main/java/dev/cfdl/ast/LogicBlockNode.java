package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * AST node representing a CFDL logic block definition.
 * Corresponds to ontology/behavior/logic-block.schema.yaml
 * 
 * Logic blocks encapsulate custom calculations, validations, triggers, or data-generation
 * steps during model execution. They contain executable code snippets that can
 * reference inputs and produce outputs or perform side effects.
 */
public class LogicBlockNode extends ASTNode {
    private String description;
    private String scope;
    private String type;
    private List<String> inputs;
    private List<String> outputs;
    private Integer executionOrder;
    private String code;
    private String language;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public LogicBlockNode() {
        super();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }
    
    public LogicBlockNode(String id, String name) {
        super(id, name);
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/behavior/logic-block.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (scope != null) json.put("scope", scope);
        if (type != null) json.put("type", type);
        if (code != null) json.put("code", code);
        if (language != null) json.put("language", language);
        if (executionOrder != null) json.put("executionOrder", executionOrder);
        
        // Input references
        if (!inputs.isEmpty()) {
            json.set("inputs", objectMapper.valueToTree(inputs));
        }
        
        // Output references
        if (!outputs.isEmpty()) {
            json.set("outputs", objectMapper.valueToTree(outputs));
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
    
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public Integer getExecutionOrder() { return executionOrder; }
    public void setExecutionOrder(Integer executionOrder) { this.executionOrder = executionOrder; }
    
    public List<String> getInputs() { return inputs; }
    public void setInputs(List<String> inputs) { this.inputs = inputs; }
    public void addInput(String input) { this.inputs.add(input); }
    
    public List<String> getOutputs() { return outputs; }
    public void setOutputs(List<String> outputs) { this.outputs = outputs; }
    public void addOutput(String output) { this.outputs.add(output); }
}