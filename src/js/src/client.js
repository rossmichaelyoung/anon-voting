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

export const addPlayerToElection = (username, electionId) =>
  fetch("api/elections/" + electionId + "/players", {
    method: "POST",
    body: username,
  });

export const getPlayerElectionMapping = (username) =>
  fetch("api/players/election", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: username,
  });

export const getYesVotes = (electionId) =>
  fetch("api/elections/" + electionId + "/vote/yes");
export const getNoVotes = (electionId) =>
  fetch("api/elections/" + electionId + "/vote/no");
export const isVotingActive = (electionId) =>
  fetch("api/elections/" + electionId + "/vote/active");

export const vote = (electionId, playerVote) =>
  fetch("api/players/vote/" + electionId, {
    headers: {
      "Content-Type": "application/json",
    },
    method: "POST",
    body: JSON.stringify(playerVote),
  });

export const electionSpotsAvailable = (username, electionId) =>
  fetch("api/elections/" + electionId + "/availablespot", {
    method: "POST",
    body: username,
  });

export const doesElectionExists = (electionId) =>
  fetch("api/elections/" + electionId);

export const deleteElection = (electionId) =>
  fetch("api/elections/" + electionId + "/delete", {
    method: "DELETE",
  });

export const hasVoted = (electionId, username) =>
  fetch("api/players/hasvoted/" + electionId, {
    method: "POST",
    body: username,
  });
