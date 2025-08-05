// Generated from cfdl.g4 by ANTLR 4.13.1

  package dev.cfdl.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link cfdlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface cfdlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link cfdlParser#specification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSpecification(cfdlParser.SpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(cfdlParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#genericDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericDefinition(cfdlParser.GenericDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#dealDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDealDefinition(cfdlParser.DealDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#assetDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssetDefinition(cfdlParser.AssetDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#componentDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponentDefinition(cfdlParser.ComponentDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#streamDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStreamDefinition(cfdlParser.StreamDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#assumptionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssumptionDefinition(cfdlParser.AssumptionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#logicBlockDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicBlockDefinition(cfdlParser.LogicBlockDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#ruleBlockDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleBlockDefinition(cfdlParser.RuleBlockDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#scheduleDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScheduleDefinition(cfdlParser.ScheduleDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#eventTriggerDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventTriggerDefinition(cfdlParser.EventTriggerDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#templateDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateDefinition(cfdlParser.TemplateDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#contractDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContractDefinition(cfdlParser.ContractDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#partyDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartyDefinition(cfdlParser.PartyDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#fundDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFundDefinition(cfdlParser.FundDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#portfolioDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortfolioDefinition(cfdlParser.PortfolioDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#capitalStackDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapitalStackDefinition(cfdlParser.CapitalStackDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#waterFallDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaterFallDefinition(cfdlParser.WaterFallDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#metricDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetricDefinition(cfdlParser.MetricDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#dealProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDealProperty(cfdlParser.DealPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#assetProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssetProperty(cfdlParser.AssetPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#componentProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComponentProperty(cfdlParser.ComponentPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#streamProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStreamProperty(cfdlParser.StreamPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#assumptionProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssumptionProperty(cfdlParser.AssumptionPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#logicBlockProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicBlockProperty(cfdlParser.LogicBlockPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#ruleBlockProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleBlockProperty(cfdlParser.RuleBlockPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#scheduleProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScheduleProperty(cfdlParser.SchedulePropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#eventTriggerProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventTriggerProperty(cfdlParser.EventTriggerPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#templateProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateProperty(cfdlParser.TemplatePropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#contractProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContractProperty(cfdlParser.ContractPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#partyProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartyProperty(cfdlParser.PartyPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#fundProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFundProperty(cfdlParser.FundPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#portfolioProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPortfolioProperty(cfdlParser.PortfolioPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#capitalStackProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapitalStackProperty(cfdlParser.CapitalStackPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#waterFallProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWaterFallProperty(cfdlParser.WaterFallPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#metricProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetricProperty(cfdlParser.MetricPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#genericProperty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericProperty(cfdlParser.GenericPropertyContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(cfdlParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#propertyDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyDef(cfdlParser.PropertyDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#propertyAssign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyAssign(cfdlParser.PropertyAssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#logicBlockDef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicBlockDef(cfdlParser.LogicBlockDefContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#referenceDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReferenceDecl(cfdlParser.ReferenceDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#importDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDecl(cfdlParser.ImportDeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#typeRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRef(cfdlParser.TypeRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link cfdlParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(cfdlParser.ValueContext ctx);
}