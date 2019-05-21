package fr.loria.orpailleur.revisor.engine.revisorPL.pl.interpretation;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;

/**
 * Un conteneur pour un nom de variable et son interpretation.
 * 
 * @author Gabin PERSONENI
 */
public class AtomicInterpretation {
	
	// Fields :
	
	public Integer id;
	public boolean interpretation;
	
	// Constructors :
	
	public AtomicInterpretation(final Integer id, final boolean interpretation) {
		super();
		this.id = Math.abs(id);
		this.interpretation = interpretation;
	}
	
	public AtomicInterpretation(final LI li, final String nom, final boolean interpretation) {
		this(li.getID(nom), interpretation);
	}
	
}
