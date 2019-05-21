package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;

public class OperatorLessEquals extends Operator {

	public OperatorLessEquals() {
	}

	@Override
	public String toString(final boolean latex) {
		if (latex) {
			return "\\:{\\leq}\\:";
		}
		return "<=";
	}

}
