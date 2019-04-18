package fr.loria.orpailleur.revisor.engine.core.console.exception;

/**
 * @author William Philbert
 */
public class FormulaValidationException extends InstructionValidationException {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public FormulaValidationException() {
		super();
	}
	
	public FormulaValidationException(String message) {
		super(message);
	}
	
	public FormulaValidationException(Throwable throwable) {
		super(throwable);
	}
	
	public FormulaValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
