package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

import java.util.HashMap;
import java.util.Vector;

public class LinearFunction {
	
	// Constants :
	
	private HashMap<Variable, Value> coefficients;
	
	// Constructors :
	
	public LinearFunction() {
		this.coefficients = new HashMap<>();
	}
	
	public LinearFunction(AttributeValueSpace espace) {
		this();
		// is an attibute Space needed ?
	}
	
	// Getters :
	
	public HashMap<Variable, Value> getCoefficients() {
		return this.coefficients;
	}
	
	// Methods :
	
	public void addTerm(Variable var, Value val) {
		this.coefficients.put(var, val);
	}
	
	public Vector<Value> getTabValue() {
		Vector<Value> tab = new Vector<>();
		tab = (Vector<Value>) this.coefficients.values();
		return tab;
	}
	
}
