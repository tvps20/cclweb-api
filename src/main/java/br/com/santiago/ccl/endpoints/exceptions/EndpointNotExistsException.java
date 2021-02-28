package br.com.santiago.ccl.endpoints.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EndpointNotExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EndpointNotExistsException(String msg) {
		super(msg);
	}

	public EndpointNotExistsException(String msg, Throwable cause) {
		super(msg, cause);
	}

}