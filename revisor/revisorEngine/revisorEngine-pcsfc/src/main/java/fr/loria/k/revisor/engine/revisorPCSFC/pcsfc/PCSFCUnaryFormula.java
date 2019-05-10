package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc;

public abstract class PCSFCUnaryFormula extends PCSFCFormula {

	protected PCSFCFormula child;
	
	public PCSFCUnaryFormula(final PCSFCFormula f) {
		this.child = f;
	}

}
