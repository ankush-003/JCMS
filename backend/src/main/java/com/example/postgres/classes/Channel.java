package com.example.postgres.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CHANNEL")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date_created", updatable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE", nullable = false)
    private LocalDate dateCreated;

    // relationships
    @OneToMany(mappedBy = "channel")
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name="owner_id", referencedColumnName = "id")
    private User owner;

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
}
