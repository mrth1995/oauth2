package com.daksa.oauth.repository;

import com.daksa.oauth.domain.OAuthAuthorization;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Dependent
public class OAuthAuthorizationRepository extends JpaRepository<OAuthAuthorization, String> {
	@Inject
	private EntityManager entityManager;

	public OAuthAuthorizationRepository() {
		super(OAuthAuthorization.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
