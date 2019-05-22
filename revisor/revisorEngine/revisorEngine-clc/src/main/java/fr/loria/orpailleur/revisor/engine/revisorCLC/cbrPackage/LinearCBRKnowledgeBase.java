package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

public class LinearCBRKnowledgeBase {
	
	// Fields :
	
	private SimplexCaseBase caseBase;
	private SimplexDomainKnowledge dk;
	private LinearizableDistanceBasedConservativeAdaptation adaptationKnowledge;
	
	// Constructors :
	
	public LinearizableDistanceBasedConservativeAdaptation getAdaptationKnowledge() {
		return this.adaptationKnowledge;
	}
	
	// Getters :
	
	public SimplexCaseBase getCaseBase() {
		return this.caseBase;
	}
	
	// Methods :
	
	public SimplexDomainKnowledge getDomainKnowledge() {
		return this.dk;
	}
	
}
