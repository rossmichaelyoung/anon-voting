package com.rossyoung.anonvoting.vote;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "Vote")
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(name = "username_unique", columnNames = "username")
})
public class Vote {
    @Id
    @Column(name = "username", columnDefinition = "TEXT", unique = true)
    private String username;

    @Column(name = "election_id", columnDefinition = "BIGINT", nullable = false)
    private Long electionId;

    @Column(name = "has_voted", columnDefinition = "BOOLEAN", nullable = false)
    private boolean hasVoted;

    public Vote() {}

    public Vote(String username, Long electionId) {
        this.username = username;
        this.electionId = electionId;
        this.hasVoted = false;
    }

    public Vote(String username, Long electionId, boolean hasVoted) {
        this.username = username;
        this.electionId = electionId;
        this.hasVoted = hasVoted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
