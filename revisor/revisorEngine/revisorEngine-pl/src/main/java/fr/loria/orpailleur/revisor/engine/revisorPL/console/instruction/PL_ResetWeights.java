package fr.loria.orpailleur.revisor.engine.revisorPL.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;

/**
 * @author William Philbert
 */
public class PL_ResetWeights<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String WEIGHT_RESET = "Weights reset.";
	
	// Constructors :
	
	public PL_ResetWeights(final C console, final String inputText) {
		super(console, inputText);
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		// Nothing to check.
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.resetWeights();
	}
	
	@Override
	public String createFormatedInputText() {
		return this.simplifiedString(this.inputText);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return "";
	}
	
	@Override
	protected String createOutputText() {
		return WEIGHT_RESET;
	}
	
}
