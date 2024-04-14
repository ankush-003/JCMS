// Write Vote Controller similar to Comment Controller
//
// Path: src/main/java/com/example/postgres/controller/VoteController.java
package com.example.postgres.controller;

import com.example.postgres.classes.Vote;
import com.example.postgres.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/channel/{channelId}/post/{postId}/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("")
    public Vote saveVote(@RequestBody Vote vote) {
        return voteService.saveVote(vote);
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("")
    public List<Vote> findAllVotes() {
        return voteService.findAllVotes();
    }

    @PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/{vote-id}")
    public Vote findByVoteId(@PathVariable("vote-id") Long id) {
        return voteService.findByVoteId(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteAllVotes() {
        return voteService.deleteAllVotes();
    }

    @PreAuthorize("hasAuthority('SCOPE_DELETE')")
    @DeleteMapping("/{vote-id}")
    public ResponseEntity<String> deleteVoteById(@PathVariable("vote-id") Long id) {
        return voteService.deleteVoteById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_UPDATE')")
    @PutMapping("/{vote-id}")
    public ResponseEntity<Vote> updateVote(@PathVariable("vote-id") Long id,
                                           @RequestBody Vote vote) {
        return voteService.updateVote(id, vote);
    }
}