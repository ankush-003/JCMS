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
import java.util.Objects;

public class SearchService {

    private final Gson gson = new Gson();

    public final List<Channel> searchChannelBy(String query, String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request;
        System.out.println("Recieved Token: " + query);
        if (query == null) {
            request = new Request.Builder()
                    .url("http://localhost:8080/api/channels/search")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
        } else {
        request = new Request.Builder()
                .url("http://localhost:8080/api/channels/search/" + query)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        }

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


// Give what each class does in the context of the project
//Comment.java : This class is used to represent a comment object. It has fields for the comment id, the comment text, the user who made the comment, the post the comment is on, and the date the comment was created.
//CommentDto.java : This is a dto class that is used to represent a comment object.
//CommentService.java : This class is used to handle the business logic for comments. It has methods for getting all comments for a post, adding a comment to a post, and deleting a comment.
//CommentController.java : This class is used to handle the HTTP requests for comments. It has methods for getting all comments for a post, adding a comment to a post, and deleting a comment.
//CommentRepository.java : This interface is used to interact with the database for comments. It has methods for finding comments by post id, saving a comment, and deleting a comment.
//ChannelServiceFrontend.java : This class is used to handle the frontend logic for channels. It has methods for getting all channels, getting the posts for a channel, and subscribing to a channel.
//FrontendUserService.java : This class is used to handle the frontend logic for users. It has methods for checking if a user is logged in and getting the channels a user is subscribed to.
//SearchService.java : This class is used to handle the frontend logic for searching. It has a method for searching for channels by a query string.





