import React, { Component } from "react";


class Order extends Component{
    constructor(props)
    {
        super(props);
        this.state.transactions = props.transactions;
        console.log(props.transactions);
    };
    state ={}

    render() {
        const transactions = this.state.transactions;
        return(
            <div>Order History
                <br></br><br></br>
                {transactions !== null && transactions !== undefined &&
                    <div>
                        <ol type = "1">
                        {transactions.map((transaction, i) =>
                            {
                                const amount = parseFloat(transaction.amount.total) + parseFloat(transaction.transaction_fee.value);
                                var date ="";
                                
                                transaction.items.map((item) =>
                                    {
                                        date = item.sale_date;
                                        return(<p></p>);
                                    }
                                )
                                return(
                                    <li key = {i}>Amount: ${amount} <br></br> Date: {date}</li>
                                );
                            }
                        )}
                        </ol>
                    </div>
                }
            </div>
        );
    }


}

export default Order;