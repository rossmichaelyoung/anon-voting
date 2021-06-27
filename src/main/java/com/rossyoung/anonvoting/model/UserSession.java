package com.rossyoung.anonvoting.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "UserSession")
@Table(name = "user_session", uniqueConstraints = {
        @UniqueConstraint(name = "username_unique", columnNames = "username")
})
public class UserSession {
    @Id
    @Column(name = "username", columnDefinition = "TEXT", nullable = false, unique = true)
    private String username;

    @Column(name = "cookie", columnDefinition = "TEXT", nullable = false, unique = true)
    private String cookie;

    public UserSession() {}

    public UserSession(String username, String cookie) {
        this.username = username;
        this.cookie = cookie;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "username='" + username + '\'' +
                ", cookie='" + cookie + '\'' +
                '}';
    }
}
