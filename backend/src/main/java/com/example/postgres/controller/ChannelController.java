// Write Channel Controller similar to Comment Controller

// Path: src/main/java/com/example/postgres/controller/ChannelController.java
package com.example.postgres.controller;

import com.example.postgres.classes.Channel;
import com.example.postgres.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/channels")
public class ChannelController
{
    @Autowired
    private ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("")
    public Channel saveChannel(
            @RequestBody Channel channel
    ){
        return channelService.saveChannel(channel);
    }

    @GetMapping("")
    public List<Channel> findAllChannels()
    {
        return channelService.findAllChannels();
    }

    @GetMapping("/{channel-id}")
    public Channel findByChannelId(
            @PathVariable("channel-id") Long id
    )
    {
        return channelService.findByChannelId(id);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteAllChannels() {
        return channelService.deleteAllChannels();
    }

    @DeleteMapping("/{channel-id}")
    public ResponseEntity<String> deleteChannelById(
            @PathVariable("channel-id") Long id
    ) {
        return channelService.deleteChannelById(id);
    }

    @PutMapping("/{channel-id}")
    public ResponseEntity<Channel> updateChannel(
            @PathVariable("channel-id") Long id,
            @RequestBody Channel channel
    ) {
        return channelService.updateChannel(id, channel);
    }
}