package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Waterfall entities.
 * 
 * Corresponds to ontology/result/waterfall.schema.yaml and contains all
 * normalized waterfall properties plus schema metadata needed for execution.
 * 
 * Waterfalls define ordered, tiered distribution of available cash with
 * complex logic for preferred returns, catch-up provisions, and promote structures.
 */
public class IRWaterfall extends IRNode {
    private String description;
    private List<Map<String, Object>> tiers;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRWaterfall() {
        super();
        this.tiers = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/result/waterfall.schema.yaml");
    }
    
    public IRWaterfall(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/result/waterfall.schema.yaml");
        this.tiers = new ArrayList<>();
    }
    
    // Getters and setters for waterfall-specific properties
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public List<Map<String, Object>> getTiers() { return tiers; }
    public void setTiers(List<Map<String, Object>> tiers) { 
        this.tiers = tiers;
        setProperty("tiers", tiers);
        
        // Extract dependencies from tier recipients
        if (tiers != null) {
            for (Map<String, Object> tier : tiers) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> distribute = (List<Map<String, Object>>) tier.get("distribute");
                if (distribute != null) {
                    for (Map<String, Object> dist : distribute) {
                        Object recipient = dist.get("recipient");
                        // If recipient is a string URI, add as dependency
                        if (recipient instanceof String && ((String) recipient).contains("party-")) {
                            addDependency((String) recipient);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Get the number of tiers in the waterfall.
     */
    public int getTierCount() {
        return tiers != null ? tiers.size() : 0;
    }
    
    /**
     * Determine if this waterfall has preferred return tiers.
     */
    public boolean hasPreferredReturns() {
        if (tiers == null) return false;
        
        return tiers.stream().anyMatch(tier -> tier.containsKey("prefRate"));
    }
    
    /**
     * Determine if this waterfall uses capital stack proportional distribution.
     */
    public boolean usesCapitalStackProportions() {
        if (tiers == null) return false;
        
        return tiers.stream().anyMatch(tier -> {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> distribute = (List<Map<String, Object>>) tier.get("distribute");
            if (distribute != null) {
                return distribute.stream().anyMatch(dist -> Boolean.TRUE.equals(dist.get("fromCapitalStack")));
            }
            return false;
        });
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        
        // Tiers are required
        if (tiers != null && !tiers.isEmpty()) {
            json.set("tiers", objectMapper.valueToTree(tiers));
        }
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // Add computed metadata for engine convenience
        json.put("tierCount", getTierCount());
        json.put("hasPreferredReturns", hasPreferredReturns());
        json.put("usesCapitalStackProportions", usesCapitalStackProportions());
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Waterfall id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Waterfall name is required");
        }
        
        if (tiers == null || tiers.isEmpty()) {
            addValidationMessage("Waterfall tiers are required");
        }
        
        // Validate tiers structure
        if (tiers != null) {
            for (int i = 0; i < tiers.size(); i++) {
                Map<String, Object> tier = tiers.get(i);
                
                // Each tier must have id and distribute
                if (!tier.containsKey("id") || tier.get("id") == null) {
                    addValidationMessage("Waterfall tier " + i + " must have id");
                }
                
                if (!tier.containsKey("distribute") || tier.get("distribute") == null) {
                    addValidationMessage("Waterfall tier " + i + " must have distribute");
                }
                
                // Each tier must have one of: condition, until, or prefRate
                boolean hasCondition = tier.containsKey("condition") && tier.get("condition") != null;
                boolean hasUntil = tier.containsKey("until") && tier.get("until") != null;
                boolean hasPrefRate = tier.containsKey("prefRate") && tier.get("prefRate") != null;
                
                if (!hasCondition && !hasUntil && !hasPrefRate) {
                    addValidationMessage("Waterfall tier " + i + " must have one of: condition, until, or prefRate");
                }
                
                // Validate distribute array
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> distribute = (List<Map<String, Object>>) tier.get("distribute");
                if (distribute != null) {
                    double totalPercentage = 0.0;
                    boolean hasCapitalStackDistribution = false;
                    
                    for (int j = 0; j < distribute.size(); j++) {
                        Map<String, Object> dist = distribute.get(j);
                        
                        if (Boolean.TRUE.equals(dist.get("fromCapitalStack"))) {
                            // Capital stack distribution
                            hasCapitalStackDistribution = true;
                            if (!dist.containsKey("layerName") || dist.get("layerName") == null) {
                                addValidationMessage("Waterfall tier " + i + " distribution " + j + " with fromCapitalStack must have layerName");
                            }
                        } else {
                            // Explicit distribution
                            if (!dist.containsKey("recipient") || dist.get("recipient") == null) {
                                addValidationMessage("Waterfall tier " + i + " distribution " + j + " must have recipient");
                            }
                            if (!dist.containsKey("percentage") || dist.get("percentage") == null) {
                                addValidationMessage("Waterfall tier " + i + " distribution " + j + " must have percentage");
                            } else {
                                Object percentageObj = dist.get("percentage");
                                if (percentageObj instanceof Number) {
                                    double percentage = ((Number) percentageObj).doubleValue();
                                    if (percentage < 0.0 || percentage > 1.0) {
                                        addValidationMessage("Waterfall tier " + i + " distribution " + j + " percentage must be between 0 and 1");
                                    }
                                    totalPercentage += percentage;
                                }
                            }
                        }
                    }
                    
                    // Validate that explicit percentages sum to ~1.0 (unless using capital stack distribution)
                    if (!hasCapitalStackDistribution && Math.abs(totalPercentage - 1.0) > 0.001) {
                        addValidationMessage("Waterfall tier " + i + " distribution percentages must sum to 1.0, got " + totalPercentage);
                    }
                }
            }
        }
    }
}