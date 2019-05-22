package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

public class LinearizableAttributeValueMetricSpace extends AttributeValueMetricSpace {
	
	// Fields :
	
	private LinearizableDistance distance;
	
	// Constructors :
	
	public LinearizableAttributeValueMetricSpace() {
		this(new LinearizableDistance());
	}
	
	public LinearizableAttributeValueMetricSpace(LinearizableDistance d) {
		super();
		this.distance = d;
	}
	
	// Getters :
	
	public LinearizableDistance getDistance() {
		return this.distance;
	}
	
	// Methods :
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		Value val;
		
		for(Variable var : getVariables()) {
			sb.append("\t" + var.toString());
			val = this.distance.getCoef(var);
			
			if(val == null) {
				sb.append(":0\n");
			}
			else {
				sb.append(":" + val + "\n");
			}
		}
		
		sb.append("}");
		return sb.toString();
	}
	
}
