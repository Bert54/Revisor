package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

public abstract class Value {
	
	// Constants :
	
	public static final int nbDecimalRoud = 2;
	
	// Methods :
	
	public static double roundToDecimal(double val, int nbdecimals) {
		double threshold = Math.pow(10, nbdecimals);
		long temp = Math.round(val * threshold);
		return temp / threshold;
	}
	
	//public abstract float getValueFloat();
	
	public abstract double getValueDouble();
	
	public abstract int getValueInt();
	
	@Override
	public String toString() {
		return roundToDecimal(this.getValueDouble(), nbDecimalRoud) + "";
	}
	
}
