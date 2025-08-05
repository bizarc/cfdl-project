package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Template entities.
 * 
 * Corresponds to ontology/entity/template.schema.yaml and contains all
 * normalized template properties plus schema metadata needed for execution.
 * 
 * Templates define reusable CFDL DSL snippets with named parameters
 * for instantiating common constructs like standard leases, loans, or waterfalls.
 */
public class IRTemplate extends IRNode {
    private String description;
    private String templateType;
    private List<Map<String, Object>> parameters;
    private String body;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRTemplate() {
        super();
        this.parameters = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/template.schema.yaml");
    }
    
    public IRTemplate(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/template.schema.yaml");
        this.parameters = new ArrayList<>();
    }
    
    // Getters and setters for template-specific properties
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { 
        this.templateType = templateType;
        setProperty("templateType", templateType);
    }
    
    public String getBody() { return body; }
    public void setBody(String body) { 
        this.body = body;
        setProperty("body", body);
    }
    
    public List<Map<String, Object>> getParameters() { return parameters; }
    public void setParameters(List<Map<String, Object>> parameters) { 
        this.parameters = parameters;
        setProperty("parameters", parameters);
    }
    
    public void addParameter(Map<String, Object> parameter) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.add(parameter);
        setProperty("parameters", this.parameters);
    }
    
    /**
     * Get the number of parameters this template requires.
     */
    public int getParameterCount() {
        return parameters != null ? parameters.size() : 0;
    }
    
    /**
     * Get the number of required parameters.
     */
    public int getRequiredParameterCount() {
        if (parameters == null) return 0;
        return (int) parameters.stream()
            .filter(param -> Boolean.TRUE.equals(param.get("required")))
            .count();
    }
    
    /**
     * Get the number of optional parameters.
     */
    public int getOptionalParameterCount() {
        return getParameterCount() - getRequiredParameterCount();
    }
    
    /**
     * Check if template has any parameters.
     */
    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }
    
    /**
     * Check if template has any default values.
     */
    public boolean hasDefaultValues() {
        if (parameters == null) return false;
        return parameters.stream()
            .anyMatch(param -> param.containsKey("defaultValue"));
    }
    
    /**
     * Get all parameter names.
     */
    public List<String> getParameterNames() {
        if (parameters == null) return new ArrayList<>();
        return parameters.stream()
            .map(param -> (String) param.get("name"))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Check if template is for a specific entity type.
     */
    public boolean isForEntityType(String entityType) {
        return entityType != null && entityType.equals(templateType);
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        json.put("templateType", templateType);
        json.put("body", body);
        
        // Parameters array
        if (parameters != null && !parameters.isEmpty()) {
            json.set("parameters", objectMapper.valueToTree(parameters));
        }
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // Add computed metadata for engine convenience
        json.put("parameterCount", getParameterCount());
        json.put("requiredParameterCount", getRequiredParameterCount());
        json.put("optionalParameterCount", getOptionalParameterCount());
        json.put("hasParameters", hasParameters());
        json.put("hasDefaultValues", hasDefaultValues());
        json.set("parameterNames", objectMapper.valueToTree(getParameterNames()));
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Template id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Template name is required");
        }
        
        if (templateType == null || templateType.trim().isEmpty()) {
            addValidationMessage("Template templateType is required");
        }
        
        if (body == null || body.trim().isEmpty()) {
            addValidationMessage("Template body is required");
        }
        
        if (parameters == null || parameters.isEmpty()) {
            addValidationMessage("Template parameters are required");
        }
        
        // Validate templateType against allowed values
        if (templateType != null) {
            String[] validTypes = {
                "deal", "asset", "component", "stream", "logic-block", "rule-block", 
                "assumption", "view", "scenario", "contract", "waterfall"
            };
            boolean validType = false;
            for (String valid : validTypes) {
                if (valid.equals(templateType)) {
                    validType = true;
                    break;
                }
            }
            if (!validType) {
                addValidationMessage("Template templateType must be one of: deal, asset, component, stream, logic-block, rule-block, assumption, view, scenario, contract, waterfall");
            }
        }
        
        // Validate parameters
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                Map<String, Object> param = parameters.get(i);
                
                if (!param.containsKey("name") || param.get("name") == null) {
                    addValidationMessage("Template parameter " + i + " must have name");
                } else {
                    String paramName = (String) param.get("name");
                    if (paramName.trim().isEmpty()) {
                        addValidationMessage("Template parameter " + i + " name cannot be empty");
                    }
                }
                
                if (!param.containsKey("dataType") || param.get("dataType") == null) {
                    addValidationMessage("Template parameter " + i + " must have dataType");
                } else {
                    String dataType = (String) param.get("dataType");
                    String[] validDataTypes = {"string", "number", "date", "boolean", "enum"};
                    boolean validDataType = false;
                    for (String valid : validDataTypes) {
                        if (valid.equals(dataType)) {
                            validDataType = true;
                            break;
                        }
                    }
                    if (!validDataType) {
                        addValidationMessage("Template parameter " + i + " dataType must be one of: string, number, date, boolean, enum");
                    }
                }
            }
        }
    }
}