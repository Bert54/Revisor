package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCXor extends PCSFCBinaryFormula {

	public static final String LATEX_XOR_SYMBOL = "{\\oplus}";
	public static final String XOR_SYMBOL = "XOR";
	
	public PCSFCXor(PCSFCFormula lc, PCSFCFormula rc) {
		super(lc, rc);
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCOr(new PCSFCAnd(this.leftChild.toPCLC(), new PCSFCNot(this.rightChild.toPCLC())), new PCSFCAnd(new PCSFCNot(this.leftChild.toPCLC()), this.rightChild.toPCLC()));
	}

	@Override
	public boolean canRevise() {
		return false;
	}
	
	@Override
	public String operator(boolean latex) {
		if (latex) {
			return "\\:" + LATEX_XOR_SYMBOL + "\\:";
		}
		return XOR_SYMBOL;
	}

}
