package com.daksa.oauth.exception;

import io.olivia.webutil.exception.RestException;

public class AuthenticationException extends RestException {

	public AuthenticationException(String message) {
		super(400, "63", message);
	}

	public AuthenticationException() {
		this("Unauthenticated");
	}
}
