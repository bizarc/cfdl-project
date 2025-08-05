grammar cfdl;

@header {
  package dev.cfdl.parser;
}

options {
  language = Java;
}

//=====================================================================
// Parser Rules
//=====================================================================

specification
    : definition+ EOF
    ;

// Top-level definitions: specific constructs based on schemas
//=====================================================================

definition
    : dealDefinition
    | assetDefinition
    | componentDefinition
    | streamDefinition
    | assumptionDefinition
    | logicBlockDefinition
    | ruleBlockDefinition
    | scheduleDefinition
    | eventTriggerDefinition
    | templateDefinition
    | contractDefinition
    | partyDefinition
    | fundDefinition
    | portfolioDefinition
    | capitalStackDefinition
    | waterFallDefinition
    | metricDefinition
    | genericDefinition
    ;

// Generic fallback for other constructs
genericDefinition
    : ('entity'|'behavior'|'temporal'|'result') IDENTIFIER '{' statement* '}'
    ;

// Specific entity definitions
dealDefinition
    : 'deal' IDENTIFIER '{' dealProperty* '}'
    ;

assetDefinition
    : 'asset' IDENTIFIER '{' assetProperty* '}'
    ;

componentDefinition
    : 'component' IDENTIFIER '{' componentProperty* '}'
    ;

streamDefinition
    : 'stream' IDENTIFIER '{' streamProperty* '}'
    ;

assumptionDefinition
    : 'assumption' IDENTIFIER '{' assumptionProperty* '}'
    ;

logicBlockDefinition
    : 'logic_block' IDENTIFIER '{' logicBlockProperty* '}'
    ;

ruleBlockDefinition
    : 'rule_block' IDENTIFIER '{' ruleBlockProperty* '}'
    ;

scheduleDefinition
    : 'schedule' IDENTIFIER '{' scheduleProperty* '}'
    ;

eventTriggerDefinition
    : 'event_trigger' IDENTIFIER '{' eventTriggerProperty* '}'
    ;

templateDefinition
    : 'template' IDENTIFIER '{' templateProperty* '}'
    ;

contractDefinition
    : 'contract' IDENTIFIER '{' contractProperty* '}'
    ;

partyDefinition
    : 'party' IDENTIFIER '{' partyProperty* '}'
    ;

fundDefinition
    : 'fund' IDENTIFIER '{' fundProperty* '}'
    ;

portfolioDefinition
    : 'portfolio' IDENTIFIER '{' portfolioProperty* '}'
    ;

capitalStackDefinition
    : 'capital_stack' IDENTIFIER '{' capitalStackProperty* '}'
    ;

waterFallDefinition
    : 'waterfall' IDENTIFIER '{' waterFallProperty* '}'
    ;

metricDefinition
    : 'metric' IDENTIFIER '{' metricProperty* '}'
    ;

//=====================================================================
// Specific Property Rules for Each Construct Type
//=====================================================================

// Deal properties
dealProperty
    : 'name' ':' STRING ';'
    | 'dealType' ':' DEAL_TYPE ';' 
    | 'entryDate' ':' STRING ';'  // Date string
    | 'exitDate' ':' STRING ';'
    | 'analysisStart' ':' STRING ';'
    | 'currency' ':' STRING ';'
    | 'holdingPeriodYears' ':' NUMBER ';'
    | 'assets' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';'
    | 'streams' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';'
    | genericProperty
    ;

// Asset properties  
assetProperty
    : 'name' ':' STRING ';'
    | 'dealId' ':' IDENTIFIER ';'
    | 'category' ':' ASSET_CATEGORY ';'
    | 'description' ':' STRING ';'
    | 'components' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';'
    | 'streams' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';'
    | 'state' ':' ASSET_STATE ';'
    | genericProperty
    ;

// Component properties
componentProperty
    : 'name' ':' STRING ';'
    | 'assetId' ':' IDENTIFIER ';'
    | 'scope' ':' COMPONENT_SCOPE ';'
    | 'streams' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';'
    | genericProperty
    ;

// Stream properties
streamProperty
    : 'name' ':' STRING ';'
    | 'scope' ':' COMPONENT_SCOPE ';'
    | 'category' ':' STREAM_CATEGORY ';'
    | 'subType' ':' STREAM_SUB_TYPE ';' 
    | 'schedule' ':' IDENTIFIER ';'
    | 'amount' ':' (NUMBER | IDENTIFIER) ';'
    | 'amountType' ':' AMOUNT_TYPE ';'
    | genericProperty
    ;

// Assumption properties
assumptionProperty
    : 'name' ':' STRING ';'
    | 'category' ':' ASSUMPTION_CATEGORY ';'
    | 'scope' ':' COMPONENT_SCOPE ';'
    | 'type' ':' ASSUMPTION_TYPE ';'
    | 'value' ':' (NUMBER | STRING | IDENTIFIER) ';'
    | 'distributionType' ':' DISTRIBUTION_TYPE ';'
    | genericProperty
    ;

// Logic block properties
logicBlockProperty
    : 'name' ':' STRING ';'
    | 'scope' ':' COMPONENT_SCOPE ';'
    | 'type' ':' LOGIC_BLOCK_TYPE ';'
    | 'code' ':' STRING ';'
    | 'language' ':' STRING ';'
    | 'inputs' ':' '[' STRING (',' STRING)* ']' ';'
    | 'outputs' ':' '[' STRING (',' STRING)* ']' ';'
    | 'executionOrder' ':' NUMBER ';'
    | genericProperty
    ;

// Rule block properties
ruleBlockProperty
    : 'name' ':' STRING ';'
    | 'scope' ':' COMPONENT_SCOPE ';'
    | 'condition' ':' STRING ';'
    | 'action' ':' STRING ';'
    | genericProperty
    ;

// Schedule properties
scheduleProperty
    : 'name' ':' STRING ';'
    | 'type' ':' SCHEDULE_TYPE ';'
    | 'frequency' ':' FREQUENCY ';'
    | 'startDate' ':' STRING ';'
    | 'endDate' ':' STRING ';'
    | genericProperty
    ;

// Event trigger properties
eventTriggerProperty
    : 'name' ':' STRING ';'
    | 'eventType' ':' STRING ';'
    | 'operator' ':' TRIGGER_OPERATOR ';'
    | 'threshold' ':' NUMBER ';'
    | genericProperty
    ;

// Template properties
templateProperty
    : 'name' ':' STRING ';'
    | 'templateType' ':' TEMPLATE_TYPE ';'
    | 'body' ':' STRING ';'
    | genericProperty
    ;

// Contract properties
contractProperty
    : 'name' ':' STRING ';'
    | 'parties' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';'
    | 'terms' ':' STRING ';'
    | genericProperty
    ;

// Party properties
partyProperty
    : 'name' ':' STRING ';'
    | 'role' ':' PARTY_ROLE ';'
    | 'contact' ':' STRING ';'
    | genericProperty
    ;

// Fund properties
fundProperty
    : 'name' ':' STRING ';'
    | 'participantRole' ':' FUND_PARTICIPANT_ROLE ';'
    | 'totalCommitment' ':' NUMBER ';'
    | genericProperty
    ;

// Portfolio properties
portfolioProperty
    : 'name' ':' STRING ';'
    | 'deals' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';'
    | genericProperty
    ;

// Capital stack properties
capitalStackProperty
    : 'name' ':' STRING ';'
    | 'totalAmount' ':' NUMBER ';'
    | genericProperty
    ;

// Waterfall properties
waterFallProperty
    : 'name' ':' STRING ';'
    | 'distributionRules' ':' STRING ';'
    | genericProperty
    ;

// Metric properties
metricProperty
    : 'name' ':' STRING ';'
    | 'type' ':' METRIC_TYPE ';'
    | 'unit' ':' (PAYBACK_UNIT | STRING) ';'
    | genericProperty
    ;

// Generic property fallback
genericProperty
    : IDENTIFIER ':' value ';'
    ;

//=====================================================================
// Core Statement & Property Rules
//=====================================================================

statement
    : propertyAssign ';'
    | logicBlockDef
    | referenceDecl ';'
    | importDecl ';'
    ;

propertyDef
    : IDENTIFIER ':' typeRef (':' value)? ';'
    ;

propertyAssign
    : IDENTIFIER ':' value
    ;

logicBlockDef
    : 'logic' IDENTIFIER '{' statement* '}'
    ;

referenceDecl
    : '$ref' ':' STRING
    ;

importDecl
    : 'import' STRING
    ;

// Type references (embedded enums or named types)
//=====================================================================

typeRef
    : IDENTIFIER
    ;

//=====================================================================
// Value Constructs
//=====================================================================

value
    : NUMBER
    | STRING
    | BOOLEAN
    | '[' value (',' value)* ']'            // arrays
    | '{' propertyAssign (',' propertyAssign)* '}'  // inline objects
    ;

//=====================================================================
// Lexer Rules & Enums
//=====================================================================

IDENTIFIER
    : [a-zA-Z_][a-zA-Z0-9_]*
    ;

NUMBER
    : '-'? [0-9]+ ('.' [0-9]+)?
    ;

STRING
    : '"' (~["\\] | '\\' .)* '"'
    ;

BOOLEAN
    : 'true'
    | 'false'
    ;

// Embedded token enums

DEAL_TYPE
    : 'commercial_real_estate'
    | 'residential_real_estate'
    | 'financial_instrument'
    | 'syndicated_loan'
    | 'private_credit'
    | 'litigation_finance'
    | 'royalty_stream'
    | 'infrastructure_project'
    | 'renewable_energy_project'
    | 'equipment_leasing'
    | 'art_and_collectibles'
    | 'business_acquisition'
    | 'deal_other'
    ;

ASSET_CATEGORY
    : 'real_estate'
    | 'financial_asset'
    | 'physical_asset'
    | 'legal_right'
    | 'operating_entity'
    | 'contract_bundle'
    | 'mixed'
    | 'asset_other'
    ;

COMPONENT_SCOPE
    : 'component'
    | 'asset'
    | 'deal'
    | 'portfolio'
    | 'fund'
    ;

ASSUMPTION_CATEGORY
    : 'revenue'
    | 'expense'
    | 'capital'
    | 'leasing'
    | 'timing'
    | 'financing'
    | 'assumption_other'
    ;

DISTRIBUTION_TYPE
    : 'normal'
    | 'uniform'
    | 'triangular'
    | 'lognormal'
    | 'beta'
    | 'dist_custom'
    ;

LOGIC_BLOCK_TYPE
    : 'calculation'
    | 'aggregation'
    | 'validation'
    | 'trigger'
    | 'generator'
    | 'logic_custom'
    ;

METRIC_TYPE
    : 'npv'
    | 'irr'
    | 'moic'
    | 'dscr'
    | 'payback'
    | 'enpv'
    | 'eirr'
    ;

FREQUENCY
    : 'daily'
    | 'weekly'
    | 'monthly'
    | 'quarterly'
    | 'annual'
    ;

BUSINESS_DAY_CONVENTION
    : 'following'
    | 'modified_following'
    | 'preceding'
    ;

DAY_COUNT
    : 'actual/360'
    | 'actual/365'
    | '30/360'
    | 'actual/actual'
    ;

PARTY_ROLE
    : 'sponsor'
    | 'equity'
    | 'lender'
    | 'operator'
    | 'guarantor'
    | 'party_other'
    ;

// Stream-related enums
STREAM_CATEGORY
    : 'Revenue'
    | 'Expense'
    | 'OtherIncome'
    ;

STREAM_SUB_TYPE
    : 'Operating'
    | 'Financing'
    | 'Tax'
    | 'CapEx'
    | 'Fee'
    | 'Other'
    ;

AMOUNT_TYPE
    : 'amount_fixed'
    | 'amount_expression'
    | 'amount_distribution'
    | 'randomWalk'
    ;

// Assumption-related enums  
ASSUMPTION_TYPE
    : 'assumption_fixed'
    | 'assumption_distribution'
    | 'table'
    | 'assumption_expression'
    ;

// Schedule-related enums
SCHEDULE_TYPE
    : 'oneTime'
    | 'recurring'
    | 'dateBounded'
    ;

// Entity-specific enums
ASSET_STATE
    : 'pre_operational'
    | 'operational'
    | 'non_operational'
    | 'disposed'
    ;

FUND_PARTICIPANT_ROLE
    : 'general_partner'
    | 'limited_partner'
    | 'advisor'
    | 'fund_other'
    ;

TEMPLATE_TYPE
    : 'template_deal'
    | 'template_asset'
    | 'template_component'
    | 'stream'
    | 'logic-block'
    | 'rule-block'
    | 'assumption'
    | 'view'
    | 'scenario'
    | 'contract'
    | 'waterfall'
    ;

PARAMETER_DATA_TYPE
    : 'string'
    | 'number'
    | 'date'
    | 'boolean'
    | 'enum'
    ;

// Temporal-related enums
RECURRENCE_FREQUENCY
    : 'DAILY'
    | 'WEEKLY'
    | 'MONTHLY'
    | 'YEARLY'
    ;

WEEKDAY
    : 'MO'
    | 'TU'
    | 'WE'
    | 'TH'
    | 'FR'
    | 'SA'
    | 'SU'
    ;

TRIGGER_OPERATOR
    : 'eq'
    | 'ne'
    | 'lt'
    | 'lte'
    | 'gt'
    | 'gte'
    ;

// Market data enums
DATA_SOURCE_TYPE
    : 'api'
    | 'database'
    | 'file'
    | 'service'
    ;

// Metric result enums
PAYBACK_UNIT
    : 'period'
    | 'year'
    ;

//=====================================================================
// Whitespace & Comments
//=====================================================================

WS
    : [ \t\r\n]+ -> skip
    ;

LINE_COMMENT
    : '//' ~[\r\n]* -> skip
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> skip
    ;
