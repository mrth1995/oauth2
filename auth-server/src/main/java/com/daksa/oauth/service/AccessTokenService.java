package com.daksa.oauth.service;

import com.daksa.oauth.domain.OAuthAccessToken;
import com.daksa.oauth.exception.AuthenticationException;
import com.daksa.oauth.exception.AuthorizationException;
import com.daksa.oauth.exception.InvalidAuthCodeException;
import com.daksa.oauth.exception.InvalidEncryptionMethodException;

public interface AccessTokenService {
	String createAuthCode(String code);
	OAuthAccessToken requestTokenAuthCode(String clientId, String code, String codeVerifier, String codeChallengeMethod) throws InvalidAuthCodeException, InvalidEncryptionMethodException;
	OAuthAccessToken requestTokenClientCredential(String clientId, String clientSecret) throws AuthorizationException;
	OAuthAccessToken requestTokenRefreshToken(String clientId, String clientSecret, String refreshToken) throws AuthenticationException;
	String getClientId(String accesToken);
}
