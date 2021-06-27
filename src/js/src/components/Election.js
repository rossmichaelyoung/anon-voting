import { useState, useEffect, useContext } from "react";
import React from "react";
import { Button } from "antd";
import Container from "./Container";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { Context } from "../common/Store";
import {
  setIsVotingActive,
  setYes,
  setNo,
  setHasVoted,
  electionOver,
} from "../common/actions";
import { deleteElection } from "../common/client";

const divStyle = { marginTop: "15%", marginBottom: "5%" };

const Election = () => {
  const { state, dispatch } = useContext(Context);
  const { username, yes, no, isVotingActive, hasVoted, electionId } = state;

  const [loading, setLoading] = useState(true);

  const [stompClient, setStompClient] = useState(null);
  useEffect(() => {
    const socket = new SockJS(
      window.location.protocol + "//" + window.location.hostname + ":8080/ws"
    );
    const sc = Stomp.over(socket);
    dispatch(setHasVoted(false));
    sc.connect({ username: username, webSocketType: "Vote" }, (frame) => {
      console.log("Connected: " + frame);
      sc.subscribe("/topic/election/" + electionId, (frame) => {
        setLoading(true);
        console.log(frame);
        console.log(JSON.parse(frame.body));
        const { votingActive, yesVotes, noVotes } = JSON.parse(frame.body);
        dispatch(setIsVotingActive(votingActive));
        dispatch(setYes(yesVotes));
        dispatch(setNo(noVotes));
        setLoading(false);
      });
    });
    setStompClient(sc);

    return () => {
      if (sc !== null) {
        sc.disconnect();
        console.log("Vote WebSocket Disconnected");
      }
    };
  }, [dispatch, electionId, username]);

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
    dispatch(setHasVoted(true));
  };

  const exitElection = () => {
    deleteElection(electionId);
    dispatch(electionOver(state));
  };

  if (loading) {
    return (
      <Container>
        <h1>Loading Election</h1>
      </Container>
    );
  } else {
    return (
      <Container>
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
                  dispatch(setYes(yes + 1));
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
                  dispatch(setNo(no + 1));
                }}
                type='primary'
                style={{ backgroundColor: "white", color: "black" }}>
                No
              </Button>
            </div>
          )}

          {!isVotingActive && (
            <div style={{ marginTop: "0.5%" }}>
              <Button onClick={exitElection} type='primary'>
                Exit
              </Button>
            </div>
          )}
        </div>
      </Container>
    );
  }
};

export default Election;
