package fr.loria.k.revisor.engine.revisorPCSFC.console.exceptions;

public class VariableNotDeclaredException extends Exception {

	public VariableNotDeclaredException() {
	}

	public VariableNotDeclaredException(String message) {
		super(message);
	}

	public VariableNotDeclaredException(Throwable cause) {
		super(cause);
	}

	public VariableNotDeclaredException(String message, Throwable cause) {
		super(message, cause);
	}

	public VariableNotDeclaredException(String message, Throwable cause, boolean enableSuppression,
	boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
