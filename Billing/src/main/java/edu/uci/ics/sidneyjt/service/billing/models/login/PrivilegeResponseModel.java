package edu.uci.ics.sidneyjt.service.billing.models.login;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.sidneyjt.service.billing.models.base.ResponseModel;

public class PrivilegeResponseModel extends ResponseModel
{
    @JsonProperty(value = "resultCode", required = true)
    private Integer resultCode;

    @JsonProperty(value = "message", required = true)
    private String message;

    @JsonCreator
    public PrivilegeResponseModel(@JsonProperty(value = "resultCode", required = true) Integer resultCode,
                                  @JsonProperty(value = "message", required = true) String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public PrivilegeResponseModel(){}

    @JsonProperty(value = "resultCode")
    public Integer getResultCode() {
        return resultCode;
    }
    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }
}
