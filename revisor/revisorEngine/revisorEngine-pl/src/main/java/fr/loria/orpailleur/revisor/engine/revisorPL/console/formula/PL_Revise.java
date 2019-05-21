package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.SpecialOperator;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.sidenote.SubstitutionSideNote;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * @author William Philbert
 */
public class PL_Revise<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends SpecialOperator<C, PLFormula> {
	
	// Fields :
	
	protected final Formula<C, PLFormula> psi;
	protected final Formula<C, PLFormula> mu;
	
	// Constructors :
	
	public PL_Revise(final Formula<C, PLFormula> psi, final Formula<C, PLFormula> mu) {
		super(psi, mu);
		this.psi = psi;
		this.mu = mu;
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
		final PLFormula result = console.simplifiedDNF(console.revise(psi, mu));
		
		this.setSideNote(new SubstitutionSideNote<>(console, psi, mu, result));
		return result;
	}
	
}
