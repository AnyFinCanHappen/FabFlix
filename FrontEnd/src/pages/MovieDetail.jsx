import React, { Component } from "react";
import "../css/common.css";
import "../css/image.css";
import Billing from "../services/Billing.js";
const localStorage = require("local-storage");

class MovieDetail extends Component 
{
    constructor(props)
    {
        super(props);
        this.state.movieDetail = props.movieDetail;
    }
    state = {
        quantity:1
    };

    handleQuantity = e =>{
        const {value} = e.target;
        this.setState({quantity:value});
    }

    addToCart = e => {
        const movie_id = this.state.movieDetail.movie_id;
        const {quantity}= this.state;
        const email = localStorage.get("email");
        const payload = {
            email: email,
            movie_id: movie_id,
            quantity: quantity
        }
        Billing.insert(email,localStorage.get("session_id"), payload)
        .then(response => {
            alert(JSON.stringify(response.data.message, null, 4));
            console.log(JSON.stringify(response.data, null, 4));
          })
          .catch(error => console.log(error));
    }



    render() {
        const movieDetail = this.state.movieDetail;
        return (
        <div>
            {movieDetail !== null &&
            <div id = "details">
                <div className = "right">
                    Rating
                    <br></br>
                    {movieDetail.rating}
                    <br></br><br></br>
                    Number of Votes
                    <br></br>
                    {movieDetail.num_votes}
                    <br></br><br></br>
                    Budget
                    <br></br>
                    ${movieDetail.budget}
                    <br></br><br></br>
                    Revenue
                    <br></br>
                    ${movieDetail.revenue}
                    <br></br><br></br>
                </div>
                Title:
                <br></br>
                {movieDetail.title}
                <br></br><br></br>
                Year:
                <br></br>
                {movieDetail.year}
                <br></br><br></br>
                Director:
                <br></br>
                {movieDetail.director}
                <br></br><br></br>
                Quantity <br></br>
                <select value = {this.state.quantity} onChange = {this.handleQuantity} >
                    <option value = "1">1</option>
                    <option value = "2">2</option>
                    <option value = "3">3</option>
                    <option value = "4">4</option>
                    <option value = "5">5</option>
                    <option value = "6">6</option>
                    <option value = "7">7</option>
                    <option value = "8">8</option>
                    <option value = "9">9</option>
                    <option value = "10">10</option>
                    <option value = "15">15</option>
                    <option value = "20">20</option>
                </select>
                <br></br>
                <button className = "buttonWhite" onClick = {this.addToCart}>Add To Cart</button>
                <br></br><br></br><br></br><br></br><br></br><br></br>
                Overview:
                <br></br>
                {movieDetail.overview}
                <br></br><br></br><br></br>
                <div className = "right">
                Genre:
                {movieDetail.genres.map((genre, i) => 
                    {
                    return(
                        <li key = {i}>
                            {genre.name}
                        </li>
                    );
                    })
                } 
                </div>
                People:
                <div>
                {movieDetail.people.map((people, i) => 
                    {
                    return(
                        <li key = {i}>
                            {people.name}
                        </li>
                    );
                    })
                }  
                </div>
        
            </div>
            }
        </div>
        );
    }
}
export default MovieDetail;