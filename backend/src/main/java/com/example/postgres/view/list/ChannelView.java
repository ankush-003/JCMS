package com.example.postgres.view.list;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.postgres.classes.*;
import com.example.postgres.dto.CommentDto;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.backend.ChannelService;
import com.example.postgres.service.backend.PostService;
import com.example.postgres.service.backend.UserService;
import com.example.postgres.service.backend.VoteService;
import com.example.postgres.service.frontend.CommentFetcher;
import com.example.postgres.service.frontend.PostServiceFrontend;
import com.example.postgres.utils.UserUtils;
import com.example.postgres.utils.UserUtils;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PageTitle("Channel")
@Route(value = "channel/:channelName", layout = MainLayout.class)
public class ChannelView extends Div implements BeforeEnterObserver {
    private final PostService postService;
    private final UserService userService;
    private final VoteService voteService;
    private final ChannelService channelService;

    private final PostServiceFrontend postServiceFrontend;

    private final Binder<Channel> binder = new Binder<>(Channel.class);
    private List<PostDto> posts;
    private final Button createPostButton = new Button("Create Post");
    private final Dialog dialog = new Dialog();
    private String channelName;

    private VirtualList<PostDto> postList;
    private final Div statusLabel = new Div();


    private final CommentFetcher commentFetcher;


    public ChannelView(PostService postService, UserService userService, VoteService voteService, ChannelService channelService, PostServiceFrontend postServiceFrontend, CommentFetcher commentFetcher) {
        this.postService = postService;
        this.userService = userService;
        this.voteService = voteService;
        this.channelService = channelService;
        this.postServiceFrontend = postServiceFrontend;
        this.commentFetcher = commentFetcher;
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        createPostButton.addClickListener(event -> {
            UserDetailsDto user = new UserDetailsDto();
            UserUtils.showStoredValue(user, UI.getCurrent(), () -> {
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
                            dialog.removeAll();
                            PostForm postForm = new PostForm(userService.findByUserId(user.getUser_id()), postService, channel);
                            dialog.add(postForm);
                            dialog.open();
                            dialog.addDialogCloseActionListener(e -> loadPosts());
                        }
                    } catch (JWTDecodeException e) {
                        UI.getCurrent().navigate("register");
                    }
                }
            });
        });
        createPostButton.addClassName("create-channel-button");
        add(createPostButton);
    }

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

                Div actual_outer = new Div(outer, getCommentElement(post));
                actual_outer.addClassName("post-full");
                outer.addClassName("post-outer");

                return actual_outer;
            }
    );

    @NotNull
    private  Div getInfo(PostDto post) {
        Div title = new Div(new Text(post.getTitle()));
        title.addClassName("post-title");

        Div description = new Div(new Text(post.getDescription()));
        description.addClassName("post-description");

        // Comment Section


        Div info = new Div(title, description);
        info.addClassName("post-info");

        return info;
    }

    private Div getCommentElement(PostDto post) {
        Div commentSection = new Div();
        commentSection.addClassName("comment-section");

        Div commentHeader = new Div(new Text("Comment Section"));
        commentHeader.addClassName("comment-header");

        // Render comments from an endpoint
        // Replace the following line with your code to fetch and render comments
        Div commentList = new Div(); // This will contain the rendered comments
        commentList.addClassName("comment-list");

        ComponentRenderer<Component, CommentDto> commentsRenderer = new ComponentRenderer<>(
                comment -> {
                    Div commentContainer = new Div();
                    commentContainer.addClassName("comment-container");

                    Div commentUser = new Div(new Text(comment.getUserName()));
                    commentUser.addClassName("comment-user");

                    Div commentText = new Div(new Text(comment.getCommentText()));
                    commentText.addClassName("comment-text");

                    Date myDate = Date.from(comment.getDateTime());

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, MMM d, yyyy");
                    String formattedDate = formatter.format(myDate);

                    Div commentTime =  new Div(formattedDate);
                    commentTime.addClassName("comment-time");

                    Div commentDeets = new Div(commentUser, commentTime);
                    commentDeets.addClassName("comment-deets");

                    commentContainer.add(commentDeets, commentText);
                    return commentContainer;
                }
        );

        commentFetcher.fetchComments(post.getId())
                .forEach(commentDto -> commentList.add(commentsRenderer.createComponent(commentDto)));

        // Add a text field for new comments
        TextArea newCommentField = new TextArea();
        newCommentField.addClassName("new-comment-field");
        newCommentField.setPlaceholder("Add a comment...");

        // Add a button to submit new comments
        Button submitCommentButton = new Button("Submit");
        submitCommentButton.addClassName("submit-comment-button");
        // Replace the following line with your code to handle the submission of new comments
        submitCommentButton.addClickListener(e -> {System.out.println("New comment: " + newCommentField.getValue());

            UserDetailsDto user = new UserDetailsDto();
            UserUtils.showStoredValue(user, UI.getCurrent(), () -> {
                Comment newComment = new Comment();
                newComment.setDescription(newCommentField.getValue());

                // Set the user and post IDs based on your application logic
                newComment.setUser(new User(user.getUser_id()));
                newComment.setPost(new Post(post.getId()));

                commentFetcher.addComment(newComment);
                // Reload entire page
                UI.getCurrent().getPage().reload();
            });
        });

        Div newCommentContainer = new Div(newCommentField, submitCommentButton);
        newCommentContainer.addClassName("new-comment-container");

        commentSection.add(commentHeader, commentList, newCommentContainer);

        return commentSection;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> channelNameParam = event.getRouteParameters().get("channelName");
        channelNameParam.ifPresent(name -> {
            channelName = name;
            loadPosts();
        });

        if (channelName == null) {
            setText("No channel selected");
        }
    }

    private void loadPosts() {
        if (postList != null) {
            remove(postList);
        }

        statusLabel.setVisible(true);

        UserDetailsDto user = new UserDetailsDto();
        UserUtils.showStoredValue(user, UI.getCurrent(), () -> {
            if (user.getAccessToken() == null) {
                statusLabel.setText("Please log in.");
                statusLabel.addClassNames(LumoUtility.TextColor.ERROR);
                statusLabel.setVisible(true);
                Notification notification = Notification
                        .show("Please log in.", 3000, Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }


            postServiceFrontend.getChannelPosts(posts -> {
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

            },user.getAccessToken(), channelName);

        });
    }
}