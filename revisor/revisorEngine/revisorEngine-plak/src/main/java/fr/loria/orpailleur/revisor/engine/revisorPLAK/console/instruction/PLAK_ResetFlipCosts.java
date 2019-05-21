package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_ResetFlipCosts<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String FLIPCOSTS_RESET = "Flip costs reset.";
	
	// Constructors :
	
	public PLAK_ResetFlipCosts(final C console, final String inputText) {
		super(console, inputText);
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		// Nothing to check.
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.resetFlipCosts();
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
		return FLIPCOSTS_RESET;
	}
	
}
