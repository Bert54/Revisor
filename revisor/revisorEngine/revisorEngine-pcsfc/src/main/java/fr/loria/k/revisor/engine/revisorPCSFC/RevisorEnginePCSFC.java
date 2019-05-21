package fr.loria.k.revisor.engine.revisorPCSFC;

import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCFormula;
import fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.PCSFCTautology;
import fr.loria.orpailleur.revisor.engine.core.AbstractRevisorEngine;

public class RevisorEnginePCSFC extends AbstractRevisorEngine {

	public PCSFCFormula revise(PCSFCFormula psi, PCSFCFormula mu, double epsilon) {
		// TODO Revision.
		// You take it from here Dylan
		return new PCSFCTautology();
		
		// Temporary return statement
		//return <insert revised formula here>;
	}
	
	
}
