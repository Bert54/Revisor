package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

public abstract class Variable {
	
	// Fields :
	
	private String name;
	private double valMin;
	private double valMax;
	
	// Constructors :
	
	public Variable(String noun) {
		this.name = noun;
		this.valMax = Double.POSITIVE_INFINITY;
		this.valMin = Double.NEGATIVE_INFINITY;
	}
	
	// Getters :
	
	public String getName() {
		return this.name;
	}
	
	public double getValMin() {
		return this.valMin;
	}
	
	public double getValMax() {
		return this.valMax;
	}
	
	// Setters :
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValMin(double valMin) {
		this.valMin = valMin;
	}
	
	public void setValMax(double valMax) {
		this.valMax = valMax;
	}
	
	// Methods :
	
	public void domaineVariationReal(double vmin, double vmax) {
		setValMax(vmax);
		setValMin(vmin);
	}
	
	public void domaineVariationInt(int vmin, int vmax) {
		domaineVariationReal(vmin, vmax);
	}
	
	abstract public Variable duplicate(String string);
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object arg0) {
		return ((Variable) arg0).getName().equals(this.name);
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
}
