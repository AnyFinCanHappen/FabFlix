import React, { Component } from "react";
import {Link} from 'react-router-dom';
import Idm from "../services/Idm";

import "../css/common.css";

class Login extends Component {
  state = {
    email: "",
    password: "",
    message: null
  };

  handleSubmit = e => {
    e.preventDefault();
    const loggedInCode = 120;
    const { handleLogIn } = this.props;
    const { email, password } = this.state;

    Idm.login(email, password)
      .then(response => {
        console.log(response);
        if(response.data.resultCode === loggedInCode){
          handleLogIn(email, response["data"]["session_id"]);
          this.props.history.push("/");
        }
        else{
          this.setState ({message:response.data.message})
        }
      })
      .catch(error => console.log(error));
  };

  updateField = ({ target }) => {
    const { name, value } = target;

    this.setState({ [name]: value });
  };

  render() {
    const { email, password } = this.state;

    return (
      <div>
        <h1>Login</h1>
        <form onSubmit={this.handleSubmit}>
          <label className="label">Email</label>
          <input
            className="input"
            type="email"
            name="email"
            value={email}
            onChange={this.updateField}
          ></input>
          <label className="label">Password</label>
          <input
            className="input"
            type="password"
            name="password"
            value={password}
            onChange={this.updateField}
          ></input>
          {this.state.message !== null && (
          <p>
             {this.state.message}
           </p>
          )}
          <button className="button">Login</button>
          <br></br>
          <ul>
            <Link to = "/register"> Sign Up</Link>
          </ul>
        </form>
      </div>
    );
  }
}

export default Login;
