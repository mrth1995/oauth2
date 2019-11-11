package com.daksa.oauth.ui;

import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@RouteScoped
@Route(value = "oauth/success")
public class SuccessPage extends VerticalLayout {

	@PostConstruct
	public void init() {
		add(new H1("Success"));
	}
}
