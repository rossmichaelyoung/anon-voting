import React from "react";
import CreateAccount from "../forms/CreateAccount";
import Login from "../forms/Login";
import { Button } from "antd";
import "antd/dist/antd.css";
import "../css/antd.css";
import Container from "./Container";
import { useContext } from "react";
import { Context } from "../common/Store";
import {
  setUsername,
  setCreateAccount,
  setIsLoggedIn,
} from "../common/actions";
import { loginPlayer } from "../common/client";

const Landing = () => {
  const { state, dispatch } = useContext(Context);
  const { createAccount } = state;

  const createAccountFalse = () => {
    dispatch(setCreateAccount(false));
  };

  const createAccountTrue = () => {
    dispatch(setCreateAccount(true));
  };

  const afterLogin = (username) => {
    dispatch(setUsername(username));
    dispatch(setIsLoggedIn(true));
  };

  const login = ({ username, password }) => {
    return loginPlayer({
      username: username,
      password: password,
    });
  };

  if (createAccount) {
    return (
      <Container>
        <h1>Create Account</h1>
        <CreateAccount onCreate={createAccountFalse} />

        <Button onClick={createAccountFalse} type='primary' block>
          Back To Login
        </Button>
      </Container>
    );
  } else {
    return (
      <Container>
        <h1>Anon-Voting</h1>
        <Login login={login} afterLogin={afterLogin} />

        <Button onClick={createAccountTrue} type='primary' block>
          Create Account
        </Button>
      </Container>
    );
  }
};

export default Landing;
