package fr.loria.orpailleur.revisor.engine.revisorPL.pl.output;

import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexFormatable;
import fr.loria.orpailleur.revisor.engine.core.utils.string.StringUtils;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.PLFormula;

/**
 * This class represent a substitution that can be applied to any propositional formula.</br>
 * It doesn't contain important methods and is designed only to be used as a return type for Reduction class methods.
 * @author Quentin BRABANT
 * @author William PHILBERT
 */
public class Substitution implements LatexFormatable {
	
	// Fields :
	
	/**
	 * Left part of the substitution.
	 */
	private PLFormula left;
	
	/**
	 * Right part of the substitution.
	 */
	private PLFormula right;
	
	// Constructors :
	
	/**
	 * @param left - the left part of the substitution.
	 * @param right - the right part of the substitution.
	 */
	public Substitution(PLFormula left, PLFormula right) {
		this.left = left;
		this.right = right;
	}
	
	// Getters :
	
	/**
	 * @return the left part of the substitution.
	 */
	public PLFormula getLeft() {
		return this.left;
	}
	
	/**
	 * @return the right part of the substitution.
	 */
	public PLFormula getRight() {
		return this.right;
	}
	
	// Setters :
	
	protected void setLeft(PLFormula left) {
		this.left = left;
	}
	
	protected void setRight(PLFormula right) {
		this.right = right;
	}
	
	// Methods :
	
	public String operator(final boolean latex) {
		return latex ? PL.LATEX_SUBSTITUTION_SYMBOL : PL.SUBSTITUTION_SYMBOL;
	}
	
	@Override
	public String toString(final boolean latex) {
		final StringBuilder builder = new StringBuilder();
		final String space = StringUtils.symbol(" ", latex);
		
		builder.append(this.left.toString(latex));
		builder.append(space);
		builder.append(this.operator(latex));
		builder.append(space);
		builder.append(this.right.toString(latex));
		
		return builder.toString();
	}
	
	@Override
	public final String toString() {
		return this.toString(false);
	}
	
	@Override
	public final String toLatex() {
		return this.toString(true);
	}
	
}
