package com.daksa.oauth.repository;

import com.daksa.oauth.domain.OAuthAccessToken;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Dependent
public class AccessTokenRepository extends JpaRepository<OAuthAccessToken, String> {

	@Inject
	private EntityManager entityManager;

	public AccessTokenRepository() {
		super(OAuthAccessToken.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
