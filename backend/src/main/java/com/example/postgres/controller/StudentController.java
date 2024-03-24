package com.example.postgres.controller;

import com.example.postgres.classes.Student;
import com.example.postgres.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class    StudentController
{
    @Autowired
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @PostMapping("/students")
    public Student postsNewStudent(
            @RequestBody Student student
    ){
    return studentRepository.save(student);
    }

    @GetMapping("/students")
    public List<Student> findAllStudents()
    {
        return studentRepository.findAll();
    }

    @GetMapping("/students/{student-id}")
    public Student findByStudentId(
            @PathVariable("student-id") Integer id
    )
    {
        return studentRepository.findById(id).orElse(null);

    }

    @DeleteMapping("/students")
    public ResponseEntity<String> deleteAllStudents() {
        studentRepository.deleteAll();
        String message = "All students have been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/students/{student-id}")
    public ResponseEntity<String> deleteStudentById(
            @PathVariable("student-id") Integer id
    ) {
        studentRepository.deleteById(id);
        String message = "Student with id " + id + " has been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    @PutMapping("/students/{student-id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable("student-id") Integer id,
            @RequestBody Student student
    ) {
        Student studentToUpdate = studentRepository.findById(id).orElse(null);
        if (studentToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        studentToUpdate.setFirstname(student.getFirstname());
        studentToUpdate.setLastname(student.getLastname());
        studentToUpdate.setEmail(student.getEmail());
        studentToUpdate.setAge(student.getAge());
        studentToUpdate.setSome_col(student.getSome_col());
        Student updatedStudent = studentRepository.save(studentToUpdate);
        return ResponseEntity.ok(updatedStudent);
    }


    @GetMapping("/students/firstname/{firstname}")
    public List<Student> findByFirstname(
            @PathVariable("firstname") String firstname
    )
    {
        return studentRepository.findAllByFirstnameContaining(firstname);
    }


}
