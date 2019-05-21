package fr.loria.orpailleur.revisor.engine.revisorPLAK.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Identifier;
import fr.loria.orpailleur.revisor.engine.revisorPL.RevisorPL;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.console.AbstractRevisorConsolePLAK;

/**
 * @author William Philbert
 */
public class PLAK_RuleSetIdentifier<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends Identifier<C, PLAK_RuleSet<C>> {
	
	// Constants :
	
	private static final String UNDEFINED_RULESET_MACRO = "Undefine rule set macro : '%s'.";
	
	// Constructors :
	
	public PLAK_RuleSetIdentifier(final String name) {
		super(name);
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		return new LinkedList<>();
	}
	
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		if(!this.isRightTypeMacro(console)) {
			throw new FormulaValidationException(String.format(UNDEFINED_RULESET_MACRO, this.name));
		}
	}
	
	@Override
	public PLAK_RuleSet<C> getValue(final C console) {
		return console.getRuleSets().getValue(this.name);
	}
	
	@Override
	public boolean isRightTypeMacro(final C console) {
		return console.getRuleSets().isMacro(this.name);
	}
	
	@Override
	public String formatNameToLatex(final String name) {
		return RevisorPL.formatNameToLatex(name);
	}
	
}
