package fr.loria.orpailleur.revisor.engine.revisorCLC.representationSpace;

public class RealVariable extends Variable {
	
	// Constructors :
	
	public RealVariable(String noun) {
		super(noun);
	}
	
	// Methods :
	
	@Override
	public Variable duplicate(String noun) {
		return new RealVariable(noun);
	}
	
}
