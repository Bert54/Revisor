package fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae;

import java.io.Serializable;
import java.util.Iterator;

import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexFormatable;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.Interpretation;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationFunctionDomainException;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation.InterpretationIterator;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.pi.PINormalizer;

public abstract class PLFormula implements Iterable<Interpretation>, Serializable, LatexFormatable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	protected final LI li;
	
	// Constructors :
	
	protected PLFormula(final LI li) {
		this.li = li;
	}
	
	// Getters :
	
	public LI getLi() {
		return this.li;
	}
	
	// Methods :
	
	/**
	 * 
	 * @param i
	 *            une interpretation
	 * @return Vrai ssi cette formule est satisfaite par l'intepretation i
	 * @throws InterpretationFunctionDomainException
	 *             lance cette exception quand une variable de la formule n'a
	 *             pas d'interpretation
	 */
	public abstract boolean estSatisfaitePar(Interpretation i) throws InterpretationFunctionDomainException;
	
	/**
	 * 
	 * @return la liste des fils de cette formule dans sa representation sous
	 *         forme d'arbre
	 */
	public abstract PLFormula[] listeFils();
	
	/**
	 * 
	 * @return le nombre de fils direct de cette formule dans sa representation
	 *         sous forme d'arbre
	 */
	public abstract int nombreFils();
	
	/**
	 * 
	 * @return une formule equivalente, mise en Forme Normale Negative
	 */
	public abstract PLFormula toNNF();
	
	/**
	 * 
	 * @param options_distributeur
	 *            options d'optimisation pour l'algo de distribution
	 * @return une formule equivalente, sous forme d'une disjonction de
	 *         conjonctions
	 */
	public abstract PLFormula toDNF(int options_distributeur);
	
	public PLFormula toDNF() {
		return this.toDNF(0x0FFFFFFF);
	}
	
	public PLFormula toCNF() {
		return this.toCNF(0x0FFFFFFF);
	}
	
	public abstract PLFormula toCNF(int options_distributeur);
	
	public PLFormula toPI_DNF() {
		return new PINormalizer(this).normalize();
	}
	
	/**
	 * 
	 * @return une formule equivalente, sans niveaux de parentheses superflus
	 */
	public abstract PLFormula flatten();
	
	@Override
	public Iterator<Interpretation> iterator() {
		return new InterpretationIterator(this);
	}
	
	@Override
	public boolean equals(final Object o) {
		return o == this;
	}
	
	protected abstract boolean isUnary();
	
	@Override
	public final String toString() {
		return this.toString(false);
	}
	
	@Override
	public final String toLatex() {
		return this.toString(true);
	}
	
	public abstract String toString_Prefixed();
	
}
