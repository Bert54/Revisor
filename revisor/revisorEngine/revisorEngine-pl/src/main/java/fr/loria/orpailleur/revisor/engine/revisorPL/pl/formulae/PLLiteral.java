package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import fr.loria.orpailleur.revisor.engine.revisorPL.RevisorPL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;

public class PLLiteral extends PLFormula {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected int id;
	
	// Constructors :
	
	public PLLiteral(final LI li, final String name) {
		super(li);
		this.id = li.getOrCreateID(name);
	}
	
	public PLLiteral(final LI li, final int id) {
		super(li);
		this.id = id;
	}
	
	// Getters :
	
	public int getID() {
		return this.id;
	}
	
	// Methods :
	
	@Override
	public boolean estSatisfaitePar(final Interpretation i) throws InterpretationFunctionDomainException {
		if(this.id == LI.TRUE)
			return true;
		else if(this.id == LI.FALSE)
			return false;
		return i.satisfies(this);
	}
	
	public boolean getPolarity() {
		if(this.id > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
	
	public String getName() {
		return this.li.getName(this.id);
	}
	
	public String getSimpleName() {
		return this.li.getSimpleName(this.id);
	}
	
	@Override
	public PLFormula[] listeFils() {
		return new PLFormula[0];
	}
	
	@Override
	public PLFormula toNNF() {
		return this;
	}
	
	public PLLiteral getOpposite() {
		return new PLLiteral(this.li, -this.id);
	}
	
	@Override
	public PLFormula toDNF(final int options_distributeur) {
		return this;
	}
	
	@Override
	public PLFormula flatten() {
		return this;
	}
	
	@Override
	public PLFormula toCNF(final int options_distributeur) {
		return this;
	}
	
	@Override
	public int nombreFils() {
		return 0;
	}
	
	@Override
	public boolean equals(final Object o) {
		if(o instanceof PLLiteral) {
			return this.id == ((PLLiteral) o).id;
		}
		return false;
	}
	
	static final PLLiteral[] asLiteralArray(final PLFormula[] formulae) {
		PLLiteral[] lits = new PLLiteral[formulae.length];
		for(int i = 0; i < lits.length; i++) {
			lits[i] = (PLLiteral) formulae[i];
		}
		return lits;
	}
	
	@Override
	protected boolean isUnary() {
		return true;
	}
	
	@Override
	public String toString(boolean latex) {
		return latex ? RevisorPL.formatNameToLatex(this.getName()) : this.getName();
	}
	
	@Override
	public String toString_Prefixed() {
		return this.getName();
	}
	
}
