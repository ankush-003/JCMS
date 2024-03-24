package com.example.postgres.controller;

import com.example.postgres.classes.Comment;
import com.example.postgres.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CommentController
{
    @Autowired
    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @PostMapping("/comments")
    public Comment postsNewComment(
            @RequestBody Comment comment
    ){
        return commentRepository.save(comment);
    }

    @GetMapping("/comments")
    public List<Comment> findAllComments()
    {
        return commentRepository.findAll();
    }

    @GetMapping("/comments/{comment-id}")
    public Comment findByCommentId(
            @PathVariable("comment-id") Integer id
    )
    {
        return commentRepository.findById(id).orElse(null);

    }

    @DeleteMapping("/comments")
    public ResponseEntity<String> deleteAllComments() {
        commentRepository.deleteAll();
        String message = "All Comments have been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/comments/{comment-id}")
    public ResponseEntity<String> deleteCommentById(
            @PathVariable("comment-id") Integer id
    ) {
        commentRepository.deleteById(id);
        String message = "Comment with id " + id + " has been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/comments/{comment-id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("comment-id") Integer id,
            @RequestBody Comment comment
    ) {
        Comment CommentToUpdate = commentRepository.findById(id).orElse(null);
        if (CommentToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        CommentToUpdate.setComment(comment.getComment());
        Comment updatedComment = commentRepository.save(CommentToUpdate);
        return ResponseEntity.ok(updatedComment);
    }





}
