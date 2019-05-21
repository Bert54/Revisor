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
public class PLAK_Identifier<C extends AbstractRevisorConsolePLAK<C, ?, ?, ?>> extends Identifier<C, Object> {
	
	// Constructors :
	
	public PLAK_Identifier(final String name) {
		super(name);
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		return new LinkedList<>();
	}
	
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		// Nothing to check. This identifier can be anything because it is only used alone, to display its value.
	}
	
	@Override
	public Object getValue(final C console) {
		if(console.getFormulae().isMacro(this.name)) {
			return console.getFormulae().getValue(this.name);
		}
		else if(console.getRules().isMacro(this.name)) {
			return console.getRules().getValue(this.name);
		}
		else if(console.getRuleSets().isMacro(this.name)) {
			return console.getRuleSets().getValue(this.name);
		}
		else {
			return console.getPl().LITT(this.name);
		}
	}
	
	@Override
	public boolean isRightTypeMacro(final C console) {
		return console.isMacro(this.name);
	}
	
	@Override
	public String formatNameToLatex(final String name) {
		return RevisorPL.formatNameToLatex(name);
	}
	
}
