package com.example.postgres.controller;

import com.example.postgres.classes.Comment;
import com.example.postgres.repository.CommentRepository;
import com.example.postgres.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/comments")
public class CommentController
{
    @Autowired
    private CommentRepository commentRepository;

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("")
    public Comment saveComment(
            @RequestBody Comment comment
    ){
        return commentService.saveComment(comment);
    }

    @GetMapping("")
    public List<Comment> findAllComments()
    {
        return commentService.findAllComments();
    }

    @GetMapping("/{comment-id}")
    public Comment findByCommentId(
            @PathVariable("comment-id") Long id
    )
    {
        return commentService.findByCommentId(id);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteAllComments() {
        return commentService.deleteAllComments();
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<String> deleteCommentById(
            @PathVariable("comment-id") Long id
    ) {
        return commentService.deleteCommentById(id);
    }

    @PutMapping("/{comment-id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("comment-id") Long id,
            @RequestBody Comment comment
    ) {
        return commentService.updateComment(id, comment);
    }
}
