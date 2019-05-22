package fr.loria.orpailleur.revisor.engine.revisorCLC;

import java.io.File;
import java.util.Vector;

import fr.loria.orpailleur.revisor.engine.core.AbstractRevisorEngine;
import fr.loria.orpailleur.revisor.engine.core.utils.files.Resources;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.LinearizableDistanceBasedConservativeAdaptation;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexCase;
import fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage.SimplexDomainKnowledge;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.IntegerVariable;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableAttributeValueMetricSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableDistance;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealValue;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealVariable;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Value;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

/**
 * @author Julien Cojan
 * @author William Philbert
 */
public class RevisorEngineCLC extends AbstractRevisorEngine {
	
	// Types :
	
	public enum variableType {
		Real,
		Integer
	}
	
	// Constants :
	
	public static final String LPSOLVE_ENV_VAR = "LP_SOLVE";
	public static final String LPSOLVE_JAR = "lpsolve55j.jar";
	
	// Class init :
	
	static {
		// Add LP_SOLVE/lpsolve55j.jar to the classpath when RevisorEngineCLC is loaded.
		Resources.addJarsToClassPath(new File(System.getenv(LPSOLVE_ENV_VAR), LPSOLVE_JAR));
	}
	
	// Fields :
	
	private LinearizableDistance distance;
	private LinearizableAttributeValueMetricSpace representationSpace;
	
	// Constructors :
	
	public RevisorEngineCLC() {
		this.initSpace();
	}
	
	// Methods :
	
	/**
	 * Initializes the representation space.
	 */
	public void initSpace() {
		this.distance = new LinearizableDistance();
		this.representationSpace = new LinearizableAttributeValueMetricSpace(this.distance);
	}
	
	/**
	 * Resets (set to 1) the weights of every variable.
	 */
	public void resetWeights() {
		Vector<Variable> vars = this.representationSpace.getVariables();
		
		for(Variable var : vars) {
			this.distance.addTerm(var, new RealValue(1));
		}
	}
	
	/**
	 * Sets the weight of a single variable.
	 * @param variable - an instance of the variable.
	 * @param weight - the weight to be given to the variable. The weight must be a strictly positive (> 0) number.
	 */
	public void setWeight(final Variable variable, final Value weight) {
		this.representationSpace.addDimention(variable);
		this.distance.addTerm(variable, weight);
	}
	
	/**
	 * Sets the weight of a single variable.
	 * @param variableName - a string representing the literal
	 * @param weight - the weight to be given to the literal. The weight must be a strictly positive (> 0) number.
	 */
	public void setWeight(final String variableName, final double weight, final variableType varType) {
		Variable var = this.representationSpace.getVariables(variableName);
		
		if(var == null) {
			switch(varType) {
				case Real:
					var = new RealVariable(variableName);
					break;
				case Integer:
					var = new IntegerVariable(variableName);
					break;
			}
		}
		
		this.representationSpace.addDimention(var);
		this.distance.addTerm(var, new RealValue(weight));
	}
	
	/**
	 * Adapts a source to a target given a domain knowledge.
	 * @param source - a SimplexCase representing the source.
	 * @param target - a SimplexCase representing the target.
	 * @param dk - a SimplexDomainKnowledge representing the domain knowledge.
	 * @return the source adapted to the target given the domain knowledge according to the distance-revision adaptation.
	 */
	public SimplexCase adapt(final SimplexCase source, final SimplexCase target, final SimplexDomainKnowledge dk) {
		SimplexCase ctxtSrce = dk.contextualize(source);
		System.out.println("source with txt:\n" + ctxtSrce);
		SimplexCase ctxtTgt = dk.contextualize(target);
		System.out.println("tgt with txt:\n" + ctxtTgt);
		return revise(ctxtSrce, ctxtTgt);
	}
	
	/**
	 * Revises a formula psi by an other formula mu, using Dalal revision operator.
	 * @param psi - a first SimplexCase.
	 * @param mu - a second SimplexCase.
	 * @return psi revised by mu, using distance-revision operator.
	 */
	public SimplexCase revise(final SimplexCase psi, final SimplexCase mu) {
		LinearizableDistanceBasedConservativeAdaptation adaptKnowledge = new LinearizableDistanceBasedConservativeAdaptation(this.representationSpace);
		return adaptKnowledge.adapt(mu, psi);
	}
	
	public void print(final SimplexCase simplexCase) {
		System.out.println(simplexCase);
	}
	
	//	/**
	//	* Instantiate a PL formula from a string
	//	*
	//	* @param input
	//	* A string representing a propositional formula in infix
	//	* notation
	//	* @return The instantiated PL formula, or null if input is not
	//	* syntactically correct PL formula.
	//	*/
	//	public PLFormula parseFormula(final String input) {
	//		try {
	//			PLFormula f = new PLFormulaParser().parse(input);
	//			return f;
	//		}
	//		catch(PLFormulaSyntaxError e) {
	//			e.printStackTrace();
	//			return null;
	//		}
	//	}
	
}
