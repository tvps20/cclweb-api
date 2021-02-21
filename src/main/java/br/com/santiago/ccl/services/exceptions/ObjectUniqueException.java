package br.com.santiago.ccl.services.exceptions;

public class ObjectUniqueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectUniqueException(String msg) {
		super(msg);
	}

	public ObjectUniqueException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}