package com.rossyoung.anonvoting.election;

import com.rossyoung.anonvoting.player.Player;
import com.rossyoung.anonvoting.player.PlayerDataAccessService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectionService {

    private final ElectionDataAccessService electionDataAccessService;
    private final PlayerDataAccessService playerDataAccessService;

    @Autowired
    public ElectionService(ElectionDataAccessService electionDataAccessService, PlayerDataAccessService playerDataAccessService) {
        this.electionDataAccessService = electionDataAccessService;
        this.playerDataAccessService = playerDataAccessService;
    }

    List<Election> getAllElections() {
        return electionDataAccessService.selectAllElections();
    }

    int addElection(Election election) {
        return electionDataAccessService.insertElection(election);
    }

    void addPlayerToElection(String username, String electionId) {
        if(!electionDataAccessService.selectPlayerInElection(username, electionId).equals(username) && !playerDataAccessService.selectUserName(new Player(username, "")).isEmpty()) {
            electionDataAccessService.insertPlayerInElection(username, electionId);
        }
    }

    List<String> getPlayersInElection(String electionId) {
        return electionDataAccessService.selectPlayersInElection(electionId);
    }

    boolean checkElectionIdAvailability(Election election) {
        String electionId = election.getElectionId();
        return !electionDataAccessService.selectElectionIdFromElection(electionId).contains(electionId);
    }

    public int totalElectionSpots(String electionId) {
        return electionDataAccessService.selectElectionSize(electionId).get(0);
    }

    boolean electionSpotsAvailable(String electionId) {
        int maxSize = totalElectionSpots(electionId);
        int currentNumberOfPlayers = electionDataAccessService.selectNumberOfPlayersInElection(electionId).get(0);
        return currentNumberOfPlayers < maxSize;
    }

    public boolean doesElectionExist(String electionId) {
        return electionDataAccessService.selectElectionIdFromElection(electionId).contains(electionId);
    }

    void deleteElection(String electionId) {
        electionDataAccessService.deleteFromElections(electionId);
        electionDataAccessService.deleteFromPlayerElectionMap(electionId);
    }

    boolean isPlayerInElection(String username, String electionId) {
        return electionDataAccessService.selectPlayerInElection(username, electionId).equals(username);
    }
}
