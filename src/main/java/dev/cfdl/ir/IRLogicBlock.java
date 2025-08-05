package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;

/**
 * Intermediate Representation for CFDL Logic Block entities.
 * 
 * Corresponds to ontology/behavior/logic-block.schema.yaml and contains all
 * normalized logic block properties plus schema metadata needed for execution.
 * 
 * Logic blocks encapsulate custom calculations, validations, triggers, or data-generation
 * steps with executable code that can reference inputs and produce outputs.
 */
public class IRLogicBlock extends IRNode {
    private String description;
    private String scope;
    private String type;
    private List<String> inputs;
    private List<String> outputs;
    private Integer executionOrder;
    private String code;
    private String language;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRLogicBlock() {
        super();
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/behavior/logic-block.schema.yaml");
    }
    
    public IRLogicBlock(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/behavior/logic-block.schema.yaml");
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
    }
    
    // Getters and setters for logic block-specific properties
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public String getScope() { return scope; }
    public void setScope(String scope) { 
        this.scope = scope;
        setProperty("scope", scope);
    }
    
    public String getType() { return type; }
    public void setType(String type) { 
        this.type = type;
        setProperty("type", type);
    }
    
    public String getCode() { return code; }
    public void setCode(String code) { 
        this.code = code;
        setProperty("code", code);
    }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { 
        this.language = language;
        setProperty("language", language);
    }
    
    public Integer getExecutionOrder() { return executionOrder; }
    public void setExecutionOrder(Integer executionOrder) { 
        this.executionOrder = executionOrder;
        setProperty("executionOrder", executionOrder);
    }
    
    public List<String> getInputs() { return inputs; }
    public void setInputs(List<String> inputs) { 
        this.inputs = inputs;
        setProperty("inputs", inputs);
        
        // Add input references as dependencies
        if (inputs != null) {
            for (String input : inputs) {
                addDependency(input);
            }
        }
    }
    
    public void addInput(String input) {
        if (this.inputs == null) {
            this.inputs = new ArrayList<>();
        }
        this.inputs.add(input);
        addDependency(input);
        setProperty("inputs", this.inputs);
    }
    
    public List<String> getOutputs() { return outputs; }
    public void setOutputs(List<String> outputs) { 
        this.outputs = outputs;
        setProperty("outputs", outputs);
        
        // Note: outputs are produced BY this block, so they're not dependencies
        // but consumers might depend on them
    }
    
    public void addOutput(String output) {
        if (this.outputs == null) {
            this.outputs = new ArrayList<>();
        }
        this.outputs.add(output);
        setProperty("outputs", this.outputs);
    }
    
    /**
     * Get the number of inputs this logic block consumes.
     */
    public int getInputCount() {
        return inputs != null ? inputs.size() : 0;
    }
    
    /**
     * Get the number of outputs this logic block produces.
     */
    public int getOutputCount() {
        return outputs != null ? outputs.size() : 0;
    }
    
    /**
     * Determine if this logic block has an execution order specified.
     */
    public boolean hasExecutionOrder() {
        return executionOrder != null;
    }
    
    /**
     * Determine if this logic block has a specified programming language.
     */
    public boolean hasLanguage() {
        return language != null && !language.trim().isEmpty();
    }
    
    /**
     * Determine if this logic block is a calculation type.
     */
    public boolean isCalculation() {
        return "calculation".equals(type);
    }
    
    /**
     * Determine if this logic block is a validation type.
     */
    public boolean isValidation() {
        return "validation".equals(type);
    }
    
    /**
     * Determine if this logic block is a trigger type.
     */
    public boolean isTrigger() {
        return "trigger".equals(type);
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("scope", scope);
        json.put("type", type);
        json.put("code", code);
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // Optional language
        if (language != null) {
            json.put("language", language);
        }
        
        // Optional execution order
        if (executionOrder != null) {
            json.put("executionOrder", executionOrder);
        }
        
        // Input references
        if (inputs != null && !inputs.isEmpty()) {
            json.set("inputs", objectMapper.valueToTree(inputs));
        }
        
        // Output references
        if (outputs != null && !outputs.isEmpty()) {
            json.set("outputs", objectMapper.valueToTree(outputs));
        }
        
        // Add computed metadata for engine convenience
        json.put("inputCount", getInputCount());
        json.put("outputCount", getOutputCount());
        json.put("hasExecutionOrder", hasExecutionOrder());
        json.put("hasLanguage", hasLanguage());
        json.put("isCalculation", isCalculation());
        json.put("isValidation", isValidation());
        json.put("isTrigger", isTrigger());
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Logic block id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Logic block name is required");
        }
        
        if (scope == null || scope.trim().isEmpty()) {
            addValidationMessage("Logic block scope is required");
        }
        
        if (type == null || type.trim().isEmpty()) {
            addValidationMessage("Logic block type is required");
        }
        
        if (code == null || code.trim().isEmpty()) {
            addValidationMessage("Logic block code is required");
        }
        
        // Validate scope against allowed values
        if (scope != null) {
            String[] validScopes = {
                "component", "asset", "deal", "portfolio", "fund"
            };
            boolean validScope = false;
            for (String valid : validScopes) {
                if (valid.equals(scope)) {
                    validScope = true;
                    break;
                }
            }
            if (!validScope) {
                addValidationMessage("Logic block scope must be one of: component, asset, deal, portfolio, fund");
            }
        }
        
        // Validate type against allowed values
        if (type != null) {
            String[] validTypes = {
                "calculation", "aggregation", "validation", "trigger", "generator", "custom"
            };
            boolean validType = false;
            for (String valid : validTypes) {
                if (valid.equals(type)) {
                    validType = true;
                    break;
                }
            }
            if (!validType) {
                addValidationMessage("Logic block type must be one of: calculation, aggregation, validation, trigger, generator, custom");
            }
        }
        
        // Validate inputs are not null/empty
        if (inputs != null) {
            for (int i = 0; i < inputs.size(); i++) {
                String input = inputs.get(i);
                if (input == null || input.trim().isEmpty()) {
                    addValidationMessage("Logic block input " + i + " cannot be null or empty");
                }
            }
        }
        
        // Validate outputs are not null/empty
        if (outputs != null) {
            for (int i = 0; i < outputs.size(); i++) {
                String output = outputs.get(i);
                if (output == null || output.trim().isEmpty()) {
                    addValidationMessage("Logic block output " + i + " cannot be null or empty");
                }
            }
        }
        
        // Validate execution order is non-negative if specified
        if (executionOrder != null && executionOrder < 0) {
            addValidationMessage("Logic block executionOrder must be non-negative");
        }
    }
}