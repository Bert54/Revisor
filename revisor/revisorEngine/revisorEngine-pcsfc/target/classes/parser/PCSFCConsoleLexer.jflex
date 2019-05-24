package fr.loria.k.revisor.engine.revisorPCSFC.console.parser;

import java_cup.runtime.Symbol;
import fr.loria.orpailleur.revisor.engine.core.console.exception.LexerException;

/**
 * This is Revisor PCSFC's console lexer.
 * @author Matthias Bertrand
 */
%%

%class PCSFCConsoleLexer
%public
%unicode
%line
%column

%cupsym PCSFCConsoleSymbols
%cup

%type Symbol

%eofval{
    return symbol(PCSFCConsoleSymbols.EOF);
%eofval}

%{
  StringBuffer string = new StringBuffer();
  
  private Symbol symbol(int type) {
	return new Symbol(type, this.yyline, this.yycolumn);
  }

  private Symbol symbol(int type, Object value) {
	return new Symbol(type, this.yyline, this.yycolumn, value);
  }
%}

%yylexthrow LexerException

%state FILE
%state CONSTROPERS

Comment = (#{1}.*\n?)

NotLineTerminator = [^\r\n]
WhiteSpace = \s
NotWhiteSpace = \S

Integer = \-?[0-9]+|\-
Real = {Integer} | \-?(([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+))
Tautology = [tT]{1}[rR]{1}[uU]{1}[eE]{1}
ConstraintOperators = (\=\=|\!\=|<\=?|>\=?)
ArithmeticOperators = (\+|\-)
Identifier = [a-zA-Z_][a-zA-Z0-9_]*
BinaryFormulaOperators = (\&|\||\=>|<\=>|\^)
Modality = \"[a-zA-Z0-9_]+\"
EndOfInstruction = ;

File = {NotWhiteSpace} ({NotLineTerminator}* {NotWhiteSpace})?

%%

<YYINITIAL> {

	{Comment}					{/* This is a comment, therefore we do nothing */}
    
    {EndOfInstruction}			{ return symbol(PCSFCConsoleSymbols.END_OF_INSTRUCTION, yytext()); }
    
    "integer"					{ return symbol(PCSFCConsoleSymbols.INTEGER_DECLARATION_KEYWORD); }
    
    "real"						{ return symbol(PCSFCConsoleSymbols.REAL_DECLARATION_KEYWORD); }
    
    "formula"					{ return symbol(PCSFCConsoleSymbols.FORMULA_DECLARATION_KEYWORD); }   
    
    "const"						{ return symbol(PCSFCConsoleSymbols.CONST_DECLARATION_KEYWORD); }  
    
    "boolean"					{ return symbol(PCSFCConsoleSymbols.BOOLEAN_DECLARATION_KEYWORD); } 
    
    "enum"						{ return symbol(PCSFCConsoleSymbols.ENUM_DECLARATION_KEYWORD); } 
    
    "revise"					{ return symbol(PCSFCConsoleSymbols.REVISE_KEYWORD); } 
    
    {ConstraintOperators}		{ return symbol(PCSFCConsoleSymbols.CONSTRAINT_OPERATOR, yytext()); }
    
    "!"							{ return symbol(PCSFCConsoleSymbols.NEGATIVE_FORMULA_SYMBOL); }
    
    {BinaryFormulaOperators}	{ return symbol(PCSFCConsoleSymbols.BINARY_FORMULA_OPERATOR, yytext()); }
    
    ":="						{ return symbol(PCSFCConsoleSymbols.ASSIGNMENT_OPERATOR); }
        
    "="							{ return symbol(PCSFCConsoleSymbols.SIMPLE_EQUAL); }
    
    ":"							{ return symbol(PCSFCConsoleSymbols.COLON); }
    
    ","							{ return symbol(PCSFCConsoleSymbols.COMMA); }
  
  	"("							{ return symbol(PCSFCConsoleSymbols.OPENING_PARENTHESIS); }
  			
  	")"							{ return symbol(PCSFCConsoleSymbols.CLOSING_PARENTHESIS); }
	
    {Modality}            		{ return symbol(PCSFCConsoleSymbols.MODALITY, yytext()); }
  
  	{Tautology}					{ return symbol(PCSFCConsoleSymbols.TAUTOLOGY_FORMULA, yytext()); }
    
    "load"						{ yybegin(FILE); return symbol(PCSFCConsoleSymbols.LOAD); }
    
    "clear"						{ return symbol(PCSFCConsoleSymbols.CLEAR); }
    
    "printvars"					{ return symbol(PCSFCConsoleSymbols.PRINTVARS); }
    
    {Real}						{ return symbol(PCSFCConsoleSymbols.REAL, yytext()); }
    
    {Identifier}				{ yybegin(CONSTROPERS); return symbol(PCSFCConsoleSymbols.IDENTIFIER, yytext()); }
    
}

<FILE> {

    {File}            			{ return symbol(PCSFCConsoleSymbols.FILE, yytext()); }
    
}

<CONSTROPERS> {

	{ArithmeticOperators}		{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.CONSTRAINT_TERM_OPERATOR, yytext()); }

	"="							{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.SIMPLE_EQUAL); }

    ":"							{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.COLON); }

    {BinaryFormulaOperators}	{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.BINARY_FORMULA_OPERATOR, yytext()); }

    {EndOfInstruction}			{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.END_OF_INSTRUCTION, yytext()); }

	":="						{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.ASSIGNMENT_OPERATOR); }

  	"("							{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.OPENING_PARENTHESIS); }

  	")"							{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.CLOSING_PARENTHESIS); }

    ","							{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.COMMA); }

	{ConstraintOperators}		{ yybegin(YYINITIAL); return symbol(PCSFCConsoleSymbols.CONSTRAINT_OPERATOR, yytext()); }

}

{WhiteSpace}            		{ /* Just skip what was found, do nothing */ }

[^]                     		{ throw new LexerException("Illegal character '" + yytext() + "'.", null, true, false); }
