package com.rossyoung.anonvoting.election;

import lombok.Data;

@Data
public class Election {

    private final String owner;
    private final int electionSize;
    private String electionId;

    public Election(String owner, int electionSize, String electionId) {
        this.owner = owner;
        this.electionSize = electionSize;
        this.electionId = electionId;
    }

}
