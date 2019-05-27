package fr.loria.orpailleur.revisor.engine.core.console.exception;

/**
 * @author William Philbert
 */
public class InstructionValidationException extends InstructionException {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public InstructionValidationException() {
		super();
	}
	
	public InstructionValidationException(String message) {
		super(message);
	}
	
	public InstructionValidationException(Throwable throwable) {
		super(throwable);
	}
	
	public InstructionValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public InstructionValidationException(String message, Throwable cause, boolean enableSuppression,
	boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
