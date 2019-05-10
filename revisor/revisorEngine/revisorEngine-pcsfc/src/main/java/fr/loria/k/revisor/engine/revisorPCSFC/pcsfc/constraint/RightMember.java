package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;

public class RightMember<T> {

	private T numberType;
	
	@SuppressWarnings("unchecked")
	public RightMember(fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.RightMember<Double> rm) {
		this.numberType = (T) rm.getNumber();
	}
	
	public String toString() {
		if ((double)numberType % 1 != 0) {
			return this.numberType.toString();
		}
		int var = ((Double) this.numberType).intValue();
		return "" + var;
	}
	
}
