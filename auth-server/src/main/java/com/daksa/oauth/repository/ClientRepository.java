package com.daksa.oauth.repository;

import com.daksa.oauth.domain.OauthClient;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Dependent
public class ClientRepository extends JpaRepository<OauthClient, String> {

	@Inject
	private EntityManager entityManager;

	public ClientRepository() {
		super(OauthClient.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
