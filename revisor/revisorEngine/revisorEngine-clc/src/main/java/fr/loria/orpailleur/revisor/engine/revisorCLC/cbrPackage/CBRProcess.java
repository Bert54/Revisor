package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Space;

public abstract class CBRProcess {
	
	// Fields :
	
	private CBRKnowledgeBase knowledgeBase;
	private Case targetCase;
	private Space representationSpace;
	
	// Methods :
	
	abstract public void oneAdaptationCycle();
	
}
