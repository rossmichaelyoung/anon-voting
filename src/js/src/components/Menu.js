import { useState, useEffect } from "react";
import React from "react";
import { Button } from "antd";
import Container from "./Container";
import CreateElectionCustom from "../forms/CreateElectionCustom";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Election from "./Election";
import { deleteElection } from "../client";

const divStyle = { marginTop: "15%", marginBottom: "5%" };

const Menu = ({ username, size, logout, useStickyState }) => {
  const [createElection, setCreateElection] = useState(false);
  const [electionId, setElectionId] = useStickyState(null, "electionId");
  const [isElection, setIsElection] = useStickyState(false, "isElection");

  const exitElection = () => {
    setElectionId(null);
    setIsPlayerInAnElection(false);
    setIsElection(false);
    deleteElection(electionId);
    window.localStorage.removeItem("isVotingActive");
    window.localStorage.removeItem("hasVoted");
    window.localStorage.removeItem("yes");
    window.localStorage.removeItem("no");
  };

  const [isPlayerInAnElection, setIsPlayerInAnElection] = useState(false);
  const [stompClient, setStompClient] = useState(null);
  useEffect(() => {
    //const socket = new SockJS("http://localhost:8080/ws");
    const socket = new SockJS(
      window.location.protocol + "//" + window.location.hostname + ":8080/ws"
    );
    const sc = Stomp.over(socket);
    sc.connect(
      { username: username, webSocketType: "FindElection" },
      (frame) => {
        console.log("Connected: " + frame);
        sc.subscribe("/user/queue/election", (frame) => {
          console.log("Received Message From Server");
          console.log(frame);
          console.log(JSON.parse(frame.body));
          setElectionId(JSON.parse(frame.body));
          setIsPlayerInAnElection(true);
        });
      }
    );
    setStompClient(sc);

    return () => {
      if (sc !== null) sc.disconnect();
    };
  }, [username, setElectionId]);

  const addPlayerToElection = (username, electionId) => {
    stompClient.send("/app/election/" + electionId, {}, username);
  };

  if (!isElection) {
    return (
      <Container size={size}>
        <h1>Welcome, {username}</h1>

        {!createElection && (
          <Button
            onClick={() => {
              setCreateElection(true);
            }}
            type='primary'
            block>
            Create Election
          </Button>
        )}

        {createElection && (
          <>
            <div style={divStyle}>
              <h2>Create an Election</h2>
              <CreateElectionCustom
                owner={username}
                electionCreated={() => setCreateElection(false)}
                addPlayerToElection={addPlayerToElection}
              />
            </div>
            <div style={divStyle}>
              <Button
                onClick={() => {
                  setCreateElection(false);
                }}
                type='primary'>
                Back
              </Button>
            </div>
          </>
        )}

        {!createElection && isPlayerInAnElection && (
          //   take user to ElectionFunctional Component
          <div style={divStyle}>
            <Button
              onClick={() => {
                setIsElection(true);
              }}
              type='primary'
              block>
              Join Election
            </Button>
          </div>
        )}

        <div style={{ marginTop: "15%" }}>
          <Button onClick={logout} type='primary' block>
            Logout
          </Button>
        </div>
      </Container>
    );
  } else {
    return (
      <Election
        electionId={electionId}
        size={size}
        username={username}
        exitElection={exitElection}
        useStickyState={useStickyState}
      />
    );
  }
};

export default Menu;
