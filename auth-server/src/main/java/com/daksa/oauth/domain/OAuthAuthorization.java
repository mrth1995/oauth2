package com.daksa.oauth.domain;

import io.olivia.webutil.IDGen;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "oauth_authorization")
public class OAuthAuthorization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 32)
	private String id;
	@Column(name = "redirect_uri", nullable = false)
	private String redirectUri;
	@Column(name = "client_id", nullable = false)
	private String clientId;
	@Column(name = "code_challenge", nullable = false)
	private String codeChallenge;
	@Column(name = "code_challenge_method", nullable = false)
	private String codeChallengeMethod;

	public OAuthAuthorization() {
		this.id = IDGen.generate();
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

	public static final class Builder {
		private String redirectUri;
		private String clientId;
		private String codeChallenge;
		private String codeChallengeMethod;

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

		public OAuthAuthorization build() {
			OAuthAuthorization oAuthAuthorization = new OAuthAuthorization();
			oAuthAuthorization.codeChallengeMethod = this.codeChallengeMethod;
			oAuthAuthorization.clientId = this.clientId;
			oAuthAuthorization.redirectUri = this.redirectUri;
			oAuthAuthorization.codeChallenge = this.codeChallenge;
			return oAuthAuthorization;
		}
	}
}
