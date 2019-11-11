package com.daksa.oauth.domain;

import io.olivia.webutil.IDGen;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "oauth_code")
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
	@Column(name = "code_challenge_method", nullable = false)
	private String codeChallengeMethod;
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
		this.status = CodeStatus.CREATED;
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

	public String getCodeChallengeMethod() {
		return codeChallengeMethod;
	}

	public String getCode() {
		return code;
	}

	public boolean valid(Date now) {
		return expiryTimestamp.after(now) && status.equals(CodeStatus.CREATED);
	}

	public void markAsUsed() {
		this.status = CodeStatus.USED;
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
			oAuthCode.codeChallengeMethod = this.codeChallengeMethod;
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
