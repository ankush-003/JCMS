package com.example.postgres.service;


import com.example.postgres.classes.Vote;
import com.example.postgres.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }

    public List<Vote> findAllVotes() {
        return voteRepository.findAll();
    }

    public ResponseEntity<Vote> updateVote(Long voteId, Vote vote) {
        Vote voteToUpdate = voteRepository.findById(voteId).orElse(null);

        if (voteToUpdate == null) {
            return null;
        }
        voteToUpdate.setVoteType(vote.getVoteType());
        Vote updatedVote = voteRepository.save(voteToUpdate);
        return ResponseEntity.ok(updatedVote);
    }
}
