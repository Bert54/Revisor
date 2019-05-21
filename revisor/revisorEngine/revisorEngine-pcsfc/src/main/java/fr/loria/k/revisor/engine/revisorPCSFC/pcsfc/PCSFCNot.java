package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCNot extends PCSFCUnaryFormula {

	public static final String LATEX_NOT_SYMBOL = "{\\neg}";
	public static final String NOT_SYMBOL = "NOT";
	
	public PCSFCNot(PCSFCFormula f) {
		super(f);
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCNot(this.child.toPCLC());
	}

	@Override
	public boolean canRevise() {
		return this.child.canRevise();
	}

	@Override
	public String operator(boolean latex) {
		if (latex) {
			return LATEX_NOT_SYMBOL;
		}
		return NOT_SYMBOL;
	}

}
