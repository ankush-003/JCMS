package com.example.postgres.view.list;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.postgres.classes.Channel;
import com.example.postgres.classes.Post;
import com.example.postgres.classes.Vote;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.backend.ChannelService;
import com.example.postgres.service.backend.PostService;
import com.example.postgres.service.backend.UserService;
import com.example.postgres.service.backend.VoteService;
import com.example.postgres.view.MainLayout;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@PageTitle("Channel")
@Route(value = "channel/:channelName", layout = MainLayout.class)
public class ChannelView extends Div implements BeforeEnterObserver {
    private final PostService postService;
    private final UserService userService;
    private final VoteService voteService;
    private final ChannelService channelService;
    private final Button createPostButton = new Button("Create Post");
    private final Dialog dialog = new Dialog();
    private String channelName;

    public ChannelView(PostService postService, UserService userService, VoteService voteService, ChannelService channelService) {
        this.postService = postService;
        this.userService = userService;
        this.voteService = voteService;
        this.channelService = channelService;
        createPostButton.addClickListener(event -> {
            UserDetailsDto user = new UserDetailsDto();
            showStoredValue(user, () -> {
                if (user.getAccessToken() == null) {
                    UI.getCurrent().navigate("register");
                } else {
                    try {
                        DecodedJWT decodedJwt = JWT.decode(user.getAccessToken());
                        Date expiresAt = decodedJwt.getExpiresAt();
                        if (expiresAt.before(new Date())) {
                            UI.getCurrent().navigate("register");
                        } else {
                            Channel channel = channelService.findByChannelName(channelName);
                            PostForm postForm = new PostForm(userService.findByUserId(user.getUser_id()), postService, channel);
                            dialog.add(postForm);
                            dialog.open();
                            dialog.addDialogCloseActionListener(e -> displayPosts());
                        }
                    } catch (JWTDecodeException e) {
                        UI.getCurrent().navigate("register");
                    }
                }
            });
        });

        add(createPostButton);
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

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> channelNameParam = event.getRouteParameters().get("channelName");
        channelNameParam.ifPresent(name -> {
            channelName = name;
            displayPosts();
        });

        if (channelName == null) {
            setText("No channel selected");
        }
    }

    private void displayPosts() {
        removeAll();
        add(createPostButton);
        Channel channel = channelService.findByChannelName(channelName);
        List<Post> posts = postService.findPostsByChannelName(channel.getName());
        for (Post post : posts) {
            List<Vote> votes = voteService.findVotesByPostId(post.getId());
            post.setVotes(votes);
            PostComponent postComponent = new PostComponent(post, voteService);
            add(postComponent);
        }
    }
}