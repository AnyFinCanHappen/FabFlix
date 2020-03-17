package edu.uci.ics.sidneyjt.service.billing.models.base.bill;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.models.base.RequestModel;

public class BillingRequestModel extends RequestModel
{
    @JsonProperty(value = "email", required = true)
    private String email;

    @JsonCreator
    public BillingRequestModel(){}

    @JsonCreator BillingRequestModel(@JsonProperty(value = "email", required = true) String email)
    {
        this.email= email;
    }
    @JsonProperty(value = "email")
    public String getEmail() {
        return email;
    }
}
