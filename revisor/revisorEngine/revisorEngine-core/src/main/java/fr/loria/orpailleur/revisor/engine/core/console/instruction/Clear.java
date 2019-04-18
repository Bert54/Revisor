package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;

/**
 * @author William Philbert
 */
public class Clear<C extends RevisorConsole<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String ENVIRONMENT_RESET = "Environment reset.";
	
	// Constructors :
	
	public Clear(final C console, final String inputText) {
		super(console, inputText);
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		// Nothing to check.
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.clear();
		Instruction<C> instruction = this;
		
		while(instruction != null) {
			instruction.setVisible(true);
			instruction = instruction.getParent();
		}
	}
	
	@Override
	protected String createFormatedInputText() {
		return this.simplifiedString(this.inputText);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return "";
	}
	
	@Override
	protected String createOutputText() {
		return ENVIRONMENT_RESET;
	}
	
}
