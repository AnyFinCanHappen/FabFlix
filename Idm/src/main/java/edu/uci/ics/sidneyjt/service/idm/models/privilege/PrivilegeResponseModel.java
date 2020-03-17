package edu.uci.ics.sidneyjt.service.idm.models.privilege;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PrivilegeResponseModel
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

    @JsonProperty(value = "resultCode")
    public Integer getResultCode() {
        return resultCode;
    }
    @JsonProperty(value = "message")
    public String getMessage() {
        return message;
    }
}
