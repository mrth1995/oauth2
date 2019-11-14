package com.daksa.oauth.service;

import com.daksa.oauth.exception.AccountNotExistException;

public interface UserAuthService {
	boolean verifyPassword(String username, String password) throws AccountNotExistException;
}
