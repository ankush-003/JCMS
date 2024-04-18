package com.example.postgres.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto
{
    @JsonProperty("name")
    private String name;

    @JsonProperty("user_name")
    private String user_name;

    @JsonProperty("user_email")
    private String user_email;

    @JsonProperty("user_id")
    private Long user_id;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("password")
    private String password;


    public UserDetailsDto(String name, String username, String email, Long id) {
        this.name = name;
        this.user_name = username;
        this.user_email = email;
        this.user_id = id;
    }
}
