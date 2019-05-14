package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCOr extends PCSFCBinaryFormula {

	public static final String OR_SYMBOL = "OR";
	
	public PCSFCOr(PCSFCFormula lc, PCSFCFormula rc) {
		super(lc, rc);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("(");
		str.append(this.leftChild);
		str.append(") " + OR_SYMBOL + " (");
		str.append(this.rightChild);
		str.append(")");
		return str.toString();
	}

}
