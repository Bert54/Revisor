package fr.loria.orpailleur.revisor.engine.revisorPL.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Identifier;

/**
 * @author William Philbert
 */
public class PL_SetWeight<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String WEIGHTS_MUST_BE_STRICTLY_POSITIVE = "Variable weights must be strictly positive.";
	
	// Fields :
	
	protected final PL_Identifier<C> identifier;
	protected final double value;
	
	// Constructors :
	
	public PL_SetWeight(final C console, final String inputText, final PL_Identifier<C> identifier, final double value) {
		super(console, inputText);
		this.identifier = identifier;
		this.value = value;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		if(this.value <= 0) {
			throw new FormulaValidationException(WEIGHTS_MUST_BE_STRICTLY_POSITIVE);
		}
		
		this.identifier.validateVariableOnly(this.console, this.newVars);
		
		this.addWarningMessages(this.identifier);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.registerVariables(this.newVars);
		this.console.setWeight(this.identifier.getName(), this.value);
	}
	
	@Override
	public String createFormatedInputText() {
		return String.format("%s.weight := %s", this.identifier, this.format(this.value));
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return String.format("%s.weight = %s", this.identifier.toString(latex), this.format(this.value));
	}
	
}
