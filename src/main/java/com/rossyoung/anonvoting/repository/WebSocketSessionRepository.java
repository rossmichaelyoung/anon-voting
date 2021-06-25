package com.rossyoung.anonvoting.repository;

import com.rossyoung.anonvoting.model.WebSocketSession;
import com.rossyoung.anonvoting.model.WebSocketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebSocketSessionRepository extends JpaRepository<WebSocketSession, String> {
    List<WebSocketSession> findByUsernameAndWebSocketType(String username, WebSocketType webSocketType);

    List<WebSocketSession> findByPrincipalNameAndWebSocketType(String principalName, WebSocketType webSocketType);

    boolean existsByUsernameAndWebSocketType(String Username, WebSocketType webSocketType);
}
