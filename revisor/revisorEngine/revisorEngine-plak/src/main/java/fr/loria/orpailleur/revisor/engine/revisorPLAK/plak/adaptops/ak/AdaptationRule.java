package fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.adaptops.ak;

import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.AND;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.formulae.LI;
import fr.loria.orpailleur.revisor.engine.revisorPL.pl.util.LitSet;
import fr.loria.orpailleur.revisor.engine.revisorPLAK.plak.util.DotIt.DotEdge;

/**
 * @author Gabin PERSONENI
 */
public class AdaptationRule implements Comparable<AdaptationRule>, DotEdge, Rule {
	
	// Constants :
	
	private final LI li;
	private final int identifier;
	private LitSet context;
	private LitSet repairs;
	private LitSet introductions;
	private double ruleCost;
	
	// Constructors :
	
	public AdaptationRule(final LI li, final LitSet context, final LitSet repairs, final LitSet introductions, final double cost, final int id) {
		this.li = li;
		this.identifier = id;
		this.context = context;
		this.repairs = repairs;
		this.introductions = introductions;
		this.ruleCost = cost;
	}
	
	// Getters :
	
	public int getIdentifier() {
		return this.identifier;
	}
	
	public LitSet getContext() {
		return this.context;
	}
	
	public LitSet getRepairs() {
		return this.repairs;
	}
	
	public LitSet getIntroductions() {
		return this.introductions;
	}
	
	public double getRuleCost() {
		return this.ruleCost;
	}
	
	// Methods :
	
	public boolean match(final LitSet case_set) {
		for(Integer c_element : this.context) {
			if(!case_set.contains(c_element)) {
				return false;
			}
		}
		
		for(Integer l_element : this.repairs) {
			if(!case_set.contains(l_element)) {
				return false;
			}
		}
		
		for(Integer l_element : this.introductions) {
			if(case_set.contains(-l_element)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return this.identifier;
	}
	
	public LitSet getRIGHT() {
		LitSet set = new LitSet(this.li, this.context);
		set.addAll(this.introductions);
		return set;
	}
	
	@Override
	public int compareTo(final AdaptationRule o) {
		if(this.ruleCost > o.ruleCost) {
			return -1;
		}
		else if(this.ruleCost < o.ruleCost) {
			return 1;
		}
		else {
			return this.identifier - o.identifier;
		}
	}
	
	@Override
	public String toString() {
		String str = "r" + this.identifier + " [" + this.ruleCost + "] ";
		str += new AND(this.li, this.context).toString();
		str += " : ";
		str += new AND(this.li, this.repairs).toString();
		str += " *= ";
		str += this.introductions.toString();
		return str;
	}
	
	public void setCost(final double cost) {
		this.ruleCost = cost;
	}
	
	@Override
	public String dotEdgeLabel() {
		return "R<sub>" + this.identifier + "</sub>";
	}
	
	public boolean contains(final Integer l) {
		if(this.repairs.contains(l)) {
			return true;
		}
		else if(this.context.contains(l)) {
			return true;
		}
		else if(this.introductions.contains(l)) {
			return true;
		}
		return false;
	}
	
}
