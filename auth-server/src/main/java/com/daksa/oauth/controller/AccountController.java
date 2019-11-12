package com.daksa.oauth.controller;

import com.daksa.oauth.domain.Account;
import com.daksa.oauth.exception.AccountNotExistException;
import com.daksa.oauth.repository.AccountRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
	public Account findDetail(@QueryParam("id") String accountId) throws AccountNotExistException {
		Account account = accountRepository.find(accountId);
		if (account == null) {
			throw new AccountNotExistException();
		}
		return account;
	}
}
