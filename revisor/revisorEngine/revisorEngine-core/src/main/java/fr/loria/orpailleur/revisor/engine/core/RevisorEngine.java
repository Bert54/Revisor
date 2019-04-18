package fr.loria.orpailleur.revisor.engine.core;

/**
 * @author William Philbert
 */
public interface RevisorEngine {
	
	/**
	 * Format an name to a LaTeX form. This allows to display greek leters and to put some part of the name in index.
	 * @param name - the name to format.
	 * @return the name, transformed to a LaTeX form.
	 */
	public String formatNameToLatex(String name);
	
}
