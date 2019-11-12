package com.daksa.oauth.service;

public interface AccessTokenService {
	String createAccessToken(String code);
	String createAuthCode(String code);
}
