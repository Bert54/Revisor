package fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import fr.loria.orpailleur.revisor.engine.revisorCLC.linearProgramming.com.LinearConstraint.ConstraintType;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.AttributeValueSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.LinearizableAttributeValueMetricSpace;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Value;
import fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace.Variable;

public class Simplex {
	
	// Fields :
	
	private Vector<LinearConstraint> constraints;
	
	// Constructors :
	
	public Simplex() {
		this.constraints = new Vector<>();
	}
	
	public Simplex(Vector<LinearConstraint> v) {
		this.constraints = v;
	}
	
	public Simplex(AttributeValueSpace espace) {
		this();
		// is an attribute representationSpace needed in this class ?
	}
	
	/**
	 * Creates a singleton Simplex.
	 */
	public Simplex(Map<Variable, Value> m) {
		this();
		Iterator<Map.Entry<Variable, Value>> it = m.entrySet().iterator();
		Map.Entry<Variable, Value> entry;
		LinearConstraint lc;
		
		// for each variable a new constraint making it equal to the
		// corresponding value in m is added to solution
		while(it.hasNext()) {
			entry = it.next();
			lc = new LinearConstraint();
			lc.setOffset(entry.getValue());
			lc.setType(ConstraintType.EQUAL);
			this.constraints.add(lc);
		}
	}
	
	// Getters :
	
	public Vector<LinearConstraint> getConstraints() {
		return this.constraints;
	}
	
	// Methods :
	
	public void addConstraint(LinearConstraint constraint) {
		this.constraints.add(constraint);
	}
	
	public Simplex intersection(Simplex srceSimplex) {
		for(LinearConstraint l : srceSimplex.getConstraints()) {
			getConstraints().add(l);
		}
		
		// return new Simplex(getConstraints());
		return this;
	}
	
	/**
	 * Makes a projection of the Simplex on subSpace.
	 * Not yet implemented.
	 */
	public Simplex restrictTo(LinearizableAttributeValueMetricSpace subSpace) {
		//		Simplex result = new Simplex();
		//		Enumeration<LinearConstraint> e = this.constraints.elements();
		//		
		//		while(e.hasMoreElements()) {
		//			result.addConstraint(e.nextElement().restrictTo(subSpace));
		//		}
		//		
		//		return result;
		
		// previous method don't work
		// (eg. if there are 2 constraints x<=z and z<=y,
		// the projection on the subspace (x,y) cannot be done on the
		// constraints separately)
		
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/**
	 * used to change the space, reformulation maps the previous variables to
	 * the new ones
	 */
	public Simplex transcribe(HashMap<Variable, Variable> reformulation) {
		Simplex result = new Simplex();
		Enumeration<LinearConstraint> e = this.constraints.elements();
		
		while(e.hasMoreElements()) {
			result.addConstraint(e.nextElement().transcribe(reformulation));
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for(LinearConstraint lc : getConstraints()) {
			sb.append(lc.toString() + "\n");
		}
		
		return sb.toString();
	}
	
}
