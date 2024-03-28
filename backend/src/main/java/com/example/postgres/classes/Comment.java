package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "comments", schema = "public")
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
    @JsonBackReference
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonBackReference
    private User user;
}
