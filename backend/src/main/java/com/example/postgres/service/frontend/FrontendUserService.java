package com.example.postgres.service.frontend;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import okhttp3.OkHttpClient;

import java.io.IOException;

@Service
public class FrontendUserService {
    public static boolean isUserLoggedIn(String token) {
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
}
