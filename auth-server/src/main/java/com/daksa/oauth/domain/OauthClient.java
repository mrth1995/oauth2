package com.daksa.oauth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "client")
public class OauthClient implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 32)
	private String id;
	@Column(length = 32, nullable = false)
	private String name;
	@Column(length = 32, nullable = false)
	private String secret;

	protected OauthClient() {
	}

	public OauthClient(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getSecret() {
		return secret;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof OauthClient)) return false;
		OauthClient that = (OauthClient) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
