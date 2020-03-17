package edu.uci.ics.sidneyjt.service.billing.models.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.models.base.ResponseModel;
import edu.uci.ics.sidneyjt.service.billing.models.base.Result;

public class PlaceResponseModel extends ResponseModel
{
    @JsonProperty(value = "approve_url")
    private String approve_url;

    @JsonProperty(value = "token")
    private String token;

    @JsonCreator
    public PlaceResponseModel(@JsonProperty(value = "approve_url") String approve_url,
                              @JsonProperty(value = "token") String token) {
        this.approve_url = approve_url;
        this.token = token;
    }

    public PlaceResponseModel(Result result, String approve_url, String token) {
        super(result);
        this.approve_url = approve_url;
        this.token = token;
    }

    @JsonCreator public PlaceResponseModel(){}

    @JsonProperty(value = "approve_url")
    public String getApprove_url() {
        return approve_url;
    }
    @JsonProperty(value = "token")
    public String getToken() {
        return token;
    }

    @JsonIgnore
    public void setApprove_url(String approve_url) {
        this.approve_url = approve_url;
    }
    @JsonIgnore
    public void setToken(String token) {
        this.token = token;
    }
}
