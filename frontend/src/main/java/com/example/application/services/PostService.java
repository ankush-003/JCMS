package com.example.application.services;

import com.example.application.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

import java.io.Serializable;
import java.util.List;

@Service
public class PostService implements Serializable {

    public static interface AsyncRestCallback<T> {
//        void onSuccess(T response);
//        void onError(String message);
        void oprationFinished(T results);
    }
    // async
    public void getAllPosts(AsyncRestCallback<List<Post>> callback, String accessToken) {
        System.out.println("Getting all posts");

        RequestHeadersSpec<?> spec = WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build()
                .get()
                .uri(
                        "http://localhost:8080/api/posts"
        ).header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json");

        spec.retrieve().toEntityList(Post.class).subscribe(
                posts -> {
                    System.out.println("Got all posts");
                    System.out.println(posts.getBody());
                    callback.oprationFinished(posts.getBody());
                }
        );
    }
}
