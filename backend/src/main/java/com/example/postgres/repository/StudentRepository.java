package com.example.postgres.repository;

import com.example.postgres.classes.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{

    List<Student> findAllByFirstnameContaining(String firstname);
}
