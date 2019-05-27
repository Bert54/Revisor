package fr.loria.orpailleur.revisor.engine.core.console.exception;

/**
 * @author William Philbert
 */
public class ParserException extends InstructionValidationException {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public ParserException() {
		super();
	}
	
	public ParserException(String message) {
		super(message);
	}
	
	public ParserException(Throwable throwable) {
		super(throwable);
	}
	
	public ParserException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ParserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
				super(message, cause, enableSuppression, writableStackTrace);
			}
	
}
