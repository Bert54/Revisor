package fr.loria.orpailleur.revisor.engine.revisorCLC.cbrPackage;

import java.util.Enumeration;
import java.util.HashMap;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LPproblem;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LPsolution;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint.ConstraintType;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Mediator;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Objective;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Objective.ObjectiveType;
import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.Simplex;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.AttributeValueSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearFunction;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableAttributeValueMetricSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealValue;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

public class LinearizableDistanceBasedConservativeAdaptation extends DistanceBasedConservativeAdaptation {
	
	// Fields :
	
	private LinearFunction cost;
	private LinearizableAttributeValueMetricSpace representationSpace;
	private AttributeValueSpace resolutionSpace;
	private Simplex constraints;
	// relate representationSpace variables to resolutionSpace variables related to the source case
	private HashMap<Variable, Variable> srceReformulation;
	// relate representationSpace variables to resolutionSpace variables related to the variations
	private HashMap<Variable, Variable> distanceReformulation;
	private Simplex resultingSimplex;
	
	// Constructors :
	
	public LinearizableDistanceBasedConservativeAdaptation(LinearizableAttributeValueMetricSpace s) {
		super();
		this.representationSpace = s;
	}
	
	// Methods :
	
	private void createResolutionSpace() {
		/**
		 * if the representation space is U, the equivalent LP problem is
		 * formalized in U³ since the variables for the target case, the source
		 * and the linearization of the distance must be identified Here a new
		 * space is created, target and source case simplex are linked to their
		 * cooresponding variables the distance is translated under the form of
		 * linear contrainst and a linear function.
		 */
		this.resolutionSpace = new AttributeValueSpace(this.representationSpace);
		this.srceReformulation = new HashMap<>();
		this.distanceReformulation = new HashMap<>();
		this.constraints = new Simplex();
		this.cost = new LinearFunction(this.resolutionSpace);
		Enumeration<Variable> e = this.representationSpace.getVariables().elements();
		Variable original, srceVar, distVar;
		
		while(e.hasMoreElements()) {
			// for every variable of the space
			original = e.nextElement();
			// the corresponding source variable is introduced and related to
			// original
			srceVar = original.duplicate("Source:" + original.getName());
			this.resolutionSpace.addDimention(srceVar);
			this.srceReformulation.put(original, srceVar);
			/*
			 * distVar is introduced to translate the distance minimization into
			 * a linear distance minimization distVar gives (when minimized) the
			 * variation between corresponding source and target variable
			 */
			distVar = original.duplicate("Variation:" + original.getName());
			this.resolutionSpace.addDimention(distVar);
			this.distanceReformulation.put(original, distVar);
			relateVariationDimension(distVar, original, srceVar);
			/*
			 * the distance is translated into a linear distance cost on the
			 * distVar variables so that minimizing the distance is equivalent
			 * to minimizing cost
			 */
			if(this.representationSpace.getDistance().getCoef(original) != null) {
				this.cost.addTerm(distVar, this.representationSpace.getDistance().getCoef(original));
			}
		}
	}
	
	private void relateVariationDimension(Variable variation, Variable original, Variable srceVar) {
		// enforces the inequality variation >= |original - srceVar|
		LinearConstraint contr1 = new LinearConstraint(); // variation >=
		// original - srceVar
		contr1.setType(ConstraintType.GEQ);
		contr1.setOffset(new RealValue(0));
		contr1.addTerm(variation, new RealValue(1));
		contr1.addTerm(original, new RealValue(-1));
		contr1.addTerm(srceVar, new RealValue(1));
		this.constraints.addConstraint(contr1);
		LinearConstraint contr2 = new LinearConstraint(); // variation >=
		// srceVar - original
		contr2.setType(ConstraintType.GEQ);
		contr2.setOffset(new RealValue(0));
		contr2.addTerm(variation, new RealValue(1));
		contr2.addTerm(original, new RealValue(1));
		contr2.addTerm(srceVar, new RealValue(-1));
		this.constraints.addConstraint(contr2);
	}
	
	public SimplexCase adapt(SimplexCase tgt, SimplexCase srce) {
		createResolutionSpace();
		// transcribe the source simplex on its dedicated variables
		Simplex srceSimplex = srce.getSimplex().transcribe(this.srceReformulation);
		// source and target constraints are integrated into the simplex
		// constraints
		this.constraints = this.constraints.intersection(srceSimplex);
		this.constraints = this.constraints.intersection(tgt.getSimplex());
		// the objective is to minimize the cost function, which comes to the
		// minimization of the distance
		Objective goal = new Objective(this.resolutionSpace);
		goal.setType(ObjectiveType.MINIMIZE);
		goal.setFunction(this.cost);
		// The equivalent linear programming problem is build and solved
		LPproblem lpPB = Mediator.getSolver(this.resolutionSpace, this.constraints, goal);
		LPsolution lpSolution = lpPB.solve();
		// The linear programming solution is translated under CBR solution
		Simplex solutionSimplex = lpSolution.getEquivalentSimplex(this.representationSpace);
		return new SimplexCase(solutionSimplex);
	}
	
}
