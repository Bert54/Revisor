package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCNot extends PCSFCUnaryFormula {

	public static final String NOT_SYMBOL = "NOT";
	
	public PCSFCNot(PCSFCFormula f) {
		super(f);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(NOT_SYMBOL + "(");
		str.append(this.child.toString());
		str.append(")");
		return str.toString();
	}

}
