package fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;

public class InterpretationFunctionDomainException extends Exception {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public InterpretationFunctionDomainException(final String variable_manquante) {
		super("PL Variable " + variable_manquante + " is not in the domain of the interpretation function.");
	}
	
	public InterpretationFunctionDomainException(final LI li, final Integer var) {
		super("PL Variable " + li.getSimpleName(var) + " is not in the domain of the interpretation function.");
	}
	
}
