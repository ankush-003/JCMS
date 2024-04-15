// Write Channel Controller similar to Comment Controller

// Path: src/main/java/com/example/postgres/controller/ChannelController.java
package com.example.postgres.controller;

import com.example.postgres.classes.Channel;
import com.example.postgres.service.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("")
    public Channel saveChannel(@RequestBody Channel channel) {
        return channelService.saveChannel(channel);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("")
    public List<Channel> findAllChannels() {
        return channelService.findAllChannels();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/{channel-id}")
    public Channel findByChannelId(@PathVariable("channel-id") Long id) {
        return channelService.findByChannelId(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteAllChannels() {
        return channelService.deleteAllChannels();
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/{channel-id}")
    public ResponseEntity<String> deleteChannelById(@PathVariable("channel-id") Long id) {
        return channelService.deleteChannelById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
    @PutMapping("/{channel-id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable("channel-id") Long id,
                                                 @RequestBody Channel channel) {
        return channelService.updateChannel(id, channel);
    }



}