package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Assignment;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_Rule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleIdentifier;

/**
 * @author William Philbert
 */
public class PLAK_RuleAssignment<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends Assignment<C, PLAK_RuleIdentifier<C>, Expression<C, PLAK_Rule<C>>, PLAK_Rule<C>> {
	
	// Constructors :
	
	public PLAK_RuleAssignment(final C console, final String inputText, final PLAK_RuleIdentifier<C> left, final Expression<C, PLAK_Rule<C>> right) {
		super(console, inputText, left, right);
	}
	
	// Methods :
	
	@Override
	protected void registerMacro(final String name, final PLAK_Rule<C> value) throws InstructionExecutionException {
		this.console.getRules().addMacro(name, value);
	}
	
}
