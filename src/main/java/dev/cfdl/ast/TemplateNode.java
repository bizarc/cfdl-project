package dev.cfdl.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * AST node representing a CFDL template definition.
 * Corresponds to ontology/entity/template.schema.yaml
 * 
 * Templates define reusable CFDL DSL snippets with named parameters
 * for instantiating common constructs (e.g., standard leases, loans, waterfalls).
 */
public class TemplateNode extends ASTNode {
    private String description;
    private String templateType;
    private List<Map<String, Object>> parameters;
    private String body;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public TemplateNode() {
        super();
        this.parameters = new ArrayList<>();
    }
    
    public TemplateNode(String id, String name) {
        super(id, name);
        this.parameters = new ArrayList<>();
    }
    
    @Override
    public String getSchemaType() {
        return "https://cfdl.dev/ontology/entity/template.schema.yaml";
    }
    
    @Override
    public JsonNode toJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        if (id != null) json.put("id", id);
        if (name != null) json.put("name", name);
        if (description != null) json.put("description", description);
        if (templateType != null) json.put("templateType", templateType);
        if (body != null) json.put("body", body);
        
        // Parameters array
        if (!parameters.isEmpty()) {
            json.set("parameters", objectMapper.valueToTree(parameters));
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
    
    public String getTemplateType() { return templateType; }
    public void setTemplateType(String templateType) { this.templateType = templateType; }
    
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    
    public List<Map<String, Object>> getParameters() { return parameters; }
    public void setParameters(List<Map<String, Object>> parameters) { this.parameters = parameters; }
    
    public void addParameter(Map<String, Object> parameter) {
        this.parameters.add(parameter);
    }
}