package com.example.application.views.list;

import com.example.application.entity.Channel;
import com.example.application.entity.UserData;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Route("cards")

public class ProfileView extends VerticalLayout {

    private static final String NAME_KEY = "access_token";
    private Binder<UserData> binder = new Binder<>(UserData.class);


    public ProfileView() {

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
        binder.bind(nameField, UserData::getName, UserData::setName);
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
        binder.bind(emailField, UserData::getUser_email, UserData::setUser_email);
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
        binder.bind(usernameField, UserData::getUser_name, UserData::setUser_name);
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

        List<Channel> channels = List.of(
                new Channel("Channel 1", "Channel 1 Description"),
                new Channel("Channel 2", "Channel 2 Description"),
                new Channel("Channel 3", "Channel 3 Description"),
                new Channel("Channel 4", "Channel 4 Description"),
                new Channel("Channel 5", "Channel 5 Description"),
                new Channel("Channel 6", "Channel 6 Description"),
                new Channel("Channel 7", "Channel 7 Description"),
                new Channel("Channel 8", "Channel 8 Description"),
                new Channel("Channel 9", "Channel 9 Description"),
                new Channel("Channel 10", "Channel 10 Description")

        );

        VirtualList<Channel> virtualList = new VirtualList<>();
        virtualList.setItems(channels);

        virtualList.setRenderer(new ComponentRenderer<>(channel -> {
//            VerticalLayout layout = new VerticalLayout(); // Change to VerticalLayout
            HorizontalLayout layout = new HorizontalLayout();
            layout.setAlignItems(Alignment.CENTER);
            layout.setSpacing(true);
            layout.setPadding(true);

            Div chan = new Div();
            chan.setText(channel.getName());
            chan.getStyle().set("font-weight", "bold");

            Div description = new Div();
            description.setText(channel.getDescription());

            layout.add(chan, description);
            return layout;
        }));

        channelLayout.add(virtualList);

        mainLayout.add(channelLayout); // Add the channelLayout to the mainLayout

        add(mainLayout); // Add the mainLayout to the ProfileView


        Button button = new Button("Get Details",
                e -> {
                    getUserData();
                });

        add(button);
//        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        UI.getCurrent().getElement().getThemeList().add("dark");


    }

        public UserData getUserData() {
            UserData userData = new UserData();

            // Retrieve the access token asynchronously
            showStoredValue(userData, () -> {
                System.out.println("Access Token: " + userData.getAccessToken());

                // Once access token is retrieved, make API request
                try {

                    System.out.println("Making API request...");
                    System.out.println("aryaaaaa Access Token: " + userData.getAccessToken());
                    // Create a URL object from the specified address
                    URL url = new URL("http://localhost:8080/api/users/user");

                    // Open a connection to the URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the request method to GET
                    connection.setRequestMethod("GET");

                    connection.setRequestProperty("Content-Type", "application/json");

                    // Add the Authorization header with the access token
                    connection.setRequestProperty("Authorization", "Bearer " + userData.getAccessToken());

                    // Set the connection timeout to 5 seconds
                    connection.setConnectTimeout(5000);

                    // Set the read timeout to 5 seconds
                    connection.setReadTimeout(5000);

                    // Get the response code from the connection
                    int responseCode = connection.getResponseCode();

                    System.out.println("Response Code: " + responseCode);

                    // If the response code is 200 (OK), read the response
                    if (responseCode == 200) {
                        // Create a BufferedReader object to read the response
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        // Create a StringBuilder object to store the response
                        StringBuilder response = new StringBuilder();



                        // Read the response line by line
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        System.out.println("Response: " + response);

                        // Close the reader
                        reader.close();

                        // Parse the response JSON string to a UserData object
                        Gson gson = new Gson();
                        UserData userData1 = gson.fromJson(response.toString(), UserData.class);



                        String name = userData1.getName();
                        String userName = userData1.getUser_name();
                        String email = userData1.getUser_email();

                        binder.setBean(userData1);


                        System.out.println("Name: " + name);
                        System.out.println("User Name: " + userName);
                        System.out.println("Email: " + email);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            return userData; // Return null as default or handle the case where user data retrieval fails
        }

        private void showStoredValue(UserData userData, Runnable callback) {
            WebStorage.getItem(
                    NAME_KEY,
                    value -> {
                        System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                        userData.setAccessToken(value);
                        callback.run(); // Call the callback after retrieving the access token
                    }
            );
        }
    }
