package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

import java.util.HashSet;

public abstract class CaseBase {
	
	// Fields :
	
	private HashSet<Case> caseSet;
	
	// Methods :
	
	public void addCase(Case c) {
		throw new UnsupportedOperationException("Not implemented for general case");
	}
	
	public Case retrieve(Case tgt) {
		throw new UnsupportedOperationException("Not implemented for general case");
	}
	
}
