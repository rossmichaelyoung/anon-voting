package com.rossyoung.anonvoting.player;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<String> selectAllPlayers() {
        String sql = "" +
                "SELECT username " +
                "FROM players";

        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("username"));
    }

    List<String> selectAllAvailablePlayers() {
        String sql = "" +
                "SELECT username " +
                "FROM players " +
                "WHERE username NOT IN (" +
                "SELECT username " +
                "FROM player_election_map)";

        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("username"));
    }

    int insertPlayer(Player player) {
        String sql = "" +
                "INSERT INTO players (username, password) " +
                "VALUES (?, ?)";

        String bcryptHashString = BCrypt.withDefaults().hashToString(12, player.getPassword().toCharArray());
        return jdbcTemplate.update(sql, player.getUsername(), bcryptHashString);
    }

    String selectPlayer(Player player) {
        String sql = "" +
                "SELECT password " +
                "FROM players " +
                "WHERE username= ?";
        RowMapper<String> mapper = (rs, rowNum) -> rs.getString("password");
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{player.getUsername()}, mapper);
        } catch (Exception e) {
            return "";
        }
    }

    public List<String> selectUserName(Player player) {
        String sql = "" +
                "SELECT username " +
                "FROM players " +
                "WHERE username='" + player.getUsername() + "'";

        return jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("username"));
    }

    String selectPlayerElection(String username) {
        String sql = "" +
                "SELECT election_id " +
                "FROM player_election_map " +
                "WHERE username='" + username + "'";
        List<String> elections = jdbcTemplate.query(sql, (resultSet, i) -> resultSet.getString("election_id"));
        return elections.isEmpty() ? "" : elections.get(0);
    }
}