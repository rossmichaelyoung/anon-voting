import { useState, useEffect } from "react";
import React from "react";
import { Button } from "antd";
import Container from "./Container";

import {
  getYesVotes,
  getNoVotes,
  vote,
  isVotingActive,
  deleteElection,
} from "../client";

const divStyle = { marginTop: "15%", marginBottom: "5%" };

const Election = (props) => {
  const { username, size, electionId, exitElection } = props;
  const [yes, setYes] = useState(0);
  const [no, setNo] = useState(0);
  const [isElectionOver, setIsElectionOver] = useState(false);
  const [hasVoted, setHasVoted] = useState(false);

  const submitVote = (electionId, playerVote) => {
    vote(electionId, playerVote);
    setHasVoted(true);
  };

  useEffect(() => {
    const updateVotingStatus = () => {
      isVotingActive(electionId)
        .then((res) => res.json())
        .then((response) => {
          setIsElectionOver(!response);
        });
    };
    const intervalId = setInterval(() => updateVotingStatus(), 2000);
    return () => clearInterval(intervalId);
  }, [electionId]);

  useEffect(() => {
    const getNo = () => {
      getNoVotes(electionId)
        .then((res) => res.json())
        .then((data) => {
          if (data !== -1) setNo(data);
        });
    };
    const intervalId = setInterval(() => getNo());
    return () => clearInterval(intervalId);
  }, [electionId]);

  useEffect(() => {
    const getYes = () => {
      getYesVotes(electionId)
        .then((res) => res.json())
        .then((data) => {
          if (data !== -1) setYes(data);
        });
    };
    const intervalId = setInterval(() => getYes());
    return () => clearInterval(intervalId);
  }, [electionId]);

  return (
    <Container size={size}>
      <div style={divStyle}>
        {!isElectionOver && <h1>Vote Wisely, {username}</h1>}

        {isElectionOver && yes > no && <h1>Motion Passed</h1>}

        {isElectionOver && yes <= no && <h1>Motion Did Not Pass</h1>}

        <div>
          <h3>Yes: {yes}</h3>
        </div>
        <div style={{ marginTop: "2%", marginBottom: "2%" }}>
          <h3>No: {no}</h3>
        </div>

        {!isElectionOver && !hasVoted && (
          <div>
            <Button
              onClick={() => {
                const playerVote = {
                  vote: "YES",
                  username: username,
                };
                submitVote(electionId, playerVote);
                setYes(yes + 1);
              }}
              type='primary'>
              Yes
            </Button>

            <Button
              onClick={() => {
                const playerVote = {
                  vote: "NO",
                  username: username,
                };
                submitVote(electionId, playerVote);
                setNo(no + 1);
              }}
              type='primary'
              style={{ backgroundColor: "white", color: "black" }}>
              No
            </Button>
          </div>
        )}

        {isElectionOver && (
          <div style={{ marginTop: "0.5%" }}>
            <Button
              onClick={() => {
                deleteElection(electionId);
                exitElection();
              }}
              type='primary'>
              Exit
            </Button>
          </div>
        )}
      </div>
    </Container>
  );
};

export default Election;
