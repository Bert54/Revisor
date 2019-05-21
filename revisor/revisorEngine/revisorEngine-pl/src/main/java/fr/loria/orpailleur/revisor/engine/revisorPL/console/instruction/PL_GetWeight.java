package fr.loria.orpailleur.revisor.engine.revisorPL.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Identifier;

/**
 * @author William Philbert
 */
public class PL_GetWeight<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected final PL_Identifier<C> identifier;
	protected double result;
	
	// Constructors :
	
	public PL_GetWeight(final C console, final String inputText, final PL_Identifier<C> identifier) {
		super(console, inputText);
		this.identifier = identifier;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		this.identifier.validateVariableOnly(this.console, this.newVars);
		
		this.addWarningMessages(this.identifier);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.result = this.console.getWeight(this.identifier.getName());
	}
	
	@Override
	public String createFormatedInputText() {
		return String.format("weight(%s)", this.identifier);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return this.format(this.result);
	}
	
}
