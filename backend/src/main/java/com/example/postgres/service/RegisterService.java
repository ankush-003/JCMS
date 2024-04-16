package com.example.postgres.service;

import com.example.postgres.view.list.Home;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.WebStorage;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Service
public class RegisterService {

    public boolean registerUser(String username, String password, String name, String email, String role) {
        HttpURLConnection connection = null;
        try {
            // Construct the URL
            URI baseUri = URI.create("http://localhost:8080/");
            URI signUpUri = baseUri.resolve("sign-up");
            URL url = signUpUri.toURL();

            // Open connection
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Prepare JSON data
            String jsonInputString = String.format("{\"name\": \"%s\", \"user_name\": \"%s\", \"userEmail\": \"%s\", \"userPassword\": \"%s\", \"userRole\": \"%s\"}", name, username, email, password, role);

            // Send JSON data
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(jsonInputString);
            }

            // Check response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Registration failed. Response Code: " + responseCode);
                return false;
            }

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse response JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            String accessToken = jsonResponse.getString("access_token");
            System.out.println("Access Token: " + accessToken);

            // Store access token in web storage
            WebStorage.setItem("access_token", accessToken);
            return true;

        } catch (IOException e) {
            System.err.println("Error in Registration: " + e.getMessage());
            return false;
        } finally {
            // Disconnect connection
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void navigateToMainView() {
        UI.getCurrent().navigate(Home.class);
    }
}
