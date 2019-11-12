package com.daksa.oauth.model;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestAccessToken {
	@FormParam("client_id")
	private String clientId;
	@FormParam("code")
	private String code;
	@FormParam("code_verifier")
	private String codeVerifier;
	@FormParam("code_challenge_method")
	private String codeChallengeMethod;

	public String getClientId() {
		return clientId;
	}

	public String getCode() {
		return code;
	}

	public String getCodeVerifier() {
		return codeVerifier;
	}

	public String getCodeChallengeMethod() {
		return codeChallengeMethod;
	}
}
