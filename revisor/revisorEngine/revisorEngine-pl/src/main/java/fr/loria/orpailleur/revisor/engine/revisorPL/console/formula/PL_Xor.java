package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.formula.BinaryOperator;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * @author William Philbert
 */
public class PL_Xor<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends BinaryOperator<C, PLFormula> {
	
	// Constructors :
	
	public PL_Xor(final Formula<C, PLFormula> left, final Formula<C, PLFormula> right) {
		super(left, right);
	}
	
	// Methods :
	
	@Override
	public boolean canExtend() {
		return false;
	}
	
	@Override
	public String operator(final boolean latex) {
		return latex ? PL.LATEX_XOR_SYMBOL : PL.XOR_SYMBOL;
	}
	
	@Override
	public PLFormula getValue(final C console) {
		return console.getPl().XOR(this.left.getValue(console), this.right.getValue(console));
	}
	
	@Override
	public PLFormula getOptimizedValue(final C console) {
		return this.getValue(console).flatten();
	}
	
}
