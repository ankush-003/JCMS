// Make User Controller similar to Channel Controller

// Path: src/main/java/com/example/postgres/controller/UserController.java
package com.example.postgres.controller;

import com.example.postgres.classes.Channel;
import com.example.postgres.classes.User;
import com.example.postgres.config.jwt.JwtTokenUtils;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.service.backend.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    public UserController(UserService userService , JwtTokenUtils jwtTokenUtils) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
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

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
    @PutMapping("/{user-id}/subscribe/{channel-id}")
    public ResponseEntity<User> subscribeToChannel(@PathVariable("channel-id") Long channel_id,
                                                   @PathVariable("user-id") Long user_id) {
        return userService.subscribeToChannel(channel_id, user_id);
    }


    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/user")
    public ResponseEntity<UserDetailsDto> getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader)
    {
        System.out.println("get Token process has started! ");

        UserDetailsDto user =  jwtTokenUtils.getUserDetails(authorizationHeader);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/channels/{user-id}")
    public ResponseEntity<List<Channel>> getUserChannels(@PathVariable("user-id") Long id) {

        List<Channel> channels = userService.getUserChannels(id);
        return ResponseEntity.ok(channels);

    }

}