package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint.ConstraintType;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.AttributeValueSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.RealValue;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Value;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

public class LPsolution {
	
	// Fields :
	
	// private Vector<AttributeValueInstance> edges;
	private HashMap<Variable, Value> resultat;
	private double valeurObjectif;
	
	// Constructors :
	
	public LPsolution(double objectiveValue) {
		this.resultat = new HashMap<>();
		this.valeurObjectif = objectiveValue;
	}
	
	// Methods :
	
	public void addHashMap(Variable key, Value value) {
		this.resultat.put(key, value);
	}
	
	public double getValueof(Variable variable) {
		Value v = this.resultat.get(variable);
		return v.getValueDouble();
	}
	
	public double getValueFctObj() {
		// Value v=resultat.get(LPproblem.obj);
		// return v.getValueDouble();
		return this.valeurObjectif;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Set<Variable> listkey = this.resultat.keySet();
		sb.append("Optimal value " + this.valeurObjectif + " for the objective function reached with :\n");
		
		for(Variable variable : listkey) {
			Value v = this.resultat.get(variable);
			sb.append("\t" + variable.getName() + " = " + v.toString() + "\n");
		}
		
		return sb.toString();
	}
	
	//	public Simplex getEquivalentSimplex() {
	//		return new Simplex(this.resultat);
	//		// TODO - See if it is possible to get the simplex of all the optimal solutions.
	//	}
	
	/**
	 * Returns the singleton simplex for the variables of subspace.
	 */
	public Simplex getEquivalentSimplex(AttributeValueSpace subspace) {
		Simplex s = new Simplex();
		Vector<Variable> v = subspace.getVariables();
		LinearConstraint temp;
		
		for(int i = 0; i < v.size(); i++) {
			temp = new LinearConstraint();
			temp.addTerm(v.get(i), new RealValue(1));
			temp.setOffset(this.resultat.get(v.get(i)));
			temp.setType(ConstraintType.EQUAL);
			s.addConstraint(temp);
		}
		
		return s;
	}
	
}
