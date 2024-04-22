package com.example.postgres.view.list;


import com.example.postgres.classes.PostDto;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.frontend.PostServiceFrontend;
import com.example.postgres.utils.UserUtils;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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
@Route(value = "popular", layout = MainLayout.class)
public class PostList extends Main {
    private List<PostDto> posts;
    private final PostServiceFrontend postServiceFrontend;
    private VirtualList<PostDto> postList;
    private final Div statusLabel;

    private final ComponentRenderer<Component, PostDto> postsRenderer = new ComponentRenderer<>(
            post -> {
                StreamResource sr = new StreamResource("post", () -> new ByteArrayInputStream(post.getContent()));
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

    public PostList(@Autowired PostServiceFrontend postServiceFrontend) {
        this.postServiceFrontend = postServiceFrontend;
        statusLabel = new Div("Hold on tight...");
        statusLabel.addClassName("status-label");
        statusLabel.setVisible(false);

        loadPosts();

        add(statusLabel);
    }

    private void loadPosts() {
        statusLabel.setVisible(true);

        UserDetailsDto user = new UserDetailsDto();
        UserUtils.showStoredValue(user,UI.getCurrent(), () -> {
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

}
