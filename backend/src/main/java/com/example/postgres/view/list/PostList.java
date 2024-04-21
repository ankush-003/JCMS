package com.example.postgres.view.list;


import com.example.postgres.classes.Post;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.frontend.PostServiceFrontend;
import com.example.postgres.service.backend.UserService;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.List;

@PageTitle("Posts")
@Route(value = "posts", layout = MainLayout.class)
public class PostList extends Main {
    private List<Post> posts;
    private final PostServiceFrontend postServiceFrontend;
    private VirtualList<Post> postList;
    private final Div statusLabel;
    private UserService userService;

    private ComponentRenderer<Component, Post> postsRenderer = new ComponentRenderer<>(
            post -> {

                System.out.println("Rendering post" + post.getVotes().size() + " " + post.getComments() + " " + post.getCreated_at() + " " + post.getDescription() + " " + post.getId() + " " + post.getTitle() + " ");


                StreamResource sr = new StreamResource("post", () -> {
                    return new ByteArrayInputStream(post.getContent());
                });
                sr.setContentType("image/png");

                Image image = new Image(sr, "post");
                image.addClassName("post-image");

                Div card = new Div();

                Div channel = new Div(new Text("posted on wisdom"));
                channel.addClassName("post-channel");

                Div user = new Div(new Text("anonimoose"));
                user.addClassName("post-user");


                Div contact = new Div(user, channel);
                contact.addClassName("post-contact");

                Div info = getInfo(post);

                card.add(image, info);
                card.addClassName("post-card");

                Div outer = new Div(card, contact);
                outer.addClassName("post-outer");


                return outer;
            }
    );

    @NotNull
    private static Div getInfo(Post post) {
        Div title = new Div(new Text(post.getTitle()));
        title.addClassName("post-title");

        Div description = new Div(new Text(post.getDescription()));
        description.addClassName("post-description");

        Div info = new Div(title, description);
        info.addClassName("post-info");

        return info;
    }

    public PostList(@Autowired PostServiceFrontend postServiceFrontend, @Autowired UserService userService) {
        this.postServiceFrontend = postServiceFrontend;
        this.userService = userService;

        statusLabel = new Div("Hold on tight...");
        statusLabel.addClassName("status-label");
        statusLabel.setVisible(false);


//        Button newPostButton = new Button("New Post", e -> {
//            getUI().ifPresent(ui -> ui.navigate(NewPost.class));
//        });
        loadPosts();

        add(statusLabel);
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
            postServiceFrontend.getAllPosts(posts -> {
                System.out.println("Got all posts in PostList");
                getUI().ifPresent(ui -> ui.access(() -> {
                    statusLabel.setVisible(false);

                    System.out.println("Rendered all posts in PostList");

                    this.posts = posts;
                    postList = new VirtualList<>();
                    postList.setItems(posts);
                    postList.setRenderer(postsRenderer);
                    postList.addClassName("post-list");
                    add(postList);
                }));

            }, user.getAccessToken());
        });
    }

    private void showStoredValue(UserDetailsDto user, Runnable callback) {
        WebStorage.getItem(
                "access_token",
                value -> {
                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    user.setAccessToken(value);
                    callback.run(); // Call the callback after retrieving the access token
                }
        );
    }
}
