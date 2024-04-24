package com.example.postgres.controller;

import com.example.postgres.classes.Channel;
import com.example.postgres.service.backend.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/api/channels")
    public Channel saveChannel(@RequestBody Channel channel) {
        return channelService.saveChannel(channel);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/api/channels")
    public List<Channel> findAllChannels() {
        return channelService.findAllChannels();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/api/channels/{channel-id}")
    public Channel findByChannelId(@PathVariable("channel-id") Long id) {
        return channelService.findByChannelId(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/api/channels")
    public ResponseEntity<String> deleteAllChannels() {
        return channelService.deleteAllChannels();
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/api/channels/{channel-id}")
    public ResponseEntity<String> deleteChannelById(@PathVariable("channel-id") Long id) {
        return channelService.deleteChannelById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
    @PutMapping("/api/channels/{channel-id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable("channel-id") Long id,
                                                 @RequestBody Channel channel) {
        return channelService.updateChannel(id, channel);
    }

//    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/channels/search/{name}")
    public ResponseEntity<List<Channel>> findByChannelName(@PathVariable(required = false) String name) {
        return channelService.searchChannelBy(name);
    }

    @GetMapping("/channels/search")
    public ResponseEntity<List<Channel>> findByChannelName() {
        return channelService.searchAll();
    }
}
