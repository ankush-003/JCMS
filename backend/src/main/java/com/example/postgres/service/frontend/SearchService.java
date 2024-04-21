package com.example.postgres.service.frontend;

import com.example.postgres.classes.Channel;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    private final Gson gson = new Gson();

    public final List<Channel> searchChannelBy(String query, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/channels/search/" + query)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Response response = client.newCall(request).execute();

        JsonArray jsonArray = gson.fromJson(response.body().string(), JsonArray.class);
        List<Channel> channels = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            long id = jsonObject.get("id").getAsLong();
            String channelName = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            Instant dateCreated = Instant.parse(jsonObject.get("dateCreated").getAsString());
            Channel channel = new Channel(id, channelName, description, dateCreated);
            channels.add(channel);
        }
        return channels;
    }
}
