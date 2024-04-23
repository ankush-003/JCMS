package com.example.postgres.service.frontend;

import com.example.postgres.classes.Channel;
import com.example.postgres.dto.UserDetailsDto;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class FrontendUserService {
    public final boolean isUserLoggedIn(String token) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/user")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
            return response.isSuccessful();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final List<String> getSubscribedChannels(String token) {
        try {
            System.out.println("Making API request to get User Details");
            URL url = new URL("http://localhost:8080/api/users/user");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " +token);
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
                UserDetailsDto userDetailsDto = gson.fromJson(response.toString(), UserDetailsDto.class);
                System.out.println("User Details: " + userDetailsDto + " Subscribed Channels: " + userDetailsDto.getSubscribed_channels());
                return userDetailsDto.getSubscribed_channels();
            } else {
                throw new IOException("Failed to fetch user data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user data", e);
        }
    }
    public final String getUsername(String token) {
        try {
            System.out.println("Making API request to get User Details");
            URL url = new URL("http://localhost:8080/api/users/user");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " +token);
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
                UserDetailsDto userDetailsDto = gson.fromJson(response.toString(), UserDetailsDto.class);
                System.out.println("User Details: " + userDetailsDto + " Subscribed Channels: " + userDetailsDto.getSubscribed_channels());
                return userDetailsDto.getUser_name();
            } else {
                throw new IOException("Failed to fetch user data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user data", e);
        }
    }



}


