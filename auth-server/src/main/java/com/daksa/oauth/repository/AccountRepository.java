package com.daksa.oauth.repository;

import com.daksa.oauth.domain.Account;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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

	public Account findByUsername(String username) {
		TypedQuery<Account> query = entityManager.createNamedQuery("Account.findByUsername", Account.class);
		query.setParameter("username", username);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
