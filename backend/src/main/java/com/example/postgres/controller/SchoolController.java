package com.example.postgres.controller;


import com.example.postgres.classes.School;
import com.example.postgres.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SchoolController
{
    @Autowired
    private SchoolRepository schoolRepository;


    @PostMapping("/schools")
    public School createSchool(@RequestBody School school)
    {
        return schoolRepository.save(school);
    }


    @GetMapping("/schools")
    public List<School> findAllSchools()
    {
        return schoolRepository.findAll();
    }

}
