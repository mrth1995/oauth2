package com.daksa.oauth.model;

import io.olivia.webutil.json.JsonTimestampAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AccessTokenModel {
	private String accessToken;
	private String refreshToken;
	@XmlJavaTypeAdapter(JsonTimestampAdapter.class)
	private Date expiryTimestamp;
	private String grantType;
	private String tokenType;

	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getGrantType() {
		return grantType;
	}

	public String getTokenType() {
		return tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Date getExpiryTimestamp() {
		return expiryTimestamp;
	}

	public void setExpiryTimestamp(Date expiryTimestamp) {
		this.expiryTimestamp = expiryTimestamp;
	}
}
