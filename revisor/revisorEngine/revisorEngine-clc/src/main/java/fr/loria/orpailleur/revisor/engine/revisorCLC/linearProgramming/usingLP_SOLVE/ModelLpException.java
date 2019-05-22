package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.usingLP_SOLVE;

public class ModelLpException extends Exception {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public ModelLpException() {
		super("Le modele ne peut pas etre cree.");
	}
	
}
