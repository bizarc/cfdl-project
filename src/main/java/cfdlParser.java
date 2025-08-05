// Generated from cfdl.g4 by ANTLR 4.13.1

  package dev.cfdl.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class cfdlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, T__56=57, T__57=58, T__58=59, 
		T__59=60, T__60=61, T__61=62, T__62=63, T__63=64, T__64=65, T__65=66, 
		T__66=67, T__67=68, T__68=69, T__69=70, T__70=71, T__71=72, T__72=73, 
		T__73=74, T__74=75, T__75=76, T__76=77, T__77=78, IDENTIFIER=79, NUMBER=80, 
		STRING=81, BOOLEAN=82, DEAL_TYPE=83, ASSET_CATEGORY=84, COMPONENT_SCOPE=85, 
		ASSUMPTION_CATEGORY=86, DISTRIBUTION_TYPE=87, LOGIC_BLOCK_TYPE=88, METRIC_TYPE=89, 
		FREQUENCY=90, BUSINESS_DAY_CONVENTION=91, DAY_COUNT=92, PARTY_ROLE=93, 
		STREAM_CATEGORY=94, STREAM_SUB_TYPE=95, AMOUNT_TYPE=96, ASSUMPTION_TYPE=97, 
		SCHEDULE_TYPE=98, ASSET_STATE=99, FUND_PARTICIPANT_ROLE=100, TEMPLATE_TYPE=101, 
		PARAMETER_DATA_TYPE=102, RECURRENCE_FREQUENCY=103, WEEKDAY=104, TRIGGER_OPERATOR=105, 
		DATA_SOURCE_TYPE=106, PAYBACK_UNIT=107, WS=108, LINE_COMMENT=109, BLOCK_COMMENT=110;
	public static final int
		RULE_specification = 0, RULE_definition = 1, RULE_genericDefinition = 2, 
		RULE_dealDefinition = 3, RULE_assetDefinition = 4, RULE_componentDefinition = 5, 
		RULE_streamDefinition = 6, RULE_assumptionDefinition = 7, RULE_logicBlockDefinition = 8, 
		RULE_ruleBlockDefinition = 9, RULE_scheduleDefinition = 10, RULE_eventTriggerDefinition = 11, 
		RULE_templateDefinition = 12, RULE_contractDefinition = 13, RULE_partyDefinition = 14, 
		RULE_fundDefinition = 15, RULE_portfolioDefinition = 16, RULE_capitalStackDefinition = 17, 
		RULE_waterFallDefinition = 18, RULE_metricDefinition = 19, RULE_dealProperty = 20, 
		RULE_assetProperty = 21, RULE_componentProperty = 22, RULE_streamProperty = 23, 
		RULE_assumptionProperty = 24, RULE_logicBlockProperty = 25, RULE_ruleBlockProperty = 26, 
		RULE_scheduleProperty = 27, RULE_eventTriggerProperty = 28, RULE_templateProperty = 29, 
		RULE_contractProperty = 30, RULE_partyProperty = 31, RULE_fundProperty = 32, 
		RULE_portfolioProperty = 33, RULE_capitalStackProperty = 34, RULE_waterFallProperty = 35, 
		RULE_metricProperty = 36, RULE_genericProperty = 37, RULE_statement = 38, 
		RULE_propertyDef = 39, RULE_propertyAssign = 40, RULE_logicBlockDef = 41, 
		RULE_referenceDecl = 42, RULE_importDecl = 43, RULE_typeRef = 44, RULE_value = 45;
	private static String[] makeRuleNames() {
		return new String[] {
			"specification", "definition", "genericDefinition", "dealDefinition", 
			"assetDefinition", "componentDefinition", "streamDefinition", "assumptionDefinition", 
			"logicBlockDefinition", "ruleBlockDefinition", "scheduleDefinition", 
			"eventTriggerDefinition", "templateDefinition", "contractDefinition", 
			"partyDefinition", "fundDefinition", "portfolioDefinition", "capitalStackDefinition", 
			"waterFallDefinition", "metricDefinition", "dealProperty", "assetProperty", 
			"componentProperty", "streamProperty", "assumptionProperty", "logicBlockProperty", 
			"ruleBlockProperty", "scheduleProperty", "eventTriggerProperty", "templateProperty", 
			"contractProperty", "partyProperty", "fundProperty", "portfolioProperty", 
			"capitalStackProperty", "waterFallProperty", "metricProperty", "genericProperty", 
			"statement", "propertyDef", "propertyAssign", "logicBlockDef", "referenceDecl", 
			"importDecl", "typeRef", "value"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'entity'", "'behavior'", "'temporal'", "'result'", "'{'", "'}'", 
			"'deal'", "'asset'", "'component'", "'stream'", "'assumption'", "'logic_block'", 
			"'rule_block'", "'schedule'", "'event_trigger'", "'template'", "'contract'", 
			"'party'", "'fund'", "'portfolio'", "'capital_stack'", "'waterfall'", 
			"'metric'", "'name'", "':'", "';'", "'dealType'", "'entryDate'", "'exitDate'", 
			"'analysisStart'", "'currency'", "'holdingPeriodYears'", "'assets'", 
			"'['", "','", "']'", "'streams'", "'dealId'", "'category'", "'description'", 
			"'components'", "'state'", "'assetId'", "'scope'", "'subType'", "'amount'", 
			"'amountType'", "'type'", "'value'", "'distributionType'", "'code'", 
			"'language'", "'inputs'", "'outputs'", "'executionOrder'", "'condition'", 
			"'action'", "'frequency'", "'startDate'", "'endDate'", "'eventType'", 
			"'operator'", "'threshold'", "'templateType'", "'body'", "'parties'", 
			"'terms'", "'role'", "'contact'", "'participantRole'", "'totalCommitment'", 
			"'deals'", "'totalAmount'", "'distributionRules'", "'unit'", "'logic'", 
			"'$ref'", "'import'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "IDENTIFIER", "NUMBER", "STRING", 
			"BOOLEAN", "DEAL_TYPE", "ASSET_CATEGORY", "COMPONENT_SCOPE", "ASSUMPTION_CATEGORY", 
			"DISTRIBUTION_TYPE", "LOGIC_BLOCK_TYPE", "METRIC_TYPE", "FREQUENCY", 
			"BUSINESS_DAY_CONVENTION", "DAY_COUNT", "PARTY_ROLE", "STREAM_CATEGORY", 
			"STREAM_SUB_TYPE", "AMOUNT_TYPE", "ASSUMPTION_TYPE", "SCHEDULE_TYPE", 
			"ASSET_STATE", "FUND_PARTICIPANT_ROLE", "TEMPLATE_TYPE", "PARAMETER_DATA_TYPE", 
			"RECURRENCE_FREQUENCY", "WEEKDAY", "TRIGGER_OPERATOR", "DATA_SOURCE_TYPE", 
			"PAYBACK_UNIT", "WS", "LINE_COMMENT", "BLOCK_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "cfdl.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public cfdlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SpecificationContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(cfdlParser.EOF, 0); }
		public List<DefinitionContext> definition() {
			return getRuleContexts(DefinitionContext.class);
		}
		public DefinitionContext definition(int i) {
			return getRuleContext(DefinitionContext.class,i);
		}
		public SpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_specification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SpecificationContext specification() throws RecognitionException {
		SpecificationContext _localctx = new SpecificationContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_specification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(92);
				definition();
				}
				}
				setState(95); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 16777118L) != 0) );
			setState(97);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DefinitionContext extends ParserRuleContext {
		public DealDefinitionContext dealDefinition() {
			return getRuleContext(DealDefinitionContext.class,0);
		}
		public AssetDefinitionContext assetDefinition() {
			return getRuleContext(AssetDefinitionContext.class,0);
		}
		public ComponentDefinitionContext componentDefinition() {
			return getRuleContext(ComponentDefinitionContext.class,0);
		}
		public StreamDefinitionContext streamDefinition() {
			return getRuleContext(StreamDefinitionContext.class,0);
		}
		public AssumptionDefinitionContext assumptionDefinition() {
			return getRuleContext(AssumptionDefinitionContext.class,0);
		}
		public LogicBlockDefinitionContext logicBlockDefinition() {
			return getRuleContext(LogicBlockDefinitionContext.class,0);
		}
		public RuleBlockDefinitionContext ruleBlockDefinition() {
			return getRuleContext(RuleBlockDefinitionContext.class,0);
		}
		public ScheduleDefinitionContext scheduleDefinition() {
			return getRuleContext(ScheduleDefinitionContext.class,0);
		}
		public EventTriggerDefinitionContext eventTriggerDefinition() {
			return getRuleContext(EventTriggerDefinitionContext.class,0);
		}
		public TemplateDefinitionContext templateDefinition() {
			return getRuleContext(TemplateDefinitionContext.class,0);
		}
		public ContractDefinitionContext contractDefinition() {
			return getRuleContext(ContractDefinitionContext.class,0);
		}
		public PartyDefinitionContext partyDefinition() {
			return getRuleContext(PartyDefinitionContext.class,0);
		}
		public FundDefinitionContext fundDefinition() {
			return getRuleContext(FundDefinitionContext.class,0);
		}
		public PortfolioDefinitionContext portfolioDefinition() {
			return getRuleContext(PortfolioDefinitionContext.class,0);
		}
		public CapitalStackDefinitionContext capitalStackDefinition() {
			return getRuleContext(CapitalStackDefinitionContext.class,0);
		}
		public WaterFallDefinitionContext waterFallDefinition() {
			return getRuleContext(WaterFallDefinitionContext.class,0);
		}
		public MetricDefinitionContext metricDefinition() {
			return getRuleContext(MetricDefinitionContext.class,0);
		}
		public GenericDefinitionContext genericDefinition() {
			return getRuleContext(GenericDefinitionContext.class,0);
		}
		public DefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DefinitionContext definition() throws RecognitionException {
		DefinitionContext _localctx = new DefinitionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_definition);
		try {
			setState(117);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				dealDefinition();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				assetDefinition();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 3);
				{
				setState(101);
				componentDefinition();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(102);
				streamDefinition();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 5);
				{
				setState(103);
				assumptionDefinition();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 6);
				{
				setState(104);
				logicBlockDefinition();
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 7);
				{
				setState(105);
				ruleBlockDefinition();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 8);
				{
				setState(106);
				scheduleDefinition();
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 9);
				{
				setState(107);
				eventTriggerDefinition();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 10);
				{
				setState(108);
				templateDefinition();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 11);
				{
				setState(109);
				contractDefinition();
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 12);
				{
				setState(110);
				partyDefinition();
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 13);
				{
				setState(111);
				fundDefinition();
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 14);
				{
				setState(112);
				portfolioDefinition();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 15);
				{
				setState(113);
				capitalStackDefinition();
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 16);
				{
				setState(114);
				waterFallDefinition();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 17);
				{
				setState(115);
				metricDefinition();
				}
				break;
			case T__0:
			case T__1:
			case T__2:
			case T__3:
				enterOuterAlt(_localctx, 18);
				{
				setState(116);
				genericDefinition();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GenericDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public GenericDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterGenericDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitGenericDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitGenericDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GenericDefinitionContext genericDefinition() throws RecognitionException {
		GenericDefinitionContext _localctx = new GenericDefinitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_genericDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(120);
			match(IDENTIFIER);
			setState(121);
			match(T__4);
			setState(125);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 15L) != 0)) {
				{
				{
				setState(122);
				statement();
				}
				}
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(128);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DealDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<DealPropertyContext> dealProperty() {
			return getRuleContexts(DealPropertyContext.class);
		}
		public DealPropertyContext dealProperty(int i) {
			return getRuleContext(DealPropertyContext.class,i);
		}
		public DealDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dealDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterDealDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitDealDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitDealDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DealDefinitionContext dealDefinition() throws RecognitionException {
		DealDefinitionContext _localctx = new DealDefinitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_dealDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(T__6);
			setState(131);
			match(IDENTIFIER);
			setState(132);
			match(T__4);
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36028797018973177L) != 0)) {
				{
				{
				setState(133);
				dealProperty();
				}
				}
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(139);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssetDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<AssetPropertyContext> assetProperty() {
			return getRuleContexts(AssetPropertyContext.class);
		}
		public AssetPropertyContext assetProperty(int i) {
			return getRuleContext(AssetPropertyContext.class,i);
		}
		public AssetDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assetDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterAssetDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitAssetDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitAssetDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssetDefinitionContext assetDefinition() throws RecognitionException {
		AssetDefinitionContext _localctx = new AssetDefinitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_assetDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(T__7);
			setState(142);
			match(IDENTIFIER);
			setState(143);
			match(T__4);
			setState(147);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36028797019480065L) != 0)) {
				{
				{
				setState(144);
				assetProperty();
				}
				}
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(150);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComponentDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<ComponentPropertyContext> componentProperty() {
			return getRuleContexts(ComponentPropertyContext.class);
		}
		public ComponentPropertyContext componentProperty(int i) {
			return getRuleContext(ComponentPropertyContext.class,i);
		}
		public ComponentDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_componentDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterComponentDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitComponentDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitComponentDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComponentDefinitionContext componentDefinition() throws RecognitionException {
		ComponentDefinitionContext _localctx = new ComponentDefinitionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_componentDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(T__8);
			setState(153);
			match(IDENTIFIER);
			setState(154);
			match(T__4);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36028797020545025L) != 0)) {
				{
				{
				setState(155);
				componentProperty();
				}
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(161);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StreamDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<StreamPropertyContext> streamProperty() {
			return getRuleContexts(StreamPropertyContext.class);
		}
		public StreamPropertyContext streamProperty(int i) {
			return getRuleContext(StreamPropertyContext.class,i);
		}
		public StreamDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_streamDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterStreamDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitStreamDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitStreamDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StreamDefinitionContext streamDefinition() throws RecognitionException {
		StreamDefinitionContext _localctx = new StreamDefinitionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_streamDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(T__9);
			setState(164);
			match(IDENTIFIER);
			setState(165);
			match(T__4);
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 264432563273728L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(166);
				streamProperty();
				}
				}
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(172);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssumptionDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<AssumptionPropertyContext> assumptionProperty() {
			return getRuleContexts(AssumptionPropertyContext.class);
		}
		public AssumptionPropertyContext assumptionProperty(int i) {
			return getRuleContext(AssumptionPropertyContext.class,i);
		}
		public AssumptionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assumptionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterAssumptionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitAssumptionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitAssumptionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssumptionDefinitionContext assumptionDefinition() throws RecognitionException {
		AssumptionDefinitionContext _localctx = new AssumptionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_assumptionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			match(T__10);
			setState(175);
			match(IDENTIFIER);
			setState(176);
			match(T__4);
			setState(180);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36028797137485825L) != 0)) {
				{
				{
				setState(177);
				assumptionProperty();
				}
				}
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(183);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogicBlockDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<LogicBlockPropertyContext> logicBlockProperty() {
			return getRuleContexts(LogicBlockPropertyContext.class);
		}
		public LogicBlockPropertyContext logicBlockProperty(int i) {
			return getRuleContext(LogicBlockPropertyContext.class,i);
		}
		public LogicBlockDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicBlockDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterLogicBlockDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitLogicBlockDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitLogicBlockDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicBlockDefinitionContext logicBlockDefinition() throws RecognitionException {
		LogicBlockDefinitionContext _localctx = new LogicBlockDefinitionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_logicBlockDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			match(T__11);
			setState(186);
			match(IDENTIFIER);
			setState(187);
			match(T__4);
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36028801197539329L) != 0)) {
				{
				{
				setState(188);
				logicBlockProperty();
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(194);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RuleBlockDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<RuleBlockPropertyContext> ruleBlockProperty() {
			return getRuleContexts(RuleBlockPropertyContext.class);
		}
		public RuleBlockPropertyContext ruleBlockProperty(int i) {
			return getRuleContext(RuleBlockPropertyContext.class,i);
		}
		public RuleBlockDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleBlockDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterRuleBlockDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitRuleBlockDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitRuleBlockDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleBlockDefinitionContext ruleBlockDefinition() throws RecognitionException {
		RuleBlockDefinitionContext _localctx = new RuleBlockDefinitionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_ruleBlockDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			match(T__12);
			setState(197);
			match(IDENTIFIER);
			setState(198);
			match(T__4);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36028809904914433L) != 0)) {
				{
				{
				setState(199);
				ruleBlockProperty();
				}
				}
				setState(204);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(205);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScheduleDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<SchedulePropertyContext> scheduleProperty() {
			return getRuleContexts(SchedulePropertyContext.class);
		}
		public SchedulePropertyContext scheduleProperty(int i) {
			return getRuleContext(SchedulePropertyContext.class,i);
		}
		public ScheduleDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scheduleDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterScheduleDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitScheduleDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitScheduleDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScheduleDefinitionContext scheduleDefinition() throws RecognitionException {
		ScheduleDefinitionContext _localctx = new ScheduleDefinitionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_scheduleDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(T__13);
			setState(208);
			match(IDENTIFIER);
			setState(209);
			match(T__4);
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36028917294825473L) != 0)) {
				{
				{
				setState(210);
				scheduleProperty();
				}
				}
				setState(215);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(216);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EventTriggerDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<EventTriggerPropertyContext> eventTriggerProperty() {
			return getRuleContexts(EventTriggerPropertyContext.class);
		}
		public EventTriggerPropertyContext eventTriggerProperty(int i) {
			return getRuleContext(EventTriggerPropertyContext.class,i);
		}
		public EventTriggerDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventTriggerDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterEventTriggerDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitEventTriggerDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitEventTriggerDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventTriggerDefinitionContext eventTriggerDefinition() throws RecognitionException {
		EventTriggerDefinitionContext _localctx = new EventTriggerDefinitionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_eventTriggerDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(218);
			match(T__14);
			setState(219);
			match(IDENTIFIER);
			setState(220);
			match(T__4);
			setState(224);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36029759091638273L) != 0)) {
				{
				{
				setState(221);
				eventTriggerProperty();
				}
				}
				setState(226);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(227);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplateDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<TemplatePropertyContext> templateProperty() {
			return getRuleContexts(TemplatePropertyContext.class);
		}
		public TemplatePropertyContext templateProperty(int i) {
			return getRuleContext(TemplatePropertyContext.class,i);
		}
		public TemplateDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterTemplateDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitTemplateDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitTemplateDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateDefinitionContext templateDefinition() throws RecognitionException {
		TemplateDefinitionContext _localctx = new TemplateDefinitionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_templateDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(T__15);
			setState(230);
			match(IDENTIFIER);
			setState(231);
			match(T__4);
			setState(235);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36032095553847297L) != 0)) {
				{
				{
				setState(232);
				templateProperty();
				}
				}
				setState(237);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(238);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContractDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<ContractPropertyContext> contractProperty() {
			return getRuleContexts(ContractPropertyContext.class);
		}
		public ContractPropertyContext contractProperty(int i) {
			return getRuleContext(ContractPropertyContext.class,i);
		}
		public ContractDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contractDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterContractDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitContractDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitContractDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContractDefinitionContext contractDefinition() throws RecognitionException {
		ContractDefinitionContext _localctx = new ContractDefinitionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_contractDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(240);
			match(T__16);
			setState(241);
			match(IDENTIFIER);
			setState(242);
			match(T__4);
			setState(246);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36041991158497281L) != 0)) {
				{
				{
				setState(243);
				contractProperty();
				}
				}
				setState(248);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(249);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PartyDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<PartyPropertyContext> partyProperty() {
			return getRuleContexts(PartyPropertyContext.class);
		}
		public PartyPropertyContext partyProperty(int i) {
			return getRuleContext(PartyPropertyContext.class,i);
		}
		public PartyDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partyDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterPartyDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitPartyDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitPartyDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PartyDefinitionContext partyDefinition() throws RecognitionException {
		PartyDefinitionContext _localctx = new PartyDefinitionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_partyDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			match(T__17);
			setState(252);
			match(IDENTIFIER);
			setState(253);
			match(T__4);
			setState(257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36081573577097217L) != 0)) {
				{
				{
				setState(254);
				partyProperty();
				}
				}
				setState(259);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(260);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FundDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<FundPropertyContext> fundProperty() {
			return getRuleContexts(FundPropertyContext.class);
		}
		public FundPropertyContext fundProperty(int i) {
			return getRuleContext(FundPropertyContext.class,i);
		}
		public FundDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fundDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterFundDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitFundDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitFundDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FundDefinitionContext fundDefinition() throws RecognitionException {
		FundDefinitionContext _localctx = new FundDefinitionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_fundDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(T__18);
			setState(263);
			match(IDENTIFIER);
			setState(264);
			match(T__4);
			setState(268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36239903251496961L) != 0)) {
				{
				{
				setState(265);
				fundProperty();
				}
				}
				setState(270);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(271);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PortfolioDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<PortfolioPropertyContext> portfolioProperty() {
			return getRuleContexts(PortfolioPropertyContext.class);
		}
		public PortfolioPropertyContext portfolioProperty(int i) {
			return getRuleContext(PortfolioPropertyContext.class,i);
		}
		public PortfolioDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_portfolioDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterPortfolioDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitPortfolioDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitPortfolioDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PortfolioDefinitionContext portfolioDefinition() throws RecognitionException {
		PortfolioDefinitionContext _localctx = new PortfolioDefinitionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_portfolioDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			match(T__19);
			setState(274);
			match(IDENTIFIER);
			setState(275);
			match(T__4);
			setState(279);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36310271995674625L) != 0)) {
				{
				{
				setState(276);
				portfolioProperty();
				}
				}
				setState(281);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(282);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CapitalStackDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<CapitalStackPropertyContext> capitalStackProperty() {
			return getRuleContexts(CapitalStackPropertyContext.class);
		}
		public CapitalStackPropertyContext capitalStackProperty(int i) {
			return getRuleContext(CapitalStackPropertyContext.class,i);
		}
		public CapitalStackDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_capitalStackDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterCapitalStackDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitCapitalStackDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitCapitalStackDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CapitalStackDefinitionContext capitalStackDefinition() throws RecognitionException {
		CapitalStackDefinitionContext _localctx = new CapitalStackDefinitionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_capitalStackDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			match(T__20);
			setState(285);
			match(IDENTIFIER);
			setState(286);
			match(T__4);
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 36591746972385281L) != 0)) {
				{
				{
				setState(287);
				capitalStackProperty();
				}
				}
				setState(292);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(293);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WaterFallDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<WaterFallPropertyContext> waterFallProperty() {
			return getRuleContexts(WaterFallPropertyContext.class);
		}
		public WaterFallPropertyContext waterFallProperty(int i) {
			return getRuleContext(WaterFallPropertyContext.class,i);
		}
		public WaterFallDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_waterFallDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterWaterFallDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitWaterFallDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitWaterFallDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WaterFallDefinitionContext waterFallDefinition() throws RecognitionException {
		WaterFallDefinitionContext _localctx = new WaterFallDefinitionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_waterFallDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(T__21);
			setState(296);
			match(IDENTIFIER);
			setState(297);
			match(T__4);
			setState(301);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 37154696925806593L) != 0)) {
				{
				{
				setState(298);
				waterFallProperty();
				}
				}
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(304);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetricDefinitionContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<MetricPropertyContext> metricProperty() {
			return getRuleContexts(MetricPropertyContext.class);
		}
		public MetricPropertyContext metricProperty(int i) {
			return getRuleContext(MetricPropertyContext.class,i);
		}
		public MetricDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metricDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterMetricDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitMetricDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitMetricDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetricDefinitionContext metricDefinition() throws RecognitionException {
		MetricDefinitionContext _localctx = new MetricDefinitionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_metricDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			match(T__22);
			setState(307);
			match(IDENTIFIER);
			setState(308);
			match(T__4);
			setState(312);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 38280596849426433L) != 0)) {
				{
				{
				setState(309);
				metricProperty();
				}
				}
				setState(314);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(315);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DealPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode DEAL_TYPE() { return getToken(cfdlParser.DEAL_TYPE, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(cfdlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(cfdlParser.IDENTIFIER, i);
		}
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public DealPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dealProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterDealProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitDealProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitDealProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DealPropertyContext dealProperty() throws RecognitionException {
		DealPropertyContext _localctx = new DealPropertyContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_dealProperty);
		int _la;
		try {
			setState(372);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(317);
				match(T__23);
				setState(318);
				match(T__24);
				setState(319);
				match(STRING);
				setState(320);
				match(T__25);
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 2);
				{
				setState(321);
				match(T__26);
				setState(322);
				match(T__24);
				setState(323);
				match(DEAL_TYPE);
				setState(324);
				match(T__25);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 3);
				{
				setState(325);
				match(T__27);
				setState(326);
				match(T__24);
				setState(327);
				match(STRING);
				setState(328);
				match(T__25);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 4);
				{
				setState(329);
				match(T__28);
				setState(330);
				match(T__24);
				setState(331);
				match(STRING);
				setState(332);
				match(T__25);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 5);
				{
				setState(333);
				match(T__29);
				setState(334);
				match(T__24);
				setState(335);
				match(STRING);
				setState(336);
				match(T__25);
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 6);
				{
				setState(337);
				match(T__30);
				setState(338);
				match(T__24);
				setState(339);
				match(STRING);
				setState(340);
				match(T__25);
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 7);
				{
				setState(341);
				match(T__31);
				setState(342);
				match(T__24);
				setState(343);
				match(NUMBER);
				setState(344);
				match(T__25);
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 8);
				{
				setState(345);
				match(T__32);
				setState(346);
				match(T__24);
				setState(347);
				match(T__33);
				setState(348);
				match(IDENTIFIER);
				setState(353);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(349);
					match(T__34);
					setState(350);
					match(IDENTIFIER);
					}
					}
					setState(355);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(356);
				match(T__35);
				setState(357);
				match(T__25);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 9);
				{
				setState(358);
				match(T__36);
				setState(359);
				match(T__24);
				setState(360);
				match(T__33);
				setState(361);
				match(IDENTIFIER);
				setState(366);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(362);
					match(T__34);
					setState(363);
					match(IDENTIFIER);
					}
					}
					setState(368);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(369);
				match(T__35);
				setState(370);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 10);
				{
				setState(371);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssetPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(cfdlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(cfdlParser.IDENTIFIER, i);
		}
		public TerminalNode ASSET_CATEGORY() { return getToken(cfdlParser.ASSET_CATEGORY, 0); }
		public TerminalNode ASSET_STATE() { return getToken(cfdlParser.ASSET_STATE, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public AssetPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assetProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterAssetProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitAssetProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitAssetProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssetPropertyContext assetProperty() throws RecognitionException {
		AssetPropertyContext _localctx = new AssetPropertyContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_assetProperty);
		int _la;
		try {
			setState(421);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(374);
				match(T__23);
				setState(375);
				match(T__24);
				setState(376);
				match(STRING);
				setState(377);
				match(T__25);
				}
				break;
			case T__37:
				enterOuterAlt(_localctx, 2);
				{
				setState(378);
				match(T__37);
				setState(379);
				match(T__24);
				setState(380);
				match(IDENTIFIER);
				setState(381);
				match(T__25);
				}
				break;
			case T__38:
				enterOuterAlt(_localctx, 3);
				{
				setState(382);
				match(T__38);
				setState(383);
				match(T__24);
				setState(384);
				match(ASSET_CATEGORY);
				setState(385);
				match(T__25);
				}
				break;
			case T__39:
				enterOuterAlt(_localctx, 4);
				{
				setState(386);
				match(T__39);
				setState(387);
				match(T__24);
				setState(388);
				match(STRING);
				setState(389);
				match(T__25);
				}
				break;
			case T__40:
				enterOuterAlt(_localctx, 5);
				{
				setState(390);
				match(T__40);
				setState(391);
				match(T__24);
				setState(392);
				match(T__33);
				setState(393);
				match(IDENTIFIER);
				setState(398);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(394);
					match(T__34);
					setState(395);
					match(IDENTIFIER);
					}
					}
					setState(400);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(401);
				match(T__35);
				setState(402);
				match(T__25);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 6);
				{
				setState(403);
				match(T__36);
				setState(404);
				match(T__24);
				setState(405);
				match(T__33);
				setState(406);
				match(IDENTIFIER);
				setState(411);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(407);
					match(T__34);
					setState(408);
					match(IDENTIFIER);
					}
					}
					setState(413);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(414);
				match(T__35);
				setState(415);
				match(T__25);
				}
				break;
			case T__41:
				enterOuterAlt(_localctx, 7);
				{
				setState(416);
				match(T__41);
				setState(417);
				match(T__24);
				setState(418);
				match(ASSET_STATE);
				setState(419);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 8);
				{
				setState(420);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ComponentPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(cfdlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(cfdlParser.IDENTIFIER, i);
		}
		public TerminalNode COMPONENT_SCOPE() { return getToken(cfdlParser.COMPONENT_SCOPE, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public ComponentPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_componentProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterComponentProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitComponentProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitComponentProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComponentPropertyContext componentProperty() throws RecognitionException {
		ComponentPropertyContext _localctx = new ComponentPropertyContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_componentProperty);
		int _la;
		try {
			setState(449);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(423);
				match(T__23);
				setState(424);
				match(T__24);
				setState(425);
				match(STRING);
				setState(426);
				match(T__25);
				}
				break;
			case T__42:
				enterOuterAlt(_localctx, 2);
				{
				setState(427);
				match(T__42);
				setState(428);
				match(T__24);
				setState(429);
				match(IDENTIFIER);
				setState(430);
				match(T__25);
				}
				break;
			case T__43:
				enterOuterAlt(_localctx, 3);
				{
				setState(431);
				match(T__43);
				setState(432);
				match(T__24);
				setState(433);
				match(COMPONENT_SCOPE);
				setState(434);
				match(T__25);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 4);
				{
				setState(435);
				match(T__36);
				setState(436);
				match(T__24);
				setState(437);
				match(T__33);
				setState(438);
				match(IDENTIFIER);
				setState(443);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(439);
					match(T__34);
					setState(440);
					match(IDENTIFIER);
					}
					}
					setState(445);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(446);
				match(T__35);
				setState(447);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(448);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StreamPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode COMPONENT_SCOPE() { return getToken(cfdlParser.COMPONENT_SCOPE, 0); }
		public TerminalNode STREAM_CATEGORY() { return getToken(cfdlParser.STREAM_CATEGORY, 0); }
		public TerminalNode STREAM_SUB_TYPE() { return getToken(cfdlParser.STREAM_SUB_TYPE, 0); }
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public TerminalNode AMOUNT_TYPE() { return getToken(cfdlParser.AMOUNT_TYPE, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public StreamPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_streamProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterStreamProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitStreamProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitStreamProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StreamPropertyContext streamProperty() throws RecognitionException {
		StreamPropertyContext _localctx = new StreamPropertyContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_streamProperty);
		int _la;
		try {
			setState(480);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(451);
				match(T__23);
				setState(452);
				match(T__24);
				setState(453);
				match(STRING);
				setState(454);
				match(T__25);
				}
				break;
			case T__43:
				enterOuterAlt(_localctx, 2);
				{
				setState(455);
				match(T__43);
				setState(456);
				match(T__24);
				setState(457);
				match(COMPONENT_SCOPE);
				setState(458);
				match(T__25);
				}
				break;
			case T__38:
				enterOuterAlt(_localctx, 3);
				{
				setState(459);
				match(T__38);
				setState(460);
				match(T__24);
				setState(461);
				match(STREAM_CATEGORY);
				setState(462);
				match(T__25);
				}
				break;
			case T__44:
				enterOuterAlt(_localctx, 4);
				{
				setState(463);
				match(T__44);
				setState(464);
				match(T__24);
				setState(465);
				match(STREAM_SUB_TYPE);
				setState(466);
				match(T__25);
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 5);
				{
				setState(467);
				match(T__13);
				setState(468);
				match(T__24);
				setState(469);
				match(IDENTIFIER);
				setState(470);
				match(T__25);
				}
				break;
			case T__45:
				enterOuterAlt(_localctx, 6);
				{
				setState(471);
				match(T__45);
				setState(472);
				match(T__24);
				setState(473);
				_la = _input.LA(1);
				if ( !(_la==IDENTIFIER || _la==NUMBER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(474);
				match(T__25);
				}
				break;
			case T__46:
				enterOuterAlt(_localctx, 7);
				{
				setState(475);
				match(T__46);
				setState(476);
				match(T__24);
				setState(477);
				match(AMOUNT_TYPE);
				setState(478);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 8);
				{
				setState(479);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssumptionPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode ASSUMPTION_CATEGORY() { return getToken(cfdlParser.ASSUMPTION_CATEGORY, 0); }
		public TerminalNode COMPONENT_SCOPE() { return getToken(cfdlParser.COMPONENT_SCOPE, 0); }
		public TerminalNode ASSUMPTION_TYPE() { return getToken(cfdlParser.ASSUMPTION_TYPE, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public TerminalNode DISTRIBUTION_TYPE() { return getToken(cfdlParser.DISTRIBUTION_TYPE, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public AssumptionPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assumptionProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterAssumptionProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitAssumptionProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitAssumptionProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssumptionPropertyContext assumptionProperty() throws RecognitionException {
		AssumptionPropertyContext _localctx = new AssumptionPropertyContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_assumptionProperty);
		int _la;
		try {
			setState(507);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(482);
				match(T__23);
				setState(483);
				match(T__24);
				setState(484);
				match(STRING);
				setState(485);
				match(T__25);
				}
				break;
			case T__38:
				enterOuterAlt(_localctx, 2);
				{
				setState(486);
				match(T__38);
				setState(487);
				match(T__24);
				setState(488);
				match(ASSUMPTION_CATEGORY);
				setState(489);
				match(T__25);
				}
				break;
			case T__43:
				enterOuterAlt(_localctx, 3);
				{
				setState(490);
				match(T__43);
				setState(491);
				match(T__24);
				setState(492);
				match(COMPONENT_SCOPE);
				setState(493);
				match(T__25);
				}
				break;
			case T__47:
				enterOuterAlt(_localctx, 4);
				{
				setState(494);
				match(T__47);
				setState(495);
				match(T__24);
				setState(496);
				match(ASSUMPTION_TYPE);
				setState(497);
				match(T__25);
				}
				break;
			case T__48:
				enterOuterAlt(_localctx, 5);
				{
				setState(498);
				match(T__48);
				setState(499);
				match(T__24);
				setState(500);
				_la = _input.LA(1);
				if ( !(((((_la - 79)) & ~0x3f) == 0 && ((1L << (_la - 79)) & 7L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(501);
				match(T__25);
				}
				break;
			case T__49:
				enterOuterAlt(_localctx, 6);
				{
				setState(502);
				match(T__49);
				setState(503);
				match(T__24);
				setState(504);
				match(DISTRIBUTION_TYPE);
				setState(505);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 7);
				{
				setState(506);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogicBlockPropertyContext extends ParserRuleContext {
		public List<TerminalNode> STRING() { return getTokens(cfdlParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(cfdlParser.STRING, i);
		}
		public TerminalNode COMPONENT_SCOPE() { return getToken(cfdlParser.COMPONENT_SCOPE, 0); }
		public TerminalNode LOGIC_BLOCK_TYPE() { return getToken(cfdlParser.LOGIC_BLOCK_TYPE, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public LogicBlockPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicBlockProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterLogicBlockProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitLogicBlockProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitLogicBlockProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicBlockPropertyContext logicBlockProperty() throws RecognitionException {
		LogicBlockPropertyContext _localctx = new LogicBlockPropertyContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_logicBlockProperty);
		int _la;
		try {
			setState(560);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(509);
				match(T__23);
				setState(510);
				match(T__24);
				setState(511);
				match(STRING);
				setState(512);
				match(T__25);
				}
				break;
			case T__43:
				enterOuterAlt(_localctx, 2);
				{
				setState(513);
				match(T__43);
				setState(514);
				match(T__24);
				setState(515);
				match(COMPONENT_SCOPE);
				setState(516);
				match(T__25);
				}
				break;
			case T__47:
				enterOuterAlt(_localctx, 3);
				{
				setState(517);
				match(T__47);
				setState(518);
				match(T__24);
				setState(519);
				match(LOGIC_BLOCK_TYPE);
				setState(520);
				match(T__25);
				}
				break;
			case T__50:
				enterOuterAlt(_localctx, 4);
				{
				setState(521);
				match(T__50);
				setState(522);
				match(T__24);
				setState(523);
				match(STRING);
				setState(524);
				match(T__25);
				}
				break;
			case T__51:
				enterOuterAlt(_localctx, 5);
				{
				setState(525);
				match(T__51);
				setState(526);
				match(T__24);
				setState(527);
				match(STRING);
				setState(528);
				match(T__25);
				}
				break;
			case T__52:
				enterOuterAlt(_localctx, 6);
				{
				setState(529);
				match(T__52);
				setState(530);
				match(T__24);
				setState(531);
				match(T__33);
				setState(532);
				match(STRING);
				setState(537);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(533);
					match(T__34);
					setState(534);
					match(STRING);
					}
					}
					setState(539);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(540);
				match(T__35);
				setState(541);
				match(T__25);
				}
				break;
			case T__53:
				enterOuterAlt(_localctx, 7);
				{
				setState(542);
				match(T__53);
				setState(543);
				match(T__24);
				setState(544);
				match(T__33);
				setState(545);
				match(STRING);
				setState(550);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(546);
					match(T__34);
					setState(547);
					match(STRING);
					}
					}
					setState(552);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(553);
				match(T__35);
				setState(554);
				match(T__25);
				}
				break;
			case T__54:
				enterOuterAlt(_localctx, 8);
				{
				setState(555);
				match(T__54);
				setState(556);
				match(T__24);
				setState(557);
				match(NUMBER);
				setState(558);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 9);
				{
				setState(559);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RuleBlockPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode COMPONENT_SCOPE() { return getToken(cfdlParser.COMPONENT_SCOPE, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public RuleBlockPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleBlockProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterRuleBlockProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitRuleBlockProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitRuleBlockProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleBlockPropertyContext ruleBlockProperty() throws RecognitionException {
		RuleBlockPropertyContext _localctx = new RuleBlockPropertyContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_ruleBlockProperty);
		try {
			setState(579);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(562);
				match(T__23);
				setState(563);
				match(T__24);
				setState(564);
				match(STRING);
				setState(565);
				match(T__25);
				}
				break;
			case T__43:
				enterOuterAlt(_localctx, 2);
				{
				setState(566);
				match(T__43);
				setState(567);
				match(T__24);
				setState(568);
				match(COMPONENT_SCOPE);
				setState(569);
				match(T__25);
				}
				break;
			case T__55:
				enterOuterAlt(_localctx, 3);
				{
				setState(570);
				match(T__55);
				setState(571);
				match(T__24);
				setState(572);
				match(STRING);
				setState(573);
				match(T__25);
				}
				break;
			case T__56:
				enterOuterAlt(_localctx, 4);
				{
				setState(574);
				match(T__56);
				setState(575);
				match(T__24);
				setState(576);
				match(STRING);
				setState(577);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(578);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SchedulePropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode SCHEDULE_TYPE() { return getToken(cfdlParser.SCHEDULE_TYPE, 0); }
		public TerminalNode FREQUENCY() { return getToken(cfdlParser.FREQUENCY, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public SchedulePropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scheduleProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterScheduleProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitScheduleProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitScheduleProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SchedulePropertyContext scheduleProperty() throws RecognitionException {
		SchedulePropertyContext _localctx = new SchedulePropertyContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_scheduleProperty);
		try {
			setState(602);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(581);
				match(T__23);
				setState(582);
				match(T__24);
				setState(583);
				match(STRING);
				setState(584);
				match(T__25);
				}
				break;
			case T__47:
				enterOuterAlt(_localctx, 2);
				{
				setState(585);
				match(T__47);
				setState(586);
				match(T__24);
				setState(587);
				match(SCHEDULE_TYPE);
				setState(588);
				match(T__25);
				}
				break;
			case T__57:
				enterOuterAlt(_localctx, 3);
				{
				setState(589);
				match(T__57);
				setState(590);
				match(T__24);
				setState(591);
				match(FREQUENCY);
				setState(592);
				match(T__25);
				}
				break;
			case T__58:
				enterOuterAlt(_localctx, 4);
				{
				setState(593);
				match(T__58);
				setState(594);
				match(T__24);
				setState(595);
				match(STRING);
				setState(596);
				match(T__25);
				}
				break;
			case T__59:
				enterOuterAlt(_localctx, 5);
				{
				setState(597);
				match(T__59);
				setState(598);
				match(T__24);
				setState(599);
				match(STRING);
				setState(600);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 6);
				{
				setState(601);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EventTriggerPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode TRIGGER_OPERATOR() { return getToken(cfdlParser.TRIGGER_OPERATOR, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public EventTriggerPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventTriggerProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterEventTriggerProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitEventTriggerProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitEventTriggerProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventTriggerPropertyContext eventTriggerProperty() throws RecognitionException {
		EventTriggerPropertyContext _localctx = new EventTriggerPropertyContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_eventTriggerProperty);
		try {
			setState(621);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(604);
				match(T__23);
				setState(605);
				match(T__24);
				setState(606);
				match(STRING);
				setState(607);
				match(T__25);
				}
				break;
			case T__60:
				enterOuterAlt(_localctx, 2);
				{
				setState(608);
				match(T__60);
				setState(609);
				match(T__24);
				setState(610);
				match(STRING);
				setState(611);
				match(T__25);
				}
				break;
			case T__61:
				enterOuterAlt(_localctx, 3);
				{
				setState(612);
				match(T__61);
				setState(613);
				match(T__24);
				setState(614);
				match(TRIGGER_OPERATOR);
				setState(615);
				match(T__25);
				}
				break;
			case T__62:
				enterOuterAlt(_localctx, 4);
				{
				setState(616);
				match(T__62);
				setState(617);
				match(T__24);
				setState(618);
				match(NUMBER);
				setState(619);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(620);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TemplatePropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode TEMPLATE_TYPE() { return getToken(cfdlParser.TEMPLATE_TYPE, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public TemplatePropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterTemplateProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitTemplateProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitTemplateProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplatePropertyContext templateProperty() throws RecognitionException {
		TemplatePropertyContext _localctx = new TemplatePropertyContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_templateProperty);
		try {
			setState(636);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(623);
				match(T__23);
				setState(624);
				match(T__24);
				setState(625);
				match(STRING);
				setState(626);
				match(T__25);
				}
				break;
			case T__63:
				enterOuterAlt(_localctx, 2);
				{
				setState(627);
				match(T__63);
				setState(628);
				match(T__24);
				setState(629);
				match(TEMPLATE_TYPE);
				setState(630);
				match(T__25);
				}
				break;
			case T__64:
				enterOuterAlt(_localctx, 3);
				{
				setState(631);
				match(T__64);
				setState(632);
				match(T__24);
				setState(633);
				match(STRING);
				setState(634);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(635);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContractPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(cfdlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(cfdlParser.IDENTIFIER, i);
		}
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public ContractPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_contractProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterContractProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitContractProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitContractProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContractPropertyContext contractProperty() throws RecognitionException {
		ContractPropertyContext _localctx = new ContractPropertyContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_contractProperty);
		int _la;
		try {
			setState(660);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(638);
				match(T__23);
				setState(639);
				match(T__24);
				setState(640);
				match(STRING);
				setState(641);
				match(T__25);
				}
				break;
			case T__65:
				enterOuterAlt(_localctx, 2);
				{
				setState(642);
				match(T__65);
				setState(643);
				match(T__24);
				setState(644);
				match(T__33);
				setState(645);
				match(IDENTIFIER);
				setState(650);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(646);
					match(T__34);
					setState(647);
					match(IDENTIFIER);
					}
					}
					setState(652);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(653);
				match(T__35);
				setState(654);
				match(T__25);
				}
				break;
			case T__66:
				enterOuterAlt(_localctx, 3);
				{
				setState(655);
				match(T__66);
				setState(656);
				match(T__24);
				setState(657);
				match(STRING);
				setState(658);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(659);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PartyPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode PARTY_ROLE() { return getToken(cfdlParser.PARTY_ROLE, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public PartyPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partyProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterPartyProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitPartyProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitPartyProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PartyPropertyContext partyProperty() throws RecognitionException {
		PartyPropertyContext _localctx = new PartyPropertyContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_partyProperty);
		try {
			setState(675);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(662);
				match(T__23);
				setState(663);
				match(T__24);
				setState(664);
				match(STRING);
				setState(665);
				match(T__25);
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 2);
				{
				setState(666);
				match(T__67);
				setState(667);
				match(T__24);
				setState(668);
				match(PARTY_ROLE);
				setState(669);
				match(T__25);
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 3);
				{
				setState(670);
				match(T__68);
				setState(671);
				match(T__24);
				setState(672);
				match(STRING);
				setState(673);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(674);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FundPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode FUND_PARTICIPANT_ROLE() { return getToken(cfdlParser.FUND_PARTICIPANT_ROLE, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public FundPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fundProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterFundProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitFundProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitFundProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FundPropertyContext fundProperty() throws RecognitionException {
		FundPropertyContext _localctx = new FundPropertyContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_fundProperty);
		try {
			setState(690);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(677);
				match(T__23);
				setState(678);
				match(T__24);
				setState(679);
				match(STRING);
				setState(680);
				match(T__25);
				}
				break;
			case T__69:
				enterOuterAlt(_localctx, 2);
				{
				setState(681);
				match(T__69);
				setState(682);
				match(T__24);
				setState(683);
				match(FUND_PARTICIPANT_ROLE);
				setState(684);
				match(T__25);
				}
				break;
			case T__70:
				enterOuterAlt(_localctx, 3);
				{
				setState(685);
				match(T__70);
				setState(686);
				match(T__24);
				setState(687);
				match(NUMBER);
				setState(688);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(689);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PortfolioPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(cfdlParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(cfdlParser.IDENTIFIER, i);
		}
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public PortfolioPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_portfolioProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterPortfolioProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitPortfolioProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitPortfolioProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PortfolioPropertyContext portfolioProperty() throws RecognitionException {
		PortfolioPropertyContext _localctx = new PortfolioPropertyContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_portfolioProperty);
		int _la;
		try {
			setState(710);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(692);
				match(T__23);
				setState(693);
				match(T__24);
				setState(694);
				match(STRING);
				setState(695);
				match(T__25);
				}
				break;
			case T__71:
				enterOuterAlt(_localctx, 2);
				{
				setState(696);
				match(T__71);
				setState(697);
				match(T__24);
				setState(698);
				match(T__33);
				setState(699);
				match(IDENTIFIER);
				setState(704);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(700);
					match(T__34);
					setState(701);
					match(IDENTIFIER);
					}
					}
					setState(706);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(707);
				match(T__35);
				setState(708);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(709);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CapitalStackPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public CapitalStackPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_capitalStackProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterCapitalStackProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitCapitalStackProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitCapitalStackProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CapitalStackPropertyContext capitalStackProperty() throws RecognitionException {
		CapitalStackPropertyContext _localctx = new CapitalStackPropertyContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_capitalStackProperty);
		try {
			setState(721);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(712);
				match(T__23);
				setState(713);
				match(T__24);
				setState(714);
				match(STRING);
				setState(715);
				match(T__25);
				}
				break;
			case T__72:
				enterOuterAlt(_localctx, 2);
				{
				setState(716);
				match(T__72);
				setState(717);
				match(T__24);
				setState(718);
				match(NUMBER);
				setState(719);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(720);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WaterFallPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public WaterFallPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_waterFallProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterWaterFallProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitWaterFallProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitWaterFallProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WaterFallPropertyContext waterFallProperty() throws RecognitionException {
		WaterFallPropertyContext _localctx = new WaterFallPropertyContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_waterFallProperty);
		try {
			setState(732);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(723);
				match(T__23);
				setState(724);
				match(T__24);
				setState(725);
				match(STRING);
				setState(726);
				match(T__25);
				}
				break;
			case T__73:
				enterOuterAlt(_localctx, 2);
				{
				setState(727);
				match(T__73);
				setState(728);
				match(T__24);
				setState(729);
				match(STRING);
				setState(730);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(731);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MetricPropertyContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode METRIC_TYPE() { return getToken(cfdlParser.METRIC_TYPE, 0); }
		public TerminalNode PAYBACK_UNIT() { return getToken(cfdlParser.PAYBACK_UNIT, 0); }
		public GenericPropertyContext genericProperty() {
			return getRuleContext(GenericPropertyContext.class,0);
		}
		public MetricPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_metricProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterMetricProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitMetricProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitMetricProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MetricPropertyContext metricProperty() throws RecognitionException {
		MetricPropertyContext _localctx = new MetricPropertyContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_metricProperty);
		int _la;
		try {
			setState(747);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(734);
				match(T__23);
				setState(735);
				match(T__24);
				setState(736);
				match(STRING);
				setState(737);
				match(T__25);
				}
				break;
			case T__47:
				enterOuterAlt(_localctx, 2);
				{
				setState(738);
				match(T__47);
				setState(739);
				match(T__24);
				setState(740);
				match(METRIC_TYPE);
				setState(741);
				match(T__25);
				}
				break;
			case T__74:
				enterOuterAlt(_localctx, 3);
				{
				setState(742);
				match(T__74);
				setState(743);
				match(T__24);
				setState(744);
				_la = _input.LA(1);
				if ( !(_la==STRING || _la==PAYBACK_UNIT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(745);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(746);
				genericProperty();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GenericPropertyContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public GenericPropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericProperty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterGenericProperty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitGenericProperty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitGenericProperty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GenericPropertyContext genericProperty() throws RecognitionException {
		GenericPropertyContext _localctx = new GenericPropertyContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_genericProperty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(749);
			match(IDENTIFIER);
			setState(750);
			match(T__24);
			setState(751);
			value();
			setState(752);
			match(T__25);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public PropertyAssignContext propertyAssign() {
			return getRuleContext(PropertyAssignContext.class,0);
		}
		public LogicBlockDefContext logicBlockDef() {
			return getRuleContext(LogicBlockDefContext.class,0);
		}
		public ReferenceDeclContext referenceDecl() {
			return getRuleContext(ReferenceDeclContext.class,0);
		}
		public ImportDeclContext importDecl() {
			return getRuleContext(ImportDeclContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_statement);
		try {
			setState(764);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(754);
				propertyAssign();
				setState(755);
				match(T__25);
				}
				break;
			case T__75:
				enterOuterAlt(_localctx, 2);
				{
				setState(757);
				logicBlockDef();
				}
				break;
			case T__76:
				enterOuterAlt(_localctx, 3);
				{
				setState(758);
				referenceDecl();
				setState(759);
				match(T__25);
				}
				break;
			case T__77:
				enterOuterAlt(_localctx, 4);
				{
				setState(761);
				importDecl();
				setState(762);
				match(T__25);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyDefContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public TypeRefContext typeRef() {
			return getRuleContext(TypeRefContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PropertyDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterPropertyDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitPropertyDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitPropertyDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyDefContext propertyDef() throws RecognitionException {
		PropertyDefContext _localctx = new PropertyDefContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_propertyDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(766);
			match(IDENTIFIER);
			setState(767);
			match(T__24);
			setState(768);
			typeRef();
			setState(771);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__24) {
				{
				setState(769);
				match(T__24);
				setState(770);
				value();
				}
			}

			setState(773);
			match(T__25);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PropertyAssignContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public PropertyAssignContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyAssign; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterPropertyAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitPropertyAssign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitPropertyAssign(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyAssignContext propertyAssign() throws RecognitionException {
		PropertyAssignContext _localctx = new PropertyAssignContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_propertyAssign);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(775);
			match(IDENTIFIER);
			setState(776);
			match(T__24);
			setState(777);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LogicBlockDefContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public LogicBlockDefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicBlockDef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterLogicBlockDef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitLogicBlockDef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitLogicBlockDef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicBlockDefContext logicBlockDef() throws RecognitionException {
		LogicBlockDefContext _localctx = new LogicBlockDefContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_logicBlockDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(779);
			match(T__75);
			setState(780);
			match(IDENTIFIER);
			setState(781);
			match(T__4);
			setState(785);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 15L) != 0)) {
				{
				{
				setState(782);
				statement();
				}
				}
				setState(787);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(788);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReferenceDeclContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public ReferenceDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_referenceDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterReferenceDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitReferenceDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitReferenceDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferenceDeclContext referenceDecl() throws RecognitionException {
		ReferenceDeclContext _localctx = new ReferenceDeclContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_referenceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(790);
			match(T__76);
			setState(791);
			match(T__24);
			setState(792);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportDeclContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public ImportDeclContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDecl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterImportDecl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitImportDecl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitImportDecl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDeclContext importDecl() throws RecognitionException {
		ImportDeclContext _localctx = new ImportDeclContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_importDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(794);
			match(T__77);
			setState(795);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeRefContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public TypeRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterTypeRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitTypeRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitTypeRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeRefContext typeRef() throws RecognitionException {
		TypeRefContext _localctx = new TypeRefContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_typeRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(797);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public TerminalNode STRING() { return getToken(cfdlParser.STRING, 0); }
		public TerminalNode BOOLEAN() { return getToken(cfdlParser.BOOLEAN, 0); }
		public List<ValueContext> value() {
			return getRuleContexts(ValueContext.class);
		}
		public ValueContext value(int i) {
			return getRuleContext(ValueContext.class,i);
		}
		public List<PropertyAssignContext> propertyAssign() {
			return getRuleContexts(PropertyAssignContext.class);
		}
		public PropertyAssignContext propertyAssign(int i) {
			return getRuleContext(PropertyAssignContext.class,i);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_value);
		int _la;
		try {
			setState(824);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(799);
				match(NUMBER);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(800);
				match(STRING);
				}
				break;
			case BOOLEAN:
				enterOuterAlt(_localctx, 3);
				{
				setState(801);
				match(BOOLEAN);
				}
				break;
			case T__33:
				enterOuterAlt(_localctx, 4);
				{
				setState(802);
				match(T__33);
				setState(803);
				value();
				setState(808);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(804);
					match(T__34);
					setState(805);
					value();
					}
					}
					setState(810);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(811);
				match(T__35);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 5);
				{
				setState(813);
				match(T__4);
				setState(814);
				propertyAssign();
				setState(819);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(815);
					match(T__34);
					setState(816);
					propertyAssign();
					}
					}
					setState(821);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(822);
				match(T__5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001n\u033b\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
		"-\u0007-\u0001\u0000\u0004\u0000^\b\u0000\u000b\u0000\f\u0000_\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0003\u0001v\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0005\u0002|\b\u0002\n\u0002\f\u0002\u007f\t\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005"+
		"\u0003\u0087\b\u0003\n\u0003\f\u0003\u008a\t\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u0092\b\u0004"+
		"\n\u0004\f\u0004\u0095\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u009d\b\u0005\n\u0005\f\u0005"+
		"\u00a0\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0005\u0006\u00a8\b\u0006\n\u0006\f\u0006\u00ab\t\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005"+
		"\u0007\u00b3\b\u0007\n\u0007\f\u0007\u00b6\t\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0005\b\u00be\b\b\n\b\f\b\u00c1\t\b\u0001"+
		"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0005\t\u00c9\b\t\n\t\f\t\u00cc"+
		"\t\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u00d4\b\n"+
		"\n\n\f\n\u00d7\t\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0005\u000b\u00df\b\u000b\n\u000b\f\u000b\u00e2\t\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00ea\b\f\n"+
		"\f\f\f\u00ed\t\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0005"+
		"\r\u00f5\b\r\n\r\f\r\u00f8\t\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0005\u000e\u0100\b\u000e\n\u000e\f\u000e\u0103"+
		"\t\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0005\u000f\u010b\b\u000f\n\u000f\f\u000f\u010e\t\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010"+
		"\u0116\b\u0010\n\u0010\f\u0010\u0119\t\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u0121\b\u0011\n"+
		"\u0011\f\u0011\u0124\t\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u012c\b\u0012\n\u0012\f\u0012"+
		"\u012f\t\u0012\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0005\u0013\u0137\b\u0013\n\u0013\f\u0013\u013a\t\u0013\u0001"+
		"\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005"+
		"\u0014\u0160\b\u0014\n\u0014\f\u0014\u0163\t\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0005\u0014\u016d\b\u0014\n\u0014\f\u0014\u0170\t\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0003\u0014\u0175\b\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0005\u0015\u018d\b\u0015\n\u0015\f\u0015\u0190\t\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0005\u0015\u019a\b\u0015\n\u0015\f\u0015\u019d"+
		"\t\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0003\u0015\u01a6\b\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u01ba\b\u0016\n"+
		"\u0016\f\u0016\u01bd\t\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003"+
		"\u0016\u01c2\b\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001"+
		"\u0017\u0003\u0017\u01e1\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u01fc\b\u0018\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0005\u0019\u0218\b\u0019\n\u0019\f\u0019\u021b\t\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0005\u0019\u0225\b\u0019\n\u0019\f\u0019\u0228"+
		"\t\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u0231\b\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u0244\b\u001a\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0003\u001b\u025b\b\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u026e\b\u001c\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003"+
		"\u001d\u027d\b\u001d\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0005"+
		"\u001e\u0289\b\u001e\n\u001e\f\u001e\u028c\t\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0001\u001e\u0003\u001e"+
		"\u0295\b\u001e\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0003\u001f\u02a4\b\u001f\u0001 \u0001 \u0001"+
		" \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001"+
		" \u0003 \u02b3\b \u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001"+
		"!\u0001!\u0001!\u0005!\u02bf\b!\n!\f!\u02c2\t!\u0001!\u0001!\u0001!\u0003"+
		"!\u02c7\b!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0003\"\u02d2\b\"\u0001#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001#\u0001#\u0003#\u02dd\b#\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0003$\u02ec"+
		"\b$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001&\u0001&\u0001&\u0001&\u0001"+
		"&\u0001&\u0001&\u0001&\u0001&\u0001&\u0003&\u02fd\b&\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0003\'\u0304\b\'\u0001\'\u0001\'\u0001(\u0001(\u0001"+
		"(\u0001(\u0001)\u0001)\u0001)\u0001)\u0005)\u0310\b)\n)\f)\u0313\t)\u0001"+
		")\u0001)\u0001*\u0001*\u0001*\u0001*\u0001+\u0001+\u0001+\u0001,\u0001"+
		",\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0005-\u0327\b-\n-"+
		"\f-\u032a\t-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0005-\u0332\b"+
		"-\n-\f-\u0335\t-\u0001-\u0001-\u0003-\u0339\b-\u0001-\u0000\u0000.\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\u0000\u0004\u0001\u0000\u0001\u0004"+
		"\u0001\u0000OP\u0001\u0000OQ\u0002\u0000QQkk\u038f\u0000]\u0001\u0000"+
		"\u0000\u0000\u0002u\u0001\u0000\u0000\u0000\u0004w\u0001\u0000\u0000\u0000"+
		"\u0006\u0082\u0001\u0000\u0000\u0000\b\u008d\u0001\u0000\u0000\u0000\n"+
		"\u0098\u0001\u0000\u0000\u0000\f\u00a3\u0001\u0000\u0000\u0000\u000e\u00ae"+
		"\u0001\u0000\u0000\u0000\u0010\u00b9\u0001\u0000\u0000\u0000\u0012\u00c4"+
		"\u0001\u0000\u0000\u0000\u0014\u00cf\u0001\u0000\u0000\u0000\u0016\u00da"+
		"\u0001\u0000\u0000\u0000\u0018\u00e5\u0001\u0000\u0000\u0000\u001a\u00f0"+
		"\u0001\u0000\u0000\u0000\u001c\u00fb\u0001\u0000\u0000\u0000\u001e\u0106"+
		"\u0001\u0000\u0000\u0000 \u0111\u0001\u0000\u0000\u0000\"\u011c\u0001"+
		"\u0000\u0000\u0000$\u0127\u0001\u0000\u0000\u0000&\u0132\u0001\u0000\u0000"+
		"\u0000(\u0174\u0001\u0000\u0000\u0000*\u01a5\u0001\u0000\u0000\u0000,"+
		"\u01c1\u0001\u0000\u0000\u0000.\u01e0\u0001\u0000\u0000\u00000\u01fb\u0001"+
		"\u0000\u0000\u00002\u0230\u0001\u0000\u0000\u00004\u0243\u0001\u0000\u0000"+
		"\u00006\u025a\u0001\u0000\u0000\u00008\u026d\u0001\u0000\u0000\u0000:"+
		"\u027c\u0001\u0000\u0000\u0000<\u0294\u0001\u0000\u0000\u0000>\u02a3\u0001"+
		"\u0000\u0000\u0000@\u02b2\u0001\u0000\u0000\u0000B\u02c6\u0001\u0000\u0000"+
		"\u0000D\u02d1\u0001\u0000\u0000\u0000F\u02dc\u0001\u0000\u0000\u0000H"+
		"\u02eb\u0001\u0000\u0000\u0000J\u02ed\u0001\u0000\u0000\u0000L\u02fc\u0001"+
		"\u0000\u0000\u0000N\u02fe\u0001\u0000\u0000\u0000P\u0307\u0001\u0000\u0000"+
		"\u0000R\u030b\u0001\u0000\u0000\u0000T\u0316\u0001\u0000\u0000\u0000V"+
		"\u031a\u0001\u0000\u0000\u0000X\u031d\u0001\u0000\u0000\u0000Z\u0338\u0001"+
		"\u0000\u0000\u0000\\^\u0003\u0002\u0001\u0000]\\\u0001\u0000\u0000\u0000"+
		"^_\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000"+
		"\u0000`a\u0001\u0000\u0000\u0000ab\u0005\u0000\u0000\u0001b\u0001\u0001"+
		"\u0000\u0000\u0000cv\u0003\u0006\u0003\u0000dv\u0003\b\u0004\u0000ev\u0003"+
		"\n\u0005\u0000fv\u0003\f\u0006\u0000gv\u0003\u000e\u0007\u0000hv\u0003"+
		"\u0010\b\u0000iv\u0003\u0012\t\u0000jv\u0003\u0014\n\u0000kv\u0003\u0016"+
		"\u000b\u0000lv\u0003\u0018\f\u0000mv\u0003\u001a\r\u0000nv\u0003\u001c"+
		"\u000e\u0000ov\u0003\u001e\u000f\u0000pv\u0003 \u0010\u0000qv\u0003\""+
		"\u0011\u0000rv\u0003$\u0012\u0000sv\u0003&\u0013\u0000tv\u0003\u0004\u0002"+
		"\u0000uc\u0001\u0000\u0000\u0000ud\u0001\u0000\u0000\u0000ue\u0001\u0000"+
		"\u0000\u0000uf\u0001\u0000\u0000\u0000ug\u0001\u0000\u0000\u0000uh\u0001"+
		"\u0000\u0000\u0000ui\u0001\u0000\u0000\u0000uj\u0001\u0000\u0000\u0000"+
		"uk\u0001\u0000\u0000\u0000ul\u0001\u0000\u0000\u0000um\u0001\u0000\u0000"+
		"\u0000un\u0001\u0000\u0000\u0000uo\u0001\u0000\u0000\u0000up\u0001\u0000"+
		"\u0000\u0000uq\u0001\u0000\u0000\u0000ur\u0001\u0000\u0000\u0000us\u0001"+
		"\u0000\u0000\u0000ut\u0001\u0000\u0000\u0000v\u0003\u0001\u0000\u0000"+
		"\u0000wx\u0007\u0000\u0000\u0000xy\u0005O\u0000\u0000y}\u0005\u0005\u0000"+
		"\u0000z|\u0003L&\u0000{z\u0001\u0000\u0000\u0000|\u007f\u0001\u0000\u0000"+
		"\u0000}{\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~\u0080\u0001"+
		"\u0000\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u0080\u0081\u0005\u0006"+
		"\u0000\u0000\u0081\u0005\u0001\u0000\u0000\u0000\u0082\u0083\u0005\u0007"+
		"\u0000\u0000\u0083\u0084\u0005O\u0000\u0000\u0084\u0088\u0005\u0005\u0000"+
		"\u0000\u0085\u0087\u0003(\u0014\u0000\u0086\u0085\u0001\u0000\u0000\u0000"+
		"\u0087\u008a\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000\u0000"+
		"\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008b\u0001\u0000\u0000\u0000"+
		"\u008a\u0088\u0001\u0000\u0000\u0000\u008b\u008c\u0005\u0006\u0000\u0000"+
		"\u008c\u0007\u0001\u0000\u0000\u0000\u008d\u008e\u0005\b\u0000\u0000\u008e"+
		"\u008f\u0005O\u0000\u0000\u008f\u0093\u0005\u0005\u0000\u0000\u0090\u0092"+
		"\u0003*\u0015\u0000\u0091\u0090\u0001\u0000\u0000\u0000\u0092\u0095\u0001"+
		"\u0000\u0000\u0000\u0093\u0091\u0001\u0000\u0000\u0000\u0093\u0094\u0001"+
		"\u0000\u0000\u0000\u0094\u0096\u0001\u0000\u0000\u0000\u0095\u0093\u0001"+
		"\u0000\u0000\u0000\u0096\u0097\u0005\u0006\u0000\u0000\u0097\t\u0001\u0000"+
		"\u0000\u0000\u0098\u0099\u0005\t\u0000\u0000\u0099\u009a\u0005O\u0000"+
		"\u0000\u009a\u009e\u0005\u0005\u0000\u0000\u009b\u009d\u0003,\u0016\u0000"+
		"\u009c\u009b\u0001\u0000\u0000\u0000\u009d\u00a0\u0001\u0000\u0000\u0000"+
		"\u009e\u009c\u0001\u0000\u0000\u0000\u009e\u009f\u0001\u0000\u0000\u0000"+
		"\u009f\u00a1\u0001\u0000\u0000\u0000\u00a0\u009e\u0001\u0000\u0000\u0000"+
		"\u00a1\u00a2\u0005\u0006\u0000\u0000\u00a2\u000b\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a4\u0005\n\u0000\u0000\u00a4\u00a5\u0005O\u0000\u0000\u00a5"+
		"\u00a9\u0005\u0005\u0000\u0000\u00a6\u00a8\u0003.\u0017\u0000\u00a7\u00a6"+
		"\u0001\u0000\u0000\u0000\u00a8\u00ab\u0001\u0000\u0000\u0000\u00a9\u00a7"+
		"\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000\u0000\u00aa\u00ac"+
		"\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000\u00ac\u00ad"+
		"\u0005\u0006\u0000\u0000\u00ad\r\u0001\u0000\u0000\u0000\u00ae\u00af\u0005"+
		"\u000b\u0000\u0000\u00af\u00b0\u0005O\u0000\u0000\u00b0\u00b4\u0005\u0005"+
		"\u0000\u0000\u00b1\u00b3\u00030\u0018\u0000\u00b2\u00b1\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b6\u0001\u0000\u0000\u0000\u00b4\u00b2\u0001\u0000\u0000"+
		"\u0000\u00b4\u00b5\u0001\u0000\u0000\u0000\u00b5\u00b7\u0001\u0000\u0000"+
		"\u0000\u00b6\u00b4\u0001\u0000\u0000\u0000\u00b7\u00b8\u0005\u0006\u0000"+
		"\u0000\u00b8\u000f\u0001\u0000\u0000\u0000\u00b9\u00ba\u0005\f\u0000\u0000"+
		"\u00ba\u00bb\u0005O\u0000\u0000\u00bb\u00bf\u0005\u0005\u0000\u0000\u00bc"+
		"\u00be\u00032\u0019\u0000\u00bd\u00bc\u0001\u0000\u0000\u0000\u00be\u00c1"+
		"\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001\u0000\u0000\u0000\u00bf\u00c0"+
		"\u0001\u0000\u0000\u0000\u00c0\u00c2\u0001\u0000\u0000\u0000\u00c1\u00bf"+
		"\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005\u0006\u0000\u0000\u00c3\u0011"+
		"\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005\r\u0000\u0000\u00c5\u00c6\u0005"+
		"O\u0000\u0000\u00c6\u00ca\u0005\u0005\u0000\u0000\u00c7\u00c9\u00034\u001a"+
		"\u0000\u00c8\u00c7\u0001\u0000\u0000\u0000\u00c9\u00cc\u0001\u0000\u0000"+
		"\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000\u00ca\u00cb\u0001\u0000\u0000"+
		"\u0000\u00cb\u00cd\u0001\u0000\u0000\u0000\u00cc\u00ca\u0001\u0000\u0000"+
		"\u0000\u00cd\u00ce\u0005\u0006\u0000\u0000\u00ce\u0013\u0001\u0000\u0000"+
		"\u0000\u00cf\u00d0\u0005\u000e\u0000\u0000\u00d0\u00d1\u0005O\u0000\u0000"+
		"\u00d1\u00d5\u0005\u0005\u0000\u0000\u00d2\u00d4\u00036\u001b\u0000\u00d3"+
		"\u00d2\u0001\u0000\u0000\u0000\u00d4\u00d7\u0001\u0000\u0000\u0000\u00d5"+
		"\u00d3\u0001\u0000\u0000\u0000\u00d5\u00d6\u0001\u0000\u0000\u0000\u00d6"+
		"\u00d8\u0001\u0000\u0000\u0000\u00d7\u00d5\u0001\u0000\u0000\u0000\u00d8"+
		"\u00d9\u0005\u0006\u0000\u0000\u00d9\u0015\u0001\u0000\u0000\u0000\u00da"+
		"\u00db\u0005\u000f\u0000\u0000\u00db\u00dc\u0005O\u0000\u0000\u00dc\u00e0"+
		"\u0005\u0005\u0000\u0000\u00dd\u00df\u00038\u001c\u0000\u00de\u00dd\u0001"+
		"\u0000\u0000\u0000\u00df\u00e2\u0001\u0000\u0000\u0000\u00e0\u00de\u0001"+
		"\u0000\u0000\u0000\u00e0\u00e1\u0001\u0000\u0000\u0000\u00e1\u00e3\u0001"+
		"\u0000\u0000\u0000\u00e2\u00e0\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005"+
		"\u0006\u0000\u0000\u00e4\u0017\u0001\u0000\u0000\u0000\u00e5\u00e6\u0005"+
		"\u0010\u0000\u0000\u00e6\u00e7\u0005O\u0000\u0000\u00e7\u00eb\u0005\u0005"+
		"\u0000\u0000\u00e8\u00ea\u0003:\u001d\u0000\u00e9\u00e8\u0001\u0000\u0000"+
		"\u0000\u00ea\u00ed\u0001\u0000\u0000\u0000\u00eb\u00e9\u0001\u0000\u0000"+
		"\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ee\u0001\u0000\u0000"+
		"\u0000\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ee\u00ef\u0005\u0006\u0000"+
		"\u0000\u00ef\u0019\u0001\u0000\u0000\u0000\u00f0\u00f1\u0005\u0011\u0000"+
		"\u0000\u00f1\u00f2\u0005O\u0000\u0000\u00f2\u00f6\u0005\u0005\u0000\u0000"+
		"\u00f3\u00f5\u0003<\u001e\u0000\u00f4\u00f3\u0001\u0000\u0000\u0000\u00f5"+
		"\u00f8\u0001\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000\u0000\u0000\u00f6"+
		"\u00f7\u0001\u0000\u0000\u0000\u00f7\u00f9\u0001\u0000\u0000\u0000\u00f8"+
		"\u00f6\u0001\u0000\u0000\u0000\u00f9\u00fa\u0005\u0006\u0000\u0000\u00fa"+
		"\u001b\u0001\u0000\u0000\u0000\u00fb\u00fc\u0005\u0012\u0000\u0000\u00fc"+
		"\u00fd\u0005O\u0000\u0000\u00fd\u0101\u0005\u0005\u0000\u0000\u00fe\u0100"+
		"\u0003>\u001f\u0000\u00ff\u00fe\u0001\u0000\u0000\u0000\u0100\u0103\u0001"+
		"\u0000\u0000\u0000\u0101\u00ff\u0001\u0000\u0000\u0000\u0101\u0102\u0001"+
		"\u0000\u0000\u0000\u0102\u0104\u0001\u0000\u0000\u0000\u0103\u0101\u0001"+
		"\u0000\u0000\u0000\u0104\u0105\u0005\u0006\u0000\u0000\u0105\u001d\u0001"+
		"\u0000\u0000\u0000\u0106\u0107\u0005\u0013\u0000\u0000\u0107\u0108\u0005"+
		"O\u0000\u0000\u0108\u010c\u0005\u0005\u0000\u0000\u0109\u010b\u0003@ "+
		"\u0000\u010a\u0109\u0001\u0000\u0000\u0000\u010b\u010e\u0001\u0000\u0000"+
		"\u0000\u010c\u010a\u0001\u0000\u0000\u0000\u010c\u010d\u0001\u0000\u0000"+
		"\u0000\u010d\u010f\u0001\u0000\u0000\u0000\u010e\u010c\u0001\u0000\u0000"+
		"\u0000\u010f\u0110\u0005\u0006\u0000\u0000\u0110\u001f\u0001\u0000\u0000"+
		"\u0000\u0111\u0112\u0005\u0014\u0000\u0000\u0112\u0113\u0005O\u0000\u0000"+
		"\u0113\u0117\u0005\u0005\u0000\u0000\u0114\u0116\u0003B!\u0000\u0115\u0114"+
		"\u0001\u0000\u0000\u0000\u0116\u0119\u0001\u0000\u0000\u0000\u0117\u0115"+
		"\u0001\u0000\u0000\u0000\u0117\u0118\u0001\u0000\u0000\u0000\u0118\u011a"+
		"\u0001\u0000\u0000\u0000\u0119\u0117\u0001\u0000\u0000\u0000\u011a\u011b"+
		"\u0005\u0006\u0000\u0000\u011b!\u0001\u0000\u0000\u0000\u011c\u011d\u0005"+
		"\u0015\u0000\u0000\u011d\u011e\u0005O\u0000\u0000\u011e\u0122\u0005\u0005"+
		"\u0000\u0000\u011f\u0121\u0003D\"\u0000\u0120\u011f\u0001\u0000\u0000"+
		"\u0000\u0121\u0124\u0001\u0000\u0000\u0000\u0122\u0120\u0001\u0000\u0000"+
		"\u0000\u0122\u0123\u0001\u0000\u0000\u0000\u0123\u0125\u0001\u0000\u0000"+
		"\u0000\u0124\u0122\u0001\u0000\u0000\u0000\u0125\u0126\u0005\u0006\u0000"+
		"\u0000\u0126#\u0001\u0000\u0000\u0000\u0127\u0128\u0005\u0016\u0000\u0000"+
		"\u0128\u0129\u0005O\u0000\u0000\u0129\u012d\u0005\u0005\u0000\u0000\u012a"+
		"\u012c\u0003F#\u0000\u012b\u012a\u0001\u0000\u0000\u0000\u012c\u012f\u0001"+
		"\u0000\u0000\u0000\u012d\u012b\u0001\u0000\u0000\u0000\u012d\u012e\u0001"+
		"\u0000\u0000\u0000\u012e\u0130\u0001\u0000\u0000\u0000\u012f\u012d\u0001"+
		"\u0000\u0000\u0000\u0130\u0131\u0005\u0006\u0000\u0000\u0131%\u0001\u0000"+
		"\u0000\u0000\u0132\u0133\u0005\u0017\u0000\u0000\u0133\u0134\u0005O\u0000"+
		"\u0000\u0134\u0138\u0005\u0005\u0000\u0000\u0135\u0137\u0003H$\u0000\u0136"+
		"\u0135\u0001\u0000\u0000\u0000\u0137\u013a\u0001\u0000\u0000\u0000\u0138"+
		"\u0136\u0001\u0000\u0000\u0000\u0138\u0139\u0001\u0000\u0000\u0000\u0139"+
		"\u013b\u0001\u0000\u0000\u0000\u013a\u0138\u0001\u0000\u0000\u0000\u013b"+
		"\u013c\u0005\u0006\u0000\u0000\u013c\'\u0001\u0000\u0000\u0000\u013d\u013e"+
		"\u0005\u0018\u0000\u0000\u013e\u013f\u0005\u0019\u0000\u0000\u013f\u0140"+
		"\u0005Q\u0000\u0000\u0140\u0175\u0005\u001a\u0000\u0000\u0141\u0142\u0005"+
		"\u001b\u0000\u0000\u0142\u0143\u0005\u0019\u0000\u0000\u0143\u0144\u0005"+
		"S\u0000\u0000\u0144\u0175\u0005\u001a\u0000\u0000\u0145\u0146\u0005\u001c"+
		"\u0000\u0000\u0146\u0147\u0005\u0019\u0000\u0000\u0147\u0148\u0005Q\u0000"+
		"\u0000\u0148\u0175\u0005\u001a\u0000\u0000\u0149\u014a\u0005\u001d\u0000"+
		"\u0000\u014a\u014b\u0005\u0019\u0000\u0000\u014b\u014c\u0005Q\u0000\u0000"+
		"\u014c\u0175\u0005\u001a\u0000\u0000\u014d\u014e\u0005\u001e\u0000\u0000"+
		"\u014e\u014f\u0005\u0019\u0000\u0000\u014f\u0150\u0005Q\u0000\u0000\u0150"+
		"\u0175\u0005\u001a\u0000\u0000\u0151\u0152\u0005\u001f\u0000\u0000\u0152"+
		"\u0153\u0005\u0019\u0000\u0000\u0153\u0154\u0005Q\u0000\u0000\u0154\u0175"+
		"\u0005\u001a\u0000\u0000\u0155\u0156\u0005 \u0000\u0000\u0156\u0157\u0005"+
		"\u0019\u0000\u0000\u0157\u0158\u0005P\u0000\u0000\u0158\u0175\u0005\u001a"+
		"\u0000\u0000\u0159\u015a\u0005!\u0000\u0000\u015a\u015b\u0005\u0019\u0000"+
		"\u0000\u015b\u015c\u0005\"\u0000\u0000\u015c\u0161\u0005O\u0000\u0000"+
		"\u015d\u015e\u0005#\u0000\u0000\u015e\u0160\u0005O\u0000\u0000\u015f\u015d"+
		"\u0001\u0000\u0000\u0000\u0160\u0163\u0001\u0000\u0000\u0000\u0161\u015f"+
		"\u0001\u0000\u0000\u0000\u0161\u0162\u0001\u0000\u0000\u0000\u0162\u0164"+
		"\u0001\u0000\u0000\u0000\u0163\u0161\u0001\u0000\u0000\u0000\u0164\u0165"+
		"\u0005$\u0000\u0000\u0165\u0175\u0005\u001a\u0000\u0000\u0166\u0167\u0005"+
		"%\u0000\u0000\u0167\u0168\u0005\u0019\u0000\u0000\u0168\u0169\u0005\""+
		"\u0000\u0000\u0169\u016e\u0005O\u0000\u0000\u016a\u016b\u0005#\u0000\u0000"+
		"\u016b\u016d\u0005O\u0000\u0000\u016c\u016a\u0001\u0000\u0000\u0000\u016d"+
		"\u0170\u0001\u0000\u0000\u0000\u016e\u016c\u0001\u0000\u0000\u0000\u016e"+
		"\u016f\u0001\u0000\u0000\u0000\u016f\u0171\u0001\u0000\u0000\u0000\u0170"+
		"\u016e\u0001\u0000\u0000\u0000\u0171\u0172\u0005$\u0000\u0000\u0172\u0175"+
		"\u0005\u001a\u0000\u0000\u0173\u0175\u0003J%\u0000\u0174\u013d\u0001\u0000"+
		"\u0000\u0000\u0174\u0141\u0001\u0000\u0000\u0000\u0174\u0145\u0001\u0000"+
		"\u0000\u0000\u0174\u0149\u0001\u0000\u0000\u0000\u0174\u014d\u0001\u0000"+
		"\u0000\u0000\u0174\u0151\u0001\u0000\u0000\u0000\u0174\u0155\u0001\u0000"+
		"\u0000\u0000\u0174\u0159\u0001\u0000\u0000\u0000\u0174\u0166\u0001\u0000"+
		"\u0000\u0000\u0174\u0173\u0001\u0000\u0000\u0000\u0175)\u0001\u0000\u0000"+
		"\u0000\u0176\u0177\u0005\u0018\u0000\u0000\u0177\u0178\u0005\u0019\u0000"+
		"\u0000\u0178\u0179\u0005Q\u0000\u0000\u0179\u01a6\u0005\u001a\u0000\u0000"+
		"\u017a\u017b\u0005&\u0000\u0000\u017b\u017c\u0005\u0019\u0000\u0000\u017c"+
		"\u017d\u0005O\u0000\u0000\u017d\u01a6\u0005\u001a\u0000\u0000\u017e\u017f"+
		"\u0005\'\u0000\u0000\u017f\u0180\u0005\u0019\u0000\u0000\u0180\u0181\u0005"+
		"T\u0000\u0000\u0181\u01a6\u0005\u001a\u0000\u0000\u0182\u0183\u0005(\u0000"+
		"\u0000\u0183\u0184\u0005\u0019\u0000\u0000\u0184\u0185\u0005Q\u0000\u0000"+
		"\u0185\u01a6\u0005\u001a\u0000\u0000\u0186\u0187\u0005)\u0000\u0000\u0187"+
		"\u0188\u0005\u0019\u0000\u0000\u0188\u0189\u0005\"\u0000\u0000\u0189\u018e"+
		"\u0005O\u0000\u0000\u018a\u018b\u0005#\u0000\u0000\u018b\u018d\u0005O"+
		"\u0000\u0000\u018c\u018a\u0001\u0000\u0000\u0000\u018d\u0190\u0001\u0000"+
		"\u0000\u0000\u018e\u018c\u0001\u0000\u0000\u0000\u018e\u018f\u0001\u0000"+
		"\u0000\u0000\u018f\u0191\u0001\u0000\u0000\u0000\u0190\u018e\u0001\u0000"+
		"\u0000\u0000\u0191\u0192\u0005$\u0000\u0000\u0192\u01a6\u0005\u001a\u0000"+
		"\u0000\u0193\u0194\u0005%\u0000\u0000\u0194\u0195\u0005\u0019\u0000\u0000"+
		"\u0195\u0196\u0005\"\u0000\u0000\u0196\u019b\u0005O\u0000\u0000\u0197"+
		"\u0198\u0005#\u0000\u0000\u0198\u019a\u0005O\u0000\u0000\u0199\u0197\u0001"+
		"\u0000\u0000\u0000\u019a\u019d\u0001\u0000\u0000\u0000\u019b\u0199\u0001"+
		"\u0000\u0000\u0000\u019b\u019c\u0001\u0000\u0000\u0000\u019c\u019e\u0001"+
		"\u0000\u0000\u0000\u019d\u019b\u0001\u0000\u0000\u0000\u019e\u019f\u0005"+
		"$\u0000\u0000\u019f\u01a6\u0005\u001a\u0000\u0000\u01a0\u01a1\u0005*\u0000"+
		"\u0000\u01a1\u01a2\u0005\u0019\u0000\u0000\u01a2\u01a3\u0005c\u0000\u0000"+
		"\u01a3\u01a6\u0005\u001a\u0000\u0000\u01a4\u01a6\u0003J%\u0000\u01a5\u0176"+
		"\u0001\u0000\u0000\u0000\u01a5\u017a\u0001\u0000\u0000\u0000\u01a5\u017e"+
		"\u0001\u0000\u0000\u0000\u01a5\u0182\u0001\u0000\u0000\u0000\u01a5\u0186"+
		"\u0001\u0000\u0000\u0000\u01a5\u0193\u0001\u0000\u0000\u0000\u01a5\u01a0"+
		"\u0001\u0000\u0000\u0000\u01a5\u01a4\u0001\u0000\u0000\u0000\u01a6+\u0001"+
		"\u0000\u0000\u0000\u01a7\u01a8\u0005\u0018\u0000\u0000\u01a8\u01a9\u0005"+
		"\u0019\u0000\u0000\u01a9\u01aa\u0005Q\u0000\u0000\u01aa\u01c2\u0005\u001a"+
		"\u0000\u0000\u01ab\u01ac\u0005+\u0000\u0000\u01ac\u01ad\u0005\u0019\u0000"+
		"\u0000\u01ad\u01ae\u0005O\u0000\u0000\u01ae\u01c2\u0005\u001a\u0000\u0000"+
		"\u01af\u01b0\u0005,\u0000\u0000\u01b0\u01b1\u0005\u0019\u0000\u0000\u01b1"+
		"\u01b2\u0005U\u0000\u0000\u01b2\u01c2\u0005\u001a\u0000\u0000\u01b3\u01b4"+
		"\u0005%\u0000\u0000\u01b4\u01b5\u0005\u0019\u0000\u0000\u01b5\u01b6\u0005"+
		"\"\u0000\u0000\u01b6\u01bb\u0005O\u0000\u0000\u01b7\u01b8\u0005#\u0000"+
		"\u0000\u01b8\u01ba\u0005O\u0000\u0000\u01b9\u01b7\u0001\u0000\u0000\u0000"+
		"\u01ba\u01bd\u0001\u0000\u0000\u0000\u01bb\u01b9\u0001\u0000\u0000\u0000"+
		"\u01bb\u01bc\u0001\u0000\u0000\u0000\u01bc\u01be\u0001\u0000\u0000\u0000"+
		"\u01bd\u01bb\u0001\u0000\u0000\u0000\u01be\u01bf\u0005$\u0000\u0000\u01bf"+
		"\u01c2\u0005\u001a\u0000\u0000\u01c0\u01c2\u0003J%\u0000\u01c1\u01a7\u0001"+
		"\u0000\u0000\u0000\u01c1\u01ab\u0001\u0000\u0000\u0000\u01c1\u01af\u0001"+
		"\u0000\u0000\u0000\u01c1\u01b3\u0001\u0000\u0000\u0000\u01c1\u01c0\u0001"+
		"\u0000\u0000\u0000\u01c2-\u0001\u0000\u0000\u0000\u01c3\u01c4\u0005\u0018"+
		"\u0000\u0000\u01c4\u01c5\u0005\u0019\u0000\u0000\u01c5\u01c6\u0005Q\u0000"+
		"\u0000\u01c6\u01e1\u0005\u001a\u0000\u0000\u01c7\u01c8\u0005,\u0000\u0000"+
		"\u01c8\u01c9\u0005\u0019\u0000\u0000\u01c9\u01ca\u0005U\u0000\u0000\u01ca"+
		"\u01e1\u0005\u001a\u0000\u0000\u01cb\u01cc\u0005\'\u0000\u0000\u01cc\u01cd"+
		"\u0005\u0019\u0000\u0000\u01cd\u01ce\u0005^\u0000\u0000\u01ce\u01e1\u0005"+
		"\u001a\u0000\u0000\u01cf\u01d0\u0005-\u0000\u0000\u01d0\u01d1\u0005\u0019"+
		"\u0000\u0000\u01d1\u01d2\u0005_\u0000\u0000\u01d2\u01e1\u0005\u001a\u0000"+
		"\u0000\u01d3\u01d4\u0005\u000e\u0000\u0000\u01d4\u01d5\u0005\u0019\u0000"+
		"\u0000\u01d5\u01d6\u0005O\u0000\u0000\u01d6\u01e1\u0005\u001a\u0000\u0000"+
		"\u01d7\u01d8\u0005.\u0000\u0000\u01d8\u01d9\u0005\u0019\u0000\u0000\u01d9"+
		"\u01da\u0007\u0001\u0000\u0000\u01da\u01e1\u0005\u001a\u0000\u0000\u01db"+
		"\u01dc\u0005/\u0000\u0000\u01dc\u01dd\u0005\u0019\u0000\u0000\u01dd\u01de"+
		"\u0005`\u0000\u0000\u01de\u01e1\u0005\u001a\u0000\u0000\u01df\u01e1\u0003"+
		"J%\u0000\u01e0\u01c3\u0001\u0000\u0000\u0000\u01e0\u01c7\u0001\u0000\u0000"+
		"\u0000\u01e0\u01cb\u0001\u0000\u0000\u0000\u01e0\u01cf\u0001\u0000\u0000"+
		"\u0000\u01e0\u01d3\u0001\u0000\u0000\u0000\u01e0\u01d7\u0001\u0000\u0000"+
		"\u0000\u01e0\u01db\u0001\u0000\u0000\u0000\u01e0\u01df\u0001\u0000\u0000"+
		"\u0000\u01e1/\u0001\u0000\u0000\u0000\u01e2\u01e3\u0005\u0018\u0000\u0000"+
		"\u01e3\u01e4\u0005\u0019\u0000\u0000\u01e4\u01e5\u0005Q\u0000\u0000\u01e5"+
		"\u01fc\u0005\u001a\u0000\u0000\u01e6\u01e7\u0005\'\u0000\u0000\u01e7\u01e8"+
		"\u0005\u0019\u0000\u0000\u01e8\u01e9\u0005V\u0000\u0000\u01e9\u01fc\u0005"+
		"\u001a\u0000\u0000\u01ea\u01eb\u0005,\u0000\u0000\u01eb\u01ec\u0005\u0019"+
		"\u0000\u0000\u01ec\u01ed\u0005U\u0000\u0000\u01ed\u01fc\u0005\u001a\u0000"+
		"\u0000\u01ee\u01ef\u00050\u0000\u0000\u01ef\u01f0\u0005\u0019\u0000\u0000"+
		"\u01f0\u01f1\u0005a\u0000\u0000\u01f1\u01fc\u0005\u001a\u0000\u0000\u01f2"+
		"\u01f3\u00051\u0000\u0000\u01f3\u01f4\u0005\u0019\u0000\u0000\u01f4\u01f5"+
		"\u0007\u0002\u0000\u0000\u01f5\u01fc\u0005\u001a\u0000\u0000\u01f6\u01f7"+
		"\u00052\u0000\u0000\u01f7\u01f8\u0005\u0019\u0000\u0000\u01f8\u01f9\u0005"+
		"W\u0000\u0000\u01f9\u01fc\u0005\u001a\u0000\u0000\u01fa\u01fc\u0003J%"+
		"\u0000\u01fb\u01e2\u0001\u0000\u0000\u0000\u01fb\u01e6\u0001\u0000\u0000"+
		"\u0000\u01fb\u01ea\u0001\u0000\u0000\u0000\u01fb\u01ee\u0001\u0000\u0000"+
		"\u0000\u01fb\u01f2\u0001\u0000\u0000\u0000\u01fb\u01f6\u0001\u0000\u0000"+
		"\u0000\u01fb\u01fa\u0001\u0000\u0000\u0000\u01fc1\u0001\u0000\u0000\u0000"+
		"\u01fd\u01fe\u0005\u0018\u0000\u0000\u01fe\u01ff\u0005\u0019\u0000\u0000"+
		"\u01ff\u0200\u0005Q\u0000\u0000\u0200\u0231\u0005\u001a\u0000\u0000\u0201"+
		"\u0202\u0005,\u0000\u0000\u0202\u0203\u0005\u0019\u0000\u0000\u0203\u0204"+
		"\u0005U\u0000\u0000\u0204\u0231\u0005\u001a\u0000\u0000\u0205\u0206\u0005"+
		"0\u0000\u0000\u0206\u0207\u0005\u0019\u0000\u0000\u0207\u0208\u0005X\u0000"+
		"\u0000\u0208\u0231\u0005\u001a\u0000\u0000\u0209\u020a\u00053\u0000\u0000"+
		"\u020a\u020b\u0005\u0019\u0000\u0000\u020b\u020c\u0005Q\u0000\u0000\u020c"+
		"\u0231\u0005\u001a\u0000\u0000\u020d\u020e\u00054\u0000\u0000\u020e\u020f"+
		"\u0005\u0019\u0000\u0000\u020f\u0210\u0005Q\u0000\u0000\u0210\u0231\u0005"+
		"\u001a\u0000\u0000\u0211\u0212\u00055\u0000\u0000\u0212\u0213\u0005\u0019"+
		"\u0000\u0000\u0213\u0214\u0005\"\u0000\u0000\u0214\u0219\u0005Q\u0000"+
		"\u0000\u0215\u0216\u0005#\u0000\u0000\u0216\u0218\u0005Q\u0000\u0000\u0217"+
		"\u0215\u0001\u0000\u0000\u0000\u0218\u021b\u0001\u0000\u0000\u0000\u0219"+
		"\u0217\u0001\u0000\u0000\u0000\u0219\u021a\u0001\u0000\u0000\u0000\u021a"+
		"\u021c\u0001\u0000\u0000\u0000\u021b\u0219\u0001\u0000\u0000\u0000\u021c"+
		"\u021d\u0005$\u0000\u0000\u021d\u0231\u0005\u001a\u0000\u0000\u021e\u021f"+
		"\u00056\u0000\u0000\u021f\u0220\u0005\u0019\u0000\u0000\u0220\u0221\u0005"+
		"\"\u0000\u0000\u0221\u0226\u0005Q\u0000\u0000\u0222\u0223\u0005#\u0000"+
		"\u0000\u0223\u0225\u0005Q\u0000\u0000\u0224\u0222\u0001\u0000\u0000\u0000"+
		"\u0225\u0228\u0001\u0000\u0000\u0000\u0226\u0224\u0001\u0000\u0000\u0000"+
		"\u0226\u0227\u0001\u0000\u0000\u0000\u0227\u0229\u0001\u0000\u0000\u0000"+
		"\u0228\u0226\u0001\u0000\u0000\u0000\u0229\u022a\u0005$\u0000\u0000\u022a"+
		"\u0231\u0005\u001a\u0000\u0000\u022b\u022c\u00057\u0000\u0000\u022c\u022d"+
		"\u0005\u0019\u0000\u0000\u022d\u022e\u0005P\u0000\u0000\u022e\u0231\u0005"+
		"\u001a\u0000\u0000\u022f\u0231\u0003J%\u0000\u0230\u01fd\u0001\u0000\u0000"+
		"\u0000\u0230\u0201\u0001\u0000\u0000\u0000\u0230\u0205\u0001\u0000\u0000"+
		"\u0000\u0230\u0209\u0001\u0000\u0000\u0000\u0230\u020d\u0001\u0000\u0000"+
		"\u0000\u0230\u0211\u0001\u0000\u0000\u0000\u0230\u021e\u0001\u0000\u0000"+
		"\u0000\u0230\u022b\u0001\u0000\u0000\u0000\u0230\u022f\u0001\u0000\u0000"+
		"\u0000\u02313\u0001\u0000\u0000\u0000\u0232\u0233\u0005\u0018\u0000\u0000"+
		"\u0233\u0234\u0005\u0019\u0000\u0000\u0234\u0235\u0005Q\u0000\u0000\u0235"+
		"\u0244\u0005\u001a\u0000\u0000\u0236\u0237\u0005,\u0000\u0000\u0237\u0238"+
		"\u0005\u0019\u0000\u0000\u0238\u0239\u0005U\u0000\u0000\u0239\u0244\u0005"+
		"\u001a\u0000\u0000\u023a\u023b\u00058\u0000\u0000\u023b\u023c\u0005\u0019"+
		"\u0000\u0000\u023c\u023d\u0005Q\u0000\u0000\u023d\u0244\u0005\u001a\u0000"+
		"\u0000\u023e\u023f\u00059\u0000\u0000\u023f\u0240\u0005\u0019\u0000\u0000"+
		"\u0240\u0241\u0005Q\u0000\u0000\u0241\u0244\u0005\u001a\u0000\u0000\u0242"+
		"\u0244\u0003J%\u0000\u0243\u0232\u0001\u0000\u0000\u0000\u0243\u0236\u0001"+
		"\u0000\u0000\u0000\u0243\u023a\u0001\u0000\u0000\u0000\u0243\u023e\u0001"+
		"\u0000\u0000\u0000\u0243\u0242\u0001\u0000\u0000\u0000\u02445\u0001\u0000"+
		"\u0000\u0000\u0245\u0246\u0005\u0018\u0000\u0000\u0246\u0247\u0005\u0019"+
		"\u0000\u0000\u0247\u0248\u0005Q\u0000\u0000\u0248\u025b\u0005\u001a\u0000"+
		"\u0000\u0249\u024a\u00050\u0000\u0000\u024a\u024b\u0005\u0019\u0000\u0000"+
		"\u024b\u024c\u0005b\u0000\u0000\u024c\u025b\u0005\u001a\u0000\u0000\u024d"+
		"\u024e\u0005:\u0000\u0000\u024e\u024f\u0005\u0019\u0000\u0000\u024f\u0250"+
		"\u0005Z\u0000\u0000\u0250\u025b\u0005\u001a\u0000\u0000\u0251\u0252\u0005"+
		";\u0000\u0000\u0252\u0253\u0005\u0019\u0000\u0000\u0253\u0254\u0005Q\u0000"+
		"\u0000\u0254\u025b\u0005\u001a\u0000\u0000\u0255\u0256\u0005<\u0000\u0000"+
		"\u0256\u0257\u0005\u0019\u0000\u0000\u0257\u0258\u0005Q\u0000\u0000\u0258"+
		"\u025b\u0005\u001a\u0000\u0000\u0259\u025b\u0003J%\u0000\u025a\u0245\u0001"+
		"\u0000\u0000\u0000\u025a\u0249\u0001\u0000\u0000\u0000\u025a\u024d\u0001"+
		"\u0000\u0000\u0000\u025a\u0251\u0001\u0000\u0000\u0000\u025a\u0255\u0001"+
		"\u0000\u0000\u0000\u025a\u0259\u0001\u0000\u0000\u0000\u025b7\u0001\u0000"+
		"\u0000\u0000\u025c\u025d\u0005\u0018\u0000\u0000\u025d\u025e\u0005\u0019"+
		"\u0000\u0000\u025e\u025f\u0005Q\u0000\u0000\u025f\u026e\u0005\u001a\u0000"+
		"\u0000\u0260\u0261\u0005=\u0000\u0000\u0261\u0262\u0005\u0019\u0000\u0000"+
		"\u0262\u0263\u0005Q\u0000\u0000\u0263\u026e\u0005\u001a\u0000\u0000\u0264"+
		"\u0265\u0005>\u0000\u0000\u0265\u0266\u0005\u0019\u0000\u0000\u0266\u0267"+
		"\u0005i\u0000\u0000\u0267\u026e\u0005\u001a\u0000\u0000\u0268\u0269\u0005"+
		"?\u0000\u0000\u0269\u026a\u0005\u0019\u0000\u0000\u026a\u026b\u0005P\u0000"+
		"\u0000\u026b\u026e\u0005\u001a\u0000\u0000\u026c\u026e\u0003J%\u0000\u026d"+
		"\u025c\u0001\u0000\u0000\u0000\u026d\u0260\u0001\u0000\u0000\u0000\u026d"+
		"\u0264\u0001\u0000\u0000\u0000\u026d\u0268\u0001\u0000\u0000\u0000\u026d"+
		"\u026c\u0001\u0000\u0000\u0000\u026e9\u0001\u0000\u0000\u0000\u026f\u0270"+
		"\u0005\u0018\u0000\u0000\u0270\u0271\u0005\u0019\u0000\u0000\u0271\u0272"+
		"\u0005Q\u0000\u0000\u0272\u027d\u0005\u001a\u0000\u0000\u0273\u0274\u0005"+
		"@\u0000\u0000\u0274\u0275\u0005\u0019\u0000\u0000\u0275\u0276\u0005e\u0000"+
		"\u0000\u0276\u027d\u0005\u001a\u0000\u0000\u0277\u0278\u0005A\u0000\u0000"+
		"\u0278\u0279\u0005\u0019\u0000\u0000\u0279\u027a\u0005Q\u0000\u0000\u027a"+
		"\u027d\u0005\u001a\u0000\u0000\u027b\u027d\u0003J%\u0000\u027c\u026f\u0001"+
		"\u0000\u0000\u0000\u027c\u0273\u0001\u0000\u0000\u0000\u027c\u0277\u0001"+
		"\u0000\u0000\u0000\u027c\u027b\u0001\u0000\u0000\u0000\u027d;\u0001\u0000"+
		"\u0000\u0000\u027e\u027f\u0005\u0018\u0000\u0000\u027f\u0280\u0005\u0019"+
		"\u0000\u0000\u0280\u0281\u0005Q\u0000\u0000\u0281\u0295\u0005\u001a\u0000"+
		"\u0000\u0282\u0283\u0005B\u0000\u0000\u0283\u0284\u0005\u0019\u0000\u0000"+
		"\u0284\u0285\u0005\"\u0000\u0000\u0285\u028a\u0005O\u0000\u0000\u0286"+
		"\u0287\u0005#\u0000\u0000\u0287\u0289\u0005O\u0000\u0000\u0288\u0286\u0001"+
		"\u0000\u0000\u0000\u0289\u028c\u0001\u0000\u0000\u0000\u028a\u0288\u0001"+
		"\u0000\u0000\u0000\u028a\u028b\u0001\u0000\u0000\u0000\u028b\u028d\u0001"+
		"\u0000\u0000\u0000\u028c\u028a\u0001\u0000\u0000\u0000\u028d\u028e\u0005"+
		"$\u0000\u0000\u028e\u0295\u0005\u001a\u0000\u0000\u028f\u0290\u0005C\u0000"+
		"\u0000\u0290\u0291\u0005\u0019\u0000\u0000\u0291\u0292\u0005Q\u0000\u0000"+
		"\u0292\u0295\u0005\u001a\u0000\u0000\u0293\u0295\u0003J%\u0000\u0294\u027e"+
		"\u0001\u0000\u0000\u0000\u0294\u0282\u0001\u0000\u0000\u0000\u0294\u028f"+
		"\u0001\u0000\u0000\u0000\u0294\u0293\u0001\u0000\u0000\u0000\u0295=\u0001"+
		"\u0000\u0000\u0000\u0296\u0297\u0005\u0018\u0000\u0000\u0297\u0298\u0005"+
		"\u0019\u0000\u0000\u0298\u0299\u0005Q\u0000\u0000\u0299\u02a4\u0005\u001a"+
		"\u0000\u0000\u029a\u029b\u0005D\u0000\u0000\u029b\u029c\u0005\u0019\u0000"+
		"\u0000\u029c\u029d\u0005]\u0000\u0000\u029d\u02a4\u0005\u001a\u0000\u0000"+
		"\u029e\u029f\u0005E\u0000\u0000\u029f\u02a0\u0005\u0019\u0000\u0000\u02a0"+
		"\u02a1\u0005Q\u0000\u0000\u02a1\u02a4\u0005\u001a\u0000\u0000\u02a2\u02a4"+
		"\u0003J%\u0000\u02a3\u0296\u0001\u0000\u0000\u0000\u02a3\u029a\u0001\u0000"+
		"\u0000\u0000\u02a3\u029e\u0001\u0000\u0000\u0000\u02a3\u02a2\u0001\u0000"+
		"\u0000\u0000\u02a4?\u0001\u0000\u0000\u0000\u02a5\u02a6\u0005\u0018\u0000"+
		"\u0000\u02a6\u02a7\u0005\u0019\u0000\u0000\u02a7\u02a8\u0005Q\u0000\u0000"+
		"\u02a8\u02b3\u0005\u001a\u0000\u0000\u02a9\u02aa\u0005F\u0000\u0000\u02aa"+
		"\u02ab\u0005\u0019\u0000\u0000\u02ab\u02ac\u0005d\u0000\u0000\u02ac\u02b3"+
		"\u0005\u001a\u0000\u0000\u02ad\u02ae\u0005G\u0000\u0000\u02ae\u02af\u0005"+
		"\u0019\u0000\u0000\u02af\u02b0\u0005P\u0000\u0000\u02b0\u02b3\u0005\u001a"+
		"\u0000\u0000\u02b1\u02b3\u0003J%\u0000\u02b2\u02a5\u0001\u0000\u0000\u0000"+
		"\u02b2\u02a9\u0001\u0000\u0000\u0000\u02b2\u02ad\u0001\u0000\u0000\u0000"+
		"\u02b2\u02b1\u0001\u0000\u0000\u0000\u02b3A\u0001\u0000\u0000\u0000\u02b4"+
		"\u02b5\u0005\u0018\u0000\u0000\u02b5\u02b6\u0005\u0019\u0000\u0000\u02b6"+
		"\u02b7\u0005Q\u0000\u0000\u02b7\u02c7\u0005\u001a\u0000\u0000\u02b8\u02b9"+
		"\u0005H\u0000\u0000\u02b9\u02ba\u0005\u0019\u0000\u0000\u02ba\u02bb\u0005"+
		"\"\u0000\u0000\u02bb\u02c0\u0005O\u0000\u0000\u02bc\u02bd\u0005#\u0000"+
		"\u0000\u02bd\u02bf\u0005O\u0000\u0000\u02be\u02bc\u0001\u0000\u0000\u0000"+
		"\u02bf\u02c2\u0001\u0000\u0000\u0000\u02c0\u02be\u0001\u0000\u0000\u0000"+
		"\u02c0\u02c1\u0001\u0000\u0000\u0000\u02c1\u02c3\u0001\u0000\u0000\u0000"+
		"\u02c2\u02c0\u0001\u0000\u0000\u0000\u02c3\u02c4\u0005$\u0000\u0000\u02c4"+
		"\u02c7\u0005\u001a\u0000\u0000\u02c5\u02c7\u0003J%\u0000\u02c6\u02b4\u0001"+
		"\u0000\u0000\u0000\u02c6\u02b8\u0001\u0000\u0000\u0000\u02c6\u02c5\u0001"+
		"\u0000\u0000\u0000\u02c7C\u0001\u0000\u0000\u0000\u02c8\u02c9\u0005\u0018"+
		"\u0000\u0000\u02c9\u02ca\u0005\u0019\u0000\u0000\u02ca\u02cb\u0005Q\u0000"+
		"\u0000\u02cb\u02d2\u0005\u001a\u0000\u0000\u02cc\u02cd\u0005I\u0000\u0000"+
		"\u02cd\u02ce\u0005\u0019\u0000\u0000\u02ce\u02cf\u0005P\u0000\u0000\u02cf"+
		"\u02d2\u0005\u001a\u0000\u0000\u02d0\u02d2\u0003J%\u0000\u02d1\u02c8\u0001"+
		"\u0000\u0000\u0000\u02d1\u02cc\u0001\u0000\u0000\u0000\u02d1\u02d0\u0001"+
		"\u0000\u0000\u0000\u02d2E\u0001\u0000\u0000\u0000\u02d3\u02d4\u0005\u0018"+
		"\u0000\u0000\u02d4\u02d5\u0005\u0019\u0000\u0000\u02d5\u02d6\u0005Q\u0000"+
		"\u0000\u02d6\u02dd\u0005\u001a\u0000\u0000\u02d7\u02d8\u0005J\u0000\u0000"+
		"\u02d8\u02d9\u0005\u0019\u0000\u0000\u02d9\u02da\u0005Q\u0000\u0000\u02da"+
		"\u02dd\u0005\u001a\u0000\u0000\u02db\u02dd\u0003J%\u0000\u02dc\u02d3\u0001"+
		"\u0000\u0000\u0000\u02dc\u02d7\u0001\u0000\u0000\u0000\u02dc\u02db\u0001"+
		"\u0000\u0000\u0000\u02ddG\u0001\u0000\u0000\u0000\u02de\u02df\u0005\u0018"+
		"\u0000\u0000\u02df\u02e0\u0005\u0019\u0000\u0000\u02e0\u02e1\u0005Q\u0000"+
		"\u0000\u02e1\u02ec\u0005\u001a\u0000\u0000\u02e2\u02e3\u00050\u0000\u0000"+
		"\u02e3\u02e4\u0005\u0019\u0000\u0000\u02e4\u02e5\u0005Y\u0000\u0000\u02e5"+
		"\u02ec\u0005\u001a\u0000\u0000\u02e6\u02e7\u0005K\u0000\u0000\u02e7\u02e8"+
		"\u0005\u0019\u0000\u0000\u02e8\u02e9\u0007\u0003\u0000\u0000\u02e9\u02ec"+
		"\u0005\u001a\u0000\u0000\u02ea\u02ec\u0003J%\u0000\u02eb\u02de\u0001\u0000"+
		"\u0000\u0000\u02eb\u02e2\u0001\u0000\u0000\u0000\u02eb\u02e6\u0001\u0000"+
		"\u0000\u0000\u02eb\u02ea\u0001\u0000\u0000\u0000\u02ecI\u0001\u0000\u0000"+
		"\u0000\u02ed\u02ee\u0005O\u0000\u0000\u02ee\u02ef\u0005\u0019\u0000\u0000"+
		"\u02ef\u02f0\u0003Z-\u0000\u02f0\u02f1\u0005\u001a\u0000\u0000\u02f1K"+
		"\u0001\u0000\u0000\u0000\u02f2\u02f3\u0003P(\u0000\u02f3\u02f4\u0005\u001a"+
		"\u0000\u0000\u02f4\u02fd\u0001\u0000\u0000\u0000\u02f5\u02fd\u0003R)\u0000"+
		"\u02f6\u02f7\u0003T*\u0000\u02f7\u02f8\u0005\u001a\u0000\u0000\u02f8\u02fd"+
		"\u0001\u0000\u0000\u0000\u02f9\u02fa\u0003V+\u0000\u02fa\u02fb\u0005\u001a"+
		"\u0000\u0000\u02fb\u02fd\u0001\u0000\u0000\u0000\u02fc\u02f2\u0001\u0000"+
		"\u0000\u0000\u02fc\u02f5\u0001\u0000\u0000\u0000\u02fc\u02f6\u0001\u0000"+
		"\u0000\u0000\u02fc\u02f9\u0001\u0000\u0000\u0000\u02fdM\u0001\u0000\u0000"+
		"\u0000\u02fe\u02ff\u0005O\u0000\u0000\u02ff\u0300\u0005\u0019\u0000\u0000"+
		"\u0300\u0303\u0003X,\u0000\u0301\u0302\u0005\u0019\u0000\u0000\u0302\u0304"+
		"\u0003Z-\u0000\u0303\u0301\u0001\u0000\u0000\u0000\u0303\u0304\u0001\u0000"+
		"\u0000\u0000\u0304\u0305\u0001\u0000\u0000\u0000\u0305\u0306\u0005\u001a"+
		"\u0000\u0000\u0306O\u0001\u0000\u0000\u0000\u0307\u0308\u0005O\u0000\u0000"+
		"\u0308\u0309\u0005\u0019\u0000\u0000\u0309\u030a\u0003Z-\u0000\u030aQ"+
		"\u0001\u0000\u0000\u0000\u030b\u030c\u0005L\u0000\u0000\u030c\u030d\u0005"+
		"O\u0000\u0000\u030d\u0311\u0005\u0005\u0000\u0000\u030e\u0310\u0003L&"+
		"\u0000\u030f\u030e\u0001\u0000\u0000\u0000\u0310\u0313\u0001\u0000\u0000"+
		"\u0000\u0311\u030f\u0001\u0000\u0000\u0000\u0311\u0312\u0001\u0000\u0000"+
		"\u0000\u0312\u0314\u0001\u0000\u0000\u0000\u0313\u0311\u0001\u0000\u0000"+
		"\u0000\u0314\u0315\u0005\u0006\u0000\u0000\u0315S\u0001\u0000\u0000\u0000"+
		"\u0316\u0317\u0005M\u0000\u0000\u0317\u0318\u0005\u0019\u0000\u0000\u0318"+
		"\u0319\u0005Q\u0000\u0000\u0319U\u0001\u0000\u0000\u0000\u031a\u031b\u0005"+
		"N\u0000\u0000\u031b\u031c\u0005Q\u0000\u0000\u031cW\u0001\u0000\u0000"+
		"\u0000\u031d\u031e\u0005O\u0000\u0000\u031eY\u0001\u0000\u0000\u0000\u031f"+
		"\u0339\u0005P\u0000\u0000\u0320\u0339\u0005Q\u0000\u0000\u0321\u0339\u0005"+
		"R\u0000\u0000\u0322\u0323\u0005\"\u0000\u0000\u0323\u0328\u0003Z-\u0000"+
		"\u0324\u0325\u0005#\u0000\u0000\u0325\u0327\u0003Z-\u0000\u0326\u0324"+
		"\u0001\u0000\u0000\u0000\u0327\u032a\u0001\u0000\u0000\u0000\u0328\u0326"+
		"\u0001\u0000\u0000\u0000\u0328\u0329\u0001\u0000\u0000\u0000\u0329\u032b"+
		"\u0001\u0000\u0000\u0000\u032a\u0328\u0001\u0000\u0000\u0000\u032b\u032c"+
		"\u0005$\u0000\u0000\u032c\u0339\u0001\u0000\u0000\u0000\u032d\u032e\u0005"+
		"\u0005\u0000\u0000\u032e\u0333\u0003P(\u0000\u032f\u0330\u0005#\u0000"+
		"\u0000\u0330\u0332\u0003P(\u0000\u0331\u032f\u0001\u0000\u0000\u0000\u0332"+
		"\u0335\u0001\u0000\u0000\u0000\u0333\u0331\u0001\u0000\u0000\u0000\u0333"+
		"\u0334\u0001\u0000\u0000\u0000\u0334\u0336\u0001\u0000\u0000\u0000\u0335"+
		"\u0333\u0001\u0000\u0000\u0000\u0336\u0337\u0005\u0006\u0000\u0000\u0337"+
		"\u0339\u0001\u0000\u0000\u0000\u0338\u031f\u0001\u0000\u0000\u0000\u0338"+
		"\u0320\u0001\u0000\u0000\u0000\u0338\u0321\u0001\u0000\u0000\u0000\u0338"+
		"\u0322\u0001\u0000\u0000\u0000\u0338\u032d\u0001\u0000\u0000\u0000\u0339"+
		"[\u0001\u0000\u0000\u00004_u}\u0088\u0093\u009e\u00a9\u00b4\u00bf\u00ca"+
		"\u00d5\u00e0\u00eb\u00f6\u0101\u010c\u0117\u0122\u012d\u0138\u0161\u016e"+
		"\u0174\u018e\u019b\u01a5\u01bb\u01c1\u01e0\u01fb\u0219\u0226\u0230\u0243"+
		"\u025a\u026d\u027c\u028a\u0294\u02a3\u02b2\u02c0\u02c6\u02d1\u02dc\u02eb"+
		"\u02fc\u0303\u0311\u0328\u0333\u0338";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}