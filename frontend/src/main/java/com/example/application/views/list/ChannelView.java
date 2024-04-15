package com.example.application.views.list;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.example.postgres.service.ChannelService;
import com.example.postgres.classes.Channel;

@Route(value = "channel/:channelId", layout = MainLayout.class)
public class ChannelView extends VerticalLayout implements HasUrlParameter<String> {

    String channelId;
    private final ChannelService channelService;

    public ChannelView(ChannelService channelService) {
        this.channelService = channelService;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        channelId = parameter;
        Channel channel = channelService.findByChannelId(Long.parseLong(channelId));
        // Now you can use channelId to load the channel data
    }
}
