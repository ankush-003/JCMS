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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class RegisterService {


    public boolean registerUser(String username, String password, String name, String email, String role) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("application/json");

        // Create a JSON object
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userName", username);
        jsonBody.put("name", name);
        jsonBody.put("userEmail", email);
        jsonBody.put("userPassword", password);
        jsonBody.put("userRole", role);

        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

        Request request = new Request.Builder()
                .url("http://localhost:8080/sign-up") // Use the correct URL here
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Handle successful response
                String accessToken = response.body().string();
                System.out.println("Access Token: " + accessToken);
                // Store access token in web storage
                WebStorage.setItem("access_token", accessToken);
                return true;
            } else {
                System.err.println("Registration failed. Response Code: " + response.code());
                return false;
            }
        } catch (IOException e) {
            System.err.println("Error in Registration: " + e.getMessage());
            return false;
        }
    }


    public void navigateToMainView() {
        UI.getCurrent().navigate(Home.class);
    }
}
