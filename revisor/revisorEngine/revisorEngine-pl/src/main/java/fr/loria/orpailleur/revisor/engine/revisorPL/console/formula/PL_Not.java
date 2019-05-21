package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.UnaryOperator;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * @author William Philbert
 */
public class PL_Not<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends UnaryOperator<C, PLFormula> {
	
	// Constructors :
	
	public PL_Not(final Formula<C, PLFormula> child) {
		super(child);
	}
	
	// Methods :
	
	@Override
	public String operator(final boolean latex) {
		return latex ? PL.LATEX_NOT_SYMBOL : PL.NOT_SYMBOL;
	}
	
	@Override
	public PLFormula getValue(final C console) {
		return console.getPl().NOT(this.child.getValue(console));
	}
	
	@Override
	public PLFormula getOptimizedValue(final C console) {
		return this.getValue(console).flatten();
	}
	
}
