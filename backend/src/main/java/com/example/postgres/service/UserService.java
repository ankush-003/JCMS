// Write UserService similar to post and comment service

// Path: src/main/java/com/example/postgres/service/UserService.java
package com.example.postgres.service;

import com.example.postgres.classes.Channel;
import com.example.postgres.classes.User;
import com.example.postgres.dto.UserDetailsDto;
import com.example.postgres.repository.ChannelRepository;
import com.example.postgres.repository.UserRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.page.WebStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vaadin.flow.data.binder.Binder;
import java.net.URL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

        //Join Table Updates
        user.getSubscribedChannels().add(channel);
        channel.getSubscribers().add(user);
        userRepository.save(user);
        channelRepository.save(channel);

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

    public UserDetailsDto getUserData(Binder<UserDetailsDto> binder) {
        UserDetailsDto userData = new UserDetailsDto();

        showStoredValue(userData, () -> {
            System.out.println("Access Token: " + userData.getAccessToken());

            try {
                System.out.println("Making API request...");
                System.out.println("aryaaaaa Access Token: " + userData.getAccessToken());
                URL url = new URL("http://localhost:8080/api/users/user");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + userData.getAccessToken());
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                System.out.println("Response Code: " + responseCode);

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response);
                    reader.close();
                    Gson gson = new Gson();
                    UserDetailsDto userData1 = gson.fromJson(response.toString(), UserDetailsDto.class);
                    Long ID = userData1.getUser_id();

                    binder.setBean(userData1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return userData;
    }
    public UserDetailsDto getUserData() {
        UserDetailsDto userData = new UserDetailsDto();

        showStoredValue(userData, () -> {

            try {
                System.out.println("Making API request to get User Details");
                URL url = new URL("http://localhost:8080/api/users/user");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + userData.getAccessToken());
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                System.out.println("Response Code: " + responseCode);

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response);
                    reader.close();
                    Gson gson = new Gson();
                    UserDetailsDto userData1 = gson.fromJson(response.toString(), UserDetailsDto.class);
                    Long ID = userData1.getUser_id();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return userData;
    }

    private void showStoredValue(UserDetailsDto userData, Runnable callback) {
        WebStorage.getItem(
                "access_token",
                value -> {
//                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    userData.setAccessToken(value);
                    callback.run();
                }
        );
    }
}