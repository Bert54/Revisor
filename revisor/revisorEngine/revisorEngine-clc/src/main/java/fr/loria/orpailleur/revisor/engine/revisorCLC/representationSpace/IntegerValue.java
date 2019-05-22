package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

public class IntegerValue extends Value {
	
	// Fields :
	
	private int value;
	
	// Constructors :
	
	public IntegerValue(int n) {
		this.value = n;
	}
	
	// Setters :
	
	public void setValue(int value) {
		this.value = value;
	}
	
	// Methods :
	
	@Override
	public int getValueInt() {
		return this.value;
	}
	
	@Override
	public double getValueDouble() {
		return getValueInt();
	}
	
}
