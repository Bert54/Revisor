package fr.loria.orpailleur.revisor.engine.revisorPL.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Formula;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Assignment;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Identifier;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * @author William Philbert
 */
public class PL_Assignment<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends Assignment<C, PL_Identifier<C>, Formula<C, PLFormula>, PLFormula> {
	
	// Constructors :
	
	public PL_Assignment(final C console, final String inputText, final PL_Identifier<C> left, final Formula<C, PLFormula> right) {
		super(console, inputText, left, right);
	}
	
	// Methods :
	
	@Override
	protected void registerMacro(final String name, final PLFormula value) throws InstructionExecutionException {
		this.console.getFormulae().addMacro(name, value);
	}
	
}
