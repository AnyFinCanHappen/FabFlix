import React, { Component } from "react";
import { Route } from "react-router-dom";


import Search from "./Search";
import MovieDetail from "./MovieDetail";
import Cart from "./Cart";
import Order from "./Order";
// <Route path = "/MovieDetail" component = {MovieDetail}/>

class Home extends Component {
  state = {
    movieDetail:null
  };

  updateMovieDetail = (movieDetail) =>{
    console.log(movieDetail);
    this.setState({movieDetail: movieDetail})
  }
  render() {
    const {loggedIn, cart, transactions} = this.props;
    const {movieDetail} = this.state;
    return (
      <div>
        {loggedIn && 
          <div>
          Home
          <Route path = "/Search" component = {props => <Search updateMovieDetail = {this.updateMovieDetail} {...props} /> }/>
          <Route 
          path = "/MovieDetail" 
          component={props => <MovieDetail movieDetail = {movieDetail} {...props} />}/>
          <Route path = "/Cart" component ={props => <Cart cart = {cart} {...props} />}/>
          <Route path = "/Orders" component ={props => <Order transactions = {transactions} {...props} />}/>
          </div>
        }
        {
          !loggedIn && (<p>Log In</p>)
        }
      </div>
    );
  }
}

//<Search component = {Search} updateMovieDetail = {this.updateMovieDetail}/>
export default Home;
