package com.example.postgres.view.list;


import com.example.postgres.classes.Channel;
import com.example.postgres.classes.PostDto;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.backend.UserService;
import com.example.postgres.service.frontend.ChannelServiceFrontend;
import com.example.postgres.service.frontend.PostServiceFrontend;
import com.example.postgres.view.MainLayout;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@PageTitle("Home")
@Route(value="/home", layout = MainLayout.class)
public class Home extends VerticalLayout {

    private static final String NAME_KEY = "access_token";

    private final Binder<Channel> binder = new Binder<>(Channel.class);
    private List<PostDto> posts;
    private final PostServiceFrontend postServiceFrontend;

    private final ChannelServiceFrontend channelServiceFrontend;
    private VirtualList<PostDto> postList;
    private final Div statusLabel;



    private UserService userService;

    private ComponentRenderer<Component, PostDto> postsRenderer = new ComponentRenderer<>(
            post -> {
                StreamResource sr = new StreamResource("post", () -> {
                    return new ByteArrayInputStream(post.getContent());
                });
                sr.setContentType("image/png");

                Image image = new Image(sr, "post");
                image.addClassName("post-image");

                Div card = new Div();

                Div channel = new Div(new Text("posted on " + post.getChannelName()));
                channel.addClassName("post-channel");

                Div user = new Div(new Text(post.getUserName()));
                user.addClassName("post-user");


                Div contact = new Div(user, channel);
                contact.addClassName("post-contact");

                Div info = getInfo(post);

                card.add(image, info);
                card.addClassName("post-card");

                Div outer = new Div(card, contact);
                outer.addClassName("post-outer");

//                System.out.println("Post rendered" + post);

                return outer;
            }
    );

    @NotNull
    private static Div getInfo(PostDto post) {
        Div title = new Div(new Text(post.getTitle()));
        title.addClassName("post-title");

        Div description = new Div(new Text(post.getDescription()));
        description.addClassName("post-description");

        Div info = new Div(title, description);
        info.addClassName("post-info");

        return info;
    }


    @Autowired
    public Home(PostServiceFrontend postServiceFrontend,ChannelServiceFrontend channelServiceFrontend, UserService userService) {
        this.postServiceFrontend = postServiceFrontend;
        this.userService = userService;
        this.channelServiceFrontend = channelServiceFrontend;



        statusLabel = new Div("Hold on tight...");
        statusLabel.addClassName("status-label");
        statusLabel.setVisible(false);

        loadPosts();

        add(statusLabel);

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


    private void loadPosts() {
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


            Dialog dialog = new Dialog();
            dialog.setModal(false);
            dialog.setDraggable(true);

            H2 title = new H2("Create Channel");
            title.addClassName("draggable");
            dialog.add(title);

            VerticalLayout dialogLayout = new VerticalLayout();


            TextField title1 = new TextField("Title");
            TextField description = new TextField("Description");

            binder.forField(title1)
                    .withValidator(n -> !n.isEmpty(), "Title cannot be empty")
                    .withValidator(n -> n.length() <= 20, "Title must be at most 20 characters")
                    .bind(Channel::getName, Channel::setName);

            binder.forField(description)
                    .withValidator(n -> !n.isEmpty(), "Description cannot be empty")
                    .withValidator(n -> n.length() <= 50, "Description must be at most 50 characters")
                    .bind(Channel::getDescription, Channel::setDescription);

            binder.setBean(new Channel());

            dialogLayout.add(title1, description);

            dialog.add(dialogLayout);

            Button saveButton = new Button("Save", e -> {
                System.out.println(binder.isValid());
                if(binder.isValid())
                {
                    System.out.println(user.getUser_id());
                    Channel channel = binder.getBean();
                    boolean channelAdded = channelServiceFrontend.addChannel(channel, user.getAccessToken(), user.getUser_id());
                    System.out.println(channelAdded);
                    if(channelAdded)
                    {
                        Notification notification = Notification
                                .show("Channel created successfully!", 3000, Notification.Position.TOP_CENTER);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        dialog.close();
                    }
                    else
                    {
                        Notification notification = Notification
                                .show("Channel creation failed. Please try again.", 3000, Notification.Position.TOP_CENTER);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }

            });
            Button cancelButton = new Button("Cancel", e -> dialog.close());
            dialog.getFooter().add(cancelButton);
            dialog.getFooter().add(saveButton);

            Button button = new Button("Create Channel", e -> dialog.open());
            button.addClassName("create-channel-button");



            postServiceFrontend.getSubscribedPosts(posts -> {
                System.out.println("Got all posts in PostList");
                getUI().ifPresent(ui -> ui.access(() -> {
                    statusLabel.setVisible(false);

                    System.out.println("Rendered all posts in PostList");

                    this.posts = posts;
                    postList = new VirtualList<>();
                    postList.setItems(posts);
                    postList.setRenderer(postsRenderer);
                    postList.addClassName("post-list");
                    add(button , dialog);
                    add(postList);
                }));

            }, user.getAccessToken());

        });
    }
}