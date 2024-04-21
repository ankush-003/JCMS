package com.example.postgres.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

    public static PostDto fromPostEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .userName(post.getUser().getUsername())
                .comments(post.getComments().stream().map(Comment::getId).collect(Collectors.toList()))
                .channelName(post.getChannel().getName())
                .votes(post.getVotes().stream().map(Vote::getId).collect(Collectors.toList()))
                .build();
    }
}
