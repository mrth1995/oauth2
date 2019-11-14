package com.daksa.oauth.service;

import com.daksa.oauth.domain.OAuthAccessToken;
import com.daksa.oauth.domain.OAuthCode;
import com.daksa.oauth.domain.OauthClient;
import com.daksa.oauth.exception.AuthenticationException;
import com.daksa.oauth.exception.AuthorizationException;
import com.daksa.oauth.exception.InvalidAuthCodeException;
import com.daksa.oauth.exception.InvalidEncryptionMethodException;
import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.repository.AccessTokenRepository;
import com.daksa.oauth.repository.ClientRepository;
import com.daksa.oauth.repository.OAuthCodeRepository;
import io.olivia.webutil.DateTimeUtil;
import io.olivia.webutil.IDGen;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Base64;
import java.util.Date;

@Dependent
public class AccessTokenServiceImpl implements AccessTokenService {
	private static final Logger LOG = LoggerFactory.getLogger(AccessTokenService.class);

	@Inject
	private ClientRepository clientRepository;
	@Inject
	private OAuthCodeRepository oAuthCodeRepository;
	@Inject
	private AccessTokenRepository accessTokenRepository;
	@Inject
	private DateTimeUtil dateTimeUtil;

	private String createAccessToken(String clientId) {
		String uuid = IDGen.generate();
		String random = RandomStringUtils.randomAlphanumeric(32);
		String tokenRaw = clientId + ":01:" + uuid + random;
		return Base64.getEncoder().encodeToString(tokenRaw.getBytes());
	}

	@Override
	public String createAuthCode(String code) {
		String uuid = IDGen.generate();
		String random = RandomStringUtils.randomAlphanumeric(32);
		String tokenRaw = code + ":00:" + uuid + random;
		return Base64.getEncoder().encodeToString(tokenRaw.getBytes());
	}

	@Override
	public OAuthAccessToken requestTokenAuthCode(String clientId, String code, String codeVerifier, String codeChallengeMethod) throws InvalidAuthCodeException, InvalidEncryptionMethodException {
		if (!codeChallengeMethod.equals("SHA256")) {
			throw new InvalidEncryptionMethodException();
		}
		OauthClient client = clientRepository.find(clientId);
		String codeChallenge = HmacUtils.hmacSha256Hex(client.getSecret(), codeVerifier);
		LOG.debug("code challenge: {}", codeChallenge);
		OAuthCode oauthCode = oAuthCodeRepository.find(clientId, code, codeChallenge);
		if (oauthCode == null) {
			throw new InvalidAuthCodeException();
		}
		if (oauthCode.valid(new Date())) {
			oauthCode.invalidate();
			oAuthCodeRepository.update(oauthCode);
			OAuthAccessToken accessToken = new OAuthAccessToken.Builder()
					.client(client)
					.createdTimestamp(new Date(), Constants.CODE_EXPIRY_SECOND)
					.accessToken(createAccessToken(clientId))
					.refreshToken(createAccessToken(clientId))
					.tokenType(Constants.TOKEN_TYPE_BEARER)
					.grantType(Constants.GRANT_TYPE_AUTH_CODE)
					.build();
			accessTokenRepository.store(accessToken);
			return accessToken;
		} else {
			throw new InvalidAuthCodeException();
		}
	}

	@Override
	public OAuthAccessToken requestTokenClientCredential(String clientId, String clientSecret) throws AuthorizationException {
		OauthClient client = clientRepository.find(clientId);
		if (client == null) {
			throw new AuthorizationException();
		}
		if (!client.getSecret().equals(clientSecret)) {
			throw new AuthorizationException();
		}
		OAuthAccessToken accessToken = new OAuthAccessToken.Builder()
				.client(client)
				.createdTimestamp(new Date(), Constants.CODE_EXPIRY_SECOND)
				.accessToken(createAccessToken(clientId))
				.refreshToken(createAccessToken(clientId))
				.tokenType(Constants.TOKEN_TYPE_BEARER)
				.grantType(Constants.GRANT_TYPE_CLIENT_CREDENTIAL)
				.build();
		accessTokenRepository.store(accessToken);
		return accessToken;
	}

	@Override
	public OAuthAccessToken requestTokenRefreshToken(String clientId, String clientSecret, String refreshToken) throws AuthenticationException {
		OauthClient client = clientRepository.find(clientId);
		Date timestamp = dateTimeUtil.now();

		// Verify parameters
		if (!client.getSecret().equals(clientSecret)) {
			throw new AuthenticationException("Wrong client_secret");
		}
		OAuthAccessToken oauthToken = accessTokenRepository.findByRefreshToken(refreshToken);
		// Refresh access token
		String accessToken = createAccessToken(clientId);

		oauthToken.setAccessToken(accessToken);
		oauthToken.setLastAccessTimestamp(timestamp);
		oauthToken.refresh(timestamp, Constants.OAUTH_ACCESS_TOKEN_EXPIRES);
		accessTokenRepository.update(oauthToken);
		return oauthToken;
	}

	@Override
	public String getClientId(String accessToken) {
		String accessTokenRaw = new String(Base64.getDecoder().decode(accessToken));
		String[] parts = accessTokenRaw.split(":", 2);
		return parts[0];
	}
}
