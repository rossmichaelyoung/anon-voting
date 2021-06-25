package com.rossyoung.anonvoting.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("api/players")
    public List<Player> getAllPlayers() {
        List<String> usernames = playerService.getAllPlayers();
        List<Player> players = new ArrayList<>();
        for (String username: usernames) {
            players.add(new Player(username, ""));
        }
        return players;
    }

    @PostMapping("api/players")
    @ResponseBody
    public int addPlayer(@RequestBody Player player) {
        if(!playerService.checkUserNameAvailability(player)) {
            playerService.addPlayer(player);
            return 1;
        }
        return -1;
    }

    @GetMapping("api/players/available")
    public List<Player> getAllAvailablePlayers() {
        List<String> usernames = playerService.getAllAvailablePlayers();
        List<Player> players = new ArrayList<>();
        for (String username: usernames) {
            players.add(new Player(username, ""));
        }
        return players;
    }

    @PostMapping("api/players/login")
    @ResponseBody
    public Player loginPlayer(@RequestBody Player player) {
        if(playerService.playerExists(player)) {
            return new Player(player.getUsername(), "");
        }
        return new Player("Not found", "");
    }
}
