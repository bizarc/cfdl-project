package dev.cfdl.ir;

import java.util.List;
import java.util.ArrayList;

/**
 * Result object returned by IRBuilder containing IR nodes, validation errors, and build metadata.
 * 
 * Provides comprehensive information about the IR building process including:
 * - Successfully built IR nodes
 * - Validation errors and warnings
 * - Build success status
 * - Performance and debugging information
 */
public class IRBuildResult {
    private List<IRNode> irNodes;
    private List<String> errors;
    private List<String> warnings;
    private boolean buildSuccess;
    private long buildTimeMs;
    private int nodeCount;
    private int validationErrorCount;
    
    public IRBuildResult() {
        this.irNodes = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.buildSuccess = false;
        this.buildTimeMs = 0;
        this.nodeCount = 0;
        this.validationErrorCount = 0;
    }
    
    // Getters and setters
    public List<IRNode> getIrNodes() { return irNodes; }
    public void setIrNodes(List<IRNode> irNodes) { 
        this.irNodes = irNodes;
        this.nodeCount = irNodes != null ? irNodes.size() : 0;
    }
    
    public List<String> getErrors() { return errors; }
    public void addError(String error) { 
        this.errors.add(error);
        this.validationErrorCount++;
    }
    
    public List<String> getWarnings() { return warnings; }
    public void addWarning(String warning) { 
        this.warnings.add(warning);
    }
    
    public boolean isBuildSuccess() { return buildSuccess; }
    public void setBuildSuccess(boolean buildSuccess) { this.buildSuccess = buildSuccess; }
    
    public long getBuildTimeMs() { return buildTimeMs; }
    public void setBuildTimeMs(long buildTimeMs) { this.buildTimeMs = buildTimeMs; }
    
    public int getNodeCount() { return nodeCount; }
    public int getValidationErrorCount() { return validationErrorCount; }
    
    /**
     * Returns true if there are any errors or warnings.
     */
    public boolean hasIssues() {
        return !errors.isEmpty() || !warnings.isEmpty();
    }
    
    /**
     * Returns true if the build was successful and all nodes are valid.
     */
    public boolean isFullyValid() {
        if (!buildSuccess || !errors.isEmpty()) {
            return false;
        }
        
        for (IRNode node : irNodes) {
            if (!node.isValid()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Returns a summary of the build result.
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("IRBuildResult{");
        sb.append("success=").append(buildSuccess);
        sb.append(", nodes=").append(nodeCount);
        sb.append(", errors=").append(errors.size());
        sb.append(", warnings=").append(warnings.size());
        sb.append(", buildTime=").append(buildTimeMs).append("ms");
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Returns detailed information about the build result including all errors and warnings.
     */
    public String getDetailedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== IR Build Result ===\n");
        sb.append("Build Success: ").append(buildSuccess).append("\n");
        sb.append("Nodes Built: ").append(nodeCount).append("\n");
        sb.append("Build Time: ").append(buildTimeMs).append("ms\n");
        sb.append("\n");
        
        if (!errors.isEmpty()) {
            sb.append("ERRORS (").append(errors.size()).append("):\n");
            for (int i = 0; i < errors.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(errors.get(i)).append("\n");
            }
            sb.append("\n");
        }
        
        if (!warnings.isEmpty()) {
            sb.append("WARNINGS (").append(warnings.size()).append("):\n");
            for (int i = 0; i < warnings.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(warnings.get(i)).append("\n");
            }
            sb.append("\n");
        }
        
        if (!irNodes.isEmpty()) {
            sb.append("IR NODES (").append(irNodes.size()).append("):\n");
            for (IRNode node : irNodes) {
                sb.append("  - ").append(node.toString());
                if (!node.isValid()) {
                    sb.append(" [INVALID: ").append(node.getValidationMessages().size()).append(" errors]");
                }
                sb.append("\n");
            }
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return getSummary();
    }
}