package fr.loria.orpailleur.revisor.engine.revisorPL.console.parser;

import java_cup.runtime.Symbol;
import fr.loria.orpailleur.revisor.engine.core.console.exception.LexerException;

/**
 * This is the Revisor PL console lexer.
 * @author William Philbert
 */
%%

%class PLConsoleLexer
%public
%unicode
%line
%column

%cupsym PLConsoleSymbols
%cup

%type Symbol

%eofval{
    return symbol(PLConsoleSymbols.EOF);
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

Digit = [0-9]
PositiveInteger = 0 | [1-9] {Digit}*
PositiveReal = {PositiveInteger} "."? | {PositiveInteger}? "." {Digit}+

IdentifierStartChar = [a-zA-Z_]
IdentifierChar = {IdentifierStartChar} | {Digit} | "-"
Identifier = {IdentifierStartChar} {IdentifierChar}*
File = {NotWhiteSpace} ({NotLineTerminator}* {NotWhiteSpace})?

%%

<YYINITIAL> {
    ":="                 { return symbol(PLConsoleSymbols.ASSIGN); }

    "("                  { return symbol(PLConsoleSymbols.L_PAR); }

    ")"                  { return symbol(PLConsoleSymbols.R_PAR); }

    ","                  { return symbol(PLConsoleSymbols.COMMA); }

    "."                  { return symbol(PLConsoleSymbols.DOT); }

    "!"                  { return symbol(PLConsoleSymbols.NOT); }

    "&"                  { return symbol(PLConsoleSymbols.AND); }

    "|"                  { return symbol(PLConsoleSymbols.OR); }

    "<=>"                { return symbol(PLConsoleSymbols.EQU); }

    "=>"                 { return symbol(PLConsoleSymbols.IMPL); }

    "^"                  { return symbol(PLConsoleSymbols.XOR); }

    "help"               { return symbol(PLConsoleSymbols.HELP); }

    "load"               { yybegin(FILE); return symbol(PLConsoleSymbols.LOAD); }

    "clear"              { return symbol(PLConsoleSymbols.CLEAR); }

    "reset"              { return symbol(PLConsoleSymbols.RESET); }

    "weights"            { return symbol(PLConsoleSymbols.WEIGHTS); }

    "weight"             { return symbol(PLConsoleSymbols.WEIGHT); }

    "dnf"                { return symbol(PLConsoleSymbols.DNF); }

    "adapt"              { return symbol(PLConsoleSymbols.ADAPT); }

    "revise"             { return symbol(PLConsoleSymbols.REVISE); }

    "true"               { return symbol(PLConsoleSymbols.BOOLEAN, Boolean.TRUE); }

    "false"              { return symbol(PLConsoleSymbols.BOOLEAN, Boolean.FALSE); }

    {Identifier}         { return symbol(PLConsoleSymbols.IDENTIFIER, yytext()); }

    {PositiveReal}       { return symbol(PLConsoleSymbols.POSITIVE_REAL, new Double(yytext())); }
}

<FILE> {
    {File}               { return symbol(PLConsoleSymbols.FILE, yytext()); }
}

{WhiteSpace}             { /* Just skip what was found, do nothing */ }

[^]                      { throw new LexerException("Illegal character '" + yytext() + "'."); }
