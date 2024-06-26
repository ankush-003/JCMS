package com.example.postgres.service.frontend;

import com.example.postgres.dto.PostDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

@Service
public class PostServiceFrontend implements Serializable {

    public static interface AsyncRestCallback<T> {
        //        void onSuccess(T response);
//        void onError(String message);
        void operationFinished(T results);
    }
    // async
    public void getAllPosts(AsyncRestCallback<List<PostDto>> callback, String accessToken) {
        System.out.println("Getting all posts");

        WebClient.RequestHeadersSpec<?> spec = WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build()
                .get()
                .uri(
                        "http://localhost:8080/api/posts"
                ).header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json");

        spec.retrieve().toEntityList(PostDto.class).subscribe(
                posts -> {
                    System.out.println("Got all posts");
//                    System.out.println(posts.getBody());
//                    System.out.println("generic user " + posts.getBody().getFirst().getUser());
//                    System.out.println("generic channel" + posts.getBody().getFirst().getChannel());
                    callback.operationFinished(posts.getBody());
                }
        );
    }

    public void getChannelPosts(AsyncRestCallback<List<PostDto>> callback, String accessToken, String channelName) {
        System.out.println("Getting all posts");

        WebClient.RequestHeadersSpec<?> spec = WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build()
                .get()
                .uri(
                        "http://localhost:8080/api/posts/channel/" + channelName
                ).header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json");

        spec.retrieve().toEntityList(PostDto.class).subscribe(
                posts -> {
                    System.out.println("Got all posts");
//                    System.out.println(posts.getBody());
//                    System.out.println("generic user " + posts.getBody().getFirst().getUser());
//                    System.out.println("generic channel" + posts.getBody().getFirst().getChannel());
                    callback.operationFinished(posts.getBody());
                }
        );
    }




    public void getSubscribedPosts(AsyncRestCallback<List<PostDto>> callback, String accessToken) {
        System.out.println("Getting posts of channels you have subscribed to:");

        WebClient client = WebClient.builder().codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        // Make a request to an endpoint to get the user details based on the access token
        Mono<String> userDetailsMono = client.get()
                .uri("http://localhost:8080/api/users/user")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class);

        // After getting the user details, extract the user ID and fetch the subscribed posts
        userDetailsMono.subscribe(userDetails -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(userDetails);
                int userId = jsonNode.get("user_id").asInt();

                WebClient.RequestHeadersSpec<?> spec = client.get()
                        .uri("http://localhost:8080/api/posts/user/" + userId)
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Content-Type", "application/json");

                spec.retrieve().toEntityList(PostDto.class).subscribe(
                        posts -> {
                            System.out.println("Got all subscribed channel posts!");
//                            System.out.println(posts.getBody());
//                            System.out.println("Subscriber user " + posts.getBody().getFirst().getUserName());
//                            System.out.println("Subscriber channel" + posts.getBody().getFirst().getChannelName());
                            callback.operationFinished(posts.getBody());
                        }
                );
            } catch (Exception e) {
                // Handle exception
                e.printStackTrace();
            }
        });
    }


}
