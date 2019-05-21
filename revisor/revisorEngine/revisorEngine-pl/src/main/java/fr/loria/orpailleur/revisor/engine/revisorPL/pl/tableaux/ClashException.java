package fr.loria.orpailleur.revisor.engine.revisorPL.pl.tableaux;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;

public class ClashException extends Exception {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public ClashException(final String littName) {
		super("Clash +/- " + littName);
	}
	
	public ClashException(final LI li, final Integer littID) {
		super("Clash +/- " + li.getSimpleName(littID));
	}
	
}
