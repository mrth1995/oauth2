package com.daksa.oauth.infrastructure;

public class Constants {
	public static final String AUTH_PAGE ="oauth/authorize";
	public static final String SUCCESS_PAGE ="oauth/success";
	public static final int CODE_EXPIRY_SECOND = 86400;
	public static final String GRANT_TYPE_AUTH_CODE = "authorization_code";
	public static final String GRANT_TYPE_CLIENT_CREDENTIAL = "client_credentials";
	public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	public static String TOKEN_TYPE_BEARER = "Bearer";
	public static String RESPONSE_CODE_HEADER = "RC";
}
