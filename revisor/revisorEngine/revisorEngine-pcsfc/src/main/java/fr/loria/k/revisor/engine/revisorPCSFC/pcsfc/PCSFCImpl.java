package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCImpl extends PCSFCBinaryFormula {

	public static final String LATEX_IMPLICATION_SYMBOL = "{\\Rightarrow}";
	public static final String IMPLICATION_SYMBOL = "IMPLIES";
	
	public PCSFCImpl(PCSFCFormula lc, PCSFCFormula rc) {
		super(lc, rc);
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCOr(new PCSFCNot(this.leftChild.toPCLC()), this.rightChild.toPCLC());
	}

	@Override
	public boolean canRevise() {
		return false;
	}

	@Override
	public String operator(boolean latex) {
		if (latex) {
			return "\\:" + LATEX_IMPLICATION_SYMBOL + "\\:";
		}
		return IMPLICATION_SYMBOL;
	}
	
}
