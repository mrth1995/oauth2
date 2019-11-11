package com.daksa.oauth.service;

public interface UserAuthService {
	boolean verifyPassword(String username, String password);
}
