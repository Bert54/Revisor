package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_Rule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSetIdentifier;

/**
 * @author William Philbert
 */
public class PLAK_AddRule<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends PLAK_EditRuleSet<C> {
	
	// Constructors :
	
	public PLAK_AddRule(C console, String inputText, PLAK_RuleSetIdentifier<C> identifier, Expression<C, PLAK_Rule<C>> rule) {
		super(console, inputText, identifier, rule);
	}
	
	// Methods :
	
	@Override
	protected void editRuleSet(PLAK_RuleSet<C> ruleSet, Expression<C, PLAK_Rule<C>> rule) throws InstructionExecutionException {
		ruleSet.addRule(rule);
	}
	
	@Override
	protected String operator() {
		return "+=";
	}
	
}
