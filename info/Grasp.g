/**
 * Grasp grammar in ANTLR notation. This contains the BNF-like parser grammar as well
 * as tree rewriting grammar for generating an easy to traverse AST. 
 *
 * @author		Lakshitha de Silva
 * 
 * @version		0.20, 2011/02/22
 * 
 */


grammar Grasp;

options {
	language = Java;
	backtrack = true;
	output = AST;
	ASTLabelType = CommonTree;
}

tokens {
	//
	// Elements
	//
	ARCHITECTURE;
	REQUIREMENT;
	QATTRIBUTE;
	RATIONALE;
	REASON;
	TEMPLATE;
	SYSTEM;
	LAYER;
	COMPONENT;
	CONNECTOR;
	PROVIDES;
	REQUIRES;
	CHECK;
	LINK;
	PROPERTY;
	ANNOTATION;
	NAMEDVALUE;
	//
	// Element properties
	//
	NAME;
	ALIAS;
	BASE;
	BODY;
	PAYLOAD;
	OVER;
	PARMS;
	ARGS;
	BECAUSE;
	EXTENDS;
	SUPPORTS;
	INHIBITS;
	HANDLER;
	PROVIDER;
	CONSUMER;
	MAXDEG;
	//
	// Expressions
	//
	EXPR;
	CALL;
	MEMB;
	TRUE;
	FALSE;
	//
	// Operators
	//
	SUBSETOF = 'subsetof';
	ACCEPTS	 = 'accepts';
	DIS = '||';
	CON = '&&';
	IOR = '|';
	XOR = '^';
	AND = '&';
	EQL = '==';
	NEQ = '!=';
	GTN = '>';
	GTE = '>=';
	LTN = '<';
	LTE = '<=';
	AUG = '+=';
	NAG = '-=';
	ADD = '+';
	SUB = '-';
	MUL = '*';
	DIV = '/';
	MOD = '%';
	CMP = '~';
	NOT = '!';
	POS;
	NEG;
	//
	// Types
	//
	SET;
	PAIR;
	INTEGER;
	REAL;
	BOOLEAN;
	STRING;
	DECL;
}

@lexer::header {
	package grasp.lang.grammar;
}

@parser::header {
	package grasp.lang.grammar;
}


//
// Entry point
//
start
	: architecture_statement
	;

//
// <architecture> statement
//
architecture_statement
	: annotation* kw='architecture' name=IDENTIFIER '{' architecture_item* system_statement architecture_item* '}'
			-> ^(ARCHITECTURE[$kw] annotation* ^(NAME $name) ^(BODY architecture_item* system_statement))
	;
architecture_item
	: requirement_statement
	| quality_attribute_statement
	| template_statement
	| rationale_statement
	;

//
// <requirement> statement
//
requirement_statement
	: annotation* kw='requirement' name=IDENTIFIER ('=' atom=STRING_LITERAL)? ';'
			-> ^(REQUIREMENT[$kw] annotation* ^(NAME $name) ^(EXPR ^(STRING $atom))? ^(BODY))
	;

//
// <quality_attribute> statement
//
quality_attribute_statement
	: annotation* kw='quality_attribute' name=IDENTIFIER supports_opt? ('{' property_statement* '}' | ';')
			-> ^(QATTRIBUTE[$kw] annotation* ^(NAME $name) ^(BODY property_statement*) supports_opt?)
	;

//
// <property> statement
//
property_statement
	: annotation* kw='property' name=IDENTIFIER ('=' expression)? because_opt? ';'
			-> ^(PROPERTY[$kw] annotation* ^(NAME $name) expression? ^(BODY) because_opt?)
	;

//
// <rationale> statement
//
rationale_statement
	: annotation* kw='rationale' name=IDENTIFIER '(' parameters? ')' extends_opt? because_opt? '{' reason_statement* '}'
			-> ^(RATIONALE[$kw] annotation* ^(NAME $name) parameters? ^(BODY reason_statement*) extends_opt?)
	;

//
// <reason> statement
//
reason_statement
	: annotation* kw='reason' (expression | supports_opt) inhibits_opt?  ';'
			-> ^(REASON[$kw] annotation* expression? supports_opt? inhibits_opt? ^(BODY))
	;

//
// <template> statement
//
template_statement
	: annotation* kw='template' name=IDENTIFIER (':' maxinst=INTEGER_LITERAL)? '(' parameters? ')' extends_opt? because_opt? '{' statement* '}'
			-> ^(TEMPLATE[$kw] annotation* ^(NAME $name) parameters? ^(PAYLOAD ^(BODY statement*)) extends_opt? because_opt?)
	;

//
// <system> statement
//
system_statement
	: annotation* kw='system' name=IDENTIFIER because_opt? '{' statement* '}'
			-> ^(SYSTEM[$kw] annotation* ^(NAME $name) ^(BODY statement*) because_opt?)
	;

//
// <layer> statement
//
layer_statement
	: annotation* kw='layer' name=IDENTIFIER layer_over? because_opt? '{' statement* '}'
			-> ^(LAYER[$kw] annotation* ^(NAME $name) ^(BODY statement*) layer_over? because_opt?)
	;
layer_over
	: kw='over' layers+=IDENTIFIER (',' layers+=IDENTIFIER)*
			-> ^(OVER[$kw] ^(MEMB ^(NAME $layers))+)
	;

//
// <component> statement
//
component_statement
	: annotation* kw='component' name=IDENTIFIER '=' base=IDENTIFIER '(' arguments? ')' because_opt? ';'
			-> ^(COMPONENT[$kw] annotation* ^(NAME $name) arguments? ^(BASE ^(MEMB ^(NAME $base))) ^(BODY) because_opt?)
	;

//
// <connector> statement
//
connector_statement
	: annotation* kw='connector' name=IDENTIFIER '=' base=IDENTIFIER '(' arguments? ')' because_opt? ';'
			-> ^(CONNECTOR[$kw] annotation* ^(NAME $name) arguments? ^(BASE ^(MEMB ^(NAME $base))) ^(BODY) because_opt?)
	;

//
// <provides> statement
//
provides_statement
	: annotation* kw='provides' name=IDENTIFIER (':' maxdeg=INTEGER_LITERAL)? because_opt? ('{' provides_item* '}' | ';')
			-> ^(PROVIDES[$kw] annotation* ^(NAME $name) ^(MAXDEG $maxdeg)? ^(BODY provides_item*) because_opt?)
	;
provides_item
	: property_statement
	;

//
// <requires> statement
//
requires_statement
	: annotation* kw='requires' name=IDENTIFIER (alias+=IDENTIFIER (',' alias+=IDENTIFIER)*)? because_opt? ('{' requires_item* '}' | ';')
			-> {$alias != null}? ^(REQUIRES[$kw] annotation* ^(NAME $name) ^(ALIAS $alias) ^(BODY requires_item*) because_opt?)+
			-> ^(REQUIRES[$kw] annotation* ^(NAME $name) ^(BODY requires_item*) because_opt?)
	;
requires_item
	: property_statement
	;

//
// <check> statement
//
check_statement
	: annotation* kw='check' expression because_opt? ';'
			-> ^(CHECK[$kw] annotation* expression ^(BODY) because_opt?)
	;

//
// <link> statement
//
link_statement
	: annotation* kw='link' name=IDENTIFIER? link_consumer 'to' link_provider because_opt? ('{' link_inner_statement* '}' | ';')
			-> ^(LINK[$kw] annotation* ^(NAME $name)? link_consumer link_provider ^(BODY link_inner_statement*) because_opt?)
	;
link_consumer
	: member_expression
			-> ^(CONSUMER member_expression)
	;
link_provider
	: member_expression
			-> ^(PROVIDER member_expression)
	;
link_inner_statement
	: check_statement
	;

//
// <@> annotation
//
annotation
	: kw='@' handler=IDENTIFIER? '(' annotation_nvpair (',' annotation_nvpair)* ')'
			-> ^(ANNOTATION[$kw] ^(HANDLER $handler)? annotation_nvpair+)
	;
annotation_nvpair
	: name=IDENTIFIER '=' expression
			-> ^(NAMEDVALUE ^(NAME $name) expression)
	;

//
// <extends> option
//
extends_opt
	: kw='extends' extendee=IDENTIFIER
			-> ^(EXTENDS[$kw] ^(MEMB ^(NAME $extendee)))
	;

//
// <because> option
//
because_opt
	: kw='because' because_item (',' because_item)*
			-> ^(BECAUSE[$kw] because_item)+
	;
because_item
	: name=IDENTIFIER '(' arguments? ')'
			-> ^(BASE ^(MEMB ^(NAME $name))) arguments?
	;

//
// <supports> option
//
supports_opt
	: kw='supports' suportee+=IDENTIFIER (',' suportee+=IDENTIFIER)*
			-> ^(SUPPORTS[$kw] $suportee+)
	;

//
// <hinders> option
//
inhibits_opt
	: kw='inhibits' inhibitee+=IDENTIFIER (',' inhibitee+=IDENTIFIER)*
			-> ^(INHIBITS[$kw] $inhibitee+)
	;

//
// Formal parameter list
//
parameters
	: parms+=IDENTIFIER (',' parms+=IDENTIFIER)*
			-> ^(PARMS $parms+)
	;

//
// Member-expression argument list
//
arguments
	: args+=member_expression (',' args+=member_expression)*
			-> ^(ARGS $args+)
	;


//
// Expressions
//
expression
	: expr=inner_expression	
			-> ^(EXPR $expr)
	;
inner_expression
	: subsetof_expression
	;
subsetof_expression
	: logicalOr_expression (SUBSETOF^ logicalOr_expression)*
	;
logicalOr_expression
	: logicalAnd_expression (DIS^ logicalAnd_expression)*
	;
logicalAnd_expression
	: bitwiseOr_expression (CON^ bitwiseOr_expression)*
	;
bitwiseOr_expression
	: bitwiseXor_expression (IOR^ bitwiseXor_expression)*
	;
bitwiseXor_expression
	: bitwiseAnd_expression (XOR^ bitwiseAnd_expression)*
	;
bitwiseAnd_expression
	: equality_expresion (AND^ equality_expresion)*
	;
equality_expresion
	: relational_expresion ((EQL | NEQ)^ relational_expresion)*
	;
relational_expresion
	: acceptence_expression ((GTN | GTE | LTN | LTE)^ acceptence_expression)*
	;
acceptence_expression
	: augmentation_expression (ACCEPTS^ augmentation_expression)*
	;
augmentation_expression
	: additive_expression ((AUG | NAG)^ additive_expression)*
	;
additive_expression
	: multiplicative_expression ((ADD | SUB)^ multiplicative_expression)*
	;
multiplicative_expression 
	: unary_expresion ((MUL | DIV | MOD)^ unary_expresion)*
	;
unary_expresion
	: (CMP | NOT)^ unary_expresion
	| '+' expr=unary_expresion		-> ^(POS $expr)
	| '-' expr=unary_expresion		-> ^(NEG $expr)
	| primary_expression
	;
primary_expression
	: '('! inner_expression ')'!
	| member_expression
	| literal
	;
member_expression
	: member_part ('.' member_part)*			-> ^(MEMB member_part+)
	;
member_part
	: name=IDENTIFIER '(' member_args? ')'		-> ^(CALL ^(NAME $name) member_args?)
	| name=IDENTIFIER							-> ^(NAME $name)
	;
member_args
	: args+=expression (',' args+=expression)*	-> ^(ARGS $args+)
	;
literal
	: atom=INTEGER_LITERAL			-> ^(INTEGER $atom)
	| atom=REAL_LITERAL				-> ^(REAL $atom)
	| atom=BOOLEAN_LITERAL			-> ^(BOOLEAN $atom)
	| atom=STRING_LITERAL			-> ^(STRING $atom)
	| atom=DECLARATIVE_LITERAL		-> ^(DECL $atom)
	| set_literal
	;
set_literal
	: '[' set=set_element_list? ']' -> ^(SET $set?)
	;
set_element_list
	: set_element (','! set_element)*
	;
set_element
	: literal
	| '(' pair=set_pair ')'			-> ^(PAIR $pair)
	| name=IDENTIFIER				-> ^(NAME $name)
	;
set_pair
	: IDENTIFIER ','! literal
	;

//
// Statement
//
statement
	: layer_statement
	| component_statement
	| connector_statement
	| requires_statement
	| provides_statement
	| link_statement
	| check_statement
	| property_statement
	;

//
// Literals
//
INTEGER_LITERAL
	: DECIMAL_DIGIT+
	| '0' ('x' | 'X') HEX_DIGIT+
	;

REAL_LITERAL
	: DECIMAL_DIGIT* '.' DECIMAL_DIGIT+
	;

BOOLEAN_LITERAL
	: 'true'
	| 'false'
	;

STRING_LITERAL
	: '\'' cs=SINGLE_QUOTE_TEXT '\''		{ setText(cs.getText()); }
	| '\"' cs=DOUBLE_QUOTE_TEXT '\"'		{ setText(cs.getText()); }
	;

DECLARATIVE_LITERAL
	: '#\'' cs=SINGLE_QUOTE_TEXT '\''		{ setText(cs.getText()); }
	| '#\"' cs=DOUBLE_QUOTE_TEXT '\"'		{ setText(cs.getText()); }
	;
fragment
SINGLE_QUOTE_TEXT
	: (~('\'' | '\\' | NEWLINE_CHAR) | ESC_SEQUENCE)*
	;
fragment
DOUBLE_QUOTE_TEXT
	: (~('\"' | '\\' | NEWLINE_CHAR) | ESC_SEQUENCE)*
	;

//
// Identifiers
//
IDENTIFIER
	: (ALPHA_CHAR | '_') (ALPHANUMERIC_CHAR | '_')*
	;

//
// Whitespace & comments
//
WHITESPACE
	: (WHITESPACE_CHAR | NEWLINE_CHAR)+		{ $channel = HIDDEN; }
	;
INLINE_COMMENT
	: '//' (~(NEWLINE_CHAR))*				{ $channel = HIDDEN; }
	;
BLOCK_COMMENT
	: '/*' .* '*/'							{ $channel = HIDDEN; }
	;

//
// Base tokens
//
fragment
ESC_SEQUENCE
	: '\\' ('t'|'b'|'n'|'r'|'f'|'\''|'\"'|'\\')
	| '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
	;
fragment
ALPHANUMERIC_CHAR
	: ALPHA_CHAR | DECIMAL_DIGIT
	;
fragment
ALPHA_CHAR
	: 'a'..'z' | 'A'..'Z'
	;
fragment
DECIMAL_DIGIT
	: '0'..'9'
	;
fragment
HEX_DIGIT
	: '0'..'9' | 'a'..'f' | 'A'..'F'
	;
fragment
NEWLINE_CHAR
	: '\u000A'				// LF
	| '\u000D'				// CR		
	| '\u0085'				// Next line
	| '\u2028'				// Line separator
	| '\u2029'				// Paragraph separator
	;
fragment
WHITESPACE_CHAR
	: '\u0009'				// Tab
	| '\u000B'				// Vertical tab
	| '\u000C'				// Form feed
	| '\u0020'				// Space
	;
