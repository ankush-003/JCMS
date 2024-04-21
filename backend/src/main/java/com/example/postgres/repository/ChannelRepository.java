package com.example.postgres.repository;

import com.example.postgres.classes.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findByName(String name);
}
