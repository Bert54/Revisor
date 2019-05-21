package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Literal;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_SetFlipCost<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String FLIP_COSTS_MUST_BE_STRICTLY_POSITIVE = "Literal flip costs must be strictly positive.";
	
	// Fields :
	
	protected final PL_Literal<C> literal;
	protected final double value;
	
	// Constructors :
	
	public PLAK_SetFlipCost(C console, String inputText, PL_Literal<C> literal, double value) {
		super(console, inputText);
		this.literal = literal;
		this.value = value;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		if(this.value <= 0) {
			throw new FormulaValidationException(FLIP_COSTS_MUST_BE_STRICTLY_POSITIVE);
		}
		
		this.literal.validate(this.console, this.newVars);
		
		this.addWarningMessages(this.literal);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.registerVariables(this.newVars);
		this.console.setFlipCost(this.literal.toString(), this.value);
	}
	
	@Override
	public String createFormatedInputText() {
		return String.format("%s.flipcost := %s", this.literal, this.format(this.value));
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return String.format("%s.flipcost = %s", this.literal.toString(latex), this.format(this.value));
	}
	
}
