package fr.loria.orpailleur.revisor.engine.core.console.formula;

import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.console.RevisorConsole;
import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;

/**
 * @author William Philbert
 */
public abstract class Identifier<C extends RevisorConsole<C, ?, ?, ?>, V extends Object> extends AbstractFormula<C, V> implements Comparable<Identifier<?, ?>> {
	
	// Constants :
	
	private static final String CANT_CREATE_MACRO = "Can't create macro '%s'";
	private static final String CANT_CREATE_MACRO_WITH_ALREADY_USED_NAME = CANT_CREATE_MACRO + " : name already used by a variable or an other type of macro.";
	private static final String CANT_CREATE_MACRO_WITH_RECUSIVE_DEFINITION = CANT_CREATE_MACRO + " : recursive macro declaration.";
	
	// Fields :
	
	protected final String name;
	
	// Constructors :
	
	public Identifier(final String name) {
		this.name = name;
	}
	
	// Getters :
	
	public String getName() {
		return this.name;
	}
	
	// Methods :
	
	public void validateLeft(final C console, final Set<String> newVars) throws FormulaValidationException {
		if((console.isUsedName(this.name) && !this.isRightTypeMacro(console))) {
			throw new FormulaValidationException(String.format(CANT_CREATE_MACRO_WITH_ALREADY_USED_NAME, this.name));
		}
		else if(newVars.contains(this.name)) {
			throw new FormulaValidationException(String.format(CANT_CREATE_MACRO_WITH_RECUSIVE_DEFINITION, this.name));
		}
	}
	
	public abstract boolean isRightTypeMacro(C console);
	
	@Override
	public boolean isUnary() {
		return true;
	}
	
	@Override
	public String toString(final boolean latex) {
		return latex ? this.formatNameToLatex(this.name) : this.name;
	}
	
	public abstract String formatNameToLatex(String name);
	
	@Override
	public boolean equals(final Object object) {
		if(object instanceof Identifier<?, ?>) {
			return this.name.equals(((Identifier<?, ?>) object).name);
		}
		
		return false;
	}
	
	@Override
	public int compareTo(final Identifier<?, ?> other) {
		if(other != null) {
			return this.name.compareTo(other.name);
		}
		
		return -1;
	}
	
}
