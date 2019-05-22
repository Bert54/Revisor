package fr.loria.orpailleur.revisor.webplatform.exception;

public class ValidationException extends Exception {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Methods :
	
	public ValidationException() {
		super();
	}
	
	public ValidationException(String message) {
		super(message);
	}
	
	public ValidationException(Throwable throwable) {
		super(throwable);
	}
	
	public ValidationException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
