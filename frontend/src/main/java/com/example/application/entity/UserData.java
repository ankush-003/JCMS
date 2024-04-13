package com.example.application.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserData {
    @JsonProperty("name")
    private String name;


    @JsonProperty("user_name")
    private String user_name;

    @JsonProperty("user_email")
    private String user_email;

    @JsonProperty("user_id")
    private String user_id;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("password")
    private String password;
}