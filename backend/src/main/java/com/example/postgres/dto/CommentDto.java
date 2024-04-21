package com.example.postgres.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

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

    @JsonProperty("date_time")
    Instant dateTime;


}
