// Make post controller similar to channel and comment controller

// Path: src/main/java/com/example/postgres/controller/PostController.java

package com.example.postgres.controller;

import com.example.postgres.classes.Post;
import com.example.postgres.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("")
    public Post savePost(@RequestBody Post post) {
        return postService.savePost(post);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("")
    public List<Post> findAllPosts() {
        return postService.findAllPosts();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/{post-id}")
    public Post findByPostId(@PathVariable("post-id") Long id) {
        return postService.findByPostId(id);
    }

    @PostMapping(
            value="/{post-id}/upload",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> uploadContent(@PathVariable("post-id") Long id,
                                                @RequestPart byte[] content) {
        return postService.uploadContent(id, content);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping(
            value="/{post-id}/download",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> downloadContent(@PathVariable("post-id") Long id) {
        return postService.downloadContent(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteAllPosts() {
        return postService.deleteAllPosts();
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/{post-id}")
    public ResponseEntity<String> deletePostById(@PathVariable("post-id") Long id) {
        return postService.deletePostById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
    @PutMapping("/{post-id}")
    public ResponseEntity<Post> updatePost(@PathVariable("post-id") Long id,
                                           @RequestBody Post post) {
        return postService.updatePost(id, post);
    }
}
