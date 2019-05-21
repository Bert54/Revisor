package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.SpecialOperator;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * @author William Philbert
 */
public class PL_SimplifiedDNF<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends SpecialOperator<C, PLFormula> {
	
	// Fields :
	
	protected final Formula<C, PLFormula> formula;
	
	// Constructors :
	
	public PL_SimplifiedDNF(final Formula<C, PLFormula> formula) {
		super(formula);
		this.formula = formula;
	}
	
	// Methods :
	
	@Override
	public String operator(final boolean latex) {
		return latex ? "DNF" : "dnf";
	}
	
	@Override
	public PLFormula getValue(final C console) {
		return console.simplifiedDNF(this.formula.getValue(console));
	}
	
}
