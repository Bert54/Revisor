package fr.loria.orpailleur.revisor.engine.revisorCLC;

import fr.loria.orpailleur.revisor.engine.revisorCLC.RevisorEngineCLC.variableType;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexCase;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexDomainKnowledge;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Value;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

/**
 * @author Julien Cojan
 * @author William Philbert
 */
public class RevisorCLC {
	
	// Constants :
	
	private static final RevisorEngineCLC engine = new RevisorEngineCLC();
	
	// Constructors :
	
	private RevisorCLC() {
		// This class don't have to be instantiated.
	}
	
	// Methods :
	
	/**
	 * @see RevisorEngineCLC#initSpace()
	 */
	public static final void initSpace() {
		engine.initSpace();
	}
	
	/**
	 * @see RevisorEngineCLC#resetWeights()
	 */
	public static final void resetWeights() {
		engine.resetWeights();
	}
	
	/**
	 * @see RevisorEngineCLC#setWeight(Variable, Value)
	 */
	public static final void setWeight(final Variable variable, final Value weight) {
		engine.setWeight(variable, weight);
	}
	
	/**
	 * @see RevisorEngineCLC#setWeight(String, double, variableType)
	 */
	public static final void setWeight(final String variableName, final double weight, final variableType varType) {
		engine.setWeight(variableName, weight, varType);
	}
	
	/**
	 * @see RevisorEngineCLC#adapt(SimplexCase, SimplexCase, SimplexDomainKnowledge)
	 */
	public static final SimplexCase adapt(final SimplexCase source, final SimplexCase target, final SimplexDomainKnowledge dk) {
		return engine.adapt(source, target, dk);
	}
	
	/**
	 * @see RevisorEngineCLC#revise(SimplexCase, SimplexCase)
	 */
	public static final SimplexCase revise(final SimplexCase psi, final SimplexCase mu) {
		return engine.revise(psi, mu);
	}
	
	/**
	 * @see RevisorEngineCLC#print(SimplexCase)
	 */
	public static final void print(final SimplexCase simplexCase) {
		engine.print(simplexCase);
	}
	
}
