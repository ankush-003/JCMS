package com.example.frontend;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("localstorage")
public class LocalStorage extends VerticalLayout {

    private static final String NAME_KEY = "access_token";
    private Div stored = new Div();

    public LocalStorage() {
        // Latest Vaadin versions include built-in WebStorage support.
        // With older versions, use the web storage helper (with exactly
        // the same API) from
        // https://vaadin.com/directory/component/flow-viritin
//
//        TextField name = new TextField("Your name");
//
//        Button store = new Button(
//                "Save in local storage",
//                e -> {
//                    WebStorage.setItem(NAME_KEY, name.getValue());
//                    showStoredValue();
//                }
//        );

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
    }

    private void showStoredValue() {
        WebStorage.getItem(
                NAME_KEY,
                value -> {
                    stored.setText("Stored value: " + (value == null ? "<no value stored>" : value));
                }
        );
    }
}
