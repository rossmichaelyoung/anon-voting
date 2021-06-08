import React, { Component } from "react";
import { Button } from "antd";
import {
  getYesVotes,
  getNoVotes,
  vote,
  isVotingActive,
  deleteElection,
  hasVoted,
  getPlayerElectionMapping,
  getAllAvailablePlayers,
} from "./client";
import CreateElectionCustom from "./forms/CreateElectionCustom";
import Container from "./Container";

const divStyle = { marginTop: "15%", marginBottom: "5%" };

class Election extends Component {
  constructor(props) {
    super(props);

    this.state = {
      yes: 0,
      no: 0,
      electionId: "",
      election: false,
      electionOver: false,
      hasVoted: false,
      playerInElection: false,
      usernames: [],
      createElection: false,
    };
  }

  intervalYesID;
  intervalNoID;
  intervalVotingStatusID;
  intervalPlayerVotingStatusID;
  intervalPlayerElection;

  componentDidMount() {
    this.fillUsernames();
    this.getYes();
    this.intervalYesID = setInterval(this.getYes.bind(this), 500);

    this.getNo();
    this.intervalNoID = setInterval(this.getNo.bind(this), 500);

    this.updateVotingStatus();
    this.intervalVotingStatusID = setInterval(
      this.updateVotingStatus.bind(this),
      500
    );

    this.getPlayerVotingStatus();
    this.intervalPlayerVotingStatusID = setInterval(
      this.getPlayerVotingStatus.bind(this),
      500
    );

    this.getPlayerElection();
    this.intervalPlayerElection = setInterval(
      this.getPlayerElection.bind(this),
      500
    );

    this.hydrateStateWithLocalStorage();
  }

  componentWillUnmount() {
    clearInterval(this.intervalYesID);
    clearInterval(this.intervalNoID);
    clearInterval(this.intervalVotingStatusID);
    clearInterval(this.intervalPlayerVotingStatusID);
    clearInterval(this.intervalPlayerElection);

    this.saveStateToLocalStorage();
  }

  componentDidUpdate() {
    this.saveStateToLocalStorage();
    this.fillUsernames();
  }

  hydrateStateWithLocalStorage() {
    for (let key in this.state) {
      if (localStorage.hasOwnProperty(key)) {
        let value = localStorage.getItem(key);

        try {
          value = JSON.parse(value);
          this.setState({ [key]: value });
        } catch (e) {
          this.setState({ [key]: value });
        }
      }
    }
  }

  saveStateToLocalStorage() {
    for (let key in this.state) {
      localStorage.setItem(key, JSON.stringify(this.state[key]));
    }
  }

  updateState(key, value) {
    this.setState({ [key]: value });
  }

  getPlayerElection = () => {
    if (!this.state.election) {
      getPlayerElectionMapping(this.props.username)
        .then((res) => res.text())
        .then((response) => {
          if (response) {
            this.updateState("electionId", response);
            this.updateState("playerInElection", true);
          }
        });
    }
  };

  getPlayerVotingStatus = () => {
    if (this.state.election) {
      hasVoted(this.state.electionId, this.props.username)
        .then((res) => res.json())
        .then((response) => {
          this.updateState("hasVoted", response);
        });
    }
  };

  getNo = () => {
    if (this.state.election) {
      getNoVotes(this.state.electionId)
        .then((res) => res.json())
        .then((data) => {
          if (data !== -1) this.updateState("no", data);
        });
    }
  };

  getYes = () => {
    if (this.state.election) {
      getYesVotes(this.state.electionId)
        .then((res) => res.json())
        .then((data) => {
          if (data !== -1) this.updateState("yes", data);
        });
    }
  };

  back = () => {
    this.updateState("election", false);
    this.updateState("electionId", "");
    this.updateState("electionOver", false);
    this.updateState("yes", 0);
    this.updateState("no", 0);
    this.updateState("playerInElection", false);
  };

  incrementYes = () => {
    this.updateState("yes", this.state.yes + 1);
  };

  incrementNo = () => {
    this.updateState("no", this.state.no + 1);
  };

  updateVotingStatus = () => {
    if (this.state.election) {
      isVotingActive(this.state.electionId)
        .then((res) => res.json())
        .then((response) => {
          this.updateState("electionOver", !response);
        });
    }
  };

  submitVote = (electionId, playerVote) => {
    vote(electionId, playerVote);
    this.updateState("hasVoted", true);
  };

  fillUsernames = () => {
    getAllAvailablePlayers()
      .then((res) => res.json())
      .then((response) => {
        response.map((obj) => delete obj.password);
        this.updateState("usernames", response);
      });
  };

  render() {
    const {
      yes,
      no,
      electionId,
      election,
      electionOver,
      hasVoted,
      playerInElection,
      usernames,
      createElection,
    } = this.state;
    const { username, size } = this.props;

    if (!election) {
      return (
        <Container size={size}>
          <h1>Welcome, {username}</h1>

          {!createElection && (
            <Button
              onClick={() => {
                this.updateState("createElection", true);
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
                  electionCreated={() =>
                    this.updateState("createElection", false)
                  }
                />
              </div>
              <div style={divStyle}>
                <Button
                  onClick={() => {
                    this.updateState("createElection", false);
                  }}
                  type='primary'>
                  Back
                </Button>
              </div>
            </>
          )}

          {!createElection && playerInElection && (
            <div style={divStyle}>
              <Button
                onClick={() => {
                  this.updateState("election", true);
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
                this.props.logout();
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
        <Container size={size}>
          <div style={divStyle}>
            {!electionOver && <h1>Vote Wisely, {username}</h1>}

            {electionOver && this.state.yes > this.state.no && (
              <h1>Motion Passed</h1>
            )}

            {electionOver && this.state.yes <= this.state.no && (
              <h1>Motion Did Not Pass</h1>
            )}

            <div>
              <h3>Yes: {yes}</h3>
            </div>
            <div style={{ marginTop: "2%", marginBottom: "2%" }}>
              <h3>No: {no}</h3>
            </div>

            {!electionOver && !hasVoted && (
              <div>
                <Button
                  onClick={() => {
                    const playerVote = {
                      vote: "YES",
                      username: username,
                    };
                    this.submitVote(electionId, playerVote);
                    this.incrementYes();
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
                    this.submitVote(electionId, playerVote);
                    this.incrementNo();
                  }}
                  type='primary'
                  style={{ backgroundColor: "white", color: "black" }}>
                  No
                </Button>
              </div>
            )}

            {electionOver && (
              <div style={{ marginTop: "0.5%" }}>
                <Button
                  onClick={() => {
                    deleteElection(electionId);
                    this.back();
                  }}
                  type='primary'>
                  Back
                </Button>
              </div>
            )}
          </div>
        </Container>
      );
    }
  }
}

export default Election;
