package com.example.postgres.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;

    // relationships
    @ManyToOne
    @JoinColumn(name="postid", referencedColumnName = "id")
    private Post post;

    @ManyToOne
    @JoinColumn(
            name = "userid",
            referencedColumnName = "id"
    )
    private User user;
}
