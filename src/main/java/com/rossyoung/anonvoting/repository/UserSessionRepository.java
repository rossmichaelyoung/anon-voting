package com.rossyoung.anonvoting.repository;

import com.rossyoung.anonvoting.model.UserSession;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {

}
