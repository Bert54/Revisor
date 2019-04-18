package fr.loria.orpailleur.revisor.engine.core.utils.string;

/**
 * @author William Philbert
 */
public interface LatexFormatable {
	
	/**
	 * Returns a string representation of this expression.
	 * @param latex - specifies whether the result should be formated in LaTeX.
	 * @return a string representation of this expression.
	 */
	public String toString(final boolean latex);
	
	/**
	 * Returns a string representation of this expression, formated in LaTeX.
	 * @return a string representation of this expression, formated in LaTeX.
	 */
	public String toLatex();
	
}
