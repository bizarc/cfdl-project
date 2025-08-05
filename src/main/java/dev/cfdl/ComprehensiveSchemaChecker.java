package dev.cfdl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Comprehensive Schema Checker for simplified ASTNode approach
 * 
 * Validates that all required properties are present for each schema type.
 */
public class ComprehensiveSchemaChecker {

    public static class ValidationReport {
        private final List<String> missingProperties;
        private final List<String> validEntities;
        private final int totalEntities;
        private final int validEntitiesCount;

        public ValidationReport(List<String> missingProperties, List<String> validEntities, 
                              int totalEntities, int validEntitiesCount) {
            this.missingProperties = missingProperties;
            this.validEntities = validEntities;
            this.totalEntities = totalEntities;
            this.validEntitiesCount = validEntitiesCount;
        }

        public boolean hasErrors() {
            return !missingProperties.isEmpty();
        }

        public boolean isAllValid() {
            return missingProperties.isEmpty();
        }

        public void printReport() {
            System.out.println("\n================================================================================");
            System.out.println("📋 COMPREHENSIVE SCHEMA VALIDATION REPORT");
            System.out.println("================================================================================");
            System.out.println("📊 Summary: " + validEntitiesCount + "/" + totalEntities + " entities valid");

            if (!missingProperties.isEmpty()) {
                System.out.println("\n❌ MISSING REQUIRED PROPERTIES:");
                for (String missing : missingProperties) {
                    System.out.println("  " + missing);
                }
            }

            if (!validEntities.isEmpty()) {
                System.out.println("\n✅ ENTITIES CHECKED:");
                for (String valid : validEntities) {
                    System.out.println("  " + valid);
                }
            }
            System.out.println("================================================================================");
        }
    }

    // Define required properties for each schema type
    private static final Map<String, String[]> REQUIRED_PROPERTIES = new HashMap<>();
    
    static {
        // Entity schemas
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/deal.schema.yaml", 
            new String[]{"name", "dealType"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/asset.schema.yaml", 
            new String[]{"name", "category", "dealId"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/component.schema.yaml", 
            new String[]{"name", "assetId"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/party.schema.yaml", 
            new String[]{"name", "role"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/fund.schema.yaml", 
            new String[]{"name", "fundType"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/portfolio.schema.yaml", 
            new String[]{"name"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/capital-stack.schema.yaml", 
            new String[]{"name"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/contract.schema.yaml", 
            new String[]{"name", "contractType"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/entity/template.schema.yaml", 
            new String[]{"name", "templateType"});
            
        // Behavior schemas
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/behavior/stream.schema.yaml", 
            new String[]{"name", "scope", "category", "schedule", "amount"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/behavior/assumption.schema.yaml", 
            new String[]{"name", "value"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/behavior/logic-block.schema.yaml", 
            new String[]{"name", "logic"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/behavior/rule_block.schema.yaml", 
            new String[]{"name", "rules"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/behavior/market-data.schema.yaml", 
            new String[]{"name", "dataType"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/behavior/calculators.schema.yaml", 
            new String[]{"name", "calculatorType"});
            
        // Temporal schemas
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/temporal/schedule.schema.yaml", 
            new String[]{"name", "type"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/temporal/event_trigger.schema.yaml", 
            new String[]{"name", "eventType"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/temporal/recurrence_rule.schema.yaml", 
            new String[]{"freq"});
            
        // Result schemas
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/cash-flow.schema.yaml", 
            new String[]{"name", "amount"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/waterfall.schema.yaml", 
            new String[]{"name", "distributionOrder"});
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/tag-definition.schema.yaml", 
            new String[]{"name", "tagType"});
            
        // Metric schemas (all require name and value)
        String[] metricProps = new String[]{"name", "value"};
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/metrics/dscr.schema.yaml", metricProps);
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/metrics/eirr.schema.yaml", metricProps);
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/metrics/enpv.schema.yaml", metricProps);
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/metrics/irr.schema.yaml", metricProps);
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/metrics/moic.schema.yaml", metricProps);
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/metrics/npv.schema.yaml", metricProps);
        REQUIRED_PROPERTIES.put("https://cfdl.dev/ontology/result/metrics/payback.schema.yaml", metricProps);
    }

    /**
     * Validates that all required properties are present for each node
     */
    public ValidationReport validateNodes(List<ASTNode> nodes) {
        List<String> missingProperties = new ArrayList<>();
        List<String> validEntities = new ArrayList<>();
        int validCount = 0;

        for (ASTNode node : nodes) {
            boolean isValid = validateNode(node, missingProperties);
            
            String entityInfo = "✅ " + getSchemaTypeName(node.getSchemaType()) + " '" + node.getId() + "'";
            validEntities.add(entityInfo);
            
            if (isValid) {
                validCount++;
            }
        }

        return new ValidationReport(missingProperties, validEntities, nodes.size(), validCount);
    }

    /**
     * Validates a single node for required properties
     */
    private boolean validateNode(ASTNode node, List<String> missingProperties) {
        String schemaType = node.getSchemaType();
        String[] requiredProps = REQUIRED_PROPERTIES.get(schemaType);
        
        if (requiredProps == null) {
            // Schema type not recognized - treat as valid for now
            return true;
        }

        boolean isValid = true;
        String entityType = getSchemaTypeName(schemaType);
        
        for (String requiredProp : requiredProps) {
            Object propValue;
            
            // Check direct fields first (name, id)
            if ("name".equals(requiredProp)) {
                propValue = node.getName();
            } else if ("id".equals(requiredProp)) {
                propValue = node.getId();
            } else {
                // Check properties Map
                propValue = node.getProperty(requiredProp);
            }
            
            if (propValue == null || (propValue instanceof String && ((String) propValue).trim().isEmpty())) {
                missingProperties.add("❌ " + entityType + " '" + node.getId() + "' missing required property: " + requiredProp);
                isValid = false;
            }
        }
        
        return isValid;
    }

    /**
     * Extract readable schema type name from URL
     */
    private String getSchemaTypeName(String schemaUrl) {
        if (schemaUrl == null) return "Unknown";
        
        // Extract from URLs like "https://cfdl.dev/ontology/entity/deal.schema.yaml"
        String[] parts = schemaUrl.split("/");
        if (parts.length > 0) {
            String filename = parts[parts.length - 1];
            if (filename.endsWith(".schema.yaml")) {
                String name = filename.substring(0, filename.length() - ".schema.yaml".length());
                // Capitalize first letter
                return name.substring(0, 1).toUpperCase() + name.substring(1);
            }
        }
        return "Unknown";
    }
}