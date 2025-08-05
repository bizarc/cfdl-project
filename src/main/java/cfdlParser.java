// Generated from cfdl.g4 by ANTLR 4.13.1
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
		T__73=74, T__74=75, T__75=76, T__76=77, T__77=78, T__78=79, T__79=80, 
		T__80=81, T__81=82, T__82=83, T__83=84, T__84=85, T__85=86, T__86=87, 
		T__87=88, T__88=89, T__89=90, T__90=91, T__91=92, T__92=93, T__93=94, 
		T__94=95, T__95=96, T__96=97, T__97=98, T__98=99, T__99=100, T__100=101, 
		T__101=102, T__102=103, T__103=104, T__104=105, T__105=106, T__106=107, 
		T__107=108, T__108=109, T__109=110, T__110=111, T__111=112, T__112=113, 
		T__113=114, T__114=115, T__115=116, IDENTIFIER=117, NUMBER=118, STRING=119, 
		BOOLEAN=120, DEAL_TYPE=121, ASSET_CATEGORY=122, COMPONENT_SCOPE=123, ASSUMPTION_CATEGORY=124, 
		DISTRIBUTION_TYPE=125, LOGIC_BLOCK_TYPE=126, METRIC_TYPE=127, FREQUENCY=128, 
		BUSINESS_DAY_CONVENTION=129, DAY_COUNT=130, PARTY_ROLE=131, STREAM_CATEGORY=132, 
		STREAM_SUB_TYPE=133, AMOUNT_TYPE=134, ASSUMPTION_TYPE=135, SCHEDULE_TYPE=136, 
		ASSET_STATE=137, FUND_PARTICIPANT_ROLE=138, TEMPLATE_TYPE=139, PARAMETER_DATA_TYPE=140, 
		RECURRENCE_FREQUENCY=141, WEEKDAY=142, TRIGGER_OPERATOR=143, DATA_SOURCE_TYPE=144, 
		PAYBACK_UNIT=145, WS=146, LINE_COMMENT=147, BLOCK_COMMENT=148;
	public static final int
		RULE_specification = 0, RULE_definition = 1, RULE_genericDefinition = 2, 
		RULE_dealDefinition = 3, RULE_assetDefinition = 4, RULE_componentDefinition = 5, 
		RULE_streamDefinition = 6, RULE_assumptionDefinition = 7, RULE_logicBlockDefinition = 8, 
		RULE_ruleBlockDefinition = 9, RULE_scheduleDefinition = 10, RULE_eventTriggerDefinition = 11, 
		RULE_templateDefinition = 12, RULE_contractDefinition = 13, RULE_partyDefinition = 14, 
		RULE_fundDefinition = 15, RULE_portfolioDefinition = 16, RULE_capitalStackDefinition = 17, 
		RULE_waterFallDefinition = 18, RULE_metricDefinition = 19, RULE_dealProperty = 20, 
		RULE_dealTypeValue = 21, RULE_assetProperty = 22, RULE_assetCategoryValue = 23, 
		RULE_assetStateValue = 24, RULE_componentProperty = 25, RULE_streamProperty = 26, 
		RULE_componentScopeValue = 27, RULE_streamCategoryValue = 28, RULE_streamSubTypeValue = 29, 
		RULE_amountTypeValue = 30, RULE_assumptionProperty = 31, RULE_logicBlockProperty = 32, 
		RULE_ruleBlockProperty = 33, RULE_scheduleProperty = 34, RULE_eventTriggerProperty = 35, 
		RULE_templateProperty = 36, RULE_contractProperty = 37, RULE_partyProperty = 38, 
		RULE_fundProperty = 39, RULE_portfolioProperty = 40, RULE_capitalStackProperty = 41, 
		RULE_waterFallProperty = 42, RULE_metricProperty = 43, RULE_genericProperty = 44, 
		RULE_statement = 45, RULE_propertyDef = 46, RULE_propertyAssign = 47, 
		RULE_logicBlockDef = 48, RULE_referenceDecl = 49, RULE_importDecl = 50, 
		RULE_typeRef = 51, RULE_value = 52;
	private static String[] makeRuleNames() {
		return new String[] {
			"specification", "definition", "genericDefinition", "dealDefinition", 
			"assetDefinition", "componentDefinition", "streamDefinition", "assumptionDefinition", 
			"logicBlockDefinition", "ruleBlockDefinition", "scheduleDefinition", 
			"eventTriggerDefinition", "templateDefinition", "contractDefinition", 
			"partyDefinition", "fundDefinition", "portfolioDefinition", "capitalStackDefinition", 
			"waterFallDefinition", "metricDefinition", "dealProperty", "dealTypeValue", 
			"assetProperty", "assetCategoryValue", "assetStateValue", "componentProperty", 
			"streamProperty", "componentScopeValue", "streamCategoryValue", "streamSubTypeValue", 
			"amountTypeValue", "assumptionProperty", "logicBlockProperty", "ruleBlockProperty", 
			"scheduleProperty", "eventTriggerProperty", "templateProperty", "contractProperty", 
			"partyProperty", "fundProperty", "portfolioProperty", "capitalStackProperty", 
			"waterFallProperty", "metricProperty", "genericProperty", "statement", 
			"propertyDef", "propertyAssign", "logicBlockDef", "referenceDecl", "importDecl", 
			"typeRef", "value"
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
			"'['", "','", "']'", "'streams'", "'commercial_real_estate'", "'residential_real_estate'", 
			"'financial_instrument'", "'syndicated_loan'", "'private_credit'", "'litigation_finance'", 
			"'royalty_stream'", "'infrastructure_project'", "'renewable_energy_project'", 
			"'equipment_leasing'", "'art_and_collectibles'", "'business_acquisition'", 
			"'deal_other'", "'dealId'", "'category'", "'description'", "'components'", 
			"'state'", "'real_estate'", "'financial_asset'", "'physical_asset'", 
			"'legal_right'", "'operating_entity'", "'contract_bundle'", "'mixed'", 
			"'asset_other'", "'pre_operational'", "'operational'", "'non_operational'", 
			"'disposed'", "'assetId'", "'scope'", "'subType'", "'amount'", "'amountType'", 
			"'Revenue'", "'Expense'", "'OtherIncome'", "'Operating'", "'Financing'", 
			"'Tax'", "'CapEx'", "'Fee'", "'Other'", "'amount_fixed'", "'amount_expression'", 
			"'amount_distribution'", "'randomWalk'", "'type'", "'value'", "'distributionType'", 
			"'code'", "'language'", "'inputs'", "'outputs'", "'executionOrder'", 
			"'condition'", "'action'", "'frequency'", "'startDate'", "'endDate'", 
			"'eventType'", "'operator'", "'threshold'", "'templateType'", "'body'", 
			"'parties'", "'terms'", "'role'", "'contact'", "'participantRole'", "'totalCommitment'", 
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
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "IDENTIFIER", "NUMBER", 
			"STRING", "BOOLEAN", "DEAL_TYPE", "ASSET_CATEGORY", "COMPONENT_SCOPE", 
			"ASSUMPTION_CATEGORY", "DISTRIBUTION_TYPE", "LOGIC_BLOCK_TYPE", "METRIC_TYPE", 
			"FREQUENCY", "BUSINESS_DAY_CONVENTION", "DAY_COUNT", "PARTY_ROLE", "STREAM_CATEGORY", 
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
			setState(107); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(106);
				definition();
				}
				}
				setState(109); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 16777118L) != 0) );
			setState(111);
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
			setState(131);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(113);
				dealDefinition();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(114);
				assetDefinition();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 3);
				{
				setState(115);
				componentDefinition();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(116);
				streamDefinition();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 5);
				{
				setState(117);
				assumptionDefinition();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 6);
				{
				setState(118);
				logicBlockDefinition();
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 7);
				{
				setState(119);
				ruleBlockDefinition();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 8);
				{
				setState(120);
				scheduleDefinition();
				}
				break;
			case T__14:
				enterOuterAlt(_localctx, 9);
				{
				setState(121);
				eventTriggerDefinition();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 10);
				{
				setState(122);
				templateDefinition();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 11);
				{
				setState(123);
				contractDefinition();
				}
				break;
			case T__17:
				enterOuterAlt(_localctx, 12);
				{
				setState(124);
				partyDefinition();
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 13);
				{
				setState(125);
				fundDefinition();
				}
				break;
			case T__19:
				enterOuterAlt(_localctx, 14);
				{
				setState(126);
				portfolioDefinition();
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 15);
				{
				setState(127);
				capitalStackDefinition();
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 16);
				{
				setState(128);
				waterFallDefinition();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 17);
				{
				setState(129);
				metricDefinition();
				}
				break;
			case T__0:
			case T__1:
			case T__2:
			case T__3:
				enterOuterAlt(_localctx, 18);
				{
				setState(130);
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
			setState(133);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 30L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(134);
			match(IDENTIFIER);
			setState(135);
			match(T__4);
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & 15L) != 0)) {
				{
				{
				setState(136);
				statement();
				}
				}
				setState(141);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(142);
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
			setState(144);
			match(T__6);
			setState(145);
			match(IDENTIFIER);
			setState(146);
			match(T__4);
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 154501382144L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(147);
				dealProperty();
				}
				}
				setState(152);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(153);
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
			setState(155);
			match(T__7);
			setState(156);
			match(IDENTIFIER);
			setState(157);
			match(T__4);
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 69805931679973376L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(158);
				assetProperty();
				}
				}
				setState(163);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(164);
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
			setState(166);
			match(T__8);
			setState(167);
			match(IDENTIFIER);
			setState(168);
			match(T__4);
			setState(172);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || _la==T__36 || ((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 562949953421315L) != 0)) {
				{
				{
				setState(169);
				componentProperty();
				}
				}
				setState(174);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(175);
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
			setState(177);
			match(T__9);
			setState(178);
			match(IDENTIFIER);
			setState(179);
			match(T__4);
			setState(183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4503599644164096L) != 0) || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & 281474976710671L) != 0)) {
				{
				{
				setState(180);
				streamProperty();
				}
				}
				setState(185);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(186);
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
			setState(188);
			match(T__10);
			setState(189);
			match(IDENTIFIER);
			setState(190);
			match(T__4);
			setState(194);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || _la==T__51 || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & 281474977628161L) != 0)) {
				{
				{
				setState(191);
				assumptionProperty();
				}
				}
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(197);
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
			setState(199);
			match(T__11);
			setState(200);
			match(IDENTIFIER);
			setState(201);
			match(T__4);
			setState(205);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & 281475009347585L) != 0)) {
				{
				{
				setState(202);
				logicBlockProperty();
				}
				}
				setState(207);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(208);
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
			setState(210);
			match(T__12);
			setState(211);
			match(IDENTIFIER);
			setState(212);
			match(T__4);
			setState(216);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & 281475077373953L) != 0)) {
				{
				{
				setState(213);
				ruleBlockProperty();
				}
				}
				setState(218);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(219);
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
			setState(221);
			match(T__13);
			setState(222);
			match(IDENTIFIER);
			setState(223);
			match(T__4);
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 86)) & ~0x3f) == 0 && ((1L << (_la - 86)) & 2147490817L) != 0)) {
				{
				{
				setState(224);
				scheduleProperty();
				}
				}
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(230);
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
			setState(232);
			match(T__14);
			setState(233);
			match(IDENTIFIER);
			setState(234);
			match(T__4);
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 99)) & ~0x3f) == 0 && ((1L << (_la - 99)) & 262151L) != 0)) {
				{
				{
				setState(235);
				eventTriggerProperty();
				}
				}
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(241);
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
			setState(243);
			match(T__15);
			setState(244);
			match(IDENTIFIER);
			setState(245);
			match(T__4);
			setState(249);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 102)) & ~0x3f) == 0 && ((1L << (_la - 102)) & 32771L) != 0)) {
				{
				{
				setState(246);
				templateProperty();
				}
				}
				setState(251);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(252);
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
			setState(254);
			match(T__16);
			setState(255);
			match(IDENTIFIER);
			setState(256);
			match(T__4);
			setState(260);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 104)) & ~0x3f) == 0 && ((1L << (_la - 104)) & 8195L) != 0)) {
				{
				{
				setState(257);
				contractProperty();
				}
				}
				setState(262);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(263);
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
			setState(265);
			match(T__17);
			setState(266);
			match(IDENTIFIER);
			setState(267);
			match(T__4);
			setState(271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 106)) & ~0x3f) == 0 && ((1L << (_la - 106)) & 2051L) != 0)) {
				{
				{
				setState(268);
				partyProperty();
				}
				}
				setState(273);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(274);
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
			setState(276);
			match(T__18);
			setState(277);
			match(IDENTIFIER);
			setState(278);
			match(T__4);
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 108)) & ~0x3f) == 0 && ((1L << (_la - 108)) & 515L) != 0)) {
				{
				{
				setState(279);
				fundProperty();
				}
				}
				setState(284);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(285);
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
			setState(287);
			match(T__19);
			setState(288);
			match(IDENTIFIER);
			setState(289);
			match(T__4);
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || _la==T__109 || _la==IDENTIFIER) {
				{
				{
				setState(290);
				portfolioProperty();
				}
				}
				setState(295);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(296);
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
			setState(298);
			match(T__20);
			setState(299);
			match(IDENTIFIER);
			setState(300);
			match(T__4);
			setState(304);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || _la==T__110 || _la==IDENTIFIER) {
				{
				{
				setState(301);
				capitalStackProperty();
				}
				}
				setState(306);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(307);
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
			setState(309);
			match(T__21);
			setState(310);
			match(IDENTIFIER);
			setState(311);
			match(T__4);
			setState(315);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || _la==T__111 || _la==IDENTIFIER) {
				{
				{
				setState(312);
				waterFallProperty();
				}
				}
				setState(317);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(318);
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
			setState(320);
			match(T__22);
			setState(321);
			match(IDENTIFIER);
			setState(322);
			match(T__4);
			setState(326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__23 || ((((_la - 86)) & ~0x3f) == 0 && ((1L << (_la - 86)) & 2281701377L) != 0)) {
				{
				{
				setState(323);
				metricProperty();
				}
				}
				setState(328);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(329);
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
		public DealTypeValueContext dealTypeValue() {
			return getRuleContext(DealTypeValueContext.class,0);
		}
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
			setState(387);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(331);
				match(T__23);
				setState(332);
				match(T__24);
				setState(333);
				match(STRING);
				setState(334);
				match(T__25);
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 2);
				{
				setState(335);
				match(T__26);
				setState(336);
				match(T__24);
				setState(337);
				dealTypeValue();
				setState(338);
				match(T__25);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 3);
				{
				setState(340);
				match(T__27);
				setState(341);
				match(T__24);
				setState(342);
				match(STRING);
				setState(343);
				match(T__25);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 4);
				{
				setState(344);
				match(T__28);
				setState(345);
				match(T__24);
				setState(346);
				match(STRING);
				setState(347);
				match(T__25);
				}
				break;
			case T__29:
				enterOuterAlt(_localctx, 5);
				{
				setState(348);
				match(T__29);
				setState(349);
				match(T__24);
				setState(350);
				match(STRING);
				setState(351);
				match(T__25);
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 6);
				{
				setState(352);
				match(T__30);
				setState(353);
				match(T__24);
				setState(354);
				match(STRING);
				setState(355);
				match(T__25);
				}
				break;
			case T__31:
				enterOuterAlt(_localctx, 7);
				{
				setState(356);
				match(T__31);
				setState(357);
				match(T__24);
				setState(358);
				match(NUMBER);
				setState(359);
				match(T__25);
				}
				break;
			case T__32:
				enterOuterAlt(_localctx, 8);
				{
				setState(360);
				match(T__32);
				setState(361);
				match(T__24);
				setState(362);
				match(T__33);
				setState(363);
				match(IDENTIFIER);
				setState(368);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(364);
					match(T__34);
					setState(365);
					match(IDENTIFIER);
					}
					}
					setState(370);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(371);
				match(T__35);
				setState(372);
				match(T__25);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 9);
				{
				setState(373);
				match(T__36);
				setState(374);
				match(T__24);
				setState(375);
				match(T__33);
				setState(376);
				match(IDENTIFIER);
				setState(381);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(377);
					match(T__34);
					setState(378);
					match(IDENTIFIER);
					}
					}
					setState(383);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(384);
				match(T__35);
				setState(385);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 10);
				{
				setState(386);
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
	public static class DealTypeValueContext extends ParserRuleContext {
		public DealTypeValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dealTypeValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterDealTypeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitDealTypeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitDealTypeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DealTypeValueContext dealTypeValue() throws RecognitionException {
		DealTypeValueContext _localctx = new DealTypeValueContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_dealTypeValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2251524935778304L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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
		public AssetCategoryValueContext assetCategoryValue() {
			return getRuleContext(AssetCategoryValueContext.class,0);
		}
		public AssetStateValueContext assetStateValue() {
			return getRuleContext(AssetStateValueContext.class,0);
		}
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
		enterRule(_localctx, 44, RULE_assetProperty);
		int _la;
		try {
			setState(440);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(391);
				match(T__23);
				setState(392);
				match(T__24);
				setState(393);
				match(STRING);
				setState(394);
				match(T__25);
				}
				break;
			case T__50:
				enterOuterAlt(_localctx, 2);
				{
				setState(395);
				match(T__50);
				setState(396);
				match(T__24);
				setState(397);
				match(IDENTIFIER);
				setState(398);
				match(T__25);
				}
				break;
			case T__51:
				enterOuterAlt(_localctx, 3);
				{
				setState(399);
				match(T__51);
				setState(400);
				match(T__24);
				setState(401);
				assetCategoryValue();
				setState(402);
				match(T__25);
				}
				break;
			case T__52:
				enterOuterAlt(_localctx, 4);
				{
				setState(404);
				match(T__52);
				setState(405);
				match(T__24);
				setState(406);
				match(STRING);
				setState(407);
				match(T__25);
				}
				break;
			case T__53:
				enterOuterAlt(_localctx, 5);
				{
				setState(408);
				match(T__53);
				setState(409);
				match(T__24);
				setState(410);
				match(T__33);
				setState(411);
				match(IDENTIFIER);
				setState(416);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(412);
					match(T__34);
					setState(413);
					match(IDENTIFIER);
					}
					}
					setState(418);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(419);
				match(T__35);
				setState(420);
				match(T__25);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 6);
				{
				setState(421);
				match(T__36);
				setState(422);
				match(T__24);
				setState(423);
				match(T__33);
				setState(424);
				match(IDENTIFIER);
				setState(429);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(425);
					match(T__34);
					setState(426);
					match(IDENTIFIER);
					}
					}
					setState(431);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(432);
				match(T__35);
				setState(433);
				match(T__25);
				}
				break;
			case T__54:
				enterOuterAlt(_localctx, 7);
				{
				setState(434);
				match(T__54);
				setState(435);
				match(T__24);
				setState(436);
				assetStateValue();
				setState(437);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 8);
				{
				setState(439);
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
	public static class AssetCategoryValueContext extends ParserRuleContext {
		public AssetCategoryValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assetCategoryValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterAssetCategoryValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitAssetCategoryValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitAssetCategoryValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssetCategoryValueContext assetCategoryValue() throws RecognitionException {
		AssetCategoryValueContext _localctx = new AssetCategoryValueContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_assetCategoryValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(442);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -72057594037927936L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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
	public static class AssetStateValueContext extends ParserRuleContext {
		public AssetStateValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assetStateValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterAssetStateValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitAssetStateValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitAssetStateValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssetStateValueContext assetStateValue() throws RecognitionException {
		AssetStateValueContext _localctx = new AssetStateValueContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_assetStateValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(444);
			_la = _input.LA(1);
			if ( !(((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 15L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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
		enterRule(_localctx, 50, RULE_componentProperty);
		int _la;
		try {
			setState(472);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(446);
				match(T__23);
				setState(447);
				match(T__24);
				setState(448);
				match(STRING);
				setState(449);
				match(T__25);
				}
				break;
			case T__67:
				enterOuterAlt(_localctx, 2);
				{
				setState(450);
				match(T__67);
				setState(451);
				match(T__24);
				setState(452);
				match(IDENTIFIER);
				setState(453);
				match(T__25);
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 3);
				{
				setState(454);
				match(T__68);
				setState(455);
				match(T__24);
				setState(456);
				match(COMPONENT_SCOPE);
				setState(457);
				match(T__25);
				}
				break;
			case T__36:
				enterOuterAlt(_localctx, 4);
				{
				setState(458);
				match(T__36);
				setState(459);
				match(T__24);
				setState(460);
				match(T__33);
				setState(461);
				match(IDENTIFIER);
				setState(466);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(462);
					match(T__34);
					setState(463);
					match(IDENTIFIER);
					}
					}
					setState(468);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(469);
				match(T__35);
				setState(470);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(471);
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
		public ComponentScopeValueContext componentScopeValue() {
			return getRuleContext(ComponentScopeValueContext.class,0);
		}
		public StreamCategoryValueContext streamCategoryValue() {
			return getRuleContext(StreamCategoryValueContext.class,0);
		}
		public StreamSubTypeValueContext streamSubTypeValue() {
			return getRuleContext(StreamSubTypeValueContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(cfdlParser.IDENTIFIER, 0); }
		public TerminalNode NUMBER() { return getToken(cfdlParser.NUMBER, 0); }
		public AmountTypeValueContext amountTypeValue() {
			return getRuleContext(AmountTypeValueContext.class,0);
		}
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
		enterRule(_localctx, 52, RULE_streamProperty);
		int _la;
		try {
			setState(507);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(474);
				match(T__23);
				setState(475);
				match(T__24);
				setState(476);
				match(STRING);
				setState(477);
				match(T__25);
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 2);
				{
				setState(478);
				match(T__68);
				setState(479);
				match(T__24);
				setState(480);
				componentScopeValue();
				setState(481);
				match(T__25);
				}
				break;
			case T__51:
				enterOuterAlt(_localctx, 3);
				{
				setState(483);
				match(T__51);
				setState(484);
				match(T__24);
				setState(485);
				streamCategoryValue();
				setState(486);
				match(T__25);
				}
				break;
			case T__69:
				enterOuterAlt(_localctx, 4);
				{
				setState(488);
				match(T__69);
				setState(489);
				match(T__24);
				setState(490);
				streamSubTypeValue();
				setState(491);
				match(T__25);
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 5);
				{
				setState(493);
				match(T__13);
				setState(494);
				match(T__24);
				setState(495);
				match(IDENTIFIER);
				setState(496);
				match(T__25);
				}
				break;
			case T__70:
				enterOuterAlt(_localctx, 6);
				{
				setState(497);
				match(T__70);
				setState(498);
				match(T__24);
				setState(499);
				_la = _input.LA(1);
				if ( !(_la==IDENTIFIER || _la==NUMBER) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(500);
				match(T__25);
				}
				break;
			case T__71:
				enterOuterAlt(_localctx, 7);
				{
				setState(501);
				match(T__71);
				setState(502);
				match(T__24);
				setState(503);
				amountTypeValue();
				setState(504);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 8);
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
	public static class ComponentScopeValueContext extends ParserRuleContext {
		public ComponentScopeValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_componentScopeValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterComponentScopeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitComponentScopeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitComponentScopeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComponentScopeValueContext componentScopeValue() throws RecognitionException {
		ComponentScopeValueContext _localctx = new ComponentScopeValueContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_componentScopeValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(509);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1573760L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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
	public static class StreamCategoryValueContext extends ParserRuleContext {
		public StreamCategoryValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_streamCategoryValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterStreamCategoryValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitStreamCategoryValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitStreamCategoryValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StreamCategoryValueContext streamCategoryValue() throws RecognitionException {
		StreamCategoryValueContext _localctx = new StreamCategoryValueContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_streamCategoryValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(511);
			_la = _input.LA(1);
			if ( !(((((_la - 73)) & ~0x3f) == 0 && ((1L << (_la - 73)) & 7L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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
	public static class StreamSubTypeValueContext extends ParserRuleContext {
		public StreamSubTypeValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_streamSubTypeValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterStreamSubTypeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitStreamSubTypeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitStreamSubTypeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StreamSubTypeValueContext streamSubTypeValue() throws RecognitionException {
		StreamSubTypeValueContext _localctx = new StreamSubTypeValueContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_streamSubTypeValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(513);
			_la = _input.LA(1);
			if ( !(((((_la - 76)) & ~0x3f) == 0 && ((1L << (_la - 76)) & 63L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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
	public static class AmountTypeValueContext extends ParserRuleContext {
		public AmountTypeValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_amountTypeValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).enterAmountTypeValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof cfdlListener ) ((cfdlListener)listener).exitAmountTypeValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof cfdlVisitor ) return ((cfdlVisitor<? extends T>)visitor).visitAmountTypeValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AmountTypeValueContext amountTypeValue() throws RecognitionException {
		AmountTypeValueContext _localctx = new AmountTypeValueContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_amountTypeValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(515);
			_la = _input.LA(1);
			if ( !(((((_la - 82)) & ~0x3f) == 0 && ((1L << (_la - 82)) & 15L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
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
		enterRule(_localctx, 62, RULE_assumptionProperty);
		int _la;
		try {
			setState(542);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(517);
				match(T__23);
				setState(518);
				match(T__24);
				setState(519);
				match(STRING);
				setState(520);
				match(T__25);
				}
				break;
			case T__51:
				enterOuterAlt(_localctx, 2);
				{
				setState(521);
				match(T__51);
				setState(522);
				match(T__24);
				setState(523);
				match(ASSUMPTION_CATEGORY);
				setState(524);
				match(T__25);
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 3);
				{
				setState(525);
				match(T__68);
				setState(526);
				match(T__24);
				setState(527);
				match(COMPONENT_SCOPE);
				setState(528);
				match(T__25);
				}
				break;
			case T__85:
				enterOuterAlt(_localctx, 4);
				{
				setState(529);
				match(T__85);
				setState(530);
				match(T__24);
				setState(531);
				match(ASSUMPTION_TYPE);
				setState(532);
				match(T__25);
				}
				break;
			case T__86:
				enterOuterAlt(_localctx, 5);
				{
				setState(533);
				match(T__86);
				setState(534);
				match(T__24);
				setState(535);
				_la = _input.LA(1);
				if ( !(((((_la - 117)) & ~0x3f) == 0 && ((1L << (_la - 117)) & 7L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(536);
				match(T__25);
				}
				break;
			case T__87:
				enterOuterAlt(_localctx, 6);
				{
				setState(537);
				match(T__87);
				setState(538);
				match(T__24);
				setState(539);
				match(DISTRIBUTION_TYPE);
				setState(540);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 7);
				{
				setState(541);
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
		enterRule(_localctx, 64, RULE_logicBlockProperty);
		int _la;
		try {
			setState(595);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(544);
				match(T__23);
				setState(545);
				match(T__24);
				setState(546);
				match(STRING);
				setState(547);
				match(T__25);
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 2);
				{
				setState(548);
				match(T__68);
				setState(549);
				match(T__24);
				setState(550);
				match(COMPONENT_SCOPE);
				setState(551);
				match(T__25);
				}
				break;
			case T__85:
				enterOuterAlt(_localctx, 3);
				{
				setState(552);
				match(T__85);
				setState(553);
				match(T__24);
				setState(554);
				match(LOGIC_BLOCK_TYPE);
				setState(555);
				match(T__25);
				}
				break;
			case T__88:
				enterOuterAlt(_localctx, 4);
				{
				setState(556);
				match(T__88);
				setState(557);
				match(T__24);
				setState(558);
				match(STRING);
				setState(559);
				match(T__25);
				}
				break;
			case T__89:
				enterOuterAlt(_localctx, 5);
				{
				setState(560);
				match(T__89);
				setState(561);
				match(T__24);
				setState(562);
				match(STRING);
				setState(563);
				match(T__25);
				}
				break;
			case T__90:
				enterOuterAlt(_localctx, 6);
				{
				setState(564);
				match(T__90);
				setState(565);
				match(T__24);
				setState(566);
				match(T__33);
				setState(567);
				match(STRING);
				setState(572);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(568);
					match(T__34);
					setState(569);
					match(STRING);
					}
					}
					setState(574);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(575);
				match(T__35);
				setState(576);
				match(T__25);
				}
				break;
			case T__91:
				enterOuterAlt(_localctx, 7);
				{
				setState(577);
				match(T__91);
				setState(578);
				match(T__24);
				setState(579);
				match(T__33);
				setState(580);
				match(STRING);
				setState(585);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(581);
					match(T__34);
					setState(582);
					match(STRING);
					}
					}
					setState(587);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(588);
				match(T__35);
				setState(589);
				match(T__25);
				}
				break;
			case T__92:
				enterOuterAlt(_localctx, 8);
				{
				setState(590);
				match(T__92);
				setState(591);
				match(T__24);
				setState(592);
				match(NUMBER);
				setState(593);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 9);
				{
				setState(594);
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
		enterRule(_localctx, 66, RULE_ruleBlockProperty);
		try {
			setState(614);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(597);
				match(T__23);
				setState(598);
				match(T__24);
				setState(599);
				match(STRING);
				setState(600);
				match(T__25);
				}
				break;
			case T__68:
				enterOuterAlt(_localctx, 2);
				{
				setState(601);
				match(T__68);
				setState(602);
				match(T__24);
				setState(603);
				match(COMPONENT_SCOPE);
				setState(604);
				match(T__25);
				}
				break;
			case T__93:
				enterOuterAlt(_localctx, 3);
				{
				setState(605);
				match(T__93);
				setState(606);
				match(T__24);
				setState(607);
				match(STRING);
				setState(608);
				match(T__25);
				}
				break;
			case T__94:
				enterOuterAlt(_localctx, 4);
				{
				setState(609);
				match(T__94);
				setState(610);
				match(T__24);
				setState(611);
				match(STRING);
				setState(612);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(613);
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
		enterRule(_localctx, 68, RULE_scheduleProperty);
		try {
			setState(637);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(616);
				match(T__23);
				setState(617);
				match(T__24);
				setState(618);
				match(STRING);
				setState(619);
				match(T__25);
				}
				break;
			case T__85:
				enterOuterAlt(_localctx, 2);
				{
				setState(620);
				match(T__85);
				setState(621);
				match(T__24);
				setState(622);
				match(SCHEDULE_TYPE);
				setState(623);
				match(T__25);
				}
				break;
			case T__95:
				enterOuterAlt(_localctx, 3);
				{
				setState(624);
				match(T__95);
				setState(625);
				match(T__24);
				setState(626);
				match(FREQUENCY);
				setState(627);
				match(T__25);
				}
				break;
			case T__96:
				enterOuterAlt(_localctx, 4);
				{
				setState(628);
				match(T__96);
				setState(629);
				match(T__24);
				setState(630);
				match(STRING);
				setState(631);
				match(T__25);
				}
				break;
			case T__97:
				enterOuterAlt(_localctx, 5);
				{
				setState(632);
				match(T__97);
				setState(633);
				match(T__24);
				setState(634);
				match(STRING);
				setState(635);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 6);
				{
				setState(636);
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
		enterRule(_localctx, 70, RULE_eventTriggerProperty);
		try {
			setState(656);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(639);
				match(T__23);
				setState(640);
				match(T__24);
				setState(641);
				match(STRING);
				setState(642);
				match(T__25);
				}
				break;
			case T__98:
				enterOuterAlt(_localctx, 2);
				{
				setState(643);
				match(T__98);
				setState(644);
				match(T__24);
				setState(645);
				match(STRING);
				setState(646);
				match(T__25);
				}
				break;
			case T__99:
				enterOuterAlt(_localctx, 3);
				{
				setState(647);
				match(T__99);
				setState(648);
				match(T__24);
				setState(649);
				match(TRIGGER_OPERATOR);
				setState(650);
				match(T__25);
				}
				break;
			case T__100:
				enterOuterAlt(_localctx, 4);
				{
				setState(651);
				match(T__100);
				setState(652);
				match(T__24);
				setState(653);
				match(NUMBER);
				setState(654);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(655);
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
		enterRule(_localctx, 72, RULE_templateProperty);
		try {
			setState(671);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(658);
				match(T__23);
				setState(659);
				match(T__24);
				setState(660);
				match(STRING);
				setState(661);
				match(T__25);
				}
				break;
			case T__101:
				enterOuterAlt(_localctx, 2);
				{
				setState(662);
				match(T__101);
				setState(663);
				match(T__24);
				setState(664);
				match(TEMPLATE_TYPE);
				setState(665);
				match(T__25);
				}
				break;
			case T__102:
				enterOuterAlt(_localctx, 3);
				{
				setState(666);
				match(T__102);
				setState(667);
				match(T__24);
				setState(668);
				match(STRING);
				setState(669);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(670);
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
		enterRule(_localctx, 74, RULE_contractProperty);
		int _la;
		try {
			setState(695);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(673);
				match(T__23);
				setState(674);
				match(T__24);
				setState(675);
				match(STRING);
				setState(676);
				match(T__25);
				}
				break;
			case T__103:
				enterOuterAlt(_localctx, 2);
				{
				setState(677);
				match(T__103);
				setState(678);
				match(T__24);
				setState(679);
				match(T__33);
				setState(680);
				match(IDENTIFIER);
				setState(685);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(681);
					match(T__34);
					setState(682);
					match(IDENTIFIER);
					}
					}
					setState(687);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(688);
				match(T__35);
				setState(689);
				match(T__25);
				}
				break;
			case T__104:
				enterOuterAlt(_localctx, 3);
				{
				setState(690);
				match(T__104);
				setState(691);
				match(T__24);
				setState(692);
				match(STRING);
				setState(693);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(694);
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
		enterRule(_localctx, 76, RULE_partyProperty);
		try {
			setState(710);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(697);
				match(T__23);
				setState(698);
				match(T__24);
				setState(699);
				match(STRING);
				setState(700);
				match(T__25);
				}
				break;
			case T__105:
				enterOuterAlt(_localctx, 2);
				{
				setState(701);
				match(T__105);
				setState(702);
				match(T__24);
				setState(703);
				match(PARTY_ROLE);
				setState(704);
				match(T__25);
				}
				break;
			case T__106:
				enterOuterAlt(_localctx, 3);
				{
				setState(705);
				match(T__106);
				setState(706);
				match(T__24);
				setState(707);
				match(STRING);
				setState(708);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
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
		enterRule(_localctx, 78, RULE_fundProperty);
		try {
			setState(725);
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
			case T__107:
				enterOuterAlt(_localctx, 2);
				{
				setState(716);
				match(T__107);
				setState(717);
				match(T__24);
				setState(718);
				match(FUND_PARTICIPANT_ROLE);
				setState(719);
				match(T__25);
				}
				break;
			case T__108:
				enterOuterAlt(_localctx, 3);
				{
				setState(720);
				match(T__108);
				setState(721);
				match(T__24);
				setState(722);
				match(NUMBER);
				setState(723);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(724);
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
		enterRule(_localctx, 80, RULE_portfolioProperty);
		int _la;
		try {
			setState(745);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(727);
				match(T__23);
				setState(728);
				match(T__24);
				setState(729);
				match(STRING);
				setState(730);
				match(T__25);
				}
				break;
			case T__109:
				enterOuterAlt(_localctx, 2);
				{
				setState(731);
				match(T__109);
				setState(732);
				match(T__24);
				setState(733);
				match(T__33);
				setState(734);
				match(IDENTIFIER);
				setState(739);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(735);
					match(T__34);
					setState(736);
					match(IDENTIFIER);
					}
					}
					setState(741);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(742);
				match(T__35);
				setState(743);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(744);
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
		enterRule(_localctx, 82, RULE_capitalStackProperty);
		try {
			setState(756);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(747);
				match(T__23);
				setState(748);
				match(T__24);
				setState(749);
				match(STRING);
				setState(750);
				match(T__25);
				}
				break;
			case T__110:
				enterOuterAlt(_localctx, 2);
				{
				setState(751);
				match(T__110);
				setState(752);
				match(T__24);
				setState(753);
				match(NUMBER);
				setState(754);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(755);
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
		enterRule(_localctx, 84, RULE_waterFallProperty);
		try {
			setState(767);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(758);
				match(T__23);
				setState(759);
				match(T__24);
				setState(760);
				match(STRING);
				setState(761);
				match(T__25);
				}
				break;
			case T__111:
				enterOuterAlt(_localctx, 2);
				{
				setState(762);
				match(T__111);
				setState(763);
				match(T__24);
				setState(764);
				match(STRING);
				setState(765);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(766);
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
		enterRule(_localctx, 86, RULE_metricProperty);
		int _la;
		try {
			setState(782);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__23:
				enterOuterAlt(_localctx, 1);
				{
				setState(769);
				match(T__23);
				setState(770);
				match(T__24);
				setState(771);
				match(STRING);
				setState(772);
				match(T__25);
				}
				break;
			case T__85:
				enterOuterAlt(_localctx, 2);
				{
				setState(773);
				match(T__85);
				setState(774);
				match(T__24);
				setState(775);
				match(METRIC_TYPE);
				setState(776);
				match(T__25);
				}
				break;
			case T__112:
				enterOuterAlt(_localctx, 3);
				{
				setState(777);
				match(T__112);
				setState(778);
				match(T__24);
				setState(779);
				_la = _input.LA(1);
				if ( !(_la==STRING || _la==PAYBACK_UNIT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(780);
				match(T__25);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 4);
				{
				setState(781);
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
		enterRule(_localctx, 88, RULE_genericProperty);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(784);
			match(IDENTIFIER);
			setState(785);
			match(T__24);
			setState(786);
			value();
			setState(787);
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
		enterRule(_localctx, 90, RULE_statement);
		try {
			setState(799);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(789);
				propertyAssign();
				setState(790);
				match(T__25);
				}
				break;
			case T__113:
				enterOuterAlt(_localctx, 2);
				{
				setState(792);
				logicBlockDef();
				}
				break;
			case T__114:
				enterOuterAlt(_localctx, 3);
				{
				setState(793);
				referenceDecl();
				setState(794);
				match(T__25);
				}
				break;
			case T__115:
				enterOuterAlt(_localctx, 4);
				{
				setState(796);
				importDecl();
				setState(797);
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
		enterRule(_localctx, 92, RULE_propertyDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(801);
			match(IDENTIFIER);
			setState(802);
			match(T__24);
			setState(803);
			typeRef();
			setState(806);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__24) {
				{
				setState(804);
				match(T__24);
				setState(805);
				value();
				}
			}

			setState(808);
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
		enterRule(_localctx, 94, RULE_propertyAssign);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(810);
			match(IDENTIFIER);
			setState(811);
			match(T__24);
			setState(812);
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
		enterRule(_localctx, 96, RULE_logicBlockDef);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(814);
			match(T__113);
			setState(815);
			match(IDENTIFIER);
			setState(816);
			match(T__4);
			setState(820);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 114)) & ~0x3f) == 0 && ((1L << (_la - 114)) & 15L) != 0)) {
				{
				{
				setState(817);
				statement();
				}
				}
				setState(822);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(823);
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
		enterRule(_localctx, 98, RULE_referenceDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825);
			match(T__114);
			setState(826);
			match(T__24);
			setState(827);
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
		enterRule(_localctx, 100, RULE_importDecl);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(829);
			match(T__115);
			setState(830);
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
		enterRule(_localctx, 102, RULE_typeRef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(832);
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
		enterRule(_localctx, 104, RULE_value);
		int _la;
		try {
			setState(859);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(834);
				match(NUMBER);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(835);
				match(STRING);
				}
				break;
			case BOOLEAN:
				enterOuterAlt(_localctx, 3);
				{
				setState(836);
				match(BOOLEAN);
				}
				break;
			case T__33:
				enterOuterAlt(_localctx, 4);
				{
				setState(837);
				match(T__33);
				setState(838);
				value();
				setState(843);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(839);
					match(T__34);
					setState(840);
					value();
					}
					}
					setState(845);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(846);
				match(T__35);
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 5);
				{
				setState(848);
				match(T__4);
				setState(849);
				propertyAssign();
				setState(854);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__34) {
					{
					{
					setState(850);
					match(T__34);
					setState(851);
					propertyAssign();
					}
					}
					setState(856);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(857);
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
		"\u0004\u0001\u0094\u035e\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u0001\u0000\u0004\u0000l\b"+
		"\u0000\u000b\u0000\f\u0000m\u0001\u0000\u0001\u0000\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0084"+
		"\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u008a"+
		"\b\u0002\n\u0002\f\u0002\u008d\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003\u0095\b\u0003\n\u0003"+
		"\f\u0003\u0098\t\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0005\u0004\u00a0\b\u0004\n\u0004\f\u0004\u00a3"+
		"\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0005\u0005\u00ab\b\u0005\n\u0005\f\u0005\u00ae\t\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006"+
		"\u00b6\b\u0006\n\u0006\f\u0006\u00b9\t\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0005\u0007\u00c1\b\u0007\n"+
		"\u0007\f\u0007\u00c4\t\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0005\b\u00cc\b\b\n\b\f\b\u00cf\t\b\u0001\b\u0001\b\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0005\t\u00d7\b\t\n\t\f\t\u00da\t\t\u0001\t\u0001"+
		"\t\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u00e2\b\n\n\n\f\n\u00e5\t\n"+
		"\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0005"+
		"\u000b\u00ed\b\u000b\n\u000b\f\u000b\u00f0\t\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u00f8\b\f\n\f\f\f\u00fb\t\f\u0001"+
		"\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u0103\b\r\n\r\f\r\u0106"+
		"\t\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005"+
		"\u000e\u010e\b\u000e\n\u000e\f\u000e\u0111\t\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u0119\b\u000f"+
		"\n\u000f\f\u000f\u011c\t\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0005\u0010\u0124\b\u0010\n\u0010\f\u0010"+
		"\u0127\t\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0005\u0011\u012f\b\u0011\n\u0011\f\u0011\u0132\t\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005"+
		"\u0012\u013a\b\u0012\n\u0012\f\u0012\u013d\t\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013\u0145\b\u0013"+
		"\n\u0013\f\u0013\u0148\t\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u016f\b\u0014\n"+
		"\u0014\f\u0014\u0172\t\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u017c"+
		"\b\u0014\n\u0014\f\u0014\u017f\t\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0003\u0014\u0184\b\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u019f\b\u0016\n\u0016"+
		"\f\u0016\u01a2\t\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u01ac\b\u0016"+
		"\n\u0016\f\u0016\u01af\t\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0003\u0016\u01b9"+
		"\b\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u01d1"+
		"\b\u0019\n\u0019\f\u0019\u01d4\t\u0019\u0001\u0019\u0001\u0019\u0001\u0019"+
		"\u0003\u0019\u01d9\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a"+
		"\u01fc\b\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001d"+
		"\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u021f\b\u001f"+
		"\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001"+
		" \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001"+
		" \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0005 \u023b\b \n \f \u023e"+
		"\t \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0005 \u0248"+
		"\b \n \f \u024b\t \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0003"+
		" \u0254\b \u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001"+
		"!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0001!\u0003!\u0267"+
		"\b!\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001\"\u0001"+
		"\"\u0001\"\u0001\"\u0001\"\u0003\"\u027e\b\"\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001#\u0001"+
		"#\u0001#\u0001#\u0001#\u0003#\u0291\b#\u0001$\u0001$\u0001$\u0001$\u0001"+
		"$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0001$\u0003$\u02a0"+
		"\b$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0005%\u02ac\b%\n%\f%\u02af\t%\u0001%\u0001%\u0001%\u0001%\u0001%\u0001"+
		"%\u0001%\u0003%\u02b8\b%\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001"+
		"&\u0001&\u0001&\u0001&\u0001&\u0001&\u0001&\u0003&\u02c7\b&\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001"+
		"\'\u0001\'\u0001\'\u0003\'\u02d6\b\'\u0001(\u0001(\u0001(\u0001(\u0001"+
		"(\u0001(\u0001(\u0001(\u0001(\u0001(\u0005(\u02e2\b(\n(\f(\u02e5\t(\u0001"+
		"(\u0001(\u0001(\u0003(\u02ea\b(\u0001)\u0001)\u0001)\u0001)\u0001)\u0001"+
		")\u0001)\u0001)\u0001)\u0003)\u02f5\b)\u0001*\u0001*\u0001*\u0001*\u0001"+
		"*\u0001*\u0001*\u0001*\u0001*\u0003*\u0300\b*\u0001+\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0001+\u0003"+
		"+\u030f\b+\u0001,\u0001,\u0001,\u0001,\u0001,\u0001-\u0001-\u0001-\u0001"+
		"-\u0001-\u0001-\u0001-\u0001-\u0001-\u0001-\u0003-\u0320\b-\u0001.\u0001"+
		".\u0001.\u0001.\u0001.\u0003.\u0327\b.\u0001.\u0001.\u0001/\u0001/\u0001"+
		"/\u0001/\u00010\u00010\u00010\u00010\u00050\u0333\b0\n0\f0\u0336\t0\u0001"+
		"0\u00010\u00011\u00011\u00011\u00011\u00012\u00012\u00012\u00013\u0001"+
		"3\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00054\u034a\b4\n4"+
		"\f4\u034d\t4\u00014\u00014\u00014\u00014\u00014\u00014\u00054\u0355\b"+
		"4\n4\f4\u0358\t4\u00014\u00014\u00034\u035c\b4\u00014\u0000\u00005\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfh\u0000\u000b\u0001\u0000"+
		"\u0001\u0004\u0001\u0000&2\u0001\u00008?\u0001\u0000@C\u0001\u0000uv\u0002"+
		"\u0000\u0007\t\u0013\u0014\u0001\u0000IK\u0001\u0000LQ\u0001\u0000RU\u0001"+
		"\u0000uw\u0002\u0000ww\u0091\u0091\u03ab\u0000k\u0001\u0000\u0000\u0000"+
		"\u0002\u0083\u0001\u0000\u0000\u0000\u0004\u0085\u0001\u0000\u0000\u0000"+
		"\u0006\u0090\u0001\u0000\u0000\u0000\b\u009b\u0001\u0000\u0000\u0000\n"+
		"\u00a6\u0001\u0000\u0000\u0000\f\u00b1\u0001\u0000\u0000\u0000\u000e\u00bc"+
		"\u0001\u0000\u0000\u0000\u0010\u00c7\u0001\u0000\u0000\u0000\u0012\u00d2"+
		"\u0001\u0000\u0000\u0000\u0014\u00dd\u0001\u0000\u0000\u0000\u0016\u00e8"+
		"\u0001\u0000\u0000\u0000\u0018\u00f3\u0001\u0000\u0000\u0000\u001a\u00fe"+
		"\u0001\u0000\u0000\u0000\u001c\u0109\u0001\u0000\u0000\u0000\u001e\u0114"+
		"\u0001\u0000\u0000\u0000 \u011f\u0001\u0000\u0000\u0000\"\u012a\u0001"+
		"\u0000\u0000\u0000$\u0135\u0001\u0000\u0000\u0000&\u0140\u0001\u0000\u0000"+
		"\u0000(\u0183\u0001\u0000\u0000\u0000*\u0185\u0001\u0000\u0000\u0000,"+
		"\u01b8\u0001\u0000\u0000\u0000.\u01ba\u0001\u0000\u0000\u00000\u01bc\u0001"+
		"\u0000\u0000\u00002\u01d8\u0001\u0000\u0000\u00004\u01fb\u0001\u0000\u0000"+
		"\u00006\u01fd\u0001\u0000\u0000\u00008\u01ff\u0001\u0000\u0000\u0000:"+
		"\u0201\u0001\u0000\u0000\u0000<\u0203\u0001\u0000\u0000\u0000>\u021e\u0001"+
		"\u0000\u0000\u0000@\u0253\u0001\u0000\u0000\u0000B\u0266\u0001\u0000\u0000"+
		"\u0000D\u027d\u0001\u0000\u0000\u0000F\u0290\u0001\u0000\u0000\u0000H"+
		"\u029f\u0001\u0000\u0000\u0000J\u02b7\u0001\u0000\u0000\u0000L\u02c6\u0001"+
		"\u0000\u0000\u0000N\u02d5\u0001\u0000\u0000\u0000P\u02e9\u0001\u0000\u0000"+
		"\u0000R\u02f4\u0001\u0000\u0000\u0000T\u02ff\u0001\u0000\u0000\u0000V"+
		"\u030e\u0001\u0000\u0000\u0000X\u0310\u0001\u0000\u0000\u0000Z\u031f\u0001"+
		"\u0000\u0000\u0000\\\u0321\u0001\u0000\u0000\u0000^\u032a\u0001\u0000"+
		"\u0000\u0000`\u032e\u0001\u0000\u0000\u0000b\u0339\u0001\u0000\u0000\u0000"+
		"d\u033d\u0001\u0000\u0000\u0000f\u0340\u0001\u0000\u0000\u0000h\u035b"+
		"\u0001\u0000\u0000\u0000jl\u0003\u0002\u0001\u0000kj\u0001\u0000\u0000"+
		"\u0000lm\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000mn\u0001\u0000"+
		"\u0000\u0000no\u0001\u0000\u0000\u0000op\u0005\u0000\u0000\u0001p\u0001"+
		"\u0001\u0000\u0000\u0000q\u0084\u0003\u0006\u0003\u0000r\u0084\u0003\b"+
		"\u0004\u0000s\u0084\u0003\n\u0005\u0000t\u0084\u0003\f\u0006\u0000u\u0084"+
		"\u0003\u000e\u0007\u0000v\u0084\u0003\u0010\b\u0000w\u0084\u0003\u0012"+
		"\t\u0000x\u0084\u0003\u0014\n\u0000y\u0084\u0003\u0016\u000b\u0000z\u0084"+
		"\u0003\u0018\f\u0000{\u0084\u0003\u001a\r\u0000|\u0084\u0003\u001c\u000e"+
		"\u0000}\u0084\u0003\u001e\u000f\u0000~\u0084\u0003 \u0010\u0000\u007f"+
		"\u0084\u0003\"\u0011\u0000\u0080\u0084\u0003$\u0012\u0000\u0081\u0084"+
		"\u0003&\u0013\u0000\u0082\u0084\u0003\u0004\u0002\u0000\u0083q\u0001\u0000"+
		"\u0000\u0000\u0083r\u0001\u0000\u0000\u0000\u0083s\u0001\u0000\u0000\u0000"+
		"\u0083t\u0001\u0000\u0000\u0000\u0083u\u0001\u0000\u0000\u0000\u0083v"+
		"\u0001\u0000\u0000\u0000\u0083w\u0001\u0000\u0000\u0000\u0083x\u0001\u0000"+
		"\u0000\u0000\u0083y\u0001\u0000\u0000\u0000\u0083z\u0001\u0000\u0000\u0000"+
		"\u0083{\u0001\u0000\u0000\u0000\u0083|\u0001\u0000\u0000\u0000\u0083}"+
		"\u0001\u0000\u0000\u0000\u0083~\u0001\u0000\u0000\u0000\u0083\u007f\u0001"+
		"\u0000\u0000\u0000\u0083\u0080\u0001\u0000\u0000\u0000\u0083\u0081\u0001"+
		"\u0000\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0084\u0003\u0001"+
		"\u0000\u0000\u0000\u0085\u0086\u0007\u0000\u0000\u0000\u0086\u0087\u0005"+
		"u\u0000\u0000\u0087\u008b\u0005\u0005\u0000\u0000\u0088\u008a\u0003Z-"+
		"\u0000\u0089\u0088\u0001\u0000\u0000\u0000\u008a\u008d\u0001\u0000\u0000"+
		"\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008b\u008c\u0001\u0000\u0000"+
		"\u0000\u008c\u008e\u0001\u0000\u0000\u0000\u008d\u008b\u0001\u0000\u0000"+
		"\u0000\u008e\u008f\u0005\u0006\u0000\u0000\u008f\u0005\u0001\u0000\u0000"+
		"\u0000\u0090\u0091\u0005\u0007\u0000\u0000\u0091\u0092\u0005u\u0000\u0000"+
		"\u0092\u0096\u0005\u0005\u0000\u0000\u0093\u0095\u0003(\u0014\u0000\u0094"+
		"\u0093\u0001\u0000\u0000\u0000\u0095\u0098\u0001\u0000\u0000\u0000\u0096"+
		"\u0094\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000\u0000\u0000\u0097"+
		"\u0099\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000\u0000\u0099"+
		"\u009a\u0005\u0006\u0000\u0000\u009a\u0007\u0001\u0000\u0000\u0000\u009b"+
		"\u009c\u0005\b\u0000\u0000\u009c\u009d\u0005u\u0000\u0000\u009d\u00a1"+
		"\u0005\u0005\u0000\u0000\u009e\u00a0\u0003,\u0016\u0000\u009f\u009e\u0001"+
		"\u0000\u0000\u0000\u00a0\u00a3\u0001\u0000\u0000\u0000\u00a1\u009f\u0001"+
		"\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2\u00a4\u0001"+
		"\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005"+
		"\u0006\u0000\u0000\u00a5\t\u0001\u0000\u0000\u0000\u00a6\u00a7\u0005\t"+
		"\u0000\u0000\u00a7\u00a8\u0005u\u0000\u0000\u00a8\u00ac\u0005\u0005\u0000"+
		"\u0000\u00a9\u00ab\u00032\u0019\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000"+
		"\u00ab\u00ae\u0001\u0000\u0000\u0000\u00ac\u00aa\u0001\u0000\u0000\u0000"+
		"\u00ac\u00ad\u0001\u0000\u0000\u0000\u00ad\u00af\u0001\u0000\u0000\u0000"+
		"\u00ae\u00ac\u0001\u0000\u0000\u0000\u00af\u00b0\u0005\u0006\u0000\u0000"+
		"\u00b0\u000b\u0001\u0000\u0000\u0000\u00b1\u00b2\u0005\n\u0000\u0000\u00b2"+
		"\u00b3\u0005u\u0000\u0000\u00b3\u00b7\u0005\u0005\u0000\u0000\u00b4\u00b6"+
		"\u00034\u001a\u0000\u00b5\u00b4\u0001\u0000\u0000\u0000\u00b6\u00b9\u0001"+
		"\u0000\u0000\u0000\u00b7\u00b5\u0001\u0000\u0000\u0000\u00b7\u00b8\u0001"+
		"\u0000\u0000\u0000\u00b8\u00ba\u0001\u0000\u0000\u0000\u00b9\u00b7\u0001"+
		"\u0000\u0000\u0000\u00ba\u00bb\u0005\u0006\u0000\u0000\u00bb\r\u0001\u0000"+
		"\u0000\u0000\u00bc\u00bd\u0005\u000b\u0000\u0000\u00bd\u00be\u0005u\u0000"+
		"\u0000\u00be\u00c2\u0005\u0005\u0000\u0000\u00bf\u00c1\u0003>\u001f\u0000"+
		"\u00c0\u00bf\u0001\u0000\u0000\u0000\u00c1\u00c4\u0001\u0000\u0000\u0000"+
		"\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c2\u00c3\u0001\u0000\u0000\u0000"+
		"\u00c3\u00c5\u0001\u0000\u0000\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000"+
		"\u00c5\u00c6\u0005\u0006\u0000\u0000\u00c6\u000f\u0001\u0000\u0000\u0000"+
		"\u00c7\u00c8\u0005\f\u0000\u0000\u00c8\u00c9\u0005u\u0000\u0000\u00c9"+
		"\u00cd\u0005\u0005\u0000\u0000\u00ca\u00cc\u0003@ \u0000\u00cb\u00ca\u0001"+
		"\u0000\u0000\u0000\u00cc\u00cf\u0001\u0000\u0000\u0000\u00cd\u00cb\u0001"+
		"\u0000\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00d0\u0001"+
		"\u0000\u0000\u0000\u00cf\u00cd\u0001\u0000\u0000\u0000\u00d0\u00d1\u0005"+
		"\u0006\u0000\u0000\u00d1\u0011\u0001\u0000\u0000\u0000\u00d2\u00d3\u0005"+
		"\r\u0000\u0000\u00d3\u00d4\u0005u\u0000\u0000\u00d4\u00d8\u0005\u0005"+
		"\u0000\u0000\u00d5\u00d7\u0003B!\u0000\u00d6\u00d5\u0001\u0000\u0000\u0000"+
		"\u00d7\u00da\u0001\u0000\u0000\u0000\u00d8\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00db\u0001\u0000\u0000\u0000"+
		"\u00da\u00d8\u0001\u0000\u0000\u0000\u00db\u00dc\u0005\u0006\u0000\u0000"+
		"\u00dc\u0013\u0001\u0000\u0000\u0000\u00dd\u00de\u0005\u000e\u0000\u0000"+
		"\u00de\u00df\u0005u\u0000\u0000\u00df\u00e3\u0005\u0005\u0000\u0000\u00e0"+
		"\u00e2\u0003D\"\u0000\u00e1\u00e0\u0001\u0000\u0000\u0000\u00e2\u00e5"+
		"\u0001\u0000\u0000\u0000\u00e3\u00e1\u0001\u0000\u0000\u0000\u00e3\u00e4"+
		"\u0001\u0000\u0000\u0000\u00e4\u00e6\u0001\u0000\u0000\u0000\u00e5\u00e3"+
		"\u0001\u0000\u0000\u0000\u00e6\u00e7\u0005\u0006\u0000\u0000\u00e7\u0015"+
		"\u0001\u0000\u0000\u0000\u00e8\u00e9\u0005\u000f\u0000\u0000\u00e9\u00ea"+
		"\u0005u\u0000\u0000\u00ea\u00ee\u0005\u0005\u0000\u0000\u00eb\u00ed\u0003"+
		"F#\u0000\u00ec\u00eb\u0001\u0000\u0000\u0000\u00ed\u00f0\u0001\u0000\u0000"+
		"\u0000\u00ee\u00ec\u0001\u0000\u0000\u0000\u00ee\u00ef\u0001\u0000\u0000"+
		"\u0000\u00ef\u00f1\u0001\u0000\u0000\u0000\u00f0\u00ee\u0001\u0000\u0000"+
		"\u0000\u00f1\u00f2\u0005\u0006\u0000\u0000\u00f2\u0017\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f4\u0005\u0010\u0000\u0000\u00f4\u00f5\u0005u\u0000\u0000"+
		"\u00f5\u00f9\u0005\u0005\u0000\u0000\u00f6\u00f8\u0003H$\u0000\u00f7\u00f6"+
		"\u0001\u0000\u0000\u0000\u00f8\u00fb\u0001\u0000\u0000\u0000\u00f9\u00f7"+
		"\u0001\u0000\u0000\u0000\u00f9\u00fa\u0001\u0000\u0000\u0000\u00fa\u00fc"+
		"\u0001\u0000\u0000\u0000\u00fb\u00f9\u0001\u0000\u0000\u0000\u00fc\u00fd"+
		"\u0005\u0006\u0000\u0000\u00fd\u0019\u0001\u0000\u0000\u0000\u00fe\u00ff"+
		"\u0005\u0011\u0000\u0000\u00ff\u0100\u0005u\u0000\u0000\u0100\u0104\u0005"+
		"\u0005\u0000\u0000\u0101\u0103\u0003J%\u0000\u0102\u0101\u0001\u0000\u0000"+
		"\u0000\u0103\u0106\u0001\u0000\u0000\u0000\u0104\u0102\u0001\u0000\u0000"+
		"\u0000\u0104\u0105\u0001\u0000\u0000\u0000\u0105\u0107\u0001\u0000\u0000"+
		"\u0000\u0106\u0104\u0001\u0000\u0000\u0000\u0107\u0108\u0005\u0006\u0000"+
		"\u0000\u0108\u001b\u0001\u0000\u0000\u0000\u0109\u010a\u0005\u0012\u0000"+
		"\u0000\u010a\u010b\u0005u\u0000\u0000\u010b\u010f\u0005\u0005\u0000\u0000"+
		"\u010c\u010e\u0003L&\u0000\u010d\u010c\u0001\u0000\u0000\u0000\u010e\u0111"+
		"\u0001\u0000\u0000\u0000\u010f\u010d\u0001\u0000\u0000\u0000\u010f\u0110"+
		"\u0001\u0000\u0000\u0000\u0110\u0112\u0001\u0000\u0000\u0000\u0111\u010f"+
		"\u0001\u0000\u0000\u0000\u0112\u0113\u0005\u0006\u0000\u0000\u0113\u001d"+
		"\u0001\u0000\u0000\u0000\u0114\u0115\u0005\u0013\u0000\u0000\u0115\u0116"+
		"\u0005u\u0000\u0000\u0116\u011a\u0005\u0005\u0000\u0000\u0117\u0119\u0003"+
		"N\'\u0000\u0118\u0117\u0001\u0000\u0000\u0000\u0119\u011c\u0001\u0000"+
		"\u0000\u0000\u011a\u0118\u0001\u0000\u0000\u0000\u011a\u011b\u0001\u0000"+
		"\u0000\u0000\u011b\u011d\u0001\u0000\u0000\u0000\u011c\u011a\u0001\u0000"+
		"\u0000\u0000\u011d\u011e\u0005\u0006\u0000\u0000\u011e\u001f\u0001\u0000"+
		"\u0000\u0000\u011f\u0120\u0005\u0014\u0000\u0000\u0120\u0121\u0005u\u0000"+
		"\u0000\u0121\u0125\u0005\u0005\u0000\u0000\u0122\u0124\u0003P(\u0000\u0123"+
		"\u0122\u0001\u0000\u0000\u0000\u0124\u0127\u0001\u0000\u0000\u0000\u0125"+
		"\u0123\u0001\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126"+
		"\u0128\u0001\u0000\u0000\u0000\u0127\u0125\u0001\u0000\u0000\u0000\u0128"+
		"\u0129\u0005\u0006\u0000\u0000\u0129!\u0001\u0000\u0000\u0000\u012a\u012b"+
		"\u0005\u0015\u0000\u0000\u012b\u012c\u0005u\u0000\u0000\u012c\u0130\u0005"+
		"\u0005\u0000\u0000\u012d\u012f\u0003R)\u0000\u012e\u012d\u0001\u0000\u0000"+
		"\u0000\u012f\u0132\u0001\u0000\u0000\u0000\u0130\u012e\u0001\u0000\u0000"+
		"\u0000\u0130\u0131\u0001\u0000\u0000\u0000\u0131\u0133\u0001\u0000\u0000"+
		"\u0000\u0132\u0130\u0001\u0000\u0000\u0000\u0133\u0134\u0005\u0006\u0000"+
		"\u0000\u0134#\u0001\u0000\u0000\u0000\u0135\u0136\u0005\u0016\u0000\u0000"+
		"\u0136\u0137\u0005u\u0000\u0000\u0137\u013b\u0005\u0005\u0000\u0000\u0138"+
		"\u013a\u0003T*\u0000\u0139\u0138\u0001\u0000\u0000\u0000\u013a\u013d\u0001"+
		"\u0000\u0000\u0000\u013b\u0139\u0001\u0000\u0000\u0000\u013b\u013c\u0001"+
		"\u0000\u0000\u0000\u013c\u013e\u0001\u0000\u0000\u0000\u013d\u013b\u0001"+
		"\u0000\u0000\u0000\u013e\u013f\u0005\u0006\u0000\u0000\u013f%\u0001\u0000"+
		"\u0000\u0000\u0140\u0141\u0005\u0017\u0000\u0000\u0141\u0142\u0005u\u0000"+
		"\u0000\u0142\u0146\u0005\u0005\u0000\u0000\u0143\u0145\u0003V+\u0000\u0144"+
		"\u0143\u0001\u0000\u0000\u0000\u0145\u0148\u0001\u0000\u0000\u0000\u0146"+
		"\u0144\u0001\u0000\u0000\u0000\u0146\u0147\u0001\u0000\u0000\u0000\u0147"+
		"\u0149\u0001\u0000\u0000\u0000\u0148\u0146\u0001\u0000\u0000\u0000\u0149"+
		"\u014a\u0005\u0006\u0000\u0000\u014a\'\u0001\u0000\u0000\u0000\u014b\u014c"+
		"\u0005\u0018\u0000\u0000\u014c\u014d\u0005\u0019\u0000\u0000\u014d\u014e"+
		"\u0005w\u0000\u0000\u014e\u0184\u0005\u001a\u0000\u0000\u014f\u0150\u0005"+
		"\u001b\u0000\u0000\u0150\u0151\u0005\u0019\u0000\u0000\u0151\u0152\u0003"+
		"*\u0015\u0000\u0152\u0153\u0005\u001a\u0000\u0000\u0153\u0184\u0001\u0000"+
		"\u0000\u0000\u0154\u0155\u0005\u001c\u0000\u0000\u0155\u0156\u0005\u0019"+
		"\u0000\u0000\u0156\u0157\u0005w\u0000\u0000\u0157\u0184\u0005\u001a\u0000"+
		"\u0000\u0158\u0159\u0005\u001d\u0000\u0000\u0159\u015a\u0005\u0019\u0000"+
		"\u0000\u015a\u015b\u0005w\u0000\u0000\u015b\u0184\u0005\u001a\u0000\u0000"+
		"\u015c\u015d\u0005\u001e\u0000\u0000\u015d\u015e\u0005\u0019\u0000\u0000"+
		"\u015e\u015f\u0005w\u0000\u0000\u015f\u0184\u0005\u001a\u0000\u0000\u0160"+
		"\u0161\u0005\u001f\u0000\u0000\u0161\u0162\u0005\u0019\u0000\u0000\u0162"+
		"\u0163\u0005w\u0000\u0000\u0163\u0184\u0005\u001a\u0000\u0000\u0164\u0165"+
		"\u0005 \u0000\u0000\u0165\u0166\u0005\u0019\u0000\u0000\u0166\u0167\u0005"+
		"v\u0000\u0000\u0167\u0184\u0005\u001a\u0000\u0000\u0168\u0169\u0005!\u0000"+
		"\u0000\u0169\u016a\u0005\u0019\u0000\u0000\u016a\u016b\u0005\"\u0000\u0000"+
		"\u016b\u0170\u0005u\u0000\u0000\u016c\u016d\u0005#\u0000\u0000\u016d\u016f"+
		"\u0005u\u0000\u0000\u016e\u016c\u0001\u0000\u0000\u0000\u016f\u0172\u0001"+
		"\u0000\u0000\u0000\u0170\u016e\u0001\u0000\u0000\u0000\u0170\u0171\u0001"+
		"\u0000\u0000\u0000\u0171\u0173\u0001\u0000\u0000\u0000\u0172\u0170\u0001"+
		"\u0000\u0000\u0000\u0173\u0174\u0005$\u0000\u0000\u0174\u0184\u0005\u001a"+
		"\u0000\u0000\u0175\u0176\u0005%\u0000\u0000\u0176\u0177\u0005\u0019\u0000"+
		"\u0000\u0177\u0178\u0005\"\u0000\u0000\u0178\u017d\u0005u\u0000\u0000"+
		"\u0179\u017a\u0005#\u0000\u0000\u017a\u017c\u0005u\u0000\u0000\u017b\u0179"+
		"\u0001\u0000\u0000\u0000\u017c\u017f\u0001\u0000\u0000\u0000\u017d\u017b"+
		"\u0001\u0000\u0000\u0000\u017d\u017e\u0001\u0000\u0000\u0000\u017e\u0180"+
		"\u0001\u0000\u0000\u0000\u017f\u017d\u0001\u0000\u0000\u0000\u0180\u0181"+
		"\u0005$\u0000\u0000\u0181\u0184\u0005\u001a\u0000\u0000\u0182\u0184\u0003"+
		"X,\u0000\u0183\u014b\u0001\u0000\u0000\u0000\u0183\u014f\u0001\u0000\u0000"+
		"\u0000\u0183\u0154\u0001\u0000\u0000\u0000\u0183\u0158\u0001\u0000\u0000"+
		"\u0000\u0183\u015c\u0001\u0000\u0000\u0000\u0183\u0160\u0001\u0000\u0000"+
		"\u0000\u0183\u0164\u0001\u0000\u0000\u0000\u0183\u0168\u0001\u0000\u0000"+
		"\u0000\u0183\u0175\u0001\u0000\u0000\u0000\u0183\u0182\u0001\u0000\u0000"+
		"\u0000\u0184)\u0001\u0000\u0000\u0000\u0185\u0186\u0007\u0001\u0000\u0000"+
		"\u0186+\u0001\u0000\u0000\u0000\u0187\u0188\u0005\u0018\u0000\u0000\u0188"+
		"\u0189\u0005\u0019\u0000\u0000\u0189\u018a\u0005w\u0000\u0000\u018a\u01b9"+
		"\u0005\u001a\u0000\u0000\u018b\u018c\u00053\u0000\u0000\u018c\u018d\u0005"+
		"\u0019\u0000\u0000\u018d\u018e\u0005u\u0000\u0000\u018e\u01b9\u0005\u001a"+
		"\u0000\u0000\u018f\u0190\u00054\u0000\u0000\u0190\u0191\u0005\u0019\u0000"+
		"\u0000\u0191\u0192\u0003.\u0017\u0000\u0192\u0193\u0005\u001a\u0000\u0000"+
		"\u0193\u01b9\u0001\u0000\u0000\u0000\u0194\u0195\u00055\u0000\u0000\u0195"+
		"\u0196\u0005\u0019\u0000\u0000\u0196\u0197\u0005w\u0000\u0000\u0197\u01b9"+
		"\u0005\u001a\u0000\u0000\u0198\u0199\u00056\u0000\u0000\u0199\u019a\u0005"+
		"\u0019\u0000\u0000\u019a\u019b\u0005\"\u0000\u0000\u019b\u01a0\u0005u"+
		"\u0000\u0000\u019c\u019d\u0005#\u0000\u0000\u019d\u019f\u0005u\u0000\u0000"+
		"\u019e\u019c\u0001\u0000\u0000\u0000\u019f\u01a2\u0001\u0000\u0000\u0000"+
		"\u01a0\u019e\u0001\u0000\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000\u0000"+
		"\u01a1\u01a3\u0001\u0000\u0000\u0000\u01a2\u01a0\u0001\u0000\u0000\u0000"+
		"\u01a3\u01a4\u0005$\u0000\u0000\u01a4\u01b9\u0005\u001a\u0000\u0000\u01a5"+
		"\u01a6\u0005%\u0000\u0000\u01a6\u01a7\u0005\u0019\u0000\u0000\u01a7\u01a8"+
		"\u0005\"\u0000\u0000\u01a8\u01ad\u0005u\u0000\u0000\u01a9\u01aa\u0005"+
		"#\u0000\u0000\u01aa\u01ac\u0005u\u0000\u0000\u01ab\u01a9\u0001\u0000\u0000"+
		"\u0000\u01ac\u01af\u0001\u0000\u0000\u0000\u01ad\u01ab\u0001\u0000\u0000"+
		"\u0000\u01ad\u01ae\u0001\u0000\u0000\u0000\u01ae\u01b0\u0001\u0000\u0000"+
		"\u0000\u01af\u01ad\u0001\u0000\u0000\u0000\u01b0\u01b1\u0005$\u0000\u0000"+
		"\u01b1\u01b9\u0005\u001a\u0000\u0000\u01b2\u01b3\u00057\u0000\u0000\u01b3"+
		"\u01b4\u0005\u0019\u0000\u0000\u01b4\u01b5\u00030\u0018\u0000\u01b5\u01b6"+
		"\u0005\u001a\u0000\u0000\u01b6\u01b9\u0001\u0000\u0000\u0000\u01b7\u01b9"+
		"\u0003X,\u0000\u01b8\u0187\u0001\u0000\u0000\u0000\u01b8\u018b\u0001\u0000"+
		"\u0000\u0000\u01b8\u018f\u0001\u0000\u0000\u0000\u01b8\u0194\u0001\u0000"+
		"\u0000\u0000\u01b8\u0198\u0001\u0000\u0000\u0000\u01b8\u01a5\u0001\u0000"+
		"\u0000\u0000\u01b8\u01b2\u0001\u0000\u0000\u0000\u01b8\u01b7\u0001\u0000"+
		"\u0000\u0000\u01b9-\u0001\u0000\u0000\u0000\u01ba\u01bb\u0007\u0002\u0000"+
		"\u0000\u01bb/\u0001\u0000\u0000\u0000\u01bc\u01bd\u0007\u0003\u0000\u0000"+
		"\u01bd1\u0001\u0000\u0000\u0000\u01be\u01bf\u0005\u0018\u0000\u0000\u01bf"+
		"\u01c0\u0005\u0019\u0000\u0000\u01c0\u01c1\u0005w\u0000\u0000\u01c1\u01d9"+
		"\u0005\u001a\u0000\u0000\u01c2\u01c3\u0005D\u0000\u0000\u01c3\u01c4\u0005"+
		"\u0019\u0000\u0000\u01c4\u01c5\u0005u\u0000\u0000\u01c5\u01d9\u0005\u001a"+
		"\u0000\u0000\u01c6\u01c7\u0005E\u0000\u0000\u01c7\u01c8\u0005\u0019\u0000"+
		"\u0000\u01c8\u01c9\u0005{\u0000\u0000\u01c9\u01d9\u0005\u001a\u0000\u0000"+
		"\u01ca\u01cb\u0005%\u0000\u0000\u01cb\u01cc\u0005\u0019\u0000\u0000\u01cc"+
		"\u01cd\u0005\"\u0000\u0000\u01cd\u01d2\u0005u\u0000\u0000\u01ce\u01cf"+
		"\u0005#\u0000\u0000\u01cf\u01d1\u0005u\u0000\u0000\u01d0\u01ce\u0001\u0000"+
		"\u0000\u0000\u01d1\u01d4\u0001\u0000\u0000\u0000\u01d2\u01d0\u0001\u0000"+
		"\u0000\u0000\u01d2\u01d3\u0001\u0000\u0000\u0000\u01d3\u01d5\u0001\u0000"+
		"\u0000\u0000\u01d4\u01d2\u0001\u0000\u0000\u0000\u01d5\u01d6\u0005$\u0000"+
		"\u0000\u01d6\u01d9\u0005\u001a\u0000\u0000\u01d7\u01d9\u0003X,\u0000\u01d8"+
		"\u01be\u0001\u0000\u0000\u0000\u01d8\u01c2\u0001\u0000\u0000\u0000\u01d8"+
		"\u01c6\u0001\u0000\u0000\u0000\u01d8\u01ca\u0001\u0000\u0000\u0000\u01d8"+
		"\u01d7\u0001\u0000\u0000\u0000\u01d93\u0001\u0000\u0000\u0000\u01da\u01db"+
		"\u0005\u0018\u0000\u0000\u01db\u01dc\u0005\u0019\u0000\u0000\u01dc\u01dd"+
		"\u0005w\u0000\u0000\u01dd\u01fc\u0005\u001a\u0000\u0000\u01de\u01df\u0005"+
		"E\u0000\u0000\u01df\u01e0\u0005\u0019\u0000\u0000\u01e0\u01e1\u00036\u001b"+
		"\u0000\u01e1\u01e2\u0005\u001a\u0000\u0000\u01e2\u01fc\u0001\u0000\u0000"+
		"\u0000\u01e3\u01e4\u00054\u0000\u0000\u01e4\u01e5\u0005\u0019\u0000\u0000"+
		"\u01e5\u01e6\u00038\u001c\u0000\u01e6\u01e7\u0005\u001a\u0000\u0000\u01e7"+
		"\u01fc\u0001\u0000\u0000\u0000\u01e8\u01e9\u0005F\u0000\u0000\u01e9\u01ea"+
		"\u0005\u0019\u0000\u0000\u01ea\u01eb\u0003:\u001d\u0000\u01eb\u01ec\u0005"+
		"\u001a\u0000\u0000\u01ec\u01fc\u0001\u0000\u0000\u0000\u01ed\u01ee\u0005"+
		"\u000e\u0000\u0000\u01ee\u01ef\u0005\u0019\u0000\u0000\u01ef\u01f0\u0005"+
		"u\u0000\u0000\u01f0\u01fc\u0005\u001a\u0000\u0000\u01f1\u01f2\u0005G\u0000"+
		"\u0000\u01f2\u01f3\u0005\u0019\u0000\u0000\u01f3\u01f4\u0007\u0004\u0000"+
		"\u0000\u01f4\u01fc\u0005\u001a\u0000\u0000\u01f5\u01f6\u0005H\u0000\u0000"+
		"\u01f6\u01f7\u0005\u0019\u0000\u0000\u01f7\u01f8\u0003<\u001e\u0000\u01f8"+
		"\u01f9\u0005\u001a\u0000\u0000\u01f9\u01fc\u0001\u0000\u0000\u0000\u01fa"+
		"\u01fc\u0003X,\u0000\u01fb\u01da\u0001\u0000\u0000\u0000\u01fb\u01de\u0001"+
		"\u0000\u0000\u0000\u01fb\u01e3\u0001\u0000\u0000\u0000\u01fb\u01e8\u0001"+
		"\u0000\u0000\u0000\u01fb\u01ed\u0001\u0000\u0000\u0000\u01fb\u01f1\u0001"+
		"\u0000\u0000\u0000\u01fb\u01f5\u0001\u0000\u0000\u0000\u01fb\u01fa\u0001"+
		"\u0000\u0000\u0000\u01fc5\u0001\u0000\u0000\u0000\u01fd\u01fe\u0007\u0005"+
		"\u0000\u0000\u01fe7\u0001\u0000\u0000\u0000\u01ff\u0200\u0007\u0006\u0000"+
		"\u0000\u02009\u0001\u0000\u0000\u0000\u0201\u0202\u0007\u0007\u0000\u0000"+
		"\u0202;\u0001\u0000\u0000\u0000\u0203\u0204\u0007\b\u0000\u0000\u0204"+
		"=\u0001\u0000\u0000\u0000\u0205\u0206\u0005\u0018\u0000\u0000\u0206\u0207"+
		"\u0005\u0019\u0000\u0000\u0207\u0208\u0005w\u0000\u0000\u0208\u021f\u0005"+
		"\u001a\u0000\u0000\u0209\u020a\u00054\u0000\u0000\u020a\u020b\u0005\u0019"+
		"\u0000\u0000\u020b\u020c\u0005|\u0000\u0000\u020c\u021f\u0005\u001a\u0000"+
		"\u0000\u020d\u020e\u0005E\u0000\u0000\u020e\u020f\u0005\u0019\u0000\u0000"+
		"\u020f\u0210\u0005{\u0000\u0000\u0210\u021f\u0005\u001a\u0000\u0000\u0211"+
		"\u0212\u0005V\u0000\u0000\u0212\u0213\u0005\u0019\u0000\u0000\u0213\u0214"+
		"\u0005\u0087\u0000\u0000\u0214\u021f\u0005\u001a\u0000\u0000\u0215\u0216"+
		"\u0005W\u0000\u0000\u0216\u0217\u0005\u0019\u0000\u0000\u0217\u0218\u0007"+
		"\t\u0000\u0000\u0218\u021f\u0005\u001a\u0000\u0000\u0219\u021a\u0005X"+
		"\u0000\u0000\u021a\u021b\u0005\u0019\u0000\u0000\u021b\u021c\u0005}\u0000"+
		"\u0000\u021c\u021f\u0005\u001a\u0000\u0000\u021d\u021f\u0003X,\u0000\u021e"+
		"\u0205\u0001\u0000\u0000\u0000\u021e\u0209\u0001\u0000\u0000\u0000\u021e"+
		"\u020d\u0001\u0000\u0000\u0000\u021e\u0211\u0001\u0000\u0000\u0000\u021e"+
		"\u0215\u0001\u0000\u0000\u0000\u021e\u0219\u0001\u0000\u0000\u0000\u021e"+
		"\u021d\u0001\u0000\u0000\u0000\u021f?\u0001\u0000\u0000\u0000\u0220\u0221"+
		"\u0005\u0018\u0000\u0000\u0221\u0222\u0005\u0019\u0000\u0000\u0222\u0223"+
		"\u0005w\u0000\u0000\u0223\u0254\u0005\u001a\u0000\u0000\u0224\u0225\u0005"+
		"E\u0000\u0000\u0225\u0226\u0005\u0019\u0000\u0000\u0226\u0227\u0005{\u0000"+
		"\u0000\u0227\u0254\u0005\u001a\u0000\u0000\u0228\u0229\u0005V\u0000\u0000"+
		"\u0229\u022a\u0005\u0019\u0000\u0000\u022a\u022b\u0005~\u0000\u0000\u022b"+
		"\u0254\u0005\u001a\u0000\u0000\u022c\u022d\u0005Y\u0000\u0000\u022d\u022e"+
		"\u0005\u0019\u0000\u0000\u022e\u022f\u0005w\u0000\u0000\u022f\u0254\u0005"+
		"\u001a\u0000\u0000\u0230\u0231\u0005Z\u0000\u0000\u0231\u0232\u0005\u0019"+
		"\u0000\u0000\u0232\u0233\u0005w\u0000\u0000\u0233\u0254\u0005\u001a\u0000"+
		"\u0000\u0234\u0235\u0005[\u0000\u0000\u0235\u0236\u0005\u0019\u0000\u0000"+
		"\u0236\u0237\u0005\"\u0000\u0000\u0237\u023c\u0005w\u0000\u0000\u0238"+
		"\u0239\u0005#\u0000\u0000\u0239\u023b\u0005w\u0000\u0000\u023a\u0238\u0001"+
		"\u0000\u0000\u0000\u023b\u023e\u0001\u0000\u0000\u0000\u023c\u023a\u0001"+
		"\u0000\u0000\u0000\u023c\u023d\u0001\u0000\u0000\u0000\u023d\u023f\u0001"+
		"\u0000\u0000\u0000\u023e\u023c\u0001\u0000\u0000\u0000\u023f\u0240\u0005"+
		"$\u0000\u0000\u0240\u0254\u0005\u001a\u0000\u0000\u0241\u0242\u0005\\"+
		"\u0000\u0000\u0242\u0243\u0005\u0019\u0000\u0000\u0243\u0244\u0005\"\u0000"+
		"\u0000\u0244\u0249\u0005w\u0000\u0000\u0245\u0246\u0005#\u0000\u0000\u0246"+
		"\u0248\u0005w\u0000\u0000\u0247\u0245\u0001\u0000\u0000\u0000\u0248\u024b"+
		"\u0001\u0000\u0000\u0000\u0249\u0247\u0001\u0000\u0000\u0000\u0249\u024a"+
		"\u0001\u0000\u0000\u0000\u024a\u024c\u0001\u0000\u0000\u0000\u024b\u0249"+
		"\u0001\u0000\u0000\u0000\u024c\u024d\u0005$\u0000\u0000\u024d\u0254\u0005"+
		"\u001a\u0000\u0000\u024e\u024f\u0005]\u0000\u0000\u024f\u0250\u0005\u0019"+
		"\u0000\u0000\u0250\u0251\u0005v\u0000\u0000\u0251\u0254\u0005\u001a\u0000"+
		"\u0000\u0252\u0254\u0003X,\u0000\u0253\u0220\u0001\u0000\u0000\u0000\u0253"+
		"\u0224\u0001\u0000\u0000\u0000\u0253\u0228\u0001\u0000\u0000\u0000\u0253"+
		"\u022c\u0001\u0000\u0000\u0000\u0253\u0230\u0001\u0000\u0000\u0000\u0253"+
		"\u0234\u0001\u0000\u0000\u0000\u0253\u0241\u0001\u0000\u0000\u0000\u0253"+
		"\u024e\u0001\u0000\u0000\u0000\u0253\u0252\u0001\u0000\u0000\u0000\u0254"+
		"A\u0001\u0000\u0000\u0000\u0255\u0256\u0005\u0018\u0000\u0000\u0256\u0257"+
		"\u0005\u0019\u0000\u0000\u0257\u0258\u0005w\u0000\u0000\u0258\u0267\u0005"+
		"\u001a\u0000\u0000\u0259\u025a\u0005E\u0000\u0000\u025a\u025b\u0005\u0019"+
		"\u0000\u0000\u025b\u025c\u0005{\u0000\u0000\u025c\u0267\u0005\u001a\u0000"+
		"\u0000\u025d\u025e\u0005^\u0000\u0000\u025e\u025f\u0005\u0019\u0000\u0000"+
		"\u025f\u0260\u0005w\u0000\u0000\u0260\u0267\u0005\u001a\u0000\u0000\u0261"+
		"\u0262\u0005_\u0000\u0000\u0262\u0263\u0005\u0019\u0000\u0000\u0263\u0264"+
		"\u0005w\u0000\u0000\u0264\u0267\u0005\u001a\u0000\u0000\u0265\u0267\u0003"+
		"X,\u0000\u0266\u0255\u0001\u0000\u0000\u0000\u0266\u0259\u0001\u0000\u0000"+
		"\u0000\u0266\u025d\u0001\u0000\u0000\u0000\u0266\u0261\u0001\u0000\u0000"+
		"\u0000\u0266\u0265\u0001\u0000\u0000\u0000\u0267C\u0001\u0000\u0000\u0000"+
		"\u0268\u0269\u0005\u0018\u0000\u0000\u0269\u026a\u0005\u0019\u0000\u0000"+
		"\u026a\u026b\u0005w\u0000\u0000\u026b\u027e\u0005\u001a\u0000\u0000\u026c"+
		"\u026d\u0005V\u0000\u0000\u026d\u026e\u0005\u0019\u0000\u0000\u026e\u026f"+
		"\u0005\u0088\u0000\u0000\u026f\u027e\u0005\u001a\u0000\u0000\u0270\u0271"+
		"\u0005`\u0000\u0000\u0271\u0272\u0005\u0019\u0000\u0000\u0272\u0273\u0005"+
		"\u0080\u0000\u0000\u0273\u027e\u0005\u001a\u0000\u0000\u0274\u0275\u0005"+
		"a\u0000\u0000\u0275\u0276\u0005\u0019\u0000\u0000\u0276\u0277\u0005w\u0000"+
		"\u0000\u0277\u027e\u0005\u001a\u0000\u0000\u0278\u0279\u0005b\u0000\u0000"+
		"\u0279\u027a\u0005\u0019\u0000\u0000\u027a\u027b\u0005w\u0000\u0000\u027b"+
		"\u027e\u0005\u001a\u0000\u0000\u027c\u027e\u0003X,\u0000\u027d\u0268\u0001"+
		"\u0000\u0000\u0000\u027d\u026c\u0001\u0000\u0000\u0000\u027d\u0270\u0001"+
		"\u0000\u0000\u0000\u027d\u0274\u0001\u0000\u0000\u0000\u027d\u0278\u0001"+
		"\u0000\u0000\u0000\u027d\u027c\u0001\u0000\u0000\u0000\u027eE\u0001\u0000"+
		"\u0000\u0000\u027f\u0280\u0005\u0018\u0000\u0000\u0280\u0281\u0005\u0019"+
		"\u0000\u0000\u0281\u0282\u0005w\u0000\u0000\u0282\u0291\u0005\u001a\u0000"+
		"\u0000\u0283\u0284\u0005c\u0000\u0000\u0284\u0285\u0005\u0019\u0000\u0000"+
		"\u0285\u0286\u0005w\u0000\u0000\u0286\u0291\u0005\u001a\u0000\u0000\u0287"+
		"\u0288\u0005d\u0000\u0000\u0288\u0289\u0005\u0019\u0000\u0000\u0289\u028a"+
		"\u0005\u008f\u0000\u0000\u028a\u0291\u0005\u001a\u0000\u0000\u028b\u028c"+
		"\u0005e\u0000\u0000\u028c\u028d\u0005\u0019\u0000\u0000\u028d\u028e\u0005"+
		"v\u0000\u0000\u028e\u0291\u0005\u001a\u0000\u0000\u028f\u0291\u0003X,"+
		"\u0000\u0290\u027f\u0001\u0000\u0000\u0000\u0290\u0283\u0001\u0000\u0000"+
		"\u0000\u0290\u0287\u0001\u0000\u0000\u0000\u0290\u028b\u0001\u0000\u0000"+
		"\u0000\u0290\u028f\u0001\u0000\u0000\u0000\u0291G\u0001\u0000\u0000\u0000"+
		"\u0292\u0293\u0005\u0018\u0000\u0000\u0293\u0294\u0005\u0019\u0000\u0000"+
		"\u0294\u0295\u0005w\u0000\u0000\u0295\u02a0\u0005\u001a\u0000\u0000\u0296"+
		"\u0297\u0005f\u0000\u0000\u0297\u0298\u0005\u0019\u0000\u0000\u0298\u0299"+
		"\u0005\u008b\u0000\u0000\u0299\u02a0\u0005\u001a\u0000\u0000\u029a\u029b"+
		"\u0005g\u0000\u0000\u029b\u029c\u0005\u0019\u0000\u0000\u029c\u029d\u0005"+
		"w\u0000\u0000\u029d\u02a0\u0005\u001a\u0000\u0000\u029e\u02a0\u0003X,"+
		"\u0000\u029f\u0292\u0001\u0000\u0000\u0000\u029f\u0296\u0001\u0000\u0000"+
		"\u0000\u029f\u029a\u0001\u0000\u0000\u0000\u029f\u029e\u0001\u0000\u0000"+
		"\u0000\u02a0I\u0001\u0000\u0000\u0000\u02a1\u02a2\u0005\u0018\u0000\u0000"+
		"\u02a2\u02a3\u0005\u0019\u0000\u0000\u02a3\u02a4\u0005w\u0000\u0000\u02a4"+
		"\u02b8\u0005\u001a\u0000\u0000\u02a5\u02a6\u0005h\u0000\u0000\u02a6\u02a7"+
		"\u0005\u0019\u0000\u0000\u02a7\u02a8\u0005\"\u0000\u0000\u02a8\u02ad\u0005"+
		"u\u0000\u0000\u02a9\u02aa\u0005#\u0000\u0000\u02aa\u02ac\u0005u\u0000"+
		"\u0000\u02ab\u02a9\u0001\u0000\u0000\u0000\u02ac\u02af\u0001\u0000\u0000"+
		"\u0000\u02ad\u02ab\u0001\u0000\u0000\u0000\u02ad\u02ae\u0001\u0000\u0000"+
		"\u0000\u02ae\u02b0\u0001\u0000\u0000\u0000\u02af\u02ad\u0001\u0000\u0000"+
		"\u0000\u02b0\u02b1\u0005$\u0000\u0000\u02b1\u02b8\u0005\u001a\u0000\u0000"+
		"\u02b2\u02b3\u0005i\u0000\u0000\u02b3\u02b4\u0005\u0019\u0000\u0000\u02b4"+
		"\u02b5\u0005w\u0000\u0000\u02b5\u02b8\u0005\u001a\u0000\u0000\u02b6\u02b8"+
		"\u0003X,\u0000\u02b7\u02a1\u0001\u0000\u0000\u0000\u02b7\u02a5\u0001\u0000"+
		"\u0000\u0000\u02b7\u02b2\u0001\u0000\u0000\u0000\u02b7\u02b6\u0001\u0000"+
		"\u0000\u0000\u02b8K\u0001\u0000\u0000\u0000\u02b9\u02ba\u0005\u0018\u0000"+
		"\u0000\u02ba\u02bb\u0005\u0019\u0000\u0000\u02bb\u02bc\u0005w\u0000\u0000"+
		"\u02bc\u02c7\u0005\u001a\u0000\u0000\u02bd\u02be\u0005j\u0000\u0000\u02be"+
		"\u02bf\u0005\u0019\u0000\u0000\u02bf\u02c0\u0005\u0083\u0000\u0000\u02c0"+
		"\u02c7\u0005\u001a\u0000\u0000\u02c1\u02c2\u0005k\u0000\u0000\u02c2\u02c3"+
		"\u0005\u0019\u0000\u0000\u02c3\u02c4\u0005w\u0000\u0000\u02c4\u02c7\u0005"+
		"\u001a\u0000\u0000\u02c5\u02c7\u0003X,\u0000\u02c6\u02b9\u0001\u0000\u0000"+
		"\u0000\u02c6\u02bd\u0001\u0000\u0000\u0000\u02c6\u02c1\u0001\u0000\u0000"+
		"\u0000\u02c6\u02c5\u0001\u0000\u0000\u0000\u02c7M\u0001\u0000\u0000\u0000"+
		"\u02c8\u02c9\u0005\u0018\u0000\u0000\u02c9\u02ca\u0005\u0019\u0000\u0000"+
		"\u02ca\u02cb\u0005w\u0000\u0000\u02cb\u02d6\u0005\u001a\u0000\u0000\u02cc"+
		"\u02cd\u0005l\u0000\u0000\u02cd\u02ce\u0005\u0019\u0000\u0000\u02ce\u02cf"+
		"\u0005\u008a\u0000\u0000\u02cf\u02d6\u0005\u001a\u0000\u0000\u02d0\u02d1"+
		"\u0005m\u0000\u0000\u02d1\u02d2\u0005\u0019\u0000\u0000\u02d2\u02d3\u0005"+
		"v\u0000\u0000\u02d3\u02d6\u0005\u001a\u0000\u0000\u02d4\u02d6\u0003X,"+
		"\u0000\u02d5\u02c8\u0001\u0000\u0000\u0000\u02d5\u02cc\u0001\u0000\u0000"+
		"\u0000\u02d5\u02d0\u0001\u0000\u0000\u0000\u02d5\u02d4\u0001\u0000\u0000"+
		"\u0000\u02d6O\u0001\u0000\u0000\u0000\u02d7\u02d8\u0005\u0018\u0000\u0000"+
		"\u02d8\u02d9\u0005\u0019\u0000\u0000\u02d9\u02da\u0005w\u0000\u0000\u02da"+
		"\u02ea\u0005\u001a\u0000\u0000\u02db\u02dc\u0005n\u0000\u0000\u02dc\u02dd"+
		"\u0005\u0019\u0000\u0000\u02dd\u02de\u0005\"\u0000\u0000\u02de\u02e3\u0005"+
		"u\u0000\u0000\u02df\u02e0\u0005#\u0000\u0000\u02e0\u02e2\u0005u\u0000"+
		"\u0000\u02e1\u02df\u0001\u0000\u0000\u0000\u02e2\u02e5\u0001\u0000\u0000"+
		"\u0000\u02e3\u02e1\u0001\u0000\u0000\u0000\u02e3\u02e4\u0001\u0000\u0000"+
		"\u0000\u02e4\u02e6\u0001\u0000\u0000\u0000\u02e5\u02e3\u0001\u0000\u0000"+
		"\u0000\u02e6\u02e7\u0005$\u0000\u0000\u02e7\u02ea\u0005\u001a\u0000\u0000"+
		"\u02e8\u02ea\u0003X,\u0000\u02e9\u02d7\u0001\u0000\u0000\u0000\u02e9\u02db"+
		"\u0001\u0000\u0000\u0000\u02e9\u02e8\u0001\u0000\u0000\u0000\u02eaQ\u0001"+
		"\u0000\u0000\u0000\u02eb\u02ec\u0005\u0018\u0000\u0000\u02ec\u02ed\u0005"+
		"\u0019\u0000\u0000\u02ed\u02ee\u0005w\u0000\u0000\u02ee\u02f5\u0005\u001a"+
		"\u0000\u0000\u02ef\u02f0\u0005o\u0000\u0000\u02f0\u02f1\u0005\u0019\u0000"+
		"\u0000\u02f1\u02f2\u0005v\u0000\u0000\u02f2\u02f5\u0005\u001a\u0000\u0000"+
		"\u02f3\u02f5\u0003X,\u0000\u02f4\u02eb\u0001\u0000\u0000\u0000\u02f4\u02ef"+
		"\u0001\u0000\u0000\u0000\u02f4\u02f3\u0001\u0000\u0000\u0000\u02f5S\u0001"+
		"\u0000\u0000\u0000\u02f6\u02f7\u0005\u0018\u0000\u0000\u02f7\u02f8\u0005"+
		"\u0019\u0000\u0000\u02f8\u02f9\u0005w\u0000\u0000\u02f9\u0300\u0005\u001a"+
		"\u0000\u0000\u02fa\u02fb\u0005p\u0000\u0000\u02fb\u02fc\u0005\u0019\u0000"+
		"\u0000\u02fc\u02fd\u0005w\u0000\u0000\u02fd\u0300\u0005\u001a\u0000\u0000"+
		"\u02fe\u0300\u0003X,\u0000\u02ff\u02f6\u0001\u0000\u0000\u0000\u02ff\u02fa"+
		"\u0001\u0000\u0000\u0000\u02ff\u02fe\u0001\u0000\u0000\u0000\u0300U\u0001"+
		"\u0000\u0000\u0000\u0301\u0302\u0005\u0018\u0000\u0000\u0302\u0303\u0005"+
		"\u0019\u0000\u0000\u0303\u0304\u0005w\u0000\u0000\u0304\u030f\u0005\u001a"+
		"\u0000\u0000\u0305\u0306\u0005V\u0000\u0000\u0306\u0307\u0005\u0019\u0000"+
		"\u0000\u0307\u0308\u0005\u007f\u0000\u0000\u0308\u030f\u0005\u001a\u0000"+
		"\u0000\u0309\u030a\u0005q\u0000\u0000\u030a\u030b\u0005\u0019\u0000\u0000"+
		"\u030b\u030c\u0007\n\u0000\u0000\u030c\u030f\u0005\u001a\u0000\u0000\u030d"+
		"\u030f\u0003X,\u0000\u030e\u0301\u0001\u0000\u0000\u0000\u030e\u0305\u0001"+
		"\u0000\u0000\u0000\u030e\u0309\u0001\u0000\u0000\u0000\u030e\u030d\u0001"+
		"\u0000\u0000\u0000\u030fW\u0001\u0000\u0000\u0000\u0310\u0311\u0005u\u0000"+
		"\u0000\u0311\u0312\u0005\u0019\u0000\u0000\u0312\u0313\u0003h4\u0000\u0313"+
		"\u0314\u0005\u001a\u0000\u0000\u0314Y\u0001\u0000\u0000\u0000\u0315\u0316"+
		"\u0003^/\u0000\u0316\u0317\u0005\u001a\u0000\u0000\u0317\u0320\u0001\u0000"+
		"\u0000\u0000\u0318\u0320\u0003`0\u0000\u0319\u031a\u0003b1\u0000\u031a"+
		"\u031b\u0005\u001a\u0000\u0000\u031b\u0320\u0001\u0000\u0000\u0000\u031c"+
		"\u031d\u0003d2\u0000\u031d\u031e\u0005\u001a\u0000\u0000\u031e\u0320\u0001"+
		"\u0000\u0000\u0000\u031f\u0315\u0001\u0000\u0000\u0000\u031f\u0318\u0001"+
		"\u0000\u0000\u0000\u031f\u0319\u0001\u0000\u0000\u0000\u031f\u031c\u0001"+
		"\u0000\u0000\u0000\u0320[\u0001\u0000\u0000\u0000\u0321\u0322\u0005u\u0000"+
		"\u0000\u0322\u0323\u0005\u0019\u0000\u0000\u0323\u0326\u0003f3\u0000\u0324"+
		"\u0325\u0005\u0019\u0000\u0000\u0325\u0327\u0003h4\u0000\u0326\u0324\u0001"+
		"\u0000\u0000\u0000\u0326\u0327\u0001\u0000\u0000\u0000\u0327\u0328\u0001"+
		"\u0000\u0000\u0000\u0328\u0329\u0005\u001a\u0000\u0000\u0329]\u0001\u0000"+
		"\u0000\u0000\u032a\u032b\u0005u\u0000\u0000\u032b\u032c\u0005\u0019\u0000"+
		"\u0000\u032c\u032d\u0003h4\u0000\u032d_\u0001\u0000\u0000\u0000\u032e"+
		"\u032f\u0005r\u0000\u0000\u032f\u0330\u0005u\u0000\u0000\u0330\u0334\u0005"+
		"\u0005\u0000\u0000\u0331\u0333\u0003Z-\u0000\u0332\u0331\u0001\u0000\u0000"+
		"\u0000\u0333\u0336\u0001\u0000\u0000\u0000\u0334\u0332\u0001\u0000\u0000"+
		"\u0000\u0334\u0335\u0001\u0000\u0000\u0000\u0335\u0337\u0001\u0000\u0000"+
		"\u0000\u0336\u0334\u0001\u0000\u0000\u0000\u0337\u0338\u0005\u0006\u0000"+
		"\u0000\u0338a\u0001\u0000\u0000\u0000\u0339\u033a\u0005s\u0000\u0000\u033a"+
		"\u033b\u0005\u0019\u0000\u0000\u033b\u033c\u0005w\u0000\u0000\u033cc\u0001"+
		"\u0000\u0000\u0000\u033d\u033e\u0005t\u0000\u0000\u033e\u033f\u0005w\u0000"+
		"\u0000\u033fe\u0001\u0000\u0000\u0000\u0340\u0341\u0005u\u0000\u0000\u0341"+
		"g\u0001\u0000\u0000\u0000\u0342\u035c\u0005v\u0000\u0000\u0343\u035c\u0005"+
		"w\u0000\u0000\u0344\u035c\u0005x\u0000\u0000\u0345\u0346\u0005\"\u0000"+
		"\u0000\u0346\u034b\u0003h4\u0000\u0347\u0348\u0005#\u0000\u0000\u0348"+
		"\u034a\u0003h4\u0000\u0349\u0347\u0001\u0000\u0000\u0000\u034a\u034d\u0001"+
		"\u0000\u0000\u0000\u034b\u0349\u0001\u0000\u0000\u0000\u034b\u034c\u0001"+
		"\u0000\u0000\u0000\u034c\u034e\u0001\u0000\u0000\u0000\u034d\u034b\u0001"+
		"\u0000\u0000\u0000\u034e\u034f\u0005$\u0000\u0000\u034f\u035c\u0001\u0000"+
		"\u0000\u0000\u0350\u0351\u0005\u0005\u0000\u0000\u0351\u0356\u0003^/\u0000"+
		"\u0352\u0353\u0005#\u0000\u0000\u0353\u0355\u0003^/\u0000\u0354\u0352"+
		"\u0001\u0000\u0000\u0000\u0355\u0358\u0001\u0000\u0000\u0000\u0356\u0354"+
		"\u0001\u0000\u0000\u0000\u0356\u0357\u0001\u0000\u0000\u0000\u0357\u0359"+
		"\u0001\u0000\u0000\u0000\u0358\u0356\u0001\u0000\u0000\u0000\u0359\u035a"+
		"\u0005\u0006\u0000\u0000\u035a\u035c\u0001\u0000\u0000\u0000\u035b\u0342"+
		"\u0001\u0000\u0000\u0000\u035b\u0343\u0001\u0000\u0000\u0000\u035b\u0344"+
		"\u0001\u0000\u0000\u0000\u035b\u0345\u0001\u0000\u0000\u0000\u035b\u0350"+
		"\u0001\u0000\u0000\u0000\u035ci\u0001\u0000\u0000\u00004m\u0083\u008b"+
		"\u0096\u00a1\u00ac\u00b7\u00c2\u00cd\u00d8\u00e3\u00ee\u00f9\u0104\u010f"+
		"\u011a\u0125\u0130\u013b\u0146\u0170\u017d\u0183\u01a0\u01ad\u01b8\u01d2"+
		"\u01d8\u01fb\u021e\u023c\u0249\u0253\u0266\u027d\u0290\u029f\u02ad\u02b7"+
		"\u02c6\u02d5\u02e3\u02e9\u02f4\u02ff\u030e\u031f\u0326\u0334\u034b\u0356"+
		"\u035b";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}