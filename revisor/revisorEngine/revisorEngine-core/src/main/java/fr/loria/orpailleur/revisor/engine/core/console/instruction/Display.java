package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexFormatable;

/**
 * @author William Philbert
 */
public class Display<C extends RevisorConsole<C, ?, ?, ?>, E extends Expression<C, V>, V extends Object> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected final E expression;
	protected V result;
	
	// Constructors :
	
	public Display(final C console, final String inputText, final E expression) {
		super(console, inputText);
		this.expression = expression;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		this.expression.validate(this.console, this.newVars);
		
		this.addWarningMessages(this.expression);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.result = this.expression.getOptimizedValue(this.console);
		this.sideNotes.addAll(this.expression.getSideNotes());
	}
	
	@Override
	protected void clearTmpResources() {
		this.result = null;
	}
	
	@Override
	public String createFormatedInputText() {
		return this.expression.toString();
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return (this.result instanceof LatexFormatable) ? ((LatexFormatable) this.result).toString(latex) : this.result.toString();
	}
	
}
