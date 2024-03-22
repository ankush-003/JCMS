package com.example.postgres.classes;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "student_profile" , schema = "public")
public class StudentProfile {
    @Id
    @GeneratedValue
    private Integer Id;

    private String bio;



    @OneToOne
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "id"
    )
    private Student student;
    //make sure this is the same name as the mappedBy ield in the Student class ONE TO ONE mapping


    public StudentProfile(Integer id, String bio, Student student) {
        Id = id;
        this.bio = bio;
        this.student = student;
    }

    public StudentProfile() {

    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public StudentProfile(String bio) {
        this.bio = bio;
    }


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}