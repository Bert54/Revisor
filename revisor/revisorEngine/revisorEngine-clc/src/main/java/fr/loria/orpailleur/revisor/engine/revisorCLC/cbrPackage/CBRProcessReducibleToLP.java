package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LPproblem;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableAttributeValueMetricSpace;

public class CBRProcessReducibleToLP {
	
	// Fields :
	
	private LinearCBRKnowledgeBase knowledgeBase;
	private LPproblem equivalentLPproblem;
	private LinearizableAttributeValueMetricSpace representationSpace;
	private SimplexCase targetCase;
	
	// Methods :
	
	public void oneAdaptationCycle() {
		SimplexCaseBase cb = this.knowledgeBase.getCaseBase();
		SimplexDomainKnowledge dk = this.knowledgeBase.getDomainKnowledge();
		LinearizableDistanceBasedConservativeAdaptation ak = this.knowledgeBase.getAdaptationKnowledge();
		SimplexCase srce = cb.retrieve(this.targetCase);
		this.targetCase = dk.contextualize(this.targetCase);
		srce = dk.contextualize(srce);
		this.targetCase = ak.adapt(this.targetCase, srce);
	}
	
}
