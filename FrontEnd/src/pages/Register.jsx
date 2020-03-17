import React, { Component } from "react";
import {Link, Redirect} from 'react-router-dom'

import "../css/common.css";
import Idm from "../services/Idm";


class Register extends Component {
  state = {
    email:"",
    password:"",
    isRegistered : false
  };

  handleSubmit = e =>
  {
    e.preventDefault();
    const {email,password} = this.state;
    Idm.register(email,password)
      .then(response => {
        console.log(JSON.stringify(response.data, null, 4));
        const resultCode = response.data.resultCode;
        if(resultCode === 110)
        {
          this.setState({isRegistered: true})
        } 
      })
      .catch(error => console.log(error));
      console.log(this.state.isRegistered);
  }

  updateField = e =>
  {
    const{name,value} = e.target;
    this.setState({[name]:value});
  }

  render() 
  {
    if(this.state.isRegistered === true){
      return <Redirect to = "/login" />
    }
    const {email,password} = this.state;
    return (
      <div>
        <h1>Register</h1>
        <form onSubmit = {this.handleSubmit}>
          <label className = "label">Email</label>
          <input
            className = "input"
            type = "email"
            name = "email"
            value = {email}
            onChange = {this.updateField}
          />
          <label className = "label">Password</label>
          <input
            className = "input"
            type = "password"
            name = "password"
            value = {password}
            onChange = {this.updateField}
          />
          <button className = "button"> Register </button>
          <br></br>
          <ul><Link to = "/login"> Login Page</Link></ul>
        </form>
      </div>
    );
  }
}

export default Register;
