package dev.cfdl.ir;

import dev.cfdl.ast.*;
import dev.cfdl.validation.SchemaValidator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Transforms CFDL AST nodes into normalized Intermediate Representation (IR).
 * 
 * The IRBuilder performs the following transformations:
 * 1. Converts AST nodes to IR nodes with normalized property names/values
 * 2. Enriches IR with schema metadata (types, validation rules, defaults)
 * 3. Resolves references and builds dependency graphs
 * 4. Validates the IR against JSON schemas
 * 5. Ensures the IR contains all data needed by the execution engine
 */
public class IRBuilder {
    private final SchemaValidator schemaValidator;
    private final Map<String, IRNode> nodeRegistry;
    private final Set<String> unresolvedReferences;
    
    public IRBuilder() {
        this.schemaValidator = new SchemaValidator();
        this.nodeRegistry = new HashMap<>();
        this.unresolvedReferences = new HashSet<>();
    }
    
    public IRBuilder(SchemaValidator schemaValidator) {
        this.schemaValidator = schemaValidator;
        this.nodeRegistry = new HashMap<>();
        this.unresolvedReferences = new HashSet<>();
    }
    
    /**
     * Transforms a list of AST nodes into IR nodes.
     * 
     * @param astNodes List of root AST nodes from the parser
     * @return IRBuildResult containing IR nodes, validation errors, and metadata
     */
    public IRBuildResult build(List<ASTNode> astNodes) {
        IRBuildResult result = new IRBuildResult();
        
        if (astNodes == null || astNodes.isEmpty()) {
            result.addError("No AST nodes provided for IR building");
            return result;
        }
        
        // Clear state from previous builds
        nodeRegistry.clear();
        unresolvedReferences.clear();
        
        // Phase 1: Convert AST nodes to IR nodes
        List<IRNode> irNodes = new ArrayList<>();
        for (ASTNode astNode : astNodes) {
            try {
                IRNode irNode = transformASTNode(astNode);
                if (irNode != null) {
                    irNodes.add(irNode);
                    nodeRegistry.put(irNode.getId(), irNode);
                }
            } catch (Exception e) {
                result.addError("Failed to transform AST node " + astNode.getId() + ": " + e.getMessage());
            }
        }
        
        // Phase 2: Enrich with schema metadata
        for (IRNode irNode : irNodes) {
            try {
                enrichWithSchemaMetadata(irNode);
            } catch (Exception e) {
                result.addWarning("Failed to enrich " + irNode.getId() + " with schema metadata: " + e.getMessage());
            }
        }
        
        // Phase 3: Resolve references and validate
        for (IRNode irNode : irNodes) {
            try {
                resolveReferences(irNode);
                validateIRNode(irNode, result);
            } catch (Exception e) {
                result.addError("Failed to validate IR node " + irNode.getId() + ": " + e.getMessage());
            }
        }
        
        // Phase 4: Check for unresolved references
        if (!unresolvedReferences.isEmpty()) {
            for (String ref : unresolvedReferences) {
                result.addWarning("Unresolved reference: " + ref);
            }
        }
        
        result.setIrNodes(irNodes);
        result.setBuildSuccess(result.getErrors().isEmpty());
        
        return result;
    }
    
    /**
     * Transforms a single AST node into an IR node.
     */
    private IRNode transformASTNode(ASTNode astNode) {
        if (astNode instanceof DealNode) {
            return transformDealNode((DealNode) astNode);
        } else if (astNode instanceof AssetNode) {
            return transformAssetNode((AssetNode) astNode);
        } else if (astNode instanceof ComponentNode) {
            return transformComponentNode((ComponentNode) astNode);
        } else if (astNode instanceof PartyNode) {
            return transformPartyNode((PartyNode) astNode);
        } else if (astNode instanceof ContractNode) {
            return transformContractNode((ContractNode) astNode);
        } else if (astNode instanceof CapitalStackNode) {
            return transformCapitalStackNode((CapitalStackNode) astNode);
        } else if (astNode instanceof AssumptionNode) {
            return transformAssumptionNode((AssumptionNode) astNode);
        } else if (astNode instanceof WaterfallNode) {
            return transformWaterfallNode((WaterfallNode) astNode);
        } else if (astNode instanceof PortfolioNode) {
            return transformPortfolioNode((PortfolioNode) astNode);
        } else if (astNode instanceof FundNode) {
            return transformFundNode((FundNode) astNode);
        } else if (astNode instanceof LogicBlockNode) {
            return transformLogicBlockNode((LogicBlockNode) astNode);
        } else if (astNode instanceof RuleBlockNode) {
            return transformRuleBlockNode((RuleBlockNode) astNode);
        } else if (astNode instanceof EventTriggerNode) {
            return transformEventTriggerNode((EventTriggerNode) astNode);
        } else if (astNode instanceof TemplateNode) {
            return transformTemplateNode((TemplateNode) astNode);
        } else if (astNode instanceof MarketDataNode) {
            return transformMarketDataNode((MarketDataNode) astNode);
        } else if (astNode instanceof StreamNode) {
            return transformStreamNode((StreamNode) astNode);
        } else {
            throw new IllegalArgumentException("Unsupported AST node type: " + astNode.getClass().getSimpleName());
        }
    }
    
    /**
     * Transforms a DealNode AST into an IRDeal.
     */
    private IRDeal transformDealNode(DealNode dealNode) {
        IRDeal irDeal = new IRDeal(dealNode.getId(), dealNode.getName());
        
        // Copy source location
        irDeal.setLineNumber(dealNode.getLineNumber());
        irDeal.setColumnNumber(dealNode.getColumnNumber());
        
        // Transform deal-specific properties
        irDeal.setDealType(dealNode.getDealType());
        irDeal.setCurrency(dealNode.getCurrency());
        irDeal.setEntryDate(dealNode.getEntryDate());
        irDeal.setExitDate(dealNode.getExitDate());
        irDeal.setAnalysisStart(dealNode.getAnalysisStart());
        if (dealNode.getHoldingPeriodYears() != null) {
            irDeal.setHoldingPeriodYears(dealNode.getHoldingPeriodYears().intValue());
        }
        
        // Transform complex properties
        if (dealNode.getProperty("calendar") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> calendar = (Map<String, Object>) dealNode.getProperty("calendar");
            irDeal.setCalendar(calendar);
        }
        
        if (dealNode.getProperty("participants") != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> participants = (List<Map<String, Object>>) dealNode.getProperty("participants");
            irDeal.setParticipants(participants);
        }
        
        if (dealNode.getProperty("stateConfig") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> stateConfig = (Map<String, Object>) dealNode.getProperty("stateConfig");
            irDeal.setStateConfig(stateConfig);
        }
        
        // Handle capital stack reference
        String capitalStackId = (String) dealNode.getProperty("capitalStackId");
        if (capitalStackId != null) {
            irDeal.setCapitalStackId(capitalStackId);
        }
        
        // Handle asset and stream references (stored as IDs in AST)
        if (dealNode.getAssetIds() != null) {
            for (String assetId : dealNode.getAssetIds()) {
                irDeal.addDependency(assetId);
            }
        }
        
        if (dealNode.getStreamIds() != null) {
            for (String streamId : dealNode.getStreamIds()) {
                irDeal.addDependency(streamId);
            }
        }
        
        return irDeal;
    }
    
    /**
     * Transforms an AssetNode AST into an IRAsset.
     */
    private IRAsset transformAssetNode(AssetNode assetNode) {
        IRAsset irAsset = new IRAsset(assetNode.getId(), assetNode.getName());
        
        // Copy source location
        irAsset.setLineNumber(assetNode.getLineNumber());
        irAsset.setColumnNumber(assetNode.getColumnNumber());
        
        // Transform asset-specific properties
        irAsset.setDealId(assetNode.getDealId());
        irAsset.setCategory(assetNode.getCategory());
        irAsset.setDescription(assetNode.getDescription());
        
        // Transform complex properties
        if (assetNode.getProperty("location") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> location = (Map<String, Object>) assetNode.getProperty("location");
            irAsset.setLocation(location);
        }
        
        if (assetNode.getProperty("attributes") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> attributes = (Map<String, Object>) assetNode.getProperty("attributes");
            irAsset.setAttributes(attributes);
        }
        
        if (assetNode.getProperty("stateConfig") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> stateConfig = (Map<String, Object>) assetNode.getProperty("stateConfig");
            irAsset.setStateConfig(stateConfig);
        }
        
        if (assetNode.getProperty("history") != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> history = (List<Map<String, Object>>) assetNode.getProperty("history");
            irAsset.setHistory(history);
        }
        
        // Handle references to contracts and components
        if (assetNode.getProperty("contractIds") != null) {
            @SuppressWarnings("unchecked")
            List<String> contractIds = (List<String>) assetNode.getProperty("contractIds");
            for (String contractId : contractIds) {
                irAsset.addContractId(contractId);
            }
        }
        
        if (assetNode.getProperty("componentIds") != null) {
            @SuppressWarnings("unchecked")
            List<String> componentIds = (List<String>) assetNode.getProperty("componentIds");
            for (String componentId : componentIds) {
                irAsset.addComponentId(componentId);
            }
        }
        
        // Handle stream references (stored as IDs in AST)
        if (assetNode.getStreamIds() != null) {
            for (String streamId : assetNode.getStreamIds()) {
                irAsset.addDependency(streamId);
            }
        }
        
        return irAsset;
    }
    
    /**
     * Transforms a ComponentNode AST into an IRComponent.
     */
    private IRComponent transformComponentNode(ComponentNode componentNode) {
        IRComponent irComponent = new IRComponent(componentNode.getId(), componentNode.getName());
        
        // Copy source location
        irComponent.setLineNumber(componentNode.getLineNumber());
        irComponent.setColumnNumber(componentNode.getColumnNumber());
        
        // Transform component-specific properties
        irComponent.setAssetId(componentNode.getAssetId());
        irComponent.setComponentType(componentNode.getComponentType());
        irComponent.setDescription(componentNode.getDescription());
        
        // Transform complex properties
        if (componentNode.getProperty("attributes") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> attributes = (Map<String, Object>) componentNode.getProperty("attributes");
            irComponent.setAttributes(attributes);
        }
        
        if (componentNode.getProperty("stateConfig") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> stateConfig = (Map<String, Object>) componentNode.getProperty("stateConfig");
            irComponent.setStateConfig(stateConfig);
        }
        
        // Handle stream references (stored as IDs in AST)
        if (componentNode.getStreamIds() != null) {
            for (String streamId : componentNode.getStreamIds()) {
                irComponent.addDependency(streamId);
            }
        }
        
        return irComponent;
    }
    
    /**
     * Transforms a PartyNode AST into an IRParty.
     */
    private IRParty transformPartyNode(PartyNode partyNode) {
        IRParty irParty = new IRParty(partyNode.getId(), partyNode.getName());
        
        // Copy source location
        irParty.setLineNumber(partyNode.getLineNumber());
        irParty.setColumnNumber(partyNode.getColumnNumber());
        
        // Transform party-specific properties
        irParty.setPartyType(partyNode.getPartyType());
        irParty.setDescription(partyNode.getDescription());
        
        // Transform complex properties
        if (partyNode.getProperty("contactInfo") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> contactInfo = (Map<String, Object>) partyNode.getProperty("contactInfo");
            irParty.setContactInfo(contactInfo);
        }
        
        return irParty;
    }
    
    /**
     * Transforms a ContractNode AST into an IRContract.
     */
    private IRContract transformContractNode(ContractNode contractNode) {
        IRContract irContract = new IRContract(contractNode.getId(), contractNode.getName());
        
        // Copy source location
        irContract.setLineNumber(contractNode.getLineNumber());
        irContract.setColumnNumber(contractNode.getColumnNumber());
        
        // Transform contract-specific properties
        irContract.setDealId(contractNode.getDealId());
        irContract.setAssetId(contractNode.getAssetId());
        irContract.setComponentId(contractNode.getComponentId());
        irContract.setContractType(contractNode.getContractType());
        irContract.setDescription(contractNode.getDescription());
        irContract.setStartDate(contractNode.getStartDate());
        irContract.setEndDate(contractNode.getEndDate());
        
        // Transform complex properties
        if (contractNode.getProperty("parties") != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> parties = (List<Map<String, Object>>) contractNode.getProperty("parties");
            irContract.setParties(parties);
        }
        
        if (contractNode.getProperty("terms") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> terms = (Map<String, Object>) contractNode.getProperty("terms");
            irContract.setTerms(terms);
        }
        
        if (contractNode.getProperty("stateConfig") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> stateConfig = (Map<String, Object>) contractNode.getProperty("stateConfig");
            irContract.setStateConfig(stateConfig);
        }
        
        // Handle stream references (stored as IDs in AST)
        if (contractNode.getStreamIds() != null) {
            for (String streamId : contractNode.getStreamIds()) {
                irContract.addDependency(streamId);
            }
        }
        
        return irContract;
    }
    
    /**
     * Transforms a CapitalStackNode AST into an IRCapitalStack.
     */
    private IRCapitalStack transformCapitalStackNode(CapitalStackNode capitalStackNode) {
        IRCapitalStack irCapitalStack = new IRCapitalStack(capitalStackNode.getId(), capitalStackNode.getName());
        
        // Copy source location
        irCapitalStack.setLineNumber(capitalStackNode.getLineNumber());
        irCapitalStack.setColumnNumber(capitalStackNode.getColumnNumber());
        
        // Transform capital stack-specific properties
        irCapitalStack.setWaterfallId(capitalStackNode.getWaterfallId());
        
        // Transform participants
        if (capitalStackNode.getProperty("participants") != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> participants = (List<Map<String, Object>>) capitalStackNode.getProperty("participants");
            irCapitalStack.setParticipants(participants);
        }
        
        return irCapitalStack;
    }
    
    /**
     * Transforms an AssumptionNode AST into an IRAssumption.
     */
    private IRAssumption transformAssumptionNode(AssumptionNode assumptionNode) {
        IRAssumption irAssumption = new IRAssumption(assumptionNode.getId(), assumptionNode.getName());
        
        // Copy source location
        irAssumption.setLineNumber(assumptionNode.getLineNumber());
        irAssumption.setColumnNumber(assumptionNode.getColumnNumber());
        
        // Transform assumption-specific properties
        irAssumption.setCategory(assumptionNode.getCategory());
        irAssumption.setScope(assumptionNode.getScope());
        irAssumption.setType(assumptionNode.getType());
        irAssumption.setUnit(assumptionNode.getUnit());
        irAssumption.setTemplate(assumptionNode.getTemplate());
        irAssumption.setSource(assumptionNode.getSource());
        irAssumption.setMarketDataRef(assumptionNode.getMarketDataRef());
        
        // Transform complex properties
        if (assumptionNode.getProperty("value") != null) {
            irAssumption.setValue(assumptionNode.getProperty("value"));
        }
        
        if (assumptionNode.getProperty("distribution") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> distribution = (Map<String, Object>) assumptionNode.getProperty("distribution");
            irAssumption.setDistribution(distribution);
        }
        
        if (assumptionNode.getProperty("timeSeries") != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> timeSeries = (List<Map<String, Object>>) assumptionNode.getProperty("timeSeries");
            irAssumption.setTimeSeries(timeSeries);
        }
        
        if (assumptionNode.getProperty("overrides") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> overrides = (Map<String, Object>) assumptionNode.getProperty("overrides");
            irAssumption.setOverrides(overrides);
        }
        
        return irAssumption;
    }
    
    /**
     * Transforms a WaterfallNode AST into an IRWaterfall.
     */
    private IRWaterfall transformWaterfallNode(WaterfallNode waterfallNode) {
        IRWaterfall irWaterfall = new IRWaterfall(waterfallNode.getId(), waterfallNode.getName());
        
        // Copy source location
        irWaterfall.setLineNumber(waterfallNode.getLineNumber());
        irWaterfall.setColumnNumber(waterfallNode.getColumnNumber());
        
        // Transform waterfall-specific properties
        irWaterfall.setDescription(waterfallNode.getDescription());
        
        // Transform tiers
        if (waterfallNode.getProperty("tiers") != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> tiers = (List<Map<String, Object>>) waterfallNode.getProperty("tiers");
            irWaterfall.setTiers(tiers);
        }
        
        return irWaterfall;
    }
    
    /**
     * Transforms a PortfolioNode AST into an IRPortfolio.
     */
    private IRPortfolio transformPortfolioNode(PortfolioNode portfolioNode) {
        IRPortfolio irPortfolio = new IRPortfolio(portfolioNode.getId(), portfolioNode.getName());
        
        // Copy source location
        irPortfolio.setLineNumber(portfolioNode.getLineNumber());
        irPortfolio.setColumnNumber(portfolioNode.getColumnNumber());
        
        // Transform portfolio-specific properties
        irPortfolio.setDescription(portfolioNode.getDescription());
        
        // Transform deal references
        if (portfolioNode.getDealIds() != null && !portfolioNode.getDealIds().isEmpty()) {
            irPortfolio.setDealIds(portfolioNode.getDealIds());
        }
        
        // Transform stream references
        if (portfolioNode.getStreamIds() != null && !portfolioNode.getStreamIds().isEmpty()) {
            irPortfolio.setStreamIds(portfolioNode.getStreamIds());
        }
        
        // Transform state configuration
        if (portfolioNode.getProperty("stateConfig") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> stateConfig = (Map<String, Object>) portfolioNode.getProperty("stateConfig");
            irPortfolio.setStateConfig(stateConfig);
        }
        
        return irPortfolio;
    }
    
    /**
     * Transforms a FundNode AST into an IRFund.
     */
    private IRFund transformFundNode(FundNode fundNode) {
        IRFund irFund = new IRFund(fundNode.getId(), fundNode.getName());
        
        // Copy source location
        irFund.setLineNumber(fundNode.getLineNumber());
        irFund.setColumnNumber(fundNode.getColumnNumber());
        
        // Transform fund-specific properties
        irFund.setDescription(fundNode.getDescription());
        irFund.setFundType(fundNode.getFundType());
        
        // Transform portfolio references
        if (fundNode.getPortfolioIds() != null && !fundNode.getPortfolioIds().isEmpty()) {
            irFund.setPortfolioIds(fundNode.getPortfolioIds());
        }
        
        // Transform stream references
        if (fundNode.getStreamIds() != null && !fundNode.getStreamIds().isEmpty()) {
            irFund.setStreamIds(fundNode.getStreamIds());
        }
        
        // Transform participants
        if (fundNode.getProperty("participants") != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> participants = (List<Map<String, Object>>) fundNode.getProperty("participants");
            irFund.setParticipants(participants);
        }
        
        // Transform state configuration (required for funds)
        if (fundNode.getProperty("stateConfig") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> stateConfig = (Map<String, Object>) fundNode.getProperty("stateConfig");
            irFund.setStateConfig(stateConfig);
        }
        
        return irFund;
    }
    
    /**
     * Transforms a LogicBlockNode AST into an IRLogicBlock.
     */
    private IRLogicBlock transformLogicBlockNode(LogicBlockNode logicBlockNode) {
        IRLogicBlock irLogicBlock = new IRLogicBlock(logicBlockNode.getId(), logicBlockNode.getName());
        
        // Copy source location
        irLogicBlock.setLineNumber(logicBlockNode.getLineNumber());
        irLogicBlock.setColumnNumber(logicBlockNode.getColumnNumber());
        
        // Transform logic block-specific properties
        irLogicBlock.setDescription(logicBlockNode.getDescription());
        irLogicBlock.setScope(logicBlockNode.getScope());
        irLogicBlock.setType(logicBlockNode.getType());
        irLogicBlock.setCode(logicBlockNode.getCode());
        irLogicBlock.setLanguage(logicBlockNode.getLanguage());
        irLogicBlock.setExecutionOrder(logicBlockNode.getExecutionOrder());
        
        // Transform input references
        if (logicBlockNode.getInputs() != null && !logicBlockNode.getInputs().isEmpty()) {
            irLogicBlock.setInputs(logicBlockNode.getInputs());
        }
        
        // Transform output references
        if (logicBlockNode.getOutputs() != null && !logicBlockNode.getOutputs().isEmpty()) {
            irLogicBlock.setOutputs(logicBlockNode.getOutputs());
        }
        
        return irLogicBlock;
    }
    
    /**
     * Transforms a RuleBlockNode AST into an IRRuleBlock.
     */
    private IRRuleBlock transformRuleBlockNode(RuleBlockNode ruleBlockNode) {
        IRRuleBlock irRuleBlock = new IRRuleBlock(ruleBlockNode.getId(), ruleBlockNode.getName());
        
        // Copy source location
        irRuleBlock.setLineNumber(ruleBlockNode.getLineNumber());
        irRuleBlock.setColumnNumber(ruleBlockNode.getColumnNumber());
        
        // Transform rule block-specific properties
        irRuleBlock.setDescription(ruleBlockNode.getDescription());
        irRuleBlock.setScope(ruleBlockNode.getScope());
        irRuleBlock.setScheduleId(ruleBlockNode.getScheduleId());
        irRuleBlock.setEventTriggerId(ruleBlockNode.getEventTriggerId());
        irRuleBlock.setCondition(ruleBlockNode.getCondition());
        irRuleBlock.setAction(ruleBlockNode.getAction());
        
        return irRuleBlock;
    }
    
    /**
     * Transforms an EventTriggerNode AST into an IREventTrigger.
     */
    private IREventTrigger transformEventTriggerNode(EventTriggerNode eventTriggerNode) {
        IREventTrigger irEventTrigger = new IREventTrigger(eventTriggerNode.getId(), eventTriggerNode.getName());
        
        // Copy source location
        irEventTrigger.setLineNumber(eventTriggerNode.getLineNumber());
        irEventTrigger.setColumnNumber(eventTriggerNode.getColumnNumber());
        
        // Transform event trigger-specific properties
        irEventTrigger.setType(eventTriggerNode.getType());
        irEventTrigger.setAssumptionId(eventTriggerNode.getAssumptionId());
        irEventTrigger.setStreamId(eventTriggerNode.getStreamId());
        irEventTrigger.setOperator(eventTriggerNode.getOperator());
        irEventTrigger.setThreshold(eventTriggerNode.getThreshold());
        irEventTrigger.setExternalEventName(eventTriggerNode.getExternalEventName());
        irEventTrigger.setExpression(eventTriggerNode.getExpression());
        
        return irEventTrigger;
    }
    
    /**
     * Transforms a TemplateNode AST into an IRTemplate.
     */
    private IRTemplate transformTemplateNode(TemplateNode templateNode) {
        IRTemplate irTemplate = new IRTemplate(templateNode.getId(), templateNode.getName());
        
        // Copy source location
        irTemplate.setLineNumber(templateNode.getLineNumber());
        irTemplate.setColumnNumber(templateNode.getColumnNumber());
        
        // Transform template-specific properties
        irTemplate.setDescription(templateNode.getDescription());
        irTemplate.setTemplateType(templateNode.getTemplateType());
        irTemplate.setBody(templateNode.getBody());
        
        // Transform parameters
        if (templateNode.getParameters() != null && !templateNode.getParameters().isEmpty()) {
            irTemplate.setParameters(templateNode.getParameters());
        }
        
        return irTemplate;
    }
    
    /**
     * Transforms a MarketDataNode AST into an IRMarketData.
     */
    private IRMarketData transformMarketDataNode(MarketDataNode marketDataNode) {
        IRMarketData irMarketData = new IRMarketData(marketDataNode.getId(), marketDataNode.getName());
        
        // Copy source location
        irMarketData.setLineNumber(marketDataNode.getLineNumber());
        irMarketData.setColumnNumber(marketDataNode.getColumnNumber());
        
        // Transform market data-specific properties
        irMarketData.setDescription(marketDataNode.getDescription());
        irMarketData.setDataType(marketDataNode.getDataType());
        irMarketData.setSymbol(marketDataNode.getSymbol());
        irMarketData.setSource(marketDataNode.getSource());
        irMarketData.setRefreshScheduleId(marketDataNode.getRefreshScheduleId());
        irMarketData.setField(marketDataNode.getField());
        
        return irMarketData;
    }
    
    /**
     * Transforms a StreamNode AST into an IRStream.
     */
    private IRStream transformStreamNode(StreamNode streamNode) {
        IRStream irStream = new IRStream(streamNode.getId(), streamNode.getName());
        
        // Copy source location
        irStream.setLineNumber(streamNode.getLineNumber());
        irStream.setColumnNumber(streamNode.getColumnNumber());
        
        // Transform stream-specific properties
        irStream.setDescription(streamNode.getDescription());
        irStream.setScope(streamNode.getScope());
        irStream.setCategory(streamNode.getCategory());
        irStream.setSubType(streamNode.getSubType());
        irStream.setAmount(streamNode.getAmount());
        
        // Transform complex properties
        if (streamNode.getProperty("schedule") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> schedule = (Map<String, Object>) streamNode.getProperty("schedule");
            irStream.setSchedule(schedule);
        }
        
        if (streamNode.getProperty("growth") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> growth = (Map<String, Object>) streamNode.getProperty("growth");
            irStream.setGrowth(growth);
        }
        
        if (streamNode.getProperty("tags") != null) {
            @SuppressWarnings("unchecked")
            List<String> tags = (List<String>) streamNode.getProperty("tags");
            irStream.setTags(tags);
        }
        
        return irStream;
    }
    
    /**
     * Enriches an IR node with schema metadata.
     */
    private void enrichWithSchemaMetadata(IRNode irNode) {
        // Add schema type information
        irNode.setSchemaMetadata("schemaType", irNode.getSchemaType());
        
        // Add validation rules based on schema type
        if (irNode instanceof IRDeal) {
            enrichDealMetadata((IRDeal) irNode);
        } else if (irNode instanceof IRAsset) {
            enrichAssetMetadata((IRAsset) irNode);
        } else if (irNode instanceof IRComponent) {
            enrichComponentMetadata((IRComponent) irNode);
        } else if (irNode instanceof IRParty) {
            enrichPartyMetadata((IRParty) irNode);
        } else if (irNode instanceof IRContract) {
            enrichContractMetadata((IRContract) irNode);
        } else if (irNode instanceof IRCapitalStack) {
            enrichCapitalStackMetadata((IRCapitalStack) irNode);
        } else if (irNode instanceof IRAssumption) {
            enrichAssumptionMetadata((IRAssumption) irNode);
        } else if (irNode instanceof IRWaterfall) {
            enrichWaterfallMetadata((IRWaterfall) irNode);
        } else if (irNode instanceof IRPortfolio) {
            enrichPortfolioMetadata((IRPortfolio) irNode);
        } else if (irNode instanceof IRFund) {
            enrichFundMetadata((IRFund) irNode);
        } else if (irNode instanceof IRLogicBlock) {
            enrichLogicBlockMetadata((IRLogicBlock) irNode);
        } else if (irNode instanceof IRRuleBlock) {
            enrichRuleBlockMetadata((IRRuleBlock) irNode);
        } else if (irNode instanceof IREventTrigger) {
            enrichEventTriggerMetadata((IREventTrigger) irNode);
        } else if (irNode instanceof IRTemplate) {
            enrichTemplateMetadata((IRTemplate) irNode);
        } else if (irNode instanceof IRMarketData) {
            enrichMarketDataMetadata((IRMarketData) irNode);
        } else if (irNode instanceof IRStream) {
            enrichStreamMetadata((IRStream) irNode);
        }
        
        // Add common metadata
        irNode.setSchemaMetadata("required", getRequiredFields(irNode));
        irNode.setSchemaMetadata("optional", getOptionalFields(irNode));
        irNode.setSchemaMetadata("enrichmentTimestamp", System.currentTimeMillis());
    }
    
    private void enrichDealMetadata(IRDeal deal) {
        deal.setSchemaMetadata("entityType", "deal");
        deal.setSchemaMetadata("hasAssets", !deal.getAssets().isEmpty());
        deal.setSchemaMetadata("hasStreams", !deal.getStreams().isEmpty());
        deal.setSchemaMetadata("participantCount", deal.getParticipants() != null ? deal.getParticipants().size() : 0);
    }
    
    private void enrichAssetMetadata(IRAsset asset) {
        asset.setSchemaMetadata("entityType", "asset");
        asset.setSchemaMetadata("hasStreams", !asset.getStreams().isEmpty());
        asset.setSchemaMetadata("hasLocation", asset.getLocation() != null);
        asset.setSchemaMetadata("attributeCount", asset.getAttributes() != null ? asset.getAttributes().size() : 0);
    }
    
    private void enrichComponentMetadata(IRComponent component) {
        component.setSchemaMetadata("entityType", "component");
        component.setSchemaMetadata("hasStreams", !component.getStreams().isEmpty());
        component.setSchemaMetadata("attributeCount", component.getAttributes() != null ? component.getAttributes().size() : 0);
        component.setSchemaMetadata("atomicLevel", true); // Components are the atomic level for cash flows
    }
    
    private void enrichPartyMetadata(IRParty party) {
        party.setSchemaMetadata("entityType", "party");
        party.setSchemaMetadata("hasContactInfo", party.getContactInfo() != null);
        party.setSchemaMetadata("partyClassification", party.getPartyType());
    }
    
    private void enrichContractMetadata(IRContract contract) {
        contract.setSchemaMetadata("entityType", "contract");
        contract.setSchemaMetadata("contractClassification", contract.getContractType());
        contract.setSchemaMetadata("hasStreams", !contract.getStreams().isEmpty());
        contract.setSchemaMetadata("partyCount", contract.getParties() != null ? contract.getParties().size() : 0);
        contract.setSchemaMetadata("hasTerms", contract.getTerms() != null);
        contract.setSchemaMetadata("isAssetLevel", contract.getAssetId() != null);
        contract.setSchemaMetadata("isComponentLevel", contract.getComponentId() != null);
    }
    
    private void enrichCapitalStackMetadata(IRCapitalStack capitalStack) {
        capitalStack.setSchemaMetadata("entityType", "capitalStack");
        capitalStack.setSchemaMetadata("participantCount", capitalStack.getParticipantCount());
        capitalStack.setSchemaMetadata("totalCapital", capitalStack.getTotalCapital());
        capitalStack.setSchemaMetadata("hasWaterfall", capitalStack.getWaterfallId() != null);
    }
    
    private void enrichAssumptionMetadata(IRAssumption assumption) {
        assumption.setSchemaMetadata("entityType", "assumption");
        assumption.setSchemaMetadata("assumptionCategory", assumption.getCategory());
        assumption.setSchemaMetadata("assumptionScope", assumption.getScope());
        assumption.setSchemaMetadata("assumptionType", assumption.getType());
        assumption.setSchemaMetadata("isStochastic", assumption.isStochastic());
        assumption.setSchemaMetadata("isTimeVarying", assumption.isTimeVarying());
        assumption.setSchemaMetadata("hasTemplate", assumption.getTemplate() != null);
        assumption.setSchemaMetadata("hasMarketDataRef", assumption.getMarketDataRef() != null);
        assumption.setSchemaMetadata("hasUnit", assumption.getUnit() != null);
    }
    
    private void enrichWaterfallMetadata(IRWaterfall waterfall) {
        waterfall.setSchemaMetadata("entityType", "waterfall");
        waterfall.setSchemaMetadata("tierCount", waterfall.getTierCount());
        waterfall.setSchemaMetadata("hasPreferredReturns", waterfall.hasPreferredReturns());
        waterfall.setSchemaMetadata("usesCapitalStackProportions", waterfall.usesCapitalStackProportions());
        waterfall.setSchemaMetadata("hasDescription", waterfall.getDescription() != null);
    }
    
    private void enrichPortfolioMetadata(IRPortfolio portfolio) {
        portfolio.setSchemaMetadata("entityType", "portfolio");
        portfolio.setSchemaMetadata("dealCount", portfolio.getDealCount());
        portfolio.setSchemaMetadata("streamCount", portfolio.getStreamCount());
        portfolio.setSchemaMetadata("hasStateManagement", portfolio.hasStateManagement());
        portfolio.setSchemaMetadata("hasPortfolioStreams", portfolio.hasPortfolioStreams());
        portfolio.setSchemaMetadata("hasDescription", portfolio.getDescription() != null);
        
        if (portfolio.getCurrentState() != null) {
            portfolio.setSchemaMetadata("currentState", portfolio.getCurrentState());
        }
    }
    
    private void enrichFundMetadata(IRFund fund) {
        fund.setSchemaMetadata("entityType", "fund");
        fund.setSchemaMetadata("portfolioCount", fund.getPortfolioCount());
        fund.setSchemaMetadata("streamCount", fund.getStreamCount());
        fund.setSchemaMetadata("participantCount", fund.getParticipantCount());
        fund.setSchemaMetadata("totalCommitment", fund.getTotalCommitment());
        fund.setSchemaMetadata("hasGeneralPartners", fund.hasGeneralPartners());
        fund.setSchemaMetadata("hasLimitedPartners", fund.hasLimitedPartners());
        fund.setSchemaMetadata("hasFundStreams", fund.hasFundStreams());
        fund.setSchemaMetadata("hasDescription", fund.getDescription() != null);
        fund.setSchemaMetadata("hasFundType", fund.getFundType() != null);
        
        if (fund.getCurrentState() != null) {
            fund.setSchemaMetadata("currentState", fund.getCurrentState());
        }
    }
    
    private void enrichLogicBlockMetadata(IRLogicBlock logicBlock) {
        logicBlock.setSchemaMetadata("entityType", "logicBlock");
        logicBlock.setSchemaMetadata("blockScope", logicBlock.getScope());
        logicBlock.setSchemaMetadata("blockType", logicBlock.getType());
        logicBlock.setSchemaMetadata("inputCount", logicBlock.getInputCount());
        logicBlock.setSchemaMetadata("outputCount", logicBlock.getOutputCount());
        logicBlock.setSchemaMetadata("hasExecutionOrder", logicBlock.hasExecutionOrder());
        logicBlock.setSchemaMetadata("hasLanguage", logicBlock.hasLanguage());
        logicBlock.setSchemaMetadata("isCalculation", logicBlock.isCalculation());
        logicBlock.setSchemaMetadata("isValidation", logicBlock.isValidation());
        logicBlock.setSchemaMetadata("isTrigger", logicBlock.isTrigger());
        logicBlock.setSchemaMetadata("hasDescription", logicBlock.getDescription() != null);
        
        if (logicBlock.getExecutionOrder() != null) {
            logicBlock.setSchemaMetadata("executionOrder", logicBlock.getExecutionOrder());
        }
        
        if (logicBlock.getLanguage() != null) {
            logicBlock.setSchemaMetadata("language", logicBlock.getLanguage());
        }
    }
    
    private void enrichRuleBlockMetadata(IRRuleBlock ruleBlock) {
        ruleBlock.setSchemaMetadata("entityType", "ruleBlock");
        ruleBlock.setSchemaMetadata("blockScope", ruleBlock.getScope());
        ruleBlock.setSchemaMetadata("triggerType", ruleBlock.getTriggerType());
        ruleBlock.setSchemaMetadata("hasScheduleTrigger", ruleBlock.hasScheduleTrigger());
        ruleBlock.setSchemaMetadata("hasEventTrigger", ruleBlock.hasEventTrigger());
        ruleBlock.setSchemaMetadata("hasCondition", ruleBlock.hasCondition());
        ruleBlock.setSchemaMetadata("hasMultipleActions", ruleBlock.hasMultipleActions());
        ruleBlock.setSchemaMetadata("actionCount", ruleBlock.getActionCount());
        ruleBlock.setSchemaMetadata("hasDescription", ruleBlock.getDescription() != null);
        
        if (ruleBlock.getScheduleId() != null) {
            ruleBlock.setSchemaMetadata("scheduleId", ruleBlock.getScheduleId());
        }
        
        if (ruleBlock.getEventTriggerId() != null) {
            ruleBlock.setSchemaMetadata("eventTriggerId", ruleBlock.getEventTriggerId());
        }
    }
    
    private void enrichEventTriggerMetadata(IREventTrigger eventTrigger) {
        eventTrigger.setSchemaMetadata("entityType", "eventTrigger");
        eventTrigger.setSchemaMetadata("triggerType", eventTrigger.getType());
        eventTrigger.setSchemaMetadata("isAssumptionChangeTrigger", eventTrigger.isAssumptionChangeTrigger());
        eventTrigger.setSchemaMetadata("isStreamThresholdTrigger", eventTrigger.isStreamThresholdTrigger());
        eventTrigger.setSchemaMetadata("isExternalEventTrigger", eventTrigger.isExternalEventTrigger());
        eventTrigger.setSchemaMetadata("isCustomTrigger", eventTrigger.isCustomTrigger());
        eventTrigger.setSchemaMetadata("triggerDescription", eventTrigger.getTriggerDescription());
        
        if (eventTrigger.getAssumptionId() != null) {
            eventTrigger.setSchemaMetadata("assumptionId", eventTrigger.getAssumptionId());
        }
        
        if (eventTrigger.getStreamId() != null) {
            eventTrigger.setSchemaMetadata("streamId", eventTrigger.getStreamId());
        }
        
        if (eventTrigger.getOperator() != null) {
            eventTrigger.setSchemaMetadata("operator", eventTrigger.getOperator());
        }
        
        if (eventTrigger.getThreshold() != null) {
            eventTrigger.setSchemaMetadata("threshold", eventTrigger.getThreshold());
        }
        
        if (eventTrigger.getExternalEventName() != null) {
            eventTrigger.setSchemaMetadata("externalEventName", eventTrigger.getExternalEventName());
        }
    }
    
    private void enrichTemplateMetadata(IRTemplate template) {
        template.setSchemaMetadata("entityType", "template");
        template.setSchemaMetadata("templateType", template.getTemplateType());
        template.setSchemaMetadata("parameterCount", template.getParameterCount());
        template.setSchemaMetadata("requiredParameterCount", template.getRequiredParameterCount());
        template.setSchemaMetadata("optionalParameterCount", template.getOptionalParameterCount());
        template.setSchemaMetadata("hasParameters", template.hasParameters());
        template.setSchemaMetadata("hasDefaultValues", template.hasDefaultValues());
        template.setSchemaMetadata("hasDescription", template.getDescription() != null);
        template.setSchemaMetadata("parameterNames", template.getParameterNames());
        
        // Determine template complexity based on parameter count and body length
        int complexity = 0;
        if (template.getParameterCount() > 5) complexity++;
        if (template.getBody() != null && template.getBody().length() > 500) complexity++;
        if (template.getRequiredParameterCount() > 3) complexity++;
        
        String complexityLevel = complexity == 0 ? "simple" : complexity == 1 ? "moderate" : "complex";
        template.setSchemaMetadata("complexityLevel", complexityLevel);
    }
    
    private void enrichMarketDataMetadata(IRMarketData marketData) {
        marketData.setSchemaMetadata("entityType", "marketData");
        marketData.setSchemaMetadata("dataType", marketData.getDataType());
        marketData.setSchemaMetadata("hasMultipleSymbols", marketData.hasMultipleSymbols());
        marketData.setSchemaMetadata("symbolCount", marketData.getSymbolCount());
        marketData.setSchemaMetadata("isInterestRate", marketData.isInterestRate());
        marketData.setSchemaMetadata("isIndexValue", marketData.isIndexValue());
        marketData.setSchemaMetadata("isFxRate", marketData.isFxRate());
        marketData.setSchemaMetadata("isInflationIndex", marketData.isInflationIndex());
        marketData.setSchemaMetadata("isCustomData", marketData.isCustomData());
        marketData.setSchemaMetadata("sourceType", marketData.getSourceType());
        marketData.setSchemaMetadata("hasCredentials", marketData.hasCredentials());
        marketData.setSchemaMetadata("hasSourceParameters", marketData.hasSourceParameters());
        marketData.setSchemaMetadata("hasRefreshSchedule", marketData.hasRefreshSchedule());
        marketData.setSchemaMetadata("hasField", marketData.hasField());
        marketData.setSchemaMetadata("hasDescription", marketData.getDescription() != null);
        
        // Add schedule dependency metadata if present
        if (marketData.getRefreshScheduleId() != null) {
            marketData.setSchemaMetadata("refreshScheduleId", marketData.getRefreshScheduleId());
        }
        
        // Add field metadata if present
        if (marketData.getField() != null) {
            marketData.setSchemaMetadata("field", marketData.getField());
        }
    }
    
    private void enrichStreamMetadata(IRStream stream) {
        stream.setSchemaMetadata("entityType", "stream");
        stream.setSchemaMetadata("hasGrowthModel", stream.getGrowth() != null);
        stream.setSchemaMetadata("tagCount", stream.getTags().size());
        stream.setSchemaMetadata("amountType", stream.getAmount() instanceof Number ? "fixed" : "calculated");
    }
    
    private List<String> getRequiredFields(IRNode irNode) {
        List<String> required = new ArrayList<>();
        if (irNode instanceof IRDeal) {
            required.add("id");
            required.add("name");
            required.add("dealType");
            required.add("currency");
            required.add("entryDate");
            required.add("exitDate");
            required.add("analysisStart");
            required.add("holdingPeriodYears");
        } else if (irNode instanceof IRAsset) {
            required.add("id");
            required.add("name");
            required.add("dealId");
            required.add("category");
        } else if (irNode instanceof IRComponent) {
            required.add("id");
            required.add("name");
            required.add("assetId");
        } else if (irNode instanceof IRParty) {
            required.add("id");
            required.add("name");
            required.add("partyType");
        } else if (irNode instanceof IRContract) {
            required.add("id");
            required.add("name");
            required.add("dealId");
            required.add("contractType");
            required.add("parties");
            required.add("startDate");
            required.add("endDate");
        } else if (irNode instanceof IRCapitalStack) {
            required.add("id");
            required.add("name");
            required.add("participants");
            required.add("waterfallId");
        } else if (irNode instanceof IRAssumption) {
            required.add("id");
            required.add("category");
            required.add("scope");
            required.add("type");
        } else if (irNode instanceof IRWaterfall) {
            required.add("id");
            required.add("name");
            required.add("tiers");
        } else if (irNode instanceof IRPortfolio) {
            required.add("id");
            required.add("name");
            required.add("deals");
        } else if (irNode instanceof IRFund) {
            required.add("id");
            required.add("name");
            required.add("stateConfig");
        } else if (irNode instanceof IRLogicBlock) {
            required.add("id");
            required.add("name");
            required.add("scope");
            required.add("type");
            required.add("code");
        } else if (irNode instanceof IRRuleBlock) {
            required.add("id");
            required.add("name");
            required.add("scope");
            required.add("action");
        } else if (irNode instanceof IREventTrigger) {
            required.add("type");
        } else if (irNode instanceof IRTemplate) {
            required.add("id");
            required.add("name");
            required.add("templateType");
            required.add("parameters");
            required.add("body");
        } else if (irNode instanceof IRMarketData) {
            required.add("id");
            required.add("name");
            required.add("dataType");
            required.add("symbol");
            required.add("source");
        } else if (irNode instanceof IRStream) {
            required.add("id");
            required.add("name");
            required.add("scope");
            required.add("category");
            required.add("schedule");
            required.add("amount");
        }
        return required;
    }
    
    private List<String> getOptionalFields(IRNode irNode) {
        List<String> optional = new ArrayList<>();
        if (irNode instanceof IRDeal) {
            optional.add("description");
            optional.add("calendar");
            optional.add("participants");
            optional.add("capitalStackId");
            optional.add("stateConfig");
            optional.add("metadata");
        } else if (irNode instanceof IRAsset) {
            optional.add("description");
            optional.add("location");
            optional.add("attributes");
            optional.add("stateConfig");
            optional.add("history");
            optional.add("metadata");
        } else if (irNode instanceof IRComponent) {
            optional.add("description");
            optional.add("componentType");
            optional.add("attributes");
            optional.add("stateConfig");
            optional.add("metadata");
        } else if (irNode instanceof IRParty) {
            optional.add("description");
            optional.add("contactInfo");
            optional.add("metadata");
        } else if (irNode instanceof IRContract) {
            optional.add("description");
            optional.add("assetId");
            optional.add("componentId");
            optional.add("terms");
            optional.add("stateConfig");
            optional.add("metadata");
        } else if (irNode instanceof IRCapitalStack) {
            optional.add("metadata");
        } else if (irNode instanceof IRAssumption) {
            optional.add("name");
            optional.add("value");
            optional.add("distribution");
            optional.add("timeSeries");
            optional.add("unit");
            optional.add("template");
            optional.add("overrides");
            optional.add("source");
            optional.add("marketDataRef");
            optional.add("metadata");
        } else if (irNode instanceof IRWaterfall) {
            optional.add("description");
            optional.add("metadata");
        } else if (irNode instanceof IRPortfolio) {
            optional.add("description");
            optional.add("streams");
            optional.add("stateConfig");
            optional.add("metadata");
        } else if (irNode instanceof IRFund) {
            optional.add("description");
            optional.add("fundType");
            optional.add("portfolios");
            optional.add("streams");
            optional.add("participants");
            optional.add("metadata");
        } else if (irNode instanceof IRLogicBlock) {
            optional.add("description");
            optional.add("inputs");
            optional.add("outputs");
            optional.add("executionOrder");
            optional.add("language");
            optional.add("metadata");
        } else if (irNode instanceof IRRuleBlock) {
            optional.add("description");
            optional.add("schedule");
            optional.add("eventTrigger");
            optional.add("condition");
            optional.add("metadata");
        } else if (irNode instanceof IREventTrigger) {
            optional.add("assumptionId");
            optional.add("streamId");
            optional.add("operator");
            optional.add("threshold");
            optional.add("externalEventName");
            optional.add("expression");
            optional.add("metadata");
        } else if (irNode instanceof IRTemplate) {
            optional.add("description");
            optional.add("metadata");
        } else if (irNode instanceof IRMarketData) {
            optional.add("description");
            optional.add("refreshSchedule");
            optional.add("field");
            optional.add("metadata");
        } else if (irNode instanceof IRStream) {
            optional.add("description");
            optional.add("subType");
            optional.add("growth");
            optional.add("tags");
            optional.add("metadata");
        }
        return optional;
    }
    
    /**
     * Resolves references between IR nodes.
     */
    private void resolveReferences(IRNode irNode) {
        for (String dependency : irNode.getDependencies()) {
            if (!nodeRegistry.containsKey(dependency)) {
                unresolvedReferences.add(dependency);
            }
        }
    }
    
    /**
     * Validates an IR node against its JSON schema.
     */
    private void validateIRNode(IRNode irNode, IRBuildResult result) {
        // First run the node's own validation
        if (irNode instanceof IRDeal) {
            ((IRDeal) irNode).validate();
        } else if (irNode instanceof IRAsset) {
            ((IRAsset) irNode).validate();
        } else if (irNode instanceof IRComponent) {
            ((IRComponent) irNode).validate();
        } else if (irNode instanceof IRParty) {
            ((IRParty) irNode).validate();
        } else if (irNode instanceof IRContract) {
            ((IRContract) irNode).validate();
        } else if (irNode instanceof IRCapitalStack) {
            ((IRCapitalStack) irNode).validate();
        } else if (irNode instanceof IRAssumption) {
            ((IRAssumption) irNode).validate();
        } else if (irNode instanceof IRWaterfall) {
            ((IRWaterfall) irNode).validate();
        } else if (irNode instanceof IRPortfolio) {
            ((IRPortfolio) irNode).validate();
        } else if (irNode instanceof IRFund) {
            ((IRFund) irNode).validate();
        } else if (irNode instanceof IRLogicBlock) {
            ((IRLogicBlock) irNode).validate();
        } else if (irNode instanceof IRRuleBlock) {
            ((IRRuleBlock) irNode).validate();
        } else if (irNode instanceof IREventTrigger) {
            ((IREventTrigger) irNode).validate();
        } else if (irNode instanceof IRTemplate) {
            ((IRTemplate) irNode).validate();
        } else if (irNode instanceof IRMarketData) {
            ((IRMarketData) irNode).validate();
        } else if (irNode instanceof IRStream) {
            ((IRStream) irNode).validate();
        }
        
        // Collect validation messages
        if (!irNode.isValid()) {
            for (String message : irNode.getValidationMessages()) {
                result.addError("Validation error in " + irNode.getId() + ": " + message);
            }
        }
        
        // Note: Schema validation could be added here in the future
        // The current SchemaValidator works with ASTNode objects
        // For now, we rely on the IR node's own validation logic
    }
}