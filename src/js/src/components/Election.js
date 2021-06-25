import { useState, useEffect } from "react";
import React from "react";
import { Button } from "antd";
import Container from "./Container";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

const divStyle = { marginTop: "15%", marginBottom: "5%" };

const Election = ({
  username,
  size,
  electionId,
  exitElection,
  useStickyState,
}) => {
  const [yes, setYes] = useStickyState(0, "yes");
  const [no, setNo] = useStickyState(0, "no");
  const [isVotingActive, setIsVotingActive] = useStickyState(
    true,
    "isVotingActive"
  );
  const [hasVoted, setHasVoted] = useStickyState(false, "hasVoted");

  const [stompClient, setStompClient] = useState(null);
  useEffect(() => {
    //const socket = new SockJS("http://localhost:8080/ws");
    const socket = new SockJS(
      window.location.protocol + "//" + window.location.hostname + ":8080/ws"
    );
    const sc = Stomp.over(socket);
    sc.connect({ username: username, webSocketType: "Vote" }, (frame) => {
      console.log("Connected: " + frame);
      sc.subscribe("/topic/election/" + electionId, (frame) => {
        console.log(frame);
        console.log(JSON.parse(frame.body));
        const { isVotingActive, yesVotes, noVotes } = JSON.parse(frame.body);
        setIsVotingActive(isVotingActive);
        setYes(yesVotes);
        setNo(noVotes);
      });
    });
    setStompClient(sc);

    return () => {
      if (sc !== null) sc.disconnect();
    };
  }, [username, electionId, setIsVotingActive, setNo, setYes]);

  useEffect(() => {}, []);

  const submitVote = ({ username, vote, electionId }) => {
    stompClient.send(
      "/app/election/vote",
      {},
      JSON.stringify({
        username: username,
        vote: vote,
        electionId: electionId,
      })
    );
    setHasVoted(true);
  };

  return (
    <Container size={size}>
      <div style={divStyle}>
        {isVotingActive && <h1>Vote Wisely, {username}</h1>}

        {!isVotingActive && yes > no && <h1>Motion Passed</h1>}

        {!isVotingActive && yes <= no && <h1>Motion Did Not Pass</h1>}

        <div>
          <h3>Yes: {yes}</h3>
        </div>
        <div style={{ marginTop: "2%", marginBottom: "2%" }}>
          <h3>No: {no}</h3>
        </div>

        {isVotingActive && !hasVoted && (
          <div>
            <Button
              onClick={() => {
                const ballot = {
                  username: username,
                  vote: "YES",
                  electionId: electionId,
                };
                submitVote(ballot);
                setYes(yes + 1);
              }}
              type='primary'>
              Yes
            </Button>

            <Button
              onClick={() => {
                const ballot = {
                  username: username,
                  vote: "NO",
                  electionId: electionId,
                };
                submitVote(ballot);
                setNo(no + 1);
              }}
              type='primary'
              style={{ backgroundColor: "white", color: "black" }}>
              No
            </Button>
          </div>
        )}

        {!isVotingActive && (
          <div style={{ marginTop: "0.5%" }}>
            <Button
              onClick={() => {
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
