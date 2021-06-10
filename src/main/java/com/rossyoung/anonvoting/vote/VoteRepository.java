package com.rossyoung.anonvoting.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, String> {
    Optional<Vote> findVoteByUsername(String username);

    List<Vote> findVotesByElectionId(Long electionId);

    @Transactional
    @Modifying
    void deleteAllByElectionId(Long electionId);

    @Transactional
    @Modifying
    @Query("UPDATE Vote SET hasVoted= TRUE WHERE username = :username")
    void updateVoteStatus(@Param("username") String username);
}
