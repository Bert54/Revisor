package fr.loria.k.revisor.engine.revisorPCSFC.console.tos;

public class Symbol {

	private final String name;
	private final VariableType type;
	
	public Symbol(final String n, final VariableType t) {
		this.name = n;
		this.type = t;
	}

	public VariableType getVariableType() {
		return this.type;
	}
	
}
