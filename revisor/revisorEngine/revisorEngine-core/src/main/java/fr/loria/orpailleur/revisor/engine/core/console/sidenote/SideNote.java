package fr.loria.orpailleur.revisor.engine.core.console.sidenote;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexFormatable;

/**
 * @author William Philbert
 */
public abstract class SideNote<C extends RevisorConsole<C, ?, ?, ?>> implements LatexFormatable {
	
	// Fields :
	
	private boolean init = false;
	private boolean failed = false;
	private String toString = "";
	private String toLatex = "";
	private String warning = "";
	
	// Getters :
	
	public boolean isFailed() {
		return this.failed;
	}
	
	public String getWarning() {
		return this.warning;
	}
	
	// Methods :
	
	public abstract boolean isDisplayed(final C console);
	
	protected abstract String initText(final boolean latex);
	
	protected void failWith(String warning) {
		this.failed = true;
		this.warning = warning;
	}
	
	@Override
	public final String toString(final boolean latex) {
		if(!this.init) {
			if(!this.failed) {
				this.toString = this.initText(false);
				this.toLatex = this.initText(true);
			}
			
			this.init = true;
		}
		
		return latex ? this.toLatex : this.toString;
	}
	
	@Override
	public final String toString() {
		return this.toString(false);
	}
	
	@Override
	public final String toLatex() {
		return this.toString(true);
	}
	
}
