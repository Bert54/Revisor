package fr.loria.orpailleur.revisor.engine.revisorQA;

import java.util.List;
import java.util.Set;

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
public class RevisorQA {
	
	// Constants :
	
	private static final RevisorEngineQA engine = new RevisorEngineQA();
	
	// Constructors :
	
	private RevisorQA() {
		// This class don't have to be instantiated.
	}
	
	// Methods :
	
	/**
	 * @see RevisorEngineQA#initBackend()
	 */
	public static void initBackend() throws Exception {
		engine.initBackend();
	}
	
	/**
	 * @see RevisorEngineQA#loadAlgebra(String)
	 */
	public static QualitativeAlgebra loadAlgebra(String label) throws Exception {
		return engine.loadAlgebra(label);
	}
	
	/**
	 * @see RevisorEngineQA#parseFormula(String, QualitativeAlgebra)
	 */
	public static QualitativeConstraintNetwork parseFormula(String formula, QualitativeAlgebra algebra) throws Exception {
		return engine.parseFormula(formula, algebra);
	}
	
	/**
	 * @see RevisorEngineQA#parseFile(String, QualitativeAlgebra)
	 */
	public static QualitativeConstraintNetwork parseFile(String file, QualitativeAlgebra algebra) throws Exception {
		return engine.parseFile(file, algebra);
	}
	
	/**
	 * @see RevisorEngineQA#parseFilePC(String, QualitativeAlgebra)
	 */
	public static QualitativeConstraintFormula parseFilePC(String file, QualitativeAlgebra algebra) throws Exception {
		return engine.parseFilePC(file, algebra);
	}
	
	/**
	 * @see RevisorEngineQA#getLastRevisionDistance()
	 */
	public static int getLastRevisionDistance() {
		return engine.getLastRevisionDistance();
	}
	
	/**
	 * @see RevisorEngineQA#buildDisjunction(Set)
	 */
	public static QualitativeConstraintDisjunction buildDisjunction(Set<QualitativeConstraintNetwork> disjuncts) throws Exception {
		return engine.buildDisjunction(disjuncts);
	}
	
	/**
	 * @see RevisorEngineQA#adapt(QualitativeConstraintNetwork, QualitativeConstraintNetwork, QualitativeConstraintNetwork)
	 */
	public static List<QualitativeConstraintScenario> adapt(QualitativeConstraintNetwork source, QualitativeConstraintNetwork target, QualitativeConstraintNetwork dk) throws Exception {
		return engine.adapt(source, target, dk);
	}
	
	/**
	 * @see RevisorEngineQA#adaptExhaustively(QualitativeConstraintFormula, QualitativeConstraintFormula, QualitativeConstraintFormula)
	 */
	public static List<QualitativeConstraintScenario> adaptExhaustively(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk) throws Exception {
		return engine.adaptExhaustively(source, target, dk);
	}
	
	/**
	 * @see RevisorEngineQA#adapt(QualitativeConstraintFormula, QualitativeConstraintFormula, QualitativeConstraintFormula, QualitativeVariableSubstitution)
	 */
	public static List<QualitativeConstraintScenario> adapt(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk, QualitativeVariableSubstitution subst) throws Exception {
		return engine.adapt(source, target, dk, subst);
	}
	
	/**
	 * @see RevisorEngineQA#adaptExhaustively(QualitativeConstraintFormula, QualitativeConstraintFormula, QualitativeConstraintFormula, QualitativeVariableSubstitution)
	 */
	public static List<QualitativeConstraintScenario> adaptExhaustively(QualitativeConstraintFormula source, QualitativeConstraintFormula target, QualitativeConstraintFormula dk, QualitativeVariableSubstitution subst) throws Exception {
		return engine.adaptExhaustively(source, target, dk, subst);
	}
	
	/**
	 * @see RevisorEngineQA#revise(QualitativeConstraintFormula, QualitativeConstraintFormula)
	 */
	public static List<QualitativeConstraintScenario> revise(QualitativeConstraintFormula psi, QualitativeConstraintFormula mu) throws Exception {
		return engine.revise(psi, mu);
	}
	
	/**
	 * @see RevisorEngineQA#reviseExhaustively(QualitativeConstraintFormula, QualitativeConstraintFormula)
	 */
	public static List<QualitativeConstraintScenario> reviseExhaustively(QualitativeConstraintFormula psi, QualitativeConstraintFormula mu) throws Exception {
		return engine.reviseExhaustively(psi, mu);
	}
	
	/**
	 * @see RevisorEngineQA#prepareSubstitution(String, String)
	 */
	public static QualitativeVariableSubstitution prepareSubstitution(String from, String to) throws Exception {
		return engine.prepareSubstitution(from, to);
	}
	
	/**
	 * @see RevisorEngineQA#prepareSubstitution(String[], String[])
	 */
	public static QualitativeVariableSubstitution prepareSubstitution(String[] from, String[] to) throws Exception {
		return engine.prepareSubstitution(from, to);
	}
	
}
