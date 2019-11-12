package com.daksa.oauth.repository;

import com.daksa.oauth.domain.OAuthCode;
import io.olivia.webutil.persistence.JpaRepository;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Dependent
public class OAuthCodeRepository extends JpaRepository<OAuthCode, String> {
	@Inject
	private EntityManager entityManager;

	public OAuthCodeRepository() {
		super(OAuthCode.class);
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public OAuthCode find(String clientId, String code, String codeChallenge) {
		TypedQuery<OAuthCode> query = entityManager.createNamedQuery("OAuthCode.find", OAuthCode.class);
		query.setParameter("clientId", clientId);
		query.setParameter("code", code);
		query.setParameter("codeChallenge", codeChallenge);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
