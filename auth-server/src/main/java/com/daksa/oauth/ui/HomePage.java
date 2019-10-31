package com.daksa.oauth.ui;

import com.vaadin.cdi.annotation.RouteScoped;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.Route;

import javax.annotation.PostConstruct;

@RouteScoped
@Route(value = "")
public class HomePage extends Div {

	@PostConstruct
	public void init() {
		H1 title = new H1("DAKSA OAuth Server");
		add(title);
	}
}
