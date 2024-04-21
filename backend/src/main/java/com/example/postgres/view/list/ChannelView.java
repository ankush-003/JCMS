package com.example.postgres.view.list;

import com.example.postgres.classes.Channel;
import com.example.postgres.classes.Post;
import com.example.postgres.classes.Vote;
import com.example.postgres.service.backend.ChannelService;
import com.example.postgres.service.backend.PostService;
import com.example.postgres.service.backend.UserService;
import com.example.postgres.service.backend.VoteService;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Channel")
@Route(value="channel", layout = MainLayout.class)
public class ChannelView extends Div implements HasUrlParameter<Long>  {
    private final PostService postService;
    private final UserService userService;
    private final VoteService voteService;
    private final ChannelService channelService;
    private final Button createPostButton = new Button("Create Post");
    private final Dialog dialog = new Dialog();
    private Long channelId;

    public ChannelView(PostService postService, UserService userService, VoteService voteService, ChannelService channelService) {
        this.postService = postService;
        this.userService = userService;
        this.voteService = voteService;
        this.channelService = channelService;
        createPostButton.addClickListener(event -> {
            Channel channel = channelService.findByChannelId(channelId);
            PostForm postForm = new PostForm(userService.findByUserId(202L), postService, channel);
            dialog.add(postForm);
            dialog.open();
            dialog.addDialogCloseActionListener(e -> displayPosts());
        });

        add(createPostButton);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long channelId) {
        if (channelId == null) {
            setText("No channel selected");
        } else {
            this.channelId = channelId;
            displayPosts();
        }
    }

    private void displayPosts() {
        removeAll();
        add(createPostButton);
        List<Post> posts = postService.findPostsByChannelId(channelId);
        for (Post post : posts) {
            List<Vote> votes = voteService.findVotesByPostId(post.getId());
            post.setVotes(votes);
            PostComponent postComponent = new PostComponent(post, voteService);
            add(postComponent);
        }
    }
}