package fr.loria.orpailleur.revisor.engine.revisorPL.pl.output;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.PL;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;

/**
 * This class is used in Reduction class methods
 * @author Quentin BRABANT
 */
public class ApplicationRegle {
	
	// Fields :
	
	private LI li;
	
	/**
	 * Literals at the left part of the rule
	 */
	private LitSet gauche;
	
	/**
	 * Literal at the right part of the rule
	 */
	private int droite;
	
	// Constructors :
	
	public ApplicationRegle(LI li) {
		this.li = li;
		this.gauche = new LitSet(li);
		addGauche(PL.TRUE.getID());
	}
	
	// Getters :
	
	/**
	 * @return a set of literals containing the litterals at the left part of the rule
	 */
	public LitSet getGauche() {
		return this.gauche;
	}
	
	/**
	 * @return the literal at the right part of the rule
	 */
	public int getDroite() {
		return this.droite;
	}
	
	// Setters :
	
	/**
	 * Set the right part of the rule
	 * @param droite literal to be put at the right part of the rule
	 */
	public void setDroite(int droite) {
		this.droite = droite;
	}
	
	// Methods :
	
	/**
	 * Add a literal to the left part of the rule
	 * @param i
	 */
	public void addGauche(int i) {
		this.gauche.add(i);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(int i : this.gauche) {
			builder.append(this.li.getName(i));
			builder.append(" ");
		}
		
		builder.append(" -> ");
		builder.append(this.li.getName(this.droite));
		
		return builder.toString();
	}
	
}
