package com.example.postgres.classes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "schools" , schema = "public")
public class School
{
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    //We need to have a default constructor for the JPA since it uses reflection to create an instance of the class
    public School()
    {

    }

    public School(Integer id, String name, List<Student> students) {
        this.id = id;
        this.name = name;
        this.students = students;
    }

    @OneToMany(
            mappedBy = "school"
    )
    @JsonManagedReference
    private List<Student> students;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public School(String name) {
        this.name = name;
    }
}
