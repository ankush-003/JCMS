package com.example.postgres.service.backend;

import com.example.postgres.classes.PostDto;

import java.util.List;

public interface PostDtoService {
    List<PostDto> findAllPosts();
    List<PostDto> findUserSubscribedPosts(Long id);
}
