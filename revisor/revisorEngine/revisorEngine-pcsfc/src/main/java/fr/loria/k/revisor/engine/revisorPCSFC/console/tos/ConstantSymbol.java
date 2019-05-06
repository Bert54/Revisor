package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

public class ConstantSymbol extends Symbol {

	private double constantValue;
	
	public ConstantSymbol(String n, VariableType t, double value) {
		super(n, t);
		this.constantValue = value;
	}
	
	public double getValueOfConstant() {
		return this.constantValue;
	}

}
