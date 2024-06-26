package com.example.postgres.view.list;


import com.example.postgres.classes.Channel;
import com.example.postgres.dto.PostDto;
import com.example.postgres.classes.*;
import com.example.postgres.dto.CommentDto;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.backend.UserService;
import com.example.postgres.service.frontend.ChannelServiceFrontend;
import com.example.postgres.service.frontend.CommentFetcher;
import com.example.postgres.service.frontend.PostServiceFrontend;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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


    private final CommentFetcher commentFetcher;

    private Button button;



    private UserService userService;

    private final ComponentRenderer<Component, PostDto> postsRenderer = new ComponentRenderer<>(
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

                Date myDate = Date.from(post.getCreated_at());

                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm | MMM d, yyyy");
                String formattedDate = formatter.format(myDate);

                Div dateDiv = new Div(formattedDate);
                dateDiv.addClassName("post-date");

                Div info = getInfo(post);

                card.add(image, info);
                card.addClassName("post-card");

                Div leftElement = new Div(dateDiv,contact );
                leftElement.addClassName("post-right-element");

                Div outer = new Div(card, leftElement);

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

        List<CommentDto> comments = post.getComments();

        comments.forEach(
                comment -> {
                    Div commentContainer = new Div();
                    commentContainer.addClassName("comment-container");
                    Div commentUser = new Div(new Text(comment.getUserName()));
                    commentUser.addClassName("comment-user");

                    Div commentText = new Div(new Text(comment.getCommentText()));
                    commentText.addClassName("comment-text");

                    Date myDate = Date.from(comment.getDateTime());

                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm | MMM d, yyyy");
                    String formattedDate = formatter.format(myDate);

                    Div commentTime =  new Div(formattedDate);
                    commentTime.addClassName("comment-time");

                    Div commentDeets = new Div(commentUser, commentTime);
                    commentDeets.addClassName("comment-deets");
                    commentContainer.add(commentDeets, commentText);

                    commentList.add(commentContainer);

                }
        );


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

    @Autowired
    public Home(PostServiceFrontend postServiceFrontend,ChannelServiceFrontend channelServiceFrontend, UserService userService, CommentFetcher commentFetcher) {
        this.commentFetcher = commentFetcher;
        this.postServiceFrontend = postServiceFrontend;
        this.userService = userService;
        this.channelServiceFrontend = channelServiceFrontend;



        statusLabel = new Div("Hold on tight...");
        statusLabel.addClassName("status-label");
        statusLabel.setVisible(false);

        loadPosts();

        add(statusLabel);

    }

    private void loadPosts() {
        if (postList != null) {
            remove(postList);
            remove(button);
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

            button = new Button("Create Channel", e -> dialog.open());
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