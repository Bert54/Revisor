package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorEnginePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.RevisorPCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.RevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.config.PCSFCConfig;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;

public class Symbol implements Comparable<Symbol> {

	private final String name;
	private final VariableType type;
	
	public Symbol(final String n, final VariableType t) {
		this.name = n;
		this.type = t;
	}

	public String getSymbolName() {
		return this.name;
	}
	
	public VariableType getVariableType() {
		return this.type;
	}
	
	public String toString(final boolean latex, AbstractRevisorConsolePCSFC<RevisorConsolePCSFC, RevisorEnginePCSFC, PCSFCConfig, Instruction<RevisorConsolePCSFC>> console) {
		StringBuilder str = new StringBuilder();
		if (latex) {
			str.append(RevisorPCSFC.formatNameToLatex(this.getSymbolName()) + ": " + this.getVariableType());
		}
		else {
			str.append(this.getSymbolName() + ": " + this.getVariableType());
		}
		return str.toString();
	}

	@Override
	public int compareTo(Symbol o) {
		return this.getSymbolName().compareTo(o.getSymbolName());
	}
	
}
