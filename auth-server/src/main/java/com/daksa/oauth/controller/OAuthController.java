package com.daksa.oauth.controller;

import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.model.AuthorizeParam;
import com.daksa.oauth.domain.OAuthCode;
import com.daksa.oauth.service.OAuthService;
import io.olivia.webutil.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	private OAuthService oAuthService;

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
}
