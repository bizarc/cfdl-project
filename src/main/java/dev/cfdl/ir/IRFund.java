package dev.cfdl.ir;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Intermediate Representation for CFDL Fund entities.
 * 
 * Corresponds to ontology/entity/fund.schema.yaml and contains all
 * normalized fund properties plus schema metadata needed for execution.
 * 
 * Funds are collection vehicles that hold Portfolios (or Deals directly),
 * manage capital commitments, and apply fund-level streams with participant management.
 */
public class IRFund extends IRNode {
    private String description;
    private String fundType;
    private List<String> portfolioIds;
    private List<String> streamIds;
    private List<Map<String, Object>> participants;
    private Map<String, Object> stateConfig;
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public IRFund() {
        super();
        this.portfolioIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
        this.participants = new ArrayList<>();
        this.setSchemaType("https://cfdl.dev/ontology/entity/fund.schema.yaml");
    }
    
    public IRFund(String id, String name) {
        super(id, name, "https://cfdl.dev/ontology/entity/fund.schema.yaml");
        this.portfolioIds = new ArrayList<>();
        this.streamIds = new ArrayList<>();
        this.participants = new ArrayList<>();
    }
    
    // Getters and setters for fund-specific properties
    public String getDescription() { return description; }
    public void setDescription(String description) { 
        this.description = description;
        setProperty("description", description);
    }
    
    public String getFundType() { return fundType; }
    public void setFundType(String fundType) { 
        this.fundType = fundType;
        setProperty("fundType", fundType);
    }
    
    public List<String> getPortfolioIds() { return portfolioIds; }
    public void setPortfolioIds(List<String> portfolioIds) { 
        this.portfolioIds = portfolioIds;
        setProperty("portfolioIds", portfolioIds);
        
        // Add portfolio references as dependencies
        if (portfolioIds != null) {
            for (String portfolioId : portfolioIds) {
                addDependency(portfolioId);
            }
        }
    }
    
    public void addPortfolioId(String portfolioId) {
        if (this.portfolioIds == null) {
            this.portfolioIds = new ArrayList<>();
        }
        this.portfolioIds.add(portfolioId);
        addDependency(portfolioId);
        setProperty("portfolioIds", this.portfolioIds);
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
    
    public List<Map<String, Object>> getParticipants() { return participants; }
    public void setParticipants(List<Map<String, Object>> participants) { 
        this.participants = participants;
        setProperty("participants", participants);
        
        // Add party references as dependencies
        if (participants != null) {
            for (Map<String, Object> participant : participants) {
                String partyId = (String) participant.get("partyId");
                if (partyId != null) {
                    addDependency(partyId);
                }
            }
        }
    }
    
    public Map<String, Object> getStateConfig() { return stateConfig; }
    public void setStateConfig(Map<String, Object> stateConfig) { 
        this.stateConfig = stateConfig;
        setProperty("stateConfig", stateConfig);
    }
    
    /**
     * Get the number of portfolios in the fund.
     */
    public int getPortfolioCount() {
        return portfolioIds != null ? portfolioIds.size() : 0;
    }
    
    /**
     * Get the number of fund-level streams.
     */
    public int getStreamCount() {
        return streamIds != null ? streamIds.size() : 0;
    }
    
    /**
     * Get the number of participants in the fund.
     */
    public int getParticipantCount() {
        return participants != null ? participants.size() : 0;
    }
    
    /**
     * Calculate the total capital commitment from all participants.
     */
    public double getTotalCommitment() {
        if (participants == null) return 0.0;
        
        return participants.stream()
                .mapToDouble(p -> {
                    Object commitment = p.get("commitment");
                    if (commitment instanceof Number) {
                        return ((Number) commitment).doubleValue();
                    }
                    return 0.0;
                })
                .sum();
    }
    
    /**
     * Get the current state of the fund (if configured).
     */
    public String getCurrentState() {
        if (stateConfig != null) {
            return (String) stateConfig.get("initialState");
        }
        return null;
    }
    
    /**
     * Determine if the fund has general partners.
     */
    public boolean hasGeneralPartners() {
        if (participants == null) return false;
        
        return participants.stream()
                .anyMatch(p -> "general_partner".equals(p.get("role")));
    }
    
    /**
     * Determine if the fund has limited partners.
     */
    public boolean hasLimitedPartners() {
        if (participants == null) return false;
        
        return participants.stream()
                .anyMatch(p -> "limited_partner".equals(p.get("role")));
    }
    
    /**
     * Determine if the fund has fund-level revenue streams.
     */
    public boolean hasFundStreams() {
        return streamIds != null && !streamIds.isEmpty();
    }
    
    @Override
    public JsonNode toEngineJson() {
        ObjectNode json = objectMapper.createObjectNode();
        
        // Required fields for engine
        json.put("id", id);
        json.put("name", name);
        
        // State configuration is required
        if (stateConfig != null) {
            json.set("stateConfig", objectMapper.valueToTree(stateConfig));
        }
        
        // Optional description
        if (description != null) {
            json.put("description", description);
        }
        
        // Optional fund type
        if (fundType != null) {
            json.put("fundType", fundType);
        }
        
        // Portfolio references
        if (portfolioIds != null && !portfolioIds.isEmpty()) {
            json.set("portfolios", objectMapper.valueToTree(portfolioIds));
        }
        
        // Fund-level streams
        if (streamIds != null && !streamIds.isEmpty()) {
            json.set("streams", objectMapper.valueToTree(streamIds));
        }
        
        // Participants
        if (participants != null && !participants.isEmpty()) {
            json.set("participants", objectMapper.valueToTree(participants));
        }
        
        // Add computed metadata for engine convenience
        json.put("portfolioCount", getPortfolioCount());
        json.put("streamCount", getStreamCount());
        json.put("participantCount", getParticipantCount());
        json.put("totalCommitment", getTotalCommitment());
        json.put("hasGeneralPartners", hasGeneralPartners());
        json.put("hasLimitedPartners", hasLimitedPartners());
        json.put("hasFundStreams", hasFundStreams());
        
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
            addValidationMessage("Fund id is required");
        }
        
        if (name == null || name.trim().isEmpty()) {
            addValidationMessage("Fund name is required");
        }
        
        if (stateConfig == null || stateConfig.isEmpty()) {
            addValidationMessage("Fund stateConfig is required");
        }
        
        // Validate fund type if present
        if (fundType != null) {
            String[] validFundTypes = {
                "closed_end", "open_end", "private_equity", "real_estate", "hedge", "venture_capital", "other"
            };
            boolean validFundType = false;
            for (String valid : validFundTypes) {
                if (valid.equals(fundType)) {
                    validFundType = true;
                    break;
                }
            }
            if (!validFundType) {
                addValidationMessage("Fund fundType must be one of: closed_end, open_end, private_equity, real_estate, hedge, venture_capital, other");
            }
        }
        
        // Validate portfolio IDs are not null/empty
        if (portfolioIds != null) {
            for (int i = 0; i < portfolioIds.size(); i++) {
                String portfolioId = portfolioIds.get(i);
                if (portfolioId == null || portfolioId.trim().isEmpty()) {
                    addValidationMessage("Fund portfolio " + i + " id cannot be null or empty");
                }
            }
        }
        
        // Validate stream IDs are not null/empty
        if (streamIds != null) {
            for (int i = 0; i < streamIds.size(); i++) {
                String streamId = streamIds.get(i);
                if (streamId == null || streamId.trim().isEmpty()) {
                    addValidationMessage("Fund stream " + i + " id cannot be null or empty");
                }
            }
        }
        
        // Validate participants
        if (participants != null) {
            for (int i = 0; i < participants.size(); i++) {
                Map<String, Object> participant = participants.get(i);
                
                // Required: partyId, role, commitment
                if (!participant.containsKey("partyId") || participant.get("partyId") == null) {
                    addValidationMessage("Fund participant " + i + " must have partyId");
                }
                
                if (!participant.containsKey("role") || participant.get("role") == null) {
                    addValidationMessage("Fund participant " + i + " must have role");
                } else {
                    String role = (String) participant.get("role");
                    String[] validRoles = {"general_partner", "limited_partner", "advisor", "other"};
                    boolean validRole = false;
                    for (String valid : validRoles) {
                        if (valid.equals(role)) {
                            validRole = true;
                            break;
                        }
                    }
                    if (!validRole) {
                        addValidationMessage("Fund participant " + i + " role must be one of: general_partner, limited_partner, advisor, other");
                    }
                }
                
                if (!participant.containsKey("commitment") || participant.get("commitment") == null) {
                    addValidationMessage("Fund participant " + i + " must have commitment");
                } else {
                    Object commitment = participant.get("commitment");
                    if (!(commitment instanceof Number)) {
                        addValidationMessage("Fund participant " + i + " commitment must be a number");
                    } else {
                        double commitmentValue = ((Number) commitment).doubleValue();
                        if (commitmentValue < 0) {
                            addValidationMessage("Fund participant " + i + " commitment must be non-negative");
                        }
                    }
                }
            }
        }
        
        // Validate state configuration (always required for funds)
        if (stateConfig != null) {
            if (!stateConfig.containsKey("allowedStates") || stateConfig.get("allowedStates") == null) {
                addValidationMessage("Fund stateConfig must have allowedStates");
            }
            
            if (!stateConfig.containsKey("initialState") || stateConfig.get("initialState") == null) {
                addValidationMessage("Fund stateConfig must have initialState");
            }
            
            // Validate that initialState is in allowedStates
            @SuppressWarnings("unchecked")
            List<String> allowedStates = (List<String>) stateConfig.get("allowedStates");
            String initialState = (String) stateConfig.get("initialState");
            
            if (allowedStates != null && initialState != null) {
                if (!allowedStates.contains(initialState)) {
                    addValidationMessage("Fund initialState '" + initialState + "' must be in allowedStates");
                }
            }
            
            // Validate transition rules if present
            if (stateConfig.containsKey("transitionRules") && stateConfig.get("transitionRules") != null) {
                @SuppressWarnings("unchecked")
                Map<String, List<String>> transitionRules = (Map<String, List<String>>) stateConfig.get("transitionRules");
                
                if (allowedStates != null) {
                    for (String fromState : transitionRules.keySet()) {
                        if (!allowedStates.contains(fromState)) {
                            addValidationMessage("Fund transition rule from state '" + fromState + "' must be in allowedStates");
                        }
                        
                        List<String> toStates = transitionRules.get(fromState);
                        if (toStates != null) {
                            for (String toState : toStates) {
                                if (!allowedStates.contains(toState)) {
                                    addValidationMessage("Fund transition rule to state '" + toState + "' must be in allowedStates");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}