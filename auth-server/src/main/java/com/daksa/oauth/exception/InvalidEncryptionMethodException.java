package com.daksa.oauth.exception;

import io.olivia.webutil.exception.RestException;

public class InvalidEncryptionMethodException extends RestException {
	private static final long serialVersionUID = 1L;

	public InvalidEncryptionMethodException(String message) {
		super(401, "63", message);
	}

	public InvalidEncryptionMethodException() {
		this("Invalid encryption method");
	}
}
