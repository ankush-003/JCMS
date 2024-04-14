package com.example.application.views.list;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.Route;


@Route("/home")
public class Home extends VerticalLayout {

    private static final String NAME_KEY = "access_token";
    private Div stored = new Div();

    public Home() {
        // Add the code here
        Button logout = new Button(
                "Logout",
                e -> {
                    WebStorage.removeItem(NAME_KEY);
                    showStoredValue();
                    getUI().get().navigate(LoginView.class);
                }
        );
        showStoredValue();
//        add(name, store, stored, clear);
        add(logout, stored);
        Button button = new Button("My Profile",
                e -> {
                    getUI().get().navigate(ProfileView.class);
                });

        add(button);
    }

    private void showStoredValue() {
        WebStorage.getItem(
                NAME_KEY,
                value -> {
                    stored.setText("Stored value: " + (value == null ? "<no value stored>" : value));
//                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                }
        );
    }
}
