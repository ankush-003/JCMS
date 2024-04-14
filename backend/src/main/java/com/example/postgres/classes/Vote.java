package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "votes", schema = "public")
@JsonIdentityInfo(
        scope = Vote.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"

)
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
    @JoinColumn(name = "id" , referencedColumnName = "id")
    @JsonBackReference(value = "user-vote")
    private User user;
}
