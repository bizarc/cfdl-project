import dev.cfdl.ast.ASTNode;
import dev.cfdl.ast.ValidationError;
import dev.cfdl.validation.SchemaValidator;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Main CFDL parser that provides a high-level API for parsing and validating CFDL files.
 */
public class CFDLParserMain {
    
    private final SchemaValidator schemaValidator;
    
    public CFDLParserMain() {
        this.schemaValidator = new SchemaValidator();
    }
    
    /**
     * Parse result containing AST nodes and validation results.
     */
    public static class ParseResult {
        private final List<ASTNode> nodes;
        private final List<ValidationError> validationErrors;
        private final boolean isValid;
        
        public ParseResult(List<ASTNode> nodes, List<ValidationError> validationErrors) {
            this.nodes = nodes;
            this.validationErrors = validationErrors;
            this.isValid = validationErrors.stream()
                .noneMatch(error -> error.getSeverity() == ValidationError.Severity.ERROR);
        }
        
        public List<ASTNode> getNodes() { return nodes; }
        public List<ValidationError> getValidationErrors() { return validationErrors; }
        public boolean isValid() { return isValid; }
        
        public void printErrors() {
            for (ValidationError error : validationErrors) {
                System.err.println(error);
            }
        }
    }
    
    /**
     * Parses a CFDL string and returns AST nodes with validation results.
     */
    public ParseResult parse(String cfdlCode) throws IOException {
        // Create ANTLR input stream
        var input = CharStreams.fromString(cfdlCode);
        
        // Create lexer and parser
        cfdlLexer lexer = new cfdlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cfdlParser parser = new cfdlParser(tokens);
        
        // Parse the input
        ParseTree tree = parser.specification();
        
        // Build AST using our visitor
        CFDLASTBuilder astBuilder = new CFDLASTBuilder();
        astBuilder.visit(tree);
        List<ASTNode> nodes = astBuilder.getRootNodes();
        
        // Validate AST nodes against schemas
        List<ValidationError> validationErrors = schemaValidator.validateNodes(nodes);
        
        return new ParseResult(nodes, validationErrors);
    }
    
    /**
     * Parses a CFDL file from an input stream.
     */
    public ParseResult parse(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            content.append(new String(buffer, 0, bytesRead));
        }
        return parse(content.toString());
    }
    
    /**
     * Parses a CFDL file from the file system.
     */
    public ParseResult parseFile(String filePath) throws IOException {
        try (InputStream stream = new java.io.FileInputStream(filePath)) {
            return parse(stream);
        }
    }
    
    /**
     * Main method for command-line testing.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: CFDLParserMain <cfdl-file>");
            System.exit(1);
        }
        
        CFDLParserMain parser = new CFDLParserMain();
        try {
            ParseResult result = parser.parseFile(args[0]);
            
            System.out.println("Parsed " + result.getNodes().size() + " CFDL definitions:");
            for (ASTNode node : result.getNodes()) {
                System.out.println("  " + node);
            }
            
            if (result.getValidationErrors().isEmpty()) {
                System.out.println("✓ All definitions are valid");
            } else {
                System.err.println("✗ Validation errors found:");
                result.printErrors();
            }
            
            System.exit(result.isValid() ? 0 : 1);
            
        } catch (Exception e) {
            System.err.println("Parse error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}