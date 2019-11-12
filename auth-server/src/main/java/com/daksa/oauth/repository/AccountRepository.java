package com.daksa.oauth.repository;

import com.daksa.oauth.domain.Account;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Dependent
public class AccountRepository extends JpaRepository<Account, String> {

	@Inject
	private EntityManager entityManager;

	public AccountRepository() {
		super(Account.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
