package com.rossyoung.anonvoting.player;

import lombok.Data;

import javax.persistence.*;
import java.security.Principal;

@Data
@Entity(name = "Player")
@Table(name = "player", uniqueConstraints = {
        @UniqueConstraint(name = "username_unique", columnNames = "username")
})
public class Player {
    @Id
    @Column(name = "username", columnDefinition = "TEXT", nullable = false)
    private String username;

    @Column(name = "password", columnDefinition = "TEXT", nullable = false)
    private String password;

    public Player() {}

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
