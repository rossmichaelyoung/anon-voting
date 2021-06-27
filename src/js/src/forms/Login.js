import React from "react";
import { Formik } from "formik";
import { Input, Button, Tag } from "antd";

const tagStyle = { backgroundColor: "black", color: "white" };
const divStyle = { marginTop: "4%", marginBottom: "4%" };
let loading = false;

const Login = ({ login, afterLogin }) => (
  <Formik
    initialValues={{ username: "", password: "" }}
    validate={(values) => {
      const errors = {};
      if (!values.username) {
        errors.username = "Username Required";
      }

      if (values.password.length < 8) {
        errors.password = "Password Required";
      }

      return errors;
    }}
    onSubmit={(player, { setSubmitting }) => {
      loading = true;
      login(player).then((res) => {
        if (res.status === 200) {
          afterLogin(player.username);
        } else {
          alert("Invalid Login Credentials");
          loading = false;
        }
      });
      setSubmitting(false);
    }}>
    {({
      values,
      errors,
      touched,
      handleChange,
      handleBlur,
      handleSubmit,
      isSubmitting,
      submitForm,
      isValid,
      /* and other goodies */
    }) => (
      <form onSubmit={handleSubmit}>
        <div style={divStyle}>
          <Input
            type='username'
            name='username'
            onChange={handleChange}
            onBlur={handleBlur}
            value={values.username}
            placeholder='Username'
          />
        </div>
        {errors.username && touched.username && (
          <Tag style={tagStyle}>{errors.username}</Tag>
        )}
        <div style={divStyle}>
          <Input
            type='password'
            name='password'
            onChange={handleChange}
            onBlur={handleBlur}
            value={values.password}
            placeholder='Password'
          />
        </div>
        {errors.password && touched.password && (
          <Tag style={tagStyle}>{errors.password}</Tag>
        )}
        <div style={divStyle}>
          <Button
            onClick={submitForm}
            type='primary'
            block
            disabled={isSubmitting || (touched && !isValid)}
            loading={loading}>
            Login
          </Button>
        </div>
      </form>
    )}
  </Formik>
);

export default Login;
