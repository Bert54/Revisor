package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

import java.util.HashSet;

import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableAttributeValueMetricSpace;

public class SimplexCaseBase extends CaseBase {
	
	// Fields :
	
	private HashSet<SimplexCase> caseSet;
	private LinearizableAttributeValueMetricSpace representationSpace;
	
	// Methods :
	
	public void addCase(SimplexCase c) {
		this.caseSet.add(c);
	}
	
	public SimplexCase retrieve(SimplexCase tgt) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
