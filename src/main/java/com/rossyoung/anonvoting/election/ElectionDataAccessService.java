package com.rossyoung.anonvoting.election;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ElectionDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ElectionDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Election> selectAllElections() {
        String sql = "" +
                "SELECT election_id, owner, election_size " +
                "FROM elections";

        List<Election> elections = jdbcTemplate.query(sql, ((resultSet, i) -> {
            String electionId = resultSet.getString("election_id");
            String owner = resultSet.getString("owner");
            int electionSize = resultSet.getInt("election_size");
            return new Election(owner, electionSize, electionId);
        }));
        return elections;
    }

    int insertElection(Election election) {
        String sql = "" +
                "INSERT INTO elections (election_id, owner, election_size, yes_votes, no_votes) " +
                "VALUES (?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql, election.getElectionId(), election.getOwner(), election.getElectionSize(), 0, 0);
    }

    int insertPlayerInElection(String username, String electionId) {
        String sql = "" +
                "INSERT INTO player_election_map (username, election_id, has_voted) " +
                "VALUES (?, ?, ?)";

        return jdbcTemplate.update(sql, username, electionId, false);
    }

    List<String> selectPlayersInElection(String electionId) {
        String sql = "" +
                "SELECT username " +
                "FROM player_election_map " +
                "WHERE election_id='" + electionId + "'";

        List<String> playersInElection = jdbcTemplate.query(sql,
                (resultSet, i) -> resultSet.getString("username"));

        return playersInElection;
    }

    List<String> selectElectionIdFromElection(String electionId) {
        String sql = "" +
                "SELECT election_id " +
                "FROM elections " +
                "WHERE election_id='" + electionId + "'";

        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("election_id"));
    }

    List<Integer> selectElectionSize(String electionId) {
        String sql = "" +
                "SELECT election_size " +
                "FROM elections " +
                "WHERE election_id='" + electionId + "'";

        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("election_size"));
    }

    List<Integer> selectNumberOfPlayersInElection(String electionId) {
        String sql = "" +
                "SELECT COUNT(username) as num_players " +
                "FROM player_election_map " +
                "WHERE election_id='" + electionId + "'";

        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getInt("num_players"));
    }

    void deleteFromElections(String electionId) {
        String sql = "" +
                "DELETE FROM elections " +
                "WHERE election_id='" + electionId + "'";

        jdbcTemplate.update(sql);
    }

    void deleteFromPlayerElectionMap(String electionId) {
        String sql = "" +
                "DELETE FROM player_election_map " +
                "WHERE election_id='" + electionId + "'";

        jdbcTemplate.update(sql);
    }

    String selectPlayerInElection(String username, String electionId) {
        String sql = "" +
                "SELECT username " +
                "FROM player_election_map " +
                "WHERE election_id='" + electionId + "'" +
                "AND username='" + username + "'";

        List<String> playerInElection = jdbcTemplate.query(sql,
                (resultSet, i) -> resultSet.getString("username"));

        if(playerInElection.isEmpty())
            return "";

        return playerInElection.get(0);
    }
}
