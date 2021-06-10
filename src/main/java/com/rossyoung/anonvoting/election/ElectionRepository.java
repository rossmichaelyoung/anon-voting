package com.rossyoung.anonvoting.election;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ElectionRepository extends JpaRepository<Election, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Election SET yesVotes = yesVotes+1 WHERE electionId = :electionId")
    void updateYesVotes(@Param("electionId") Long electionId);

    @Transactional
    @Modifying
    @Query("UPDATE Election SET noVotes = noVotes+1 WHERE electionId = :electionId")
    void updateNoVotes(@Param("electionId") Long electionId);
}
