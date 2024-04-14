package com.example.application.views.list;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.Route;

@Route(value = "/home", layout = MainLayout.class)
public class Home extends VerticalLayout {

    private static final String ACCESS_TOKEN_KEY = "access_token";
    private final Div storedValueDiv = new Div();

    public Home() {
        Button logoutButton = new Button("Logout", event -> logout());
        showStoredValue();
        add(logoutButton, storedValueDiv);

        Button profileButton = new Button("My Profile", event -> navigateToProfile());
        add(profileButton);
    }

    private void showStoredValue() {
        WebStorage.getItem(
                ACCESS_TOKEN_KEY,
                value -> storedValueDiv.setText("Stored value: " + (value == null ? "<no value stored>" : value))
        );
    }

    private void logout() {
        WebStorage.removeItem(ACCESS_TOKEN_KEY);
        showStoredValue();
        getUI().ifPresent(ui -> ui.navigate(LoginView.class));
    }

    private void navigateToProfile() {
        getUI().ifPresent(ui -> ui.navigate(ProfileView.class));
    }
}
