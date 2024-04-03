package com.example.postgres.controller;

import com.example.postgres.classes.Vote;
import com.example.postgres.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/channel/{channelId}/post/{postId}/votes")
public class VoteController {
    @Autowired
    private VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public Vote saveVote(@RequestBody Vote vote) {
        return voteService.saveVote(vote);
    }

    @GetMapping("")
    public List<Vote> getAllVotes()
    {
        return voteService.findAllVotes();
    }

    @PutMapping("/{voteId}")
    public ResponseEntity<Vote> updateVote(
            @PathVariable("voteId") Long voteId,
            @RequestBody Vote vote
    ) {
        return voteService.updateVote(voteId, vote);
    }
}
