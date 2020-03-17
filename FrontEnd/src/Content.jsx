import React, { Component } from "react";
import { Route, Switch } from "react-router-dom";

import Login from "./pages/Login";
import Home from "./pages/Home";
import Register from "./pages/Register";


//Route path="/" component={props => <Home loggedIn = {loggedIn} {...props}/>} />
//<Route path="/" component = {Home} loggedIn = {loggedIn}/>

class Content extends Component {
  render() {
    const { handleLogIn, loggedIn,cart, transactions } = this.props;

    return (
      <div className="content">
        <Switch>
          <Route
            path="/login"
            component={props => <Login handleLogIn={handleLogIn} {...props} />}
          />
          <Route path = "/register" component = {Register} />
          <Route path="/" component={props => <Home loggedIn = {loggedIn} cart = {cart} transactions = {transactions} {...props}/>} />
        </Switch>
      </div>
    );
  }
}

export default Content;
