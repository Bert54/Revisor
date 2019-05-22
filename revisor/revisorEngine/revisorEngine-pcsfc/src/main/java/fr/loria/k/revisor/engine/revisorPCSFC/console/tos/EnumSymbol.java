package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

import java.util.ArrayList;

import fr.loria.k.revisor.engine.revisorPCSFC.RevisorEnginePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.AbstractRevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.RevisorConsolePCSFC;
import fr.loria.k.revisor.engine.revisorPCSFC.console.config.PCSFCConfig;
import fr.loria.orpailleur.revisor.engine.core.console.instruction.Instruction;

public class EnumSymbol extends Symbol {

	private final ArrayList<String> modalities;
	
	public EnumSymbol(String n, VariableType t, ArrayList<String> mods) {
		super(n, t);
		this.modalities = mods;
	}
	
	public ArrayList<String> getModalities() {
		return this.modalities;
	}

	@Override
	public String toString(final boolean latex, AbstractRevisorConsolePCSFC<RevisorConsolePCSFC, RevisorEnginePCSFC, PCSFCConfig, Instruction<RevisorConsolePCSFC>> console) {
		StringBuilder str = new StringBuilder();
		str.append(this.getSymbolName() + ": " + this.getVariableType());
		if (console.displayVariableContent()) {
			str.append(" -- modalities: ");
			int i = 0;
			while (i != this.modalities.size() - 1) {
				str.append(this.modalities.get(i) + ", ");
				i++;
			}
			str.append(this.modalities.get(i));
		}
		return str.toString();
	}
	
}
