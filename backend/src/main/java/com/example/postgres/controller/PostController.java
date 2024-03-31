// Make post controller similar to channel and comment controller

// Path: src/main/java/com/example/postgres/controller/PostController.java

package com.example.postgres.controller;

import com.example.postgres.classes.Post;
import com.example.postgres.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController
{
    @Autowired
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("")
    public Post savePost(
            @RequestBody Post post
    ){
        return postService.savePost(post);
    }

    @GetMapping("")
    public List<Post> findAllPosts()
    {
        return postService.findAllPosts();
    }

    @GetMapping("/{post-id}")
    public Post findByPostId(
            @PathVariable("post-id") Long id
    )
    {
        return postService.findByPostId(id);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteAllPosts() {
        return postService.deleteAllPosts();
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<String> deletePostById(
            @PathVariable("post-id") Long id
    ) {
        return postService.deletePostById(id);
    }

    @PutMapping("/{post-id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable("post-id") Long id,
            @RequestBody Post post
    ) {
        return postService.updatePost(id, post);
    }

}