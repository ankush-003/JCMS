package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="REFRESH_TOKENS")
@JsonIdentityInfo(
        scope = RefreshToken.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;
    // Increase the length to a value that can accommodate your actual token lengths
    @Column(name = "REFRESH_TOKEN", nullable = false, length = 10000)
    private String refreshToken;


    //Indicative of the user has signed out or the token has expired
    @Column(name = "REVOKED")
    private boolean revoked;

    @ManyToOne
    @JsonBackReference(value = "user-refresh-token")
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
