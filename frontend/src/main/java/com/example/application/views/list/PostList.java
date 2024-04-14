package com.example.application.views.list;


import com.example.application.entity.Post;
import com.example.application.entity.UserData;
import com.example.application.services.PostService;
import com.example.application.services.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

@PageTitle("Posts")
@Route(value = "posts", layout = MainLayout.class)
public class PostList extends Main {
    private PostService postService;
    private Grid<Post> postGrid;
    private Span statusLabel;
    private UserService userService;

    public PostList(@Autowired PostService postService, @Autowired UserService userService) {
        this.postService = postService;
        this.userService = userService;

        statusLabel = new Span("Loading posts...");
        statusLabel.setVisible(false);
        postGrid = new Grid<Post>(Post.class);
//        Button newPostButton = new Button("New Post", e -> {
//            getUI().ifPresent(ui -> ui.navigate(NewPost.class));
//        });
        Button loadPostsButton = new Button("Load Posts", e -> {
            loadPosts();
        });

        add(statusLabel, postGrid, loadPostsButton);
    }

    private void loadPosts() {
        statusLabel.setVisible(true);
        postGrid.setVisible(false);

        UserData userData = new UserData();
        showStoredValue(userData, () -> {
            if (userData.getAccessToken() == null) {
                statusLabel.setText("Please log in.");
                statusLabel.addClassNames(LumoUtility.TextColor.ERROR);
                statusLabel.setVisible(true);
                Notification notification = Notification
                        .show("Please log in.", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            postService.getAllPosts(posts -> {
                System.out.println("Got all posts in PostList");
                getUI().ifPresent(ui -> ui.access(() -> {
                    statusLabel.setVisible(false);
                    postGrid.setEnabled(true);
                    postGrid.setItems(posts);
                    System.out.println("Rendered all posts in PostList");
                }));

            }, userData.getAccessToken());
        });
    }

    private void showStoredValue(UserData userData, Runnable callback) {
        WebStorage.getItem(
                "access_token",
                value -> {
                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    userData.setAccessToken(value);
                    callback.run(); // Call the callback after retrieving the access token
                }
        );
    }

}
