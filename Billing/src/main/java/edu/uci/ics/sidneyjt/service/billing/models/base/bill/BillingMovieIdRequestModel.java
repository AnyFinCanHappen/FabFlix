package edu.uci.ics.sidneyjt.service.billing.models.base.bill;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BillingMovieIdRequestModel extends BillingRequestModel
{
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;

    @JsonCreator
    public BillingMovieIdRequestModel(){}

    @JsonCreator
    public BillingMovieIdRequestModel(@JsonProperty(value = "email", required = true) String email,
                                      @JsonProperty(value = "movie_id", required = true) String movie_id) {
        super(email);
        this.movie_id = movie_id;
    }

    @JsonProperty(value = "movie_id")
    public String getMovie_id() {
        return movie_id;
    }
}
