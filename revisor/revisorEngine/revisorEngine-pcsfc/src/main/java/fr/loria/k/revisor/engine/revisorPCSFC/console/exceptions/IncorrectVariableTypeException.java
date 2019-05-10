package fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions;

public class IncorrectVariableTypeException extends Exception {

	public IncorrectVariableTypeException() {
	}

	public IncorrectVariableTypeException(String message) {
		super(message);
	}

	public IncorrectVariableTypeException(Throwable cause) {
		super(cause);
	}

	public IncorrectVariableTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public IncorrectVariableTypeException(String message, Throwable cause, boolean enableSuppression,
	boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
