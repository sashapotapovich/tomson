package com.vaadin.ui;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import lombok.extern.slf4j.Slf4j;
import org.test.di.annotations.PostConstruct;

@Slf4j
@Route(value = "welcome", layout = MenuView.class)
public class WelcomeView extends VerticalLayout implements RouterLayout {

    public static final String ID = "welcome";

    private VerticalLayout verticalLayout;
    private H2 h2;

    public WelcomeView(){
    }

    @PostConstruct
    public void init() {
        verticalLayout = new VerticalLayout();
        verticalLayout.add(h2);
        verticalLayout.setAlignItems(Alignment.CENTER);
        add(verticalLayout);
    }
}
