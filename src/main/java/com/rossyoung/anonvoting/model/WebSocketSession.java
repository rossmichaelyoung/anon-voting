package com.rossyoung.anonvoting.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "WebSocketSession")
@Table(name = "websocket_session", uniqueConstraints = {
        @UniqueConstraint(name = "principal_name_unique", columnNames = "principal_name")
})
public class WebSocketSession {

    @Column(name = "username", columnDefinition = "TEXT")
    private String username;

    @Id
    @Column(name = "principal_name", columnDefinition = "TEXT", unique = true)
    private String principalName;

    @Column(name = "websocket_type")
    @Enumerated(EnumType.STRING)
    private WebSocketType webSocketType;

    @Column(name = "creation_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime creationTime;

    public WebSocketSession() {}

    public WebSocketSession(String username, String principalName, WebSocketType webSocketType, LocalDateTime creationTime) {
        this.username = username;
        this.principalName = principalName;
        this.webSocketType = webSocketType;
        this.creationTime = creationTime;
    }

    public WebSocketSession(String username, String principalName, WebSocketType webSocketType) {
        this.username = username;
        this.principalName = principalName;
        this.webSocketType = webSocketType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public WebSocketType getWebSocketType() {
        return webSocketType;
    }

    public void setWebSocketType(WebSocketType webSocketType) {
        this.webSocketType = webSocketType;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
