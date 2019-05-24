package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public abstract class PCSFCBinaryFormula extends PCSFCFormula {

	protected PCSFCFormula leftChild;
	protected PCSFCFormula rightChild;
	
	public PCSFCBinaryFormula(PCSFCFormula lc, PCSFCFormula rc) {
		this.leftChild = lc;
		this.rightChild = rc;
	}

	public PCSFCFormula getLeftFormula() {
		return this.leftChild;
	}
	
	public PCSFCFormula getRightFormula() {
		return this.rightChild;
	}
	
	@Override
	public String toString(final boolean latex) {
		StringBuilder str = new StringBuilder();
		str.append("(");
		str.append(this.leftChild.toString(latex));
		str.append(") " + this.operator(latex) + " (");
		str.append(this.rightChild.toString(latex));
		str.append(")");
		return str.toString();
	}
	
	public abstract String operator(boolean latex);

}
