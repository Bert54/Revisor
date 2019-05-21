package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.parser;

import java_cup.runtime.Symbol;
import fr.loria.orpailleur.revisor.engine.core.console.exception.LexerException;

/**
 * This is the Revisor PLAK console lexer.
 * @author William Philbert
 */
%%

%class PLAKConsoleLexer
%public
%unicode
%line
%column

%cupsym PLAKConsoleSymbols
%cup

%type Symbol

%eofval{
    return symbol(PLAKConsoleSymbols.EOF);
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
    ":="                 { return symbol(PLAKConsoleSymbols.ASSIGN); }

    "+="                 { return symbol(PLAKConsoleSymbols.ADD); }

    "-="                 { return symbol(PLAKConsoleSymbols.REMOVE); }

    "~>"                 { return symbol(PLAKConsoleSymbols.REPLACE); }

    "("                  { return symbol(PLAKConsoleSymbols.L_PAR); }

    ")"                  { return symbol(PLAKConsoleSymbols.R_PAR); }

    "{"                  { return symbol(PLAKConsoleSymbols.L_BRACE); }

    "}"                  { return symbol(PLAKConsoleSymbols.R_BRACE); }

    "["                  { return symbol(PLAKConsoleSymbols.L_BRACKET); }

    "]"                  { return symbol(PLAKConsoleSymbols.R_BRACKET); }

    ","                  { return symbol(PLAKConsoleSymbols.COMMA); }

    "."                  { return symbol(PLAKConsoleSymbols.DOT); }

    ":"                  { return symbol(PLAKConsoleSymbols.COLON); }

    "!"                  { return symbol(PLAKConsoleSymbols.NOT); }

    "&"                  { return symbol(PLAKConsoleSymbols.AND); }

    "|"                  { return symbol(PLAKConsoleSymbols.OR); }

    "<=>"                { return symbol(PLAKConsoleSymbols.EQU); }

    "=>"                 { return symbol(PLAKConsoleSymbols.IMPL); }

    "^"                  { return symbol(PLAKConsoleSymbols.XOR); }

    "help"               { return symbol(PLAKConsoleSymbols.HELP); }

    "load"               { yybegin(FILE); return symbol(PLAKConsoleSymbols.LOAD); }

    "clear"              { return symbol(PLAKConsoleSymbols.CLEAR); }

    "reset"              { return symbol(PLAKConsoleSymbols.RESET); }

    "weights"            { return symbol(PLAKConsoleSymbols.WEIGHTS); }

    "weight"             { return symbol(PLAKConsoleSymbols.WEIGHT); }

    "flipcosts"          { return symbol(PLAKConsoleSymbols.FLIPCOSTS); }

    "flipcost"           { return symbol(PLAKConsoleSymbols.FLIPCOST); }

    "rulecosts"          { return symbol(PLAKConsoleSymbols.RULECOSTS); }

    "rulecost"           { return symbol(PLAKConsoleSymbols.RULECOST); }

    "rules"              { return symbol(PLAKConsoleSymbols.RULES); }

    "rulesets"           { return symbol(PLAKConsoleSymbols.RULESETS); }

    "ruleset"            { return symbol(PLAKConsoleSymbols.RULESET); }

    "use"                { return symbol(PLAKConsoleSymbols.USE); }

    "default"            { return symbol(PLAKConsoleSymbols.DEFAULT); }

    "dnf"                { return symbol(PLAKConsoleSymbols.DNF); }

    "adapt"              { return symbol(PLAKConsoleSymbols.ADAPT); }

    "revise"             { return symbol(PLAKConsoleSymbols.REVISE); }

    "true"               { return symbol(PLAKConsoleSymbols.BOOLEAN, Boolean.TRUE); }

    "false"              { return symbol(PLAKConsoleSymbols.BOOLEAN, Boolean.FALSE); }

    {Identifier}         { return symbol(PLAKConsoleSymbols.IDENTIFIER, yytext()); }

    {PositiveReal}       { return symbol(PLAKConsoleSymbols.POSITIVE_REAL, new Double(yytext())); }
}

<FILE> {
    {File}               { return symbol(PLAKConsoleSymbols.FILE, yytext()); }
}

{WhiteSpace}             { /* Just skip what was found, do nothing */ }

[^]                      { throw new LexerException("Illegal character '" + yytext() + "'."); }
