package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

import java.util.ArrayList;

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
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.getSymbolName() + ": " + this.getVariableType());
		str.append(" -- modalities: ");
		int i = 0;
		while (i != this.modalities.size() - 1) {
			str.append(this.modalities.get(i) + ", ");
			i++;
		}
		str.append(this.modalities.get(i));
		return str.toString();
	}
	
}
