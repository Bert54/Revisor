package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.instruction;

import fr.loria.orpailleur.revisor.engine.core.console.exception.InstructionExecutionException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Identifier;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Assignment;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.formula.PL_Identifier;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_Rule;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleIdentifier;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula.PLAK_RuleSetIdentifier;

/**
 * @author William Philbert
 */
public class PLAK_Assignment<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>, I extends Identifier<C, V>, V extends Object> extends Assignment<C, I, I, V> {
	
	// Constants :
	
	private static final String IDENTIFIER_TYPE_ERROR = "There was a problem while checking identifiers types.";
	
	// Constructors :
	
	@SuppressWarnings("unchecked")
	public PLAK_Assignment(final C console, final String inputText, final I left, final I right) {
		super(console, inputText, left, right);
		
		String leftVar = this.left.getName();
		String rightVar = this.right.getName();
		
		if(this.console.getRules().isMacro(rightVar)) {
			this.left = (I) new PLAK_RuleIdentifier<C>(leftVar);
			this.right = (I) new PLAK_RuleIdentifier<C>(rightVar);
		}
		else if(this.console.getRuleSets().isMacro(rightVar)) {
			this.left = (I) new PLAK_RuleSetIdentifier<C>(leftVar);
			this.right = (I) new PLAK_RuleSetIdentifier<C>(rightVar);
		}
		else {
			this.left = (I) new PL_Identifier<C>(leftVar);
			this.right = (I) new PL_Identifier<C>(rightVar);
		}
	}
	
	// Methods :
	
	@Override
	@SuppressWarnings("unchecked")
	protected void registerMacro(final String name, final V value) throws InstructionExecutionException {
		if(this.left instanceof PLAK_RuleIdentifier && value instanceof PLAK_Rule) {
			this.console.getRules().addMacro(name, ((PLAK_Rule<C>) value).copy());
		}
		else if(this.left instanceof PLAK_RuleSetIdentifier && value instanceof PLAK_RuleSet) {
			this.console.getRuleSets().addMacro(name, ((PLAK_RuleSet<C>) value).copy());
		}
		else if(this.left instanceof PL_Identifier && value instanceof PLFormula) {
			this.console.getFormulae().addMacro(name, (PLFormula) value);
		}
		else {
			throw new InstructionExecutionException(IDENTIFIER_TYPE_ERROR);
		}
	}
	
}
