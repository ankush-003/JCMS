// Write Vote Controller similar to Comment Controller
//
// Path: src/main/java/com/example/postgres/controller/VoteController.java
package com.example.postgres.controller;

import com.example.postgres.classes.Vote;
import com.example.postgres.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel/{channelId}/post/{postId}/votes")
public class VoteController {
    @Autowired
    private VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("")
    public Vote saveVote(
            @RequestBody Vote vote
    ){
        return voteService.saveVote(vote);
    }

    @GetMapping("")
    public List<Vote> findAllVotes()
    {
        return voteService.findAllVotes();
    }

    @GetMapping("/{vote-id}")
    public Vote findByVoteId(
            @PathVariable("vote-id") Long id
    )
    {
        return voteService.findByVoteId(id);

    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteAllVotes() {
        return voteService.deleteAllVotes();
    }

    @DeleteMapping("/{vote-id}")
    public ResponseEntity<String> deleteVoteById(
            @PathVariable("vote-id") Long id
    ) {
        return voteService.deleteVoteById(id);
    }

    @PutMapping("/{vote-id}")
    public ResponseEntity<Vote> updateVote(
            @PathVariable("vote-id") Long id,
            @RequestBody Vote vote
    ) {
        return voteService.updateVote(id, vote);
    }
}