import React, { createContext, useReducer, useMemo, useEffect } from "react";
import { Reducer, initialState, initializer } from "./Reducer";

export const Context = createContext();

export const Store = ({ children }) => {
  const [state, dispatch] = useReducer(Reducer, initialState, initializer);

  useEffect(() => {
    localStorage.setItem("localState", JSON.stringify(Object.entries(state)));
  }, [state]);

  const contextValue = useMemo(() => {
    return { state, dispatch };
  }, [state, dispatch]);

  return <Context.Provider value={contextValue}>{children}</Context.Provider>;
};
