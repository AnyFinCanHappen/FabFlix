package edu.uci.ics.sidneyjt.service.billing.models.base.bill;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BillingMovieIdQuantityRequestModel extends BillingMovieIdRequestModel
{
    @JsonProperty(value = "quantity", required = true)
    private Integer quantity;

    @JsonCreator
    public BillingMovieIdQuantityRequestModel(@JsonProperty(value = "email", required = true) String email,
                                              @JsonProperty(value = "movie_id", required = true) String movie_id,
                                              @JsonProperty(value = "quantity", required = true) Integer quantity) {
        super(email, movie_id);
        this.quantity = quantity;
    }

    @JsonProperty(value = "quantity")
    public Integer getQuantity() {
        return quantity;
    }
}
