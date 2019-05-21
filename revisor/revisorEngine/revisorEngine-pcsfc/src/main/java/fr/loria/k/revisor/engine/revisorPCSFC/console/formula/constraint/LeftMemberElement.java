package fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint;

import fr.loria.orpailleur.revisor.engine.core.console.exception.FormulaValidationException;

public class LeftMemberElement extends LeftMember {

	protected static final String UNRECOGNIZED_ARITHMETIC_OPERATOR = "Unrecognized arithmetic operator in left member of constraint.";

	private LeftMember left;
	private LeftMember right;
	private LeftMemberArithmeticOperator operator;
	
	public LeftMemberElement(LeftMember l, LeftMemberArithmeticOperator oper, LeftMember r) {
		this.left = l;
		this.operator = oper;
		this.right = r;
	}
	
	public LeftMember getLeftTerm() {
		return this.left;
	}
	
	public LeftMemberArithmeticOperator getBinaryArithmeticOperator() {
		return this.operator;
	}
	
	public LeftMember getRightTerm() {
		return this.right;
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

	@Override
	public void validate() throws FormulaValidationException {
		if (!(this.operator instanceof LeftMemberArithmeticOperatorAdd || this.operator instanceof LeftMemberArithmeticOperatorSub)) {
			throw new FormulaValidationException(UNRECOGNIZED_ARITHMETIC_OPERATOR);
		}
		this.left.validate();
		this.right.validate();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.left.toString());
		str.append(" " + this.operator.toString() + " ");
		str.append(this.right.toString());
		return str.toString();
	}
	
}
