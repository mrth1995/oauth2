package com.daksa.oauth.domain;

import io.olivia.webutil.IDGen;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "oauth_code")
public class OAuthCode implements Serializable {
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
	@Column(name = "code", nullable = false)
	private String code;

	public OAuthCode() {
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

	public String getCode() {
		return code;
	}

	public static final class Builder {
		private String redirectUri;
		private String clientId;
		private String codeChallenge;
		private String codeChallengeMethod;
		private String code;

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

		public OAuthCode build() {
			OAuthCode oAuthCode = new OAuthCode();
			oAuthCode.codeChallengeMethod = this.codeChallengeMethod;
			oAuthCode.clientId = this.clientId;
			oAuthCode.redirectUri = this.redirectUri;
			oAuthCode.codeChallenge = this.codeChallenge;
			oAuthCode.code = this.code;
			return oAuthCode;
		}
	}
}
