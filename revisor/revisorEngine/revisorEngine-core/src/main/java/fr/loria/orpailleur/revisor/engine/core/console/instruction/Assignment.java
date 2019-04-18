package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Identifier;
import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexFormatable;

/**
 * @author William Philbert
 */
public abstract class Assignment<C extends RevisorConsole<C, ?, ?, ?>, I extends Identifier<C, V>, E extends Expression<C, V>, V extends Object> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected I left;
	protected E right;
	protected V result;
	
	// Constructors :
	
	protected Assignment(final C console, final String inputText, final I left, final E right) {
		super(console, inputText);
		this.left = left;
		this.right = right;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		this.right.validate(this.console, this.newVars);
		this.left.validateLeft(this.console, this.newVars);
		
		this.addWarningMessages(this.left);
		this.addWarningMessages(this.right);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.registerVariables(this.newVars);
		this.result = this.right.getOptimizedValue(this.console);
		this.sideNotes.addAll(this.right.getSideNotes());
		this.registerMacro(this.left.getName(), this.result);
	}
	
	@Override
	protected void clearTmpResources() {
		this.result = null;
	}
	
	protected abstract void registerMacro(final String name, final V value) throws InstructionExecutionException;
	
	protected String operator() {
		return ":=";
	}
	
	@Override
	protected String createFormatedInputText() {
		return String.format("%s %s %s", this.left, this.operator(), this.right);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		String left = this.left.toString(latex);
		String right = (this.result instanceof LatexFormatable) ? ((LatexFormatable) this.result).toString(latex) : this.result.toString();
		return String.format("%s = %s", left, right);
	}
	
}
