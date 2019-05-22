package fr.loria.orpailleur.revisor.webplatform.exception;

public class PersistenceException extends Exception {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Methods :
	
	public PersistenceException() {
		super();
	}
	
	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(Throwable throwable) {
		super(throwable);
	}
	
	public PersistenceException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
