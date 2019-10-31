package com.daksa.oauth.model;

import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthorizeParam {

	@QueryParam("client_id")
	private String clientId;
	@QueryParam("redirect_uri")
	private String redirectUri;
	@QueryParam("response_type")
	private String responseType;
	@QueryParam("code_challenge")
	private String codeChallenge;
	@QueryParam("code_challenge_method")
	private String codeChallengeMethod;

	public String getClientId() {
		return clientId;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public String getResponseType() {
		return responseType;
	}

	public String getCodeChallenge() {
		return codeChallenge;
	}

	public String getCodeChallengeMethod() {
		return codeChallengeMethod;
	}
}
