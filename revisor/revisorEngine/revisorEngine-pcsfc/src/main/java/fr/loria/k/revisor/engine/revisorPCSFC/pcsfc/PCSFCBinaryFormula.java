package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public abstract class PCSFCBinaryFormula extends PCSFCFormula {

	protected PCSFCFormula leftChild;
	protected PCSFCFormula rightChild;
	
	public PCSFCBinaryFormula(PCSFCFormula lc, PCSFCFormula rc) {
		this.leftChild = lc;
		this.rightChild = rc;
	}

}
