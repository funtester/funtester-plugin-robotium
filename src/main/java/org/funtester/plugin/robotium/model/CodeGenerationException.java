package org.funtester.plugin.robotium.model;

public class CodeGenerationException extends Exception {

	private static final long serialVersionUID = -5038668425381532094L;

	public CodeGenerationException() {
		super();
	}

	public CodeGenerationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super( message, cause, enableSuppression, writableStackTrace );
	}

	public CodeGenerationException(String message, Throwable cause) {
		super( message, cause );
	}

	public CodeGenerationException(String message) {
		super( message );
	}

	public CodeGenerationException(Throwable cause) {
		super( cause );
	}

}
