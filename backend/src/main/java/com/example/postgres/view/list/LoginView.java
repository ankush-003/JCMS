package com.example.postgres.view.list;

import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Route(value="/login", layout = MainLayout.class)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {
        addClassName("dark-theme");

//         See login-rich-content.css
        addClassName("login-rich-content");

        LoginForm loginForm = new LoginForm();
        loginForm.getElement().getThemeList().add("dark");
        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.getElement().getStyle().set("border", "1px solid black");
        add(loginForm);


        Button button = new Button("Register");
        button.addClickListener(e -> {
            getUI().get().navigate(RegisterView.class);

        });
        add(button);

        // Set the login event handler
        loginForm.addLoginListener(event -> {
            String username = event.getUsername();
            String password = event.getPassword();

            // Perform authentication logic here
            boolean isAuthenticated = authenticateUser(username, password);

            if (isAuthenticated) {
                // User is authenticated, navigate to the main view
                navigateToMainView();
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

    private boolean authenticateUser(String username, String password) {
        // Your authentication logic here
        // For example, check if the username and password match a database record
        // Return true if the user is authenticated, false otherwise
//        return username.equals("admin") && password.equals("password");
        return authenticate(username, password);


    }



    public static boolean authenticate(String username, String password) {
        HttpURLConnection connection = null;
        try {
            String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            URL url = new URL("http://localhost:8080/sign-in");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + credentials);
            connection.setDoOutput(true);

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                // Write request body if needed
                // writer.write("data=value");
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Authentication failed. Response Code: " + responseCode);
                return false;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            String accessToken = jsonResponse.getString("access_token");
            System.out.println("Access Token: " + accessToken);
            // Store access token in web storage
            WebStorage.setItem("access_token", accessToken);

            return true;

        } catch (IOException e) {
            System.err.println("Error in authentication: " + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }




    private void navigateToMainView() {
        // Navigate to the main view after successful authentication
        // For example:
        getUI().get().navigate(Home.class);
    }
}
