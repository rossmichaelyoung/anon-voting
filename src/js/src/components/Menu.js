import { useState, useEffect } from "react";
import React from "react";
import { Button } from "antd";
import Container from "./Container";
import CreateElectionCustom from "../forms/CreateElectionCustom";
import { getPlayerElectionMapping, getAllAvailablePlayers } from "../client";
import Election from "./Election";

const divStyle = { marginTop: "15%", marginBottom: "5%" };

const Menu = (props) => {
  const { username, size, logout } = props;
  const [createElection, setCreateElection] = useState(false);
  const [electionId, setElectionId] = useState(-1);
  const [isElection, setIsElection] = useState(false);

  const [usernames, setUsernames] = useState([]);
  useEffect(() => {
    const fillUsernames = () => {
      getAllAvailablePlayers()
        .then((res) => res.json())
        .then((response) => {
          response.map((obj) => delete obj.password);
          setUsernames(response);
        });
    };
    fillUsernames();
  }, [createElection]);

  const [isPlayerInAnElection, setIsPlayerInAnElection] = useState(false);
  useEffect(() => {
    const isPlayerInAnElection = () => {
      getPlayerElectionMapping(username)
        .then((res) => res.text())
        .then((response) => {
          const id = Number(response);
          if (id !== -1) {
            setElectionId(id);
            setIsPlayerInAnElection(true);
          }
        });
    };
    const intervalId = setInterval(() => isPlayerInAnElection(), 2000);
    return () => {
      clearInterval(intervalId);
    };
  }, [username, setElectionId, setIsPlayerInAnElection]);

  const exitElection = () => {
    setElectionId(-1);
    setIsPlayerInAnElection(false);
    setIsElection(false);
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
                usernames={usernames}
                electionCreated={() => setCreateElection(false)}
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
          <Button
            onClick={() => {
              logout();
            }}
            type='primary'
            block>
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
        exitElection={() => exitElection()}
      />
    );
  }
};

export default Menu;
