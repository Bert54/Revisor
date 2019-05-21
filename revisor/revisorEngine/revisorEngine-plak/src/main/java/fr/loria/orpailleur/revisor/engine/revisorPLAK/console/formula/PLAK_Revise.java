package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.SpecialOperator;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.sidenote.SubstitutionSideNote;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_Revise<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends SpecialOperator<C, PLFormula> {
	
	// Fields :
	
	protected final Formula<C, PLFormula> psi;
	protected final Formula<C, PLFormula> mu;
	protected final Expression<C, PLAK_RuleSet<C>> ruleSet;
	
	// Constructors :
	
	public PLAK_Revise(final Formula<C, PLFormula> psi, final Formula<C, PLFormula> mu, final Expression<C, PLAK_RuleSet<C>> ruleSet) {
		super(psi, mu, ruleSet);
		this.psi = psi;
		this.mu = mu;
		this.ruleSet = ruleSet;
	}
	
	// Methods :
	
	@Override
	public String operator(final boolean latex) {
		return "revise";
	}
	
	@Override
	public PLFormula getValue(final C console) {
		final PLFormula psi = this.psi.getValue(console);
		final PLFormula mu = this.mu.getValue(console);
		final PLAK_RuleSet<C> ruleSet = this.ruleSet.getValue(console);
		final PLFormula result = console.simplifiedDNF(console.revise(psi, mu, ruleSet));
		
		this.setSideNote(new SubstitutionSideNote<>(console, psi, mu, result));
		return result;
	}
	
}
