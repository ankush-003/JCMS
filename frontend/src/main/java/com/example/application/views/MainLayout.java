package com.example.application.views;

import com.example.application.views.list.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

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
        Icon home = VaadinIcon.HOME.create();
        Icon login = VaadinIcon.SIGN_IN.create();
        Icon register = VaadinIcon.USER.create();
        Icon list = VaadinIcon.LIST.create();
        Icon posts = VaadinIcon.LIST.create();
        Icon profile = VaadinIcon.USER.create();
        RouterLink listLink = new RouterLink("List", ListView.class);
        HorizontalLayout listLayout = new HorizontalLayout(list, listLink);
        RouterLink loginLink = new RouterLink("Login", LoginView.class);
        HorizontalLayout loginLayout = new HorizontalLayout(login, loginLink);
        RouterLink registerLink = new RouterLink("Register", RegisterView.class);

        HorizontalLayout profileLayout = new HorizontalLayout(profile, new RouterLink("Profile", ProfileView.class));

        HorizontalLayout registerLayout = new HorizontalLayout(register, registerLink);
        RouterLink homeLink = new RouterLink("Home", Home.class);
        HorizontalLayout homeLayout = new HorizontalLayout(home, homeLink);
        RouterLink allPostsLink = new RouterLink("All Posts", PostList.class);
        HorizontalLayout allPostsLayout = new HorizontalLayout(posts, allPostsLink);

        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        loginLink.setHighlightCondition(HighlightConditions.sameLocation());
        registerLink.setHighlightCondition(HighlightConditions.sameLocation());
        homeLink.setHighlightCondition(HighlightConditions.sameLocation());
        allPostsLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                homeLayout,
                loginLayout,
                profileLayout,
                registerLayout,
                listLayout,
                allPostsLayout
        ));
    }
}