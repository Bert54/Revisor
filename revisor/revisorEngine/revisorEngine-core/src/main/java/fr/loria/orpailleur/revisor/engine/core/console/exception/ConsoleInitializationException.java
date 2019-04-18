package fr.loria.orpailleur.revisor.engine.core.console.exception;

/**
 * @author William Philbert
 */
public class ConsoleInitializationException extends Exception {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public ConsoleInitializationException() {
		super();
	}
	
	public ConsoleInitializationException(String message) {
		super(message);
	}
	
	public ConsoleInitializationException(Throwable throwable) {
		super(throwable);
	}
	
	public ConsoleInitializationException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
