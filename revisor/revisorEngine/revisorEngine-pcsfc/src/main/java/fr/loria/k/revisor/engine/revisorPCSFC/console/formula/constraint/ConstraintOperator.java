package fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;

public abstract class ConstraintOperator {

	private static final String UNRECOGNIZED_OPERATOR = "Unrecognized constraint operator.";
	
	public ConstraintOperator() {
	}
	
	public void validate() throws FormulaValidationException {
		if (!(this instanceof ConstraintOperatorLess || this instanceof ConstraintOperatorLessEquals || this instanceof ConstraintOperatorEquals ||this instanceof ConstraintOperatorNotEquals || this instanceof ConstraintOperatorMore || this instanceof ConstraintOperatorMoreEquals)) {
			throw new FormulaValidationException(UNRECOGNIZED_OPERATOR);
		}
	}
	
	public abstract String toString();

}
