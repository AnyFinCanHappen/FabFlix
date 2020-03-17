package edu.uci.ics.sidneyjt.service.billing.models.order.retrieve;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Amount
{
    @JsonProperty(value = "total", required = true)
    private String total;

    @JsonProperty(value = "currency", required = true)
    private String currency;

    @JsonCreator
    public Amount(@JsonProperty(value = "total", required = true) String total,
                  @JsonProperty(value = "currency", required = true) String currency) {
        this.total = total;
        this.currency = currency;
    }
    @JsonProperty(value = "total")
    public String getTotal() {
        return total;
    }
    @JsonProperty(value = "currency")
    public String getCurrency() {
        return currency;
    }
}
