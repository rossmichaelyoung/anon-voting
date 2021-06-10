package com.rossyoung.anonvoting.playervote;

import com.rossyoung.anonvoting.election.ElectionRepository;
import com.rossyoung.anonvoting.vote.Vote;
import com.rossyoung.anonvoting.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerVoteService {

//    private final PlayerVoteDataAccessService playerVoteDataAccessService;
    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;

    @Autowired
    public PlayerVoteService(VoteRepository voteRepository, ElectionRepository electionRepository) {
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
    }

    void playerVoted(PlayerVote playerVote, Long electionId) {
        if(playerVote.getVote().equals(PlayerVote.Vote.YES)) {
            voteYes(electionId);
        } else {
            voteNo(electionId);
        }
//        playerVoteDataAccessService.updatePlayerVoted(playerVote.getUsername(), electionId);
        voteRepository.updateVoteStatus(playerVote.getUsername());
    }

    void voteYes(Long electionId) {
//        playerVoteDataAccessService.updateYesVotes(electionId);
        electionRepository.updateYesVotes(electionId);
    }

    void voteNo(Long electionId) {
//        playerVoteDataAccessService.updateNoVotes(electionId);
        electionRepository.updateNoVotes(electionId);
    }

    boolean hasPlayerVoted(String username) {
//        return playerVoteDataAccessService.selectHasVoted(username, electionId);
        return voteRepository.findVoteByUsername(username).map(Vote::isHasVoted).orElse(true);
    }
}
