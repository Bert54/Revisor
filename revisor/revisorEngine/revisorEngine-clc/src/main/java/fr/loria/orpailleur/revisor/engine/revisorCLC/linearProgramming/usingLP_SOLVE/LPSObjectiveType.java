package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.usingLP_SOLVE;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Objective.ObjectiveType;

public class LPSObjectiveType {
	
	//	public static ObjectiveType minimize = new ObjectiveType();
	//	public static ObjectiveType maximize = new ObjectiveType();
	//	
	//	public int getVal() {
	//		int val;
	//		
	//		if(this.equals(LPSObjectiveType.minimize)) {
	//			val = 0;
	//		}
	//		else {
	//			if(this.equals(LPSObjectiveType.maximize)) {
	//				val = 1;
	//			}
	//			else {
	//				throw new UnsupportedOperationException("La contrainte d'objective n'existe pas");
	//			}
	//		}
	//		
	//		return val;
	//	}
	
	public static int getLPSconstrTypeCode(ObjectiveType type) {
		switch(type) {
			case MINIMIZE:
				return 0;
			case MAXIMIZE:
				return 1;
			default:
				throw new UnsupportedOperationException("La contrainte d'objective n'existe pas");
		}
	}
	
	//	@Override
	//	public String toString() {
	//		String resultat = "";
	//		
	//		if(this.equals(ObjectiveType.minimize)) {
	//			resultat = "MIN : ";
	//		}
	//		else {
	//			if(this.equals(ObjectiveType.maximize)) {
	//				resultat = "MAX : ";
	//			}
	//			else {
	//				throw new UnsupportedOperationException("La contrainte d'objective n'existe pas");
	//			}
	//		}
	//		
	//		return resultat;
	//	}
	
}
