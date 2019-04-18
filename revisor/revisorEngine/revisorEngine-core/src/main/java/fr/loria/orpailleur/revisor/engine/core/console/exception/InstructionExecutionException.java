package fr.loria.orpailleur.revisor.engine.core.console.exception;

/**
 * @author William Philbert
 */
public class InstructionExecutionException extends InstructionException {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Constructors :
	
	public InstructionExecutionException() {
		super();
	}
	
	public InstructionExecutionException(String message) {
		super(message);
	}
	
	public InstructionExecutionException(Throwable throwable) {
		super(throwable);
	}
	
	public InstructionExecutionException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
