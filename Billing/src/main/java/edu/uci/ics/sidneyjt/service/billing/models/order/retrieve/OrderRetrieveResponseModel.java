package edu.uci.ics.sidneyjt.service.billing.models.order.retrieve;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;

public class OrderRetrieveResponseModel extends ResponseModel
{
    @JsonProperty(value = "transactions")
    private Transaction[] transactions;

    public OrderRetrieveResponseModel(){}

    @JsonProperty(value = "transactions")
    public Transaction[] getTransactions() {
        return transactions;
    }
    @JsonProperty(value = "transactions")
    public void setTransactions(Transaction[] transactions)
    {
        if(transactions == null || transactions.length == 0)
            this.setResult(Result.ORDER_DOES_NOT_EXIST);
        else{
            this.setResult(Result.ORDER_RETRIEVED);
        }
        this.transactions = transactions;
    }
}
