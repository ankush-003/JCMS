package com.example.application.views;

import com.example.application.views.list.Home;
import com.example.application.views.list.ListView;
import com.example.application.views.list.LoginView;
import com.example.application.views.list.RegisterView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("JCMS");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("List", ListView.class);
        RouterLink loginLink = new RouterLink("Login", LoginView.class);
        RouterLink registerLink = new RouterLink("Register", RegisterView.class);
        RouterLink homeLink = new RouterLink("Home", Home.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                homeLink,
                loginLink,
                registerLink,
                listLink
        ));
    }
}