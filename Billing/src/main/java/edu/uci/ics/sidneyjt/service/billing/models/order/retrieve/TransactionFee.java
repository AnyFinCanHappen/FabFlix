package edu.uci.ics.sidneyjt.service.billing.models.order.retrieve;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionFee
{
    @JsonProperty(value = "value", required = true)
    private String value;

    @JsonProperty(value = "currency", required = true)
    private String currency;

    public TransactionFee(@JsonProperty(value = "value", required = true) String value,
                          @JsonProperty(value = "currency", required = true) String currency) {
        this.value = value;
        this.currency = currency;
    }
    @JsonProperty(value = "value")
    public String getValue() {
        return value;
    }
    @JsonProperty(value = "currency")
    public String getCurrency() {
        return currency;
    }
}
