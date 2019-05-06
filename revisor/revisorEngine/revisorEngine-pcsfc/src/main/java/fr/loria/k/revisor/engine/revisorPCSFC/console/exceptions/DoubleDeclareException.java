package fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions;

public class DoubleDeclareException extends Exception {

	public DoubleDeclareException() {
	}

	public DoubleDeclareException(String message) {
		super(message);
	}

	public DoubleDeclareException(Throwable cause) {
		super(cause);
	}

	public DoubleDeclareException(String message, Throwable cause) {
		super(message, cause);
	}

	public DoubleDeclareException(String message, Throwable cause, boolean enableSuppression,
	boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
