package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

public class IntegerVariable extends Variable {
	
	// Constructors :
	
	public IntegerVariable(String noun) {
		super(noun);
	}
	
	// Methods :
	
	@Override
	public Variable duplicate(String noun) {
		return new IntegerVariable(noun);
	}
	
}
