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

NotLineTerminator = [^\r\n]
WhiteSpace = \s
NotWhiteSpace = \S

Integer = \-?[0-9]+
Real = {Integer} | \-?(([0-9]+\.[0-9]*)|([0-9]*\.[0-9]+))
Tautology = [tT]{1}[rR]{1}[uU]{1}[eE]{1}
ConstraintOperators = (\=\=|\!\=|<\=?|>\=?)
ArithmeticOperators = (\+|\-)
Identifier = [a-zA-Z][a-zA-Z0-9]*

File = {NotWhiteSpace} ({NotLineTerminator}* {NotWhiteSpace})?

%%

<YYINITIAL> {
    
    ";"						{ return symbol(PCSFCConsoleSymbols.END_OF_INSTRUCTION); }
    
    "integer"				{ return symbol(PCSFCConsoleSymbols.INTEGER_DECLARATION_KEYWORD); }
    
    "real"					{ return symbol(PCSFCConsoleSymbols.REAL_DECLARATION_KEYWORD); }
    
    "formula"				{ return symbol(PCSFCConsoleSymbols.FORMULA_DECLARATION_KEYWORD); }   
    
    "const"					{ return symbol(PCSFCConsoleSymbols.CONST_DECLARATION_KEYWORD); }  
    
    "revise"				{ return symbol(PCSFCConsoleSymbols.REVISE_KEYWORD); } 
    
    {ConstraintOperators}	{ return symbol(PCSFCConsoleSymbols.CONSTRAINT_OPERATOR, yytext()); }
    
    {ArithmeticOperators}	{ return symbol(PCSFCConsoleSymbols.CONSTRAINT_TERM_OPERATOR, yytext()); }
    
    ":="					{ return symbol(PCSFCConsoleSymbols.ASSIGNMENT_OPERATOR); }
    
    "="						{ return symbol(PCSFCConsoleSymbols.CONST_INITIALIZER_OPERATOR); }
    
    ":"						{ return symbol(PCSFCConsoleSymbols.COLON); }
    
    ","						{ return symbol(PCSFCConsoleSymbols.COMMA); }
  
  	"("						{ return symbol(PCSFCConsoleSymbols.OPENING_PARENTHESIS); }
  			
  	")"						{ return symbol(PCSFCConsoleSymbols.CLOSING_PARENTHESIS); }
  
  	{Tautology}				{ return symbol(PCSFCConsoleSymbols.TAUTOLOGY_FORMULA, yytext()); }
    
    "load"					{ yybegin(FILE); return symbol(PCSFCConsoleSymbols.LOAD); }
    
    "clear"					{ return symbol(PCSFCConsoleSymbols.CLEAR); }
    
    {Real}					{ return symbol(PCSFCConsoleSymbols.REAL, yytext()); }
    
    {Identifier}			{ return symbol(PCSFCConsoleSymbols.IDENTIFIER, yytext()); }

}

<FILE> {
    {File}            		{ return symbol(PCSFCConsoleSymbols.FILE, yytext()); }
}

{WhiteSpace}            	{ /* Just skip what was found, do nothing */ }

[^]                     	{ throw new LexerException("Illegal character '" + yytext() + "'."); }