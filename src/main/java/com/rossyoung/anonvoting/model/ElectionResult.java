package com.rossyoung.anonvoting.model;

import lombok.Data;

@Data
public class ElectionResult {
    private int yesVotes;
    private int noVotes;
    private boolean isVotingActive;

    public ElectionResult() {}

    public ElectionResult(int yesVotes, int noVotes, boolean isVotingActive) {
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
        this.isVotingActive = isVotingActive;
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public void setYesVotes(int yesVotes) {
        this.yesVotes = yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(int noVotes) {
        this.noVotes = noVotes;
    }

    public boolean isVotingActive() {
        return isVotingActive;
    }

    public void setVotingActive(boolean votingActive) {
        isVotingActive = votingActive;
    }

    @Override
    public String toString() {
        return "ElectionResult{" +
                "yesVotes=" + yesVotes +
                ", noVotes=" + noVotes +
                ", isVotingActive=" + isVotingActive +
                '}';
    }
}

