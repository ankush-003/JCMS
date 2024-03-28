package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "CHANNEL")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    private Instant dateCreated;

    // relationships
    @OneToMany(mappedBy = "channel", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name="owner_id")
    @JsonBackReference
    private User owner;

    /*
    @ManyToMany
    @JoinTable(
            name = "SUBSCRIBERS",
            joinColumns = {
                    @JoinColumn(name = "channel_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id")
            }
    )
    private List<User> subscribers;
     */
}
