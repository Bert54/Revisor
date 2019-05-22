package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class LinearizableDistance extends Distance {
	
	// Fields :
	
	private HashMap<Variable, Value> coefficients;
	
	// Constructors :
	
	public LinearizableDistance() {
		this.coefficients = new HashMap<>();
	}
	
	// Methods :
	
	public void addTerm(Variable var, Value val) {
		this.coefficients.put(var, val);
	}
	
	public Value getCoef(Variable var) {
		return this.coefficients.get(var);
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Set<Entry<Variable, Value>> set = this.coefficients.entrySet();
		buffer.append("Sum(\n");
		
		for(Entry<Variable, Value> entry : set) {
			buffer.append("\t" + entry.getValue() + " * #" + entry.getKey() + "\n");
		}
		
		buffer.append(")\n");
		return buffer.toString();
	}
	
}
