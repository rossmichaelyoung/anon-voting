package com.rossyoung.anonvoting.electionvotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElectionVotesService {

    private final ElectionVotesDataAccessService electionVotesDataAccessService;

    @Autowired
    public ElectionVotesService(ElectionVotesDataAccessService electionVotesDataAccessService) {
        this.electionVotesDataAccessService = electionVotesDataAccessService;
    }

    int getYesVotes(String electionId) {
        return electionVotesDataAccessService.selectYesVotes(electionId);
    }

    int getNoVotes(String electionId) {
        return electionVotesDataAccessService.selectNoVotes(electionId);
    }

    int getTotalNumberVotes(String electionId) {
        return getNoVotes(electionId) + getYesVotes(electionId);
    }
}
