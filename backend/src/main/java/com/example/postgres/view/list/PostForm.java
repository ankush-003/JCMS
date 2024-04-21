package com.example.postgres.view.list;

import com.example.postgres.classes.Channel;
import com.example.postgres.classes.Post;
import com.example.postgres.classes.User;
import com.example.postgres.service.backend.PostService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

import java.io.IOException;
import java.time.Instant;

public class PostForm extends FormLayout {
    private final TextField titleField = new TextField("Title");
    private final TextArea descriptionField = new TextArea("Description");
    private final MemoryBuffer buffer = new MemoryBuffer();
    private final Upload upload = new Upload(buffer);
    private final Button submitButton = new Button("Submit");
    private final PostService postService;
    private final Channel channel;

    public PostForm(User user, PostService postService, Channel channel) {
        this.postService = postService;
        this.channel = channel;
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        submitButton.addClickListener(event -> {
            Post post = new Post();
            post.setTitle(titleField.getValue());
            post.setDescription(descriptionField.getValue());
            try {
                post.setContent(buffer.getInputStream().readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            post.setUser(user);
            post.setCreated_at(Instant.now());
            post.setChannel(channel);
            // Save the post using your service
            this.postService.savePost(post);
            Notification.show("Post created");
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        add(titleField, descriptionField, new Span("Upload Content:"), upload, submitButton);
    }
}