package com.rossyoung.anonvoting.electionvotes;

import com.rossyoung.anonvoting.election.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ElectionVotesController {

    private final ElectionVotesService electionVotesService;
    private final ElectionService electionService;

    @Autowired
    public ElectionVotesController(ElectionVotesService electionVotesService, ElectionService electionService) {
        this.electionVotesService = electionVotesService;
        this.electionService = electionService;
    }

    @GetMapping("api/elections/{electionId}/vote/yes")
    public int getYesVotes(@PathVariable Long electionId) {
        if(electionService.doesElectionExist(electionId)) {
            return electionVotesService.getYesVotes(electionId);
        }
        return -1;
    }

    @GetMapping("api/elections/{electionId}/vote/no")
    public int getNoVotes(@PathVariable Long electionId) {
        if(electionService.doesElectionExist(electionId)) {
            return electionVotesService.getNoVotes(electionId);
        }
        return -1;
    }

    @GetMapping("api/elections/{electionId}/vote/active")
    public boolean isVotingActive(@PathVariable Long electionId) {
        if(electionService.doesElectionExist(electionId)) {
            return !(
                    electionVotesService.getTotalNumberVotes(electionId) == electionService.totalElectionSpots(electionId)
                    || ((double) electionVotesService.getYesVotes(electionId) / (double) electionService.totalElectionSpots(electionId)) > 0.5
                    || ((double) electionVotesService.getNoVotes(electionId) / (double) electionService.totalElectionSpots(electionId)) > 0.5
            );
        }
        return false;
    }
}
