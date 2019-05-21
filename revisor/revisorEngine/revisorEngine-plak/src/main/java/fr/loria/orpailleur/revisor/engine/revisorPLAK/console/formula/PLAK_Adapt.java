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
public class PLAK_Adapt<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends SpecialOperator<C, PLFormula> {
	
	// Fields :
	
	protected final Formula<C, PLFormula> dk;
	protected final Formula<C, PLFormula> source;
	protected final Formula<C, PLFormula> target;
	protected final Expression<C, PLAK_RuleSet<C>> ruleSet;
	
	// Constructors :
	
	public PLAK_Adapt(final Formula<C, PLFormula> dk, final Formula<C, PLFormula> source, final Formula<C, PLFormula> target, final Expression<C, PLAK_RuleSet<C>> ruleSet) {
		super(dk, source, target, ruleSet);
		this.dk = dk;
		this.source = source;
		this.target = target;
		this.ruleSet = ruleSet;
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
		final PLAK_RuleSet<C> ruleSet = this.ruleSet.getValue(console);
		final PLFormula result = console.simplifiedDNF(console.adapt(dk, source, target, ruleSet));
		
		this.setSideNote(new SubstitutionSideNote<>(console, dk, source, target, result));
		return result;
	}
	
}
