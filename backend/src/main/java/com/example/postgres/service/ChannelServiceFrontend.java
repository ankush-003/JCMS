package com.example.postgres.service;

import com.example.postgres.classes.Channel;
import com.example.postgres.dto.UserDetailsDto;
import com.nimbusds.jose.shaded.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


@Service
public class ChannelServiceFrontend
{
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

            System.out.println("Channel JSON: " + channelJson.toString());

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

}
