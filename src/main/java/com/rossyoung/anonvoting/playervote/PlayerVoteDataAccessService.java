package com.rossyoung.anonvoting.playervote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerVoteDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerVoteDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    void updatePlayerVoted(String username, String electionId) {
        String sql = "" +
                "UPDATE player_election_map " +
                "SET has_voted = TRUE " +
                "WHERE election_id='" + electionId + "'" +
                "AND username='" + username + "'";

        jdbcTemplate.update(sql);
    }

    void updateYesVotes(String electionId) {
        String sql = "" +
                "UPDATE elections " +
                "SET yes_votes = yes_votes+1 " +
                "WHERE election_id='" + electionId + "'";

        jdbcTemplate.update(sql);
    }

    void updateNoVotes(String electionId) {
        String sql = "" +
                "UPDATE elections " +
                "SET no_votes = no_votes+1 " +
                "WHERE election_id='" + electionId + "'";

        jdbcTemplate.update(sql);
    }

    boolean selectHasVoted(String username, String electionId) {
        String sql = "" +
                "SELECT has_voted " +
                "FROM player_election_map " +
                "WHERE election_id='" + electionId + "'" +
                "AND username='" + username + "'";

        return jdbcTemplate.query(sql,
                (resultSet, i) -> resultSet.getBoolean("has_voted"))
                .get(0);
    }
}
