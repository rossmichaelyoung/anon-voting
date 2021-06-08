package com.rossyoung.anonvoting.playervote;

import lombok.Data;

@Data
public class PlayerVote {

    enum Vote {
        YES, NO
    }

    private final Vote vote;
    private final String username;
}
