package com.daksa.oauth.service;

import com.daksa.oauth.domain.OauthClient;
import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.model.AuthorizeParam;
import com.daksa.oauth.domain.OAuthCode;
import com.daksa.oauth.repository.ClientRepository;
import com.daksa.oauth.repository.OAuthCodeRepository;
import io.olivia.webutil.IDGen;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;

@Dependent
public class OauthServiceImpl implements OAuthService {

	@Inject
	private ClientRepository clientRepository;
	@Inject
	private OAuthCodeRepository oAuthCodeRepository;

	private static final String SHA256 = "S256";
	private static final String AUTHORIZATION_CODE = "code";

	@Override
	@Transactional
	public OAuthCode createAuthorization(AuthorizeParam authorizeParam) {
		if (validateParam(authorizeParam)) {
			OAuthCode authorization = new OAuthCode.Builder()
					.clientId(authorizeParam.getClientId())
					.codeChallenge(authorizeParam.getCodeChallenge())
					.codeChallengeMethod(authorizeParam.getCodeChallengeMethod())
					.redirectUri(authorizeParam.getRedirectUri())
					.code(IDGen.generate())
					.createdTimestamp(new Date(), Constants.CODE_EXPIRY_SECOND)
					.build();
			oAuthCodeRepository.store(authorization);
			return authorization;
		}
		return null;
	}

	private boolean validateParam(AuthorizeParam authorizeParam) {
		OauthClient client = clientRepository.find(authorizeParam.getClientId());
		return client != null && StringUtils.isNotEmpty(authorizeParam.getCodeChallenge())
				&& StringUtils.isNotEmpty(authorizeParam.getResponseType())
				&& StringUtils.isNotEmpty(authorizeParam.getCodeChallengeMethod())
				&& authorizeParam.getResponseType().equals(AUTHORIZATION_CODE)
				&& authorizeParam.getCodeChallengeMethod().equals(SHA256);
	}
}
