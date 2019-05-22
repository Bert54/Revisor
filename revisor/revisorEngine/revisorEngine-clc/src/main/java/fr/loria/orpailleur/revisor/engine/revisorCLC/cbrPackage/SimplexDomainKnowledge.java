package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Simplex;

public class SimplexDomainKnowledge extends DomainKnowledge {
	
	// Fields :
	
	// private LinearizableAttributeValueMetricSpace representationSpace;
	private Simplex equivalentSimplex;
	
	// Constructors :
	
	public SimplexDomainKnowledge() {
		this(new Simplex());
	}
	
	public SimplexDomainKnowledge(Simplex s) {
		this.equivalentSimplex = s;
	}
	
	// Methods :
	
	public SimplexCase contextualize(SimplexCase c) {
		return new SimplexCase(c.getSimplex().intersection(this.equivalentSimplex));
	}
	
	public void addConstraint(LinearConstraint constraint) {
		this.equivalentSimplex.addConstraint(constraint);
	}
	
	@Override
	public String toString() {
		return this.equivalentSimplex.toString();
	}
	
}
