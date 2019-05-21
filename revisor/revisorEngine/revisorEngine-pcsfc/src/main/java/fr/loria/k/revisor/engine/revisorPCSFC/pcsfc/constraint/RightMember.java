package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;

public class RightMember<T> {

	private T numberType;
	
	@SuppressWarnings("unchecked")
	public RightMember(fr.loria.k.revisor.engine.revisorPCSFC.console.formula.constraint.RightMember<Double> rm) {
		this.numberType = (T) rm.getNumber();
	}
	
	@SuppressWarnings("unchecked")
	public RightMember(RightMember<?> rm) {
		this.numberType = (T) rm.getNumber();
	}
	
	public RightMember(T num) {
		this.numberType = (T) num;
	}
	
	public T getNumber() {
		return this.numberType;
	}

	public String toString() {
		if ((double)numberType % 1 != 0) {
			return this.numberType.toString();
		}
		int var = ((Double) this.numberType).intValue();
		return "" + var;
	}
	
}
