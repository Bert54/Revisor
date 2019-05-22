package fr.loria.orpailleur.revisor.engine.revisorQA.qa;

/**
 @author Valmi Dufout-Lussier
 @date 01.02.2013
 */
public class QualitativeConstraintScenario extends QualitativeConstraintNetwork {
	
	// Fields :
	
	private int distance;
	
	// Constructors :
	
	public QualitativeConstraintScenario(QAPBackendRequest qap, String objnr) throws Exception {
		this(qap, objnr, Integer.MAX_VALUE);
	}
	
	public QualitativeConstraintScenario(QAPBackendRequest qap, String objnr, int distance) throws Exception {
		super(qap, objnr);
		this.distance = distance;
	}
	
	// Getters :
	
	public int getDistance() {
		return this.distance;
	}
	
}
