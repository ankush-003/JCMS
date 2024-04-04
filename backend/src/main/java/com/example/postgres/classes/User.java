package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    private Instant created_at;

    @Column(nullable = false, name = "roles")
    private String roles;

    //relationships
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference(value="post-user")
    private List<Post> posts;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference(value="comment-user")
    private List<Comment> comments;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @JsonManagedReference(value="channel-owner")
    private List<Channel> channels;

    @ManyToMany(mappedBy = "subscribers", fetch = FetchType.EAGER)
    private List<Channel> subscribedChannels;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference(value="user-vote")
    private Vote vote;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value="user-refresh-token")
    private List<RefreshToken> refreshTokens;

}
