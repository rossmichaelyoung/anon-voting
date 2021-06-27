import React, { useContext } from "react";
import Landing from "./components/Landing";
import Menu from "./components/Menu";
import { Context } from "./common/Store";

const App = () => {
  const { state } = useContext(Context);
  const { isLoggedIn } = state;

  if (isLoggedIn) {
    return <Menu />;
  } else {
    return <Landing />;
  }
};

export default App;
