package dev.cfdl;

import java.util.List;
import java.util.ArrayList;

/**
 * Result of IR building process
 */
public class IRBuildResult {
    private final List<IRNode> irNodes;
    private final List<String> errors;
    
    public IRBuildResult(List<IRNode> irNodes, List<String> errors) {
        this.irNodes = irNodes != null ? irNodes : new ArrayList<>();
        this.errors = errors != null ? errors : new ArrayList<>();
    }
    
    public List<IRNode> getIrNodes() { return irNodes; }
    public List<String> getErrors() { return errors; }
    
    public boolean hasErrors() { return !errors.isEmpty(); }
    public boolean isSuccessful() { return errors.isEmpty(); }
    
    public void printResults() {
        if (hasErrors()) {
            System.out.println("❌ IR build errors found:");
            for (String error : errors) {
                System.out.println("IR Build Error: " + error);
            }
        } else {
            System.out.println("✅ Successfully built " + irNodes.size() + " IR nodes:");
            for (IRNode irNode : irNodes) {
                System.out.println("  • " + irNode.getClass().getSimpleName() + 
                    ": " + irNode.getId() + " (" + irNode.getName() + ")");
            }
        }
    }
}