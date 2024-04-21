package com.example.postgres.service.frontend;

import com.example.postgres.classes.Comment;
import com.example.postgres.dto.CommentDto;
import com.example.postgres.service.backend.CommentService;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentFetcher {
    private final CommentService commentService;

    public CommentFetcher(CommentService commentService) {
        this.commentService = commentService;
    }

    public List<CommentDto> fetchComments(Long postId) {
        List<Comment> comments = commentService.findCommentsByPostId(postId);
        List<CommentDto> commentDtos = new ArrayList<>();

        for (Comment comment : comments) {
            String userName = comment.getUser().getUsername();
            String commentText = comment.getDescription();
            CommentDto commentDto = new CommentDto(userName, commentText, comment.getCreated_at());
            commentDtos.add(commentDto);
        }

        return commentDtos;
    }


    public boolean addComment(Comment comment) {
        commentService.saveComment(comment);
        Notification.show("Comment added successfully", 3000, Notification.Position.TOP_CENTER);
        return true;
    }
}