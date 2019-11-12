package com.daksa.oauth.controller;

import com.daksa.oauth.domain.OAuthAccessToken;
import com.daksa.oauth.domain.OauthClient;
import com.daksa.oauth.exception.InvalidAuthCodeException;
import com.daksa.oauth.exception.InvalidEncryptionMethodException;
import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.model.AccessTokenModel;
import com.daksa.oauth.model.AuthorizeParam;
import com.daksa.oauth.domain.OAuthCode;
import com.daksa.oauth.model.RequestAccessToken;
import com.daksa.oauth.repository.AccessTokenRepository;
import com.daksa.oauth.repository.ClientRepository;
import com.daksa.oauth.repository.OAuthCodeRepository;
import com.daksa.oauth.service.AccessTokenService;
import com.daksa.oauth.service.OAuthService;
import io.olivia.webutil.json.Json;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

@RequestScoped
@Path("authorize")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OAuthController {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthController.class);

	@Inject
	private OAuthService oAuthService;
	@Inject
	private AccessTokenService accessTokenService;
	@Inject
	private OAuthCodeRepository oAuthCodeRepository;
	@Inject
	private ClientRepository clientRepository;
	@Inject
	private AccessTokenRepository accessTokenRepository;

	@GET
	public Response authorized(@BeanParam AuthorizeParam authorizeParam,
	                           @Context HttpServletRequest request,
	                           @Context HttpServletResponse response) throws IOException {
		LOG.info("authorize {}", Json.getWriter().withDefaultPrettyPrinter().writeValueAsString(authorizeParam));
		OAuthCode oAuthCode = oAuthService.createAuthorization(authorizeParam);
		if (oAuthCode == null) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		String contextPath = request.getContextPath();
		StringBuilder pathBuilder = new StringBuilder(contextPath);
		if (!contextPath.endsWith("/")) {
			pathBuilder.append("/");
		}
		pathBuilder.append(Constants.AUTH_PAGE).append("?authorizationId=").append(oAuthCode.getId());
		String redirect = pathBuilder.toString();
		response.sendRedirect(redirect);
		return Response.accepted().build();
	}

	@POST
	@Path("accessToken")
	@Transactional
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public AccessTokenModel accessTokenRequest(@BeanParam RequestAccessToken param) throws InvalidEncryptionMethodException, InvalidAuthCodeException {
		if (!param.getCodeChallengeMethod().equals("SHA256")) {
			throw new InvalidEncryptionMethodException();
		}
		OauthClient client = clientRepository.find(param.getClientId());
		String codeChallenge = HmacUtils.hmacSha256Hex(client.getSecret(), param.getCodeVerifier());
		LOG.debug("code challenge: {}", codeChallenge);
		OAuthCode code = oAuthCodeRepository.find(param.getClientId(), param.getCode(), codeChallenge);
		if (code == null) {
			throw new InvalidAuthCodeException();
		}
		if (code.valid(new Date())) {
			code.invalidate();
			oAuthCodeRepository.update(code);
			OAuthAccessToken accessToken = new OAuthAccessToken.Builder()
					.client(client)
					.createdTimestamp(new Date(), Constants.CODE_EXPIRY_SECOND)
					.accessToken(accessTokenService.createAccessToken(code.getCode()))
					.refreshToken(accessTokenService.createAccessToken(code.getCode()))
					.build();
			accessTokenRepository.store(accessToken);
			AccessTokenModel model = new AccessTokenModel();
			model.setAccessToken(accessToken.getAccessToken());
			model.setAccessToken(accessToken.getRefreshToken());
			model.setExpiryTimestamp(accessToken.getExpiryTimestamp());
			return model;
		} else {
			throw new InvalidAuthCodeException();
		}
	}
}
