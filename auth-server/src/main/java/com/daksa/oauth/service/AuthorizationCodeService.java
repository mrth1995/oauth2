package com.daksa.oauth.service;

import com.daksa.oauth.model.AuthorizeParam;
import com.daksa.oauth.domain.OAuthCode;

public interface AuthorizationCodeService {
	OAuthCode createAuthorization(AuthorizeParam authorizeParam);
}
