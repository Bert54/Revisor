package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com;

import java.util.Vector;

import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.AttributeValueSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.IntegerValue;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearFunction;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

public class Objective {
	
	// Types :
	
	public enum ObjectiveType {
		MINIMIZE,
		MAXIMIZE;
	}
	
	// Fields :
	
	private ObjectiveType type;
	private LinearFunction objectiveFunction;
	
	// Constructors :
	
	public Objective(AttributeValueSpace espace) {
		this.type = null;
		this.objectiveFunction = new LinearFunction();
		Vector<Variable> r = espace.getVariables();
		for(Variable v : r) {
			this.objectiveFunction.addTerm(v, new IntegerValue(0));
		}
	}
	
	// Getters :
	
	public ObjectiveType getType() {
		return this.type;
	}
	
	public LinearFunction getObjectiveFunction() {
		return this.objectiveFunction;
	}
	
	// Setters :
	
	public void setType(ObjectiveType t) {
		this.type = t;
	}
	
	public void setFunction(LinearFunction cost) {
		this.objectiveFunction = cost;
	}
	
	// Methods :
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getType().toString());
		sb.append(getObjectiveFunction().toString());
		return sb.toString();
	}
	
}
