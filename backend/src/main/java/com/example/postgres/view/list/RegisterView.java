package com.example.postgres.view.list;

import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.RegisterService;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

@Route(value = "register", layout = MainLayout.class)
@PageTitle("Get Started")
public class RegisterView extends VerticalLayout {

    private final Binder<UserDetailsDto> binder = new Binder<>(UserDetailsDto.class);
    private final UserDetailsDto userDataObject;
    {
        userDataObject = new UserDetailsDto();
    }

    public RegisterView(RegisterService registerService) {
        setAlignItems(Alignment.CENTER);

        Div registerDiv = new Div();
        registerDiv.getStyle().set("border", "1px solid #000000");
        registerDiv.getStyle().set("border-radius", "10px");
        registerDiv.getStyle().set("padding", "2em");
        registerDiv.setWidth("400px");

        TextField name = new TextField("Name");
        TextField username = new TextField("Username");
        TextField email = new TextField("Email");
        PasswordField password = new PasswordField("Password");
        Select<String> select = new Select<>();
        select.setLabel("Role");
        select.setItems("ROLE_USER", "ROLE_ADMIN");
        select.setValue("ROLE_USER");

        name.setRequiredIndicatorVisible(true);
        username.setRequiredIndicatorVisible(true);
        email.setRequiredIndicatorVisible(true);
        email.setPattern("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");
        email.setErrorMessage("Invalid email address");
        password.setRequiredIndicatorVisible(true);
        password.setMinLength(8);
        password.setErrorMessage("Password must be at least 8 characters long");

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

        registerDiv.add(header, formLayout, select);
        add(registerDiv);

        Button button = new Button("Register");
        Button loginButton = new Button("Login");

        binder.forField(name)
                .withValidator(n -> !n.isEmpty(), "Name cannot be empty")
                .withValidator(n -> n.length() <= 50, "Name must be at most 50 characters")
                .bind(UserDetailsDto::getName, UserDetailsDto::setName);

        binder.forField(username)
                .withValidator(un -> !un.isEmpty(), "Username cannot be empty")
                .withValidator(un -> un.length() <= 20, "Username must be at most 20 characters")
                .withValidator(un -> un.matches("^[a-zA-Z0-9_]+$"), "Username must contain only letters, numbers, and underscores")
                .bind(UserDetailsDto::getUser_name, UserDetailsDto::setUser_name);

        binder.forField(email)
                .withValidator(new EmailValidator("This doesn't look like a valid email address"))
                .withValidator(em -> em.endsWith("gmail.com"), "Only gmail.com email addresses are allowed")
                .bind(UserDetailsDto::getUser_email, UserDetailsDto::setUser_email);

        binder.forField(password)
                .withValidator(pwd -> !pwd.isEmpty(), "Password cannot be empty")
                .withValidator(pwd -> pwd.length() >= 8, "Password must be at least 8 characters")
                .bind(UserDetailsDto::getPassword, UserDetailsDto::setPassword);

        binder.setBean(userDataObject);

        button.addClickListener(e -> {
            if (binder.isValid()) {
                UserDetailsDto userData = binder.getBean();
                String usernameValue = userData.getUser_name();
                String passwordValue = userData.getPassword();
                String nameValue = userData.getName();
                String emailValue = userData.getUser_email();
                String roleValue = select.getValue();
                boolean isAuthenticated = registerService.registerUser(usernameValue, passwordValue, nameValue, emailValue, roleValue);

                if (isAuthenticated) {
                    Notification notification = Notification
                            .show("Successfully Registered!", 3000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    registerService.navigateToMainView();
                } else {
                    Notification notification = Notification
                            .show("Registration failed. Please try again.", 3000, Notification.Position.TOP_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                Notification notification = Notification
                        .show("Please fill in all required fields correctly.", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        add(button);
        loginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
        add(loginButton);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        UI.getCurrent().getElement().getThemeList().add(Lumo.DARK);
    }
}
