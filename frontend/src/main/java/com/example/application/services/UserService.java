package com.example.application.services;

import com.example.application.entity.UserData;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UserService {

    public void userDataCallback(PostService postService) {
        UserData userData = new UserData();

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
                    UserData userData1 = gson.fromJson(response.toString(), UserData.class);

                    String name = userData1.getName();
                    String userName = userData1.getUser_name();
                    String email = userData1.getUser_email();

                    System.out.println("Name: " + name);
                    System.out.println("User Name: " + userName);
                    System.out.println("Email: " + email);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        postService.getAllPosts(posts -> {
            System.out.println("Posts: " + posts);
        }, userData.getAccessToken());
    }

    public UserData getUserData(Binder<UserData> binder) {
        UserData userData = new UserData();

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
                    UserData userData1 = gson.fromJson(response.toString(), UserData.class);

                    String name = userData1.getName();
                    String userName = userData1.getUser_name();
                    String email = userData1.getUser_email();

                    binder.setBean(userData1);

                    System.out.println("Name: " + name);
                    System.out.println("User Name: " + userName);
                    System.out.println("Email: " + email);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return userData;
    }

    private void showStoredValue(UserData userData, Runnable callback) {
        WebStorage.getItem(
                "access_token",
                value -> {
                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    userData.setAccessToken(value);
                    callback.run();
                }
        );
    }
}
