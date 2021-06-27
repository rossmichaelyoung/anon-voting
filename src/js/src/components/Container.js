import React from "react";
import { useContext, useEffect } from "react";
import { Context } from "../common/Store";
import { setWindowWidth } from "../common/actions";

const Container = ({ children }) => {
  const { state, dispatch } = useContext(Context);
  const { windowWidth } = state;

  useEffect(() => {
    dispatch(setWindowWidth(window.innerWidth));
  }, [dispatch]);

  if (windowWidth <= 500) {
    return (
      <div
        style={{
          width: "80%",
          margin: "0 auto",
          textAlign: "center",
          marginTop: "2%",
        }}>
        {children}
      </div>
    );
  } else if (windowWidth <= 1000) {
    return (
      <div
        style={{
          width: "50%",
          margin: "0 auto",
          textAlign: "center",
          marginTop: "2%",
        }}>
        {children}
      </div>
    );
  }

  return (
    <div
      style={{
        width: "20%",
        margin: "0 auto",
        textAlign: "center",
        marginTop: "2%",
      }}>
      {children}
    </div>
  );
};

export default Container;
