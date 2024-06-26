package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nimbusds.jose.shaded.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "CHANNEL")
@JsonIdentityInfo(
        scope = Channel.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Channel {
    @Expose
    @Id
    @GeneratedValue
    private Long id;


    @Expose
    @Column(name = "name", nullable = false)
    private String name;


    @Expose
    @Column(name = "description", nullable = false)
    private String description;

    @CreationTimestamp
    private Instant dateCreated;

    // relationships
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY)
    @JsonIdentityReference(
            alwaysAsId = true
    )
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name="owner_id")
    @JsonBackReference(value="channel-owner")
    private User owner;

    @ManyToMany( cascade = CascadeType.ALL)
    @JsonIdentityReference(
            alwaysAsId = true
    )
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

    public Channel(String name, String description, Long Id) {
        this.name = name;
        this.description = description;
        this.id = Id;
        this.dateCreated = Instant.now();
        this.subscribers = List.of();
    }

    public Channel(long id, String channelName, String description, Instant dateCreated) {
        this.id = id;
        this.name = channelName;
        this.description = description;
        this.dateCreated = dateCreated;
    }
}
