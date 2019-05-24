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
	
	@SuppressWarnings("rawtypes")
	public PCSFCConstraint(fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMember lm, ConstraintOperator co, fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.RightMember<Double> right) {
		if (lm.isTerminal()) {
			this.left = new LeftMemberElementTerminal<Object>((fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElementTerminal) lm);
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
		}
		this.right = new RightMember<Double>(right);
	}

	/**
	 * Constructor that copies an existing constraint
	 * @param lm left member of the constraint
	 * @param co operator of the constraint
	 * @param rm right member of the constraint
	 */
	@SuppressWarnings("unchecked")
	public PCSFCConstraint(LeftMember lm, Operator co, RightMember<?> rm) {
		if (lm.isTerminal()) {
			this.left = new LeftMemberElementTerminal<Float>((LeftMemberElementTerminal<Float>)lm);
		}
		else {
			this.left = new LeftMemberElement((LeftMemberElement)lm);
		}
		switch (co.toString(false)) {
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
		}
		this.right = new RightMember<Double>(rm);
	}

	@Override
	public String toString(boolean latex) {
		StringBuilder str = new StringBuilder();
		str.append(this.left.toString());
		str.append(" " + this.operator.toString(latex) + " ");
		str.append(this.right.toString());
		return str.toString();
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCConstraint(this.left, this.operator, this.right);
	}
	
	@Override
	public PCSFCFormula toPCSFC() {
		if (this.left.isTerminal() && ((double)((LeftMemberElementTerminal<?>) this.left).getCoefficient()) % 1 == 0 && (int)((double)((LeftMemberElementTerminal<?>) this.left).getCoefficient()) == 1 && this.operator.toString(false).equals(">=") && (double)this.right.getNumber() % 1 == 0 && (int)(double)this.right.getNumber() == PCSFCBoolean.DELIMITER) {
			return new PCSFCBoolean(((LeftMemberElementTerminal<?>) this.left).getVariable().toString().replaceAll("integer_encoding_", ""));
		}
		return new PCSFCConstraint(this.left, this.operator, this.right);
	}

	@Override
	public boolean canRevise() {
		return true;
	}

}
