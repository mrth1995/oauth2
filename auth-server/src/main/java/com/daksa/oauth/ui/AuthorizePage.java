package com.daksa.oauth.ui;

import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@RouteScoped
@Route(value = "oauth/authorize")
public class AuthorizePage extends Div implements HasUrlParameter<String> {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizePage.class);
	private String authorizationId;

	@PostConstruct
	public void init() {
		LoginForm loginForm = new LoginForm();
		Label authId = new Label(authorizationId);
		add(loginForm, authId);
	}

	@Override
	public void setParameter(BeforeEvent beforeEvent, String parameter) {
		Location location = beforeEvent.getLocation();
		QueryParameters queryParameters = location.getQueryParameters();
		LOG.info(queryParameters.getQueryString());
	}
}
