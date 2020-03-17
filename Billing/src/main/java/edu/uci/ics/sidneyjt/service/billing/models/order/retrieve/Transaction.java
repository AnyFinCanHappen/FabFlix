package edu.uci.ics.sidneyjt.service.billing.models.order.retrieve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.models.retrieve.Item;

public class Transaction
{
    @JsonProperty(value = "capture_id", required = true)
    private String capture_id;

    @JsonProperty(value = "state", required = true)
    private String state;

    @JsonProperty(value = "amount", required = true)
    private Amount amount;

    @JsonProperty(value = "transaction_fee", required = true)
    private TransactionFee transaction_fee;

    @JsonProperty(value = "create_time", required = true)
    private String create_time;

    @JsonProperty(value = "update_time", required = true)
    private String update_time;

    @JsonProperty(value = "items")
    private OrderItemModel[] items;

    @JsonCreator
    public Transaction(@JsonProperty(value = "capture_id", required = true) String capture_id,
                       @JsonProperty(value = "state", required = true) String state,
                       @JsonProperty(value = "amount", required = true) Amount amount,
                       @JsonProperty(value = "transaction_fee", required = true) TransactionFee transaction_fee,
                       @JsonProperty(value = "create_time", required = true) String create_time,
                       @JsonProperty(value = "update_time", required = true) String update_time,
                       @JsonProperty(value = "items") OrderItemModel[] items) {
        this.capture_id = capture_id;
        this.state = state;
        this.amount = amount;
        this.transaction_fee = transaction_fee;
        this.create_time = create_time;
        this.update_time = update_time;
        this.items = items;
    }
    public Transaction(){}
    @JsonProperty(value = "capture_id")
    public String getCapture_id() {
        return capture_id;
    }
    @JsonProperty(value = "state")
    public String getState() {
        return state;
    }
    @JsonProperty(value = "amount")
    public Amount getAmount() {
        return amount;
    }
    @JsonProperty(value = "transaction_fee")
    public TransactionFee getTransaction_fee() {
        return transaction_fee;
    }
    @JsonProperty(value = "create_time")
    public String getCreate_time() {
        return create_time;
    }
    @JsonProperty(value = "update_time")
    public String getUpdate_time() {
        return update_time;
    }
    @JsonProperty(value = "items")
    public OrderItemModel[] getItems() {
        return items;
    }
    @JsonIgnore
    public void setCapture_id(String capture_id) {
        this.capture_id = capture_id;
    }
    @JsonIgnore
    public void setState(String state) {
        this.state = state;
    }
    @JsonIgnore
    public void setAmount(Amount amount) {
        this.amount = amount;
    }
    @JsonIgnore
    public void setTransaction_fee(TransactionFee transaction_fee) {
        this.transaction_fee = transaction_fee;
    }
    @JsonIgnore
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
    @JsonIgnore
    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
    @JsonIgnore
    public void setItems(OrderItemModel[] items) {
        this.items = items;
    }
}
