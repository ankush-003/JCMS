package com.example.postgres.view;

import com.example.postgres.service.frontend.FrontendUserService;
import com.example.postgres.view.list.*;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
@PageTitle("Home")
public class MainLayout extends AppLayout {

    private final FrontendUserService frontendUserService;

    public MainLayout(FrontendUserService frontendUserService) {
        this.frontendUserService = frontendUserService;
        CheckTokenValidation();
    }

    private void createHeader(Boolean authenticated) {
        H1 logo = new H1("JCMS");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout logoutLayout = new HorizontalLayout();

        Button logout = new Button("Log out", e -> {
            WebStorage.removeItem("access_token");
            getUI().ifPresent(ui -> ui.navigate("login"));
//            createDrawer(false);
        });

        Div userText = new Div(new Text("Logged in as anonimoose"));

        logoutLayout.add(userText,logout);
        logoutLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                logoutLayout
        );
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.expand(logo); // Expand the logo to push the logout button to the right
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Align items to the end

        header.addClassNames("py-0", "px-m");
        if (!authenticated) {
            logoutLayout.setVisible(false);
        }
        addToNavbar(header);

    }



    private void createDrawer(Boolean authenticated) {

        Icon home = VaadinIcon.HOME.create();
        Icon unauthorizedHome = VaadinIcon.HOME.create();
        Icon login = VaadinIcon.SIGN_IN.create();
        Icon register = VaadinIcon.USER.create();
        Icon list = VaadinIcon.LIST.create();
        Icon posts = VaadinIcon.LIST.create();
        Icon profile = VaadinIcon.USER.create();
        RouterLink loginLink = new RouterLink("Login", LoginView.class);
        HorizontalLayout loginLayout = new HorizontalLayout(login, loginLink);

        RouterLink registerLink = new RouterLink("Register", RegisterView.class);

        HorizontalLayout profileLayout = new HorizontalLayout(profile, new RouterLink("Profile", ProfileView.class));

        HorizontalLayout registerLayout = new HorizontalLayout(register, registerLink);
        RouterLink homeLink = new RouterLink("Home", Home.class);
        HorizontalLayout homeLayout = new HorizontalLayout(home, homeLink);
        RouterLink allPostsLink = new RouterLink("Popular", PostList.class);
        HorizontalLayout allPostsLayout = new HorizontalLayout(posts, allPostsLink);

        RouterLink unauthorizedHomeLink = new RouterLink("Home", ListView.class);
        HorizontalLayout unauthorizedHomeLayout = new HorizontalLayout(unauthorizedHome, unauthorizedHomeLink);

        // get current href


        loginLink.setHighlightCondition(HighlightConditions.sameLocation());
        registerLink.setHighlightCondition(HighlightConditions.sameLocation());
        homeLink.setHighlightCondition(HighlightConditions.sameLocation());
        allPostsLink.setHighlightCondition(HighlightConditions.sameLocation());
        unauthorizedHomeLink.setHighlightCondition(HighlightConditions.sameLocation());

        if (authenticated) {
            unauthorizedHomeLayout.setVisible(false);
            loginLayout.setVisible(false);
            registerLayout.setVisible(false);
        } else {
            homeLayout.setVisible(false);
            profileLayout.setVisible(false);
            allPostsLayout.setVisible(false);
        }

        VerticalLayout drawer = new VerticalLayout(
                unauthorizedHomeLayout,
                homeLayout,
                loginLayout,
                registerLayout,
                allPostsLayout,
                profileLayout
        );

        drawer.addClassName("drawer");

        addToDrawer(drawer);
    }

    private void CheckTokenValidation() {
        WebStorage.getItem(
                "access_token",
                value -> {
                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    LoginCheck(value);
                }
        );
    }

    private void LoginCheck(String token) {
        Boolean check  = FrontendUserService.isUserLoggedIn(token);
        System.out.println("Check: " + check);
        createDrawer(check);
        createHeader(check);
    }
}