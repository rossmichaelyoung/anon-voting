export const setUsername = (username) => ({
  type: "setUsername",
  payload: username,
});

export const setElectionId = (electionId) => ({
  type: "setElectionId",
  payload: electionId,
});

export const setIsLoggedIn = (isLoggedIn) => ({
  type: "setIsLoggedIn",
  payload: isLoggedIn,
});

export const setIsElection = (isElection) => ({
  type: "setIsElection",
  payload: isElection,
});

export const setWindowWidth = (windowWidth) => ({
  type: "setWindowWidth",
  payload: windowWidth,
});

export const setCreateAccount = (createAccount) => ({
  type: "setCreateAccount",
  payload: createAccount,
});

export const setCreateElection = (createElection) => ({
  type: "setCreateElection",
  payload: createElection,
});

export const setYes = (yes) => ({
  type: "setYes",
  payload: yes,
});

export const setNo = (no) => ({
  type: "setNo",
  payload: no,
});

export const setIsVotingActive = (isVotingActive) => ({
  type: "setIsVotingActive",
  payload: isVotingActive,
});

export const setHasVoted = (hasVoted) => ({
  type: "setHasVoted",
  payload: hasVoted,
});

export const setIsPlayerInAnElection = (isPlayerInAnElection) => ({
  type: "setIsPlayerInAnElection",
  payload: isPlayerInAnElection,
});

export const electionOver = (state) => {
  const electionOverState = Object.fromEntries(
    Object.entries(state).filter(
      ([key, value]) =>
        key !== "yes" &&
        key !== "no" &&
        key !== "isVotingActive" &&
        key !== "hasVoted" &&
        key !== "electionId" &&
        key !== "isPlayerInAnElection" &&
        key !== "isElection"
    )
  );

  return {
    type: "electionOver",
    payload: electionOverState,
  };
};

export const logout = (state) => {
  const logoutState = Object.fromEntries(
    Object.entries(state)
      .filter(([key, value]) => key === "isLoggedIn" || key === "windowWidth")
      .map(([key, value]) => {
        if (key === "isLoggedIn") {
          return [key, false];
        }
        return [key, value];
      })
  );

  return {
    type: "logout",
    payload: logoutState,
  };
};
