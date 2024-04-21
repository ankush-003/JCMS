package com.example.postgres.view.list;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.postgres.classes.Post;
import com.example.postgres.classes.Vote;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.backend.VoteService;
import com.example.postgres.utils.UserUtils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

public class PostComponent extends Div {
    private final VoteService voteservice;

    public PostComponent(Post post, VoteService voteservice) {
        this.voteservice = voteservice;
        H3 title = new H3(post.getTitle());
        H4 description = new H4(post.getDescription());
        if (post.getContent() != null) {
            StreamResource resource = new StreamResource("postContent", () -> new ByteArrayInputStream(post.getContent()));
            Image contentImage = new Image(resource, "post content");
            contentImage.setMaxHeight("500px");
            add(contentImage);
        }

        List<Vote> votes = post.getVotes();

        Button upvoteButton = new Button("Upvote (" + getUpVotes(votes) + ")");
        Button downvoteButton = new Button("Downvote (" + getDownVotes(votes) + ")");

        upvoteButton.addClickListener(event -> {
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
                            Vote vote = new Vote();
                            vote.setVoteType(1);
                            vote.setPost(post);
                            post.getVotes().add(vote);
                            this.voteservice.saveVote(vote);
                            upvoteButton.setText("Upvote (" + (getUpVotes(post.getVotes())) + ")");
                        }
                    } catch (JWTDecodeException e) {
                        UI.getCurrent().navigate("register");
                    }
                }
            });
        });

        downvoteButton.addClickListener(event -> {
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
                            Vote vote = new Vote();
                            vote.setVoteType(0);
                            vote.setPost(post);
                            post.getVotes().add(vote);
                            this.voteservice.saveVote(vote);
                            downvoteButton.setText("Downvote (" + (getDownVotes(post.getVotes())) + ")");
                        }
                    } catch (JWTDecodeException e) {
                        UI.getCurrent().navigate("register");
                    }
                }
            });
        });

        HorizontalLayout smallFieldsLayout = new HorizontalLayout(upvoteButton, downvoteButton);
        smallFieldsLayout.setSpacing(true);

        add(title, description, smallFieldsLayout);
    }

    long getUpVotes(List<Vote> votes) {
        return votes.stream().filter(vote -> vote.getVoteType() == 1).count();
    }

    long getDownVotes(List<Vote> votes) {
        return votes.stream().filter(vote -> vote.getVoteType() == 0).count();
    }
}