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
	public PCSFCFormula toPCSFC() {
		PCSFCFormula pcsfcLeft = this.leftChild.toPCSFC();
		PCSFCFormula pcsfcRight = this.rightChild.toPCSFC();;
		
		// TODO find a way to convert compatible PCLC formulas to PCSFC Enumerations
		// (this todo has been put here as it might need to be done here)
		
		if (pcsfcLeft instanceof PCSFCImpl && pcsfcRight instanceof PCSFCImpl && ((PCSFCImpl) pcsfcLeft).getLeftFormula().equals(((PCSFCBinaryFormula) pcsfcRight).getRightFormula()) && ((PCSFCImpl) pcsfcLeft).getRightFormula().equals(((PCSFCBinaryFormula) pcsfcRight).getLeftFormula())) {
			return new PCSFCEquiv(((PCSFCBinaryFormula) pcsfcLeft).getLeftFormula(), ((PCSFCBinaryFormula) pcsfcLeft).getRightFormula());
		}
		return new PCSFCAnd(pcsfcLeft, pcsfcRight);
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
