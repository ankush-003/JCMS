// Write ChannelService similar to post and comment service

// Path: src/main/java/com/example/postgres/service/ChannelService.java
package com.example.postgres.service.backend;

import com.example.postgres.classes.Channel;
import com.example.postgres.repository.ChannelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public Channel saveChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    public Channel findByChannelId(Long id) {
        return channelRepository.findById(id).orElse(null);
    }

    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    public ResponseEntity<String> deleteChannelById(Long id) {
        channelRepository.deleteById(id);
        String message = "Channel with id " + id + " has been successfully deleted.";
        return ResponseEntity.ok().body(message);

    }

    public ResponseEntity<String> deleteAllChannels() {
        channelRepository.deleteAll();
        String message = "All Channels have been successfully deleted.";
        return ResponseEntity.ok().body(message);
    }

    public ResponseEntity<Channel> updateChannel(Long id, Channel channel) {
        Channel channelToUpdate = channelRepository.findById(id).orElse(null);

        if (channelToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        channelToUpdate.setName(channel.getName());
        channelToUpdate.setDescription(channel.getDescription());
        Channel updatedChannel =  channelRepository.save(channelToUpdate);
        return ResponseEntity.ok(updatedChannel);
    }

}