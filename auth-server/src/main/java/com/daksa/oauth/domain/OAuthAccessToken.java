package com.daksa.oauth.domain;

import io.olivia.webutil.IDGen;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "access_token")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OAuthAccessToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", nullable = false)
	private OauthClient client;
	@Column(name = "client_id", insertable = false, updatable = false)
	private String clientId;
	@Column(name = "access_token", nullable = false)
	private String accessToken;
	@Column(name = "refresh_token", nullable = false)
	private String refreshToken;
	@Column(name = "created_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTimestamp;
	@Column(name = "last_access_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastAccessTimestamp;
	@Column(name = "expiry_timestamp", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryTimestamp;
	@Column(name = "token_type")
	private String tokenType;
	@Column(name = "grant_type")
	private String grantType;

	public OAuthAccessToken() {
		this.id = IDGen.generate();
	}

	public static class Builder {
		OAuthAccessToken accessToken;

		public Builder() {
			accessToken = new OAuthAccessToken();
		}

		public Builder client(OauthClient client) {
			accessToken.client = client;
			return this;
		}

		public Builder accessToken(String accessToken) {
			this.accessToken.accessToken = accessToken;
			return this;
		}

		public Builder refreshToken(String refreshToken) {
			this.accessToken.refreshToken = refreshToken;
			return this;
		}

		public Builder createdTimestamp(Date createdTimestamp, int expirySecond) {
			accessToken.createdTimestamp= createdTimestamp;
			accessToken.lastAccessTimestamp = createdTimestamp;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(createdTimestamp);
			calendar.add(Calendar.SECOND, expirySecond);
			accessToken.expiryTimestamp = calendar.getTime();
			return this;
		}

		public Builder grantType(String grantType) {
			accessToken.grantType = grantType;
			return this;
		}

		public Builder tokenType(String tokenType) {
			accessToken.tokenType = tokenType;
			return this;
		}

		public OAuthAccessToken build() {
			return accessToken;
		}
	}

	public boolean valid(Date now) {
		return now.before(expiryTimestamp);
	}

	public String getId() {
		return id;
	}

	public String getClientId() {
		return clientId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public Date getLastAccessTimestamp() {
		return lastAccessTimestamp;
	}

	public Date getExpiryTimestamp() {
		return expiryTimestamp;
	}

	public String getTokenType() {
		return tokenType;
	}

	public String getGrantType() {
		return grantType;
	}
}
