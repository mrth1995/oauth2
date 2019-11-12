package com.daksa.oauth.controller;

import com.daksa.oauth.domain.OAuthAccessToken;
import com.daksa.oauth.domain.OauthClient;
import com.daksa.oauth.exception.AuthenticationException;
import com.daksa.oauth.exception.AuthorizationException;
import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.repository.AccessTokenRepository;
import com.daksa.oauth.repository.ClientRepository;
import com.daksa.oauth.service.AccessTokenService;
import io.olivia.webutil.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@RequestScoped
@WebFilter(urlPatterns = "/account/*", asyncSupported = true)
public class OAuthFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(OAuthFilter.class);

	@Inject
	private DateTimeUtil dateTimeUtil;
	@Inject
	private AccessTokenRepository accessTokenRepository;
	@Inject
	private ClientRepository clientRepository;
	@Inject
	private AccessTokenService accessTokenService;

	private FilterConfig filterConfig;

	@Override
	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	@Override
	@Transactional
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		Date now = dateTimeUtil.now();
		try {
			String authHeader = request.getHeader("Authorization");
			if (StringUtils.isEmpty(authHeader)) {
				throw new AuthenticationException();
			}
			String[] authPart = authHeader.split("\\s");
			if (authPart.length < 2) {
				throw new AuthenticationException("Authorization header field is not valid");
			}
			String authMode = authPart[0];
			if (!authMode.equalsIgnoreCase(Constants.TOKEN_TYPE_BEARER)) {
				throw new AuthenticationException("Unsupported authentication type");
			}
			String accessToken = authPart[1];
			OAuthAccessToken oAuthAccessToken = accessTokenRepository.finByAccessToken(accessToken);
			if (oAuthAccessToken == null || !oAuthAccessToken.valid(now)) {
				throw new AuthorizationException("Invalid access_token");
			}
			String clientId = accessTokenService.getClientId(accessToken);
			OauthClient client = clientRepository.find(clientId);
			if (client == null) {
				throw new AuthorizationException();
			}
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (AuthorizationException | AuthenticationException e) {
			response.addHeader(Constants.RESPONSE_CODE_HEADER, e.getResponseCode());
			response.setStatus(e.getHttpStatus());
			try (PrintWriter print = new PrintWriter(response.getOutputStream())) {
				print.print(e.getMessage());
			}
		}
	}

	@Override
	public void destroy() {

	}
}
