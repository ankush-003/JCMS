package com.example.postgres.view.list;

import com.example.postgres.classes.Channel;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.frontend.ChannelServiceFrontend;
import com.example.postgres.service.backend.UserService;
import com.example.postgres.view.MainLayout;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@PageTitle("My Profile")
@Route(value="cards", layout = MainLayout.class)
public class ProfileView extends VerticalLayout {
    private final static Binder<UserDetailsDto> binder = new Binder<>(UserDetailsDto.class);

    private final Span statusLabel;


    private  UserService userService;

    private  ChannelServiceFrontend channelServiceFrontend;

    public ProfileView(UserService userService, ChannelServiceFrontend channelServiceFrontend) {
        this.channelServiceFrontend = channelServiceFrontend;

        statusLabel = new Span("Loading ...");
        statusLabel.setVisible(false);

        getProfile();

        add(statusLabel);

    }



    private void getProfile()
    {
        statusLabel.setVisible(true);
        UserDetailsDto user = new UserDetailsDto();
        showStoredValue(user, () -> {

            if (user.getAccessToken() == null) {
                statusLabel.setText("Please log in.");
                statusLabel.addClassNames(LumoUtility.TextColor.ERROR);
                statusLabel.setVisible(true);
                Notification notification = Notification
                        .show("Please log in.", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }


            statusLabel.setVisible(false);

            HorizontalLayout mainLayout = new HorizontalLayout();

            VerticalLayout card = new VerticalLayout();
            card.getStyle().set("border", "1px solid #000000");
            card.getStyle().set("border-radius", "10px");
            card.getStyle().set("padding", "1em");
            card.setWidth("300px");


            H2 header = new H2("Profile Details");
            header.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.MEDIUM);
            header.getStyle().set("text-align", "center");

            H5 nameheader = new H5("Name");
            nameheader.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
            Div name = new Div();
            TextField nameField = new TextField();
            nameField.setReadOnly(true);
            binder.bind(nameField, UserDetailsDto::getName, UserDetailsDto::setName);
            nameField.getStyle().set("font-size", "1em");
            nameField.getStyle().set("font-weight", "bold");
            nameField.getStyle().set("margin-bottom", "0.5em");
            nameField.getStyle().set("text-align", "center");
            name.add( nameField);


            H5 emailheader = new H5("Email");
            emailheader.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
            Div email = new Div();
            TextField emailField = new TextField();
            emailField.setReadOnly(true);
            binder.bind(emailField, UserDetailsDto::getUser_email, UserDetailsDto::setUser_email);
            emailField.getStyle().set("font-size", "1em");
            emailField.getStyle().set("font-weight", "bold");
            emailField.getStyle().set("margin-bottom", "0.5em");
            emailField.getStyle().set("text-align", "center");
            email.add( emailField);

            H5 usernameheader = new H5("Username");
            usernameheader.addClassNames(LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.NONE);
            Div username = new Div();
            TextField usernameField = new TextField();
            usernameField.setReadOnly(true);
            binder.bind(usernameField, UserDetailsDto::getUser_name, UserDetailsDto::setUser_name);
            usernameField.getStyle().set("font-size", "1em");
            usernameField.getStyle().set("font-weight", "bold");
            usernameField.getStyle().set("margin-bottom", "0.5em");
            usernameField.getStyle().set("text-align", "center");
            username.add( usernameField);

            card.add(header, nameheader, name, emailheader, email, usernameheader, username);
            mainLayout.add(card);

            VerticalLayout channelLayout = new VerticalLayout();
            channelLayout.setWidth("500px"); // Set the width of the channel view
            channelLayout.getStyle().set("border", "1px solid #000000"); // Set border color to dark blue
            channelLayout.getStyle().set("border-radius", "10px"); // Set border radius
            channelLayout.getStyle().set("padding", "1em"); // Set padding


            Div channelHeader = new Div();
            channelHeader.setText("My Channels");
            channelHeader.getStyle().set("font-size", "1.5em");
            channelHeader.getStyle().set("font-weight", "bold");
            channelHeader.getStyle().set("margin-bottom", "0.5em");
            channelHeader.getStyle().set("text-align", "center");
            channelLayout.add(channelHeader);

//            List<Channel> channels = List.of(
//                    new Channel("Channel 1", "Channel 1 Description"),
//                    new Channel("Channel 2", "Channel 2 Description"),
//                    new Channel("Channel 3", "Channel 3 Description"),
//                    new Channel("Channel 4", "Channel 4 Description"),
//                    new Channel("Channel 5", "Channel 5 Description"),
//                    new Channel("Channel 6", "Channel 6 Description"),
//                    new Channel("Channel 7", "Channel 7 Description"),
//                    new Channel("Channel 8", "Channel 8 Description"),
//                    new Channel("Channel 9", "Channel 9 Description"),
//                    new Channel("Channel 10", "Channel 10 Description")
//
//            );

            VirtualList<Channel> virtualList = new VirtualList<>();
//            virtualList.setItems(channels);

            virtualList.setRenderer(new ComponentRenderer<>(channel -> {
                HorizontalLayout layout = new HorizontalLayout();
                layout.setAlignItems(Alignment.CENTER);
                layout.setSpacing(true);
                layout.setPadding(true);

                Div channelLink = new Div();
                channelLink.setText(channel.getName());
                channelLink.getStyle().set("font-weight", "bold");
                channelLink.addClickListener(e -> {
                    System.out.println(channel.getId());
                    System.out.println(channel.getName());
                    UI.getCurrent().navigate(ChannelView.class, new RouteParameters("channelName",channel.getName()));
                });


                Div description = new Div();
                description.setText(channel.getDescription());



                layout.add(channelLink, description);
                return layout;
            }));

            channelLayout.add(virtualList);
            mainLayout.add(channelLayout); // Add the channelLayout to the mainLayout
            add(mainLayout); // Add the mainLayout to the ProfileView

//            Button button = new Button("Get Details",
//                    e -> userService.getUserData(binder));
//
//            add(button);



            binder.setBean(user);
            channelServiceFrontend.setChannels(virtualList, user.getAccessToken(),user.getUser_id());
            setSizeFull();
            setJustifyContentMode(JustifyContentMode.CENTER);
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);
            UI.getCurrent().getElement().getThemeList().add("dark");


            });
    }

    private void showStoredValue(UserDetailsDto user, Runnable callback) {
        WebStorage.getItem(
                "access_token",
                value -> {
                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    user.setAccessToken(value);
                    if (value != null) {
                        getUserDataAsync(user)
                                .thenAccept(userData -> {
                                    getUI().ifPresent(ui -> ui.access(() -> {
                                        Long ID = userData.getUser_id();
                                        user.setUser_id(ID);
                                        user.setName(userData.getName());
                                        user.setUser_email(userData.getUser_email());
                                        user.setUser_name(userData.getUser_name());
                                        System.out.println("set");
                                        callback.run(); // Call the callback after retrieving user details
                                    }));
                                })
                                .exceptionally(ex -> {
                                    ex.printStackTrace();
                                    return null; // Return null to handle the exception
                                });
                    } else {
                        callback.run(); // Run the callback directly if the value is null
                    }
                }
        );
    }



    private CompletableFuture<UserDetailsDto> getUserDataAsync(UserDetailsDto user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Making API request to get User Details");
                URL url = new URL("http://localhost:8080/api/users/user");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + user.getAccessToken());
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                System.out.println("Response Code: " + responseCode);

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response);
                    reader.close();
                    Gson gson = new Gson();
                    return gson.fromJson(response.toString(), UserDetailsDto.class);
                } else {
                    throw new IOException("Failed to fetch user data. Response code: " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to fetch user data", e);
            }
        });
    }









}
