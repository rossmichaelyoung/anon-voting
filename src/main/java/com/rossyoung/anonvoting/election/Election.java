package com.rossyoung.anonvoting.election;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "Election")
@Table(name = "election", uniqueConstraints = {
        @UniqueConstraint(name = "election_id_unique", columnNames = "election_id")
})
public class Election {

    @Id
    @SequenceGenerator(
            name = "election_sequence",
            sequenceName = "election_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "election_sequence"
    )
    @Column(name = "election_id", columnDefinition = "BIGINT", updatable = false, nullable = false)
    private Long electionId;

    @Column(name = "owner", columnDefinition = "TEXT", nullable = false)
    private String owner;

    @Column(name = "election_size", columnDefinition = "INTEGER", nullable = false)
    private int electionSize;

    @Column(name = "yes_votes", columnDefinition = "INTEGER", nullable = false)
    private int yesVotes;

    @Column(name = "no_votes", columnDefinition = "INTEGER", nullable = false)
    private int noVotes;

    public Election() {}

    public Election(String owner, int electionSize) {
        this.owner = owner;
        this.electionSize = electionSize;
        this.yesVotes = 0;
        this.noVotes = 0;
    }

    public Election(String owner, int electionSize, int yesVotes, int noVotes) {
        this.owner = owner;
        this.electionSize = electionSize;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
    }
}
