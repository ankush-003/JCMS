// Write UserService similar to post and comment service

// Path: src/main/java/com/example/postgres/service/UserService.java
package com.example.postgres.service;

import com.example.postgres.classes.Channel;
import com.example.postgres.classes.User;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.repository.ChannelRepository;
import com.example.postgres.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ChannelRepository channelRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.channelRepository = channelRepository;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUserId(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public ResponseEntity<String> deleteUserById(Long id) {
        userRepository.deleteById(id);
        String message = "User with id " + id + " has been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<String> deleteAllUsers() {
        userRepository.deleteAll();
        String message = "All Users have been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<User> subscribeToChannel(Long channel_id, Long user_id) {

        User user = userRepository.findById(user_id).orElse(null);
        Channel channel = channelRepository.findById(channel_id).orElse(null);
        if (user == null || channel == null) {
            return ResponseEntity.notFound().build();
        }

        user.getSubscribedChannels().add(channel);
        userRepository.save(user);


        return ResponseEntity.ok(user);
    }

    public ResponseEntity<User> updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id).orElse(null);

        if (userToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        User updatedUser =  userRepository.save(userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }









}