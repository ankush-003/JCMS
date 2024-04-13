package com.example.application.views.list;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Welcome to JCMS")
@Route(value = "", layout = MainLayout.class)
public class ListView extends VerticalLayout {

    public ListView() {
        addClassName("dark-theme");
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2("Welcome to JCMS!");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("It’s a place where you can manage your content with ease."));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        Button loginButton = new Button("Login");
        loginButton.addClassNames(
                LumoUtility.TextColor.SUCCESS,
                LumoUtility.BorderColor.SUCCESS,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Margin.Right.AUTO
        );
        loginButton.addClickListener(e -> {
            getUI().get().navigate(LoginView.class);

        });

        Button registerButton = new Button("Register");
        registerButton.addClassNames(
                LumoUtility.TextColor.SUCCESS,
                LumoUtility.BorderColor.SUCCESS,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Margin.Right.AUTO
        );
        registerButton.addClickListener(e -> {
            getUI().get().navigate(RegisterView.class);
        });


        HorizontalLayout buttons = new HorizontalLayout(loginButton, registerButton);
        buttons.setSpacing(true);

        add(buttons);

    }

}
