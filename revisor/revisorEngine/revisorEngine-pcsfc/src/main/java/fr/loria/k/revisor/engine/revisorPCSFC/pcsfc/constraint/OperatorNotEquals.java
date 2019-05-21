package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;

public class OperatorNotEquals extends Operator {

	public OperatorNotEquals() {
	}

	@Override
	public String toString(final boolean latex) {
		if (latex) {
			return "{\\neq}";
		}
		return "!=";
	}

}
