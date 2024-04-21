package com.example.postgres.repository;

import com.example.postgres.classes.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    Channel findByName(String name);

    @Query("SELECT c FROM Channel c WHERE c.name LIKE %:name%")
    List<Channel> searchChannelBy(String name);
}
