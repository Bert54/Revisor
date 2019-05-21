package fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLLiteral;

public abstract class Interpretation implements Iterable<Integer> {
	
	// Fields :
	
	protected final LI li;
	
	// Constructors :
	
	protected Interpretation(final LI li) {
		this.li = li;
	}
	
	// Methods :
	
	public abstract int size();
	
	/**
	 * 
	 * @param v
	 *            Une variable propositionelle
	 * @return Vrai ssi cette interpretation associe une valeur booleene a cette
	 *         variable
	 */
	public final boolean canInterpret(final PLLiteral v) {
		return this.canInterpret(Math.abs(v.getID()));
	}
	
	/**
	 * 
	 * @param nomvar
	 *            Nom d'une variable propositionelle
	 * @return Vrai ssi cette interpretation associe une valeur booleene a cette
	 *         variable
	 */
	public final boolean canInterpret(final String nomvar) {
		return this.canInterpret(Math.abs(this.li.getID(nomvar)));
	}
	
	public abstract boolean canInterpret(Integer var);
	
	/**
	 * 
	 * @param nomvar
	 *            Une variable propositionnelle
	 * 
	 * @return Vrai ssi cette interpretation satisfait la formule en entree
	 * @throws InterpretationFunctionDomainException
	 *             lance une exception si il manque l'interpretation d'une
	 *             variable
	 */
	public final boolean satisfies(final String nomvar) throws InterpretationFunctionDomainException {
		return this.satisfies(this.li.getID(nomvar));
	}
	
	/**
	 * 
	 * @param v
	 *            Une variable propositionnelle
	 * 
	 * @return Vrai ssi cette interpretation satisfait la formule en entree
	 * @throws InterpretationFunctionDomainException
	 *             lance une exception si il manque l'interpretation d'une
	 *             variable
	 */
	public final boolean satisfies(final PLLiteral v) throws InterpretationFunctionDomainException {
		return this.satisfies(v.getID());
	}
	
	/**
	 * 
	 * @param f
	 *            Une formule propositionnelle
	 * 
	 * @return Vrai ssi cette interpretation satisfait la formule en entree
	 * @throws InterpretationFunctionDomainException
	 *             lance une exception si il manque l'interpretation d'une
	 *             variable
	 */
	public final boolean satisfies(final PLFormula f) throws InterpretationFunctionDomainException {
		return f.estSatisfaitePar(this);
	}
	
	/**
	 * Ajoute l'interpretation de la variable prop. v
	 * 
	 * @param v
	 *            Une variable propositionnelle
	 * @param interpretation
	 *            L'interpretation (Vrai ou Faux) de v
	 * 
	 */
	public final void setAsTrue(final PLLiteral v) {
		this.set(Math.abs(v.getID()), v.getPolarity());
	}
	
	/**
	 * Ajoute l'interpretation de la variable prop. v
	 * 
	 * @param v
	 *            Une variable propositionnelle
	 * @param interpretation
	 *            L'interpretation (Vrai ou Faux) de v
	 * 
	 */
	public abstract void set(Integer v, boolean interpretation);
	
	/**
	 * Supprime l'interpretation de la variable prop. v
	 * 
	 * @param v
	 *            Une variable propositionnelle
	 * 
	 */
	public abstract void delete(PLLiteral v);
	
	/**
	 * Ajoute l'interpretation d'une variable prop.
	 * 
	 * @param name
	 *            Le nom de la variable prop.
	 * @param interpretation
	 *            L'interpretation (Vrai ou Faux) de v
	 * 
	 */
	public void set(final String name, final boolean interpretation) {
		this.set(Math.abs(this.li.getID(name)), interpretation);
	}
	
	/**
	 * Supprime l'interpretation d'une variable prop.
	 * 
	 * @param name
	 *            Le nom de la variable prop.
	 * 
	 */
	public abstract void delete(String name);
	
	public abstract boolean satisfies(Integer var) throws InterpretationFunctionDomainException;
	
}
