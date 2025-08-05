package dev.cfdl.ast;

/**
 * Represents a validation error found during AST schema validation.
 */
public class ValidationError {
    public enum Severity {
        ERROR, WARNING, INFO
    }
    
    private final Severity severity;
    private final String message;
    private final String schemaPath;
    private final String instancePath;
    private final int lineNumber;
    private final int columnNumber;
    
    public ValidationError(Severity severity, String message, String schemaPath, 
                          String instancePath, int lineNumber, int columnNumber) {
        this.severity = severity;
        this.message = message;
        this.schemaPath = schemaPath;
        this.instancePath = instancePath;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }
    
    public ValidationError(Severity severity, String message) {
        this(severity, message, null, null, -1, -1);
    }
    
    // Getters
    public Severity getSeverity() { return severity; }
    public String getMessage() { return message; }
    public String getSchemaPath() { return schemaPath; }
    public String getInstancePath() { return instancePath; }
    public int getLineNumber() { return lineNumber; }
    public int getColumnNumber() { return columnNumber; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(severity.name()).append(": ").append(message);
        
        if (lineNumber > 0) {
            sb.append(" (line ").append(lineNumber);
            if (columnNumber > 0) {
                sb.append(", column ").append(columnNumber);
            }
            sb.append(")");
        }
        
        if (schemaPath != null) {
            sb.append(" [schema: ").append(schemaPath).append("]");
        }
        
        if (instancePath != null) {
            sb.append(" [instance: ").append(instancePath).append("]");
        }
        
        return sb.toString();
    }
}