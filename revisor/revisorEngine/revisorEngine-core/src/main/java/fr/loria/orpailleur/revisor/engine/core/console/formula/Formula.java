package fr.loria.orpailleur.revisor.engine.core.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;

/**
 * @author William Philbert
 */
public interface Formula<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> extends Expression<C, V> {
	
	/**
	 * Indicates if this formula is unary or nary.
	 * Used to decide where to put parentheses in formula toString(), among other things.
	 * @return true if this formula is unary, false if it is nary.
	 */
	public boolean isUnary();
	
}
