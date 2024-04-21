package com.example.postgres.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {


    @JsonProperty("user_name")
    String UserName;

    @JsonProperty("comment_text")
    String commentText;

}
