package com.example.postgres.controller;

import com.example.postgres.classes.Channel;
import com.example.postgres.repository.ChannelRepository;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

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

    @GetMapping(path = "/{id}")
    public Channel getChannelById(@PathVariable("id") int channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @PutMapping(path = "/{id}")
    public Channel update(@RequestBody Channel channel) {
        int channelId = channel.getId();
        var existing = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        return channelRepository.save(new Channel(
                existing.getId(), channel.getName(), channel.getDescription()));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") int channelId) {
        channelRepository.deleteById(channelId);
    }
}
