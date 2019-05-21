package fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser;

import java.util.ArrayList;
import java.util.HashMap;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.NOT;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.OR;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

public class PLFormulaParser {
	
	// Constants :
	
	public static final char AND_SYMBOL = '&';
	public static final char OR_SYMBOL = '|';
	public static final char NOT_SYMBOL = '!';
	public static final char IMPLICATION_SYMBOL = '>';
	
	// Fields :
	
	private final LI li;
	public boolean verbose = false;
	int parsing_index = 0;
	
	// Constructors :
	
	public PLFormulaParser(final LI li) {
		this.li = li;
	}
	
	// Methods :
	
	public boolean isVariableCharacter(final char c) {
		return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_';
	}
	
	public boolean isOperator(final char c) {
		return AND_SYMBOL == c || OR_SYMBOL == c || NOT_SYMBOL == c || IMPLICATION_SYMBOL == c;
	}
	
	public PLFormula parse(String input) throws PLFormulaSyntaxError {
		if(input.trim().length() == 0) {
			return new AND(this.li);
		}
		
		if(this.verbose) {
			System.out.print("PARSING: " + input + "\n");
		}
		
		input += " ";
		HashMap<String, PLLiteral> vars = new HashMap<>();
		ArrayList<ArrayList<Object>> pile = new ArrayList<>();
		ArrayList<Object> sous_formule = new ArrayList<>();
		pile.add(sous_formule);
		String tmp = "";
		
		for(int i = 0; i < input.length(); i++) {
			char ci = input.charAt(i);
			
			if(ci != ' ' && ci != ')' && ci != '(' && !this.isOperator(ci)) {
				tmp += ci;
			}
			else {
				if(tmp.length() > 0) {
					PLLiteral var = vars.get(tmp);
					
					if(var == null) {
						var = new PLLiteral(this.li, tmp);
						vars.put(tmp, var);
					}
					
					tmp = "";
					sous_formule.add(var);
				}
				
				if(this.isOperator(ci)) {
					sous_formule.add(ci);
				}
				else if(ci == ')') {
					if(pile.size() <= 1) {
						throw new PLFormulaSyntaxError("Unexpected closing parenthesis");
					}
					if(sous_formule.size() < 1) {
						throw new PLFormulaSyntaxError("Unexpected closing parenthesis");
					}
					
					this.reduce(sous_formule);
					pile.get(pile.size() - 2).add(sous_formule.get(0));
					pile.remove(sous_formule);
					sous_formule = pile.get(pile.size() - 1);
				}
				else if(ci == '(') {
					pile.add(sous_formule = new ArrayList<>());
				}
			}
		}
		
		this.reduce(sous_formule);
		
		if(pile.size() > 1) {
			throw new PLFormulaSyntaxError("Closing parenthesis missing");
		}
		
		return (PLFormula) sous_formule.get(0);
	}
	
	private void reduce(final ArrayList<Object> stack) throws PLFormulaSyntaxError {
		if(this.verbose) {
			System.out.print("REDUCE: ");
			this.afficherEtat(stack);
		}
		
		// NON
		for(int i = 0; i < stack.size(); i++) {
			if(i < 0)
				i = 0;
			try {
				if(stack.get(i).equals(NOT_SYMBOL) && stack.get(i + 1) instanceof PLFormula) {
					PLFormula f1 = (PLFormula) stack.remove(i + 1);
					stack.remove(i);
					stack.add(i, new NOT(this.li, f1));
					i = i - 2;
					this.afficherEtat(stack);
				}
			}
			catch(Exception e) {
				throw new PLFormulaSyntaxError("Operand missing for " + NOT_SYMBOL);
			}
		}
		
		// OU
		this.Nreduce(stack, OR_SYMBOL);
		
		// ET
		this.Nreduce(stack, AND_SYMBOL);
		
		// IMP
		for(int i = stack.size() - 1; i >= 0; i--) {
			try {
				if(stack.get(i).equals(IMPLICATION_SYMBOL) && stack.get(i - 1) instanceof PLFormula && stack.get(i + 1) instanceof PLFormula) {
					PLFormula f2 = (PLFormula) stack.remove(i + 1);
					stack.remove(i);
					PLFormula f1 = (PLFormula) stack.remove(i - 1);
					stack.add(i - 1, new OR(this.li, new NOT(this.li, f1), f2));
					i = i - 1;
					this.afficherEtat(stack);
				}
			}
			catch(Exception e) {
				throw new PLFormulaSyntaxError("Operands missing for " + IMPLICATION_SYMBOL);
			}
		}
		if(stack.size() > 1) {
			throw new PLFormulaSyntaxError("Operator missing");
		}
		
		return;
	}
	
	public void Nreduce(final ArrayList<Object> stack, final char symbole_op) throws PLFormulaSyntaxError {
		// pour chaque 'mot' de l'expression
		for(int i = 0; i < stack.size(); i++) {
			// si ce mot est l'operateur que l'on recherche
			if(stack.get(i).equals(symbole_op)) {
				try {
					ArrayList<PLFormula> operandes = new ArrayList<>();
					// on recupere ses operandes droite et gauche
					PLFormula f2 = (PLFormula) stack.remove(i + 1);
					stack.remove(i);
					PLFormula f1 = (PLFormula) stack.remove(i - 1);
					operandes.add(f1);
					operandes.add(f2);
					
					// recherche d'un pattern n-aire (v1 & v2 & v3 & ... & vN)
					while(i - 1 < stack.size() && stack.get(i - 1).equals(symbole_op)) {
						PLFormula f3 = (PLFormula) stack.remove(i);
						stack.remove(i - 1);
						operandes.add(f3);
					}
					
					if(symbole_op == OR_SYMBOL) {
						stack.add(i - 1, new OR(this.li, operandes.toArray(new PLFormula[operandes.size()])));
					}
					else if(symbole_op == AND_SYMBOL) {
						stack.add(i - 1, new AND(this.li, operandes.toArray(new PLFormula[operandes.size()])));
					}
					
					i = i - 1;
					this.afficherEtat(stack);
				}
				catch(Exception e) {
					e.printStackTrace();
					throw new PLFormulaSyntaxError("Missing operands for " + symbole_op);
				}
			}
		}
	}
	
	public void afficherEtat(final ArrayList<Object> stack) {
		if(this.verbose) {
			for(Object o : stack) {
				System.out.print("[" + o + "],");
			}
			
			System.out.print("\n");
		}
	}
	
}
