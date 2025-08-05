package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Portfolio entities.
 * 
 * Corresponds to ontology/entity/portfolio.schema.yaml and contains all
 * normalized portfolio properties plus schema metadata needed for execution.
 * 
 * Portfolios are collections of Deals grouped for aggregated analysis,
 * reporting, and governance with portfolio-level streams and lifecycle management.
 */
public class IRPortfolio extends IRNode {
    private String description;
    private List<String> dealIds;
    private List<String> streamIds;
    private Map<String, Object> stateConfig;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRPortfolio() {
        super();
        this.dealIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/portfolio.schema.yaml");
    }
    
    public IRPortfolio(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/portfolio.schema.yaml");
        this.dealIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
    }
    
    // Getters and setters for portfolio-specific properties
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public List<String> getDealIds() { return dealIds; }
    public void setDealIds(List<String> dealIds) { 
        this.dealIds = dealIds;
        setProperty("dealIds", dealIds);
        
        // Add deal references as dependencies
        if (dealIds != null) {
            for (String dealId : dealIds) {
                addDependency(dealId);
            }
        }
    }
    
    public void addDealId(String dealId) {
        if (this.dealIds == null) {
            this.dealIds = new ArrayList<>();
        }
        this.dealIds.add(dealId);
        addDependency(dealId);
        setProperty("dealIds", this.dealIds);
    }
    
    public List<String> getStreamIds() { return streamIds; }
    public void setStreamIds(List<String> streamIds) { 
        this.streamIds = streamIds;
        setProperty("streamIds", streamIds);
        
        // Add stream references as dependencies
        if (streamIds != null) {
            for (String streamId : streamIds) {
                addDependency(streamId);
            }
        }
    }
    
    public void addStreamId(String streamId) {
        if (this.streamIds == null) {
            this.streamIds = new ArrayList<>();
        }
        this.streamIds.add(streamId);
        addDependency(streamId);
        setProperty("streamIds", this.streamIds);
    }
    
    public Map<String, Object> getStateConfig() { return stateConfig; }
    public void setStateConfig(Map<String, Object> stateConfig) { 
        this.stateConfig = stateConfig;
        setProperty("stateConfig", stateConfig);
    }
    
    /**
     * Get the number of deals in the portfolio.
     */
    public int getDealCount() {
        return dealIds != null ? dealIds.size() : 0;
    }
    
    /**
     * Get the number of portfolio-level streams.
     */
    public int getStreamCount() {
        return streamIds != null ? streamIds.size() : 0;
    }
    
    /**
     * Determine if this portfolio has lifecycle state management configured.
     */
    public boolean hasStateManagement() {
        return stateConfig != null && !stateConfig.isEmpty();
    }
    
    /**
     * Get the current state of the portfolio (if configured).
     */
    public String getCurrentState() {
        if (stateConfig != null) {
            return (String) stateConfig.get("initialState");
        }
        return null;
    }
    
    /**
     * Determine if the portfolio has portfolio-level revenue streams.
     */
    public boolean hasPortfolioStreams() {
        return streamIds != null && !streamIds.isEmpty();
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        
        // Deal references are required
        if (dealIds != null && !dealIds.isEmpty()) {
            json.set("deals", objectMapper.valueToTree(dealIds));
        }
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // Portfolio-level streams
        if (streamIds != null && !streamIds.isEmpty()) {
            json.set("streams", objectMapper.valueToTree(streamIds));
        }
        
        // State configuration
        if (stateConfig != null) {
            json.set("stateConfig", objectMapper.valueToTree(stateConfig));
        }
        
        // Add computed metadata for engine convenience
        json.put("dealCount", getDealCount());
        json.put("streamCount", getStreamCount());
        json.put("hasStateManagement", hasStateManagement());
        json.put("hasPortfolioStreams", hasPortfolioStreams());
        
        if (getCurrentState() != null) {
            json.put("currentState", getCurrentState());
        }
        
        return json;
    }
    
    /**
     * Validates that all required fields are present and valid.
     */
    public void validate() {
        if (id == null || id.trim().isEmpty()) {
            addValidationMessage("Portfolio id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Portfolio name is required");
        }
        
        if (dealIds == null || dealIds.isEmpty()) {
            addValidationMessage("Portfolio deals array is required and must not be empty");
        }
        
        // Validate deal IDs are not null/empty
        if (dealIds != null) {
            for (int i = 0; i < dealIds.size(); i++) {
                String dealId = dealIds.get(i);
                if (dealId == null || dealId.trim().isEmpty()) {
                    addValidationMessage("Portfolio deal " + i + " id cannot be null or empty");
                }
            }
        }
        
        // Validate stream IDs are not null/empty
        if (streamIds != null) {
            for (int i = 0; i < streamIds.size(); i++) {
                String streamId = streamIds.get(i);
                if (streamId == null || streamId.trim().isEmpty()) {
                    addValidationMessage("Portfolio stream " + i + " id cannot be null or empty");
                }
            }
        }
        
        // Validate state configuration if present
        if (stateConfig != null) {
            if (!stateConfig.containsKey("allowedStates") || stateConfig.get("allowedStates") == null) {
                addValidationMessage("Portfolio stateConfig must have allowedStates");
            }
            
            if (!stateConfig.containsKey("initialState") || stateConfig.get("initialState") == null) {
                addValidationMessage("Portfolio stateConfig must have initialState");
            }
            
            // Validate that initialState is in allowedStates
            @SuppressWarnings("unchecked")
            List<String> allowedStates = (List<String>) stateConfig.get("allowedStates");
            String initialState = (String) stateConfig.get("initialState");
            
            if (allowedStates != null && initialState != null) {
                if (!allowedStates.contains(initialState)) {
                    addValidationMessage("Portfolio initialState '" + initialState + "' must be in allowedStates");
                }
            }
            
            // Validate transition rules if present
            if (stateConfig.containsKey("transitionRules") && stateConfig.get("transitionRules") != null) {
                @SuppressWarnings("unchecked")
                Map<String, List<String>> transitionRules = (Map<String, List<String>>) stateConfig.get("transitionRules");
                
                if (allowedStates != null) {
                    for (String fromState : transitionRules.keySet()) {
                        if (!allowedStates.contains(fromState)) {
                            addValidationMessage("Portfolio transition rule from state '" + fromState + "' must be in allowedStates");
                        }
                        
                        List<String> toStates = transitionRules.get(fromState);
                        if (toStates != null) {
                            for (String toState : toStates) {
                                if (!allowedStates.contains(toState)) {
                                    addValidationMessage("Portfolio transition rule to state '" + toState + "' must be in allowedStates");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}