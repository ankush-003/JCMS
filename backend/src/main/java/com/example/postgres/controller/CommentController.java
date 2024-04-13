package com.example.postgres.controller;

import com.example.postgres.classes.Comment;
import com.example.postgres.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("")
    public Comment saveComment(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("")
    public List<Comment> findAllComments() {
        return commentService.findAllComments();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/{comment-id}")
    public Comment findByCommentId(@PathVariable("comment-id") Long id) {
        return commentService.findByCommentId(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteAllComments() {
        return commentService.deleteAllComments();
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable("comment-id") Long id) {
        return commentService.deleteCommentById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
    @PutMapping("/{comment-id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("comment-id") Long id,
                                                 @RequestBody Comment comment) {
        return commentService.updateComment(id, comment);
    }
}