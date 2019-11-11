package com.daksa.oauth.service;

import javax.enterprise.context.Dependent;

@Dependent
public class UserAuthServiceImpl implements UserAuthService {
	@Override
	public boolean verifyPassword(String username, String password) {
		return username.equals("user") && password.equals("Password1");
	}
}
