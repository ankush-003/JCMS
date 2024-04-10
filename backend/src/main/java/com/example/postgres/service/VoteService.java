// Write Vote Service similar to Comment Service
//
// Path: src/main/java/com/example/postgres/service/VoteService.java
//

package com.example.postgres.service;

import com.example.postgres.classes.Vote;
import com.example.postgres.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }

    public List<Vote> findAllVotes() {
        return voteRepository.findAll();
    }

    public Vote findByVoteId(Long id) {
        return voteRepository.findById(id).orElse(null);
    }

    public ResponseEntity<String> deleteAllVotes() {
        voteRepository.deleteAll();
        return ResponseEntity.ok("All votes deleted");
    }

    public ResponseEntity<String> deleteVoteById(Long id) {
        voteRepository.deleteById(id);
        return ResponseEntity.ok("Vote deleted");
    }

    public ResponseEntity<Vote> updateVote(Long id, Vote vote) {
        Vote existingVote = voteRepository.findById(id).orElse(null);
        if (existingVote == null) {
            return ResponseEntity.notFound().build();
        }
        existingVote.setVoteType(vote.getVoteType());
        return ResponseEntity.ok(voteRepository.save(existingVote));
    }
}