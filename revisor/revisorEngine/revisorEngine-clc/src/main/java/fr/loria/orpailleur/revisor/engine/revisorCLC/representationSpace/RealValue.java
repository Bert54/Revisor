package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

public class RealValue extends Value {
	
	// Fields :
	
	private double value;
	
	// Constructors :
	
	public RealValue(double i) {
		this.value = i;
	}
	
	public RealValue(int i) {
		this.value = i;
	}
	
	// Setters :
	
	public void setValue(double value) {
		this.value = value;
	}
	
	// Methods :
	
	@Override
	public double getValueDouble() {
		return this.value;
	}
	
	@Override
	public int getValueInt() {
		return (int) getValueDouble();
	}
	
}
