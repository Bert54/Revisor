package fr.loria.orpailleur.revisor.engine.revisorPLAK;

import fr.loria.orpailleur.revisor.engine.revisorPL.RevisorEnginePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.output.Substitution;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops.RevOp;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.AKAdaptOp;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak.RuleSet;

/**
 * IMPORTANT NOTE : If you change the interface of this class (add/remove a public method or change the signature of a public method) you must update RevisorPLAK (utility class).
 * 
 * @author Gabin PERSONENI
 * @author William PHILBERT
 */
public class RevisorEnginePLAK extends RevisorEnginePL {
	
	// Constructors :
	
	public RevisorEnginePLAK() {
		super();
	}
	
	// Methods :
	
	/**
	 * Reset all flip costs to 1.
	 */
	public void clearFlipCosts() {
		this.weights.clear();
	}
	
	/**
	 * Sets the flip cost of a single literal.
	 * @param literal - a literal l. Note that the cost of l and !l is independent.
	 * @param cost - cost to flip this literal. The cost must be > 0.
	 */
	public void setFlipCost(final String literal, final double cost) {
		this.weights.put(this.li.getID(literal), cost);
	}
	
	/**
	 * Sets the flip cost of a single literal.
	 * @param literal - an instance of the literal.
	 * @param cost - cost to flip this literal. The cost must be > 0.
	 */
	public void setFlipCost(final PLLiteral literal, final double cost) {
		this.setFlipCost(literal.getID(), cost);
	}
	
	/**
	 * Sets the flip cost of a single literal.
	 * @param literalId - the id of the literal.
	 * @param cost - cost to flip this literal. The cost must be > 0.
	 */
	protected void setFlipCost(final int literalId, final double cost) {
		this.weights.put(literalId, cost);
	}
	
	/**
	 * Returns the flip cost of a single literal.
	 * @param literal - a literal l. Note that the cost of l and !l is independent.
	 * @return the flip cost of the literal.
	 */
	public double getFlipCost(final String literal) {
		return this.getFlipCost(this.li.getOrCreateID(literal));
	}
	
	/**
	 * Returns the flip cost of a single literal.
	 * @param literal - an instance of the literal.
	 * @return the flip cost of the literal.
	 */
	public double getFlipCost(final PLLiteral literal) {
		return this.getFlipCost(literal.getID());
	}
	
	/**
	 * Returns the flip cost of a single literal.
	 * @param literalId - the id of the literal.
	 * @return the flip cost of the literal.
	 */
	protected double getFlipCost(final int literalId) {
		Double weight = this.weights.get(literalId);
		
		if(weight != null) {
			return weight;
		}
		
		return 1;
	}
	
	/**
	 * Find commutable rules in a set of rules to optimize all adaptations performed using that set of rules.
	 * @param ruleSet - set of adaptation rules.
	 */
	public void optimizeCommutables(final RuleSet ruleSet) {
		ruleSet.computeCommutables();
	}
	
	/**
	 * 
	 * @param source - a formula representing the source case in any form.
	 * @param target - a formula representing the target case in any form.
	 * @param dk - a formula representing the domain knowledge in any form.
	 * @param ruleSet - a set of adaptation rules.
	 * @return the source adapted to the target given the domain knowledge.
	 */
	public PLFormula adaptAK(final PLFormula source, final PLFormula target, final PLFormula DK, final RuleSet ruleSet) {
		return this.adaptAK_fromPI(this.pl.AND(DK, source).toPI_DNF(), this.pl.AND(DK, target).toPI_DNF(), ruleSet);
	}
	
	/**
	 * 
	 * @param psi - a formula representing (Source & DK) in any form.
	 * @param mu - a formula representing (Target & DK) in any form.
	 * @param ruleSet - a set of adaptation rules.
	 * @return the source adapted to the target given the domain knowledge.
	 */
	public PLFormula adaptAK(final PLFormula psi, final PLFormula mu, final RuleSet ruleSet) {
		return this.adaptAK_fromPI(psi.toPI_DNF(), mu.toPI_DNF(), ruleSet);
	}
	
	/**
	 * 
	 * @param psi - DNF of primes implicants (Source & DK).
	 * @param mu - DNF of primes implicants of (Target & DK).
	 * @param ruleSet - a set of adaptation rules.
	 * @return the source adapted to the target given the domain knowledge.
	 */
	public PLFormula adaptAK_fromPI(final PLFormula psi, final PLFormula mu, final RuleSet ruleSet) {
		RevOp revop = new AKAdaptOp(this.li, psi, mu, ruleSet);
		for(Integer var : this.weights.keySet()) {
			revop.setLiteralWeight(var, this.weights.get(var));
		}
		return revop.revisePsi();
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
	 * @param ruleSet - a set of adaptation rules.
	 * @return a String representing the substitution between psi and psi'.
	 */
	public Substitution adaptSubstitution(final PLFormula source, final PLFormula target, final PLFormula dk, final RuleSet ruleSet) {
		PLFormula result = adaptAK(source, target, dk, ruleSet);
		return this.substitution(source, target, dk, result);
	}
	
	/**
	 * Returns a String representing the substitution applied to psi to obtain psi', with :<br />
	 * revise(psi, mu) = result<br />
	 * result = psi' & mu
	 * @param psi - the current belief.
	 * @param mu - a new belief.
	 * @param ruleSet - a set of adaptation rules.
	 * @return a String representing the substitution between psi and psi'.
	 */
	public Substitution reviseSubstitution(final PLFormula psi, final PLFormula mu, final RuleSet ruleSet) {
		PLFormula result = adaptAK(psi, mu, ruleSet);
		return this.substitution(psi, mu, result);
	}
	
}
