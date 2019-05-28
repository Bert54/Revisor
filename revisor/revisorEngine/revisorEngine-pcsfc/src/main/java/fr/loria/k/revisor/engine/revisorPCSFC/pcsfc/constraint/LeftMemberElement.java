package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;

public class LeftMemberElement extends LeftMember {

	private static final String ADD_OPERATOR = "+";
	private static final String SUBSTRACT_OPERATOR = "-";
	
	private LeftMember left;
	private LeftMember right;
	private LeftMemberArithmeticOperator operator;
	
	@SuppressWarnings("rawtypes")
	public LeftMemberElement(fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElement element) {
		if (element.getLeftTerm().isTerminal()) {
			this.left = new LeftMemberElementTerminal<Float>((fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElementTerminal) element.getLeftTerm());
		}
		else {
			this.left = new LeftMemberElement((fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElement) element.getLeftTerm());
		}
		switch (element.getBinaryArithmeticOperator().toString()) {
			case ADD_OPERATOR :
				this.operator = new LeftMemberArithmeticOperatorAdd();
				break;
			case SUBSTRACT_OPERATOR :
				this.operator = new LeftMemberArithmeticOperatorSub();
				break;
			default:
				this.operator = new LeftMemberArithmeticOperatorAdd();
		}
		if (element.getRightTerm().isTerminal()) {
			this.right = new LeftMemberElementTerminal<Float>((fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElementTerminal) element.getRightTerm());
		}
		else {
			this.right = new LeftMemberElement((fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.LeftMemberElement) element.getRightTerm());
		}
	}

	@SuppressWarnings("unchecked")
	public LeftMemberElement(LeftMemberElement lm) {
		if (lm.getLeftTerm().isTerminal()) {
			this.right = new LeftMemberElementTerminal<Float>((LeftMemberElementTerminal<Float>) lm.getLeftTerm());
		}
		else {
			this.right = new LeftMemberElement((LeftMemberElement) lm.getLeftTerm());
		}
		switch (lm.getBinaryArithmeticOperator().toString()) {
			case ADD_OPERATOR :
				this.operator = new LeftMemberArithmeticOperatorAdd();
				break;
			case SUBSTRACT_OPERATOR :
				this.operator = new LeftMemberArithmeticOperatorSub();
				break;
			default:
				this.operator = new LeftMemberArithmeticOperatorAdd();
		}
		if (lm.getRightTerm().isTerminal()) {
			this.left = new LeftMemberElementTerminal<Float>((LeftMemberElementTerminal<Float>) lm.getRightTerm());
		}
		else {
			this.left = new LeftMemberElement((LeftMemberElement) lm.getRightTerm());
		}
	}

	protected LeftMember getRightTerm() {
		return this.left;
	}

	protected LeftMemberArithmeticOperator getBinaryArithmeticOperator() {
		return this.operator;
	}
	
	protected LeftMember getLeftTerm() {
		return this.right;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.left.toString());
		str.append(" " + this.operator.toString() + " ");
		str.append(this.right.toString());
		return str.toString();
	}

	@Override
	public boolean isTerminal() {
		return false;
	}

}
