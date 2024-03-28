package com.example.postgres.controller;

import com.example.postgres.classes.Channel;
import com.example.postgres.repository.ChannelRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/channel")
public class ChannelController {
    private final ChannelRepository channelRepository;

    public ChannelController(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @PostMapping
    public Channel create(@RequestBody Channel channel) {
        return channelRepository.save(channel);
    }
}