package fr.loria.orpailleur.revisor.engine.core.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;

/**
 * @author William Philbert
 */
public class InvalidInstruction<C extends RevisorConsole<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String COULDNT_PARSE_COMMAND = "The command couldn't be parsed.";
	
	// Constructors :
	
	public InvalidInstruction(final C console, final String inputText, final String errorMessage) {
		super(console, inputText);
		this.addErrorMessage(errorMessage);
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		throw new InstructionValidationException(COULDNT_PARSE_COMMAND);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		// Nothing to do, this should never been called.
	}
	
	@Override
	public String createFormatedInputText() {
		return this.inputText;
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return "";
	}
	
}
