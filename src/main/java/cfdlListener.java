// Generated from cfdl.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link cfdlParser}.
 */
public interface cfdlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link cfdlParser#specification}.
	 * @param ctx the parse tree
	 */
	void enterSpecification(cfdlParser.SpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#specification}.
	 * @param ctx the parse tree
	 */
	void exitSpecification(cfdlParser.SpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#definition}.
	 * @param ctx the parse tree
	 */
	void enterDefinition(cfdlParser.DefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#definition}.
	 * @param ctx the parse tree
	 */
	void exitDefinition(cfdlParser.DefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#genericDefinition}.
	 * @param ctx the parse tree
	 */
	void enterGenericDefinition(cfdlParser.GenericDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#genericDefinition}.
	 * @param ctx the parse tree
	 */
	void exitGenericDefinition(cfdlParser.GenericDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#dealDefinition}.
	 * @param ctx the parse tree
	 */
	void enterDealDefinition(cfdlParser.DealDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#dealDefinition}.
	 * @param ctx the parse tree
	 */
	void exitDealDefinition(cfdlParser.DealDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#assetDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAssetDefinition(cfdlParser.AssetDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#assetDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAssetDefinition(cfdlParser.AssetDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#componentDefinition}.
	 * @param ctx the parse tree
	 */
	void enterComponentDefinition(cfdlParser.ComponentDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#componentDefinition}.
	 * @param ctx the parse tree
	 */
	void exitComponentDefinition(cfdlParser.ComponentDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#streamDefinition}.
	 * @param ctx the parse tree
	 */
	void enterStreamDefinition(cfdlParser.StreamDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#streamDefinition}.
	 * @param ctx the parse tree
	 */
	void exitStreamDefinition(cfdlParser.StreamDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#assumptionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAssumptionDefinition(cfdlParser.AssumptionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#assumptionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAssumptionDefinition(cfdlParser.AssumptionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#logicBlockDefinition}.
	 * @param ctx the parse tree
	 */
	void enterLogicBlockDefinition(cfdlParser.LogicBlockDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#logicBlockDefinition}.
	 * @param ctx the parse tree
	 */
	void exitLogicBlockDefinition(cfdlParser.LogicBlockDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#ruleBlockDefinition}.
	 * @param ctx the parse tree
	 */
	void enterRuleBlockDefinition(cfdlParser.RuleBlockDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#ruleBlockDefinition}.
	 * @param ctx the parse tree
	 */
	void exitRuleBlockDefinition(cfdlParser.RuleBlockDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#scheduleDefinition}.
	 * @param ctx the parse tree
	 */
	void enterScheduleDefinition(cfdlParser.ScheduleDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#scheduleDefinition}.
	 * @param ctx the parse tree
	 */
	void exitScheduleDefinition(cfdlParser.ScheduleDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#eventTriggerDefinition}.
	 * @param ctx the parse tree
	 */
	void enterEventTriggerDefinition(cfdlParser.EventTriggerDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#eventTriggerDefinition}.
	 * @param ctx the parse tree
	 */
	void exitEventTriggerDefinition(cfdlParser.EventTriggerDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#templateDefinition}.
	 * @param ctx the parse tree
	 */
	void enterTemplateDefinition(cfdlParser.TemplateDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#templateDefinition}.
	 * @param ctx the parse tree
	 */
	void exitTemplateDefinition(cfdlParser.TemplateDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#contractDefinition}.
	 * @param ctx the parse tree
	 */
	void enterContractDefinition(cfdlParser.ContractDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#contractDefinition}.
	 * @param ctx the parse tree
	 */
	void exitContractDefinition(cfdlParser.ContractDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#partyDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPartyDefinition(cfdlParser.PartyDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#partyDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPartyDefinition(cfdlParser.PartyDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#fundDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFundDefinition(cfdlParser.FundDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#fundDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFundDefinition(cfdlParser.FundDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#portfolioDefinition}.
	 * @param ctx the parse tree
	 */
	void enterPortfolioDefinition(cfdlParser.PortfolioDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#portfolioDefinition}.
	 * @param ctx the parse tree
	 */
	void exitPortfolioDefinition(cfdlParser.PortfolioDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#capitalStackDefinition}.
	 * @param ctx the parse tree
	 */
	void enterCapitalStackDefinition(cfdlParser.CapitalStackDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#capitalStackDefinition}.
	 * @param ctx the parse tree
	 */
	void exitCapitalStackDefinition(cfdlParser.CapitalStackDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#waterFallDefinition}.
	 * @param ctx the parse tree
	 */
	void enterWaterFallDefinition(cfdlParser.WaterFallDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#waterFallDefinition}.
	 * @param ctx the parse tree
	 */
	void exitWaterFallDefinition(cfdlParser.WaterFallDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#metricDefinition}.
	 * @param ctx the parse tree
	 */
	void enterMetricDefinition(cfdlParser.MetricDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#metricDefinition}.
	 * @param ctx the parse tree
	 */
	void exitMetricDefinition(cfdlParser.MetricDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#dealProperty}.
	 * @param ctx the parse tree
	 */
	void enterDealProperty(cfdlParser.DealPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#dealProperty}.
	 * @param ctx the parse tree
	 */
	void exitDealProperty(cfdlParser.DealPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#dealTypeValue}.
	 * @param ctx the parse tree
	 */
	void enterDealTypeValue(cfdlParser.DealTypeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#dealTypeValue}.
	 * @param ctx the parse tree
	 */
	void exitDealTypeValue(cfdlParser.DealTypeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#assetProperty}.
	 * @param ctx the parse tree
	 */
	void enterAssetProperty(cfdlParser.AssetPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#assetProperty}.
	 * @param ctx the parse tree
	 */
	void exitAssetProperty(cfdlParser.AssetPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#assetCategoryValue}.
	 * @param ctx the parse tree
	 */
	void enterAssetCategoryValue(cfdlParser.AssetCategoryValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#assetCategoryValue}.
	 * @param ctx the parse tree
	 */
	void exitAssetCategoryValue(cfdlParser.AssetCategoryValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#assetStateValue}.
	 * @param ctx the parse tree
	 */
	void enterAssetStateValue(cfdlParser.AssetStateValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#assetStateValue}.
	 * @param ctx the parse tree
	 */
	void exitAssetStateValue(cfdlParser.AssetStateValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#componentProperty}.
	 * @param ctx the parse tree
	 */
	void enterComponentProperty(cfdlParser.ComponentPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#componentProperty}.
	 * @param ctx the parse tree
	 */
	void exitComponentProperty(cfdlParser.ComponentPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#streamProperty}.
	 * @param ctx the parse tree
	 */
	void enterStreamProperty(cfdlParser.StreamPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#streamProperty}.
	 * @param ctx the parse tree
	 */
	void exitStreamProperty(cfdlParser.StreamPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#componentScopeValue}.
	 * @param ctx the parse tree
	 */
	void enterComponentScopeValue(cfdlParser.ComponentScopeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#componentScopeValue}.
	 * @param ctx the parse tree
	 */
	void exitComponentScopeValue(cfdlParser.ComponentScopeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#streamCategoryValue}.
	 * @param ctx the parse tree
	 */
	void enterStreamCategoryValue(cfdlParser.StreamCategoryValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#streamCategoryValue}.
	 * @param ctx the parse tree
	 */
	void exitStreamCategoryValue(cfdlParser.StreamCategoryValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#streamSubTypeValue}.
	 * @param ctx the parse tree
	 */
	void enterStreamSubTypeValue(cfdlParser.StreamSubTypeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#streamSubTypeValue}.
	 * @param ctx the parse tree
	 */
	void exitStreamSubTypeValue(cfdlParser.StreamSubTypeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#amountTypeValue}.
	 * @param ctx the parse tree
	 */
	void enterAmountTypeValue(cfdlParser.AmountTypeValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#amountTypeValue}.
	 * @param ctx the parse tree
	 */
	void exitAmountTypeValue(cfdlParser.AmountTypeValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#assumptionProperty}.
	 * @param ctx the parse tree
	 */
	void enterAssumptionProperty(cfdlParser.AssumptionPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#assumptionProperty}.
	 * @param ctx the parse tree
	 */
	void exitAssumptionProperty(cfdlParser.AssumptionPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#logicBlockProperty}.
	 * @param ctx the parse tree
	 */
	void enterLogicBlockProperty(cfdlParser.LogicBlockPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#logicBlockProperty}.
	 * @param ctx the parse tree
	 */
	void exitLogicBlockProperty(cfdlParser.LogicBlockPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#ruleBlockProperty}.
	 * @param ctx the parse tree
	 */
	void enterRuleBlockProperty(cfdlParser.RuleBlockPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#ruleBlockProperty}.
	 * @param ctx the parse tree
	 */
	void exitRuleBlockProperty(cfdlParser.RuleBlockPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#scheduleProperty}.
	 * @param ctx the parse tree
	 */
	void enterScheduleProperty(cfdlParser.SchedulePropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#scheduleProperty}.
	 * @param ctx the parse tree
	 */
	void exitScheduleProperty(cfdlParser.SchedulePropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#eventTriggerProperty}.
	 * @param ctx the parse tree
	 */
	void enterEventTriggerProperty(cfdlParser.EventTriggerPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#eventTriggerProperty}.
	 * @param ctx the parse tree
	 */
	void exitEventTriggerProperty(cfdlParser.EventTriggerPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#templateProperty}.
	 * @param ctx the parse tree
	 */
	void enterTemplateProperty(cfdlParser.TemplatePropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#templateProperty}.
	 * @param ctx the parse tree
	 */
	void exitTemplateProperty(cfdlParser.TemplatePropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#contractProperty}.
	 * @param ctx the parse tree
	 */
	void enterContractProperty(cfdlParser.ContractPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#contractProperty}.
	 * @param ctx the parse tree
	 */
	void exitContractProperty(cfdlParser.ContractPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#partyProperty}.
	 * @param ctx the parse tree
	 */
	void enterPartyProperty(cfdlParser.PartyPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#partyProperty}.
	 * @param ctx the parse tree
	 */
	void exitPartyProperty(cfdlParser.PartyPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#fundProperty}.
	 * @param ctx the parse tree
	 */
	void enterFundProperty(cfdlParser.FundPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#fundProperty}.
	 * @param ctx the parse tree
	 */
	void exitFundProperty(cfdlParser.FundPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#portfolioProperty}.
	 * @param ctx the parse tree
	 */
	void enterPortfolioProperty(cfdlParser.PortfolioPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#portfolioProperty}.
	 * @param ctx the parse tree
	 */
	void exitPortfolioProperty(cfdlParser.PortfolioPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#capitalStackProperty}.
	 * @param ctx the parse tree
	 */
	void enterCapitalStackProperty(cfdlParser.CapitalStackPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#capitalStackProperty}.
	 * @param ctx the parse tree
	 */
	void exitCapitalStackProperty(cfdlParser.CapitalStackPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#waterFallProperty}.
	 * @param ctx the parse tree
	 */
	void enterWaterFallProperty(cfdlParser.WaterFallPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#waterFallProperty}.
	 * @param ctx the parse tree
	 */
	void exitWaterFallProperty(cfdlParser.WaterFallPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#metricProperty}.
	 * @param ctx the parse tree
	 */
	void enterMetricProperty(cfdlParser.MetricPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#metricProperty}.
	 * @param ctx the parse tree
	 */
	void exitMetricProperty(cfdlParser.MetricPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#genericProperty}.
	 * @param ctx the parse tree
	 */
	void enterGenericProperty(cfdlParser.GenericPropertyContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#genericProperty}.
	 * @param ctx the parse tree
	 */
	void exitGenericProperty(cfdlParser.GenericPropertyContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(cfdlParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(cfdlParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#propertyDef}.
	 * @param ctx the parse tree
	 */
	void enterPropertyDef(cfdlParser.PropertyDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#propertyDef}.
	 * @param ctx the parse tree
	 */
	void exitPropertyDef(cfdlParser.PropertyDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#propertyAssign}.
	 * @param ctx the parse tree
	 */
	void enterPropertyAssign(cfdlParser.PropertyAssignContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#propertyAssign}.
	 * @param ctx the parse tree
	 */
	void exitPropertyAssign(cfdlParser.PropertyAssignContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#logicBlockDef}.
	 * @param ctx the parse tree
	 */
	void enterLogicBlockDef(cfdlParser.LogicBlockDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#logicBlockDef}.
	 * @param ctx the parse tree
	 */
	void exitLogicBlockDef(cfdlParser.LogicBlockDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#referenceDecl}.
	 * @param ctx the parse tree
	 */
	void enterReferenceDecl(cfdlParser.ReferenceDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#referenceDecl}.
	 * @param ctx the parse tree
	 */
	void exitReferenceDecl(cfdlParser.ReferenceDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#importDecl}.
	 * @param ctx the parse tree
	 */
	void enterImportDecl(cfdlParser.ImportDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#importDecl}.
	 * @param ctx the parse tree
	 */
	void exitImportDecl(cfdlParser.ImportDeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#typeRef}.
	 * @param ctx the parse tree
	 */
	void enterTypeRef(cfdlParser.TypeRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#typeRef}.
	 * @param ctx the parse tree
	 */
	void exitTypeRef(cfdlParser.TypeRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link cfdlParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(cfdlParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link cfdlParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(cfdlParser.ValueContext ctx);
}