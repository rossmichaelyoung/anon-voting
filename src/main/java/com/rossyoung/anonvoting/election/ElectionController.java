package com.rossyoung.anonvoting.election;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Random;

@RestController
public class ElectionController {

    private final ElectionService electionService;

    @Autowired
    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @GetMapping("api/elections")
    public List<Election> getAllElections() {
        return electionService.getAllElections();
    }

    @PostMapping("api/elections")
    @ResponseBody
    public String addElection(@RequestBody Election election) {
        String electionId = getRandomString() + election.getOwner() + election.getElectionSize();
        election.setElectionId(electionId);
        while(!electionService.checkElectionIdAvailability(election)) {
            electionId = getRandomString() + election.getOwner() + election.getElectionSize();
            election.setElectionId(electionId);
        }
        if (electionService.addElection(election) == 1) {
            return election.getElectionId();
        }
        return "N/A";
    }

    @PostMapping("api/elections/{electionId}/players")
    public void addPlayerToElection(@RequestBody String username, @PathVariable String electionId) {
        electionService.addPlayerToElection(username, electionId);
    }

    @GetMapping("api/elections/{electionId}/players")
    public List<String> getPlayersInElection(@PathVariable String electionId) {
        return electionService.getPlayersInElection(electionId);
    }

    @GetMapping("api/elections/{electionId}")
    public boolean doesElectionExist(@PathVariable String electionId) {
        return electionService.doesElectionExist(electionId);
    }

    @PostMapping("api/elections/{electionId}/availablespot")
    @ResponseBody
    public boolean electionSpotsAvailable(@RequestBody String username, @PathVariable String electionId) {
        if(!electionService.isPlayerInElection(username, electionId))
            return electionService.electionSpotsAvailable(electionId);
        return true;
    }

    @DeleteMapping("api/elections/{electionId}/delete")
    public void deleteElection(@PathVariable String electionId) {
        if(electionService.doesElectionExist(electionId)) {
            electionService.deleteElection(electionId);
        }
    }

    protected String getRandomString() {
        String characterSpace = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder s = new StringBuilder();
        Random rnd = new Random();
        while (s.length() < 30) {
            int index = (int) (rnd.nextFloat() * characterSpace.length());
            s.append(characterSpace.charAt(index));
        }
        return s.toString();
    }
}
