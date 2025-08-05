package dev.cfdl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CFDL Parser - Processes .cfdl files using YAML syntax
 */
public class Parser {
    
    public static class ParseResult {
        private final List<ASTNode> astNodes;
        private final List<ValidationError> validationErrors;
        
        public ParseResult(List<ASTNode> astNodes, List<ValidationError> validationErrors) {
            this.astNodes = astNodes;
            this.validationErrors = validationErrors;
        }
        
        public List<ASTNode> getAstNodes() { return astNodes; }
        public List<ValidationError> getValidationErrors() { return validationErrors; }
        
        public void printResults() {
            System.out.println("📄 STEP 1: CFDL → AST (Abstract Syntax Tree)");
            System.out.println("Parsed " + astNodes.size() + " CFDL definitions:");
            for (ASTNode node : astNodes) {
                System.out.println("  • " + node.getClass().getSimpleName() + 
                    ": " + node.getId() + " (" + node.getName() + ")");
            }
        }
        
        public void printErrors() {
            for (ValidationError error : validationErrors) {
                System.out.println("AST Validation Error: " + error.getMessage() + 
                    " (line " + error.getLineNumber() + ") [schema: " + error.getSchemaPath() + "]");
            }
        }
    }

    private final ObjectMapper yamlMapper;
    private final SchemaValidator schemaValidator;
    
    public Parser() {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.schemaValidator = new SchemaValidator();
    }
    
    /**
     * Parse a CFDL file using YAML parsing
     */
    public ParseResult parseFile(String filePath) throws IOException {
        System.out.println("CFDL v1.2 Demo - CFDL Pipeline Test");
        System.out.println("Processing: " + filePath);
        System.out.println("================================================================================");
        
        // Step 1: Parse CFDL file (YAML syntax) to JsonNode tree
        JsonNode rootNode = yamlMapper.readTree(new File(filePath));
        
        // Step 2: Build AST from JsonNode
        List<ASTNode> astNodes = new ArrayList<>();
        List<ValidationError> validationErrors = new ArrayList<>();
        
        ASTBuilder astBuilder = new ASTBuilder();
        
        // Process all top-level definitions
        Iterator<String> fieldNames = rootNode.fieldNames();
        while (fieldNames.hasNext()) {
            String definitionType = fieldNames.next();
            JsonNode definitionNode = rootNode.get(definitionType);
            
            // Handle both single definitions and arrays
            if (definitionNode.isArray()) {
                for (JsonNode item : definitionNode) {
                    processDefinition(definitionType, item, astBuilder, astNodes, validationErrors);
                }
            } else if (definitionNode.isObject()) {
                processDefinition(definitionType, definitionNode, astBuilder, astNodes, validationErrors);
            }
        }
        
        return new ParseResult(astNodes, validationErrors);
    }
    
    private void processDefinition(String definitionType, JsonNode definitionNode, 
                                 ASTBuilder astBuilder, List<ASTNode> astNodes, 
                                 List<ValidationError> validationErrors) {
        try {
            ASTNode astNode = astBuilder.buildASTNode(definitionType, definitionNode);
            if (astNode != null) {
                // Add the main node and all nested nodes
                astNodes.add(astNode);
                List<ASTNode> allNodes = astBuilder.getAllNodes();
                for (ASTNode node : allNodes) {
                    if (!astNodes.contains(node)) { // Avoid duplicates
                        astNodes.add(node);
                    }
                    
                    // Validate each node against schema
                    List<ValidationError> nodeErrors = schemaValidator.validateYamlNode(node.toJson(), node.getSchemaType());
                    validationErrors.addAll(nodeErrors);
                }
            }
        } catch (Exception e) {
            validationErrors.add(new ValidationError(
                ValidationError.Severity.ERROR,
                "Failed to build AST for " + definitionType + ": " + e.getMessage()
            ));
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java dev.cfdl.Parser <cfdl-file>");
            System.exit(1);
        }

        String filePath = args[0];
        Parser parser = new Parser();

        try {
            // Step 1: Parse CFDL file
            ParseResult result = parser.parseFile(filePath);
            result.printResults();

            // Step 2: Report Validation Results
            System.out.println("\n🔍 STEP 2: AST Schema Validation");
            if (result.getValidationErrors().isEmpty()) {
                System.out.println("✅ All AST nodes passed schema validation");
            } else {
                System.out.println("❌ Schema validation errors found:");
                result.printErrors();
            }

            // Step 2b: Comprehensive Required Properties Check
            System.out.println("\n🔍 STEP 2b: Comprehensive Required Properties Check");
            ComprehensiveSchemaChecker comprehensiveChecker = new ComprehensiveSchemaChecker();
            ComprehensiveSchemaChecker.ValidationReport comprehensiveReport = 
                comprehensiveChecker.validateNodes(result.getAstNodes());
            comprehensiveReport.printReport();

            if (comprehensiveReport.hasErrors()) {
                System.out.println("\n❌ Critical validation errors found. Cannot proceed to IR build.");
                parser.schemaValidator.shutdown();
                System.exit(1);
            }

            // Step 3: Build IR (Intermediate Representation)  
            System.out.println("\n🔧 STEP 3: AST → IR (Intermediate Representation)");
            IRBuilder irBuilder = new IRBuilder();
            IRBuildResult irResult = irBuilder.build(result.getAstNodes());

            if (irResult.hasErrors()) {
                System.out.println("❌ IR build errors found:");
                irResult.printResults();
                parser.schemaValidator.shutdown();
                System.exit(1);
            }

            // Success! Print final results
            System.out.println("\n🎉 COMPLETE CFDL Pipeline Success!");
            System.out.println("📊 Pipeline Summary:");
            System.out.println("  • Parsed " + result.getAstNodes().size() + " AST nodes");
            System.out.println("  • Built " + irResult.getIrNodes().size() + " IR nodes");
            System.out.println("  • All schemas validated successfully");
            
            System.out.println("\n📋 IR Nodes Ready for Execution:");
            for (IRNode irNode : irResult.getIrNodes()) {
                System.out.println("  • " + irNode.getClass().getSimpleName() + 
                    ": " + irNode.getId() + " (" + irNode.getName() + ")");
                System.out.println("    Schema: " + irNode.getSchemaType());
            }

            // Clean up resources and exit cleanly to prevent thread pool warnings
            parser.schemaValidator.shutdown();
            System.exit(0);

        } catch (IOException e) {
            System.err.println("❌ Error parsing CFDL file: " + e.getMessage());
            parser.schemaValidator.shutdown();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            parser.schemaValidator.shutdown();
            System.exit(1);
        }
    }
}