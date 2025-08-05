package dev.cfdl;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * YAML-Based AST Builder
 * 
 * Builds AST nodes from Jackson JsonNode tree using universal buildNode approach.
 */
public class ASTBuilder {
    
    private List<ASTNode> allNodes; // Collects all nodes including nested ones
    
    /**
     * Get all AST nodes created during the last build, including nested ones
     */
    public List<ASTNode> getAllNodes() {
        return allNodes != null ? allNodes : new ArrayList<>();
    }
    
    /**
     * Build an AST node from a JsonNode based on the definition type
     */
    public ASTNode buildASTNode(String definitionType, JsonNode definitionNode) {
        this.allNodes = new ArrayList<>(); // Initialize for each top-level build
        if (!definitionNode.isObject()) {
            return null;
        }
        
        // Get the entity identifier - it should be the first field name
        Iterator<String> fieldNames = definitionNode.fieldNames();
        if (!fieldNames.hasNext()) {
            return null;
        }
        
        String entityId = fieldNames.next();
        JsonNode entityData = definitionNode.get(entityId);
        
        // Build appropriate AST node based on definition type using YAML approach
        ASTNode mainNode = null;
        switch (definitionType.toLowerCase()) {
            case "deal":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/deal.schema.yaml");
                break;
            case "asset":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/asset.schema.yaml");
                break;
            case "component":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/component.schema.yaml");
                break;
            case "stream":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/behavior/stream.schema.yaml");
                break;
            // All other schema types use the universal buildNode method
            case "assumption":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/behavior/assumption.schema.yaml");
                break;
            case "logicblock":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/behavior/logic-block.schema.yaml");
                break;
            case "ruleblock":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/behavior/rule_block.schema.yaml");
                break;
            case "schedule":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/temporal/schedule.schema.yaml");
                break;
            case "eventtrigger":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/temporal/event_trigger.schema.yaml");
                break;
            case "template":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/template.schema.yaml");
                break;
            case "contract":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/contract.schema.yaml");
                break;
            case "party":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/party.schema.yaml");
                break;
            case "fund":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/fund.schema.yaml");
                break;
            case "portfolio":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/portfolio.schema.yaml");
                break;
            case "capitalstack":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/entity/capital-stack.schema.yaml");
                break;
            case "waterfall":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/waterfall.schema.yaml");
                break;
            case "marketdata":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/behavior/market-data.schema.yaml");
                break;
            case "recurrencerule":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/temporal/recurrence_rule.schema.yaml");  
                break;
            case "calculator":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/behavior/calculators.schema.yaml");
                break;
            case "cashflow":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/cash-flow.schema.yaml");
                break;
            case "tagdefinition":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/tag-definition.schema.yaml");
                break;
            case "dscr":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/metrics/dscr.schema.yaml");
                break;
            case "eirr":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/metrics/eirr.schema.yaml");
                break;
            case "enpv":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/metrics/enpv.schema.yaml");
                break;
            case "irr":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/metrics/irr.schema.yaml");
                break;
            case "moic":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/metrics/moic.schema.yaml");
                break;
            case "npv":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/metrics/npv.schema.yaml");
                break;
            case "payback":
                mainNode = buildNode(entityId, entityData, "https://cfdl.dev/ontology/result/metrics/payback.schema.yaml");
                break;
            default:
                System.out.println("Warning: Unknown definition type: " + definitionType);
                return null;
        }
        
        if (mainNode != null) {
            allNodes.add(mainNode);
        }
        
        return mainNode;
    }
    
    /**
     * Universal node builder for ALL schema types
     * Uses properties Map approach - no specific fields
     */
    private ASTNode buildNode(String id, JsonNode data, String schemaUrl) {
        String name = getStringValue(data, "name", id);
        ASTNode node = new ASTNode(id, name, schemaUrl);
        
        // Store ALL JSON properties in the properties Map
        data.fields().forEachRemaining(field -> {
            String key = field.getKey();
            JsonNode value = field.getValue();
            
            if (!key.equals("name")) { // name is already set
                if (value.isTextual()) {
                    node.setProperty(key, value.asText());
                } else if (value.isNumber()) {
                    node.setProperty(key, value.asDouble());
                } else if (value.isBoolean()) {
                    node.setProperty(key, value.asBoolean());
                } else if (value.isNull()) {
                    node.setProperty(key, null);
                } else {
                    // Complex objects (arrays, nested objects)
                    node.setProperty(key, convertJsonNodeToObject(value));
                }
            }
        });
        
        // Handle nested entities (assets, components, streams)
        handleNestedEntities(node, data);
        
        return node;
    }
    
    /**
     * Handle nested entity definitions within a parent node
     */
    private void handleNestedEntities(ASTNode parentNode, JsonNode data) {
        // Handle nested assets
        if (data.has("assets") && data.get("assets").isArray()) {
            List<String> assetIds = new ArrayList<>();
            for (JsonNode assetNode : data.get("assets")) {
                if (assetNode.isObject() && assetNode.has("asset")) {
                    JsonNode assetDef = assetNode.get("asset");
                    Iterator<String> fields = assetDef.fieldNames();
                    if (fields.hasNext()) {
                        String assetId = fields.next();
                        JsonNode assetData = assetDef.get(assetId);
                        ASTNode asset = buildNode(assetId, assetData, "https://cfdl.dev/ontology/entity/asset.schema.yaml");
                        allNodes.add(asset);
                        assetIds.add(assetId);
                    }
                } else if (assetNode.isTextual()) {
                    assetIds.add(assetNode.asText());
                }
            }
            parentNode.setProperty("assetIds", assetIds);
        }
        
        // Handle nested components
        if (data.has("components") && data.get("components").isArray()) {
            List<String> componentIds = new ArrayList<>();
            for (JsonNode componentNode : data.get("components")) {
                if (componentNode.isObject() && componentNode.has("component")) {
                    JsonNode componentDef = componentNode.get("component");
                    Iterator<String> fields = componentDef.fieldNames();
                    if (fields.hasNext()) {
                        String componentId = fields.next();
                        JsonNode componentData = componentDef.get(componentId);
                        ASTNode component = buildNode(componentId, componentData, "https://cfdl.dev/ontology/entity/component.schema.yaml");
                        allNodes.add(component);
                        componentIds.add(componentId);
                    }
                } else if (componentNode.isTextual()) {
                    componentIds.add(componentNode.asText());
                }
            }
            parentNode.setProperty("componentIds", componentIds);
        }
        
        // Handle nested streams  
        if (data.has("streams") && data.get("streams").isArray()) {
            List<String> streamIds = new ArrayList<>();
            for (JsonNode streamNode : data.get("streams")) {
                if (streamNode.isObject() && streamNode.has("stream")) {
                    JsonNode streamDef = streamNode.get("stream");
                    Iterator<String> fields = streamDef.fieldNames();  
                    if (fields.hasNext()) {
                        String streamId = fields.next();
                        JsonNode streamData = streamDef.get(streamId);
                        ASTNode stream = buildNode(streamId, streamData, "https://cfdl.dev/ontology/behavior/stream.schema.yaml");
                        allNodes.add(stream);
                        streamIds.add(streamId);
                    }
                } else if (streamNode.isTextual()) {
                    streamIds.add(streamNode.asText());
                }
            }
            parentNode.setProperty("streamIds", streamIds);
        }
    }
    
    // Helper methods
    private String getStringValue(JsonNode data, String fieldName, String defaultValue) {
        if (data.has(fieldName) && data.get(fieldName).isTextual()) {
            return data.get(fieldName).asText();
        }
        return defaultValue;
    }
    
    /**
     * Convert a JsonNode to a Java Object for storage in AST properties
     */
    private Object convertJsonNodeToObject(JsonNode node) {
        if (node.isNull()) {
            return null;
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isNumber()) {
            if (node.isInt()) {
                return node.asInt();
            } else if (node.isLong()) {
                return node.asLong();
            } else {
                return node.asDouble();
            }
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonNode item : node) {
                list.add(convertJsonNodeToObject(item));
            }
            return list;
        } else if (node.isObject()) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            Iterator<java.util.Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                java.util.Map.Entry<String, JsonNode> field = fields.next();
                map.put(field.getKey(), convertJsonNodeToObject(field.getValue()));
            }
            return map;
        }
        return node.toString();
    }
}