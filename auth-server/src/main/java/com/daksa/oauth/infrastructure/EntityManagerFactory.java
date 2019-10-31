package com.daksa.oauth.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerFactory {

	@PersistenceContext
	@Produces
	private EntityManager entityManager;
}
