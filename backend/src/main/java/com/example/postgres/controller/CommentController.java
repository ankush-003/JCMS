package com.example.postgres.controller;

import com.example.postgres.classes.Comment;
import com.example.postgres.repository.CommentRepository;
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

    @PostMapping("")
    public Comment postsNewComment(
            @RequestBody Comment comment
    ){
        return commentRepository.save(comment);
    }

    @GetMapping("")
    public List<Comment> findAllComments()
    {
        return commentRepository.findAll();
    }

    @GetMapping("/{comment-id}")
    public Comment findByCommentId(
            @PathVariable("comment-id") Long id
    )
    {
        return commentRepository.findById(id).orElse(null);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteAllComments() {
        commentRepository.deleteAll();
        String message = "All Comments have been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<String> deleteCommentById(
            @PathVariable("comment-id") Long id
    ) {
        commentRepository.deleteById(id);
        String message = "Comment with id " + id + " has been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/{comment-id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable("comment-id") Long id,
            @RequestBody Comment comment
    ) {
        Comment CommentToUpdate = commentRepository.findById(id).orElse(null);
        if (CommentToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        CommentToUpdate.setDescription(comment.getDescription());
        Comment updatedComment = commentRepository.save(CommentToUpdate);
        return ResponseEntity.ok(updatedComment);
    }
}
