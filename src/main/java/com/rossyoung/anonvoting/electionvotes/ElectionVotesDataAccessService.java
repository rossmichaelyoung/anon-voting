package com.rossyoung.anonvoting.electionvotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ElectionVotesDataAccessService {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public ElectionVotesDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    int selectYesVotes(String electionId) {
        String sql = "" +
                "SELECT yes_votes " +
                "FROM elections " +
                "WHERE election_id='" + electionId + "'";

        List<Integer> yesVotes = jdbcTemplate.query(sql,
                (resultSet, i) -> resultSet.getInt("yes_votes"));

        if(!yesVotes.isEmpty()) {
            return yesVotes.get(0);
        }
        return -1;
    }

    int selectNoVotes(String electionId) {
        String sql = "" +
                "SELECT no_votes " +
                "FROM elections " +
                "WHERE election_id='" + electionId + "'";

        List<Integer> noVotes = jdbcTemplate.query(sql,
                (resultSet, i) -> resultSet.getInt("no_votes"));

        if(!noVotes.isEmpty()) {
            return noVotes.get(0);
        }
        return -1;
    }
}
