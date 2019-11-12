package com.daksa.oauth.service;

import com.daksa.oauth.domain.OAuthAccessToken;
import com.daksa.oauth.exception.AuthorizationException;
import com.daksa.oauth.exception.InvalidAuthCodeException;

public interface AccessTokenService {
	String createAuthCode(String code);
	OAuthAccessToken requestTokenAuthCode(String clientId, String code, String codeVerifier) throws InvalidAuthCodeException;
	OAuthAccessToken requestTokenClientCredential(String clientId, String clientSecret) throws AuthorizationException;
	OAuthAccessToken requestTokenRefreshToken(String clientId, String clientSecret, String refreshToken);
}
