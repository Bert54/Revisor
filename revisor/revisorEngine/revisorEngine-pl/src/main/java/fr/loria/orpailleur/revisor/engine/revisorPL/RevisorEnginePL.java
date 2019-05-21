package fr.loria.orpailleur.revisor.engine.revisorPL;

import java.util.HashMap;
import java.util.Map;

import fr.loria.orpailleur.revisor.engine.core.AbstractRevisorEngine;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.NOT;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.HashedInterpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.output.Reduction;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.output.Substitution;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser.PLFormulaParser;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser.PLFormulaSyntaxError;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops.RevOp;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops.TableauxDalalRevOp;

/**
 * IMPORTANT NOTE : If you change the interface of this class (add/remove a public method or change the signature of a public method) you must update RevisorPL and RevisorPLAK (utility classes).
 * 
 * @author Gabin PERSONENI
 * @author William PHILBERT
 */
public class RevisorEnginePL extends AbstractRevisorEngine {
	
	// Fields :
	
	public final LI li;
	public final PL pl;
	public final Reduction reduction;
	protected final Map<Integer, Double> weights;
	
	// Constructors :
	
	public RevisorEnginePL() {
		this.li = new LI();
		this.pl = new PL(this.li);
		this.reduction = new Reduction(this.li, this.pl);
		this.weights = new HashMap<>();
	}
	
	// Methods :
	
	/**
	 * Resets (set to 1) the weights of every literal.
	 */
	public void resetWeights() {
		this.weights.clear();
	}
	
	/**
	 * Sets the weight of a single literal.
	 * @param literal - a string representing the literal (ex: a, !a, !!a, etc... without spaces).
	 * @param weight - the weight to be given to the literal. The weight must be a strictly positive (> 0) number.
	 */
	public void setWeight(final String literal, final double weight) {
		this.setWeight(this.li.getOrCreateID(literal), weight);
	}
	
	/**
	 * Sets the weight of a single literal.
	 * @param literal - an instance of the literal.
	 * @param weight - the weight to be given to the literal. The weight must be a strictly positive (> 0) number.
	 */
	public void setWeight(final PLLiteral literal, final double weight) {
		this.setWeight(literal.getID(), weight);
	}
	
	/**
	 * Sets the weight of a single literal.
	 * @param literalId - the id of the literal.
	 * @param weight - the weight to be given to the literal. The weight must be a strictly positive (> 0) number.
	 */
	protected void setWeight(final int literalId, final double weight) {
		this.weights.put(literalId, weight);
		this.weights.put(-literalId, weight);
	}
	
	/**
	 * Returns the weight of a single literal.
	 * @param literal - a string representing the literal (ex: a, !a, !!a, etc... without spaces).
	 * @return the weight of the literal.
	 */
	public double getWeight(final String literal) {
		return this.getWeight(this.li.getOrCreateID(literal));
	}
	
	/**
	 * Returns the weight of a single literal.
	 * @param literal - an instance of the literal.
	 * @return the weight of the literal.
	 */
	public double getWeight(final PLLiteral literal) {
		return this.getWeight(literal.getID());
	}
	
	/**
	 * Returns the weight of a single literal.
	 * @param literalId - the id of the literal.
	 * @return the weight of the literal.
	 */
	protected double getWeight(final int literalId) {
		Double weight = this.weights.get(literalId);
		
		if(weight != null) {
			return weight;
		}
		
		return 1;
	}
	
	/**
	 * Instantiates a PL formula from a string.
	 * @param input - a string representing a propositional formula in infix notation.
	 * @return the instantiated PL formula, or null if input is not syntactically correct PL formula.
	 */
	public PLFormula parseFormula(final String input) {
		try {
			PLFormula f = new PLFormulaParser(this.li).parse(input);
			return f;
		}
		catch(PLFormulaSyntaxError e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Adapts a source to a target given a domain knowledge.
	 * @param source - a PL formula representing the source.
	 * @param target - a PL formula representing the target.
	 * @param dk - a PL formula representing the domain knowledge.
	 * @return the source adapted to the target given the domain knowledge.
	 */
	public PLFormula adapt(final PLFormula source, final PLFormula target, final PLFormula dk) {
		return this.revise(this.pl.AND(dk, source), this.pl.AND(dk, target));
	}
	
	/**
	 * Revises a formula psi by an other formula mu, using Dalal revision operator.
	 * @param psi - a first PL formula.
	 * @param mu - a second PL formula.
	 * @return psi revised by mu, using Dalal revision operator.
	 */
	public PLFormula revise(final PLFormula psi, final PLFormula mu) {
		RevOp revop = new TableauxDalalRevOp(this.li, psi, this.pl.AND(mu));
		for(Integer var : this.weights.keySet()) {
			revop.setLiteralWeight(var, this.weights.get(var));
		}
		return revop.revisePsi();
	}
	
	/**
	 * Create a new interpretation.
	 * @return an interpretation.
	 */
	public Interpretation createInterpretation() {
		return new HashedInterpretation(this.li);
	}
	
	/**
	 * Puts a formula in DNF, and removes its redundant clauses.
	 * @param phi - a PL formula.
	 * @return phi in Disjunctive Normal Form, without redundant clauses.
	 */
	public PLFormula simplifiedDNF(final PLFormula phi) {
		return phi.toDNF(0x0FFFFFFF);
	}
	
	/**
	 * Returns a String representing the substitution applied to psi to obtain psi', with :<br />
	 * adapt(source, target, dk) = result<br />
	 * psi = dk & source<br />
	 * mu = dk & target<br />
	 * result = psi' & mu<br />
	 * revise(psi, mu) = psi' & mu
	 * @param source - the current belief.
	 * @param target - a new belief.
	 * @param dk - the domain knowledge.
	 * @return a String representing the substitution applied to psi to obtain psi'.
	 */
	public Substitution adaptSubstitution(final PLFormula source, final PLFormula target, final PLFormula dk) {
		PLFormula result = adapt(source, target, dk);
		return this.substitution(source, target, dk, result);
	}
	
	/**
	 * Returns a String representing the substitution applied to psi to obtain psi', with :<br />
	 * revise(psi, mu) = result<br />
	 * result = psi' & mu
	 * @param psi - the current belief.
	 * @param mu - a new belief.
	 * @return a String representing the substitution applied to psi to obtain psi'.
	 */
	public Substitution reviseSubstitution(final PLFormula psi, final PLFormula mu) {
		PLFormula result = revise(psi, mu);
		return this.substitution(psi, mu, result);
	}
	
	/**
	 * Returns a String representing the substitution applied to psi to obtain psi', with :<br />
	 * adapt(source, target, dk) = result<br />
	 * psi = dk & source<br />
	 * mu = dk & target<br />
	 * result = psi' & mu<br />
	 * revise(psi, mu) = psi' & mu
	 * @param source - the current belief.
	 * @param target - a new belief.
	 * @param dk - the domain knowledge.
	 * @param result - the result of adapt(source, target, dk).
	 * @return a String representing the substitution applied to psi to obtain psi'.
	 */
	public Substitution substitution(final PLFormula source, final PLFormula target, final PLFormula dk, final PLFormula result) {
		PLFormula psi = this.pl.AND(dk, source);
		PLFormula mu = this.pl.AND(dk, target);
		return this.substitution(psi, mu, result);
	}
	
	/**
	 * Returns a String representing the substitution applied to psi to obtain psi', with :<br />
	 * revise(psi, mu) = result<br />
	 * result = psi' & mu
	 * @param psi - the current belief.
	 * @param mu - a new belief.
	 * @param result - the result of revise(psi, mu).
	 * @return a String representing the substitution applied to psi to obtain psi'.
	 */
	public Substitution substitution(final PLFormula psi, final PLFormula mu, final PLFormula result) {
		return this.reduction.findSubstitution(psi, mu, result);
	}
	
	/**
	 * Indicates whether f1 is equivalent to f2.
	 * @param f1 - a first PL formula.
	 * @param f2 - a second PL formula.
	 * @return true if and only if f1 is equivalent to f2.
	 */
	public boolean equivalent(final PLFormula f1, final PLFormula f2) {
		return entails(f1, f2) && entails(f2, f1);
	}
	
	/**
	 * Indicates whether f1 entails f2.
	 * @param f1 - a first PL formula.
	 * @param f2 - a second PL formula.
	 * @return true if and only if f1 entails f2.
	 */
	public boolean entails(final PLFormula f1, final PLFormula f2) {
		// f3 = f1 & !f2 = (f1 doesn't entails f2)
		PLFormula f3 = new AND(this.li, f1, new NOT(this.li, f2));
		f3 = f3.toDNF(0x0FFFFFFF);
		try {
			// f1 entails f2 <=> f3 is unsatisfiable
			Interpretation i = this.createInterpretation();
			return !i.satisfies(f3);
		}
		catch(InterpretationFunctionDomainException e) {
			return false;
		}
	}
	
	/**
	 * Prints a formula in infixes notation.
	 * @param f - a PL formula.
	 */
	public void print(final PLFormula f) {
		System.out.println(f);
	}
	
	/**
	 * Prints a formula in prefixed notation.
	 * @param f - a PL formula.
	 */
	public void printPrefixed(final PLFormula f) {
		System.out.println(f.toString_Prefixed());
	}
	
	@Override
	public String formatNameToLatex(String name) {
		StringBuilder builder = new StringBuilder();
		
		while(name.startsWith(PL.NOT_SYMBOL)) {
			builder.append(PL.LATEX_NOT_SYMBOL);
			name = name.substring(1);
		}
		
		if(name.equals(PL.TRUE_SYMBOL) || name.equals(PL.FALSE_SYMBOL)) {
			builder.append(PL.symbol(name));
		}
		else {
			builder.append(super.formatNameToLatex(name));
		}
		
		return builder.toString();
	}
	
}
