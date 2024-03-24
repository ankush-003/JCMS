package com.example.postgres.classes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "students" , schema = "public")//table name in the database can be different from the class name using the @Table annotation
public class Student
{
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @GeneratedValue // by default it is AUTO
    private Integer id;

    @Column(length = 20)
    private String firstname;

    @Column(length = 20)
    private String lastname;

    @Column(name = "mail" ,  unique = true)//column name in the database can be different from the class name using the @Column annotation
    private String email;

    @Column(name = "age")
    private int age;


    @Column(
            updatable = false
    )
    private String some_col;


    @OneToOne(
            mappedBy = "student",
            cascade = CascadeType.ALL
    )
    private StudentProfile studentProfile;


    @ManyToOne
    @JoinColumn(
            name = "school_id",
            referencedColumnName = "id"
    )
    @JsonBackReference
    private School school;

    public Student(Integer id, String firstname, String lastname, String email, int age, String some_col, StudentProfile studentProfile, School school) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.age = age;
        this.some_col = some_col;
        this.studentProfile = studentProfile;
        this.school = school;
    }

    public Student()
    {

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;}



    public String getSome_col() {
        return some_col;
    }

    public void setSome_col(String some_col) {
        this.some_col = some_col;
    }

}
