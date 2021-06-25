import fetch from "unfetch";

export const getAllAvailablePlayers = () => fetch("api/players/available");

export const addPlayer = (player) =>
  fetch("api/players", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(player),
  });

export const loginPlayer = (player) =>
  fetch("api/players/login", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(player),
  });

export const createElection = (election) =>
  fetch("api/elections", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(election),
  });

export const deleteElection = (electionId) =>
  fetch("api/elections/" + electionId + "/delete", {
    method: "DELETE",
  });
