package com.rossyoung.anonvoting.playervote;

import lombok.Data;

@Data
public class PlayerVote {

    enum Vote {
        YES, NO
    }

    private final String username;

    private final Vote vote;
}
