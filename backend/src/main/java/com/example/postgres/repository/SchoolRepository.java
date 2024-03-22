package com.example.postgres.repository;

import com.example.postgres.classes.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School,Integer> {
}
