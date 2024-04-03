package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "votes", schema = "public")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vote_type", nullable = false)
    private int voteType;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference(value = "post-vote")
    private Post post;

    @OneToOne
    @JoinColumn(name = "id")
    private User user;
}
