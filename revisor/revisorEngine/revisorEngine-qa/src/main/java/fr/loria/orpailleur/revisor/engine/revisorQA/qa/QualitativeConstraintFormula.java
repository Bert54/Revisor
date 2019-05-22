package fr.loria.orpailleur.revisor.engine.revisorQA.qa;

import java.util.List;
import java.util.Set;

/**
 * @author Valmi Dufout-Lussier
 */
public interface QualitativeConstraintFormula {
	
	public List<QualitativeConstraintScenario> reviseExhaustivelyWith(QualitativeConstraintFormula mu) throws Exception;
	
	public QualitativeConstraintFormula abstractWith(QualitativeVariableSubstitution subst) throws Exception;
	
	public QualitativeConstraintFormula conjoinWith(QualitativeConstraintFormula qcn2) throws Exception;
	
	public QualitativeConstraintFormula refineWith(QualitativeVariableSubstitution subst, QualitativeConstraintFormula qcn1, QualitativeConstraintFormula qcn2) throws Exception;
	
	public QualitativeConstraintFormula refineWith(QualitativeVariableSubstitution subst, QualitativeConstraintFormula qcn1) throws Exception;
	
	public QualitativeConstraintFormula refineWith(QualitativeVariableSubstitution subst) throws Exception;
	
	public List<QualitativeConstraintScenario> reviseWith(QualitativeConstraintFormula mu) throws Exception;
	
	public QualitativeAlgebra getAlgebra();
	
	public String getObjnr();
	
	public QAPBackendRequest getQAP();
	
	public Set<QualitativeConstraintNetwork> getDisjuncts();
	
	public boolean isConsistent();
	
}
