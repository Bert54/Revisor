package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

import java.util.Vector;

public class AttributeValueSpace {
	
	// Fields :
	
	private Vector<Variable> variables;
	
	// Constructors :
	
	public AttributeValueSpace() {
		this.variables = new Vector<>();
	}
	
	public AttributeValueSpace(Vector<Variable> space) {
		this.variables = new Vector<>(space);
	}
	
	public AttributeValueSpace(LinearizableAttributeValueMetricSpace representationSpace) {
		this(representationSpace.getVariables());
	}
	
	// Getters :
	
	public Vector<Variable> getVariables() {
		return this.variables;
	}
	
	// Methods :
	
	/**
	 * Retrieves a variable fom its name.
	 * @param name - variable name.
	 * @return the Variable object associated with this name, null otherwise.
	 */
	public Variable getVariables(String name) {
		for(Variable v : this.variables) {
			if(v.getName().equals(name)) {
				return v;
			}
		}
		
		return null;
	}
	
	public void addDimention(Variable additionalVariable) {
		getVariables().add(additionalVariable);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		
		for(Variable v : getVariables()) {
			sb.append("\t" + v.toString() + ";\n");
		}
		
		sb.deleteCharAt(sb.lastIndexOf(";"));
		sb.append("}");
		return sb.toString();
	}
	
}
