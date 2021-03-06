import { useState, useEffect } from "react";
import React from "react";
import { Button } from "antd";
import { createElection } from "../common/client";
import { getAllAvailablePlayers } from "../common/client";
import TextField from "@material-ui/core/TextField";
import Autocomplete from "@material-ui/lab/Autocomplete";

const divStyle = { marginTop: "5%", marginBottom: "5%" };

const CreateElectionCustom = ({
  owner,
  electionCreated,
  addPlayerToElection,
}) => {
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [usernames, setUsernames] = useState([]);
  useEffect(() => {
    const fillUsernames = () => {
      getAllAvailablePlayers()
        .then((res) => res.json())
        .then((response) => {
          setUsernames(response.map((obj) => obj.username));
        });
    };
    fillUsernames();
  }, []);

  const onSubmit = () => {
    setLoading(true);
    const playersSet = [...new Set(players)];
    let validPlayers = true;
    for (let i = 0; i < playersSet.length; i++) {
      if (!usernames.some((username) => username === playersSet[i].value)) {
        alert("Each player must come from the dropdown menu");
        validPlayers = false;
        break;
      }
    }
    if (playersSet.length >= 2 && validPlayers) {
      const election = {
        electionSize: playersSet.length,
        owner: owner,
        yesVotes: 0,
        noVotes: 0,
      };

      createElection(election).then((res) =>
        res.json().then((response) => {
          const electionId = response["electionId"];
          if (electionId !== -1) {
            playersSet.forEach((player) => {
              addPlayerToElection(player.value, electionId);
            });
          } else {
            alert("Cannot create election");
          }
        })
      );
      electionCreated();
    } else {
      if (validPlayers) {
        alert("An election must have at least two players");
      }
    }
    setLoading(false);
  };

  return (
    <div>
      <div>
        {(players || []).length > 0 &&
          players.map((player, index) => (
            <div className='row' key={index} style={divStyle}>
              <div className='col'>
                <>
                  <Autocomplete
                    value={player.value}
                    onChange={(event, newValue) => {
                      setPlayers(
                        players.map((p) => {
                          if (p.index === index) {
                            p.value = newValue;
                          }
                          return p;
                        })
                      );
                    }}
                    id={`player-${index}`}
                    options={usernames}
                    style={{ width: 300 }}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label={`Player ${index + 1}`}
                        variant='outlined'
                      />
                    )}
                  />
                  <Button
                    type='primary'
                    onClick={() => {
                      if (players.length === 1) {
                        setPlayers([]);
                      } else {
                        setPlayers(
                          players.filter((player) => player.index !== index)
                        );
                      }
                    }}>
                    X
                  </Button>
                </>
              </div>
            </div>
          ))}
      </div>
      <div style={{ marginTop: "5%", marginBottom: "5%" }}>
        <Button
          type='primary'
          onClick={() =>
            setPlayers([...players, { index: players.length, value: null }])
          }>
          Add Player
        </Button>
      </div>
      <div style={divStyle}>
        <Button
          onClick={() => {
            onSubmit();
          }}
          type='primary'
          block
          loading={loading}>
          Create Election
        </Button>
      </div>
    </div>
  );
};

export default CreateElectionCustom;
