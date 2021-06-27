package com.rossyoung.anonvoting.player;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.rossyoung.anonvoting.model.UserSession;
import com.rossyoung.anonvoting.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;
    private final UserSessionRepository userSessionRepository;

    @Autowired
    public PlayerController(PlayerService playerService, UserSessionRepository userSessionRepository) {
        this.playerService = playerService;
        this.userSessionRepository = userSessionRepository;
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
    public ResponseEntity<String> loginPlayer(@RequestBody Player player) {
        if(playerService.playerExists(player)) {
            String cookie = BCrypt.withDefaults().hashToString(12, (player.getUsername() + LocalDateTime.now()).toCharArray());
            ResponseCookie springCookie = ResponseCookie.from("user-id", cookie)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60)
                    .build();

            if(userSessionRepository.existsById(player.getUsername())) {
                userSessionRepository.deleteById(player.getUsername());
            }
            userSessionRepository.save(new UserSession(player.getUsername(), cookie));
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                    .build();
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("api/players/logout")
    public void logoutPlayer(@RequestBody Player player, @CookieValue(name = "user-id", defaultValue = "default-user-id") String userId) {
        userSessionRepository.delete(new UserSession(player.getUsername(), userId));
    }
}
