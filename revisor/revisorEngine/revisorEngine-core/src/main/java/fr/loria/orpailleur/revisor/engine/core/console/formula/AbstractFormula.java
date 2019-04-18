package fr.loria.orpailleur.revisor.engine.core.console.formula;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;

/**
 * @author William Philbert
 */
public abstract class AbstractFormula<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> extends AbstractExpression<C, V> implements Formula<C, V> {
	
	// Constructors :
	
	protected AbstractFormula() {
		super();
	}
	
}
