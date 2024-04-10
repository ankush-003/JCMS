package com.example.postgres.service;

import com.example.postgres.classes.Post;
import com.example.postgres.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findByPostId(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public ResponseEntity<byte[]> uploadContent(Long id, byte[] content) {
        Post postToUpdate = postRepository.findById(id).orElse(null);

        if (postToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        postToUpdate.setContent(content);
        Post updatedPost = postRepository.save(postToUpdate);
        return ResponseEntity.ok(updatedPost.getContent());
    }

    public ResponseEntity<byte[]> downloadContent(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post.getContent());
    }

    public ResponseEntity<String> deleteAllPosts() {
        postRepository.deleteAll();
        String message = "All Posts have been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<String> deletePostById(Long id) {
        postRepository.deleteById(id);
        String message = "Post with id " + id + " has been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<Post> updatePost(Long id, Post post) {
        Post postToUpdate = postRepository.findById(id).orElse(null);

        if (postToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setDescription(post.getDescription());
        Post updatedPost = postRepository.save(postToUpdate);
        return ResponseEntity.ok(updatedPost);
    }
}
