package com.example.postgres.view.list;

import com.example.postgres.classes.Channel;
import com.example.postgres.service.frontend.FrontendUserService;
import com.example.postgres.service.frontend.SearchService;
import com.example.postgres.view.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Route(value = "search", layout = MainLayout.class)
public class SearchView extends Div
        implements HasUrlParameter<String> {

    private Div loading;


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String s) {


        removeAll();
        String headingText;
        if (s == null) {
            headingText = "Showing All Channels";
        } else {
            headingText = String.format("You searched for %s!", s);
        }
        Div searchText = new Div( new Text(headingText));
        searchText.addClassName("search-text");
        add(searchText);
        loading = new Div(new Text("Loading..."));
        loading.addClassName("loading");
        add(loading);
        CheckTokenValidation(s);


    }


    public void CreateChannelElements(List<Channel> channels) {

        if (channels.isEmpty()) {
            Div noResults = new Div(new Text("No results found"));
            noResults.addClassName("no-results");
            loading.setVisible(false);
            add(noResults);
            return;
        }

        Div allChannels = new Div();
        allChannels.addClassName("all-channels");




        for (Channel channel : channels) {
            Div channelDiv = new Div();
            Div channelName = new Div();
            Div channelDescription = new Div();
            Div channelDate = new Div();
            Div subCount = new Div();

            // Navigate on clicking channel div to /channel/channelName
            channelDiv.addClickListener(e -> {
                getUI().ifPresent(ui -> ui.navigate("channel/" + channel.getName()));
            });

            Date myDate = Date.from(channel.getDateCreated());

            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
            String formattedDate = formatter.format(myDate);

            channelDiv.addClassName("channel-div");
            channelName.addClassName("channel-name");
            channelDescription.addClassName("channel-description");
            channelDate.addClassName("channel-date");
//            subCount.addClassName("sub-count");

            channelName.setText(channel.getName());
            channelDescription.setText(channel.getDescription());
            channelDate.setText("Created on " + formattedDate);
//            subCount.setText(channel.getSubscribers().size() + " subscribers");


            channelDiv.add(channelName, channelDescription, channelDate);
            allChannels.add(channelDiv);
        }
        loading.setVisible(false);
        add(allChannels);


    }

    private void CheckTokenValidation( String query) {
        WebStorage.getItem(
                "access_token",
                value -> {
                    System.out.println("Stored value: " + (value == null ? "<no value stored>" : value));
                    try {
                        LoginCheck(value, query);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }



    private void LoginCheck(String token, String query) throws IOException {
        FrontendUserService frontendUserService = new FrontendUserService();
        SearchService searchService = new SearchService();
        Boolean check  = frontendUserService.isUserLoggedIn(token);
        if (check) {
            List<Channel> channels = searchService.searchChannelBy(query, token);
            System.out.println("Got all channels : " + channels);
            CreateChannelElements(channels);
        }
        System.out.println("Check: " + check);

    }



}