package com.rossyoung.anonvoting.election;

import com.rossyoung.anonvoting.player.PlayerRepository;
import com.rossyoung.anonvoting.vote.Vote;
import com.rossyoung.anonvoting.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;
    private final PlayerRepository playerRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public ElectionService(ElectionRepository electionRepository, PlayerRepository playerRepository, VoteRepository voteRepository) {
        this.electionRepository = electionRepository;
        this.playerRepository = playerRepository;
        this.voteRepository = voteRepository;
    }

    List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    public Election addElection(Election election) {
        return electionRepository.save(election);
    }

    public void addPlayerToElection(String username, Long electionId) {
        if(!voteRepository.existsById(username) && playerRepository.existsById(username)) {
            voteRepository.save(new Vote(username, electionId));
        }
    }

    public int totalElectionSpots(Long electionId) {
        return electionRepository.findById(electionId).map(Election::getElectionSize).orElse(-1);
    }

    public boolean doesElectionExist(Long electionId) {

        return electionRepository.existsById(electionId);
    }

    void deleteElection(Long electionId) {
        electionRepository.deleteById(electionId);
        voteRepository.deleteAllByElectionId(electionId);
    }

    public int getYesVotes(Long electionId) {
        return electionRepository.findById(electionId).map(Election::getYesVotes).orElse(-1);
    }

    public int getNoVotes(Long electionId) {
        System.out.println(electionRepository.findById(electionId));
        return electionRepository.findById(electionId).map(Election::getNoVotes).orElse(-1);
    }

    int getTotalNumberVotes(Long electionId) {
        return getNoVotes(electionId) + getYesVotes(electionId);
    }

    public boolean isVotingActive(Long electionId) {
        if(doesElectionExist(electionId)) {
            return !(
                    getTotalNumberVotes(electionId) == totalElectionSpots(electionId)
                            || (((double) getYesVotes(electionId) / (double) totalElectionSpots(electionId)) > 0.5)
                            || (((double) getNoVotes(electionId) / (double) totalElectionSpots(electionId)) > 0.5)
            );
        }
        return false;
    }
}
