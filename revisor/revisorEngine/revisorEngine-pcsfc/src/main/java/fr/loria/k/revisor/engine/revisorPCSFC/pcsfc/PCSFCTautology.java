package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public class PCSFCTautology extends PCSFCFormula {

	public static final String LATEX_TAUTOLOGY = "{\\top}";
	public static final String TAUTOLOGY = "tautology";
	
	public String toString(boolean latex) {
		StringBuilder str = new StringBuilder();
		if (latex) {
			str.append(LATEX_TAUTOLOGY);
		}
		else {
			str.append(TAUTOLOGY);
		}
		return str.toString();
	}

	@Override
	public PCSFCFormula toPCLC() {
		return new PCSFCTautology();
	}

	@Override
	public boolean canRevise() {
		return true;
	}
}
