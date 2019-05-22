package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Value;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

public class LinearConstraint {
	
	// Types :
	
	public enum ConstraintType {
		GEQ,
		LEQ,
		EQUAL;
	}
	
	// Fields :
	
	private HashMap<Variable, Value> pounderedVariables;
	private ConstraintType type;
	private Value offset;
	
	// Constructors :
	
	public LinearConstraint() {
		this.pounderedVariables = new HashMap<>();
	}
	
	// Getters :
	
	public HashMap<Variable, Value> getPounderedVariables() {
		return this.pounderedVariables;
	}
	
	public ConstraintType getType() {
		return this.type;
	}
	
	public Value getOffset() {
		return this.offset;
	}
	
	// Setters :
	
	public void setType(ConstraintType t) {
		this.type = t;
	}
	
	public void setOffset(Value v) {
		this.offset = v;
	}
	
	// Methods :
	
	public void addTerm(Variable var, Value v) {
		this.pounderedVariables.put(var, v);
	}
	
	public boolean existVariable(Variable v) {
		return getPounderedVariables().containsKey(v);
	}
	
	public String getValueOnPounderedVariableWithVariable() {
		return getOffset().toString();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Set<Variable> listkey = getPounderedVariables().keySet();
		
		for(Variable variable : listkey) {
			Value v = getPounderedVariables().get(variable);
			sb.append(v.toString() + "*");
			sb.append(variable.getName() + " + ");
		}
		
		// on efface le dernier +
		if(sb.lastIndexOf("+") >= 0) {
			sb.deleteCharAt(sb.lastIndexOf("+"));
		}
		
		switch(getType()) {
			case EQUAL:
				sb.append(" = ");
				break;
			case GEQ:
				sb.append(" >= ");
				break;
			case LEQ:
				sb.append(" <= ");
				break;
		}
		
		// sb.append(getType().toString());
		sb.append(getOffset());// .getValueDouble());
		return sb.toString();
	}
	
	/**
	 * Changes the variables to their image by the map reformulation used to change space.
	 */
	public LinearConstraint transcribe(HashMap<Variable, Variable> reformulation) {
		LinearConstraint result = new LinearConstraint();
		result.setOffset(getOffset());
		result.setType(getType());
		Iterator<Map.Entry<Variable, Value>> e = this.pounderedVariables.entrySet().iterator();
		Map.Entry<Variable, Value> temp;
		
		while(e.hasNext()) {
			temp = e.next();
			result.addTerm(reformulation.get(temp.getKey()), temp.getValue());
		}
		
		return result;
	}
	
	//	public LinearConstraint restrictTo(LinearizableAttributeValueMetricSpace subSpace) {
	//		// TODO Auto-generated method stub
	//		throw new UnsupportedOperationException("Not yet implemented");
	//	}
	
}
