package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCOr extends PCSFCBinaryFormula {

	public static final String LATEX_OR_SYMBOL = "{\\lor}";
	public static final String OR_SYMBOL = "OR";
	
	public PCSFCOr(PCSFCFormula lc, PCSFCFormula rc) {
		super(lc, rc);
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCOr(this.leftChild.toPCLC(), this.rightChild.toPCLC());
	}

	@Override
	public boolean canRevise() {
		return this.leftChild.canRevise() && this.rightChild.canRevise();
	}

	@Override
	public String operator(boolean latex) {
		if (latex) {
			return "\\:" + LATEX_OR_SYMBOL + "\\:";
		}
		return OR_SYMBOL;
	}

}
