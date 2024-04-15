package com.example.postgres.view.list;


import com.example.postgres.classes.Post;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.PostServiceFrontend;
import com.example.postgres.service.UserService;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.List;

@PageTitle("Posts")
@Route(value = "posts", layout = MainLayout.class)
public class PostList extends Main {
    private List<Post> posts;
    private final PostServiceFrontend postServiceFrontend;
    private VirtualList<Post> postList;
    private final Span statusLabel;
    private UserService userService;

    private ComponentRenderer<Component, Post> postsRenderer = new ComponentRenderer<>(
            post -> {

                StreamResource sr = new StreamResource("post", () -> {
                    return new ByteArrayInputStream(post.getContent());
                });
                sr.setContentType("image/png");

                Image image = new Image(sr, "post");
                image.setWidth("100px");
                image.setHeight("100px");

                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setMargin(true);


                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.setPadding(false);
                infoLayout.getElement().appendChild(
                        ElementFactory.createStrong(post.getTitle()));
                infoLayout.add(new Div(new Text(post.getDescription())));

                VerticalLayout contactLayout = new VerticalLayout();
                contactLayout.setSpacing(false);
                contactLayout.setPadding(false);
                contactLayout.add(new Div(new Text("User: " + post.getUser())));
                contactLayout
                        .add(new Div(new Text("Channel: " + post.getChannel())));
                infoLayout
                        .add(new Details("Additional Info", contactLayout));

                cardLayout.add(image, infoLayout);
                return cardLayout;
            }
    );

    public PostList(@Autowired PostServiceFrontend postServiceFrontend, @Autowired UserService userService) {
        this.postServiceFrontend = postServiceFrontend;
        this.userService = userService;

        statusLabel = new Span("Loading posts...");
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
