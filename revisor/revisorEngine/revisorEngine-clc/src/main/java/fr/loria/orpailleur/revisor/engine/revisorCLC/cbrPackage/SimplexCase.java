package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Simplex;

public class SimplexCase extends Case {
	
	// Fields :
	
	private Simplex equivalentSimplex;
	
	// Constructors :
	
	public SimplexCase() {
		this(new Simplex());
	}
	
	public SimplexCase(Simplex s) {
		super();
		this.equivalentSimplex = s;
	}
	
	// Methods :
	
	public Simplex getSimplex() {
		return this.equivalentSimplex;
	}
	
	public void addConstraint(LinearConstraint constraint) {
		this.equivalentSimplex.addConstraint(constraint);
	}
	
	@Override
	public String toString() {
		return this.equivalentSimplex.toString();
	}
	
}
