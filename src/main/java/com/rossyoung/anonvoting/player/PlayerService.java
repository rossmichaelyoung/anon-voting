package com.rossyoung.anonvoting.player;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {

    private final PlayerDataAccessService playerDataAccessService;

    @Autowired
    public PlayerService(PlayerDataAccessService playerDataAccessService) {
        this.playerDataAccessService = playerDataAccessService;
    }

    List<String> getAllPlayers() {
        return playerDataAccessService.selectAllPlayers();
    }

    List<String> getAllAvailablePlayers() {
        return playerDataAccessService.selectAllAvailablePlayers();
    }

    int addPlayer(Player player) {
        return playerDataAccessService.insertPlayer(player);
    }

    boolean playerExists(Player player) {
        String password = playerDataAccessService.selectPlayer(player);
        if(password.length() > 0) {
            BCrypt.Result result = BCrypt.verifyer().verify(player.getPassword().toCharArray(), password);
            return result.verified;
        }
        return false;
    }

    boolean checkUserNameAvailability(Player player) {
        return playerDataAccessService.selectUserName(player).isEmpty();
    }

    String getPlayerElection(String username) {
        return playerDataAccessService.selectPlayerElection(username);
    }
}
