package fr.loria.orpailleur.revisor.engine.core;

import fr.loria.orpailleur.revisor.engine.core.utils.string.LatexUtils;

/**
 * @author William Philbert
 */
public abstract class AbstractRevisorEngine implements RevisorEngine {
	
	// Constants :
	
	protected static final String TO_INDEX = "-";
	protected static final String LATEX_TO_INDEX = "%s_{%s}";
	
	protected static final String UNDERSCORE = "_";
	protected static final String LATEX_UNDERSCORE = "{\\_}";
	
	// Methods :
	
	protected String getLatexSymbol(String name) {
		return LatexUtils.symbol(name.replace(UNDERSCORE, LATEX_UNDERSCORE));
	}
	
	@Override
	public String formatNameToLatex(String name) {
		String[] parts = name.split(TO_INDEX);
		int nbParts = parts.length;
		
		String result = this.getLatexSymbol(parts[nbParts - 1]);
		
		for(int i = nbParts - 2; i >= 0; i--) {
			result = String.format(LATEX_TO_INDEX, this.getLatexSymbol(parts[i]), result);
		}
		
		return result;
	}
	
}
