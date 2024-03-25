package com.example.postgres.classes;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "posts", schema = "public")
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Nullable
    @Column(name = "content")
    private byte[] content;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime createdAt;

    // relationships

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;
}
