package com.daksa.oauth.exception;

import io.olivia.webutil.exception.RestException;

public class InvalidGrantTypeException extends RestException {
	public InvalidGrantTypeException(String message) {
		super(400, "63", message);
	}

	public InvalidGrantTypeException() {
		this("Invalid grant type");
	}
}
