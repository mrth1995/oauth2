package com.daksa.oauth.exception;

import io.olivia.webutil.exception.RestException;

public class AuthorizationException extends RestException {
	public AuthorizationException(String message) {
		super(400, "63", message);
	}

	public AuthorizationException() {
		this("Unauthorized");
	}
}
