package com.example.application.views.list;

import com.example.application.entity.UserData;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Route(value="register", layout = MainLayout.class)
@PageTitle("Get Started")
public class RegisterView extends VerticalLayout {

    public RegisterView() {
        setAlignItems(Alignment.CENTER); // Center the form


        Div registerDiv = new Div(); // Create a Div for registration components
        registerDiv.getStyle().set("border", "1px solid #000000"); // Set border color to dark blue
        registerDiv.getStyle().set("border-radius", "10px"); // Set border radius
        registerDiv.getStyle().set("padding", "2em"); // Set padding
        registerDiv.setWidth("400px"); // Set width of the registration area

        TextField name = new TextField("Name");
        TextField username = new TextField("Username");
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Password");
        Select<String> select = new Select<>();
        select.setLabel("Role");
        select.setItems("ROLE_USER", "ROLE_ADMIN");
        select.setValue("ROLE_USER");


        name.setRequiredIndicatorVisible(true); // Make the name field required
        username.setRequiredIndicatorVisible(true); // Make the username field required
        email.setRequiredIndicatorVisible(true); // Make the email field required
        email.setPattern("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$"); // Set email pattern validation
        email.setErrorMessage("Invalid email address"); // Set error message for invalid email
        password.setRequiredIndicatorVisible(true); // Make the password field required
        password.setMinLength(8); // Set minimum length for password
        password.setErrorMessage("Password must be at least 8 characters long"); // Set error message for invalid password

        FormLayout formLayout = new FormLayout();
        formLayout.add(name, username, email, password);

        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2));
        formLayout.setColspan(username, 1);

        Div header = new Div();
        header.setText("Register");
        header.getStyle().set("font-size", "1.5em");
        header.getStyle().set("font-weight", "bold");
        header.getStyle().set("margin-bottom", "1em");
        header.getStyle().set("text-align", "center");

        registerDiv.add(header, formLayout, select); // Add components to registerDiv
        add(registerDiv);

        Button button = new Button("Register");
        Button loginButton = new Button("Login");

        Binder<UserData> binder = new Binder<>(UserData.class);
        UserData userDataObject = new UserData();

        // Bindings for fields...
        binder.forField(name)
                .withValidator(n -> !n.isEmpty(), "Name cannot be empty")
                .withValidator(n -> n.length() <= 50, "Name must be at most 50 characters")
                .bind(UserData::getName, UserData::setName);

        binder.forField(username)
                .withValidator(un -> !un.isEmpty(), "Username cannot be empty")
                .withValidator(un -> un.length() <= 20, "Username must be at most 20 characters")
                .withValidator(un -> un.matches("^[a-zA-Z0-9_]+$"), "Username must contain only letters, numbers, and underscores")
                .bind(UserData::getUser_name, UserData::setUser_name);

        binder.forField(email)
                .withValidator(new EmailValidator("This doesn't look like a valid email address"))
                .withValidator(em -> em.endsWith("gmail.com"), "Only gmail.com email addresses are allowed")
                .bind(UserData::getUser_email, UserData::setUser_email);

        binder.forField(password)
                .withValidator(pwd -> !pwd.isEmpty(), "Password cannot be empty")
                .withValidator(pwd -> pwd.length() >= 8, "Password must be at least 8 characters")
//                .withValidator(pwd -> pwd.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$"), "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
                .bind(UserData::getPassword, UserData::setPassword);

        binder.setBean(userDataObject); // Moved after all bindings are set up


        button.addClickListener(e -> {
            if (binder.isValid()) {
                UserData userData = binder.getBean();

                String usernameValue = userData.getUser_name();
                String passwordValue = userData.getPassword();
                String nameValue = userData.getName();
                String emailValue = userData.getUser_email();
                String roleValue = select.getValue();

                boolean isAuthenticated = registerUser(usernameValue, passwordValue, nameValue, emailValue, roleValue);

                if (isAuthenticated) {
                    // User is authenticated, navigate to the main view
                    Notification notification = Notification
                            .show("Successfully Registered!", 3000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    navigateToMainView();
                } else {
                    // Authentication failed, show an error message
                    System.out.println("Registration failed");
                    Notification notification = Notification
                            .show("Registration failed. Please try again.", 3000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                // Form validation failed, show an error message
                Notification notification = Notification
                        .show("Please fill in all required fields correctly.", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(button);
        loginButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });
        add(loginButton);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        UI.getCurrent().getElement().getThemeList().add("dark");


    }

    private boolean registerUser(String username, String password, String name, String email, String role) {

        HttpURLConnection connection = null;
        try {
            // Your registration logic here
            // For example, send a POST request to a REST API
            // Return true if the user is registered successfully, false otherwise
            URL url = new URL("http://localhost:8080/sign-up");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"name\": \"" + name + "\", \"user_name\": \"" + username + "\", \"userEmail\": \"" + email + "\", \"userPassword\": \"" + password + "\", \"userRole\": \"" + role + "\"}";
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(jsonInputString);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Registration failed. Response Code: " + responseCode);
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
            System.err.println("Error in Registration: " + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void navigateToMainView() {
        UI.getCurrent().navigate(Home.class);
    }
}

