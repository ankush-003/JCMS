package com.example.postgres.service.backend;

import com.example.postgres.classes.Comment;
import com.example.postgres.repository.CommentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;


    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // Do similar for all other controller methods in CommentController.java

    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    public Comment findByCommentId(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public ResponseEntity<String> deleteAllComments() {
        commentRepository.deleteAll();
        String message = "All Comments have been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<String> deleteCommentById(Long id) {
        commentRepository.deleteById(id);
        String message = "Comment with id " + id + " has been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<Comment> updateComment(Long id, Comment comment) {
        Comment CommentToUpdate = commentRepository.findById(id).orElse(null);

        if (CommentToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        CommentToUpdate.setDescription(comment.getDescription());
        Comment updatedComment = commentRepository.save(CommentToUpdate);
        return ResponseEntity.ok(updatedComment);
    }



}
