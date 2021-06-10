package com.rossyoung.anonvoting.election;

import com.rossyoung.anonvoting.player.Player;
import com.rossyoung.anonvoting.player.PlayerRepository;
import com.rossyoung.anonvoting.vote.Vote;
import com.rossyoung.anonvoting.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElectionService {

//    private final ElectionDataAccessService electionDataAccessService;
//    private final PlayerDataAccessService playerDataAccessService;
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
//        return electionDataAccessService.selectAllElections();
        return electionRepository.findAll();
    }

    Election addElection(Election election) {
//        return electionDataAccessService.insertElection(election);
        return electionRepository.save(election);
    }

    void addPlayerToElection(String username, Long electionId) {
        if(!voteRepository.existsById(username) && playerRepository.existsById(username)) {
            voteRepository.save(new Vote(username, electionId));
        }
    }

    List<String> getPlayersInElection(Long electionId) {
//        return electionDataAccessService.selectPlayersInElection(electionId);
        return voteRepository.findVotesByElectionId(electionId).stream().map(Vote::getUsername).collect(Collectors.toList());
    }

    boolean checkElectionIdAvailability(Election election) {
//        String electionId = election.getElectionId();
//        return !electionDataAccessService.selectElectionIdFromElection(electionId).contains(electionId);
        return electionRepository.existsById(election.getElectionId());
    }

    public int totalElectionSpots(Long electionId) {
//        return electionDataAccessService.selectElectionSize(electionId).get(0);
        return electionRepository.findById(electionId).map(Election::getElectionSize).orElse(-1);
    }

    boolean electionSpotsAvailable(Long electionId) {
//        int maxSize = totalElectionSpots(electionId);
//        int currentNumberOfPlayers = electionDataAccessService.selectNumberOfPlayersInElection(electionId).get(0);
//        return currentNumberOfPlayers < maxSize;
        int maxSize = totalElectionSpots(electionId);
        int currentNumberOfPlayers = voteRepository.findVotesByElectionId(electionId).size();
        return currentNumberOfPlayers < maxSize;
    }

    public boolean doesElectionExist(Long electionId) {
//        return electionDataAccessService.selectElectionIdFromElection(electionId).contains(electionId);
        return electionRepository.existsById(electionId);
    }

    void deleteElection(Long electionId) {
//        electionDataAccessService.deleteFromElections(electionId);
//        electionDataAccessService.deleteFromPlayerElectionMap(electionId);
        electionRepository.deleteById(electionId);
        voteRepository.deleteAllByElectionId(electionId);
    }

    boolean isPlayerInElection(String username, Long electionId) {
//        return electionDataAccessService.selectPlayerInElection(username, electionId).equals(username);
        return voteRepository.findVoteByUsername(username).map(v -> v.getElectionId().equals(electionId)).orElse(false);
    }
}
