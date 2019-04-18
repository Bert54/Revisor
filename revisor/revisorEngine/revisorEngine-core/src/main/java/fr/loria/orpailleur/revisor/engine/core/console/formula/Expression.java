package fr.loria.orpailleur.revisor.engine.core.console.formula;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.sidenote.SideNote;
import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexFormatable;

/**
 * @author William Philbert
 */
public interface Expression<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> extends LatexFormatable {
	
	/**
	 * Returns a collection containing the direct children of this expresion.
	 * @return a collection containing the direct children of this expresion.
	 */
	public Collection<Expression<C, ?>> getChildren();
	
	/**
	 * Checks that this expression is valid in the given console environment.
	 * @param console - the console environment in which the expression must be evaluated.
	 * @param newVars - the list of the variables that are used in this expression, but aren't registered in the environment yet. This list is filled during the validation process.
	 * @throws FormulaValidationException if the expression isn't valid.
	 */
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException;
	
	/**
	 * Returns the value of the expression in the given console environment.
	 * @param console - the console environment in which the expression must be evaluated.
	 * @return the value of the expression in the given console environment.
	 */
	public V getValue(final C console);
	
	/**
	 * Returns the simplified/optimized value of the expression in the given console environment.
	 * @param console - the console environment in which the expression must be evaluated.
	 * @return the simplified/optimized value of the expression in the given console environment.
	 */
	public V getOptimizedValue(final C console);
	
	/**
	 * Returns the warning messages for this node of this expression. Can be empty.
	 * @return the warning messages for this node of this expression.
	 */
	public List<String> getNodeWarningMessages();
	
	/**
	 * Returns the warning messages for all nodes of this expression. Can be empty.
	 * @return the warning messages for all nodes of this expression.
	 */
	public List<String> getWarningMessages();
	
	/**
	 * Indicates whether this node of this expression has warning messages.
	 * @return true if this node of this expression has warning messages, else false.
	 */
	public boolean hasNodeWarningMessages();
	
	/**
	 * Indicates whether this expression has warning messages.
	 * @return true if this expression has warning messages, else false.
	 */
	public boolean hasWarningMessages();
	
	/**
	 * Appends a warning message to the messages to display.
	 * @param message - the warning message to add.
	 */
	public void addWarningMessage(final String message);
	
	/**
	 * Returns the side note of the main node of this expression if there is one, else null.
	 * @return a SideNote or null.
	 */
	public SideNote<C> getSideNote();
	
	/**
	 * Returns the side notes of all the nodes of this expression.
	 * @return a collection of SideNote.
	 */
	public Collection<SideNote<C>> getSideNotes();
	
}
