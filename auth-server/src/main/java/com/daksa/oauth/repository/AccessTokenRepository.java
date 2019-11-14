package com.daksa.oauth.repository;

import com.daksa.oauth.domain.OAuthAccessToken;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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

	public OAuthAccessToken finByAccessToken(String accessToken) {
		TypedQuery<OAuthAccessToken> query = entityManager.createNamedQuery("OAuthAccessToken.findByAccessToken", OAuthAccessToken.class);
		query.setParameter("accessToken", accessToken);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public OAuthAccessToken findByRefreshToken(String refreshToken) {
		TypedQuery<OAuthAccessToken> query = entityManager.createNamedQuery("OAuthAccessToken.findByRefreshToken", OAuthAccessToken.class);
		query.setParameter("refreshToken", refreshToken);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
