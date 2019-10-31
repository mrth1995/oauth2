package com.daksa.oauth.service;

import com.daksa.oauth.model.AuthorizeParam;
import com.daksa.oauth.domain.OAuthAuthorization;

public interface OAuthService {
	OAuthAuthorization createAuthorization(AuthorizeParam authorizeParam);
}
