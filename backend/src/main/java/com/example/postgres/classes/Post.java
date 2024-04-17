package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "posts", schema = "public")
@JsonIdentityInfo(
        scope = Post.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

//    @Lob
    @Column(name = "description", nullable = false)
    private String description;

//    @Lob
    @Nullable
    @Column(name = "content")
    private byte[] content;

    @CreationTimestamp
    private Instant created_at;


    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonBackReference(value="post-user")
    @JsonIdentityReference(
            alwaysAsId = true
    )
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @JsonManagedReference(value="comment-post")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "channel_id")
//    @JsonBackReference(value="post-channel")
    @JsonIdentityReference(
            alwaysAsId = true
    )
    private Channel channel;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference(value="post-vote")
    @JsonIdentityReference(
            alwaysAsId = true
    )
    private List<Vote> votes;
}
