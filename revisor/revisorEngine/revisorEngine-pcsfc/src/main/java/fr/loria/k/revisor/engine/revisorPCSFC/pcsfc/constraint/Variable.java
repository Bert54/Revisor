package fr.loria.k.revisor.engine.revisorPCSFC.pcsfc.constraint;

public abstract class Variable {

	protected String name;
	
	protected Variable(String n) {
		this.name = n;
	}
	
	public String toString() {
		return this.name;
	}

}
