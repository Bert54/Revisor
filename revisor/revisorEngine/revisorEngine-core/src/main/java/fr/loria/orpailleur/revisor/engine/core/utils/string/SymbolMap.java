package fr.loria.orpailleur.revisor.engine.core.utils.string;

import java.util.HashMap;

/**
 * @author William Philbert
 */
public class SymbolMap extends HashMap<String, String> {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Methods :
	
	public String symbol(String textSymbol, boolean latex) {
		if(latex) {
			String latexSymbol = this.get(textSymbol);
			
			if(latexSymbol != null) {
				return latexSymbol;
			}
		}
		
		return textSymbol;
	}
	
}
