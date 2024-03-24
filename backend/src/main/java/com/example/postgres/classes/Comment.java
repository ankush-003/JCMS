package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Comment" , schema = "public")
public class Comment {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(
            name = "postid",
            referencedColumnName = "id"
    )
    @JsonBackReference
    private Post post;

    @ManyToOne
    @JoinColumn(
            name = "userid",
            referencedColumnName = "id"
    )
    @JsonBackReference
    private User user;

    private String comment;

    private Date createdate;

    public Integer getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
}
