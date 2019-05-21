package fr.loria.orpailleur.revisor.engine.revisorPLAK;

import fr.loria.orpailleur.revisor.engine.revisorPL.RevisorEnginePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.output.Substitution;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.RuleSet;

/**
 * @author Gabin PERSONENI
 * @author William PHILBERT
 */
public class RevisorPLAK {
	
	// Constants :
	
	private static final RevisorEnginePLAK engine = new RevisorEnginePLAK();
	public static final LI li = engine.li;
	public static final PL pl = engine.pl;
	
	// Constructors :
	
	private RevisorPLAK() {
		// This class don't have to be instantiated.
	}
	
	// PL Methods :
	
	/**
	 * @see RevisorEnginePL#resetWeights()
	 */
	public static final void resetWeights() {
		engine.resetWeights();
	}
	
	/**
	 * @see RevisorEnginePL#setWeight(String, double)
	 */
	public static final void setWeight(final String literal, final double weight) {
		engine.setWeight(literal, weight);
	}
	
	/**
	 * @see RevisorEnginePL#setWeight(PLLiteral, double)
	 */
	public static final void setWeight(final PLLiteral variable, final double weight) {
		engine.setWeight(variable, weight);
	}
	
	/**
	 * @see RevisorEnginePL#getWeight(String)
	 */
	public static final double getWeight(final String literal) {
		return engine.getWeight(literal);
	}
	
	/**
	 * @see RevisorEnginePL#getWeight(PLLiteral)
	 */
	public static final double getWeight(final PLLiteral variable) {
		return engine.getWeight(variable);
	}
	
	/**
	 * @see RevisorEnginePL#parseFormula(String)
	 */
	public static final PLFormula parseFormula(final String input) {
		return engine.parseFormula(input);
	}
	
	/**
	 * @see RevisorEnginePL#adapt(PLFormula, PLFormula, PLFormula)
	 */
	public static final PLFormula adapt(final PLFormula source, final PLFormula target, final PLFormula dk) {
		return engine.adapt(source, target, dk);
	}
	
	/**
	 * @see RevisorEnginePL#revise(PLFormula, PLFormula)
	 */
	public static final PLFormula revise(final PLFormula psi, final PLFormula mu) {
		return engine.revise(psi, mu);
	}
	
	/**
	 * @see RevisorEnginePL#createInterpretation()
	 */
	public static final Interpretation createInterpretation() {
		return engine.createInterpretation();
	}
	
	/**
	 * @see RevisorEnginePL#simplifiedDNF(PLFormula)
	 */
	public static final PLFormula simplifiedDNF(final PLFormula phi) {
		return engine.simplifiedDNF(phi);
	}
	
	/**
	 * @see RevisorEnginePL#adaptSubstitution(PLFormula, PLFormula, PLFormula)
	 */
	public static final Substitution adaptSubstitution(final PLFormula source, final PLFormula target, final PLFormula dk) {
		return engine.adaptSubstitution(source, target, dk);
	}
	
	/**
	 * @see RevisorEnginePL#reviseSubstitution(PLFormula, PLFormula)
	 */
	public static final Substitution reviseSubstitution(final PLFormula psi, final PLFormula mu) {
		return engine.reviseSubstitution(psi, mu);
	}
	
	/**
	 * @see RevisorEnginePL#substitution(PLFormula, PLFormula, PLFormula, PLFormula)
	 */
	public static final Substitution substitution(final PLFormula source, final PLFormula target, final PLFormula dk, final PLFormula result) {
		return engine.substitution(source, target, dk, result);
	}
	
	/**
	 * @see RevisorEnginePL#substitution(PLFormula, PLFormula, PLFormula)
	 */
	public static final Substitution substitution(final PLFormula psi, final PLFormula mu, final PLFormula result) {
		return engine.substitution(psi, mu, result);
	}
	
	/**
	 * @see RevisorEnginePL#equivalent(PLFormula, PLFormula)
	 */
	public static final boolean equivalent(final PLFormula f1, final PLFormula f2) {
		return engine.equivalent(f1, f2);
	}
	
	/**
	 * @see RevisorEnginePL#entails(PLFormula, PLFormula)
	 */
	public static final boolean entails(final PLFormula f1, final PLFormula f2) {
		return engine.entails(f1, f2);
	}
	
	/**
	 * @see RevisorEnginePL#print(PLFormula)
	 */
	public static final void print(final PLFormula f) {
		engine.print(f);
	}
	
	/**
	 * @see RevisorEnginePL#printPrefixed(PLFormula)
	 */
	public static final void printPrefixed(final PLFormula f) {
		engine.printPrefixed(f);
	}
	
	/**
	 * @see RevisorEnginePL#formatNameToLatex(String)
	 */
	public static final String formatNameToLatex(final String name) {
		return engine.formatNameToLatex(name);
	}
	
	// PLAK Methods :
	
	/**
	 * @see RevisorEnginePLAK#clearFlipCosts()
	 */
	public static final void clearFlipCosts() {
		engine.clearFlipCosts();
	}
	
	/**
	 * @see RevisorEnginePLAK#setFlipCost(String, double)
	 */
	public static final void setFlipCost(final String literal, final double cost) {
		engine.setFlipCost(literal, cost);
	}
	
	/**
	 * @see RevisorEnginePLAK#setFlipCost(PLLiteral, double)
	 */
	public static final void setFlipCost(final PLLiteral literal, final double cost) {
		engine.setFlipCost(literal, cost);
	}
	
	/**
	 * @see RevisorEnginePLAK#getFlipCost(String)
	 */
	public static final double getFlipCost(final String literal) {
		return engine.getFlipCost(literal);
	}
	
	/**
	 * @see RevisorEnginePLAK#getFlipCost(PLLiteral)
	 */
	public static final double getFlipCost(final PLLiteral literal) {
		return engine.getFlipCost(literal);
	}
	
	/**
	 * @see RevisorEnginePLAK#optimizeCommutables(RuleSet)
	 */
	public static final void optimizeCommutables(final RuleSet ruleSet) {
		engine.optimizeCommutables(ruleSet);
	}
	
	/**
	 * @see RevisorEnginePLAK#adaptAK(PLFormula, PLFormula, PLFormula, RuleSet)
	 */
	public static final PLFormula adaptAK(final PLFormula source, final PLFormula target, final PLFormula DK, final RuleSet ruleSet) {
		return engine.adaptAK(source, target, DK, ruleSet);
	}
	
	/**
	 * @see RevisorEnginePLAK#adaptAK(PLFormula, PLFormula, RuleSet)
	 */
	public static final PLFormula adaptAK(final PLFormula psi, final PLFormula mu, final RuleSet ruleSet) {
		return engine.adaptAK(psi, mu, ruleSet);
	}
	
	/**
	 * @see RevisorEnginePLAK#adaptAK_fromPI(PLFormula, PLFormula, RuleSet)
	 */
	public static final PLFormula adaptAK_fromPI(final PLFormula psi, final PLFormula mu, final RuleSet ruleSet) {
		return engine.adaptAK_fromPI(psi, mu, ruleSet);
	}
	
	/**
	 * @see RevisorEnginePLAK#adaptSubstitution(PLFormula, PLFormula, RuleSet)
	 */
	public static final Substitution adaptSubstitution(final PLFormula source, final PLFormula target, final PLFormula dk, final RuleSet ruleSet) {
		return engine.adaptSubstitution(source, target, dk, ruleSet);
	}
	
	/**
	 * @see RevisorEnginePLAK#reviseSubstitution(PLFormula, PLFormula, RuleSet)
	 */
	public static final Substitution reviseSubstitution(final PLFormula psi, final PLFormula mu, final RuleSet ruleSet) {
		return engine.reviseSubstitution(psi, mu, ruleSet);
	}
	
}
