package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCAnd extends PCSFCBinaryFormula {

	public static final String LATEX_AND_SYMBOL = "{\\land}";
	public static final String AND_SYMBOL = "AND";
	
	public PCSFCAnd(PCSFCFormula lc, PCSFCFormula rc) {
		super(lc, rc);
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCAnd(this.leftChild.toPCLC(), this.rightChild.toPCLC());
	}

	@Override
	public boolean canRevise() {
		return this.leftChild.canRevise() && this.rightChild.canRevise();
	}

	@Override
	public String operator(boolean latex) {
		if (latex) {
			return "\\:" + LATEX_AND_SYMBOL + "\\:" ;
		}
		return AND_SYMBOL;
	}

}
