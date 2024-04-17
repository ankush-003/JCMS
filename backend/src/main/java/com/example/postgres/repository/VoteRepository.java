package com.example.postgres.repository;

import com.example.postgres.classes.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findVotesByPostId(Long id);
}
