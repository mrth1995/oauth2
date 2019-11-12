package com.daksa.oauth.domain;

import io.olivia.webutil.IDGen;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "oauth_code")
@NamedQueries({
		@NamedQuery(name = "OAuthCode.find", query = "SELECT a FROM OAuthCode a WHERE a.clientId = :clientId AND a.code = :code AND a.codeChallenge = :codeChallenge")
})
public class OAuthCode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 32)
	private String id;
	@Column(name = "redirect_uri")
	private String redirectUri;
	@Column(name = "client_id", nullable = false)
	private String clientId;
	@Column(name = "code_challenge", nullable = false)
	private String codeChallenge;
	@Column(name = "code", nullable = false)
	private String code;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiry_timestamp", nullable = false)
	private Date expiryTimestamp;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_access_timestamp", nullable = false)
	private Date lastAccessTimestamp;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_timestamp", nullable = false)
	private Date createdTimestamp;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private CodeStatus status;

	public OAuthCode() {
		this.id = IDGen.generate();
		this.status = CodeStatus.ACTIVE;
	}

	public String getId() {
		return id;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public String getClientId() {
		return clientId;
	}

	public String getCodeChallenge() {
		return codeChallenge;
	}

	public String getCode() {
		return code;
	}

	public boolean valid(Date now) {
		return expiryTimestamp.after(now) && status.equals(CodeStatus.ACTIVE);
	}

	public void invalidate() {
		this.status = CodeStatus.INACTIVE;
	}

	public void markAsUsed() {
		this.status = CodeStatus.INACTIVE;
	}

	public static final class Builder {
		private String redirectUri;
		private String clientId;
		private String codeChallenge;
		private String codeChallengeMethod;
		private String code;
		private Date createdTimestamp;
		private Date lastAccessTimestamp;
		private Date expiryTimestamp;
		private long expirySeconds;

		public Builder() {
		}

		public Builder redirectUri(String redirectUri) {
			this.redirectUri = redirectUri;
			return this;
		}

		public Builder clientId(String clientId) {
			this.clientId = clientId;
			return this;
		}

		public Builder codeChallenge(String codeChallenge) {
			this.codeChallenge = codeChallenge;
			return this;
		}

		public Builder codeChallengeMethod(String codeChallengeMethod) {
			this.codeChallengeMethod = codeChallengeMethod;
			return this;
		}

		public Builder code(String code) {
			this.code = code;
			return this;
		}

		public Builder createdTimestamp(Date timestamp, int expirySeconds) {
			this.createdTimestamp = timestamp;
			this.lastAccessTimestamp = timestamp;
			this.expirySeconds = expirySeconds;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(timestamp);
			calendar.add(Calendar.SECOND, expirySeconds);
			this.expiryTimestamp = calendar.getTime();
			return this;
		}

		public OAuthCode build() {
			OAuthCode oAuthCode = new OAuthCode();
			oAuthCode.clientId = this.clientId;
			oAuthCode.redirectUri = this.redirectUri;
			oAuthCode.codeChallenge = this.codeChallenge;
			oAuthCode.code = this.code;
			oAuthCode.createdTimestamp = this.createdTimestamp;
			oAuthCode.lastAccessTimestamp = this.lastAccessTimestamp;
			oAuthCode.expiryTimestamp = expiryTimestamp;
			return oAuthCode;
		}
	}
}
