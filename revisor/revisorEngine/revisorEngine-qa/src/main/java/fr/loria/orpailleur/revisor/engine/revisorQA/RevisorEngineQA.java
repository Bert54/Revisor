package fr.loria.orpailleur.revisor.engine.revisorQA;

import java.util.List;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.core.AbstractRevisorEngine;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QAPBackendRequest;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeAlgebra;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintDisjunction;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintFormula;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintNetwork;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintScenario;
import fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeVariableSubstitution;

/**
 * @author Valmi Dufout-Lussier
 * @author William Philbert
 */
public class RevisorEngineQA extends AbstractRevisorEngine {
	
	// Fields :
	
	private boolean backend_ready;
	private QAPBackendRequest backend;
	
	// Constructors :
	
	public RevisorEngineQA() {
		this.backend_ready = false;
	}
	
	// Methods :
	
	/**
	 * Instanciate a Process to communicate with the backend.
	 * @throws Exception
	 */
	public void initBackend() throws Exception {
		this.backend = new QAPBackendRequest();
		this.backend_ready = true;
	}
	
	/**
	 * Check if a Process was instanciated already, otherwise do it now.
	 * @throws Exception
	 */
	private void checkBackend() throws Exception {
		if(!this.backend_ready) {
			this.initBackend();
		}
	}
	
	public QualitativeAlgebra loadAlgebra(String label) throws Exception {
		this.checkBackend();
		return new QualitativeAlgebra(this.backend, label);
	}
	
	public QualitativeConstraintNetwork parseFormula(String formula, QualitativeAlgebra algebra) throws Exception {
		this.checkBackend();
		return new QualitativeConstraintNetwork(this.backend, formula, algebra, "formula");
	}
	
	public QualitativeConstraintNetwork parseFile(String file, QualitativeAlgebra algebra) throws Exception {
		this.checkBackend();
		return new QualitativeConstraintNetwork(this.backend, file, algebra);
	}
	
	public QualitativeConstraintFormula parseFilePC(String file, QualitativeAlgebra algebra) throws Exception {
		this.checkBackend();
		return new QualitativeConstraintDisjunction(this.backend, file, algebra);
	}
	
	public int getLastRevisionDistance() {
		return this.backend.getLastRevisionDistance();
	}
	
	public QualitativeConstraintDisjunction buildDisjunction(Set<QualitativeConstraintNetwork> disjuncts) throws Exception {
		return new QualitativeConstraintDisjunction(disjuncts);
	}
	
	public List<QualitativeConstraintScenario> adapt(QualitativeConstraintNetwork source, QualitativeConstraintNetwork target, QualitativeConstraintNetwork dk) throws Exception {
		return revise(source.conjoinWith(dk), target.conjoinWith(dk));
	}
	
	public List<QualitativeConstraintScenario> adaptExhaustively(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk) throws Exception {
		return reviseExhaustively(source.conjoinWith(dk), target.conjoinWith(dk));
	}
	
	public List<QualitativeConstraintScenario> adapt(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk, QualitativeVariableSubstitution subst) throws Exception {
		QualitativeConstraintFormula abstractedSourceWithDK = source.abstractWith(subst).conjoinWith(dk);
		QualitativeConstraintFormula refinedTargetWithDK = target.refineWith(subst, abstractedSourceWithDK).conjoinWith(dk);
		return revise(abstractedSourceWithDK, refinedTargetWithDK);
	}
	
	public List<QualitativeConstraintScenario> adaptExhaustively(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk, QualitativeVariableSubstitution subst) throws Exception {
		QualitativeConstraintFormula abstractedSourceWithDK = source.abstractWith(subst).conjoinWith(dk);
		QualitativeConstraintFormula refinedTargetWithDK = target.refineWith(subst, abstractedSourceWithDK).conjoinWith(dk);
		
		//System.err.println( "abstractedSourceWithDK: " + abstractedSourceWithDK.getDisjuncts() );
		//System.err.println( "refinedTargetWithDK: " + refinedTargetWithDK.getDisjuncts() );
		
		return reviseExhaustively(abstractedSourceWithDK, refinedTargetWithDK);
	}
	
	public List<QualitativeConstraintScenario> revise(QualitativeConstraintFormula psi, QualitativeConstraintFormula mu) throws Exception {
		return psi.reviseWith(mu);
	}
	
	public List<QualitativeConstraintScenario> reviseExhaustively(QualitativeConstraintFormula psi, QualitativeConstraintFormula mu) throws Exception {
		//System.err.println("ER");
		//System.err.println("PSI: " + psi);
		//System.err.println(" MU: " + mu);
		
		return psi.reviseExhaustivelyWith(mu);
	}
	
	public QualitativeVariableSubstitution prepareSubstitution(String from, String to) throws Exception {
		return new QualitativeVariableSubstitution(from, to);
	}
	
	public QualitativeVariableSubstitution prepareSubstitution(String[] from, String[] to) throws Exception {
		return new QualitativeVariableSubstitution(from, to);
	}
	
}
