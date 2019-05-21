package fr.loria.orpailleur.revisor.engine.revisorPL.console.sidenote;

import fr.loria.orpailleur.revisor.engine.core.console.sidenote.SideNote;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.output.Substitution;

/**
 * @author William Philbert
 */
public class SubstitutionSideNote<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends SideNote<C> {
	
	// Constants :
	
	private static final String COULNDT_COMPUTE_SUBSTITUTION = "Couldn't compute substitution: ";
	
	// Fields :
	
	private final Substitution substitution;
	
	// Constructors :
	
	public SubstitutionSideNote(final Substitution substitution) {
		this.substitution = substitution;
	}
	
	public SubstitutionSideNote(final C console, final PLFormula dk, final PLFormula source, final PLFormula target, final PLFormula result) {
		Substitution substitution;
		
		try {
			substitution = console.substitution(dk, source, target, result);
		}
		catch(Exception argh) {
			substitution = null;
			this.failWith(COULNDT_COMPUTE_SUBSTITUTION + argh.getMessage());
			console.getLogger().logWarning(argh);
		}
		
		this.substitution = substitution;
	}
	
	public SubstitutionSideNote(final C console, final PLFormula psi, final PLFormula mu, final PLFormula result) {
		Substitution substitution;
		
		try {
			substitution = console.substitution(psi, mu, result);
		}
		catch(Exception argh) {
			substitution = null;
			this.failWith(COULNDT_COMPUTE_SUBSTITUTION + argh.getMessage());
			console.getLogger().logWarning(argh);
		}
		
		this.substitution = substitution;
	}
	
	// Methods :
	
	@Override
	public boolean isDisplayed(final C console) {
		return console.getConfig().displaySubstitutions.getValue();
	}
	
	@Override
	public String initText(final boolean latex) {
		return "Substitution:" + StringUtils.symbol(" ", latex) + this.substitution.toString(latex);
	}
	
}
