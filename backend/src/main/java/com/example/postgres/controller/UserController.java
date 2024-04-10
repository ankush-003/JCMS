// Make User Controller similar to Channel Controller

// Path: src/main/java/com/example/postgres/controller/UserController.java
package com.example.postgres.controller;

import com.example.postgres.classes.User;
import com.example.postgres.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/{user-id}")
    public User findByUserId(@PathVariable("user-id") Long id) {
        return userService.findByUserId(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteAllUsers() {
        return userService.deleteAllUsers();
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/{user-id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("user-id") Long id) {
        return userService.deleteUserById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
    @PutMapping("/{user-id}")
    public ResponseEntity<User> updateUser(@PathVariable("user-id") Long id,
                                           @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}