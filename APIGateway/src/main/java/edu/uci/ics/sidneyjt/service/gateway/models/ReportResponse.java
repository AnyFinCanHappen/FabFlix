package edu.uci.ics.sidneyjt.service.gateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponse extends ResponseModel
{

    @JsonProperty(value = "JSON")
    private String JSON;

    @JsonCreator
    public ReportResponse(){}

    public ReportResponse(String JSON) {
        this.JSON = JSON;
    }

    @JsonProperty(value = "JSON")
    public void setJSON(String JSON) {
        this.JSON = JSON;
    }
}
