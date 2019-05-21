package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;

public class OperatorMoreEquals extends Operator {

	public OperatorMoreEquals() {
	}

	@Override
	public String toString(final boolean latex) {
		if (latex) {
			return "\\:{\\geq}\\:";
		}
		return ">=";
	}

}
