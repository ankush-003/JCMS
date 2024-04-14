package com.example.postgres.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto
{
    @JsonProperty("name")
    private String name;

@JsonProperty("user_name")
    private String userName;


@JsonProperty("user_email")
    private String userEmail;

@JsonProperty("user_id")
    private Long userId;
}
