package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.usingLP_SOLVE.LPSproblem;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.AttributeValueSpace;

public class Mediator {
	
	// Types :
	
	public enum Solver {
		LPSOLVE
	}
	
	// Fields :
	
	private static Solver solver = Solver.LPSOLVE;
	
	// Getters :
	
	public static Solver getSolver() {
		return solver;
	}
	
	// Setters :
	
	public static void setSolver(Solver solv) {
		solver = solv;
	}
	
	// Methods :
	
	public static LPproblem getSolver(AttributeValueSpace espace, Simplex contexteCible, Objective objectif) {
		switch(solver) {
			case LPSOLVE:
				return new LPSproblem(espace, contexteCible, objectif);
			default:
				throw new UnsupportedOperationException("No mediation yet for this solver :" + solver);
		}
	}
	
}
