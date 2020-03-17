import React, { Component, Fragment } from "react";
import { NavLink } from "react-router-dom";

import "./css/style.css";
import "./services/Billing.js"

//const localStorage = require("local-storage");


class NavBar extends Component {
  state ={
  };

//<NavLink className="nav-link" onClick = {this.getCart} to={{pathname : "/Cart", cartInfo:{ cart: this.state.cart } } } >

  render() {
    const { handleLogOut, loggedIn, getCart, getOrder } = this.props;

    return (
      <nav className="nav-bar">
        <NavLink className="nav-link" to="/">
          Home
        </NavLink>
        {!loggedIn && (
          <Fragment>
            <NavLink className="nav-link" to="/login">
              Login/Register
            </NavLink>
          </Fragment>
        )}
        {loggedIn && (
          <Fragment> 
            <NavLink className="nav-link" to="/Search">
              Search
            </NavLink>
            <NavLink className="nav-link" onClick = {getCart} to= "/Cart" >
              Cart
            </NavLink>
            <NavLink className="nav-link" to="/Orders" onClick = {getOrder}>
              Order History
            </NavLink>
            <button onClick={handleLogOut} className="nav-button">
              Log Out
            </button>
          </Fragment>
        )}
      </nav>
    );
  }
}

export default NavBar;
