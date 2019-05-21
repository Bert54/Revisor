package fr.loria.orpailleur.revisor.engine.revisorPL.console.formula;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Expression;
import fr.loria.orpailleur.revisor.engine.core.console.formula.Identifier;
import fr.loria.orpailleur.revisor.engine.revisorPL.RevisorPL;
import fr.loria.orpailleur.revisor.engine.revisorPL.console.AbstractRevisorConsolePL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * @author William Philbert
 */
public class PL_Identifier<C extends AbstractRevisorConsolePL<C, ?, ?, ?>> extends Identifier<C, PLFormula> {
	
	// Constants :
	
	private static final String WRONG_TYPE_MACRO = "The macro '%s' can't be used here : wrong type.";
	private static final String CANT_USE_MACRO_HERE = "The macro '%s' can't be used here : only propositional variables are accepted.";
	
	// Constructors :
	
	public PL_Identifier(final String name) {
		super(name);
	}
	
	// Methods :
	
	@Override
	public Collection<Expression<C, ?>> getChildren() {
		return new LinkedList<>();
	}
	
	@Override
	public void validate(final C console, final Set<String> newVars) throws FormulaValidationException {
		if(console.isMacro(this.name)) {
			if(!isRightTypeMacro(console)) {
				throw new FormulaValidationException(String.format(WRONG_TYPE_MACRO, this.name));
			}
		}
		else if(!(console.isVariable(this.name) || newVars.contains(this.name))) {
			newVars.add(this.name);
		}
	}
	
	public void validateVariableOnly(final C console, final Set<String> newVars) throws FormulaValidationException {
		if(console.isMacro(this.name)) {
			throw new FormulaValidationException(String.format(CANT_USE_MACRO_HERE, this.name));
		}
		else if(!(console.isVariable(this.name) || newVars.contains(this.name))) {
			newVars.add(this.name);
		}
	}
	
	@Override
	public PLFormula getValue(final C console) {
		if(console.getFormulae().isMacro(this.name)) {
			return console.getFormulae().getValue(this.name);
		}
		else {
			return console.getPl().LITT(this.name);
		}
	}
	
	@Override
	public boolean isRightTypeMacro(final C console) {
		return console.getFormulae().isMacro(this.name);
	}
	
	@Override
	public String formatNameToLatex(final String name) {
		return RevisorPL.formatNameToLatex(name);
	}
	
}
