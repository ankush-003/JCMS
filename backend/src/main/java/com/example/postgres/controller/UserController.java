// Make User Controller similar to Channel Controller

// Path: src/main/java/com/example/postgres/controller/UserController.java
package com.example.postgres.controller;

import com.example.postgres.classes.User;
import com.example.postgres.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public User saveUser(
            @RequestBody User user
    ) {
        return userService.saveUser(user);
    }

    @GetMapping("")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{user-id}")
    public User findByUserId(
            @PathVariable("user-id") Long id
    ) {
        return userService.findByUserId(id);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteAllUsers() {
        return userService.deleteAllUsers();
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<String> deleteUserById(
            @PathVariable("user-id") Long id
    ) {
        return userService.deleteUserById(id);
    }

    @PutMapping("/{user-id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("user-id") Long id,
            @RequestBody User user
    ) {
        return userService.updateUser(id, user);
    }
}