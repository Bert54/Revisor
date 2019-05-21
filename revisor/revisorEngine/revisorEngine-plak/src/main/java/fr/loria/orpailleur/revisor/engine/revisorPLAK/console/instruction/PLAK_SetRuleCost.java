package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleIdentifier;

/**
 * @author William Philbert
 */
public class PLAK_SetRuleCost<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String RULE_COSTS_MUST_BE_STRICTLY_POSITIVE = "Rule costs must be strictly positive.";
	
	// Fields :
	
	protected final PLAK_RuleIdentifier<C> identifier;
	protected final double value;
	
	// Constructors :
	
	public PLAK_SetRuleCost(C console, String inputText, PLAK_RuleIdentifier<C> identifier, double value) {
		super(console, inputText);
		this.identifier = identifier;
		this.value = value;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		if(this.value <= 0) {
			throw new FormulaValidationException(RULE_COSTS_MUST_BE_STRICTLY_POSITIVE);
		}
		
		this.identifier.validate(this.console, this.newVars);
		
		this.addWarningMessages(this.identifier);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.setRuleCost(this.identifier.toString(), this.value);
	}
	
	@Override
	public String createFormatedInputText() {
		return String.format("%s.rulecost := %s", this.identifier, this.format(this.value));
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return String.format("%s.rulecost = %s", this.identifier.toString(latex), this.format(this.value));
	}
	
}
