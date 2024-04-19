package com.example.postgres.service;

import com.example.postgres.classes.Channel;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import com.vaadin.flow.component.virtuallist.VirtualList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ChannelServiceFrontend {
    public boolean addChannel(Channel channel, String accessToken, Long ownerId) {
        System.out.println(channel.getName() + " " + channel.getDescription());
        HttpURLConnection connection = null;
        try {
            URI baseUri = URI.create("http://localhost:8080/api/"); // Change to http instead of https
            URI channelsUri = baseUri.resolve("channels");
            URL url = channelsUri.toURL();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setDoOutput(true);

            //Creating the JSON Object
            JSONObject channelJson = new JSONObject();
            channelJson.put("name", channel.getName());
            channelJson.put("description", channel.getDescription());

            JSONObject ownerJson = new JSONObject();
            ownerJson.put("id", ownerId);

            channelJson.put("owner", ownerJson);

            System.out.println("Channel JSON: " + channelJson);

            // Send JSON data
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(channelJson.toString());
            }

            // Check response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Adding channel failed. Response Code: " + responseCode);
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

            System.out.println(response);
            return true;
        } catch (IOException e) {
            System.err.println("Error adding channel: " + e.getMessage());
            return false;
        } finally {
            // Disconnect connection
            if (connection != null) {
                connection.disconnect();
            }
        }
    }



    public void setChannels(VirtualList<Channel> virtualList, String accessToken, Long userID) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/users/channels/" + userID) // Use the correct URL here
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                System.err.println("Getting channels failed. Response Code: " + response.code());
                return;
            }

            String responseBody = response.body().string();
            System.out.println("The response is this :");
            System.out.println(responseBody);

            // Create Gson instance with custom configuration to exclude fields without @Expose annotation
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            Channel[] channels = gson.fromJson(responseBody, Channel[].class);
            System.out.println(Arrays.toString(channels));
            List<Channel> myChannels = Arrays.stream(channels)
                    .map(c -> new Channel(c.getName(), c.getDescription()))
                    .collect(Collectors.toList());
            virtualList.setItems(myChannels);

        } catch (IOException e) {
            System.err.println("Error getting channels: " + e.getMessage());
        }
    }

}
