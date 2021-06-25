import React, { Component } from "react";
import Login from "./forms/Login";
import CreateAccount from "./forms/CreateAccount";
import Menu from "./components/Menu";
import { Button } from "antd";
import "antd/dist/antd.css";
import "./antd.css";
import Container from "./components/Container";
import { useState, useEffect } from "react";

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isLoggedIn: false,
      createAccount: false,
      username: "",
      width: window.innerWidth,
    };
  }

  componentDidMount() {
    this.hydrateStateWithLocalStorage();
    this.handleWindowSizeChange();
  }

  componentWillUnmount() {
    this.saveStateToLocalStorage();
  }

  componentDidUpdate() {
    this.saveStateToLocalStorage();
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

  handleWindowSizeChange = () => {
    this.updateState("width", window.innerWidth);
  };

  createAccountFalse = () => {
    this.updateState("createAccount", false);
  };

  createAccountTrue = () => {
    this.updateState("createAccount", true);
  };

  login = (username) => {
    this.updateState("username", username);
    this.updateState("isLoggedIn", true);
  };

  logout = () => {
    this.updateState("username", "");
    this.updateState("isLoggedIn", false);
  };

  useStickyState = (defaultValue, key) => {
    const [value, setValue] = useState(() => {
      const stickyValue = window.localStorage.getItem(key);
      return stickyValue !== null ? JSON.parse(stickyValue) : defaultValue;
    });

    useEffect(() => {
      window.localStorage.setItem(key, JSON.stringify(value));
    }, [key, value]);
    return [value, setValue];
  };

  render() {
    const { isLoggedIn, createAccount, width } = this.state;

    if (isLoggedIn) {
      const { username, width } = this.state;
      // return <Election username={username} size={width} logout={this.logout} />;
      return (
        <Menu
          username={username}
          size={width}
          logout={this.logout}
          useStickyState={this.useStickyState}
        />
      );
    } else {
      if (createAccount) {
        return (
          <Container size={width}>
            <h1>Create Account</h1>
            <CreateAccount
              onCreate={() => {
                this.createAccountFalse();
              }}
            />

            <Button
              onClick={() => {
                this.createAccountFalse();
              }}
              type='primary'
              block>
              Back To Login
            </Button>
          </Container>
        );
      } else {
        return (
          <Container size={width}>
            <h1>Anon-Voting</h1>
            <Login
              onLogIn={(user) => {
                this.login(user);
              }}
            />

            <Button
              onClick={() => {
                this.createAccountTrue();
              }}
              type='primary'
              block>
              Create Account
            </Button>
          </Container>
        );
      }
    }
  }
}

export default App;
