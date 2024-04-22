package com.example.postgres.service.frontend;

import com.example.postgres.dto.AuthResponseDto;
import com.example.postgres.view.list.Home;
import com.nimbusds.jose.shaded.gson.Gson;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.WebStorage;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;

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
                ResponseBody responseBody = response.body();
                // Handle successful response
                Gson gson = new Gson();
                AuthResponseDto entity = gson.fromJson(responseBody.string(), AuthResponseDto.class);
                System.out.println(entity.getAccessToken());

                WebStorage.setItem("access_token", entity.getAccessToken());
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
        UI.getCurrent().getPage().reload();
        UI.getCurrent().navigate(Home.class);
    }
}
