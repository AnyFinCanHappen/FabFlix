import React, { Component } from "react";
import Axios from "axios";

import NavBar from "./NavBar";
import Content from "./Content";
import Billing from "./services/Billing.js";


const localStorage = require("local-storage");

class App extends Component {
  state = {
    loggedIn: this.checkedLoggedIn(),
    cart:[],
    transactions:[]
  };

  handleLogIn = (email, session_id) => {
    const { common } = Axios.defaults.headers;


    localStorage.set("email", email);
    localStorage.set("session_id", session_id);

    common["email"] = email;
    common["session_id"] = session_id;

    this.setState({ loggedIn: true });

  };

  handleLogOut = () => {
    const { common } = Axios.defaults.headers;

    localStorage.remove("email");
    localStorage.remove("session_id");

    delete common["email"];
    delete common["session_id"];

    this.setState({ loggedIn: false });
  };

  checkedLoggedIn() {
    return (
      localStorage.get("email") !== null &&
      localStorage.get("session_id") !== null
    );
  };


  getCart = () =>{
    const payload = {email:localStorage.get("email")}
    console.log(payload);
    Billing.retrieve(localStorage.get("email"), localStorage.get("session_id"), payload)
    .then(response => {
      if(response.data.resultCode !== 3130)
      {
        alert(JSON.stringify(response.data.message, null, 4));
      }
      else{
        this.setState({cart:response.data.items});
        console.log(JSON.stringify(this.state.cart));
      }
      //console.log(JSON.stringify(response.data, null, 4));
    })
    .catch(error => console.log(error));
  };


  getOrder = () =>
  {
    const payload = {email:localStorage.get("email")}
    Billing.getOrder(localStorage.get("email"), localStorage.get("session_id"), payload)
    .then(response => {
      if(response.data.resultCode !== 3410)
      {
        alert(JSON.stringify(response.data.message, null, 4));
      }
      else{
        this.setState({transactions:response.data.transactions});
        console.log(JSON.stringify(this.state.transactions));
      }
      //console.log(JSON.stringify(response.data, null, 4));
    })
    .catch(error => console.log(error));
  }

  render() {
    const { loggedIn } = this.state;

    return (
      <div className="app">
        <NavBar handleLogOut={this.handleLogOut} loggedIn={loggedIn} getCart = {this.getCart} getOrder = {this.getOrder}/>
        <Content handleLogIn={this.handleLogIn} loggedIn={loggedIn} cart = {this.state.cart} transactions = {this.state.transactions}/>
      </div>
    );
  }
}

export default App;
