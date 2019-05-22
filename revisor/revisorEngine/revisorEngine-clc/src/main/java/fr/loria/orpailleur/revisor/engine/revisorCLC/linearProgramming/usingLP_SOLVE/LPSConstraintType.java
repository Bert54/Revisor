package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.usingLP_SOLVE;

import lpsolve.LpSolve;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint.ConstraintType;

public class LPSConstraintType {
	
	//	public static ConstraintType geq = new ConstraintType();
	//	public static ConstraintType equal = new ConstraintType();
	//	public static ConstraintType leq = new ConstraintType();
	//	
	//	public int getVal() {
	//		int val;
	//		
	//		if(this.equals(LPSConstraintType.leq)) {
	//			val = LpSolve.LE;
	//		}
	//		else {
	//			if(this.equals(LPSConstraintType.equal)) {
	//				val = LpSolve.EQ;
	//			}
	//			else {
	//				if(this.equals(LPSConstraintType.geq)) {
	//					val = LpSolve.GE;
	//				}
	//				else {
	//					throw new UnsupportedOperationException("Le type de contraintes n'existe pas");
	//				}
	//			}
	//		}
	//		
	//		return val;
	//	}
	
	public static int getLPSconstrTypeCode(ConstraintType type) {
		switch(type) {
			case EQUAL:
				return LpSolve.EQ;
			case LEQ:
				return LpSolve.LE;
			case GEQ:
				return LpSolve.GE;
			default:
				throw new UnsupportedOperationException("Le type de contraintes n'existe pas");
		}
	}
	
	//	@Override
	//	public String toString() {
	//		String resultat = "";
	//		
	//		if(this.equals(ConstraintType.geq)) {
	//			resultat = " >= ";
	//		}
	//		else {
	//			if(this.equals(ConstraintType.equal)) {
	//				resultat = " = ";
	//			}
	//			else {
	//				if(this.equals(ConstraintType.leq)) {
	//					resultat = " <= ";
	//				}
	//				else {
	//					throw new UnsupportedOperationException("Le type de contraintes n'existe pas");
	//				}
	//			}
	//		}
	//		
	//		return resultat;
	//	}
	
}
