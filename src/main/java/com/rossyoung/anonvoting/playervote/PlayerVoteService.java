package com.rossyoung.anonvoting.playervote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerVoteService {

    private final PlayerVoteDataAccessService playerVoteDataAccessService;

    @Autowired
    public PlayerVoteService(PlayerVoteDataAccessService playerVoteDataAccessService) {
        this.playerVoteDataAccessService = playerVoteDataAccessService;
    }

    void playerVoted(PlayerVote playerVote, String electionId) {
        if(playerVote.getVote().equals(PlayerVote.Vote.YES)) {
            voteYes(electionId);
        } else {
            voteNo(electionId);
        }
        playerVoteDataAccessService.updatePlayerVoted(playerVote.getUsername(), electionId);
    }

    void voteYes(String electionId) {
        playerVoteDataAccessService.updateYesVotes(electionId);
    }

    void voteNo(String electionId) {
        playerVoteDataAccessService.updateNoVotes(electionId);
    }

    boolean hasPlayerVoted(String username, String electionId) {
        return playerVoteDataAccessService.selectHasVoted(username, electionId);
    }
}
