package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_SetDefaultRuleCost<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String RULE_COSTS_MUST_BE_STRICTLY_POSITIVE = "Rule costs must be strictly positive.";
	private static final String DEFAULT_RULE_COST_SET = "Default rule cost set to %s.";
	
	// Fields :
	
	protected final double value;
	
	// Constructors :
	
	public PLAK_SetDefaultRuleCost(final C console, final String inputText, final double value) {
		super(console, inputText);
		this.value = value;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		if(this.value <= 0) {
			throw new FormulaValidationException(RULE_COSTS_MUST_BE_STRICTLY_POSITIVE);
		}
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.setDefaultRuleCost(this.value);
	}
	
	@Override
	public String createFormatedInputText() {
		String[] parts = this.inputText.split(":=");
		return String.format("%s := %s", this.simplifiedString(parts[0]), this.format(this.value));
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return "";
	}
	
	@Override
	protected String createOutputText() {
		return String.format(DEFAULT_RULE_COST_SET, this.format(this.value));
	}
	
}
