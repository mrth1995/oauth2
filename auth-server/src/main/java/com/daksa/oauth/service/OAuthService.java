package com.daksa.oauth.service;

import com.daksa.oauth.model.AuthorizeParam;
import com.daksa.oauth.domain.OAuthCode;

public interface OAuthService {
	OAuthCode createAuthorization(AuthorizeParam authorizeParam);
}
