package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.formula.SpecialOperator;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.sidenote.SubstitutionSideNote;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * @author William Philbert
 */
public class PL_Adapt<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends SpecialOperator<C, PLFormula> {
	
	// Fields :
	
	protected final Formula<C, PLFormula> dk;
	protected final Formula<C, PLFormula> source;
	protected final Formula<C, PLFormula> target;
	
	// Constructors :
	
	public PL_Adapt(final Formula<C, PLFormula> dk, final Formula<C, PLFormula> source, final Formula<C, PLFormula> target) {
		super(dk, source, target);
		this.dk = dk;
		this.source = source;
		this.target = target;
	}
	
	// Methods :
	
	@Override
	public String operator(final boolean latex) {
		return "adapt";
	}
	
	@Override
	public PLFormula getValue(final C console) {
		final PLFormula dk = this.dk.getValue(console);
		final PLFormula source = this.source.getValue(console);
		final PLFormula target = this.target.getValue(console);
		final PLFormula result = console.simplifiedDNF(console.adapt(dk, source, target));
		
		this.setSideNote(new SubstitutionSideNote<>(console, dk, source, target, result));
		return result;
	}
	
}
