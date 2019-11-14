package com.daksa.oauth.controller;

import com.daksa.oauth.domain.Account;
import com.daksa.oauth.exception.AccountNotExistException;
import com.daksa.oauth.model.AccountRegister;
import com.daksa.oauth.repository.AccountRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

	@Inject
	private AccountRepository accountRepository;

	@GET
	@Path("detail")
	public Account findDetail(@QueryParam("username") String username) throws AccountNotExistException {
		Account account = accountRepository.findByUsername(username);
		if (account == null) {
			throw new AccountNotExistException();
		}
		return account;
	}

	@POST
	@Transactional
	public void registerAccount(AccountRegister param) {
		Account account = new Account();
		account.setName(param.getName());
		account.setPassword(param.getPassword());
		account.setUsername(param.getUsername());
		accountRepository.store(account);
	}
}
