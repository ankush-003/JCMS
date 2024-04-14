package com.example.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("content")
    private byte[] content;

    @JsonProperty("created_at")
    private Instant created_at;

    @JsonProperty("user")
    private Long user;

    @JsonProperty("comments")
    private List<Long> comments;

    @JsonProperty("channel")
    private Long channel;

    @JsonProperty("votes")
    private List<Long> votes;
}
