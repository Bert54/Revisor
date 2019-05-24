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
	public PCSFCFormula toPCSFC() {
		if (this.leftChild instanceof PCSFCNot) {
			return new PCSFCImpl(((PCSFCNot) this.leftChild).getFormulaWithoutUnaryConnector().toPCSFC(), this.rightChild);
		}
		else if (this.leftChild instanceof PCSFCAnd && ((PCSFCAnd) this.leftChild).getRightFormula() instanceof PCSFCNot && this.rightChild instanceof PCSFCAnd && ((PCSFCBinaryFormula) this.rightChild).getLeftFormula() instanceof PCSFCNot) {
			if (((PCSFCBinaryFormula) this.leftChild).getLeftFormula().equals(((PCSFCUnaryFormula) ((PCSFCBinaryFormula) this.rightChild).getLeftFormula()).getFormulaWithoutUnaryConnector()) && ((PCSFCUnaryFormula) ((PCSFCBinaryFormula) this.leftChild).getRightFormula()).getFormulaWithoutUnaryConnector().equals(((PCSFCBinaryFormula) this.rightChild).getRightFormula())) {
				return new PCSFCXor(((PCSFCBinaryFormula) this.leftChild).getLeftFormula().toPCSFC(), ((PCSFCBinaryFormula) this.rightChild).getRightFormula().toPCSFC());
			}
		}
		return new PCSFCOr(this.leftChild.toPCSFC(), this.rightChild.toPCSFC());
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
