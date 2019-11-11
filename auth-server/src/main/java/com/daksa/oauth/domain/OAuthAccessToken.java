package com.daksa.oauth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "access_token")
public class OAuthAccessToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	public String id;
}
