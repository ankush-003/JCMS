package com.example.application.services;

import com.example.application.entity.UserData;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.page.WebStorage;
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

        // Retrieve the access token asynchronously
        showStoredValue(userData, () -> {
            System.out.println("Access Token: " + userData.getAccessToken());

            // Once access token is retrieved, make API request
            try {

                System.out.println("Making API request...");
                System.out.println("aryaaaaa Access Token: " + userData.getAccessToken());
                // Create a URL object from the specified address
                URL url = new URL("http://localhost:8080/api/users/user");

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to GET
                connection.setRequestMethod("GET");

                connection.setRequestProperty("Content-Type", "application/json");

                // Add the Authorization header with the access token
                connection.setRequestProperty("Authorization", "Bearer " + userData.getAccessToken());

                // Set the connection timeout to 5 seconds
                connection.setConnectTimeout(5000);

                // Set the read timeout to 5 seconds
                connection.setReadTimeout(5000);

                // Get the response code from the connection
                int responseCode = connection.getResponseCode();

                System.out.println("Response Code: " + responseCode);

                // If the response code is 200 (OK), read the response
                if (responseCode == 200) {
                    // Create a BufferedReader object to read the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    // Create a StringBuilder object to store the response
                    StringBuilder response = new StringBuilder();

                    // Read the response line by line
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response);
                    // Close the reader
                    reader.close();
                    // Parse the response JSON string to a UserData object
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

    private void showStoredValue(UserData userData, Runnable callback) {
        WebStorage.getItem(
                "access_token",
                value -> {
                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    userData.setAccessToken(value);
                    callback.run(); // Call the callback after retrieving the access token
                }
        );
    }
}
