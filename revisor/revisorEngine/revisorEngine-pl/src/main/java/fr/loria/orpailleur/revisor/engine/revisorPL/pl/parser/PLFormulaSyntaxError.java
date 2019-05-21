package fr.loria.orpailleur.revisor.engine.revisorPL.pl.parser;

public class PLFormulaSyntaxError extends Exception {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public PLFormulaSyntaxError(String commentaire) {
		super(commentaire);
	}
	
}
