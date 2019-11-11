package com.daksa.oauth.ui;

import com.daksa.oauth.domain.OAuthCode;
import com.daksa.oauth.infrastructure.Constants;
import com.daksa.oauth.repository.OAuthCodeRepository;
import com.daksa.oauth.service.UserAuthService;
import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Date;

@RouteScoped
@Route(value = "oauth/authorize")
public class AuthorizePage extends Div implements BeforeEnterObserver {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizePage.class);

	@Inject
	private OAuthCodeRepository oauthCodeRepository;
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

	@Transactional
	private void createLoginForm(BeforeEnterEvent beforeEnterEvent) {
		if (StringUtils.isNotEmpty(authorizationId)) {
			OAuthCode authCode = oauthCodeRepository.find(authorizationId);
			if (authCode != null && authCode.valid(new Date())) {
				LoginForm loginForm = new LoginForm();
				loginForm.addLoginListener(loginEvent -> {
					String username = loginEvent.getUsername();
					String password = loginEvent.getPassword();
					if (userAuthService.verifyPassword(username, password)) {
						StringBuilder pathBuilder = new StringBuilder(StringUtils.isNotEmpty(authCode.getRedirectUri()) ? authCode.getRedirectUri() : Constants.SUCCESS_PAGE);
						pathBuilder.append("?code=").append(authCode.getCode());
						LOG.info("redirect to: {}", pathBuilder.toString());
						UI.getCurrent().getPage().executeJs("window.location.href='" + pathBuilder.toString() + "'", "_self");
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
