package com.rossyoung.anonvoting.player;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.rossyoung.anonvoting.election.Election;
import com.rossyoung.anonvoting.vote.Vote;
import com.rossyoung.anonvoting.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

//    private final PlayerDataAccessService playerDataAccessService;
    private final PlayerRepository playerRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, VoteRepository voteRepository) {
        this.playerRepository = playerRepository;
        this.voteRepository = voteRepository;
    }

//    @Autowired
//    public PlayerService(PlayerDataAccessService playerDataAccessService) {
//        this.playerDataAccessService = playerDataAccessService;
//    }

    List<String> getAllPlayers() {
//        return playerDataAccessService.selectAllPlayers();
        return playerRepository.findAll().stream().map(Player::getUsername).collect(Collectors.toList());
    }

    List<String> getAllAvailablePlayers() {
//        return playerDataAccessService.selectAllAvailablePlayers();
        List<String> playersInElection = voteRepository.findAll().stream().map(Vote::getUsername).collect(Collectors.toList());
        return playerRepository.findAll().stream().filter(lifter -> !playersInElection.contains(lifter.getUsername())).map(Player::getUsername).collect(Collectors.toList());
    }

    Player addPlayer(Player player) {
//        return playerDataAccessService.insertPlayer(player);
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, player.getPassword().toCharArray());
        player.setPassword(bcryptHashString);
        return playerRepository.save(player);
    }

    boolean playerExists(Player player) {
        return playerRepository.findById(player.getUsername())
                .map(p -> verifyPassword(player.getPassword(), p.getPassword()))
                .orElse(false);
    }

    private boolean verifyPassword(String password, String storedHash) {
        return BCrypt.verifyer().verify(password.toCharArray(), storedHash).verified;
    }

    boolean checkUserNameAvailability(Player player) {
//        return playerDataAccessService.selectUserName(player).isEmpty();
        return playerRepository.existsById(player.getUsername());
    }

    Long getPlayerElection(String username) {
//        return playerDataAccessService.selectPlayerElection(username);
        return voteRepository.findVoteByUsername(username).map(Vote::getElectionId).orElse((long) -1);
    }
}
