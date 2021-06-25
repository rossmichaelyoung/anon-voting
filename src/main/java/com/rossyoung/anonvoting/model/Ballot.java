package com.rossyoung.anonvoting.model;

import lombok.Data;

@Data
public class Ballot {

    public enum Vote {
        YES, NO
    }

    private String username;
    private Vote vote;
    private Long electionId;

    public Ballot() {}

    public Ballot(String username, Vote vote, Long electionId) {
        this.username = username;
        this.vote = vote;
        this.electionId = electionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    @Override
    public String toString() {
        return "Ballot{" +
                "username='" + username + '\'' +
                ", vote=" + vote +
                ", electionId=" + electionId +
                '}';
    }
}

