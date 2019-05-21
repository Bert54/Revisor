package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.AbstractInstruction;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_Rule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSetIdentifier;

/**
 * @author William Philbert
 */
public abstract class PLAK_EditRuleSet<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends AbstractInstruction<C> {
	
	// Fields :
	
	protected final PLAK_RuleSetIdentifier<C> identifier;
	protected final Expression<C, PLAK_Rule<C>> rule;
	protected PLAK_RuleSet<C> result;
	
	// Constructors :
	
	protected PLAK_EditRuleSet(C console, String inputText, PLAK_RuleSetIdentifier<C> identifier, Expression<C, PLAK_Rule<C>> rule) {
		super(console, inputText);
		this.identifier = identifier;
		this.rule = rule;
	}
	
	// Methods :
	
	@Override
	protected void doValidate() throws InstructionValidationException {
		this.identifier.validate(this.console, this.newVars);
		this.rule.validate(this.console, this.newVars);
		
		this.addWarningMessages(this.identifier);
		this.addWarningMessages(this.rule);
	}
	
	@Override
	protected void doExecute() throws InstructionExecutionException {
		this.console.registerVariables(this.newVars);
		PLAK_RuleSet<C> ruleSet = this.console.getRuleSets().getValue(this.identifier.getName());
		this.editRuleSet(ruleSet, this.rule);
		this.result = this.getConsole().getRuleSets().getValue(this.identifier.getName());
	}
	
	@Override
	protected void clearTmpResources() {
		this.result = null;
	}
	
	protected abstract void editRuleSet(PLAK_RuleSet<C> ruleSet, Expression<C, PLAK_Rule<C>> rule) throws InstructionExecutionException;
	
	protected abstract String operator();
	
	@Override
	public String createFormatedInputText() {
		return String.format("%s %s %s", this.identifier, this.operator(), this.rule);
	}
	
	@Override
	protected String createOutput(boolean latex) {
		return String.format("%s = %s", this.identifier.toString(latex), this.result.toString(latex));
	}
	
}
