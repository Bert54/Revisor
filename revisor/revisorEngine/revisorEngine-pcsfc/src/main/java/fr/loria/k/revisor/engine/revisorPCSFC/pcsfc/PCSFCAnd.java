package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCAnd extends PCSFCBinaryFormula {

	public static final String AND_SYMBOL = "AND";
	
	public PCSFCAnd(PCSFCFormula lc, PCSFCFormula rc) {
		super(lc, rc);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("(");
		str.append(this.leftChild);
		str.append(") " + AND_SYMBOL + " (");
		str.append(this.rightChild);
		str.append(")");
		return str.toString();
	}

}
