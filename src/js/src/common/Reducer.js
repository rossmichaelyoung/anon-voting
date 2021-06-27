export const initialState = {
  isLoggedIn: false,
};

export const initializer = (initialValue) => {
  if (localStorage.getItem("localState") !== null) {
    return Object.fromEntries(JSON.parse(localStorage.getItem("localState")));
  } else {
    return initialValue;
  }
};

export const Reducer = (state, action) => {
  switch (action.type) {
    case "setUsername":
      return { ...state, username: action.payload };
    case "setElectionId":
      return { ...state, electionId: action.payload };
    case "setIsLoggedIn":
      return { ...state, isLoggedIn: action.payload };
    case "setIsElection":
      return { ...state, isElection: action.payload };
    case "setWindowWidth":
      return { ...state, windowWidth: action.payload };
    case "setCreateAccount":
      return { ...state, createAccount: action.payload };
    case "setCreateElection":
      return { ...state, createElection: action.payload };
    case "setYes":
      return { ...state, yes: action.payload };
    case "setNo":
      return { ...state, no: action.payload };
    case "setIsVotingActive":
      return { ...state, isVotingActive: action.payload };
    case "setHasVoted":
      return { ...state, hasVoted: action.payload };
    case "setIsPlayerInAnElection":
      return { ...state, isPlayerInAnElection: action.payload };
    case "electionOver":
      return action.payload;
    case "logout":
      return action.payload;
    default:
      return state;
  }
};
