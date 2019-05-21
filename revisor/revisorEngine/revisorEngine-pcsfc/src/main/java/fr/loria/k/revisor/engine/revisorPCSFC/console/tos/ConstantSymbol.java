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

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.getSymbolName() + ": " + this.getVariableType());
		str.append(" -- value: " + this.getValueOfConstant());
		return str.toString();
	}
	
}
