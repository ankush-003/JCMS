package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "comments", schema = "public")
@JsonIdentityInfo(
        scope = Comment.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    private Instant created_at;

    // relationships
    @ManyToOne
    @JoinColumn(name="postid")
    @JsonBackReference(value="comment-post")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonBackReference(value="comment-user")
    private User user;
}
