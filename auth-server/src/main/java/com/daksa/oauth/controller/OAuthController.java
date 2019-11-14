package com.daksa.oauth.controller;

import com.daksa.oauth.domain.OAuthAccessToken;
import com.daksa.oauth.domain.OAuthCode;
import com.daksa.oauth.exception.*;
import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.model.AccessTokenModel;
import com.daksa.oauth.model.AuthorizeParam;
import com.daksa.oauth.model.RequestAccessToken;
import com.daksa.oauth.service.AccessTokenService;
import com.daksa.oauth.service.AuthorizationCodeService;
import io.olivia.webutil.json.Json;
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

@RequestScoped
@Path("authorize")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OAuthController {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthController.class);

	@Inject
	private AuthorizationCodeService authorizationCodeService;
	@Inject
	private AccessTokenService accessTokenService;

	@GET
	public Response authorized(@BeanParam AuthorizeParam authorizeParam,
	                           @Context HttpServletRequest request,
	                           @Context HttpServletResponse response) throws IOException {
		LOG.info("authorize {}", Json.getWriter().withDefaultPrettyPrinter().writeValueAsString(authorizeParam));
		OAuthCode oAuthCode = authorizationCodeService.createAuthorization(authorizeParam);
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
	public AccessTokenModel accessTokenRequest(@BeanParam RequestAccessToken param) throws InvalidEncryptionMethodException,
			InvalidAuthCodeException, InvalidGrantTypeException, AuthorizationException, AuthenticationException {
		OAuthAccessToken accessToken;
		if (param.getGrantType().equals(Constants.GRANT_TYPE_AUTH_CODE)) {
			accessToken = accessTokenService.requestTokenAuthCode(param.getClientId(), param.getCode(),
					param.getCodeVerifier(), param.getCodeChallengeMethod());
		} else if (param.getGrantType().equals(Constants.GRANT_TYPE_CLIENT_CREDENTIAL)) {
			accessToken = accessTokenService.requestTokenClientCredential(param.getClientId(), param.getClientSecret());
		} else if (param.getGrantType().equals(Constants.GRANT_TYPE_REFRESH_TOKEN)) {
			accessToken = accessTokenService.requestTokenRefreshToken(param.getClientId(), param.getClientSecret(),
					param.getRefreshToken());
		} else{
			throw new InvalidGrantTypeException();
		}
		AccessTokenModel model = new AccessTokenModel();
		model.setAccessToken(accessToken.getAccessToken());
		model.setExpiryTimestamp(accessToken.getExpiryTimestamp());
		model.setRefreshToken(accessToken.getRefreshToken());
		model.setTokenType(accessToken.getTokenType());
		model.setGrantType(accessToken.getGrantType());
		return model;
	}
}
