package com.example.postgres.dto;

import com.example.postgres.classes.Comment;
import com.example.postgres.classes.Post;
import com.example.postgres.classes.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Comparator;
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
    private List<CommentDto> comments; // Changed to use CommentDto
    private String channelName;
    private List<Long> votes;

    public static PostDto fromPostEntity(Post post) {
        List<CommentDto> commentDtos = post.getComments().stream()
                .map(comment -> CommentDto.builder()
                        .UserName(comment.getUser().getUsername())
                        .commentText(comment.getDescription())
                        .dateTime(comment.getCreated_at())
                        .build())
                .sorted(Comparator.comparing(CommentDto::getDateTime))
                .collect(Collectors.toList());

        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .created_at(post.getCreated_at())
                .userName(post.getUser().getUsername())
                .comments(commentDtos) // Set CommentDto list directly
                .channelName(post.getChannel().getName())
                .votes(post.getVotes().stream().map(Vote::getId).collect(Collectors.toList()))
                .build();
    }
}
