package com.example.postgres.service.backend;

import com.example.postgres.classes.Post;
import com.example.postgres.classes.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostDtoServiceImpl implements PostDtoService {
    private final PostService postService;

    @Autowired
    public PostDtoServiceImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    public List<PostDto> findAllPosts() {
        List<Post> posts = postService.findAllPosts();
        List<PostDto> postDtos = posts.stream().map(PostDto::fromPostEntity).toList();
        return postDtos;
    }

    @Override
    public List<PostDto> findUserSubscribedPosts(Long id) {
        List<Post> posts = postService.findUserSubscribedPosts(id);
        List<PostDto> postDtos = posts.stream().map(PostDto::fromPostEntity).toList();
        return postDtos;
    }

    @Override
    public List<PostDto> findChannelPosts(String name) {
        List<Post> posts = postService.findChannelPosts(name);
        List<PostDto> postDtos = posts.stream().map(PostDto::fromPostEntity).toList();
        return postDtos;
    }
}
