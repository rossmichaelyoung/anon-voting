package com.rossyoung.anonvoting.playervote;

import com.rossyoung.anonvoting.election.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerVoteController {

    private final PlayerVoteService playerVoteService;
    private final ElectionService electionService;

    @Autowired
    public PlayerVoteController(PlayerVoteService playerVoteService, ElectionService electionService) {
        this.playerVoteService = playerVoteService;
        this.electionService = electionService;
    }

    @PostMapping("api/players/vote/{electionId}")
    public void vote(@RequestBody PlayerVote playerVote, @PathVariable String electionId) {
        playerVoteService.playerVoted(playerVote, electionId);
    }

    @PostMapping("api/players/hasvoted/{electionId}")
    @ResponseBody
    public boolean hasVoted(@RequestBody String username, @PathVariable String electionId) {
        if(electionService.doesElectionExist(electionId))
            return playerVoteService.hasPlayerVoted(username, electionId);

        return true;
    }
}
