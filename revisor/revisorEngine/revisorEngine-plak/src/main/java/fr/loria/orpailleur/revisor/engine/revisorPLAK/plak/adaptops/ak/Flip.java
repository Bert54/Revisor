package fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak;

public class Flip implements Rule {
	
	// Fields :
	
	private int literal;
	
	// Constructors :
	
	public Flip(final int literal) {
		this.literal = literal;
	}
	
	// Getters :
	
	public int getLiteral() {
		return this.literal;
	}
	
}
