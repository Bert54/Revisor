package fr.loria.orpailleur.revisor.engine.core.console.exception;

/**
 * @author William Philbert
 */
public class LexerException extends InstructionValidationException {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public LexerException() {
		super();
	}
	
	public LexerException(String message) {
		super(message);
	}
	
	public LexerException(Throwable throwable) {
		super(throwable);
	}
	
	public LexerException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
