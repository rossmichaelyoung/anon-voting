package com.rossyoung.anonvoting.election;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @DeleteMapping("api/elections/{electionId}/delete")
    public void deleteElection(@PathVariable Long electionId) {
        if(electionService.doesElectionExist(electionId)) {
            electionService.deleteElection(electionId);
        }
    }
}
