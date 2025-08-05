grammar CFDL;

// Parser Rules
cfdlModel
    : specification? importSection* definition* EOF
    ;

specification
    : 'version' ':' versionLiteral NEWLINE?
    ;

importSection
    : 'include' ':' includeList NEWLINE
    ;

includeList
    : YAML_PATH (',' YAML_PATH)*
    ;

definition
    : dealDef
    | assetDef
    | componentDef
    | portfolioDef
    | fundDef
    | contractDef
    | partyDef
    | capitalStackDef
    | templateDef
    | streamDef
    | assumptionDef
    | logicBlockDef
    | ruleBlockDef
    | viewDef
    ;

// Deal
dealDef
    : 'deal' IDENTIFIER '{' dealBody '}'
    ;

dealBody
    : (dealProperty NEWLINE?)*
    ;

dealProperty
    : 'entryDate' ':' dateLiteral
    | 'analysisStart' ':' dateLiteral
    | 'holdingPeriod' ':' durationLiteral
    | 'calendar' ':' calendarBlock
    ;

// Asset
assetDef
    : 'asset' IDENTIFIER '{' (assetProperty NEWLINE?)* '}'
    ;

assetProperty
    : 'id' ':' STRING
    | 'name' ':' STRING
    | 'parentDeal' ':' IDENTIFIER
    ;

// Component
componentDef
    : 'component' IDENTIFIER '{' (componentProperty NEWLINE?)* '}'
    ;

componentProperty
    : 'type' ':' IDENTIFIER
    | 'parentAsset' ':' IDENTIFIER
    | 'streams' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']'
    ;

// Portfolio
portfolioDef
    : 'portfolio' IDENTIFIER '{' (portfolioProperty NEWLINE?)* '}'
    ;

portfolioProperty
    : 'deals' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']'
    ;

// Fund
fundDef
    : 'fund' IDENTIFIER '{' (fundProperty NEWLINE?)* '}'
    ;

fundProperty
    : 'portfolios' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']'
    ;

// Contract
contractDef
    : 'contract' IDENTIFIER '{' (contractProperty NEWLINE?)* '}'
    ;

contractProperty
    : 'type' ':' STRING
    | 'parties' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']'
    | 'terms' ':' '{' (~[\}])* '}'
    ;

// Party
partyDef
    : 'party' IDENTIFIER '{' (partyProperty NEWLINE?)* '}'
    ;

partyProperty
    : 'name' ':' STRING
    | 'role' ':' STRING
    ;

// Capital Stack
capitalStackDef
    : 'capital_stack' IDENTIFIER '{' (capitalStackProperty NEWLINE?)* '}'
    ;

capitalStackProperty
    : 'layers' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']'
    ;

// Template
templateDef
    : 'template' IDENTIFIER '{' (templateProperty NEWLINE?)* '}'
    ;

templateProperty
    : 'include' ':' YAML_PATH
    ;

// Stream
streamDef
    : 'stream' IDENTIFIER '{' streamBody '}'
    ;

streamBody
    : (streamProperty NEWLINE?)*
    ;

streamProperty
    : 'level' ':' levelEnum
    | 'category' ':' categoryEnum
    | 'subType' ':' subTypeEnum
    | 'schedule' ':' scheduleLiteral
    | 'amount' ':' amountExpr
    | 'growth' ':' growthExpr
    | 'tags' ':' tagList
    ;

// Assumption
assumptionDef
    : 'assumption' IDENTIFIER '{' (assumptionProperty NEWLINE?)* '}'
    ;

assumptionProperty
    : 'type' ':' dataType
    | 'default' ':' value
    | 'range' ':' '[' value ',' value ']'
    ;

// LogicBlock
logicBlockDef
    : 'logic_block' IDENTIFIER '{' (logicBlockProperty NEWLINE?)* '}'
    ;

logicBlockProperty
    : 'inputs' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']'
    | 'outputs' ':' '[' IDENTIFIER (',' IDENTIFIER)* ']'
    | 'code' ':' CODE_BLOCK
    ;

// RuleBlock
ruleBlockDef
    : 'rule_block' IDENTIFIER '{' (ruleBlockProperty NEWLINE?)* '}'
    ;

ruleBlockProperty
    : 'when' ':' conditionExpr
    | 'action' ':' IDENTIFIER ':=' expression
    ;

// View
viewDef
    : 'view' IDENTIFIER '{' (viewProperty NEWLINE?)* '}'
    ;

viewProperty
    : 'includeTags' ':' tagList
    | 'excludeTags' ':' tagList
    ;

// Calendar
calendarBlock
    : '{' (calendarProperty NEWLINE?)* '}'
    ;

calendarProperty
    : 'frequency' ':' freqEnum
    | 'businessDayConvention' ':' IDENTIFIER
    | 'dayCount' ':' IDENTIFIER
    | 'holidayCalendar' ':' IDENTIFIER
    ;

freqEnum
    : 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'annual'
    ;

// Literals & Enums
levelEnum
    : 'asset' | 'deal' | 'portfolio' | 'fund'
    ;

categoryEnum
    : 'Revenue' | 'Expense' | 'OtherIncome'
    ;

subTypeEnum
    : 'Operating' | 'Financing' | 'CapEx' | 'Acquisition' | 'Fees'
    ;

tagEnum
    : 'GAAP' | 'NonGAAP' | 'Tax' | 'Actual' | 'Forecast'
    ;

distributionType
    : 'logNormal' | 'normal' | 'uniform'
    ;

scheduleLiteral
    : 'oneTime' '(' dateLiteral ')'
    | 'yearly' ('(' 'count' '=' INT ')')?
    | 'monthly' ('(' 'count' '=' INT ')')?
    | 'quarterly'
    ;

// Expressions
expression
    : value
    | IDENTIFIER
    | expression operator expression
    ;

conditionExpr
    : expression compareOp expression
    ;

operator
    : '+' | '-' | '*' | '/'
    ;

compareOp
    : '>' | '<' | '>=' | '<=' | '==' | '!='
    ;

// Amount & Growth
growthExpr
    : 'fixed' '(' DECIMAL ')'
    | 'distribution' '(' distributionType ',' paramList ')'
    | 'randomWalk' '(' paramList ')'
    | 'expression' '(' STRING ')'
    ;

paramList
    : param (',' param)*
    ;

param
    : IDENTIFIER '=' value
    ;

amountExpr
    : 'fixed' '(' DECIMAL ')'
    | 'loanPayment' '(' loanParamList ')'
    ;

loanParamList
    : 'principal' '=' DECIMAL ',' 'rate' '=' DECIMAL ',' 'termMonths' '=' INT
    ;

tagList
    : '[' tagEnum (',' tagEnum)* ']'
    ;

// Dates & Durations
dateLiteral
    : 'Date' '(' INT ',' INT ',' INT ')'
    ;

durationLiteral
    : 'duration' '(' 'years' '=' INT (',' 'months' '=' INT)? ')'
    ;

versionLiteral
    : STRING
    ;

// Values
value
    : DECIMAL | INT | STRING
    ;

YAML_PATH
    : PATH_CHARS+ ('.' PATH_CHARS+)*
    ;

PATH_CHARS
    : [a-zA-Z0-9_\/\-]
    ;

// Tokens
STRING
    : '"' (~["])* '"'
    ;

INT
    : [0-9]+
    ;

DECIMAL
    : INT '.' [0-9]+
    ;

CODE_BLOCK
    : '|' ~[]*
    ;

NEWLINE
    : '\r'? '\n'
    ;

IDENTIFIER
    : [a-zA-Z_] [a-zA-Z0-9_]*
    ;

WS
    : [ \t]+ -> skip
    ;
