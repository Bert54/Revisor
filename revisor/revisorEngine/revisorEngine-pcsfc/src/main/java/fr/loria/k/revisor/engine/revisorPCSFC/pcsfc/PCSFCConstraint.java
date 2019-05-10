package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

import fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.ConstraintOperator;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint.*;

public class PCSFCConstraint extends PCSFCFormula {

	private static final String LESS_EQUALS_OPERATOR = "<=";
	private static final String LESS_OPERATOR = "<";
	private static final String EQUALS_OPERATOR = "==";
	private static final String NOT_EQUALS_OPERATOR = "!=";
	private static final String MORE_EQUALS_OPERATOR = ">=";
	private static final String MORE_OPERATOR = ">";
	
	private LeftMember left;
	private Operator operator;
	private RightMember<?> right;
	
	public PCSFCConstraint(fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMember lm, ConstraintOperator co, fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.RightMember<Double> right) {
		if (lm.isTerminal()) {
			this.left = new LeftMemberElementTerminal((fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElementTerminal) lm);
		}
		else {
			this.left = new LeftMemberElement((fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElement) lm);
		}
		switch (co.toString()) {
			case LESS_EQUALS_OPERATOR:
				this.operator = new OperatorLessEquals();
				break;
			case LESS_OPERATOR:
				this.operator = new OperatorLess();
				break;
			case EQUALS_OPERATOR:
				this.operator = new OperatorEquals();
				break;
			case NOT_EQUALS_OPERATOR:
				this.operator = new OperatorNotEquals();
				break;
			case MORE_EQUALS_OPERATOR:
				this.operator = new OperatorMoreEquals();
				break;
			case MORE_OPERATOR:
				this.operator = new OperatorMore();
				break;
			default:
				this.operator = new OperatorMore();
				break;
				
		}
		this.right = new RightMember<Double>(right);
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
