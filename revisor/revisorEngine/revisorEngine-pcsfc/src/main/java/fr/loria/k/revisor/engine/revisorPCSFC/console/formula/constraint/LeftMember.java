package fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;

public abstract class LeftMember {

	public LeftMember() {
	}
	
	public abstract void validate() throws FormulaValidationException;
	
	public abstract boolean isTerminal();

}
