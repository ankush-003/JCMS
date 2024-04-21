package com.example.postgres.service.frontend;

import com.example.postgres.view.list.Home;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.WebStorage;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Base64;

@Service
public class LoginService {

    public static boolean authenticate(String username, String password) {
        HttpURLConnection connection = null;
        try {
            // Encode credentials
            String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

            // Prepare connection
            URI uri = URI.create("http://localhost:8080");
            URI signInUri = uri.resolve("sign-in");
            URL url = signInUri.toURL();


            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Basic " + credentials);
            connection.setDoOutput(true);

//            // Send request
//            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
//                // You can write request body here if needed
//                // writer.write("data=value");
//            }

            // Check response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Authentication failed. Response Code: " + responseCode);
                return false;
            }

            // Read response
            String accessToken = getString(connection);
            System.out.println("Access Token: " + accessToken);

            // Store access token in web storage
            WebStorage.setItem("access_token", accessToken);

            return true;

        } catch (IOException e) {
            System.err.println("Error in authentication: " + e.getMessage());
            return false;
        } finally {
            // Disconnect connection
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String getString(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Parse JSON response
        JSONObject jsonResponse = new JSONObject(response.toString());
        String accessToken = jsonResponse.getString("access_token");
        return accessToken;
    }

    public void navigateToMainView() {
        // Navigate to the main view after successful authentication
        UI.getCurrent().navigate(Home.class);
    }
}
