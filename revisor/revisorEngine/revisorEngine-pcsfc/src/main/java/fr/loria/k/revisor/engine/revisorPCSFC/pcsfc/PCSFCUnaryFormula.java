package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public abstract class PCSFCUnaryFormula extends PCSFCFormula {

	protected PCSFCFormula child;
	
	public PCSFCUnaryFormula(final PCSFCFormula f) {
		this.child = f;
	}

	@Override
	public String toString(boolean latex) {
		StringBuilder str = new StringBuilder();
		str.append(this.operator(latex) + "(");
		str.append(this.child.toString(latex));
		str.append(")");
		return str.toString();
	}
	
	public abstract String operator(boolean latex);
	
}
