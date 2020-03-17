import React, { Component } from "react";
//import {useHistory} from 'react-router-dom';
import "../css/common.css";
import Billing from "../services/Billing.js";

const localStorage = require("local-storage");


class Cart extends Component{
    constructor(props)
    {
        super(props);
        this.state.cart = props.cart;
        console.log(props.cart);
    };
    state = {
    };

    changeQuantity = e =>
    {
        const {id,value} = e.target;
        this.setState({[id]:value});
    }

    updateCartQuantity = e =>
    {
        const movie_id = e.target.id;
        const quantity = this.state[movie_id];
        const email = localStorage.get("email");
        const session_id = localStorage.get("session_id");
        const payLoad ={
            email: email,
            quantity: quantity,
            movie_id: movie_id
        };
        
        Billing.update(email, session_id, payLoad)
        .then(response => {
            alert(JSON.stringify(response.data.message, null, 4));
            console.log(JSON.stringify(response.data, null, 4));
          })
          .catch(error => console.log(error));

    }

    
    deleteCart = e =>
    {
        //e.preventDefault();
        const email = localStorage.get("email");
        const movie_id = e.target.id;
        const session_id = localStorage.get("session_id");
        const payLoad = {
            email: email,
            movie_id : movie_id
        }
        Billing.deleteCart(email, session_id, payLoad)
        .then(response => {
            alert(JSON.stringify(response.data.message, null, 4));
            console.log(JSON.stringify(response.data, null, 4));
          })
          .catch(error => console.log(error));
    }

    placeOrder =  e =>
    {
        const email = localStorage.get("email");
        const session_id = localStorage.get("session_id");
        const payLoad = {
            email: email
        }
        Billing.placeOrder(email, session_id, payLoad)
        .then(response => {
            alert(JSON.stringify(response.data.message, null, 4));
            console.log(JSON.stringify(response.data, null, 4));
            if(response.data.resultCode === 3400){
                const aprrove_url = response.data.approve_url;
                const token = response.data.token;
                console.log("Paypal URL: " + aprrove_url);
                window.open(aprrove_url);
                const payload = {
                    token: token
                }
                    Billing.completeOrder(payload)
                    .then(response => {
                        console.log(JSON.stringify(response.data, null, 4));
                        if(response.data.resultCode === 3420)
                        {
                            alert(JSON.stringify(response.data.message, null, 4));
                            return;
                        }
                    })
                    .catch(error => console.log(error));
            }
          })
          .catch(error => console.log(error));
    }

    render(){
        const cart = this.state.cart;
        return(
            <div>
            <br></br>
            Items In CART:
            <br></br><br></br>
            {cart !== null && cart !== undefined &&
                <div >
                    <button onClick = {this.placeOrder}>Buy</button>
                    {cart.map((item, i) =>
                    {
                        const totalPrice = item.quantity * item.unit_price;
                        return(
                            <div key = {i}>
                            Title: {item.movie_title}
                            <br></br>
                            Price: {item.unit_price}
                            <br></br>
                            Quantity: {item.quantity}
                            <br></br>
                            Total: {totalPrice}
                            <br></br>
                            Change Quantity <br></br>
                            <form id = {item.movie_id} >
                                <select id = {item.movie_id} onChange = {this.changeQuantity}>
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
                                <button id = {item.movie_id} onClick = {this.updateCartQuantity}> 
                                Change Quantity </button>
                                <br></br>
                                <button id = {item.movie_id} onClick = {this.deleteCart}> Delete </button>
                            </form>
                            <br></br><br></br>
                            </div>
                        );
                    })
                    }
                </div>
            }
            
            </div>
        );
    }
}

/*
                    {cart.map((item, i) =>
                    {
                        return(
                            <div key = {i}>{item} </div>
                        );
                    })
                    }
*/
export default Cart;
