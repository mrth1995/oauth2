package com.daksa.oauth.ui;

import com.daksa.oauth.domain.OAuthCode;
import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.repository.OAuthAuthorizationRepository;
import com.daksa.oauth.service.UserAuthService;
import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@RouteScoped
@Route(value = "oauth/authorize")
public class AuthorizePage extends Div implements BeforeEnterObserver {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizePage.class);

	@Inject
	private OAuthAuthorizationRepository authorizationRepository;
	@Inject
	private UserAuthService userAuthService;

	private String authorizationId;

	@PostConstruct
	public void init() {
	}

	@Override
	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		authorizationId = getQueryParam("authorizationId", beforeEnterEvent.getLocation().getQueryParameters());
		createLoginForm(beforeEnterEvent);
	}

	private void createLoginForm(BeforeEnterEvent beforeEnterEvent) {
		if (StringUtils.isNotEmpty(authorizationId)) {
			OAuthCode authAuthorization = authorizationRepository.find(authorizationId);
			if (authAuthorization != null) {
				LoginForm loginForm = new LoginForm();
				loginForm.addLoginListener(loginEvent -> {
					String username = loginEvent.getUsername();
					String password = loginEvent.getPassword();
					if (userAuthService.verifyPassword(username, password)) {
						StringBuilder pathBuilder = new StringBuilder(StringUtils.isNotEmpty(authAuthorization.getRedirectUri()) ? authAuthorization.getRedirectUri() : Constants.AUTH_PAGE);
						pathBuilder.append("?code=").append(authAuthorization.getCode());
						VaadinServletResponse response = (VaadinServletResponse) VaadinService.getCurrentResponse();
						try {
							response.getHttpServletResponse().sendRedirect(pathBuilder.toString());
						} catch (IOException e) {
							beforeEnterEvent.rerouteTo(HomePage.class);
						}
					} else {
						beforeEnterEvent.rerouteTo(HomePage.class);
					}
				});
				add(loginForm);
			} else {
				beforeEnterEvent.rerouteTo(HomePage.class);
			}
		} else {
			beforeEnterEvent.rerouteTo(HomePage.class);
		}
	}

	private String getQueryParam(String key, QueryParameters queryParameters) {
		return queryParameters.getParameters().get(key)
				.stream().findFirst().orElse(null);
	}
}
