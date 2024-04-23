package com.example.postgres.repository;

import com.example.postgres.classes.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByChannelId(Long channelId);

    List<Post> findByChannelName(String channelName);

    @Query("SELECT p FROM Post p WHERE p.channel.name = :channelName ORDER BY p.created_at DESC LIMIT :limit")
    List<Post> findByChannelNameModified(String channelName, int limit);

    @Query("SELECT p FROM Post p WHERE p.channel.id = :channelId ORDER BY p.created_at DESC LIMIT :limit")
    List<Post> findByChannelIdModified(@Param("channelId") Long channelId, @Param("limit") int limit);

    @Query("SELECT p FROM Post p ORDER BY p.created_at DESC LIMIT :limit")
    List<Post> findAllPostsModified(@Param("limit") int limit);
}