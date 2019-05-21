package fr.loria.orpailleur.revisor.engine.revisorPL.pl.revops;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

public abstract class RevOp {
	
	// Fields :
	
	protected PLFormula psi = null, mu = null;
	
	// Constructors :
	
	public RevOp(final PLFormula psi, final PLFormula mu) {
		this.psi = psi;
		this.mu = mu;
	}
	
	// Methods :
	
	public boolean verbose = false;
	
	public abstract PLFormula revisePsi();
	
	public abstract void setLiteralWeight(Integer lit, double weight);
	
}
