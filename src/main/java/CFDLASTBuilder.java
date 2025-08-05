import dev.cfdl.ast.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.List;
import java.util.ArrayList;

/**
 * ANTLR visitor that builds CFDL AST nodes from the parse tree.
 * Extends the generated cfdlBaseVisitor to override specific visit methods.
 */
public class CFDLASTBuilder extends cfdlBaseVisitor<ASTNode> {
    
    private final List<ASTNode> rootNodes = new ArrayList<>();
    
    /**
     * Returns all top-level AST nodes created during parsing.
     */
    public List<ASTNode> getRootNodes() {
        return rootNodes;
    }
    
    @Override
    public ASTNode visitSpecification(cfdlParser.SpecificationContext ctx) {
        // Visit all definitions and collect them
        for (cfdlParser.DefinitionContext defCtx : ctx.definition()) {
            ASTNode node = visit(defCtx);
            if (node != null) {
                rootNodes.add(node);
            }
        }
        return null; // Specification itself is not an AST node
    }
    
    @Override
    public ASTNode visitDealDefinition(cfdlParser.DealDefinitionContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        DealNode deal = new DealNode(id, id); // Use ID as name initially
        
        // Set source location
        deal.setLineNumber(ctx.start.getLine());
        deal.setColumnNumber(ctx.start.getCharPositionInLine());
        
        // Process all deal properties
        for (cfdlParser.DealPropertyContext propCtx : ctx.dealProperty()) {
            processDealProperty(deal, propCtx);
        }
        
        return deal;
    }
    
    @Override
    public ASTNode visitAssetDefinition(cfdlParser.AssetDefinitionContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        AssetNode asset = new AssetNode(id, id);
        
        // Set source location
        asset.setLineNumber(ctx.start.getLine());
        asset.setColumnNumber(ctx.start.getCharPositionInLine());
        
        // Process all asset properties
        for (cfdlParser.AssetPropertyContext propCtx : ctx.assetProperty()) {
            processAssetProperty(asset, propCtx);
        }
        
        return asset;
    }
    
    @Override
    public ASTNode visitStreamDefinition(cfdlParser.StreamDefinitionContext ctx) {
        String id = ctx.IDENTIFIER().getText();
        StreamNode stream = new StreamNode(id, id);
        
        // Set source location
        stream.setLineNumber(ctx.start.getLine());
        stream.setColumnNumber(ctx.start.getCharPositionInLine());
        
        // Process all stream properties
        for (cfdlParser.StreamPropertyContext propCtx : ctx.streamProperty()) {
            processStreamProperty(stream, propCtx);
        }
        
        return stream;
    }
    
    // Property processing methods
    private void processDealProperty(DealNode deal, cfdlParser.DealPropertyContext ctx) {
        if (ctx.getText().startsWith("name:")) {
            String name = extractStringValue(ctx);
            deal.setName(name);
        } else if (ctx.getText().startsWith("dealType:")) {
            String dealType = extractEnumValue(ctx);
            deal.setDealType(dealType);
        } else {
            // Handle other properties...
            processGenericProperty(deal, ctx.getText());
        }
    }
    
    private void processAssetProperty(AssetNode asset, cfdlParser.AssetPropertyContext ctx) {
        String text = ctx.getText();
        if (text.startsWith("name:")) {
            String name = extractStringValue(ctx);
            asset.setName(name);
        } else if (text.startsWith("category:")) {
            String category = extractEnumValue(ctx);
            asset.setCategory(category);
        } else {
            processGenericProperty(asset, text);
        }
    }
    
    private void processStreamProperty(StreamNode stream, cfdlParser.StreamPropertyContext ctx) {
        String text = ctx.getText();
        if (text.startsWith("name:")) {
            String name = extractStringValue(ctx);
            stream.setName(name);
        } else if (text.startsWith("scope:")) {
            String scope = extractEnumValue(ctx);
            stream.setScope(scope);
        } else {
            processGenericProperty(stream, text);
        }
    }
    
    // Utility methods for extracting values from parse tree
    private String extractStringValue(ParseTree ctx) {
        // Find STRING terminal and remove quotes
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                TerminalNode terminal = (TerminalNode) child;
                String text = terminal.getText();
                if (text.startsWith("\"") && text.endsWith("\"")) {
                    return text.substring(1, text.length() - 1);
                }
            }
        }
        return null;
    }
    
    private String extractEnumValue(ParseTree ctx) {
        // Find enum terminal and return without quotes
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                TerminalNode terminal = (TerminalNode) child;
                String text = terminal.getText();
                // Enum values should not have quotes
                if (!text.equals(":") && !text.equals(";")) {
                    return text;
                }
            }
        }
        return null;
    }
    
    private void processGenericProperty(ASTNode node, String propertyText) {
        // Handle generic property assignment
        if (propertyText.contains(":")) {
            String[] parts = propertyText.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].replace(";", "").trim();
                node.setProperty(key, value);
            }
        }
    }
}