package com.daksa.oauth.service;

import com.daksa.oauth.domain.Account;
import com.daksa.oauth.exception.AccountNotExistException;
import com.daksa.oauth.repository.AccountRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class UserAuthServiceImpl implements UserAuthService {

	@Inject
	private AccountRepository accountRepository;

	@Override
	public boolean verifyPassword(String username, String password) throws AccountNotExistException {
		Account account = accountRepository.findByUsername(username);
		if (account == null) {
			throw new AccountNotExistException();
		}
		return account.verifyPassword(password);
	}
}
