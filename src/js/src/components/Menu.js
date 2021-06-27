import { useState, useEffect } from "react";
import React from "react";
import { Button } from "antd";
import Container from "./Container";
import CreateElectionCustom from "../forms/CreateElectionCustom";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Election from "./Election";
import { useContext } from "react";
import { Context } from "../common/Store";
import {
  setCreateElection,
  setElectionId,
  setIsElection,
  setIsPlayerInAnElection,
  logout,
} from "../common/actions";

const divStyle = { marginTop: "15%", marginBottom: "5%" };

const Menu = () => {
  const { state, dispatch } = useContext(Context);
  const {
    username,
    createElection,
    isElection,
    isPlayerInAnElection,
    keyPair,
  } = state;

  const [stompClient, setStompClient] = useState(null);
  useEffect(() => {
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
          dispatch(setElectionId(JSON.parse(frame.body)));
          dispatch(setIsPlayerInAnElection(true));
        });
      }
    );
    setStompClient(sc);

    return () => {
      if (sc !== null) {
        sc.disconnect();
        console.log("FindElection WebSocket Disconnected");
      }
    };
  }, [dispatch, username]);

  const addPlayerToElection = (username, electionId) => {
    stompClient.send("/app/election/" + electionId, {}, username);
  };

  if (!isElection) {
    return (
      <Container>
        <h1>Welcome, {username}</h1>

        {!createElection && (
          <Button
            onClick={() => {
              dispatch(setCreateElection(true));
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
                electionCreated={() => dispatch(setCreateElection(false))}
                addPlayerToElection={addPlayerToElection}
              />
            </div>
            <div style={divStyle}>
              <Button
                onClick={() => {
                  dispatch(setCreateElection(false));
                }}
                type='primary'>
                Back
              </Button>
            </div>
          </>
        )}

        {!createElection && isPlayerInAnElection && (
          <div style={divStyle}>
            <Button
              onClick={() => {
                dispatch(setIsElection(true));
              }}
              type='primary'
              block>
              Join Election
            </Button>
          </div>
        )}

        <div style={{ marginTop: "15%" }}>
          <Button onClick={() => dispatch(logout(state))} type='primary' block>
            Logout
          </Button>
        </div>
      </Container>
    );
  } else {
    return <Election />;
  }
};

export default Menu;
