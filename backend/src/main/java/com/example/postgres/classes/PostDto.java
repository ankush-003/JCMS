package com.example.postgres.classes;

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
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private byte[] content;
    private Instant created_at;
    private String userName;
    private List<Long> comments;
    private String channelName;
    private List<Long> votes;
}
