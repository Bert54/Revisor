package fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;

public class RightMember<T> {

	private static final String NOT_AN_INTEGER_OR_REAL = "Right member not an integer or real.";
	
	private T numberType;
	
	public RightMember(T type) {
		this.numberType = type;
	}

	public T getNumber() {
		return this.numberType;
	}
	
	public void validate() throws FormulaValidationException {
		if (!(numberType instanceof Integer || numberType instanceof Double)) {
			throw new FormulaValidationException(NOT_AN_INTEGER_OR_REAL);
		}
	}
	
	public String toString() {
		return this.numberType.toString();
	}
	
}
