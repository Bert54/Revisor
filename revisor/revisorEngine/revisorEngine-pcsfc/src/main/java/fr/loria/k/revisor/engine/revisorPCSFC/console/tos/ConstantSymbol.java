package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorEnginePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.RevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.config.PCSFCConfig;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;

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
	public String toString(final boolean latex, AbstractRevisorConsolePCSFC<RevisorConsolePCSFC, RevisorEnginePCSFC, PCSFCConfig, Instruction<RevisorConsolePCSFC>> console) {
		StringBuilder str = new StringBuilder();
		str.append(this.getSymbolName() + ": " + this.getVariableType());
		if (console.displayVariableContent()) {
			str.append(" -- value: " + this.getValueOfConstant());
		}
		return str.toString();
	}
	
}
