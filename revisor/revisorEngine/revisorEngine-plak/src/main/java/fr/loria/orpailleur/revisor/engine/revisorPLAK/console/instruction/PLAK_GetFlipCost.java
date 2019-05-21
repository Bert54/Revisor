package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Literal;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_GetFlipCost<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected final PL_Literal<C> literal;
	protected double result;
	
	// Constructors :
	
	public PLAK_GetFlipCost(final C console, final String inputText, final PL_Literal<C> literal) {
		super(console, inputText);
		this.literal = literal;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		this.literal.validate(this.console, this.newVars);
		
		this.addWarningMessages(this.literal);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.result = this.console.getFlipCost(this.literal.toString());
	}
	
	@Override
	public String createFormatedInputText() {
		return String.format("flipcost(%s)", this.literal);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return this.format(this.result);
	}
	
}
