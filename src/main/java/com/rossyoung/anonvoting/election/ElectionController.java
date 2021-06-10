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
    public Election addElection(@RequestBody Election election) {
        return electionService.addElection(election);
    }

    @PostMapping("api/elections/{electionId}/players")
    public void addPlayerToElection(@RequestBody String username, @PathVariable Long electionId) {
        electionService.addPlayerToElection(username, electionId);
    }

    @GetMapping("api/elections/{electionId}/players")
    public List<String> getPlayersInElection(@PathVariable Long electionId) {
        return electionService.getPlayersInElection(electionId);
    }

    @GetMapping("api/elections/{electionId}")
    public boolean doesElectionExist(@PathVariable Long electionId) {
        return electionService.doesElectionExist(electionId);
    }

    @PostMapping("api/elections/{electionId}/availablespot")
    @ResponseBody
    public boolean electionSpotsAvailable(@RequestBody String username, @PathVariable Long electionId) {
        if(!electionService.isPlayerInElection(username, electionId))
            return electionService.electionSpotsAvailable(electionId);
        return true;
    }

    @DeleteMapping("api/elections/{electionId}/delete")
    public void deleteElection(@PathVariable Long electionId) {
        if(electionService.doesElectionExist(electionId)) {
            electionService.deleteElection(electionId);
        }
    }
}
