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
        if (ctx.STRING() != null) {
            // Handle string properties like name, currency, etc.
            String value = ctx.STRING().getText();
            // Remove quotes
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            String propertyName = ctx.getChild(0).getText();
            if (propertyName.equals("name")) {
                deal.setName(value);
            } else if (propertyName.equals("currency")) {
                deal.setCurrency(value);
            } else if (propertyName.equals("entryDate")) {
                deal.setEntryDate(value);
            } else if (propertyName.equals("exitDate")) {
                deal.setExitDate(value);
            } else if (propertyName.equals("analysisStart")) {
                deal.setAnalysisStart(value);
            }
        } else if (ctx.dealTypeValue() != null) {
            // Handle dealType enum
            String dealType = ctx.dealTypeValue().getText();
            deal.setDealType(dealType);
        } else if (ctx.NUMBER() != null) {
            // Handle numeric properties
            String propertyName = ctx.getChild(0).getText();
            Double value = Double.parseDouble(ctx.NUMBER().getText());
            if (propertyName.equals("holdingPeriodYears")) {
                deal.setHoldingPeriodYears(value);
            }
        } else {
            // Handle other properties...
            processGenericProperty(deal, ctx.getText());
        }
    }
    
    private void processAssetProperty(AssetNode asset, cfdlParser.AssetPropertyContext ctx) {
        if (ctx.STRING() != null) {
            // Handle string properties
            String value = ctx.STRING().getText();
            // Remove quotes
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            String propertyName = ctx.getChild(0).getText();
            if (propertyName.equals("name")) {
                asset.setName(value);
            } else if (propertyName.equals("description")) {
                asset.setDescription(value);
            }
        } else if (ctx.assetCategoryValue() != null) {
            // Handle category enum
            String category = ctx.assetCategoryValue().getText();
            asset.setCategory(category);
        } else if (ctx.assetStateValue() != null) {
            // Handle state enum
            String state = ctx.assetStateValue().getText();
            asset.setState(state);
        } else {
            // Handle other properties using generic traversal
            String text = ctx.getText();
            if (text.startsWith("dealId:")) {
                // Extract identifier value after colon
                for (int i = 0; i < ctx.getChildCount(); i++) {
                    ParseTree child = ctx.getChild(i);
                    if (child instanceof TerminalNode) {
                        TerminalNode terminal = (TerminalNode) child;
                        String termText = terminal.getText();
                        if (!termText.equals("dealId") && !termText.equals(":") && !termText.equals(";")) {
                            asset.setDealId(termText);
                            break;
                        }
                    }
                }
            } else {
                processGenericProperty(asset, text);
            }
        }
    }
    
    private void processStreamProperty(StreamNode stream, cfdlParser.StreamPropertyContext ctx) {
        if (ctx.STRING() != null) {
            // Handle string properties
            String value = ctx.STRING().getText();
            // Remove quotes
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            String propertyName = ctx.getChild(0).getText();
            if (propertyName.equals("name")) {
                stream.setName(value);
            }
        } else if (ctx.componentScopeValue() != null) {
            // Handle scope enum
            String scope = ctx.componentScopeValue().getText();
            stream.setScope(scope);
        } else if (ctx.streamCategoryValue() != null) {
            // Handle category enum
            String category = ctx.streamCategoryValue().getText();
            stream.setCategory(category);
        } else if (ctx.streamSubTypeValue() != null) {
            // Handle subType enum
            String subType = ctx.streamSubTypeValue().getText();
            stream.setSubType(subType);
        } else if (ctx.amountTypeValue() != null) {
            // Handle amountType enum
            String amountType = ctx.amountTypeValue().getText();
            stream.setAmountType(amountType);
        } else if (ctx.NUMBER() != null) {
            // Handle numeric properties like amount
            Double amount = Double.parseDouble(ctx.NUMBER().getText());
            stream.setAmount(amount);
        } else {
            // Handle other properties using generic traversal
            String text = ctx.getText();
            if (text.startsWith("schedule:")) {
                // Extract identifier value after colon
                for (int i = 0; i < ctx.getChildCount(); i++) {
                    ParseTree child = ctx.getChild(i);
                    if (child instanceof TerminalNode) {
                        TerminalNode terminal = (TerminalNode) child;
                        String termText = terminal.getText();
                        if (!termText.equals("schedule") && !termText.equals(":") && !termText.equals(";")) {
                            stream.setScheduleId(termText);
                            break;
                        }
                    }
                }
            } else {
                processGenericProperty(stream, text);
            }
        }
    }
    
    // These utility methods are no longer needed since we're using the specific context methods
    // Removed extractStringValue and extractEnumValue methods
    
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