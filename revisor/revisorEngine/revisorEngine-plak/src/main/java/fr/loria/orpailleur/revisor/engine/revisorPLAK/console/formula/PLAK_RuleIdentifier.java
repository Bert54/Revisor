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
public class PLAK_RuleIdentifier<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends Identifier<C, PLAK_Rule<C>> {
	
	// Constants :
	
	private static final String UNDEFINED_RULE_MACRO = "Undefine rule macro : '%s'.";
	
	// Constructors :
	
	public PLAK_RuleIdentifier(final String name) {
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
			throw new FormulaValidationException(String.format(UNDEFINED_RULE_MACRO, this.name));
		}
	}
	
	@Override
	public PLAK_Rule<C> getValue(final C console) {
		return console.getRules().getValue(this.name);
	}
	
	@Override
	public boolean isRightTypeMacro(final C console) {
		return console.getRules().isMacro(this.name);
	}
	
	@Override
	public String formatNameToLatex(final String name) {
		return RevisorPL.formatNameToLatex(name);
	}
	
}
