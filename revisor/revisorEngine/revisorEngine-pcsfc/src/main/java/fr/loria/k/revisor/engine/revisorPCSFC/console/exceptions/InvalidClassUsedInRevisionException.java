package fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions;

public class InvalidClassUsedInRevisionException extends Exception {

	public InvalidClassUsedInRevisionException() {
	}

	public InvalidClassUsedInRevisionException(String message) {
		super(message);
	}

	public InvalidClassUsedInRevisionException(Throwable cause) {
		super(cause);
	}

	public InvalidClassUsedInRevisionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidClassUsedInRevisionException(String message, Throwable cause, boolean enableSuppression,
	boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
