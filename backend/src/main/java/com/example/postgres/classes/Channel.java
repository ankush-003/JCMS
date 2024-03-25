package com.example.postgres.classes;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "CHANNEL")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    /*
    @OneToMany(mappedBy = "channel")
    List<Post> posts;
    */

    @Column
    private LocalDate dateCreated;

    public Channel(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateCreated = LocalDate.now();
    }

    public Channel() {}

    /*
    @ManyToOne
    @JoinColumn(
            name = "userid",
            referencedColumnName = "id"
    )
    User user;
    */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    /*
    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    */
}
