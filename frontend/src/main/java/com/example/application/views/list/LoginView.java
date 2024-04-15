package com.example.application.views.list;

import com.example.application.services.LoginService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Login")
@Route(value="/login", layout = MainLayout.class)
public class LoginView extends VerticalLayout {



    @Autowired
    public LoginView(LoginService loginService) {
        addClassName("dark-theme");

        addClassName("login-rich-content");

        LoginForm loginForm = new LoginForm();
        loginForm.getElement().getThemeList().add("dark");
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.getElement().getStyle().set("border", "1px solid black");
        add(loginForm);


        Button button = new Button("Register");
        button.addClickListener(e -> getUI().get().navigate(RegisterView.class));
        add(button);

        // Set the login event handler
        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            // Perform authentication logic here
            boolean isAuthenticated = loginService.authenticate(username, password);

            if (isAuthenticated) {
                // User is authenticated, navigate to the main view
                loginService.navigateToMainView();
                Notification notification = Notification
                        .show("Successfully Logged In!", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } else {
                // Authentication failed, show an error message
                loginForm.setError(true);
            }
        });

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        getStyle().setBorder("1px solid #000000");

        UI.getCurrent().getElement().getThemeList().add("dark");
    }

}
