package com.daksa.oauth.exception;

import io.olivia.webutil.exception.RestException;

public class InvalidAuthCodeException extends RestException {
	private static final long serialVersionUID = 1L;

	public InvalidAuthCodeException(String message) {
		super(400, "63", message);
	}

	public InvalidAuthCodeException() {
		this("Invalid auth code");
	}
}
