package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_SetUseDefaultRuleSet<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Constants :
	
	private static final String NOW_USING_X_AS_DEFAULT_RULE_SET = "Now using %s as the default rule set.";
	private static final String ALL_DEFINED_RULES = "all defined rules";
	private static final String EMPTY_RULE_SET = "an empty set";
	
	// Fields :
	
	protected final boolean value;
	
	// Constructors :
	
	public PLAK_SetUseDefaultRuleSet(final C console, final String inputText, boolean value) {
		super(console, inputText);
		this.value = value;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		// Nothing to check.
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.setUseDefaultRuleSet(this.value);
	}
	
	@Override
	public String createFormatedInputText() {
		String[] parts = this.inputText.split(":=");
		return String.format("%s := %b", this.simplifiedString(parts[0]), this.value);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return "";
	}
	
	@Override
	protected String createOutputText() {
		return String.format(NOW_USING_X_AS_DEFAULT_RULE_SET, this.value ? ALL_DEFINED_RULES : EMPTY_RULE_SET);
	}
	
}
