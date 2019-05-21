package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Assignment;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSetIdentifier;

/**
 * @author William Philbert
 */
public class PLAK_RuleSetAssignment<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends Assignment<C, PLAK_RuleSetIdentifier<C>, Expression<C, PLAK_RuleSet<C>>, PLAK_RuleSet<C>> {
	
	// Constructors :
	
	public PLAK_RuleSetAssignment(final C console, final String inputText, final PLAK_RuleSetIdentifier<C> left, final Expression<C, PLAK_RuleSet<C>> right) {
		super(console, inputText, left, right);
	}
	
	// Methods :
	
	@Override
	protected void registerMacro(final String name, final PLAK_RuleSet<C> value) throws InstructionExecutionException {
		this.console.getRuleSets().addMacro(name, value);
	}
	
}
