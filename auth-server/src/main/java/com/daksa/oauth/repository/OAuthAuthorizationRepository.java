package com.daksa.oauth.repository;

import com.daksa.oauth.domain.OAuthCode;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Dependent
public class OAuthAuthorizationRepository extends JpaRepository<OAuthCode, String> {
	@Inject
	private EntityManager entityManager;

	public OAuthAuthorizationRepository() {
		super(OAuthCode.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
