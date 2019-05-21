package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;

public class NOT extends PLUnaryFormula {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public NOT(final LI li, final PLFormula f) {
		super(li, f);
	}
	
	// Methods :
	
	@Override
	public boolean estSatisfaitePar(final Interpretation i) throws InterpretationFunctionDomainException {
		return !this.child.estSatisfaitePar(i);
	}
	
	@Override
	public String toString_Prefixed() {
		return PL.NOT_SYMBOL + this.child.toString_Prefixed();
	}
	
	@Override
	public PLFormula toDNF(final int options_distributeur) {
		if(this.child instanceof PLLiteral) {
			return this;
		}
		else {
			return this.toNNF().toDNF(options_distributeur);
		}
	}
	
	@Override
	public PLFormula toCNF(final int options_distributeur) {
		if(this.child instanceof PLLiteral) {
			return this;
		}
		else {
			return this.toNNF().toCNF(options_distributeur);
		}
	}
	
	@Override
	public PLFormula toNNF() {
		if(this.child instanceof NOT) {
			// NNF of !!f == NNF of f
			return ((NOT) this.child).child.toNNF();
		}
		else if(this.child instanceof AND) {
			PLFormula[] fils_degre2 = this.child.listeFils();
			PLFormula[] nouveaux_fils_degre_2 = new PLFormula[fils_degre2.length];
			
			for(int i = 0; i < fils_degre2.length; i++) {
				nouveaux_fils_degre_2[i] = new NOT(this.li, fils_degre2[i]);
			}
			
			return new OR(this.li, nouveaux_fils_degre_2).toNNF();
		}
		else if(this.child instanceof OR) {
			PLFormula[] fils_degre2 = this.child.listeFils();
			PLFormula[] nouveaux_fils_degre_2 = new PLFormula[fils_degre2.length];
			
			for(int i = 0; i < fils_degre2.length; i++) {
				nouveaux_fils_degre_2[i] = new NOT(this.li, fils_degre2[i]);
			}
			
			return new AND(this.li, nouveaux_fils_degre_2).toNNF();
		}
		else if(this.child instanceof PLLiteral) {
			return ((PLLiteral) this.child).getOpposite();
		}
		else {
			return this;
		}
	}
	
	@Override
	public PLFormula flatten() {
		this.child.flatten();
		return this;
	}
	
	@Override
	public int nombreFils() {
		return 1;
	}
	
	@Override
	public String operator(boolean latex) {
		return latex ? PL.LATEX_NOT_SYMBOL : PL.NOT_SYMBOL;
	}
	
}
