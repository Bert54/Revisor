package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleIdentifier;

/**
 * @author William Philbert
 */
public class PLAK_GetRuleCost<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected final PLAK_RuleIdentifier<C> identifier;
	protected double result;
	
	// Constructors :
	
	public PLAK_GetRuleCost(final C console, final String inputText, final PLAK_RuleIdentifier<C> identifier) {
		super(console, inputText);
		this.identifier = identifier;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		this.identifier.validate(this.console, this.newVars);
		
		this.addWarningMessages(this.identifier);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.result = this.console.getRuleCost(this.identifier.toString());
	}
	
	@Override
	public String createFormatedInputText() {
		return String.format("rulecost(%s)", this.identifier);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return this.format(this.result);
	}
	
}
