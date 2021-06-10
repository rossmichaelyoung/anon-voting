package com.rossyoung.anonvoting.electionvotes;

import com.rossyoung.anonvoting.election.Election;
import com.rossyoung.anonvoting.election.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElectionVotesService {

//    private final ElectionVotesDataAccessService electionVotesDataAccessService;
    private final ElectionRepository electionRepository;

    @Autowired
    public ElectionVotesService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    int getYesVotes(Long electionId) {
//        return electionVotesDataAccessService.selectYesVotes(electionId);
        return electionRepository.findById(electionId).map(Election::getYesVotes).orElse(-1);
    }

    int getNoVotes(Long electionId) {
        return electionRepository.findById(electionId).map(Election::getNoVotes).orElse(-1);
    }

    int getTotalNumberVotes(Long electionId) {
        return getNoVotes(electionId) + getYesVotes(electionId);
    }
}
