package dev.cfdl;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * IR Builder - Transforms validated AST nodes into Intermediate Representation
 * 
 * The IR is ready for execution by the CFDL execution engine.
 */
public class IRBuilder {
    
    /**
     * Build IR from validated AST nodes
     */
    public IRBuildResult build(List<ASTNode> astNodes) {
        List<IRNode> irNodes = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        System.out.println("🔧 Building IR from " + astNodes.size() + " AST nodes...");
        
        // Transform each AST node to IR node
        for (ASTNode astNode : astNodes) {
            try {
                IRNode irNode = transformToIR(astNode);
                if (irNode != null) {
                    irNodes.add(irNode);
                }
            } catch (Exception e) {
                errors.add("Failed to transform " + astNode.getId() + " to IR: " + e.getMessage());
            }
        }
        
        // Validate relationships and dependencies
        validateIRRelationships(irNodes, errors);
        
        return new IRBuildResult(irNodes, errors);
    }
    
    /**
     * Transform a single AST node to IR node
     */
    private IRNode transformToIR(ASTNode astNode) {
        IRNode irNode = new IRNode(astNode.getId(), astNode.getName(), astNode.getSchemaType());
        
        // Copy all properties from AST to IR
        for (Map.Entry<String, Object> entry : astNode.getProperties().entrySet()) {
            irNode.setProperty(entry.getKey(), entry.getValue());
        }
        
        // Apply schema-specific transformations
        applySchemaSpecificTransformations(irNode);
        
        return irNode;
    }
    
    /**
     * Apply schema-specific transformations to optimize for execution
     */
    private void applySchemaSpecificTransformations(IRNode irNode) {
        String schemaType = irNode.getSchemaType();
        
        if (schemaType != null) {
            if (schemaType.contains("stream.schema.yaml")) {
                // Transform stream-specific properties
                transformStreamNode(irNode);
            } else if (schemaType.contains("deal.schema.yaml")) {
                // Transform deal-specific properties
                transformDealNode(irNode);
            } else if (schemaType.contains("asset.schema.yaml")) {
                // Transform asset-specific properties
                transformAssetNode(irNode);
            } else if (schemaType.contains("component.schema.yaml")) {
                // Transform component-specific properties
                transformComponentNode(irNode);
            }
            // Add more transformations as needed for other schema types
        }
    }
    
    /**
     * Transform stream nodes for execution optimization
     */
    private void transformStreamNode(IRNode streamNode) {
        // Ensure amount is properly typed
        Object amount = streamNode.getProperty("amount");
        if (amount instanceof String) {
            try {
                Double amountValue = Double.parseDouble((String) amount);
                streamNode.setProperty("amount", amountValue);
            } catch (NumberFormatException e) {
                // Keep as string if not numeric (might be a formula)
            }
        }
        
        // Add execution flags
        streamNode.setProperty("isExecutable", true);
        streamNode.setProperty("executionOrder", determineExecutionOrder(streamNode));
    }
    
    /**
     * Transform deal nodes
     */
    private void transformDealNode(IRNode dealNode) {
        // Add deal-level execution context
        dealNode.setProperty("isRootEntity", true);
        dealNode.setProperty("executionContext", "deal-level");
    }
    
    /**
     * Transform asset nodes
     */
    private void transformAssetNode(IRNode assetNode) {
        // Add asset-level execution context
        assetNode.setProperty("executionContext", "asset-level");
        
        // Validate dealId reference exists
        String dealId = assetNode.getStringProperty("dealId");
        if (dealId != null) {
            assetNode.setProperty("hasValidDealReference", true);
        }
    }
    
    /**
     * Transform component nodes
     */
    private void transformComponentNode(IRNode componentNode) {
        // Add component-level execution context
        componentNode.setProperty("executionContext", "component-level");
        
        // Validate assetId reference exists
        String assetId = componentNode.getStringProperty("assetId");
        if (assetId != null) {
            componentNode.setProperty("hasValidAssetReference", true);
        }
    }
    
    /**
     * Determine execution order for streams based on dependencies
     */
    private int determineExecutionOrder(IRNode streamNode) {
        String scope = streamNode.getStringProperty("scope");
        String category = streamNode.getStringProperty("category");
        
        // Simple execution order logic
        if ("deal".equals(scope)) {
            return 1; // Deal-level streams execute first
        } else if ("asset".equals(scope)) {
            return 2; // Asset-level streams execute second
        } else if ("component".equals(scope)) {
            return 3; // Component-level streams execute last
        }
        
        return 999; // Default order for unknown scopes
    }
    
    /**
     * Validate relationships and dependencies between IR nodes
     */
    private void validateIRRelationships(List<IRNode> irNodes, List<String> errors) {
        // Create lookup maps for validation
        java.util.Set<String> dealIds = new java.util.HashSet<>();
        java.util.Set<String> assetIds = new java.util.HashSet<>();
        java.util.Set<String> componentIds = new java.util.HashSet<>();
        
        // Collect all entity IDs
        for (IRNode node : irNodes) {
            String schemaType = node.getSchemaType();
            if (schemaType != null) {
                if (schemaType.contains("deal.schema.yaml")) {
                    dealIds.add(node.getId());
                } else if (schemaType.contains("asset.schema.yaml")) {
                    assetIds.add(node.getId());
                } else if (schemaType.contains("component.schema.yaml")) {
                    componentIds.add(node.getId());
                }
            }
        }
        
        // Validate references
        for (IRNode node : irNodes) {
            String schemaType = node.getSchemaType();
            if (schemaType != null) {
                if (schemaType.contains("asset.schema.yaml")) {
                    String dealId = node.getStringProperty("dealId");
                    if (dealId != null && !dealIds.contains(dealId)) {
                        errors.add("Asset '" + node.getId() + "' references non-existent deal: " + dealId);
                    }
                } else if (schemaType.contains("component.schema.yaml")) {
                    String assetId = node.getStringProperty("assetId");
                    if (assetId != null && !assetIds.contains(assetId)) {
                        errors.add("Component '" + node.getId() + "' references non-existent asset: " + assetId);
                    }
                }
            }
        }
        
        System.out.println("📊 IR Validation: " + irNodes.size() + " nodes, " + errors.size() + " errors");
    }
}